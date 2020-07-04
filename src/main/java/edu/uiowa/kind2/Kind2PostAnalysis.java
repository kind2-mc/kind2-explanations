/*
 * Copyright (C) 2020  The University of Iowa
 * @author Mudathir Mohamed
 */

package edu.uiowa.kind2;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ToDo: document this
 */
public class Kind2PostAnalysis
{
  /**
   * Kind2 json output for this object.
   */
  private final String json;
  /**
   * ToDo: document this
   */
  private final String name;
  /**
   * ToDo: document this
   */
  private final List<Kind2ModelElementSet> modelElements;

  /**
   * The associated kind2 analysis.
   */
  private final Kind2Analysis analysis;

  public Kind2PostAnalysis(Kind2Analysis analysis, JsonElement jsonElement)
  {
    this.analysis = analysis;
    json = new GsonBuilder().setPrettyPrinting().create().toJson(jsonElement);

    this.name = jsonElement.getAsJsonObject().get(Kind2Labels.name).getAsString();
    modelElements = new ArrayList<>();
  }

  public void addModelElementSet(Kind2ModelElementSet modelElementSet)
  {
    modelElements.add(modelElementSet);
  }

  /**
   * @return Kind2 json output for this object.
   */
  public String getJson()
  {
    return json;
  }

  /**
   * ToDo: document this
   */
  public String getName()
  {
    return name;
  }

  /**
   * @return the associated kind2 analysis.
   */
  public Kind2Analysis getAnalysis()
  {
    return analysis;
  }

  /**
   * ToDo: document this
   */
  public List<Kind2ModelElementSet> getModelElements()
  {
    return modelElements;
  }
}
