package AST;
import TYPES.*;
import SYMBOL_TABLE.*;
import TEMP.*;
import IR.*;

public class AST_DEC_CLASS extends AST_DEC
{
	/****************/
	/* DATA MEMBERS */
	/****************/
	public AST_CLASSDEC class_dec;

	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_DEC_CLASS(AST_CLASSDEC class_dec, int line)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		if (class_dec != null) System.out.print("====================== dec -> dec_class\n");

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.class_dec = class_dec;
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
		System.out.print("AST NODE DEC_CLASS\n");

		/*************************************/
		/* RECURSIVELY PRINT ... */
		/*************************************/
		class_dec.PrintMe();

		/**********************************/
		/* PRINT to AST GRAPHVIZ DOT file */
		/**********************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			"dec_class\n");
		
		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,class_dec.SerialNumber);

	}
	
	public TYPE SemantMe() throws Exception
	{
		class_dec.SemantMe();
		return null;
	}
	
	
	public TEMP IRme() {
		return class_dec.IRme();
	}
}
