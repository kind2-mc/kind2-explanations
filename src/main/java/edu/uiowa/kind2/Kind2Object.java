/*
 * Copyright (C) 2019  The University of Iowa
 * @author Mudathir Mohamed
 */

package edu.uiowa.kind2;

/**
 * Enum for the kind2 json objects
 */
public enum Kind2Object
{
  kind2Options("kind2Options"),
  log("log"),
  analysisStart("analysisStart"),
  property("property"),
  analysisStop("analysisStop"),
  postAnalysisStart("postAnalysisStart"),
  postAnalysisEnd("postAnalysisEnd"),
  modelElementSet("modelElementSet");

  private final String value;

  private Kind2Object(String value)
  {
    this.value = value;
  }

  public static Kind2Object getKind2Object(String kind2Object)
  {
    switch (kind2Object)
    {
      case "kind2Options":
        return kind2Options;
      case "log":
        return log;
      case "analysisStart":
        return analysisStart;
      case "property":
        return property;
      case "analysisStop":
        return analysisStop;
      case "postAnalysisStart":
        return postAnalysisStart;
      case "modelElementSet":
        return modelElementSet;
      case "postAnalysisEnd":
        return postAnalysisEnd;
      default:
        throw new UnsupportedOperationException("Value " + kind2Object + " is not defined");
    }
  }

  @Override
  public String toString()
  {
    return this.value;
  }
}