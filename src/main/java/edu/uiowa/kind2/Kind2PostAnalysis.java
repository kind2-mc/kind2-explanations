/*
 * Copyright (C) 2020  The University of Iowa
 * @author Mudathir Mohamed
 */

package edu.uiowa.kind2;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Kind2PostAnalysis
{
    private final String json;
    private final JsonElement jsonElement;
    private final String name;
    private final Map<String, Kind2ModelElementSet> modelElementSetMap;

    private final Kind2Analysis analysis;

    public Kind2PostAnalysis(Kind2Analysis analysis, JsonElement jsonElement)
    {
        this.analysis = analysis;
        this.jsonElement = jsonElement;
        json = new GsonBuilder().setPrettyPrinting().create().toJson(jsonElement);

        this.name = jsonElement.getAsJsonObject().get(Kind2Labels.name).getAsString();
        modelElementSetMap = new HashMap<>();
    }

    public void addModelElementSet(Kind2ModelElementSet modelElementSet)
    {
        // add the modelElementSet
        if (modelElementSetMap.containsKey(modelElementSet.getClassField()))
        {
            throw new RuntimeException(modelElementSet.getClassField() + "is already added");
        }
        modelElementSetMap.put(modelElementSet.getClassField(), modelElementSet);
    }

    public String getJson()
    {
        return json;
    }

    public String getName()
    {
        return name;
    }

    public Kind2Analysis getAnalysis()
    {
        return analysis;
    }

    public Map<String, Kind2ModelElementSet> getModelElementSetMap()
    {
        return modelElementSetMap;
    }
}
