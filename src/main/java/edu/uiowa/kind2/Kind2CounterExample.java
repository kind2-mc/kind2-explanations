/*
 * Copyright (C) 2019  The University of Iowa
 * @author Mudathir Mohamed
 */

package edu.uiowa.kind2;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;

/**
 * Counterexample to the property satisfaction (only available when answer is falsifiable).
 * It describes a sequence of values for each stream, and automaton,
 * that leads the system to the violation of the property.
 * It also gives the list of contract modes that are active at each step, if any.
 */
public class Kind2CounterExample
{
    private final Kind2SubNode mainNode;
    /**
     * Kind2 json output for this object
     */
    private final String json;
    private final Kind2Property property;

    public Kind2CounterExample(Kind2Property property, JsonElement jsonElement)
    {
        this.property = property;
        json = new GsonBuilder().setPrettyPrinting().create().toJson(jsonElement);
        mainNode = new Kind2SubNode(this, jsonElement);
    }

    public Kind2SubNode getMainNode()
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
