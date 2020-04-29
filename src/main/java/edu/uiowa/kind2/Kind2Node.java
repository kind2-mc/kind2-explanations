/*
 * Copyright (C) 2019  The University of Iowa
 * @author Mudathir Mohamed
 */

package edu.uiowa.kind2;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;

import java.util.ArrayList;
import java.util.List;

public class Kind2Node
{
    private final String name;
    private final String blockType;
    private final List<Kind2Stream> streams;
    private final List<Kind2Node> subNodes;
    private final String json;
    private final Kind2CounterExample counterExample;

    public Kind2Node(Kind2CounterExample counterExample, JsonElement jsonElement)
    {
        this.counterExample = counterExample;
        json = new GsonBuilder().setPrettyPrinting().create().toJson(jsonElement);

        if (jsonElement.isJsonArray())
        {
            jsonElement = jsonElement.getAsJsonArray().get(0);
        }

        blockType = jsonElement.getAsJsonObject().get(Kind2Labels.blockType).getAsString();
        name = jsonElement.getAsJsonObject().get(Kind2Labels.name).getAsString();

        streams = new ArrayList<>();
        JsonElement streamElements = jsonElement.getAsJsonObject().get(Kind2Labels.streams);

        if (streamElements != null)
        {
            if (streamElements.isJsonArray())
            {
                for (JsonElement element : streamElements.getAsJsonArray())
                {
                    Kind2Stream stream = new Kind2Stream(this, element);
                    streams.add(stream);
                }
            }
            else
            {
                Kind2Stream stream = new Kind2Stream(this, streamElements);
                streams.add(stream);
            }
        }
        subNodes = new ArrayList<>();
        JsonElement subNodeElements = jsonElement.getAsJsonObject().get(Kind2Labels.subNodes);

        if (subNodeElements != null)
        {
            if (subNodeElements.isJsonArray())
            {
                for (JsonElement element : subNodeElements.getAsJsonArray())
                {
                    Kind2Node node = new Kind2Node(counterExample, element);
                    subNodes.add(node);
                }
            }
            else
            {
                Kind2Node node = new Kind2Node(counterExample, subNodeElements);
                subNodes.add(node);
            }
        }
    }

    public String getName()
    {
        return name;
    }

    public String getOriginalName()
    {
        if (getKind2Result() == null)
        {
            return name;
        }
        return getKind2Result().getOriginalName(name);
    }

    public Kind2Result getKind2Result()
    {
        return counterExample.getKind2Result();
    }

    public List<Kind2Stream> getStreams()
    {
        return streams;
    }

    public List<Kind2Node> getSubNodes()
    {
        return subNodes;
    }

    public String getBlockType()
    {
        return blockType;
    }

    @Override
    public String toString()
    {
        return print(getMaxNameLength(), getMaxValueLength());
    }

    public String getJson()
    {
        return json;
    }

    public int getMaxNameLength()
    {
        int maxLength = 0;
        for (Kind2Stream stream : streams)
        {
            if (stream.getOriginalName().length() > maxLength)
            {
                maxLength = stream.getOriginalName().length();
            }
        }

        // visit sub nodes
        for (Kind2Node node : subNodes)
        {
            int subNodeMaxLength = node.getMaxNameLength();
            if (subNodeMaxLength > maxLength)
            {
                maxLength = subNodeMaxLength;
            }
        }

        return maxLength;
    }


    public int getMaxValueLength()
    {
        int maxLength = 0;
        for (Kind2Stream stream : streams)
        {
            for (Kind2StepValue value : stream.getStepValues())
            {
                if (value.toString().length() > maxLength)
                {
                    maxLength = value.toString().length();
                }
            }
        }

        // visit sub nodes
        for (Kind2Node node : subNodes)
        {
            int subNodeMaxLength = node.getMaxValueLength();
            if (subNodeMaxLength > maxLength)
            {
                maxLength = subNodeMaxLength;
            }
        }

        return maxLength;
    }

    public String print(int maxNameLength, int maxValueLength)
    {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("\n  Node " + getOriginalName() + ":\n");
        stringBuilder.append("    == Inputs ==");
        // first print the time
        for (Kind2Stream stream : streams)
        {
            if (stream.getStreamClass().equals(Kind2Labels.input))
            {
                printStream(maxNameLength, maxValueLength, stringBuilder, stream);
            }
        }
        stringBuilder.append("\n    == Outputs ==");

        for (Kind2Stream stream : streams)
        {
            if (stream.getStreamClass().equals(Kind2Labels.output))
            {
                printStream(maxNameLength, maxValueLength, stringBuilder, stream);
            }
        }

        stringBuilder.append("\n    == Locals ==");
        for (Kind2Stream stream : streams)
        {
            if (stream.getStreamClass().equals(Kind2Labels.local))
            {
                printStream(maxNameLength, maxValueLength, stringBuilder, stream);
            }
        }

        // visit sub nodes
        for (Kind2Node node : subNodes)
        {
            stringBuilder.append(node.print(maxNameLength, maxValueLength));
        }

        return stringBuilder.toString();
    }

    private void printStream(int maxNameLength, int maxValueLength, StringBuilder stringBuilder, Kind2Stream stream)
    {
        String streamName = String.format("%-" + maxNameLength + "s", stream.getOriginalName());
        stringBuilder.append("\n    " + streamName + "\t");
        for (Kind2StepValue value : stream.getStepValues())
        {
            String paddedValue = String.format("%1$" + maxValueLength + "s", value.print());
            stringBuilder.append(paddedValue + "\t");
        }
    }
}
