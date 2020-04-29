/*
 * Copyright (C) 2019  The University of Iowa
 * @author Mudathir Mohamed
 */


package edu.uiowa.kind2;

import java.util.*;
import java.util.stream.Collectors;

public class Kind2Suggestion
{
    private Kind2SuggestionType suggestionType;

    private List<String> explanations = new ArrayList<>();
    private String label;
    private final Kind2NodeResult nodeResult;

    private Kind2Suggestion(Kind2NodeResult nodeResult, Kind2SuggestionType suggestionType)
    {
        this.nodeResult = nodeResult;
        this.suggestionType = suggestionType;
    }

    private Kind2Result getKind2Result()
    {
        return nodeResult.getKind2Result();
    }

    public Kind2SuggestionType getSuggestionType()
    {
        return suggestionType;
    }

    @Override
    public String toString()
    {
        StringBuilder stringBuilder = new StringBuilder();

        for (String explanation : explanations)
        {
            stringBuilder.append(explanation + "\n");
        }

        stringBuilder.append("\nSuggestion:\n" + label + "\n");

        return stringBuilder.toString();
    }

    public static Kind2Suggestion increaseTimeout(Kind2NodeResult nodeResult, List<Kind2Property> unknownProperties)
    {
        Kind2Suggestion suggestion = new Kind2Suggestion(nodeResult, Kind2SuggestionType.increaseTimeout);
        suggestion.explanations.add("Unknown answer for properties:");
        for (Kind2Property property : unknownProperties)
        {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(property.toString());

            if (Kind2Result.isPrintingUnknownCounterExamplesEnabled())
            {
                List<Kind2Property> propertyList = nodeResult.getLastAnalysis()
                        .getPropertiesMap().get(property.getJsonName());
                if (propertyList.size() > 1)
                {
                    // get the last property with a counter example
                    Kind2Property lastUnprovenInductiveStep = propertyList.get(propertyList.size() - 2);
                    if (lastUnprovenInductiveStep.getCounterExample() != null &&
                            lastUnprovenInductiveStep.getKInductionStep() != null)
                    {
                        // fix this message
                        String message = "\nIf the starting state of the following trace is reachable, then the property" +
                                " is falsified within %1$d steps. If it is not reachable, " +
                                "please add auxiliary lemmas to prove this property.\n";
                        stringBuilder.append(String.format(message, lastUnprovenInductiveStep.getKInductionStep()));
                        stringBuilder.append(lastUnprovenInductiveStep.getCounterExample().toString());
                    }
                }
            }

            suggestion.explanations.add(stringBuilder.toString());
        }
        suggestion.label = String.format("Kind2 did not find an answer for component %1$s " +
                        "within time limit = %2$s seconds. Try to increase the timeout.",
                nodeResult.getOriginalName(),
                nodeResult.getKind2Result().getTimeout());
        return suggestion;
    }

    static Kind2Suggestion noActionRequired(Kind2NodeResult nodeResult)
    {
        // suggestion 1
        Kind2Suggestion suggestion = new Kind2Suggestion(nodeResult, Kind2SuggestionType.noActionRequired);

        suggestion.label = "No action required.";

        suggestion.explanations.add("All components of the system satisfy their contracts.");

        suggestion.explanations.add("No component of the system was refined.");

        return suggestion;
    }

