package AST;
import TYPES.*;
import SYMBOL_TABLE.*;
import TEMP.*;
import IR.*;

public class AST_CFIELD_VAR extends AST_CFIELD
{
	/************************/
	/* simple variable name */
	/************************/
	public AST_VARDEC vardec;
	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_CFIELD_VAR(AST_VARDEC vardec, int line)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();
	
		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.format("====================== cField -> varDec\n");

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.vardec = vardec;
		this.line = line;
	}

	/**************************************************/
	/* The printing message for a simple var AST node */
	/**************************************************/
	public void PrintMe()
	{
		/**********************************/
		/* AST NODE TYPE = AST SIMPLE VAR */
		/**********************************/
		System.out.format("AST NODE CFIELD VAR\n");

		/*****************************/
		/* RECURSIVELY PRINT... */
		/*****************************/
		vardec.PrintMe();

		/*********************************/
		/* Print to AST GRAPHIZ DOT file */
		/*********************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			String.format("CFIELD\nVAR\n"));

		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,vardec.SerialNumber);
	}

	public TYPE_CLASS_MEMBER SemantMe(TYPE_CLASS type_class) throws Exception
	{
		return vardec.SemantMe(type_class);
	}
	
	public TEMP IRme() {
		return vardec.IRme();
	}
}
