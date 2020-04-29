package edu.uiowa.kind2;

abstract public class Kind2Type
{
    public final String name;

    public Kind2Type(String name)
    {
        this.name = name;
    }

    public static Kind2Type getType(String type)
    {
        switch (type)
        {
            case "bool":
                return new Kind2Bool();
            case "int":
            case "uint8":
            case "uint16":
            case "uint32":
            case "uint64":
            case "int8":
            case "int16":
            case "int32":
            case "int64":
                return new Kind2Int();
            case "real":
                return new Kind2Real();
            default:
            {
                if (type.matches("subrange \\[.*?\\] of int"))
                {
                    return new Kind2SubRange();
                }

                if (type.startsWith("array of"))
                {
                    String elementTypeName = type.replaceFirst("array of", "").trim();
                    Kind2Type elementType = getType(elementTypeName);
                    return new Kind2Array(elementType);
                }

                // the type is enum
                return new Kind2Enum(type);
            }
        }
    }

    @Override
    public String toString()
    {
        return name;
    }
}
