/*
 * Copyright (C) 2019  The University of Iowa
 * @author Mudathir Mohamed
 */


package edu.uiowa.kind2;

public enum Kind2SuggestionType
{
  noActionRequired, // suggestion 1
  strengthenSubComponentContract, // suggestion 2
  completeSpecificationOrRemoveComponent, // suggestion 3
  makeWeakerOrFixDefinition, // suggestion 4
  makeAssumptionStrongerOrFixDefinition, // suggestion 5
  fixSubComponentIssues, // suggestion 6
  fixOneModeActive, // suggestion 7
  increaseTimeout // for unknown properties
}
