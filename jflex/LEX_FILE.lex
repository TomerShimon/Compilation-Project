/***************************/
/* FILE NAME: LEX_FILE.lex */
/***************************/

/***************************/
/* AUTHOR: OREN ISH SHALOM */
/***************************/

/*************/
/* USER CODE */
/*************/
   
import java_cup.runtime.*;

/******************************/
/* DOLAR DOLAR - DON'T TOUCH! */
/******************************/
      
%%
   
/************************************/
/* OPTIONS AND DECLARATIONS SECTION */
/************************************/
   
/*****************************************************/ 
/* Lexer is the name of the class JFlex will create. */
/* The code will be written to the file Lexer.java.  */
/*****************************************************/ 
%class Lexer

/********************************************************************/
/* The current line number can be accessed with the variable yyline */
/* and the current column number with the variable yycolumn.        */
/********************************************************************/
%line
%column
    
/*******************************************************************************/
/* Note that this has to be the EXACT smae name of the class the CUP generates */
/*******************************************************************************/
%cupsym TokenNames

/******************************************************************/
/* CUP compatibility mode interfaces with a CUP generated parser. */
/******************************************************************/
%cup
   
/****************/
/* DECLARATIONS */
/****************/
/*****************************************************************************/   
/* Code between %{ and %}, both of which must be at the beginning of a line, */
/* will be copied letter to letter into the Lexer class code.                */
/* Here you declare member variables and functions that are used inside the  */
/* scanner actions.                                                          */  
/*****************************************************************************/   
%{
	/*********************************************************************************/
	/* Create a new java_cup.runtime.Symbol with information about the current token */
	/*********************************************************************************/
	private Symbol symbol(int type)               {return new Symbol(type, yyline, yycolumn);}
	private Symbol symbol(int type, Object value) {return new Symbol(type, yyline, yycolumn, value);}

	/*******************************************/
	/* Enable line number extraction from main */
	/*******************************************/
	public int getLine()    { return yyline + 1; } 
	public int getCharPos() { return yycolumn;   } 
%}

/***********************/
/* MACRO DECALARATIONS */
/***********************/
LineTerminator				= \r|\n|\r\n
WhiteSpace					= {LineTerminator} | [ \t\f]
INTEGER						= 0 | [1-9][0-9]*
INT_ERR						= 0 [0-9][0-9]*
ID								= [a-zA-Z][a-zA-Z0-9]*
LETTER						= [a-zA-Z]
ONE_LINE_COMMENT		= "/""/"[a-zA-Z0-9"(" ")" "[" "]" "{" "}" "?" "!" "+" "-" "*" "/" ";" "." \t\f]* {LineTerminator} 
MULT_LINE_COMMENT		="/""*" ( "*"*[a-zA-Z0-9"(" ")" "[" "]" "{" "}" "?" "!" "+" "-" ";" "." \t\f\r \n \r\n] | [a-zA-Z0-9"(" ")" "[" "]" "{" "}" "?" "!" "+" "-"  "/" ";" "." \t\f\r \n \r\n] )*  "*"+"/"
COMMENT_ERR1				= "/""/"[a-zA-Z0-9"(" ")" "[" "]" "{" "}" "?" "!" "+" "-" "*" "/" ";" "." \t\f]* [^a-zA-Z0-9"(" ")" "[" "]" "{" "}" "?" "!" "+" "-" "*" "/" ";" "." \t\f \r \n \r\n]  .*
COMMENT_ERR2				= "/""*" [^"*/"]*
COMMENT_ERR3				= "/""*" ( "*"*[a-zA-Z0-9"(" ")" "[" "]" "{" "}" "?" "!" "+" "-" ";" "." \t\f\r \n \r\n] | [a-zA-Z0-9"(" ")" "[" "]" "{" "}" "?" "!" "+" "-"  "/" ";" "." \t\f\r \n \r\n] )* [^a-zA-Z0-9"(" ")" "[" "]" "{" "}" "?" "!" "+" "-"  "*" "/" ";" "." \t\f\r \n \r\n]*  "*""/"
COMMENT_ERR				={COMMENT_ERR1}|{COMMENT_ERR2}|{COMMENT_ERR3}
STRING						= \" {LETTER}* \"
STRING_ERR1					= \" [a-zA-Z]* [^a-zA-Z\"][^a-zA-Z\"]* .* 
STRING_ERR2					= \" {LETTER}* 
STRING_ERR					= {STRING_ERR1} | {STRING_ERR2}
ANY							= [^a-zA-Z0-9"(" ")" "[" "]" "{" "}" "?" "!" "+" "-"  "*" "/" ";" "." "=""<"">""," ":=" \t\f\r \n \r\n]*
   
