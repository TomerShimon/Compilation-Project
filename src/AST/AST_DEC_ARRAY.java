package AST;
import TYPES.*;
import SYMBOL_TABLE.*;
import TEMP.*;
import IR.*;

public class AST_DEC_ARRAY extends AST_DEC
{
	/****************/
	/* DATA MEMBERS */
	/****************/
	public AST_ARRAY_TYPEDEF arrayTypedef;

	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_DEC_ARRAY(AST_ARRAY_TYPEDEF arrayTypedef, int line)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		if (arrayTypedef != null) System.out.print("====================== dec -> dec_arrayTypedef\n");

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.arrayTypedef = arrayTypedef;
		this.line = line;
	}

	/******************************************************/
	/* The printing message for a statement list AST node */
	/******************************************************/
	public void PrintMe()
	{
		/**************************************/
		/* AST NODE TYPE = AST STATEMENT LIST */
		/**************************************/
		System.out.print("AST NODE DEC ARRAY\n");

		/*************************************/
		/* RECURSIVELY PRINT ... */
		/*************************************/
		arrayTypedef.PrintMe();

		/**********************************/
		/* PRINT to AST GRAPHVIZ DOT file */
		/**********************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			"dec_array\n");
		
		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,arrayTypedef.SerialNumber);

	}

	public TYPE SemantMe() throws Exception
	{
		arrayTypedef.SemantMe();
		return null;
	}
	
	public TEMP IRme()
	{
		return arrayTypedef.IRme();
	}
		
}
