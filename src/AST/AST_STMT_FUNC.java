package AST;
import TYPES.*;
import SYMBOL_TABLE.*;
import TEMP.*;
import IR.*;

public class AST_STMT_FUNC extends AST_STMT
{

	public String id;
	public AST_EXP_LIST expList;

	/*******************/
	/*  CONSTRUCTOR(S) */
	/*******************/
	public AST_STMT_FUNC(String id, AST_EXP_LIST expList, int line)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		if (expList!=null) System.out.print("====================== stmt -> ID LPAREN expList RPAREN SEMICOLON\n");
		else System.out.print("====================== stmt -> ID LPAREN RPAREN SEMICOLON\n");

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.expList = expList;
		this.id = id;
		this.line = line;
	}

	/*********************************************************/
	/* The printing message for a method statement AST node */
	/*********************************************************/
	public void PrintMe()
	{
		/********************************************/
		/* AST NODE TYPE = AST METHOD STATEMENT */
		/********************************************/
		System.out.print("AST NODE FUNC STMT\n");

		/***********************************/
		/* RECURSIVELY PRINT... */
		/***********************************/
		if(expList!=null) expList.PrintMe();

		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			String.format("FUNC STMT\n%s\n",id));
		
		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (expList != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,expList.SerialNumber);
	}

	public TYPE SemantMe(TYPE returnType) throws Exception
	{

		TYPE t = SYMBOL_TABLE.getInstance().find(this.id);
		if(t == null)
		{
			System.out.format(">> ERROR non existing type %s\n",this.id);
			throw new SEMANTIC_ERROR_EXCEPTION(String.valueOf(this.line));
		}
		if(!t.isFunc())
		{
			System.out.format(">> ERROR not a function %s\n",this.id);
			throw new SEMANTIC_ERROR_EXCEPTION(String.valueOf(this.line));
		}
		t = (TYPE_FUNCTION) t;
		boolean is_identical;
		if(this.expList == null)
			is_identical = ((TYPE_FUNCTION) t).compare_params(null);
		else
		{
			TYPE t1;
			TYPE_LIST type_list = null;
			for (AST_EXP_LIST it = expList; it  != null; it = it.tail)
			{
				t1 = it.head.SemantMe();
				if (t1 == null)
				{
					System.out.format(">> ERROR non existing type \n");
					throw new SEMANTIC_ERROR_EXCEPTION(String.valueOf(this.line));				
				}
				else
				{
					type_list = new TYPE_LIST(t1,type_list);
				}
			}
			is_identical = ((TYPE_FUNCTION) t).compare_params(type_list);
		}
		if(!is_identical)
		{
			System.out.format("function is: %s\n", t.name);
			System.out.format(">> ERROR incompatible parameters\n");
			throw new SEMANTIC_ERROR_EXCEPTION(String.valueOf(this.line));
		}
		return ((TYPE_FUNCTION) t).returnType;
		
	}

	public TYPE SemantMe(TYPE returnType, TYPE_CLASS type_class) throws Exception
	{
		TYPE t = SYMBOL_TABLE.getInstance().find_in_func(this.id);
		if(t == null)
		{
			t = type_class.find_member(this.id);
			if(t == null)
			{
				t = SYMBOL_TABLE.getInstance().find(this.id);
				if(t == null)
				{
					System.out.format(">> ERROR non existing type %s\n",this.id);
					throw new SEMANTIC_ERROR_EXCEPTION(String.valueOf(this.line));
				}
			}
		}
		if(!t.isFunc())
		{
			System.out.format(">> ERROR not a function %s\n",this.id);
			System.exit(0);
		}
		t = (TYPE_FUNCTION) t;
		boolean is_identical;
		if(this.expList == null)
			is_identical = ((TYPE_FUNCTION) t).compare_params(null);
		else
		{
			TYPE t1;
			TYPE_LIST type_list = null;
			for (AST_EXP_LIST it = expList; it  != null; it = it.tail)
			{
				t1 = it.head.SemantMe();
				if (t1 == null)
				{
					System.out.format(">> ERROR non existing type \n");
					throw new SEMANTIC_ERROR_EXCEPTION(String.valueOf(this.line));				
				}
				else
				{
					type_list = new TYPE_LIST(t1,type_list);
				}
			}
			is_identical = ((TYPE_FUNCTION) t).compare_params(type_list);
		}
		if(!is_identical)
		{
			System.out.format("function is: %s\n", t.name);
			System.out.format(">> ERROR incompatible parameters\n");
			throw new SEMANTIC_ERROR_EXCEPTION(String.valueOf(this.line));
		}
		return ((TYPE_FUNCTION) t).returnType;
		
	}
	
	

	public TEMP IRme()
	{	
		AST_EXP_LIST arg_list = expList;
		int number_of_arguments = 0;
		if ("PrintInt".equals(id)){
			TEMP t = arg_list.head.IRme();	
			IR.getInstance().Add_IRcommand(new IRcommand_PrintInt(t));
			return null;
		}
		if ("PrintString".equals(id)){
			TEMP t = arg_list.head.IRme();	
			IR.getInstance().Add_IRcommand(new IRcommand_PrintStringTEMP(t));
			return null;
		}
		while (arg_list != null){
			TEMP t = arg_list.head.IRme();
			IR.getInstance().Add_IRcommand(new IRcommand_Push(t));
			number_of_arguments++;
			arg_list = arg_list.tail;
		}

		IR.getInstance().Add_IRcommand(new IRcommand_Jal(id));
		IR.getInstance().Add_IRcommand(new IRcommand_Add_SP(number_of_arguments*4));

		TEMP return_value = TEMP_FACTORY.getInstance().getFreshTEMP();
		IR.getInstance().Add_IRcommand(new IRcommand_takeFromV0(return_value));
		return return_value;
	}
}
