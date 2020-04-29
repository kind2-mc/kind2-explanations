/*
 * Copyright (C) 2019  The University of Iowa
 * @author Mudathir Mohamed
 */

package edu.uiowa.kind2;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;

public class Kind2StepValue
{
    private final String json;
    private final int time;
    private final Kind2Value kind2Value;
    private final Kind2Stream stream;

    public Kind2StepValue(Kind2Stream stream, JsonElement jsonElement)
    {
        this.stream = stream;
        json = new GsonBuilder().setPrettyPrinting().create().toJson(jsonElement);
        time = jsonElement.getAsJsonArray().get(0).getAsInt();
        kind2Value = Kind2Value.getKind2Value(this, getKind2Type(), jsonElement.getAsJsonArray().get(1));
    }

    public Kind2Type getKind2Type()
    {
        return stream.getKind2Type();
    }

    public String getJson()
    {
        return json;
    }

    public Kind2Value getKind2Value()
    {
        return kind2Value;
    }

    public int getTime()
    {
        return time;
    }

    public Kind2Stream getStream()
    {
        return stream;
    }

    public Kind2Result getKind2Result()
    {
        return stream.getKind2Result();
    }

    public String print()
    {
        return kind2Value.toString();
    }

    @Override
    public String toString()
    {
        return kind2Value.toString();
    }
}
