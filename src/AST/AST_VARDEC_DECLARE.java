package AST;
import TYPES.*;
import SYMBOL_TABLE.*;
import IR.*;
import VAR.VAR;
import TEMP.*;
	

public class AST_VARDEC_DECLARE extends AST_VARDEC
{
	public AST_TYPE type;
	public String id;
	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_VARDEC_DECLARE(AST_TYPE type,String id, int line)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.print("====================== varDec -> type ID SEMICOLON\n");

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.type = type;
		this.id = id;
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
		System.out.print("AST NODE VARDEC DECLARE\n");

		/**********************************************/
		/* RECURSIVELY PRINT ... */
		/**********************************************/
		if (type != null) type.PrintMe();

		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			String.format("varDec Declare\n(%s)\n",id));
		
		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (type != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,type.SerialNumber);

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
		/* [2] Check That Name does NOT exist */
		/**************************************/
		if (SYMBOL_TABLE.getInstance().find_in_scope(id) != null)
		{
			System.out.format(">> ERROR variable %s already exists in scope\n",id);	
			throw new SEMANTIC_ERROR_EXCEPTION(String.valueOf(this.line));		
		}

		/***************************************************/
		/* [3] Enter the Function Type to the Symbol Table */
		/***************************************************/
		symbol_table_entry = SYMBOL_TABLE.getInstance().enter(id,t); 
		symbol_table_entry.offset = -1 * AST_FUNCDEC.local_var_counter * 4; 


		/*********************************************************/
		/* [4] Return value is irrelevant for class declarations */
		/*********************************************************/
		return null;		
	}




	public TYPE_CLASS_MEMBER SemantMe(TYPE_CLASS type_class) throws Exception
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

		id_type = type_class.find_member(id);
		if(id_type != null)
		{
			if(!(id_type.name.equals(this.type.type_name)))
			{
				System.out.format(">> ERROR invalid shadowing, non matching types %s\n",id);	
				throw new SEMANTIC_ERROR_EXCEPTION(String.valueOf(this.line));		
			}
		}
		
		return new TYPE_CLASS_MEMBER(id,t);
	}




	public TYPE SemantMe(TYPE returnType, TYPE_CLASS type_class) throws Exception
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

		id_type = type_class.find_member(id);
		if(id_type != null)
		{
			if(!(id_type.name.equals(this.type.type_name)))
			{
				System.out.format(">> ERROR invalid shadowing, non matching types %s\n",id);	
				throw new SEMANTIC_ERROR_EXCEPTION(String.valueOf(this.line));		
			}
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
			}
			else {
			}
			
		}	
		
		return null;
	}	
	

}
