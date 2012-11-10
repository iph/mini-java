/**
 * Lexer for MiniJava specification
 */
package tools;
import java.util.*;
import java_cup.runtime.Symbol;
%%

%public
%class MiniJavaLexer
%unicode // input should be considered unicode
%cup
%implements sym
%line
%column

%{   
  private void error(String message) {
      System.err.printf("Error on line %d, column %d: %s\n", (yyline+1), (yycolumn+1), message);
  }

  private Symbol token(int type) {
      return symbol(type, new MJToken(yyline, yycolumn));
  }

  private Symbol token(int type, Object value) {
      // wrap the line, col, and value data in a token object that
      // we'll pass to the Symbol class so we won't lose it in the parser
      return symbol(type, new MJToken(yyline, yycolumn, value));
  }

  private Symbol symbol(int type, Object value) {
      return new Symbol(type, yyline, yycolumn, value);
  }
%}


end_of_line   = [\r\n] 
              | \r\n
whitespace    = [\t\f ]
              | {end_of_line}
integer       = 0 
              | [1-9][0-9]*
line_comment  = "//" ~{end_of_line}
block_comment = "/*" ~"*/"
comment       = {line_comment} 
              | {block_comment}
identifier    = [a-zA-Z][a-zA-Z0-9_]*
%%

/* Lexical Rules */
<YYINITIAL> {
  {comment}            {} /* ignore */
  {whitespace}         {} /* ignore */
  "."                  { return token(DOT); }
  ","                  { return token(COMMA); }
  ";"                  { return token(SEMI); }
  "="                  { return token(ASSIGN); }
  "!"                  { return token(NOT); }
  "&&"                 { return token(AND); }
  "<"                  { return token(LESSTHAN); }
  "+"                  { return token(PLUS); }
  "-"                  { return token(MINUS); }
  "*"                  { return token(TIMES); }
  "("                  { return token(LPAREN); }
  ")"                  { return token(RPAREN); }
  "{"                  { return token(LCURLY); }
  "}"                  { return token(RCURLY); }
  "["                  { return token(LBRACKET); }
  "]"                  { return token(RBRACKET); }
  "class"              { return token(CLASS); }
  "public"             { return token(PUBLIC); }
  "static"             { return token(STATIC); }
  "void"               { return token(VOID); }
  "main"               { return token(MAIN); }
  "String"             { return token(STRING); }
  "extends"            { return token(EXTENDS); }
  "return"             { return token(RETURN); }
  "if"                 { return token(IF); }
  "else"               { return token(ELSE); }
  "while"              { return token(WHILE); }
  "System.out.println" { return token(PRINTLN); }
  "length"             { return token(LENGTH); }
  "this"               { return token(THIS); }
  "new"                { return token(NEW); }
  "boolean"            { return token(BOOLEANTYPE); }
  "int"                { return token(INTTYPE); }
  "true"               { return token(BOOLEAN, true); }
  "false"              { return token(BOOLEAN, false); }
  {integer}            { return token(INTEGER, Integer.parseInt(yytext())); }
  {identifier}         { return token(IDENTIFIER, yytext()); }
  .                    { error("Illegal character '" + yytext() + "'"); }
}

