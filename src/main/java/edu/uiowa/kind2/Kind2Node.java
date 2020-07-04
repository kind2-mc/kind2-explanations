package edu.uiowa.kind2;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

/**
 * ToDo: document this
 */
public class Kind2Node
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
  private final String name;
  /**
   * ToDo: document this
   */
  private final Kind2ModelElementSet modelElementSet;
  /**
   * ToDo: document this
   */
  private final List<Kind2Element> elements;

  public Kind2Node(Kind2ModelElementSet modelElementSet, JsonElement jsonElement)
  {
    this.modelElementSet = modelElementSet;
    this.jsonElement = jsonElement;
    json = new GsonBuilder().setPrettyPrinting().create().toJson(jsonElement);
    JsonObject jsonObject = jsonElement.getAsJsonObject();
    name = jsonObject.get(Kind2Labels.name).getAsString();

    elements = new ArrayList<>();
    for (JsonElement element : jsonObject.get(Kind2Labels.elements).getAsJsonArray())
    {
      Kind2Element kind2Element = new Kind2Element(this, element);
      elements.add(kind2Element);
    }
  }

  /**
   * Kind2 json output for this object.
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
   * ToDo: document this
   */
  public Kind2ModelElementSet getModelElementSet()
  {
    return modelElementSet;
  }

  /**
   * ToDo: document this
   */
  public List<Kind2Element> getElements()
  {
    return elements;
  }
}
