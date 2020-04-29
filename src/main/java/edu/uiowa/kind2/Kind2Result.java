/*
 * Copyright (C) 2019  The University of Iowa
 * @author Mudathir Mohamed
 */


package edu.uiowa.kind2;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.util.*;
import java.util.stream.Collectors;

public class Kind2Result
{
    private static boolean printingCounterExamplesEnabled = false;
    private static boolean printingUnknownCounterExamplesEnabled = false;
    private static boolean printingLineNumbersEnabled = false;
    private static String openingSymbols = "{{";
    private static String closingSymbols = "}}";
    private static boolean printingOriginalNameEnabled = false;
    private Kind2NodeResult root;
    private Map<String, Kind2NodeResult> resultMap = new HashMap<>();
    private final String timeout;
    private final String json;
    private final List<Kind2Log> kind2Logs;
    private final Kind2Mapping kind2Mapping;

    private Kind2Result(Kind2Mapping kind2Mapping, String timeout, JsonArray jsonArray)
    {
        this.kind2Mapping = kind2Mapping;
        this.timeout = timeout;
        json = new GsonBuilder().setPrettyPrinting().create().toJson(jsonArray);
        kind2Logs = new ArrayList<>();
    }


    private void put(String key, Kind2Analysis analysis)
    {
        if (resultMap.containsKey(key))
        {
            resultMap.get(key).addAnalysis(analysis);
        }
        else
        {
            Kind2NodeResult nodeResult = new Kind2NodeResult(this, key);
            nodeResult.addAnalysis(analysis);
            resultMap.put(key, nodeResult);
            // root is the last analysis in kind2
            root = nodeResult;
        }
    }

    public Kind2NodeResult getNodeResult(String nodeName)
    {
        return resultMap.get(nodeName);
    }

    private void analyze()
    {
        if (root == null)
        {
            throwKind2Errors();
            return;
        }
        root.analyze();
    }

    private void throwKind2Errors()
    {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("An error has occurred during kind2 analysis. Please check the following logs:\n");
        boolean someError = false;
        for (Kind2Log log : kind2Logs)
        {
            if (log.getLevel() == Kind2LogLevel.error ||
                    log.getLevel() == Kind2LogLevel.fatal ||
                    log.getLevel() == Kind2LogLevel.off)
            {
                stringBuilder.append(log + "\n");
                someError = true;
            }
        }
        if (someError)
        {
            throw new RuntimeException(stringBuilder.toString());
        }
    }

    @Override
    public String toString()
    {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(root.toString());
        stringBuilder.append("Verification summary\n");
        stringBuilder.append(root.printVerificationSummary());
        return stringBuilder.toString();
    }

    /**
     * Analyze the json output of kind2 verification.
     *
     * @param json kind2 json output
     * @return an object of Kind2Result which contains the result of analyzing kind2 output.
     */
    public static Kind2Result analyzeJsonResult(String json)
    {
        return analyzeJsonResult(json, new Kind2Mapping());
    }

