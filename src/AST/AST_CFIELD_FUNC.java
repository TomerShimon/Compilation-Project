package AST;
import TYPES.*;
import SYMBOL_TABLE.*;
import TEMP.*;
import IR.*;

public class AST_CFIELD_FUNC extends AST_CFIELD
{
	/************************/
	/* simple variable name */
	/************************/
	public AST_FUNCDEC funcdec;
	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_CFIELD_FUNC(AST_FUNCDEC funcdec, int line)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();
	
		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.format("====================== cField -> funcdec\n");

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.funcdec = funcdec;
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
		System.out.format("AST NODE CFIELD FUNC\n");

		/*****************************/
		/* RECURSIVELY PRINT... */
		/*****************************/
		funcdec.PrintMe();

		/*********************************/
		/* Print to AST GRAPHIZ DOT file */
		/*********************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			String.format("CFIELD\nFUNC\n"));

		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,funcdec.SerialNumber);
	}

	public TYPE_CLASS_MEMBER SemantMe(TYPE_CLASS type_class) throws Exception
	{
		return funcdec.SemantMe(type_class);
	}
	
	
	public TEMP IRme() {
		return funcdec.IRme();
	}
}
