/*
 * Copyright (C) 2019  The University of Iowa
 * @author Mudathir Mohamed
 */

package edu.uiowa.kind2;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.util.*;
import java.util.stream.Collectors;

public class Kind2Analysis
{
  /**
   * Kind2 json output for this object
   */
  private final String json;
  /**
   * Name of the current top-level component.
   */
  private final String nodeName;
  /**
   * Names of the subcomponents whose contract is used in the analysis.
   */
  private final List<String> abstractNodes;
  /**
   * Names of the subcomponents whose implementation is used in the analysis.
   */
  private final List<String> concreteNodes;
  /**
   * Array of pairs (name of subcomponent, number of considered invariants).
   */
  private final List<Pair<String, String>> assumptions;
  /**
   * names of the subcomponents of the current node
   */
  private final List<String> subNodes;
  /**
   * a map between json property name and kind2 attempt to prove this property in the current analysis
   */
  private final Map<String, List<Kind2Property>> propertiesMap;
  /**
   * is the current analysis comes from an exhaustiveness check of the state space covered by the modes of a contract.
   */
  private boolean isModeAnalysis = false;
  /**
   * The associated node result
   */
  private Kind2NodeResult nodeResult = null;
  /**
   * The post analysis performed after the current analysis.
   */
  private Kind2PostAnalysis postAnalysis;

  public Kind2Analysis(JsonElement jsonElement)
  {
    json = new GsonBuilder().setPrettyPrinting().create().toJson(jsonElement);

    this.nodeName = jsonElement.getAsJsonObject().get(Kind2Labels.top).getAsString();
    this.abstractNodes = new ArrayList<>();
    JsonArray abstractArray = jsonElement.getAsJsonObject().get(Kind2Labels.abstractField).getAsJsonArray();
    for (JsonElement node : abstractArray)
    {
      abstractNodes.add(node.getAsString());
    }
    this.concreteNodes = new ArrayList<>();
    JsonArray concreteArray = jsonElement.getAsJsonObject().get(Kind2Labels.concrete).getAsJsonArray();
    for (JsonElement node : concreteArray)
    {
      concreteNodes.add(node.getAsString());
    }

    subNodes = new ArrayList<>(abstractNodes);
    subNodes.addAll(concreteNodes);

    assumptions = new ArrayList<>();

    JsonArray assumptionInvariants = jsonElement.getAsJsonObject().get(Kind2Labels.assumptions).getAsJsonArray();
    for (JsonElement invariant : assumptionInvariants)
    {
      JsonArray invariantArray = invariant.getAsJsonArray();
      String nodeName = invariantArray.get(0).getAsString();
      String number = invariantArray.get(1).getAsString();
      assumptions.add(new Pair<>(nodeName, number));
      if (!subNodes.contains(nodeName) && !nodeName.equals(this.nodeName))
      {
        //ToDo: find a permanent solution to invariants.
        // subNodes.add(nodeName);
      }
    }

    this.propertiesMap = new HashMap<>();
  }

  public void addProperty(Kind2Property property)
  {
    // add the property
    if (propertiesMap.containsKey(property.getJsonName()))
    {
      propertiesMap.get(property.getJsonName()).add(property);
    }
    else
    {
      List<Kind2Property> list = new ArrayList<>();
      list.add(property);
      propertiesMap.put(property.getJsonName(), list);
    }
    if (property.getSource() == Kind2PropertyType.oneModeActive)
    {
      isModeAnalysis = true;
    }
  }

  public String getJson()
  {
    return json;
  }

  /**
   * @return
   */
  public String getNodeName()
  {
    return nodeName;
  }

  public String getNodeMappedName()
  {
    if (nodeResult == null)
    {
      return nodeName;
    }
    return nodeResult.getName();
  }

  public List<String> getAbstractNodes()
  {
    return abstractNodes;
  }

  public List<String> getConcreteNodes()
  {
    return concreteNodes;
  }

  public List<Kind2Property> getProperties()
  {
    List<Kind2Property> lastProperties = new ArrayList<>();
    for (Map.Entry<String, List<Kind2Property>> entry : propertiesMap.entrySet())
    {
      // add the last property object
      Kind2Property lastProperty = entry.getValue().get(entry.getValue().size() - 1);
      lastProperties.add(lastProperty);
    }
    return lastProperties;
  }

  private List<Kind2Property> filterProperties(Kind2Answer answer)
  {
    return getProperties().stream().filter(p -> p.getAnswer() == answer).collect(Collectors.toList());
  }

  public List<Kind2Property> getFalsifiedProperties()
  {
    return filterProperties(Kind2Answer.falsifiable);
  }

  public List<Kind2Property> getUnknownProperties()
  {
    return filterProperties(Kind2Answer.unknown);
  }

  public List<String> getSubNodes()
  {
    return subNodes;
  }

  public boolean isModeAnalysis()
  {
    return isModeAnalysis;
  }

  public void setNodeResult(Kind2NodeResult nodeResult)
  {
    this.nodeResult = nodeResult;
  }

  public Kind2NodeResult getNodeResult()
  {
    return nodeResult;
  }

  public Kind2Result getKind2Result()
  {
    if (nodeResult == null)
    {
      return null;
    }
    else
    {
      return nodeResult.getKind2Result();
    }
  }

  public List<Kind2Property> getValidProperties()
  {
    return filterProperties(Kind2Answer.valid);
  }

  public Map<String, List<Kind2Property>> getPropertiesMap()
  {
    return propertiesMap;
  }

  public Optional<Kind2Property> getProperty(String jsonName)
  {
    return getProperties()
        .stream().filter(p -> p.getJsonName().equals(jsonName)).findFirst();
  }

  public Kind2PostAnalysis getPostAnalysis()
  {
    return postAnalysis;
  }

  public void setPostAnalysis(Kind2PostAnalysis postAnalysis)
  {
    if (this.postAnalysis == null)
    {
      this.postAnalysis = postAnalysis;
    }
    else
    {
      throw new RuntimeException(String.format("Post Analysis is already set for '%1$s'.", nodeName));
    }
  }
}
