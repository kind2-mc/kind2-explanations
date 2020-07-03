/*
 * Copyright (C) 2019  The University of Iowa
 * @author Mudathir Mohamed
 */


package edu.uiowa.kind2;

import java.util.*;
import java.util.stream.Collectors;

public class Kind2NodeResult
{
    private final String name;

    private List<Kind2Analysis> analyses = new ArrayList<>();

    private Set<Kind2NodeResult> children = new HashSet<>();

    private List<Kind2NodeResult> parents = new ArrayList<>();

    private boolean isAnalyzed = false;

    private boolean isVisited = true;

    private List<Kind2Suggestion> suggestions = new ArrayList<>();

    private final Kind2Result kind2Result;

    public Kind2NodeResult(Kind2Result kind2Result, String name)
    {
        this.kind2Result = kind2Result;
        this.name = name;
    }

    public void addAnalysis(Kind2Analysis analysis)
    {
        getAnalyses().add(analysis);
        analysis.setNodeResult(this);
    }

    public List<Kind2Suggestion> getSuggestions()
    {
        return suggestions;
    }

    public String getName()
    {
        return name;
    }

    @Override
    public String toString()
    {
        StringBuilder stringBuilder = new StringBuilder();

        for (Kind2NodeResult child : children)
        {
            stringBuilder.append(child.toString() + "\n");
        }

        stringBuilder.append("Component: " + name + "\n");

        //ToDo: review refactoring toString with print()
        for (Kind2Suggestion suggestion : suggestions)
        {
            stringBuilder.append(suggestion.toString());
        }

        return stringBuilder.toString();
    }

    public String printVerificationSummary()
    {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("\nValid properties:\n");
        Set<Kind2Property> validProperties = this.getValidProperties();
        printProperties(stringBuilder, validProperties);

        stringBuilder.append("\nFalsified properties:\n");
        Set<Kind2Property> falsifiedProperties = this.getFalsifiedProperties();
        printProperties(stringBuilder, falsifiedProperties);

        stringBuilder.append("\nUnknown properties:\n");
        Set<Kind2Property> unknownProperties = this.getUnknownProperties();
        printProperties(stringBuilder, unknownProperties);

        return stringBuilder.toString();
    }

    private void printProperties(StringBuilder stringBuilder, Set<Kind2Property> validProperties)
    {
        if(validProperties.isEmpty())
        {
            stringBuilder.append("None.\n");
        }
        else
        {
            for (Kind2Property property: validProperties)
            {

                stringBuilder.append(property.getSource() +": ");
                stringBuilder.append(property.getQualifiedName());

                if (Kind2Result.isPrintingLineNumbersEnabled())
                {
                    stringBuilder.append(" in line " + property.getLine() + " ");
                    stringBuilder.append("column " + property.getColumn() + ".");
                }
                stringBuilder.append("\n");
            }
        }
    }

    public List<Kind2Analysis> getAnalyses()
    {
        return analyses;
    }

    public Set<Kind2NodeResult> getChildren()
    {
        return children;
    }

    public Kind2Analysis getLastAnalysis()
    {
        return analyses.get(analyses.size() - 1);
    }

    public void addChild(Kind2NodeResult child)
    {
        children.add(child);
        // add this as another parent to the nodeResult
        child.parents.add(this);
    }

    public List<Kind2NodeResult> getParents()
    {
        return parents;
    }

    public boolean isAnalyzed()
    {
        return isAnalyzed;
    }