    /**
     * Analyze the json output of kind2 verification.
     *
     * @param json     kind2 json output
     * @param mappings a map from lustre identifiers to the original identifiers. Any lustre identifier
     *                 is replaced with the original name in printed messages if it exists.
     *                 Otherwise the lustre identifier would be used.
     * @return an object of Kind2Result which contains the result of analyzing kind2 output.
     */
    public static Kind2Result analyzeJsonResult(String json, Kind2Mapping mappings)
    {
        Kind2Result kind2Result = null;
        JsonArray resultArray = new JsonParser().parse(json).getAsJsonArray();
        Kind2Analysis kind2Analysis = null;

        for (JsonElement jsonElement : resultArray)
        {
            String objectType = jsonElement.getAsJsonObject().get(Kind2Labels.objectType).getAsString();
            Kind2Object kind2Object = Kind2Object.getKind2Object(objectType);

            if (kind2Object == Kind2Object.kind2Options)
            {
                String timeout = jsonElement.getAsJsonObject().get(Kind2Labels.timeout).getAsString();
                kind2Result = new Kind2Result(mappings, timeout, resultArray);
            }

            if (kind2Object == Kind2Object.log && kind2Result != null)
            {
                Kind2Log log = new Kind2Log(kind2Result, jsonElement);
                kind2Result.kind2Logs.add(log);
            }

            if (kind2Object == Kind2Object.analysisStart)
            {
                // define new analysis
                kind2Analysis = new Kind2Analysis(jsonElement);
            }

            if (kind2Object == Kind2Object.property)
            {
                if (kind2Analysis != null)
                {
                    Kind2Property property = new Kind2Property(kind2Analysis, jsonElement);
                    kind2Analysis.addProperty(property);
                }
                else
                {
                    throw new RuntimeException("Can not parse kind2 json output");
                }
            }

            if (kind2Object == Kind2Object.analysisStop)
            {
                if (kind2Analysis != null)
                {
                    // 	finish the analysis
                    kind2Result.put(kind2Analysis.getNodeName(), kind2Analysis);
                    kind2Analysis = null;
                }
                else
                {
                    throw new RuntimeException("Failed to analyze kind2 json output");
                }
            }

            if (kind2Object == Kind2Object.property)
            {
                if (kind2Analysis != null)
                {
                    Kind2Property property = new Kind2Property(kind2Analysis, jsonElement);
                    kind2Analysis.addProperty(property);
                }
                else
                {
                    throw new RuntimeException("Can not parse kind2 json output");
                }
            }
            if (kind2Object == Kind2Object.property)
            {
                if (kind2Analysis != null)
                {
                    Kind2Property property = new Kind2Property(kind2Analysis, jsonElement);
                    kind2Analysis.addProperty(property);
                }
                else
                {
                    throw new RuntimeException("Can not parse kind2 json output");
                }
            }
            if (kind2Object == Kind2Object.property)
            {
                if (kind2Analysis != null)
                {
                    Kind2Property property = new Kind2Property(kind2Analysis, jsonElement);
                    kind2Analysis.addProperty(property);
                }
                else
                {
                    throw new RuntimeException("Can not parse kind2 json output");
                }
            }
            if (kind2Object == Kind2Object.property)
            {
                if (kind2Analysis != null)
                {
                    Kind2Property property = new Kind2Property(kind2Analysis, jsonElement);
                    kind2Analysis.addProperty(property);
                }
                else
                {
                    throw new RuntimeException("Can not parse kind2 json output");
                }
            }
            if (kind2Object == Kind2Object.property)
            {
                if (kind2Analysis != null)
                {
                    Kind2Property property = new Kind2Property(kind2Analysis, jsonElement);
                    kind2Analysis.addProperty(property);
                }
                else
                {
                    throw new RuntimeException("Can not parse kind2 json output");
                }
            }
            if (kind2Object == Kind2Object.property)
            {
                if (kind2Analysis != null)
                {
                    Kind2Property property = new Kind2Property(kind2Analysis, jsonElement);
                    kind2Analysis.addProperty(property);
                }
                else
                {
                    throw new RuntimeException("Can not parse kind2 json output");
                }
            }
            if (kind2Object == Kind2Object.property)
            {
                if (kind2Analysis != null)
                {
                    Kind2Property property = new Kind2Property(kind2Analysis, jsonElement);
                    kind2Analysis.addProperty(property);
                }
                else
                {
                    throw new RuntimeException("Can not parse kind2 json output");
                }
            }
            if (kind2Object == Kind2Object.property)
            {
                if (kind2Analysis != null)
                {
                    Kind2Property property = new Kind2Property(kind2Analysis, jsonElement);
                    kind2Analysis.addProperty(property);
                }
                else
                {
                    throw new RuntimeException("Can not parse kind2 json output");
                }
            }
            if (kind2Object == Kind2Object.property)
            {
                if (kind2Analysis != null)
                {
                    Kind2Property property = new Kind2Property(kind2Analysis, jsonElement);
                    kind2Analysis.addProperty(property);
                }
                else
                {
                    throw new RuntimeException("Can not parse kind2 json output");
                }
            }
            if (kind2Object == Kind2Object.property)
            {
                if (kind2Analysis != null)
                {
                    Kind2Property property = new Kind2Property(kind2Analysis, jsonElement);
                    kind2Analysis.addProperty(property);
                }
                else
                {
                    throw new RuntimeException("Can not parse kind2 json output");
                }
            }
            if (kind2Object == Kind2Object.property)
            {
                if (kind2Analysis != null)
                {
                    Kind2Property property = new Kind2Property(kind2Analysis, jsonElement);
                    kind2Analysis.addProperty(property);
                }
                else
                {
                    throw new RuntimeException("Can not parse kind2 json output");
                }
            }
            if (kind2Object == Kind2Object.property)
            {
                if (kind2Analysis != null)
                {
                    Kind2Property property = new Kind2Property(kind2Analysis, jsonElement);
                    kind2Analysis.addProperty(property);
                }
                else
                {
                    throw new RuntimeException("Can not parse kind2 json output");
                }
            }
            if (kind2Object == Kind2Object.property)
            {
                if (kind2Analysis != null)
                {
                    Kind2Property property = new Kind2Property(kind2Analysis, jsonElement);
                    kind2Analysis.addProperty(property);
                }
                else
                {
                    throw new RuntimeException("Can not parse kind2 json output");
                }
            }
            if (kind2Object == Kind2Object.property)
            {
                if (kind2Analysis != null)
                {
                    Kind2Property property = new Kind2Property(kind2Analysis, jsonElement);
                    kind2Analysis.addProperty(property);
                }
                else
                {
                    throw new RuntimeException("Can not parse kind2 json output");
                }
            }
            if (kind2Object == Kind2Object.property)
            {
                if (kind2Analysis != null)
                {
                    Kind2Property property = new Kind2Property(kind2Analysis, jsonElement);
                    kind2Analysis.addProperty(property);
                }
                else
                {
                    throw new RuntimeException("Can not parse kind2 json output");
                }
            }
            if (kind2Object == Kind2Object.property)
            {
                if (kind2Analysis != null)
                {
                    Kind2Property property = new Kind2Property(kind2Analysis, jsonElement);
                    kind2Analysis.addProperty(property);
                }
                else
                {
                    throw new RuntimeException("Can not parse kind2 json output");
                }
            }
            if (kind2Object == Kind2Object.property)
            {
                if (kind2Analysis != null)
                {
                    Kind2Property property = new Kind2Property(kind2Analysis, jsonElement);
                    kind2Analysis.addProperty(property);
                }
                else
                {
                    throw new RuntimeException("Can not parse kind2 json output");
                }
            }
            if (kind2Object == Kind2Object.property)
            {
                if (kind2Analysis != null)
                {
                    Kind2Property property = new Kind2Property(kind2Analysis, jsonElement);
                    kind2Analysis.addProperty(property);
                }
                else
                {
                    throw new RuntimeException("Can not parse kind2 json output");
                }
            }
            if (kind2Object == Kind2Object.property)
            {
                if (kind2Analysis != null)
                {
                    Kind2Property property = new Kind2Property(kind2Analysis, jsonElement);
                    kind2Analysis.addProperty(property);
                }
                else
                {
                    throw new RuntimeException("Can not parse kind2 json output");
                }
            }
            if (kind2Object == Kind2Object.property)
            {
                if (kind2Analysis != null)
                {
                    Kind2Property property = new Kind2Property(kind2Analysis, jsonElement);
                    kind2Analysis.addProperty(property);
                }
                else
                {
                    throw new RuntimeException("Can not parse kind2 json output");
                }
            }
            if (kind2Object == Kind2Object.property)
            {
                if (kind2Analysis != null)
                {
                    Kind2Property property = new Kind2Property(kind2Analysis, jsonElement);
                    kind2Analysis.addProperty(property);
                }
                else
                {
                    throw new RuntimeException("Can not parse kind2 json output");
                }
            }
            if (kind2Object == Kind2Object.property)
            {
                if (kind2Analysis != null)
                {
                    Kind2Property property = new Kind2Property(kind2Analysis, jsonElement);
                    kind2Analysis.addProperty(property);
                }
                else
                {
                    throw new RuntimeException("Can not parse kind2 json output");
                }
            }
            if (kind2Object == Kind2Object.property)
            {
                if (kind2Analysis != null)
                {
                    Kind2Property property = new Kind2Property(kind2Analysis, jsonElement);
                    kind2Analysis.addProperty(property);
                }
                else
                {
                    throw new RuntimeException("Can not parse kind2 json output");
                }
            }
            if (kind2Object == Kind2Object.property)
            {
                if (kind2Analysis != null)
                {
                    Kind2Property property = new Kind2Property(kind2Analysis, jsonElement);
                    kind2Analysis.addProperty(property);
                }
                else
                {
                    throw new RuntimeException("Can not parse kind2 json output");
                }
            }
            if (kind2Object == Kind2Object.property)
            {
                if (kind2Analysis != null)
                {
                    Kind2Property property = new Kind2Property(kind2Analysis, jsonElement);
                    kind2Analysis.addProperty(property);
                }
                else
                {
                    throw new RuntimeException("Can not parse kind2 json output");
                }
            }
            if (kind2Object == Kind2Object.property)
            {
                if (kind2Analysis != null)
                {
                    Kind2Property property = new Kind2Property(kind2Analysis, jsonElement);
                    kind2Analysis.addProperty(property);
                }
                else
                {
                    throw new RuntimeException("Can not parse kind2 json output");
                }
            }
            if (kind2Object == Kind2Object.property)
            {
                if (kind2Analysis != null)
                {
                    Kind2Property property = new Kind2Property(kind2Analysis, jsonElement);
                    kind2Analysis.addProperty(property);
                }
                else
                {
                    throw new RuntimeException("Can not parse kind2 json output");
                }
            }
            if (kind2Object == Kind2Object.property)
            {
                if (kind2Analysis != null)
                {
                    Kind2Property property = new Kind2Property(kind2Analysis, jsonElement);
                    kind2Analysis.addProperty(property);
                }
                else
                {
                    throw new RuntimeException("Can not parse kind2 json output");
                }
            }
            if (kind2Object == Kind2Object.property)
            {
                if (kind2Analysis != null)
                {
                    Kind2Property property = new Kind2Property(kind2Analysis, jsonElement);
                    kind2Analysis.addProperty(property);
                }
                else
                {
                    throw new RuntimeException("Can not parse kind2 json output");
                }
            }
            if (kind2Object == Kind2Object.property)
            {
                if (kind2Analysis != null)
                {
                    Kind2Property property = new Kind2Property(kind2Analysis, jsonElement);
                    kind2Analysis.addProperty(property);
                }
                else
                {
                    throw new RuntimeException("Can not parse kind2 json output");
                }
            }
            if (kind2Object == Kind2Object.property)
            {
                if (kind2Analysis != null)
                {
                    Kind2Property property = new Kind2Property(kind2Analysis, jsonElement);
                    kind2Analysis.addProperty(property);
                }
                else
                {
                    throw new RuntimeException("Can not parse kind2 json output");
                }
            }
        }

        // build the node tree
        kind2Result.buildTree();
        // analyze the result
        kind2Result.analyze();

        return kind2Result;
    }

