/*
 * Copyright (C) 2019  The University of Iowa
 * @author Mudathir Mohamed
 */

package edu.uiowa.kind2;

/**
 * A pair class 
 * @param <K> the java type of the first element.
 * @param <V> the java type of the second element.
 */
final class Pair<K, V>
{
  private final K key;
  private final V value;

  public Pair(K key, V value)
  {
    this.key = key;
    this.value = value;
  }

  public K getKey()
  {
    return key;
  }

  public V getValue()
  {
    return value;
  }
}