/*
 * Copyright (C) 2019  The University of Iowa
 * @author Mudathir Mohamed
 */


package edu.uiowa.kind2;


import com.google.gson.JsonElement;

/**
 * This class stores the boolean value of kind2 bool type.
 */
public class Kind2BoolValue extends Kind2Value
{
  private final boolean value;

  public Kind2BoolValue(Kind2StepValue kind2StepValue, Kind2Type kind2Type, JsonElement jsonElement)
  {
    super(kind2StepValue, kind2Type, jsonElement);
    value = Boolean.parseBoolean(jsonElement.getAsString());
  }

  public boolean getValue()
  {
    return value;
  }

  @Override
  public String toString()
  {
    return Boolean.toString(value);
  }
}
