package com.backusnaurparser.parser;

import java.util.*;

import com.backusnaurparser.helper.LanguageParseException;
import com.backusnaurparser.helper.SyntaxHelper;

/**
 * SyntaxTree is a tree structure that stores the EBNF Grammar in a more
 * computer-friendly way
 * 
 * @author Dominik Horn
 *
 */
public class SyntaxTree {
  private static final String EBNF_ALTERNATIVE = "|";

  private static final char EBNF_DEFINITION = '=';

  private static final char EBNF_RULE_TERMINATOR = ';';

  private static final char EBNF_OPTIONAL_OPEN = '[';

  private static final char EBNF_OPTIONAL_CLOSE = ']';

  private static final char EBNF_REPEAT_OPEN = '{';

  private static final char EBNF_REPEAT_CLOSE = '}';

  private static final char EBNF_GROUP_OPEN = '(';

  private static final char EBNF_GROUP_CLOSE = ')';

  private static final char EBNF_TERMINAL_MARKER_1 = '"';

  private static final char EBNF_TERMINAL_MARKER_2 = '\'';

  /** Root object of this tree */
  private NonTerminal startObject;

  /** All rules in a dictionary, where <RuleName> is the key */
  private Map<String, String> rules;

  /***
   * See Parser's constructor's documentation for information. Note: This method
   * should not be called directly, use Parser() instead
   * 
   * @param startSymbol
   * @param rules
   */
  public SyntaxTree(String startSymbol, String... rules) {
    this.startObject = null;
    this.rules = null;

    this.parseRules(startSymbol, rules);
  }

  /**
   * See "startObject"'s documentation
   * 
   * @return
   */
  public NonTerminal getStartObject() {
    return this.startObject;
  }

  /**
   * Creates a SyntaxTree from given rules
   * 
   * @param startRuleName
   *          start symbol
   * @param stringRules
   *          collection of all rules in backus naur-syntax that define your
   *          language
   * 
   */
  private void parseRules(String startRuleName, String... stringRules) {
    if (stringRules.length <= 0)
      throw new LanguageParseException("No Rules were given");

    // Sort all rules
    this.getRules(stringRules);

    if (rules.isEmpty())
      throw new LanguageParseException("No Rules could be parsed");

    this.startObject = this.parseRule(rules.get(startRuleName));
  }

  /**
   * Sorts through all rules and puts them into the "rules" dictionary
   * 
   */
  private void getRules(String... stringRules) {
    this.rules = new HashMap<>();

    // Get individual rules
    for (String rule : stringRules) {
      // Syntax verification
      if (!rule.contains("" + EBNF_DEFINITION)
          || rule.split("" + EBNF_DEFINITION).length != 2)
        throw new LanguageParseException("Invalid Rule: " + rule
            + ".\nCount of \"" + EBNF_DEFINITION + "\" does not match 1");

      String[] ruleComponents = rule.split("" + EBNF_DEFINITION);
      this.rules.put(ruleComponents[0].trim(), ruleComponents[1].trim());
    }
  }

  /**
   * Method that parses the rule for this nonTerminal, parsing subrules
   * recursively upon finding them
   * 
   * @param currentSObject
   *          Current object in the syntax tree that this rule corresponds to
   * @param rule
   *          Current rule that needs to be parsed
   * @return NonTerminal object/subtree that holds parsed rule
   */
  private NonTerminal parseRule(String rule) {
    // Validate rule to avoid messing with annoying errors
    if (!isValidRule(rule))
      return null;

    NonTerminal nonTerminal = new NonTerminal(rule);

    // Splits rule into high level components
    List<String> ruleComponents = getRuleComponents(rule);
    for (int i = 0; i < ruleComponents.size(); i++) {
      String currentRuleComponent = ruleComponents.get(i);

      // Which kind of component is this?
      if (currentRuleComponent.startsWith("" + EBNF_GROUP_OPEN)) {
        // Grouping -> simply remove as grouping is covered because this
        // subrule will be one level bellow current level on the AST and thus
        // automatically be grouped correctly
        currentRuleComponent = currentRuleComponent.substring(1,
            currentRuleComponent.length() - 1).trim();
        NonTerminal groupTerminal = parseRule(currentRuleComponent);
        if (i < ruleComponents.size() - 1)
          if (ruleComponents.get(i + 1).equals(EBNF_ALTERNATIVE))
            groupTerminal.setRelationLinear(false);
        nonTerminal.addSubobject(groupTerminal);
      } else if (currentRuleComponent.startsWith("" + EBNF_REPEAT_OPEN)) {
        // Repeat -> set returned object to be a repeating object
        currentRuleComponent = currentRuleComponent.substring(1,
            currentRuleComponent.length() - 1).trim();
        NonTerminal repeatTerminal = parseRule(currentRuleComponent);
        repeatTerminal.setRepeating(true);
        if (i < ruleComponents.size() - 1)
          if (ruleComponents.get(i + 1).equals(EBNF_ALTERNATIVE))
            repeatTerminal.setRelationLinear(false);
        nonTerminal.addSubobject(parseRule(currentRuleComponent));
      } else if (currentRuleComponent.startsWith("" + EBNF_OPTIONAL_OPEN)) {
        // Optional -> Set returned object to be an optional object
        currentRuleComponent = currentRuleComponent.substring(1,
            currentRuleComponent.length() - 1).trim();
        NonTerminal optionalTerminal = parseRule(currentRuleComponent);
        optionalTerminal.setOptional(true);
        if (i < ruleComponents.size() - 1)
          if (ruleComponents.get(i + 1).equals(EBNF_ALTERNATIVE))
            optionalTerminal.setRelationLinear(false);
        nonTerminal.addSubobject(parseRule(currentRuleComponent));
      } else if (!currentRuleComponent.equals(EBNF_ALTERNATIVE)) {
        // Flat component -> Actual parsing
        NonTerminal subTerminal = null;

        // Terminal or non terminal?
        if (!(currentRuleComponent.startsWith("" + EBNF_TERMINAL_MARKER_1) || currentRuleComponent
            .startsWith("" + EBNF_TERMINAL_MARKER_2))) {
          // find and parse new rule
          if (!rules.containsKey(currentRuleComponent))
            throw new LanguageParseException("Could not find subrule \""
                + currentRuleComponent + "\"");

          subTerminal = parseRule(rules.get(currentRuleComponent));
        } else {
          subTerminal = new NonTerminal(currentRuleComponent);
          subTerminal.setTerminal(currentRuleComponent.substring(1,
              currentRuleComponent.length() - 1));
        }

        if (i < ruleComponents.size() - 1)
          if (ruleComponents.get(i + 1).equals(EBNF_ALTERNATIVE))
            subTerminal.setRelationLinear(false);
      }
    }

    return nonTerminal;
  }

