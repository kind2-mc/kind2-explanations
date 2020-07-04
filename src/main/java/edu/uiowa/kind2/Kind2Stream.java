/*
 * Copyright (C) 2019  The University of Iowa
 * @author Mudathir Mohamed
 */


package edu.uiowa.kind2;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.util.ArrayList;
import java.util.List;

public class Kind2Stream
{
    /**
     * Kind2 json output for this object
     */
    private final String json;
    private final String name;
    private final Kind2Type kind2Type;
    private final String min;
    private final String max;
    private final String streamClass;
    private final List<Kind2StepValue> stepValues;
    private final Kind2SubNode kind2SubNode;

    public Kind2Stream(Kind2SubNode kind2SubNode, JsonElement jsonElement)
    {
        this.kind2SubNode = kind2SubNode;
        json = new GsonBuilder().setPrettyPrinting().create().toJson(jsonElement);
        name = jsonElement.getAsJsonObject().get(Kind2Labels.name).getAsString();
        String typeString = jsonElement.getAsJsonObject().get(Kind2Labels.type).getAsString();
        kind2Type = Kind2Type.getType(typeString);
        streamClass = jsonElement.getAsJsonObject().get(Kind2Labels.classField).getAsString();
        max = jsonElement.getAsJsonObject().get(Kind2Labels.max) == null ? null :
                jsonElement.getAsJsonObject().get(Kind2Labels.max).getAsString();
        min = jsonElement.getAsJsonObject().get(Kind2Labels.max) == null ? null :
                jsonElement.getAsJsonObject().get(Kind2Labels.min).getAsString();


        this.stepValues = new ArrayList<>();

        JsonArray streamValues = jsonElement.getAsJsonObject().get(Kind2Labels.instantValues).getAsJsonArray();

        for (JsonElement element : streamValues)
        {
            Kind2StepValue stepValue = new Kind2StepValue(this, element);
            stepValues.add(stepValue);
        }
    }

    public Kind2Result getKind2Result()
    {
        return kind2SubNode.getKind2Result();
    }

    public String getName()
    {
        return name;
    }

    public Kind2Type getKind2Type()
    {
        return kind2Type;
    }

    public String getMin()
    {
        return min;
    }

    public String getMax()
    {
        return max;
    }

    public String getStreamClass()
    {
        return streamClass;
    }

    public List<Kind2StepValue> getStepValues()
    {
        return stepValues;
    }

    public String getJson()
    {
        return json;
    }

    @Override
    public String toString()
    {
        return getName();
    }
}
