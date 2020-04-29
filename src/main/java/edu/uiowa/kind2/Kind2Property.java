/*
 * Copyright (C) 2019  The University of Iowa
 * @author Mudathir Mohamed
 */

package edu.uiowa.kind2;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

public class Kind2Property
{
    private final String json;
    private final JsonElement jsonElement;
    private final String jsonName;
    private final String name;
    private final String qualifiedName;
    private final String scope;
    private final String line;
    private final String column;
    private final String trueFor;
    private final Kind2PropertyType source;
    private final Kind2Answer answer;
    private final Kind2CounterExample counterExample;
    private final Kind2Analysis analysis;
    private final Integer kInductionStep;

    public Kind2Property(Kind2Analysis analysis, JsonElement jsonElement)
    {
        this.analysis = analysis;
        this.jsonElement = jsonElement;
        JsonObject jsonObject = jsonElement.getAsJsonObject();
        json = new GsonBuilder().setPrettyPrinting().create().toJson(jsonElement);
        jsonName = jsonObject.get(Kind2Labels.name).getAsString();
        name = jsonName.replaceAll("\\[.*?\\]", "").replaceFirst(".*?\\.", "");
        qualifiedName = analysis.getNodeName() + "." + name;
        scope = jsonObject.get(Kind2Labels.scope) == null ? "" :
                jsonObject.get(Kind2Labels.scope).getAsString();
        line = jsonObject.get(Kind2Labels.line).getAsString();
        column = jsonObject.get(Kind2Labels.column).getAsString();
        source = Kind2PropertyType.getPropertyType(jsonObject.get(Kind2Labels.source).getAsString());
        JsonElement answerJsonObject = jsonObject.get(Kind2Labels.answer);
        answer = Kind2Answer.getAnswer(answerJsonObject.getAsJsonObject().get(Kind2Labels.value).getAsString());
        JsonElement counterExampleElement = jsonObject.get(Kind2Labels.counterExample);
        counterExample = counterExampleElement == null ? null :
                new Kind2CounterExample(this, counterExampleElement);
        trueFor = jsonObject.get(Kind2Labels.trueFor) == null ? null :
                jsonObject.get(Kind2Labels.trueFor).getAsString();
        JsonElement k = jsonObject.get(Kind2Labels.k);
        kInductionStep = k == null ? null : k.getAsInt();
    }

    @Override
    public String toString()
    {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append("The answer for property '" + getOriginalName() + "' ");

        if (Kind2Result.isPrintingLineNumbersEnabled())
        {
            stringBuilder.append("in line " + getOriginalLine() + " ");
            stringBuilder.append("column " + getOriginalColumn() + " ");
        }
        stringBuilder.append("is " + answer + ".");
        if (answer == Kind2Answer.unknown)
        {
            if (trueFor != null)
            {
                stringBuilder.append(String.format(" This property is satisfied for %1s steps.", trueFor));
            }
            if (kInductionStep != null)
            {
                stringBuilder.append(String.format(" K induction step is  %1s.", kInductionStep));
            }
        }
        return stringBuilder.toString();
    }

    public JsonElement getJsonElement()
    {
        return jsonElement;
    }

    public String getJson()
    {
        return json;
    }

    public String getJsonName()
    {
        return jsonName;
    }

    public String getName()
    {
        return name;
    }

    public String getOriginalName()
    {
        String qualifiedName;
        if(source == Kind2PropertyType.assumption)
        {
            qualifiedName = getOriginalScope() + ".";
        }
        else
        {
            qualifiedName = getAnalysis().getNodeResult().getOriginalName() + ".";
        }
        if (getKind2Result() == null)
        {
            qualifiedName += name;
        }
        else
        {
            qualifiedName += getKind2Result().getOriginalName(name);
        }
        return qualifiedName;
    }

    public String getOriginalLine()
    {
        return getKind2Result().getOriginalLine(name, line);
    }

    public String getOriginalColumn()
    {
        return getKind2Result().getOriginalColumn(name, column);
    }

    public Kind2Result getKind2Result()
    {
        return analysis.getKind2Result();
    }

    public String getScope()
    {
        return scope;
    }

    public String getOriginalScope()
    {
        if (analysis.getKind2Result() == null)
        {
            return scope;
        }
        return analysis.getKind2Result().getOriginalName(scope);
    }

    public String getLine()
    {
        return line;
    }

    public String getColumn()
    {
        return column;
    }

    public Kind2Answer getAnswer()
    {
        return answer;
    }

    public Kind2PropertyType getSource()
    {
        return source;
    }

    public Kind2CounterExample getCounterExample()
    {
        return counterExample;
    }

    public String getTrueFor()
    {
        return trueFor;
    }

    public Integer getKInductionStep()
    {
        return kInductionStep;
    }

    public String getQualifiedName()
    {
        return qualifiedName;
    }

    public Kind2Analysis getAnalysis()
    {
        return analysis;
    }
}
