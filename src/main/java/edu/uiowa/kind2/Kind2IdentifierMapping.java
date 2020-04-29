/*
 * Copyright (C) 2019  The University of Iowa
 * @author Mudathir Mohamed
 */

package edu.uiowa.kind2;

public class Kind2IdentifierMapping
{
    private String originalName;
    private String originalLine;
    private String originalColumn;
    private String lustreName;
    private String lustreLine;
    private String lustreColumn;

    public Kind2IdentifierMapping(String originalName, String lustreName)
    {
        this(originalName, "", "", lustreName, "", "");
    }

    public Kind2IdentifierMapping(String originalName, String originalLine, String originalColumn,
                                  String lustreName)
    {
        this(originalName, originalLine, originalColumn, lustreName, "", "");
    }

    public Kind2IdentifierMapping(String originalName, String originalLine, String originalColumn,
                                  String lustreName, String lustreLine, String lustreColumn)
    {
        this.originalName = originalName;
        this.originalLine = originalLine;
        this.originalColumn = originalColumn;
        this.lustreName = lustreName;
        this.lustreLine = lustreLine;
        this.lustreColumn = lustreColumn;
    }

    public String getOriginalName()
    {
        return originalName;
    }

    public String getOriginalLine()
    {
        return originalLine;
    }

    public String getOriginalColumn()
    {
        return originalColumn;
    }

    public String getLustreName()
    {
        return lustreName;
    }

    public String getLustreLine()
    {
        return lustreLine;
    }

    public String getLustreColumn()
    {
        return lustreColumn;
    }

    public void setOriginalName(String originalName)
    {
        this.originalName = originalName;
    }

    public void setOriginalLine(String originalLine)
    {
        this.originalLine = originalLine;
    }

    public void setOriginalColumn(String originalColumn)
    {
        this.originalColumn = originalColumn;
    }

    public void setLustreName(String lustreName)
    {
        this.lustreName = lustreName;
    }

    public void setLustreLine(String lustreLine)
    {
        this.lustreLine = lustreLine;
    }

    public void setLustreColumn(String lustreColumn)
    {
        this.lustreColumn = lustreColumn;
    }
}
