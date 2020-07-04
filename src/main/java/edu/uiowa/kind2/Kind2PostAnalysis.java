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

public class Kind2PostAnalysis
{
  /**
   * Kind2 json output for this object
   */
  private final String json;
  private final JsonElement jsonElement;
  private final String name;
  private final List<Kind2ModelElementSet> modelElements;

  private final Kind2Analysis analysis;

  public Kind2PostAnalysis(Kind2Analysis analysis, JsonElement jsonElement)
  {
    this.analysis = analysis;
    this.jsonElement = jsonElement;
    json = new GsonBuilder().setPrettyPrinting().create().toJson(jsonElement);

    this.name = jsonElement.getAsJsonObject().get(Kind2Labels.name).getAsString();
    modelElements = new ArrayList<>();
  }

  public void addModelElementSet(Kind2ModelElementSet modelElementSet)
  {
    modelElements.add(modelElementSet);
  }

  public String getJson()
  {
    return json;
  }

  public String getName()
  {
    return name;
  }

  public Kind2Analysis getAnalysis()
  {
    return analysis;
  }

  public List<Kind2ModelElementSet> getModelElements()
  {
    return modelElements;
  }
}
