This parser is able to parse EBNF (Backus-Naur) grammar rules of any formal language and automatically build a finite state machine out of these rules that can verify any String input of that language and determine the next legal inputs.


Quick Documentation:
*  Parser is your API Interface for inputting backus-naur rules and retrieve a "LanguageVerificationDevice" back.
*  LanguageVerificationDevice is an interface atm only implemented by FiniteStateMachine that allows you to verify language input and determine the next legal inputs

See examples provided within the Main class for further info on how to use this code.



Known Issues:
   * Infinite Recursion can't be parsed (e.g.: Rule = Rule | " "), you must use "{}" repeat to specify your language (e.g. instead of previous example do: Rule = " " { " " })
