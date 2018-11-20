This parser is able to parse EBNF (Backus-Naur) grammar rules of any formal language and automatically build a finite state machine out of these rules that can verify any String input of that language and determine the next legal inputs.


Quick Documentation:
*  Parser is your API Interface for inputting backus-naur rules and retrieve a "LanguageVerificationDevice" back.
*  LanguageVerificationDevice is an interface atm only implemented by FiniteStateMachine that allows you to verify language input and determine the next legal inputs

See examples provided within the Main class for further info on how to use this code.


# NOTE:
* I now know a a lot more about theoretical computer science to understand, that this approach will only work for a narrow subset of all formal languages. This project won't be deleted because it still is a part of my past work efforts.
