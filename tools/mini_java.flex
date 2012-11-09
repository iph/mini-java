package tools;
import java.util.*;
import java_cup.runtime.Symbol;

%%
%state DATA_STATE
%state TEXT_STATE
%class JavaLex
%cup
%implements sym
%line
%public
%column
%ignorecase
%eofclose
%{
  
%}


/*       MACROS!!!!!    */

whitespace = \t|\r|\n|\r\n|" "
id = [a-zA-Z_][a-zA-Z0-9_]*
int =  (0 | [1-9][0-9]*)
comment = ("//"~\n)|("/*"~"*/")
%% 
{comment}               {}
"extends"               {return new Symbol(EXTENDS, yyline+1, yycolumn+1);}
"!"						{return new Symbol(NOT, yyline +1, yycolumn + 1);}
"&&"					{return new Symbol(AND, yyline +1, yycolumn + 1);}
"int"                   {return new Symbol(INT, yyline +1, yycolumn + 1);}
"boolean"               {return new Symbol(BOOLEAN, yyline +1, yycolumn + 1);}
"true"                  {return new Symbol(TRUE, yyline +1, yycolumn + 1);}
"false"                 {return new Symbol(FALSE, yyline +1, yycolumn + 1);}
"public"                {return new Symbol(PUBLIC, yyline +1, yycolumn + 1);}
"static"                {return new Symbol(STATIC, yyline +1, yycolumn + 1);}
"void"                  {return new Symbol(VOID, yyline +1, yycolumn + 1);}
"main"                  {return new Symbol(MAIN, yyline +1, yycolumn + 1);}
"if"                    {return new Symbol(IF, yyline +1, yycolumn + 1);}
"String"                {return new Symbol(STRING, yyline +1, yycolumn + 1);}
"while"                 {return new Symbol(WHILE, yyline +1, yycolumn + 1);}
"System"                {return new Symbol(SYSTEM, yyline +1, yycolumn + 1);}
"out"                   {return new Symbol(OUT, yyline +1, yycolumn + 1);}
"println"               {return new Symbol(PRINTLN, yyline +1, yycolumn + 1);}
"class"                 {return new Symbol(CLASS, yyline +1, yycolumn + 1);}
"return"                {return new Symbol(RETURN, yyline +1, yycolumn + 1);}
"new"                   {return new Symbol(NEW, yyline +1, yycolumn + 1);}
"else"                  {return new Symbol(ELSE, yyline +1, yycolumn + 1);}
"length"                {return new Symbol(LENGTH, yyline +1, yycolumn + 1);}
"this"                  {return new Symbol(THIS, yyline +1, yycolumn + 1);}
"{"                     {return new Symbol(LBRACE, yyline +1, yycolumn + 1);}
"}"                     {return new Symbol(RBRACE, yyline +1, yycolumn + 1);}
"("                     {return new Symbol(LPAREN, yyline +1, yycolumn + 1);}
")"                     {return new Symbol(RPAREN, yyline +1, yycolumn + 1);}
"["                     {return new Symbol(LSQBRACKET, yyline +1, yycolumn + 1);}
"]"                     {return new Symbol(RSQBRACKET, yyline +1, yycolumn + 1);}
";"                     {return new Symbol(SEMICOLON, yyline +1, yycolumn + 1);}
","                     {return new Symbol(COMMA, yyline +1, yycolumn + 1);}
"."                     {return new Symbol(PERIOD, yyline +1, yycolumn + 1);}
"="                     {return new Symbol(EQUAL, yyline +1, yycolumn + 1);}
"<"                     {return new Symbol(LESS, yyline + 1, yycolumn + 1, yytext());}
"+"                     {return new Symbol(PLUS, yyline + 1, yycolumn + 1, yytext());}
"-"                     {return new Symbol(MINUS, yyline + 1, yycolumn + 1, yytext());}
"*"                     {return new Symbol(TIMES, yyline + 1, yycolumn + 1, yytext());}

{id}                    {return new Symbol(IDENT, yyline + 1, yycolumn + 1, yytext());}
{int}                   {return new Symbol(INT_LIT, yyline + 1, yycolumn + 1, Integer.parseInt(yytext()));}
{whitespace}            {}

