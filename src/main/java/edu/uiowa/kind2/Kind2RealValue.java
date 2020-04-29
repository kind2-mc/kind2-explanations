package edu.uiowa.kind2;

import com.google.gson.JsonElement;

import java.math.BigDecimal;

public class Kind2RealValue extends Kind2Value
{
    private final BigDecimal value;

    public Kind2RealValue(Kind2StepValue kind2StepValue, Kind2Type kind2Type, JsonElement jsonElement)
    {
        super(kind2StepValue, kind2Type, jsonElement);
        value = new BigDecimal(jsonElement.getAsString());
    }

    public BigDecimal getValue()
    {
        return value;
    }

    @Override
    public String toString()
    {
        return value.toString();
    }
}
