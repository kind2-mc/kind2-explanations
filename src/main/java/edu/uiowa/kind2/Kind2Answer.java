/*
 * Copyright (C) 2019  The University of Iowa
 * @author Mudathir Mohamed
 */

package edu.uiowa.kind2;

/**
 * The source of the answer, and the result value of the check.
 * The result can be valid, falsifiable, or unknown
 */
public enum Kind2Answer
{
  valid("valid"),
  falsifiable("falsifiable"),
  unknown("unknown");

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