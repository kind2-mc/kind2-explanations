/*
 * Copyright (C) 2019  The University of Iowa
 * @author Mudathir Mohamed
 */


package edu.uiowa.kind2;

import com.google.gson.JsonElement;

import java.math.BigInteger;

public class Kind2IntValue extends Kind2Value
{
  private final BigInteger value;

  public Kind2IntValue(Kind2StepValue kind2StepValue, Kind2Type kind2Type, JsonElement jsonElement)
  {
    super(kind2StepValue, kind2Type, jsonElement);
    value = new BigInteger(jsonElement.getAsString());
  }

  public BigInteger getValue()
  {
    return value;
  }

  @Override
  public String toString()
  {
    return value.toString();
  }
}
