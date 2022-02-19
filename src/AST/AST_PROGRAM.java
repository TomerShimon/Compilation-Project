package AST;
import TYPES.*;
import SYMBOL_TABLE.*;
import IR.*;
import TEMP.*;

public class AST_PROGRAM extends AST_Node
{
	/****************/
	/* DATA MEMBERS */
	/****************/
	public AST_DEC_LIST decs;

	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_PROGRAM(AST_DEC_LIST declist, int line)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.print("====================== program -> declist\n");

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.decs = declist;
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
		System.out.print("AST NODE program\n");

		/*************************************/
		/* RECURSIVELY PRINT HEAD + TAIL ... */
		/*************************************/
		if (decs != null) decs.PrintMe();

		/**********************************/
		/* PRINT to AST GRAPHVIZ DOT file */
		/**********************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			"program\n");
		
		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (decs != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,decs.SerialNumber);
	}

	public TYPE SemantMe() throws Exception
	{
		decs.SemantMe();
		return null;
	}
	
	
	public TEMP IRme()
	{
		return decs.IRme();		
	}
	
}
