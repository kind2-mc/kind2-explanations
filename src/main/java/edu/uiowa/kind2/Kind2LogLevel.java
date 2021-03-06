/*
 * Copyright (C) 2019  The University of Iowa
 * @author Mudathir Mohamed
 */

package edu.uiowa.kind2;

public enum Kind2LogLevel
{
  off("off"), // -1: off disables all messages
  fatal("fatal"), // 0: most severe
  error("error"), // 1
  warn("warn"), // 2
  note("note"), // 3
  info("info"), // 4
  debug("debug"), // 5
  trace("trace"); // 6: least severe

  private final String value;

  Kind2LogLevel(String value)
  {
    this.value = value;
  }

  public static Kind2LogLevel getLevel(String level)
  {
    switch (level)
    {
      case "warn":
        return warn;
      case "fatal":
        return fatal;
      case "error":
        return error;
      case "info":
        return info;
      case "note":
        return note;
      case "off":
        return off;
      case "debug":
        return debug;
      case "trace":
        return trace;

      default:
        throw new UnsupportedOperationException("Log " + level + " is not defined");
    }
  }

  @Override
  public String toString()
  {
    return this.value;
  }
}
