package edu.uiowa.kind2;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class Kind2Log
{
    private final Kind2LogLevel level;
    private final String source;
    private final String value;
    private final Kind2Result kind2Result;
    private final String prettyJson;
    private final String json;
    private final String logClass;
    private final String line;
    private final String column;
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
