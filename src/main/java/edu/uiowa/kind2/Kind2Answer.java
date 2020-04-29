/*
 * Copyright (C) 2019  The University of Iowa
 * @author Mudathir Mohamed
 */

package edu.uiowa.kind2;

public enum Kind2Answer
{
    valid("valid"),
    falsifiable("falsifiable"),
    unknown("unknown"),
    property("property");

    private final String value;

    Kind2Answer(String value)
    {
        this.value = value;
    }

    public static Kind2Answer getAnswer(String answer)
    {
        switch (answer)
        {
            case "valid":
                return valid;
            case "falsifiable":
                return falsifiable;
            case "unknown":
                return unknown;
            case "property":
                return property;
            default:
                throw new UnsupportedOperationException("Answer " + answer + " is not defined");
        }
    }

    @Override
    public String toString()
    {
        return this.value;
    }
}