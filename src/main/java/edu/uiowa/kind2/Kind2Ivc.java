package edu.uiowa.kind2;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class Kind2Ivc
{
    private final String json;
    private JsonElement jsonElement;
    private final Kind2PostAnalysis kind2PostAnalysis;
    private final int size;

    public Kind2Ivc(Kind2PostAnalysis kind2PostAnalysis, JsonElement jsonElement)
    {
        this.kind2PostAnalysis = kind2PostAnalysis;
        this.jsonElement = jsonElement;
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        json = new GsonBuilder().setPrettyPrinting().create().toJson(jsonElement);
        size = jsonObject.get(Kind2Labels.size).getAsInt();
    }
}
