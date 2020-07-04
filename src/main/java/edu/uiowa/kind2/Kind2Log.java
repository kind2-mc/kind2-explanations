/*
 * Copyright (C) 2019  The University of Iowa
 * @author Mudathir Mohamed
 */


package edu.uiowa.kind2;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class Kind2Log
{
  /**
   * A level that gives a rough guide of the importance of the message.
   */
  private final Kind2LogLevel level;
  /**
   * The name of the Kind 2 module which wrote the log.
   */
  private final String source;
  /**
   * The log message.
   */
  private final String value;
  /**
   * Associated kind2Result object
   */
  private final Kind2Result kind2Result;
  /**
   * The original kind2 output for this object in pretty json format
   */
  private final String prettyJson;
  /**
   * The original kind2 output for this object in json format
   */
  private final String json;
  private final String logClass;
  /**
   * Associated line in the input file, if any.
   */
  private final String line;
  /**
   * Associated column in the input file, if any.
   */
  private final String column;
  /**
   * isHidden determines whether the current log is printed
   */
  private boolean isHidden;

  public Kind2Log(Kind2Result kind2Result, JsonElement jsonElement)
  {
    this.kind2Result = kind2Result;
    JsonObject jsonObject = jsonElement.getAsJsonObject();
    prettyJson = new GsonBuilder().setPrettyPrinting().create().toJson(jsonElement);
    json = new GsonBuilder().create().toJson(jsonElement);
    this.level = Kind2LogLevel.getLevel(jsonObject.get(Kind2Labels.level).getAsString());
    this.source = jsonObject.get(Kind2Labels.source).getAsString();
    this.value = jsonObject.get(Kind2Labels.value).getAsString();
    this.logClass = jsonObject.get(Kind2Labels.classField) == null ? null :
        jsonObject.get(Kind2Labels.classField).getAsString();
    this.line = jsonObject.get(Kind2Labels.line) == null ? null :
        jsonObject.get(Kind2Labels.line).getAsString();
    this.column = jsonObject.get(Kind2Labels.column) == null ? null :
        jsonObject.get(Kind2Labels.column).getAsString();
    hideSpecialLogs();
  }

  private void hideSpecialLogs()
  {
    this.isHidden = value.equals("Wallclock timeout.");
  }

  public Kind2LogLevel getLevel()
  {
    return level;
  }

  public String getSource()
  {
    return source;
  }

  public String getValue()
  {
    return value;
  }

  public Kind2Result getKind2Result()
  {
    return kind2Result;
  }

  public String getJson()
  {
    return prettyJson;
  }

  public String getLogClass()
  {
    return logClass;
  }

  public String getLine()
  {
    return line;
  }

  public String getColumn()
  {
    return column;
  }

  @Override
  public String toString()
  {
    return json;
  }

  public boolean isHidden()
  {
    return isHidden;
  }
}
