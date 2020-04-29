package edu.uiowa.kind2;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Kind2EnumMapping
{
    private final Map<String, Kind2IdentifierMapping> enumValues;
    private final Kind2IdentifierMapping enumName;

    public Kind2EnumMapping(Kind2IdentifierMapping enumName, List<Kind2IdentifierMapping> enumValues)
    {
        this.enumName = enumName;
        this.enumValues = getNamesMap(enumValues);
    }

    private Map<String, Kind2IdentifierMapping> getNamesMap(List<Kind2IdentifierMapping> enumValues)
    {
        Map<String, Kind2IdentifierMapping> map = new HashMap<>();
        for (Kind2IdentifierMapping mapping : enumValues)
        {
            map.put(mapping.getLustreName(), mapping);
        }
        return map;
    }

    public String getLustreName()
    {
        return enumName.getLustreName();
    }

    public String getOriginalName()
    {
        return enumName.getOriginalName();
    }

    public Map<String, Kind2IdentifierMapping> getEnumValues()
    {
        return enumValues;
    }

    public Kind2IdentifierMapping getEnumName()
    {
        return enumName;
    }
}
