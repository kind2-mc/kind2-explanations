/*
 * Copyright (C) 2019  The University of Iowa
 * @author Mudathir Mohamed
 */

package edu.uiowa.kind2;

import java.util.*;

public class Kind2Mapping
{
    private final Map<String, Kind2IdentifierMapping> namesMap;
    private final Map<String, Kind2EnumMapping> enumsMap;
    private Set<String> unmappedNames = new HashSet<>();

    public Kind2Mapping()
    {
        this.namesMap = new HashMap<>();
        this.enumsMap = new HashMap<>();
    }

    public Kind2Mapping(List<Kind2IdentifierMapping> identifierMappings)
    {
        this.namesMap = getNamesMap(identifierMappings);
        this.enumsMap = new HashMap<>();
    }

    public Kind2Mapping(List<Kind2IdentifierMapping> identifierMappings, List<Kind2EnumMapping> enumsMap)
    {
        this.namesMap = getNamesMap(identifierMappings);
        this.enumsMap = getEnumsMap(enumsMap);
    }

    private Map<String, Kind2IdentifierMapping> getNamesMap(List<Kind2IdentifierMapping> mappings)
    {
        Map<String, Kind2IdentifierMapping> map = new HashMap<>();
        for (Kind2IdentifierMapping mapping : mappings)
        {
            map.put(mapping.getLustreName(), mapping);
        }
        return map;
    }

    private Map<String, Kind2EnumMapping> getEnumsMap(List<Kind2EnumMapping> mappings)
    {
        Map<String, Kind2EnumMapping> map = new HashMap<>();
        for (Kind2EnumMapping mapping : mappings)
        {
            map.put(mapping.getLustreName(), mapping);
        }
        return map;
    }

    public void addIdentifierMapping(Kind2IdentifierMapping identifierMapping)
    {
        this.namesMap.put(identifierMapping.getLustreName(), identifierMapping);
    }

    public void addEnumMapping(Kind2EnumMapping enumMapping)
    {
        this.enumsMap.put(enumMapping.getLustreName(), enumMapping);
    }

    private String getOriginalEnumName(String lustreEnumName, String lustreEnumValue)
    {
        if (enumsMap.containsKey(lustreEnumName))
        {
            Kind2EnumMapping enumMapping = enumsMap.get(lustreEnumName);
            Map<String, Kind2IdentifierMapping> enumValues = enumMapping.getEnumValues();
            if (enumValues.containsKey(lustreEnumValue))
            {
                return enumValues.get(lustreEnumValue).getOriginalName();
            }
        }
        return getOriginalName(lustreEnumValue);
    }

    public String getOriginalName(String kind2Name)
    {
        String printedName;
        if (Kind2Result.isPrintingOriginalNameEnabled() && namesMap.containsKey(kind2Name))
        {
            printedName = namesMap.get(kind2Name).getOriginalName();
        }
        else
        {
            unmappedNames.add(kind2Name);
            printedName = kind2Name;
        }

        printedName = Kind2Result.getOpeningSymbols() + printedName + Kind2Result.getClosingSymbols();
        return printedName;
    }

    public String getOriginalLine(String kind2Name, String line)
    {
        if (namesMap.containsKey(kind2Name))
        {
            String originalLine = namesMap.get(kind2Name).getOriginalLine();
            if (originalLine == null || originalLine.isEmpty())
            {
                return line;
            }
            else
            {
                return originalLine;
            }
        }
        return line;
    }

    public String getOriginalColumn(String kind2Name, String column)
    {
        if (namesMap.containsKey(kind2Name))
        {
            String originalColumn = namesMap.get(kind2Name).getOriginalColumn();
            if (originalColumn == null || originalColumn.isEmpty())
            {
                return column;
            }
            else
            {
                return originalColumn;
            }
        }
        return column;
    }

    public Map<String, Kind2EnumMapping> getEnumsMap()
    {
        return enumsMap;
    }

    public Map<String, Kind2IdentifierMapping> getNamesMap()
    {
        return namesMap;
    }

    public Set<String> getUnmappedNames()
    {
        return unmappedNames;
    }
}
