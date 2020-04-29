package edu.uiowa.kind2;

import com.google.gson.JsonElement;

import java.util.List;

public class Kind2ArrayValue extends Kind2Value
{
    private final List<Kind2Value> values;

    public Kind2ArrayValue(Kind2StepValue kind2StepValue, Kind2Type kind2Type, JsonElement jsonElement, List<Kind2Value> values)
    {
        super(kind2StepValue, kind2Type, jsonElement);
        this.values = values;
    }

    public List<Kind2Value> getValues()
    {
        return values;
    }

    @Override
    public String toString()
    {
        return values.toString();
    }
}
