/*
 * Copyright (C) 2019  The University of Iowa
 * @author Mudathir Mohamed
 */

package edu.uiowa.kind2;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;

public class Kind2CounterExample
{
    private final Kind2Node mainNode;
    private final String json;
    private final Kind2Property property;

    public Kind2CounterExample(Kind2Property property, JsonElement jsonElement)
    {
        this.property = property;
        json = new GsonBuilder().setPrettyPrinting().create().toJson(jsonElement);
        mainNode = new Kind2Node(this, jsonElement);
    }

    public Kind2Node getMainNode()
    {
        return mainNode;
    }

    public Kind2Result getKind2Result()
    {
        return property.getKind2Result();
    }

    @Override
    public String toString()
    {
        int maxNameLength = mainNode.getMaxNameLength();
        int maxValueLength = mainNode.getMaxValueLength();
        return "Counterexample:" + mainNode.print(maxNameLength, maxValueLength);
    }
}