    public void analyze()
    {
        // analyze children first
        for (Kind2NodeResult child : children)
        {
            if (!child.isAnalyzed())
            {
                child.analyze();
            }
        }

        Kind2Analysis lastAnalysis = getLastAnalysis();
        List<Kind2Property> unknownProperties = lastAnalysis.getUnknownProperties();
        List<Kind2Property> falsifiedProperties = lastAnalysis.getFalsifiedProperties();
        List<Kind2Property> falsifiedAssumptions = falsifiedProperties
                .stream().filter(p -> p.getSource() == Kind2PropertyType.assumption)
                .collect(Collectors.toList());

        if (falsifiedProperties.isEmpty())
        {
            if (analyses.size() == 1)
            {
                if (unknownProperties.isEmpty())
                {
                    // Suggestion 1: No action required.
                    suggestions.add(Kind2Suggestion.noActionRequired(this));
                }
            }
            else
            {
                // mode analysis is not included in the last analysis
                Optional<Kind2Analysis> modeAnalysis = analyses
                        .stream().filter(a -> a.isModeAnalysis()).findFirst();

                if (modeAnalysis.isPresent())
                {
                    if (!modeAnalysis.get().getFalsifiedProperties().isEmpty())
                    {
                        // suggestion 7: Fix one mode active
                        suggestions.add(Kind2Suggestion.fixOneModeActive(this, modeAnalysis.get()));
                    }
                    else
                    {
                        if (analyses.size() == 2)
                        {
                            if (unknownProperties.isEmpty())
                            {
                                // Suggestion 1: No action required.
                                suggestions.add(Kind2Suggestion.noActionRequired(this));
                            }
                        }
                        else
                        {
                            if (unknownProperties.isEmpty())
                            {
                                // suggestion 2: Fix the contract of a sub component
                                suggestions.add(Kind2Suggestion.strengthenSubComponentContract(this));
                            }
                        }
                    }
                }
                else
                {
                    if (unknownProperties.isEmpty())
                    {
                        // suggestion 2: Fix the contract of a sub component
                        suggestions.add(Kind2Suggestion.strengthenSubComponentContract(this));
                    }
                }
            }
        }
        else
        {
            if (falsifiedAssumptions.size() == falsifiedProperties.size())
            {
                // suggestion 3: Either complete specification of N ’s contract, or remove component M.
                if (unknownProperties.isEmpty())
                {
                    suggestions.add(Kind2Suggestion.
                            completeSpecificationOrRemoveSubNodes(this, falsifiedAssumptions, true));
                }
                else
                {
                    // we are not sure if contract is satisfied with unknown properties.
                    suggestions.add(Kind2Suggestion.
                            completeSpecificationOrRemoveSubNodes(this, falsifiedAssumptions, false));
                }
            }
            else // some guarantees or ensures are falsified
            {
                if (falsifiedAssumptions.size() > 0)
                {
                    // some assumptions are falsified
                    // suggestion 4: Either make A weaker, or fix N ’s definition to satisfy A.
                    suggestions.add(Kind2Suggestion.makeWeakerOrFixDefinition(this, falsifiedAssumptions));
                }
                else
                {
                    // check whether the subcomponents satisfy their contracts

                    List<Kind2Property> subComponentUnprovenProperties = new ArrayList<>();
                    for (Kind2NodeResult nodeResult : children)
                    {
                        List<Kind2Property> properties = nodeResult
                                .getLastAnalysis().getFalsifiedProperties();
                        properties.addAll(nodeResult.getLastAnalysis().getUnknownProperties());
                        subComponentUnprovenProperties.addAll(properties);
                    }

                    if (subComponentUnprovenProperties.size() == 0)
                    {
                        // suggestion 5: Either make assumptions stronger, or fix definition to satisfy guarantees
                        suggestions.add(Kind2Suggestion.makeAssumptionStrongerOrFixDefinition(this,
                                falsifiedProperties));
                    }
                    else
                    {
                        // suggestion 6: fix reported issues for N ’s subcomponents.
                        suggestions.add(Kind2Suggestion.fixSubComponentIssues(this,
                                subComponentUnprovenProperties));
                    }
                }
            }
        }

        if (!unknownProperties.isEmpty())
        {
            // increase the time out for unknown properties
            suggestions.add(Kind2Suggestion.increaseTimeout(this, unknownProperties));
        }

        isAnalyzed = true;
    }

    public Kind2Result getKind2Result()
    {
        return kind2Result;
    }

    public void setIsVisited(boolean value)
    {
        isVisited = value;
    }

    public String print()
    {
        if (isVisited)
        {
            return "";
        }

        StringBuilder stringBuilder = new StringBuilder();

        for (Kind2NodeResult child : children)
        {
            if (!child.isVisited)
            {
                stringBuilder.append(child.print() + "\n");
            }
        }

        stringBuilder.append("Component: " + name + "\n");

        for (Kind2Suggestion suggestion : suggestions)
        {
            stringBuilder.append(suggestion.toString() + "\n");
        }

        isVisited = true;

        return stringBuilder.toString();
    }

    public boolean isVisited()
    {
        return isVisited;
    }

    public Set<Kind2Property> getFalsifiedProperties()
    {
        Set<Kind2Property> falsifiedProperties = new HashSet<>();

        for (Kind2NodeResult child : children)
        {
            falsifiedProperties.addAll(child.getFalsifiedProperties());
        }

        for (Kind2Analysis analysis : analyses)
        {
            if (analysis.isModeAnalysis())
            {
                falsifiedProperties.addAll(analysis.getFalsifiedProperties());
            }
        }
        falsifiedProperties.addAll(getLastAnalysis().getFalsifiedProperties());
        return falsifiedProperties;
    }

    public Set<Kind2Property> getValidProperties()
    {
        Set<Kind2Property> validProperties = new HashSet<>();

        for (Kind2NodeResult child : children)
        {
            validProperties.addAll(child.getValidProperties());
        }

        for (Kind2Analysis analysis : analyses)
        {
            validProperties.addAll(analysis.getValidProperties());
        }
        return validProperties;
    }

    public Set<Kind2Property> getUnknownProperties()
    {
        Set<Kind2Property> unknownProperties = new HashSet<>();

        for (Kind2NodeResult child : children)
        {
            unknownProperties.addAll(child.getUnknownProperties());
        }

        unknownProperties.addAll(getLastAnalysis().getUnknownProperties());

        return unknownProperties;
    }
}