    public static Kind2Suggestion strengthenSubComponentContract(Kind2NodeResult nodeResult)
    {
        // suggestion 2
        Kind2Suggestion suggestion = new Kind2Suggestion(nodeResult,
                Kind2SuggestionType.strengthenSubComponentContract);

        Set<String> nodes = new HashSet<>();

        // find concrete nodes which were abstracted in a previous analysis
        List<Kind2Analysis> analyses = nodeResult.getAnalyses();
        for (int i = analyses.size() - 1; i > 0; i--)
        {
            List<String> concreteNodes = analyses.get(i).getConcreteNodes();
            for (int j = i - 1; j >= 0; j--)
            {
                List<String> abstractNodes = analyses.get(j).getAbstractNodes();
                List<String> weakNodes = concreteNodes.stream()
                        .filter(n -> abstractNodes.contains(n))
                        .collect(Collectors.toList());
                nodes.addAll(weakNodes);
            }
        }

        suggestion.explanations.add(String.format("Component %1$s is correct after refinement.",
                nodeResult.getOriginalName()));
        StringBuilder stringBuilder = new StringBuilder();
        for (String node : nodes)
        {
            suggestion.explanations
                    .add(String.format("%1$s is a subcomponent of %2$s. " +
                                    "Its contract is too weak to prove %2$s, "
                                    + "but its definition is strong enough. ",
                            nodeResult.getKind2Result().getOriginalName(node),
                            nodeResult.getOriginalName()));

            stringBuilder.append(String.format("Fix the contract of  %1$s.\n",
                    nodeResult.getKind2Result().getOriginalName(node)));
        }

        suggestion.label = stringBuilder.toString();
        return suggestion;
    }

    public static Kind2Suggestion completeSpecificationOrRemoveSubNodes(Kind2NodeResult nodeResult,
                                                                        List<Kind2Property> assumptions,
                                                                        boolean isNodeContractSatisfied)
    {
        // suggestion 3
        Kind2Suggestion suggestion = new Kind2Suggestion(nodeResult,
                Kind2SuggestionType.completeSpecificationOrRemoveComponent);
        if (isNodeContractSatisfied)
        {
            suggestion.explanations.add(String.format("Component %1$s satisfies its current contract.",
                    nodeResult.getOriginalName()));
        }

        Map<String, List<Kind2Property>> subComponents = new HashMap<>();

        for (Kind2Property assumption : assumptions)
        {
            String scope = assumption.getOriginalScope();
            if (subComponents.containsKey(scope))
            {
                subComponents.get(scope).add(assumption);
            }
            else
            {
                List<Kind2Property> properties = new ArrayList<>();
                properties.add(assumption);
                subComponents.put(scope, properties);
            }
        }

        for (Map.Entry<String, List<Kind2Property>> subcomponent : subComponents.entrySet())
        {
            suggestion.explanations.add(String.format("%1$s is a direct subcomponent of %2$s, "
                            + "but one or more assumptions of %1$s are not satisfied by %2$s.",
                    subcomponent.getKey(),
                    nodeResult.getOriginalName()));

            suggestion.explanations.add("Falsified assumptions:");

            for (Kind2Property assumption : subcomponent.getValue())
            {
                suggestion.explanations.add(assumption.getOriginalName());
                if (Kind2Result.isPrintingCounterExamplesEnabled())
                {
                    suggestion.explanations.add(assumption.getCounterExample().toString());
                }
            }
        }

        suggestion.label = String.format("Either complete specification in the contract of  %1$s’, " +
                "or remove components: %2$s.",
                nodeResult.getOriginalName(),
                subComponents.values());
        return suggestion;
    }

    public static Kind2Suggestion makeWeakerOrFixDefinition(Kind2NodeResult nodeResult, List<Kind2Property> assumptions)
    {
        // suggestion 4

        Kind2Suggestion suggestion = new Kind2Suggestion(nodeResult,
                Kind2SuggestionType.makeWeakerOrFixDefinition);

        suggestion.explanations
                .add(String.format("Component %1$s does not satisfy its contract after refinement.",
                        nodeResult.getOriginalName()));


        StringBuilder stringBuilder = new StringBuilder();

        for (Kind2Property assumption : assumptions)
        {
            suggestion.explanations.add(String.format("Assumption %1$s of subcomponent %2$s is " +
                            "not satisfied by %3$s.",
                    assumption.getOriginalName(),
                    assumption.getOriginalScope(),
                    nodeResult.getOriginalName()));
            if (Kind2Result.isPrintingCounterExamplesEnabled())
            {
                suggestion.explanations.add(assumption.getCounterExample().toString());
            }
            stringBuilder.append(String.format("Either make assumption %1$s weaker, or fix the definition of %2$s " +
                            "to satisfy %1$s.\n",
                    assumption.getOriginalName(),
                    nodeResult.getOriginalName()));
        }

        suggestion.explanations.add("\nFalsified Properties:");
        Set<Kind2Property> falsifiedProperties = nodeResult.getLastAnalysis().getFalsifiedProperties()
                .stream().filter(p -> p.getSource() != Kind2PropertyType.assumption)
                .collect(Collectors.toSet());
        for (Kind2Property property : falsifiedProperties)
        {
            suggestion.explanations.add(property.toString());
            if (Kind2Result.isPrintingCounterExamplesEnabled())
            {
                suggestion.explanations.add(property.getCounterExample().toString());
            }
        }

        suggestion.label = stringBuilder.toString();
        return suggestion;
    }

