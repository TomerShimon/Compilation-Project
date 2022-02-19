package AST;
import TYPES.*;
import SYMBOL_TABLE.*;
import IR.*;
import VAR.VAR;
import TEMP.*;
	

public class AST_DEC_FUNC extends AST_DEC
{
	/****************/
	/* DATA MEMBERS */
	/****************/
	public AST_FUNCDEC funcdec;

	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_DEC_FUNC(AST_FUNCDEC func_dec, int line)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		if (func_dec != null) System.out.print("====================== dec -> funcdec\n");

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.funcdec = func_dec;
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
		System.out.print("AST NODE DEC_FUNC\n");

		/*************************************/
		/* RECURSIVELY PRINT HEAD + TAIL ... */
		/*************************************/
		funcdec.PrintMe();

		/**********************************/
		/* PRINT to AST GRAPHVIZ DOT file */
		/**********************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			"dec_func\n");
		
		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,funcdec.SerialNumber);

	}

	public TYPE SemantMe() throws Exception
	{
		funcdec.SemantMe();
		return null;
	}
	
	public TEMP IRme()
	{		
		return funcdec.IRme();			
	}
	
}
