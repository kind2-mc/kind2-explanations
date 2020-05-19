package edu.uiowa.kind2;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

public class Kind2Node
{
    private final String json;
    private final JsonElement jsonElement;
    private final String name;
    private final Kind2ModelElementSet modelElementSet;
    private final List<Kind2Element> elements;
    public Kind2Node(Kind2ModelElementSet modelElementSet, JsonElement jsonElement)
    {
        this.modelElementSet = modelElementSet;
        this.jsonElement = jsonElement;
        json = new GsonBuilder().setPrettyPrinting().create().toJson(jsonElement);
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        name = jsonObject.get(Kind2Labels.name).getAsString();

        elements = new ArrayList<>();
        for (JsonElement element: jsonObject.get(Kind2Labels.elements).getAsJsonArray())
        {
            Kind2Element kind2Element = new Kind2Element(this, element);
            elements.add(kind2Element);
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

    public String getName()
    {
        return name;
    }

    public Kind2ModelElementSet getModelElementSet()
    {
        return modelElementSet;
    }

    public List<Kind2Element> getElements()
    {
        return elements;
    }
}
