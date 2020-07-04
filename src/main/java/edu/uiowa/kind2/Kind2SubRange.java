/*
 * Copyright (C) 2019  The University of Iowa
 * @author Mudathir Mohamed
 */


package edu.uiowa.kind2;

/**
 * Kind2 type for subranges.
 */
public class Kind2SubRange extends Kind2Type
{
  /**
   * the min value of the subrange.
   */
  private final int min;
  /**
   * the max value of the subrange.
   */
  private final int max;

  public Kind2SubRange(int min, int max)
  {
    super("subrange");
    this.min = min;
    this.max = max;
  }

  /**
   * @return the min value of the subrange.
   */
  public int getMin()
  {
    return min;
  }

  /**
   * @return the maximum value of the subrange.
   */
  public int getMax()
  {
    return max;
  }
}