    private void buildTree()
    {
        for (Map.Entry<String, Kind2NodeResult> entry : resultMap.entrySet())
        {
            Kind2NodeResult nodeResult = entry.getValue();

            // we need this foreach loop to iterate through all analyses, and not just a single analysis.
            // There are some cases where the child nodes are not returned in concrete or abstract fields
            // and scattered across multiple analyses.

            for (Kind2Analysis analysis : nodeResult.getAnalyses())
            {
                List<String> subNodes = analysis.getSubNodes();

                for (String node : subNodes)
                {
                    if (resultMap.containsKey(node))
                    {
                        nodeResult.addChild(resultMap.get(node));
                    }
                }
            }
        }
    }

    public String getTimeout()
    {
        return timeout;
    }

    public String print()
    {
        for (Map.Entry<String, Kind2NodeResult> entry : resultMap.entrySet())
        {
            entry.getValue().setIsVisited(false);
        }
        return root.print();
    }

    public String getJson()
    {
        return json;
    }

    public Kind2NodeResult getRoot()
    {
        return root;
    }

    public Kind2Mapping getNamesMap()
    {
        return kind2Mapping;
    }

    public Map<String, Kind2NodeResult> getResultMap()
    {
        return resultMap;
    }

    public Set<Kind2Property> getFalsifiedProperties()
    {
        return root.getFalsifiedProperties();
    }