  private boolean isValidRule(String rule) {
    if (countMatches(rule, EBNF_GROUP_OPEN) != countMatches(rule,
        EBNF_GROUP_CLOSE))
      return false;
    if (countMatches(rule, EBNF_OPTIONAL_OPEN) != countMatches(rule,
        EBNF_OPTIONAL_CLOSE))
      return false;
    if (countMatches(rule, EBNF_REPEAT_OPEN) != countMatches(rule,
        EBNF_REPEAT_CLOSE))
      return false;
    if (countMatches(rule, EBNF_TERMINAL_MARKER_1) % 2 != 0)
      return false;
    if (countMatches(rule, EBNF_TERMINAL_MARKER_2) % 2 != 0)
      return false;
    if (countMatches(rule, EBNF_RULE_TERMINATOR) > 1)
      return false;

    return true;
  }

  private int countMatches(String string, char toMatch) {
    int matchCount = 0;
    for (int i = 0; i < string.length(); i++)
      if (string.charAt(i) == toMatch)
        matchCount++;

    return matchCount;
  }

  /**
   * Splits rule into logical components
   * 
   * @param rule
   * @return
   */
  private ArrayList<String> getRuleComponents(String rule) {
    ArrayList<String> ruleComponents = new ArrayList<>();
    int lastHitIndex = 0;
    for (int i = 0; i < rule.length(); i++) {
      switch (rule.charAt(i)) {
        case EBNF_TERMINAL_MARKER_1:
        case EBNF_TERMINAL_MARKER_2:
        case EBNF_GROUP_OPEN:
        case EBNF_OPTIONAL_OPEN:
        case EBNF_REPEAT_OPEN:
          // Find matching closing
          for (int j = i + 1; j < rule.length(); j++) {
            if (rule.charAt(j) == getMatchingClosing(rule.charAt(i))) {
              // Matching found, add to ruleComponents and call quits
              ruleComponents.add(rule.substring(i, j + 1).trim());
              i = j + 1;
              lastHitIndex = i;
              break;
            }
          }

          break;
        case ' ':
          ruleComponents.add(rule.substring(lastHitIndex, i).trim());
          lastHitIndex = i + 1;
        default:
          break;
      }
    }

    return ruleComponents;
  }

  private char getMatchingClosing(char opening) {
    switch (opening) {
      case EBNF_TERMINAL_MARKER_1:
        return EBNF_TERMINAL_MARKER_1;
      case EBNF_TERMINAL_MARKER_2:
        return EBNF_TERMINAL_MARKER_2;
      case EBNF_GROUP_OPEN:
        return EBNF_GROUP_CLOSE;
      case EBNF_OPTIONAL_OPEN:
        return EBNF_OPTIONAL_CLOSE;
      case EBNF_REPEAT_OPEN:
        return EBNF_REPEAT_CLOSE;
      default:
        return '\0';
    }
  }

  /**
   * Retrieves amount of NonTerminal Objects in this Graph
   * 
   * @return
   */
  public int getNonTerminalCount() {
    return this.startObject.getNonTerminalCount();
  }

  /**
   * Don't!!!! Touch this method as it is needed for flattenGraph()
   */
  @Override
  public String toString() {
    return this.startObject.toString();
  }
}
