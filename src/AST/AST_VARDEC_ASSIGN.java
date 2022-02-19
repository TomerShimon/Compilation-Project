package AST;
import TYPES.*;
import SYMBOL_TABLE.*;
import IR.*;
import VAR.*;
import TEMP.*;

public class AST_VARDEC_ASSIGN extends AST_VARDEC
{
	public AST_TYPE type;
	public String id;
	public AST_EXP exp;
	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_VARDEC_ASSIGN(AST_TYPE type,String id, AST_EXP exp, int line)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.print("====================== varDec -> type ID ASSIGN exp\n");

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.type = type;
		this.id = id;
		this.exp = exp;
		this.line = line;
	}
	
	/*************************************************/
	/* The printing message for a varDec assign AST node */
	/*************************************************/
	public void PrintMe()
	{
		/*********************************/
		/* AST NODE TYPE = varDec assign */
		/*********************************/
		System.out.print("AST NODE VARDEC ASSIGN\n");

		/**********************************************/
		/* RECURSIVELY PRINT ... */
		/**********************************************/
		if (type != null) type.PrintMe();
		System.out.format("ID( %s )\n",id);
		if (exp != null) exp.PrintMe();


		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			String.format("varDec assign\n"));
		
		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (type != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,type.SerialNumber);
		if (exp != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,exp.SerialNumber);

	}
	
	
	private int get_number_of_local_func_or_method_var(){
		SYMBOL_TABLE_ENTRY entry = symbol_table_entry;
		int number_of_vars = 0;
		while (!entry.type.isFunc()){
			entry = entry.prevtop;
			number_of_vars++;
		}
		number_of_vars--; 
		
		
		TYPE_FUNCTION function_type = (TYPE_FUNCTION) entry.type;
		TYPE_LIST params = function_type.params;
		if (params == null || params.head == null) {
			return number_of_vars;
		}

		return number_of_vars - params.get_list_length();


	}

	
	public TYPE SemantMe() throws Exception
	{
		TYPE t;
		if(this.type.type_name.equals("void"))
		{
			System.out.format(">> ERROR void can only be declared as a return type of a function\n");
			throw new SEMANTIC_ERROR_EXCEPTION(String.valueOf(this.line));
		}
		/****************************/
		/* [1] Check If Type exists */
		/****************************/
		t = SYMBOL_TABLE.getInstance().find(this.type.type_name);
		if (t == null)
		{
			System.out.format(">> ERROR non existing type %s\n",type.type_name);
			throw new SEMANTIC_ERROR_EXCEPTION(String.valueOf(this.line));
		}
		/**************************************/
		/* [2] Check That types match	      */
		/**************************************/
		
		TYPE exp_type = exp.SemantMe();

		if(!exp_type.isinstanceof(t))
		{
			System.out.format(">> ERROR non matching types %s and %s\n",t.name, exp_type.name);
			throw new SEMANTIC_ERROR_EXCEPTION(String.valueOf(this.line));
		}
		/**************************************/
		/* [3] Check That Name does NOT exist */
		/**************************************/
		if (SYMBOL_TABLE.getInstance().find_in_scope(id) != null)
		{
			System.out.format(">> ERROR variable %s already exists in scope\n",id);	
			throw new SEMANTIC_ERROR_EXCEPTION(String.valueOf(this.line));		
		}

		/***************************************************/
		/* [4] Enter the Function Type to the Symbol Table */
		/***************************************************/
		symbol_table_entry = SYMBOL_TABLE.getInstance().enter(id,t); 
		symbol_table_entry.offset = -1 * AST_FUNCDEC.local_var_counter * 4; 
		

		/*********************************************************/
		/* [5] Return value is irrelevant for class declarations */
		/*********************************************************/
		return null;		
	}




	public TYPE_CLASS_MEMBER SemantMe(TYPE_CLASS type_class) throws Exception
	{
		symbol_table_entry = null; 
		
		if(this.type.type_name.equals("void"))
		{
			System.out.format(">> ERROR void can only be declared as a return type of a function\n");
			throw new SEMANTIC_ERROR_EXCEPTION(String.valueOf(this.line));
		}
		TYPE t = type_class.find_member(this.type.type_name);
		if(t == null)
		{
			t = SYMBOL_TABLE.getInstance().find(this.type.type_name);
			if (t == null)
			{
				System.out.format(">> ERROR non existing type %s\n",type.type_name);
				throw new SEMANTIC_ERROR_EXCEPTION(String.valueOf(this.line));
			}
		}

		TYPE id_type = SYMBOL_TABLE.getInstance().find_in_scope(id);
		if(id_type != null)
		{
			System.out.format(">> ERROR variable name already defined in scope %s\n",id);	
			throw new SEMANTIC_ERROR_EXCEPTION(String.valueOf(this.line));	
		}

		id_type = type_class.find_member(id);
		if(id_type != null)
		{
			if(!(id_type.name.equals(this.type.type_name)))
			{
				System.out.format(">> ERROR invalid shadowing, non matching types %s\n",id);	
				throw new SEMANTIC_ERROR_EXCEPTION(String.valueOf(this.line));		
			}
		}

		TYPE exp_type = exp.SemantMe(type_class);

		if(!exp_type.isinstanceof(t))
		{
			System.out.format(">> ERROR non matching types %s and %s\n",t.name, exp_type.name);
			throw new SEMANTIC_ERROR_EXCEPTION(String.valueOf(this.line));
		}
		
		return new TYPE_CLASS_MEMBER(id,t, exp);
	}
	
	
	
	public TYPE_CLASS_MEMBER SemantMe(TYPE returnType, TYPE_CLASS type_class) throws Exception
	{
		if(this.type.type_name.equals("void"))
		{
			System.out.format(">> ERROR void can only be declared as a return type of a function\n");
			throw new SEMANTIC_ERROR_EXCEPTION(String.valueOf(this.line));
		}
		TYPE t = type_class.find_member(this.type.type_name);
		if(t == null)
		{
			t = SYMBOL_TABLE.getInstance().find(this.type.type_name);
			if (t == null)
			{
				System.out.format(">> ERROR non existing type %s\n",type.type_name);
				throw new SEMANTIC_ERROR_EXCEPTION(String.valueOf(this.line));
			}
		}


		TYPE id_type = SYMBOL_TABLE.getInstance().find_in_scope(id);
		if(id_type != null)
		{
			System.out.format(">> ERROR variable name already defined in scope %s\n",id);	
			throw new SEMANTIC_ERROR_EXCEPTION(String.valueOf(this.line));	
		}

		TYPE exp_type = exp.SemantMe(returnType, type_class);

		if(!exp_type.isinstanceof(t))
		{
			System.out.format(">> ERROR non matching types %s and %s\n",t.name, exp_type.name);
			throw new SEMANTIC_ERROR_EXCEPTION(String.valueOf(this.line));
		}

		symbol_table_entry = SYMBOL_TABLE.getInstance().enter(id,t); 
		symbol_table_entry.offset = -1 * AST_FUNCDEC.local_var_counter * 4; 
		type_class.addMember(new TYPE_CLASS_MEMBER(id,t));
		return null;
	}
	

	public TEMP IRme() 
	{	
		if (symbol_table_entry == null) { 			
		}

		else { 
			if (symbol_table_entry.scope == 1) {
					IR.getInstance().Add_IRcommand(new IRcommand_Allocate(id));
					TEMP value = exp.IRme(true);
					IR.getInstance().Add_IRcommandBeforeMain(new IRcommand_Store(id, value));
			}

			else {
				VAR var = new VAR(symbol_table_entry.offset, id, false);
				TEMP value = exp.IRme();
				IR.getInstance().Add_IRcommand(new IRcommand_Set_Var(var, value));
			}
			
		}	
		return null;
	}
}
