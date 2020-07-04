package edu.uiowa.kind2;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

public class Kind2ModelElementSet
{
  /**
   * Kind2 json output for this object.
   */
  private final String json;
  /**
   * ToDo: document this 
   */
  private final JsonElement jsonElement;
  /**
   * ToDo: document this 
   */
  private final String classField;
  /**
   * ToDo: document this 
   */
  private final int size;
  /**
   * ToDo: document this 
   */
  private final String runtimeUnit;
  /**
   * ToDo: document this 
   */
  private final double runtimeValue;
  /**
   * ToDo: document this 
   */
  private final List<Kind2Node> nodes;

  /**
   * The associated  post analysis object. 
   */
  private Kind2PostAnalysis postAnalysis;

  public Kind2ModelElementSet(Kind2PostAnalysis analysis, JsonElement jsonElement)
  {
    this.postAnalysis = analysis;
    this.jsonElement = jsonElement;
    JsonObject jsonObject = jsonElement.getAsJsonObject();
    json = new GsonBuilder().setPrettyPrinting().create().toJson(jsonElement);
    classField = jsonObject.get(Kind2Labels.classField).getAsString();
    size = jsonObject.get(Kind2Labels.size).getAsInt();
    JsonObject runtime = jsonObject.get(Kind2Labels.runtime).getAsJsonObject();
    runtimeUnit = runtime.get(Kind2Labels.unit).getAsString();
    runtimeValue = runtime.get(Kind2Labels.value).getAsDouble();
    nodes = new ArrayList<>();
    JsonArray nodeElements = jsonObject.get(Kind2Labels.nodes).getAsJsonArray();
    for (JsonElement element : nodeElements)
    {
      Kind2Node kind2Node = new Kind2Node(this, element);
      this.nodes.add(kind2Node);
    }
  }

  /**
   * @return
   *    Kind2 json output for this object
   */
  public String getJson()
  {
    return json;
  }

  /**
   * ToDo: document this
   */
  public JsonElement getJsonElement()
  {
    return jsonElement;
  }

  /**
   * ToDo: document this
   */
  public String getClassField()
  {
    return classField;
  }

  /**
   * ToDo: document this
   */
  public int getSize()
  {
    return size;
  }

  /**
   * ToDo: document this
   */
  public String getRuntimeUnit()
  {
    return runtimeUnit;
  }

  /**
   * ToDo: document this
   */
  public double getRuntimeValue()
  {
    return runtimeValue;
  }

  /**
   * ToDo: document this
   */
  public List<Kind2Node> getNodes()
  {
    return nodes;
  }

  /**
   * @return the associated post analysis.
   */
  public Kind2PostAnalysis getPostAnalysis()
  {
    return postAnalysis;
  }
}
