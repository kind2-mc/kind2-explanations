/*
 * Copyright (C) 2019  The University of Iowa
 * @author Mudathir Mohamed
 */

package edu.uiowa.kind2;

public class Kind2Array extends Kind2Type
{
    private final Kind2Type elementType;

    public Kind2Array(Kind2Type elementType)
    {
        super("array of " + elementType.toString());
        this.elementType = elementType;
    }

    public Kind2Type getElementType()
    {
        return elementType;
    }
}
