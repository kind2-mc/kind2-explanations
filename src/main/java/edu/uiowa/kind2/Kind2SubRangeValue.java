/*
 * Copyright (C) 2019  The University of Iowa
 * @author Mudathir Mohamed
 */

package edu.uiowa.kind2;

import com.google.gson.JsonElement;

public class Kind2SubRangeValue extends Kind2IntValue
{
    public Kind2SubRangeValue(Kind2StepValue kind2StepValue, Kind2Type kind2Type, JsonElement jsonElement)
    {
        super(kind2StepValue, kind2Type, jsonElement);
    }
}
