package AST;
import TYPES.*;
import SYMBOL_TABLE.*;
import TEMP.*;
import IR.*;
import RA.*;

public class AST_FUNCDEC extends AST_Node
{
	public AST_TYPE type;
	public String name;
	public AST_PARAM_LIST params;
	public AST_STMT_LIST statements;
	
	// fucntion/method local variable counter, also counts variables inside IF/WHILE scopes that are inside the function scope
	// the counter is increased by one for every stmt -> varDec derivation (inside AST_STMT_VARDEC class)
	// someone told me it is better to start counting from 1
	// the counter is used to calculate offset from fucntion/method $fp
	public static int local_var_counter = 11; // i added

	
	public TYPE_CLASS cls; // i added
	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_FUNCDEC(AST_TYPE t,String id, AST_PARAM_LIST params, AST_STMT_LIST stmts, int line)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.print("====================== funcDec -> type ID paramlist stmtList\n");

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.type = t;
		this.name = id;
		this.params = params;
		this.statements = stmts;
		this.line = line;
	}
	
	/*************************************************/
	/* The printing message for a varDec assign AST node */
	/*************************************************/
	public void PrintMe()
	{
		/*********************************/
		/* AST NODE TYPE = funcDec */
		/*********************************/
		System.out.print("AST NODE FUNCDEC\n");

		/**********************************************/
		/* RECURSIVELY PRINT ... */
		/**********************************************/
		if (type != null) type.PrintMe();
		if (params != null) params.PrintMe();
		if (statements != null) statements.PrintMe();


		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			String.format("funcDec\n%s",name));
		
		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (type != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,type.SerialNumber);
		if (params != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,params.SerialNumber);
		if (statements != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,statements.SerialNumber);

	}





	// i think this SemantMe is called only for functions (never for methods)
	public TYPE SemantMe() throws Exception
	{
		local_var_counter = 11; 
		
		
		TYPE t;
		TYPE returnType = null;
		TYPE_LIST type_list = null;

		/*******************/
		/* [0] return type */
		/*******************/
		returnType = SYMBOL_TABLE.getInstance().find(type.type_name);
		if (returnType == null)
		{
			System.out.format(">> ERROR non existing return type %s\n",type.type_name);
			throw new SEMANTIC_ERROR_EXCEPTION(String.valueOf(this.line));			
		}
		t = SYMBOL_TABLE.getInstance().find(name);
		if (t != null)
		{
			System.out.format(">> ERROR variable name already exists %s\n",name);
			throw new SEMANTIC_ERROR_EXCEPTION(String.valueOf(this.line));			
		}
		
		TYPE_FUNCTION func = new TYPE_FUNCTION(returnType,name,null);
		SYMBOL_TABLE.getInstance().enter(name,func);
	
		/****************************/
		/* [1] Begin Function Scope */
		/****************************/
		SYMBOL_TABLE.getInstance().beginScope_func();

		/***************************/
		/* [2] Semant Input Params */
		/***************************/
		for (AST_PARAM_LIST it = params; it  != null; it = it.tail)
		{
			if(it.head.type.type_name.equals("void"))
			{
				System.out.format(">> ERROR void can only be declared as a return type of a function\n");
				throw new SEMANTIC_ERROR_EXCEPTION(String.valueOf(this.line));
			}
			t = SYMBOL_TABLE.getInstance().find(it.head.type.type_name);
			if (t == null)
			{
				System.out.format(">> ERROR non existing type %s\n",it.head.type.type_name);				
				throw new SEMANTIC_ERROR_EXCEPTION(String.valueOf(this.line));
			}
			else
			{
				type_list = new TYPE_LIST(t,type_list);
				it.head.symbol_table_entry = SYMBOL_TABLE.getInstance().enter(it.head.name,t); // I changed.
			}
		}
		func.params = type_list;

		/*******************/
		/* [3] Semant Body */
		/*******************/
		statements.SemantMe(returnType);

		/*****************/
		/* [4] End Scope */
		/*****************/
		SYMBOL_TABLE.getInstance().endScope();

		/***************************************************/
		/* [5] Enter the Function Type to the Symbol Table */
		/***************************************************/

		/*********************************************************/
		/* [6] Return value is irrelevant for func declarations */
		/*********************************************************/
		
		
		local_var_counter = 11; 
		
		
		return null;		
	}
	
	
	
	public TYPE_CLASS_MEMBER SemantMe(TYPE_CLASS type_class) throws Exception
	{
		
		local_var_counter = 11; 
		this.cls = type_class; 
		
		
		TYPE t;
		TYPE prev;
		TYPE_FUNCTION prev_f;
		TYPE returnType = null;
		TYPE_LIST type_list = null;
		returnType = type_class.find_member(type.type_name);
		if (returnType == null)
		{
			returnType = SYMBOL_TABLE.getInstance().find(type.type_name);
			if (returnType == null)
			{
				System.out.format(">> ERROR non existing return type %s\n",type.type_name);
				throw new SEMANTIC_ERROR_EXCEPTION(String.valueOf(this.line));				
			}			
		}

		TYPE_FUNCTION func = new TYPE_FUNCTION(returnType,name,null);
		SYMBOL_TABLE.getInstance().enter(name,func);

		SYMBOL_TABLE.getInstance().beginScope_func();
		for (AST_PARAM_LIST it = params; it  != null; it = it.tail)
		{
			t = SYMBOL_TABLE.getInstance().find(it.head.type.type_name);
			if (t == null)
			{
				System.out.format(">> ERROR non existing type %s\n",it.head.type.type_name);				
				throw new SEMANTIC_ERROR_EXCEPTION(String.valueOf(this.line));
			}
			else
			{
				type_list = new TYPE_LIST(t,type_list);
				it.head.symbol_table_entry = SYMBOL_TABLE.getInstance().enter(it.head.name,t); 
			}
		}
		func.params = type_list;

		prev = type_class.find_member(name);
		if (prev != null)
		{
			if(!prev.isFunc())
			{
				System.out.format(">> ERROR can't override non-func %s\n",name);
				throw new SEMANTIC_ERROR_EXCEPTION(String.valueOf(this.line));
			}
			else 
			{
				prev_f = (TYPE_FUNCTION) prev;
				if(!prev_f.compare_params(type_list))
				{
					System.out.format(">> ERROR can't override, wrong parameters %s\n",name);
					throw new SEMANTIC_ERROR_EXCEPTION(String.valueOf(this.line));
				}
				if(!(returnType.name == prev_f.returnType.name))	
				{
					System.out.format(">> ERROR can't override, wrong return type %s\n",name);
					throw new SEMANTIC_ERROR_EXCEPTION(String.valueOf(this.line));
				}
			}	
		}
		type_class.addMember(new TYPE_CLASS_MEMBER(name,func));
		statements.SemantMe(returnType, type_class);
		SYMBOL_TABLE.getInstance().endScope();
		
		local_var_counter = 11;
		
		return null;
	}
	
	
	
	
	public TEMP IRme()
	{
		if (params!=null) {
			params.IRme();
		}
		IRcommandList function_ir_commands = IR.getInstance().getEndOfListIRCommands();

		if (this.cls == null) {
			IR.getInstance().Add_IRcommand(new IRcommand_Func_Prologue( name, null ));
		}
		else {
			IR.getInstance().Add_IRcommand(new IRcommand_Func_Prologue( name, this.cls.name ));
		}
		
		if (name.equals("main")) {
			IR.getInstance().mainLabel = IR.getInstance().getEndOfListIRCommands();
		}

		statements.IRme();


		if (name.equals("main")) {
			IR.getInstance().Add_IRcommand(new IRcommand_Exit());
		}
		else {
			IR.getInstance().Add_IRcommand(new IRcommand_Func_Epilogue());
		}
		register_allocation.allocate(function_ir_commands);
		return null;
	}


	
}
