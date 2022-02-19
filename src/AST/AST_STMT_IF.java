package AST;
import TYPES.*;
import SYMBOL_TABLE.*;
import TEMP.*;
import IR.*;

public class AST_STMT_IF extends AST_STMT
{
	public AST_EXP cond;
	public AST_STMT_LIST body;

	/*******************/
	/*  CONSTRUCTOR(S) */
	/*******************/
	public AST_STMT_IF(AST_EXP cond,AST_STMT_LIST body, int line)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.print("====================== stmt -> IF LPAREN exp RPAREN LBRACE stmtList RBRACE\n");

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.cond = cond;
		this.body = body;
		this.line = line;
	}

	/*********************************************************/
	/* The printing message for a method statement AST node */
	/*********************************************************/
	public void PrintMe()
	{
		/********************************************/
		/* AST NODE TYPE = AST IF STATEMENT */
		/********************************************/
		System.out.print("AST NODE IF STMT\n");

		/***********************************/
		/* RECURSIVELY PRINT... */
		/***********************************/
		cond.PrintMe();
		body.PrintMe();

		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			String.format("IF STMT\n"));
		
		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,cond.SerialNumber);
		AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,body.SerialNumber);
	}

	public TYPE SemantMe(TYPE returnType) throws Exception
	{
		TYPE t = cond.SemantMe();
		if(t.name != "int")
		{
			System.out.format(">> ERROR non int condition\n");
			throw new SEMANTIC_ERROR_EXCEPTION(String.valueOf(this.line));			
		}
		SYMBOL_TABLE.getInstance().beginScope();
		body.SemantMe(returnType);
		SYMBOL_TABLE.getInstance().endScope();
		return null;
	}

	public TYPE SemantMe(TYPE returnType, TYPE_CLASS type_class) throws Exception
	{
		TYPE t = cond.SemantMe(returnType, type_class);
		if(t.name != "int")
		{
			System.out.format(">> ERROR non int condition\n");
			throw new SEMANTIC_ERROR_EXCEPTION(String.valueOf(this.line));			
		}
		SYMBOL_TABLE.getInstance().beginScope();
		body.SemantMe(returnType, type_class);
		SYMBOL_TABLE.getInstance().endScope();
		return null;
	}


	public TEMP IRme()
	{
		String label_end = IRcommand.getFreshLabel("end");

		TEMP cond_temp = cond.IRme();

		IR.getInstance().Add_IRcommand(new IRcommand_Jump_If_Eq_To_Zero(cond_temp,label_end));


		body.IRme();

		IR.getInstance().Add_IRcommand(new IRcommand_Label(label_end));
		return null;
	}

	
	
	
}
