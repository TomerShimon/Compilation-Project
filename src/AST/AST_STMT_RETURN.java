package AST;
import TYPES.*;
import SYMBOL_TABLE.*;
import TEMP.*;
import IR.*;

public class AST_STMT_RETURN extends AST_STMT
{

	public AST_EXP exp;
	TYPE_FUNCTION function_type;


	/*******************/
	/*  CONSTRUCTOR(S) */
	/*******************/
	public AST_STMT_RETURN(AST_EXP exp, int line)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		if (exp!=null) System.out.print("====================== stmt -> RETURN exp SEMICOLON\n");
		else System.out.print("====================== stmt -> RETURN SEMICOLON\n");

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.exp = exp;
		this.line = line;
	}

	/*********************************************************/
	/* The printing message for an assign statement AST node */
	/*********************************************************/
	public void PrintMe()
	{
		/********************************************/
		/* AST NODE TYPE = AST ASSIGNMENT STATEMENT */
		/********************************************/
		if(exp != null)
			System.out.print("AST NODE RETURN EXP STMT\n");
		else
			System.out.print("AST NODE RETURN STMT\n");

		/***********************************/
		/* RECURSIVELY PRINT EXP... */
		/***********************************/
		if(exp != null)
			exp.PrintMe();

		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		if (exp != null)
			AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			"RETURN\nEXP\n");
		else
			AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			"RETURN\n");
		
		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (exp != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,exp.SerialNumber);
	}

	public TYPE SemantMe(TYPE returnType) throws Exception
	{	

		function_type = getFuncType(SYMBOL_TABLE.getInstance().top);
	
		if(exp == null)
		{
			if(!returnType.name.equals("void"))
			{
				System.out.format(">> ERROR return type is void\n");
				throw new SEMANTIC_ERROR_EXCEPTION(String.valueOf(this.line));			
			}
			return null;		
		}
		if(exp != null && returnType.name.equals("void"))
		{
			System.out.format(">> ERROR return type is void\n");
			throw new SEMANTIC_ERROR_EXCEPTION(String.valueOf(this.line));			
		
		}
		TYPE exp_type = exp.SemantMe();
		if(!exp_type.isinstanceof(returnType)) 
		{
			System.out.format(">> ERROR incompatible return types\n");
			throw new SEMANTIC_ERROR_EXCEPTION(String.valueOf(this.line));			
		}
		return null;
	}

	public TYPE SemantMe(TYPE returnType, TYPE_CLASS type_class) throws Exception
	{
		function_type = getFuncType(SYMBOL_TABLE.getInstance().top);
	
		
		if(exp == null)
		{
			if(!returnType.name.equals("void"))
			{
				System.out.format(">> ERROR return type is void\n");
				throw new SEMANTIC_ERROR_EXCEPTION(String.valueOf(this.line));			
			}
			return null;		
		}
		if(exp != null && returnType.name.equals("void"))
		{
			System.out.format(">> ERROR return type is void\n");
			throw new SEMANTIC_ERROR_EXCEPTION(String.valueOf(this.line));			
		
		}
		TYPE exp_type = exp.SemantMe(returnType, type_class);
		if(!exp_type.isinstanceof(returnType)) 
		{
			System.out.format(">> ERROR incompatible return types\n");
			throw new SEMANTIC_ERROR_EXCEPTION(String.valueOf(this.line));			
		}
		return null;
	}
	
	
	
	public TYPE_FUNCTION getFuncType(SYMBOL_TABLE_ENTRY entry){
		while (!entry.type.isFunc()) {
			entry = entry.prevtop;
		}
		return (TYPE_FUNCTION) entry.type;
	}
	
	
	
	public TEMP IRme() {
		if (function_type.name.equals("main")) {
			IR.getInstance().Add_IRcommand(new IRcommand_Exit());
		}
		
		
		if (exp == null) { 
			IR.getInstance().Add_IRcommand(new IRcommand_Func_Epilogue());
		}
		else {
			TEMP ret = exp.IRme();
			IR.getInstance().Add_IRcommand(new IRcommand_Return(ret));
			IR.getInstance().Add_IRcommand(new IRcommand_Func_Epilogue());
		}
		return null;
	}
}
