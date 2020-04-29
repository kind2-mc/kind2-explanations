package edu.uiowa.kind2;

import com.google.gson.JsonElement;

public class Kind2EnumValue extends Kind2Value
{
    private final String name;

    public Kind2EnumValue(Kind2StepValue kind2StepValue, Kind2Type kind2Type, JsonElement jsonElement)
    {
        super(kind2StepValue, kind2Type, jsonElement);
        name = jsonElement.getAsString().trim();
    }

    public String getName()
    {
        return name;
    }

    public String getOriginalName()
    {
        return getKind2Result().getOriginalName(name);
    }

    @Override
    public String toString()
    {
        return getOriginalName();
    }
}