/******************************/
/* DOLAR DOLAR - DON'T TOUCH! */
/******************************/

%%

/************************************************************/
/* LEXER matches regular expressions to actions (Java code) */
/************************************************************/
   
/**************************************************************/
/* YYINITIAL is the state at which the lexer begins scanning. */
/* So these regular expressions will only be matched if the   */
/* scanner is in the start state YYINITIAL.                   */
/**************************************************************/

<YYINITIAL> {

{MULT_LINE_COMMENT}		{ /* just skip what was found, do nothing */}
{ONE_LINE_COMMENT}		{ /* just skip what was found, do nothing */}
"new"							{ return symbol(TokenNames.NEW);}
"if"								{ return symbol(TokenNames.IF);}
"while"							{ return symbol(TokenNames.WHILE);}
"return"						{ return symbol(TokenNames.RETURN);}
"extends"						{ return symbol(TokenNames.EXTENDS);}
"class"							{ return symbol(TokenNames.CLASS);}
"array"							{ return symbol(TokenNames.ARRAY);}
"<"								{ return symbol(TokenNames.LT);}
">"								{ return symbol(TokenNames.GT);}
":="							{ return symbol(TokenNames.ASSIGN);}
"="								{ return symbol(TokenNames.EQ);}
"void"							{ return symbol(TokenNames.TYPE_VOID);}
"int"							{ return symbol(TokenNames.TYPE_INT);}
"string"						{return symbol(TokenNames.TYPE_STRING);}
";"								{ return symbol(TokenNames.SEMICOLON);}
"."								{ return symbol(TokenNames.DOT);}
","								{ return symbol(TokenNames.COMMA);}
"nil"							{ return symbol(TokenNames.NIL);}
"{"								{ return symbol(TokenNames.LBRACE);}
"}"								{ return symbol(TokenNames.RBRACE);}
"["								{ return symbol(TokenNames.LBRACK);}
"]"								{ return symbol(TokenNames.RBRACK);}
"+"								{ return symbol(TokenNames.PLUS);}
"-"								{ return symbol(TokenNames.MINUS);}
"*"								{ return symbol(TokenNames.TIMES);}
"/"								{ return symbol(TokenNames.DIVIDE);}
"("								{ return symbol(TokenNames.LPAREN);}
")"								{ return symbol(TokenNames.RPAREN);}
{STRING}						{return symbol(TokenNames.STRING, new String(yytext()));}
{STRING_ERR}					{return symbol(TokenNames.ERROR);}
{COMMENT_ERR}				{return symbol(TokenNames.ERROR);}
{INTEGER}						{ if(yytext().length() > 5 || Integer.parseInt(yytext()) > 32767) { return symbol(TokenNames.ERROR);}
								return symbol(TokenNames.INT,new Integer(yytext()));}
{INT_ERR}					{return symbol(TokenNames.ERROR);}	

{ID}							{ return symbol(TokenNames.ID ,new String(yytext()));}   
{WhiteSpace}					{ /* just skip what was found, do nothing */ }
{ANY}							{return symbol(TokenNames.ERROR);}
<<EOF>>						{ return symbol(TokenNames.EOF);}
}