    static Kind2Suggestion makeAssumptionStrongerOrFixDefinition(Kind2NodeResult nodeResult,
                                                                 List<Kind2Property> falsifiedProperties)
    {
        // suggestion 5
        Kind2Suggestion suggestion = new Kind2Suggestion(nodeResult,
                Kind2SuggestionType.makeAssumptionStrongerOrFixDefinition);

        suggestion.label = String.format(
                "Either make assumptions stronger in the contract of %1$ss, " +
                        "or fix the definition of %1$s to satisfy its contract",
                nodeResult.getOriginalName());

        suggestion.explanations
                .add(String.format("Component %1$s does not satisfy its contract after refinement.",
                        nodeResult.getOriginalName()));

        suggestion.explanations.add("\nFalsified Properties:");

        for (Kind2Property property : falsifiedProperties)
        {
            suggestion.explanations.add(property.toString());
            if (Kind2Result.isPrintingCounterExamplesEnabled())
            {
                suggestion.explanations.add(property.getCounterExample().toString());
            }
        }
        return suggestion;
    }

    static Kind2Suggestion fixSubComponentIssues(Kind2NodeResult nodeResult,
                                                 List<Kind2Property> subComponentUnprovenProperties)
    {
        // suggestion 6: Fix reported issues for N ’s subcomponents.
        Kind2Suggestion suggestion = new Kind2Suggestion(nodeResult,
                Kind2SuggestionType.fixSubComponentIssues);

        suggestion.explanations.add("\nUnproved Properties:");
        Set<String> subComponents = new HashSet<>();
        for (Kind2Property property : subComponentUnprovenProperties)
        {
            suggestion.explanations.add(property.toString());
            subComponents.add(property.getOriginalScope());
            if (Kind2Result.isPrintingCounterExamplesEnabled())
            {
                suggestion.explanations.add(property.getCounterExample().toString());
            }
        }

        for (Kind2Property property : nodeResult.getLastAnalysis().getFalsifiedProperties())
        {
            suggestion.explanations.add(property.toString());
            if (Kind2Result.isPrintingCounterExamplesEnabled())
            {
                suggestion.explanations.add(property.getCounterExample().toString());
            }
        }

        suggestion.explanations.add(String.format(
                "Component %1$s does not satisfy its contract after refinement.",
                nodeResult.getOriginalName()));

        suggestion.label = String.format(String.format("Fix reported issues for %1$s’s subcomponents: %2$s.",
                nodeResult.getOriginalName(),
                subComponents));
        return suggestion;
    }

    public static Kind2Suggestion fixOneModeActive(Kind2NodeResult nodeResult,
                                                   Kind2Analysis modeAnalysis)
    {
        // suggestion 7: Fix one mode active
        Kind2Suggestion suggestion = new Kind2Suggestion(nodeResult,
                Kind2SuggestionType.fixOneModeActive);

        suggestion.explanations.add("\nIssues:");

        for (Kind2Property property : modeAnalysis.getFalsifiedProperties())
        {
            suggestion.explanations.add(property.toString());
            if (Kind2Result.isPrintingCounterExamplesEnabled())
            {
                suggestion.explanations.add(property.getCounterExample().toString());
            }
        }

        suggestion.explanations.add(String.format(
                "The modes defined in the contract of %1$s does not cover all states.",
                nodeResult.getOriginalName()));

        suggestion.label = String.format(String.format("Fix the modes of component %1$s.",
                nodeResult.getOriginalName()));
        return suggestion;
    }
}