    public Set<Kind2Property> getValidProperties()
    {
        return root.getValidProperties();
    }

    public Set<Kind2Property> getUnknownProperties()
    {
        return root.getUnknownProperties();
    }

    public static boolean isPrintingCounterExamplesEnabled()
    {
        return printingCounterExamplesEnabled;
    }

    public static void setPrintingCounterExamplesEnabled(boolean value)
    {
        Kind2Result.printingCounterExamplesEnabled = value;
    }

    public static boolean isPrintingUnknownCounterExamplesEnabled()
    {
        return printingUnknownCounterExamplesEnabled;
    }

    public static void setPrintingUnknownCounterExamplesEnabled(boolean printingUnknownCounterExamplesEnabled)
    {
        Kind2Result.printingUnknownCounterExamplesEnabled = printingUnknownCounterExamplesEnabled;
    }

    public static boolean isPrintingLineNumbersEnabled()
    {
        return printingLineNumbersEnabled;
    }

    public static void setPrintingLineNumbersEnabled(boolean printingLineNumbersEnabled)
    {
        Kind2Result.printingLineNumbersEnabled = printingLineNumbersEnabled;
    }

    public static void setOpeningSymbols(String symbols)
    {
        Kind2Result.openingSymbols = symbols;
    }

    public static void setClosingSymbols(String symbols)
    {
        Kind2Result.closingSymbols = symbols;
    }

