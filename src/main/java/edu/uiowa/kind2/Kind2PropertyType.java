/*
 * Copyright (C) 2019  The University of Iowa
 * @author Mudathir Mohamed
 */

package edu.uiowa.kind2;

public enum Kind2PropertyType
{
    assumption("Assumption"),
    guarantee("Guarantee"),
    ensure("Ensure"),
    annotation("PropAnnot"),
    oneModeActive("OneModeActive");

    private final String value;

    Kind2PropertyType(String value)
    {
        this.value = value;
    }

    public static Kind2PropertyType getPropertyType(String propertyType)
    {
        switch (propertyType)
        {
            case "Assumption":
                return assumption;
            case "Guarantee":
                return guarantee;
            case "Ensure":
                return ensure;
            case "PropAnnot":
                return annotation;
            case "OneModeActive":
                return oneModeActive;
            default:
                throw new UnsupportedOperationException("Property type " + propertyType + " is not defined");
        }
    }

    @Override
    public String toString()
    {
        return this.value;
    }
}
