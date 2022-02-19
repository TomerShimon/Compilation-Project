package AST;
import TYPES.*;
import SYMBOL_TABLE.*;
import TEMP.*;
import IR.*;


public class AST_ARRAY_TYPEDEF extends AST_Node
{
	public AST_TYPE type;
	public String id;
	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_ARRAY_TYPEDEF(AST_TYPE type,String id, int line)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.print("====================== arrayTypedef -> ARRAY ID EQ type LBRACK RBRACK SEMICOLON\n");

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
		System.out.print("AST NODE ARRAY_TYPEDEF\n");

		/**********************************************/
		/* RECURSIVELY PRINT ... */
		/**********************************************/
		if (type != null) type.PrintMe();
		System.out.format("arrayTypedef(%s)\n",id);


		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			String.format("arrayTypedef(%s)\n", id));
		
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
		if(this.type.isId())
		{
			if(!(t.isClass() || t.isArray()))
			{
				System.out.format(">> ERROR invalid type for array %s\n",this.type.type_name);	
				throw new SEMANTIC_ERROR_EXCEPTION(String.valueOf(this.line));			
			}
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
		/* [3] Enter the ARRAY Type to the Symbol Table    */
		/***************************************************/
		TYPE_ARRAY array_ = new TYPE_ARRAY(id,t);
		SYMBOL_TABLE.getInstance().enter(id,array_);

		/*********************************************************/
		/* [4] Return value is irrelevant for class declarations */
		/*********************************************************/
		return null;	


	}
	
	
	public TEMP IRme() {
		return null;
	}
	
}


