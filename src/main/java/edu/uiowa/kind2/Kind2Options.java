/*
 * Copyright (C) 2020  The University of Iowa
 * @author Mudathir Mohamed
 */

package edu.uiowa.kind2;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A Kind2 options object describes the options used by kind2 process.
 */
public class Kind2Options
{
  /**
   * Kind2 json output for this object
   */
  private final String json;
  /**
   * List of Kind 2 module names that are enabled
   */
  private final List<String> enabledModules;
  /**
   * The wallclock timeout used for all the analyses
   */
  private final double timeout;
  /**
   * Maximal number of iterations for BMC and K-induction.
   */
  private final int bmcMax;
  /**
   * Whether compositional analysis is enabled or not
   */
  private final boolean compositional;
  /**
   * Whether modular analysis is enabled or not.
   */
  private final boolean modular;

  public Kind2Options(JsonElement jsonElement)
  {
    JsonObject jsonObject = jsonElement.getAsJsonObject();
    json = new GsonBuilder().setPrettyPrinting().create().toJson(jsonElement);
    timeout = jsonObject.get(Kind2Labels.timeout).getAsDouble();
    bmcMax = jsonObject.get(Kind2Labels.bmcMax).getAsInt();
    compositional = jsonObject.get(Kind2Labels.compositional).getAsBoolean();
    modular = jsonObject.get(Kind2Labels.modular).getAsBoolean();

    JsonArray modules = jsonObject.get(Kind2Labels.enabled).getAsJsonArray();
    enabledModules = new ArrayList<>();
    for (JsonElement module : modules)
    {
      enabledModules.add(module.getAsString());
    }
  }

  @Override
  public String toString()
  {
    return json;
  }

  public String getJson()
  {
    return json;
  }

  public List<String> getEnabledModules()
  {
    return enabledModules;
  }

  public double getTimeout()
  {
    return timeout;
  }

  public int getBmcMax()
  {
    return bmcMax;
  }

  public boolean isCompositional()
  {
    return compositional;
  }

  public boolean isModular()
  {
    return modular;
  }
}