    public static String getOpeningSymbols()
    {
        return openingSymbols;
    }

    public static String getClosingSymbols()
    {
        return closingSymbols;
    }

    public Kind2NodeResult getOriginalNodeResult(String originalNodeName)
    {
        if (Kind2Result.isPrintingOriginalNameEnabled())
        {
            // find the lustre name for the original node
            Optional<Kind2IdentifierMapping> mapping = kind2Mapping.getNamesMap().values()
                    .stream().filter(m -> m.getOriginalName().equals(originalNodeName))
                    .findFirst();
            if (mapping.isPresent())
            {
                return resultMap.get(mapping.get().getLustreName());
            }
            throw new RuntimeException(String.format("No mapping is found for '%1$s'.", originalNodeName));
        }
        return resultMap.get(originalNodeName);
    }

    public List<Kind2Log> getKind2Logs()
    {
        return kind2Logs.stream().filter(l -> !l.isHidden()).collect(Collectors.toList());
    }

    public List<Kind2Log> getAllKind2Logs()
    {
        return kind2Logs;
    }

    public String getOriginalName(String name)
    {
        return kind2Mapping.getOriginalName(name);
    }

    public String getOriginalLine(String kind2Name, String line)
    {
        return kind2Mapping.getOriginalLine(kind2Name, line);
    }

    public String getOriginalColumn(String kind2Name, String column)
    {
        return kind2Mapping.getOriginalColumn(kind2Name, column);
    }

    public Kind2Mapping getKind2Mapping()
    {
        return kind2Mapping;
    }

    public Set<String> getUnmappedNames()
    {
        return kind2Mapping.getUnmappedNames();
    }

    public static boolean isPrintingOriginalNameEnabled()
    {
        return printingOriginalNameEnabled;
    }

    public static void setPrintingOriginalNameEnabled(boolean printingOriginalNameEnabled)
    {
        Kind2Result.printingOriginalNameEnabled = printingOriginalNameEnabled;
    }
}
