package edu.uiowa.kind2;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

public class Kind2ModelElementSet
{
    private final String json;
    private final JsonElement jsonElement;
    private final String classField;
    private final int size;
    private final String runtimeUnit;
    private final double runtimeValue;
    private final List<Kind2Node> nodes;

    private Kind2PostAnalysis postAnalysis;

    public Kind2ModelElementSet(Kind2PostAnalysis analysis, JsonElement jsonElement)
    {
        this.postAnalysis = analysis;
        this.jsonElement = jsonElement;
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        json = new GsonBuilder().setPrettyPrinting().create().toJson(jsonElement);
        classField = jsonObject.get(Kind2Labels.classField).getAsString();
        size = jsonObject.get(Kind2Labels.size).getAsInt();
        JsonObject runtime = jsonObject.get(Kind2Labels.runtime).getAsJsonObject();
        runtimeUnit = runtime.get(Kind2Labels.unit).getAsString();
        runtimeValue = runtime.get(Kind2Labels.value).getAsDouble();
        nodes = new ArrayList<>();
        JsonArray nodeElements = jsonObject.get(Kind2Labels.nodes).getAsJsonArray();
        for (JsonElement element: nodeElements)
        {
            Kind2Node kind2Node = new Kind2Node(this, element);
            this.nodes.add(kind2Node);
        }
    }

    public String getJson()
    {
        return json;
    }

    public JsonElement getJsonElement()
    {
        return jsonElement;
    }

    public String getClassField()
    {
        return classField;
    }

    public int getSize()
    {
        return size;
    }

    public String getRuntimeUnit()
    {
        return runtimeUnit;
    }

    public double getRuntimeValue()
    {
        return runtimeValue;
    }

    public List<Kind2Node> getNodes()
    {
        return nodes;
    }

    public Kind2PostAnalysis getPostAnalysis()
    {
        return postAnalysis;
    }
}
