/*
 * Copyright (C) 2019  The University of Iowa
 * @author Mudathir Mohamed
 */

package edu.uiowa.kind2;

/**
 * Enum for property types.
 */
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
      case "assumption":
        return assumption;
      case "Guarantee":
      case "guarantee":
        return guarantee;
      case "Ensure":
      case "ensure":
        return ensure;
      case "PropAnnot":
      case "propAnnot":
        return annotation;
      case "OneModeActive":
      case "oneModeActive":
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
