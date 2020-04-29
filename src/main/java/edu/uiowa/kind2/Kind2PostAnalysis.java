/*
 * Copyright (C) 2019  The University of Iowa
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
    private final String name;
    private final List<Kind2Ivc> ivcList = new ArrayList<>();

    public Kind2PostAnalysis(JsonElement jsonElement)
    {
        json = new GsonBuilder().setPrettyPrinting().create().toJson(jsonElement);
        this.name = jsonElement.getAsJsonObject().get(Kind2Labels.name).getAsString();
    }

    public String getJson()
    {
        return json;
    }

    public void addIvc(Kind2Ivc ivc)
    {
        ivcList.add(ivc);
    }

    public List<Kind2Ivc> getIvcList()
    {
        return ivcList;
    }
}
