package AST;
import TYPES.*;
import SYMBOL_TABLE.*;
import TEMP.*;
import IR.*;

public class AST_NEW_EXP_DEC extends AST_NEW_EXP
{
	public AST_TYPE type;

	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_NEW_EXP_DEC(AST_TYPE type, int line)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.print("====================== newExp -> NEW type\n");

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.type = type;
		this.line = line;
	}
	
	/***********************************************/
	/* The default message for an exp var AST node */
	/***********************************************/
	public void PrintMe()
	{
		/************************************/
		/* AST NODE TYPE = EXP VAR AST NODE */
		/************************************/
		System.out.print("AST NODE NEW EXP DEC\n");

		/*****************************/
		/* RECURSIVELY PRINT var ... */
		/*****************************/
		if (type != null) type.PrintMe();
		
		/*********************************/
		/* Print to AST GRAPHIZ DOT file */
		/*********************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			"NEW_EXP\nDEC");

		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,type.SerialNumber);
			
	}

	public TYPE SemantMe() throws Exception
	{
		symbol_table_entry = SYMBOL_TABLE.getInstance().findEntry(this.type.type_name); 


		
		TYPE t;
		if(this.type.type_name.equals("void"))
		{
			System.out.format(">> ERROR void can only be declared as a return type of a function\n");
			throw new SEMANTIC_ERROR_EXCEPTION(String.valueOf(this.line));
		}
		t = SYMBOL_TABLE.getInstance().find(this.type.type_name);
		if (t == null)
		{
			System.out.format(">> ERROR non existing type %s\n",type.type_name);
			throw new SEMANTIC_ERROR_EXCEPTION(String.valueOf(this.line));
		}
		if(!t.isClass())
		{
			System.out.format(">> ERROR not a class %s\n",type.type_name);
			throw new SEMANTIC_ERROR_EXCEPTION(String.valueOf(this.line));
		}
		return t;		
	}

	public TYPE SemantMe(TYPE returnType, TYPE_CLASS type_class) throws Exception
	{
		symbol_table_entry = SYMBOL_TABLE.getInstance().findEntry(this.type.type_name); 

		
		
		if(this.type.type_name.equals("void"))
		{
			System.out.format(">> ERROR void can only be declared as a return type of a function\n");
			throw new SEMANTIC_ERROR_EXCEPTION(String.valueOf(this.line));
		}
		TYPE t = SYMBOL_TABLE.getInstance().find_in_func(this.type.type_name);
		if(t == null)
		{
			t = type_class.find_member(this.type.type_name);
			if(t == null)
			{
				t = SYMBOL_TABLE.getInstance().find(this.type.type_name);
				if(t == null)
				{
					System.out.format(">> ERROR non existing type %s\n",this.type.type_name);
					throw new SEMANTIC_ERROR_EXCEPTION(String.valueOf(this.line));
				}
			}
		}
		if(!t.isClass())
		{
			System.out.format(">> ERROR not a class %s\n",type.type_name);
			throw new SEMANTIC_ERROR_EXCEPTION(String.valueOf(this.line));
		}
		return t;		
	}
	
	
	

	public TEMP IRme() {
		TEMP instance_pointer = TEMP_FACTORY.getInstance().getFreshTEMP();
		TEMP bytes_to_allocate = TEMP_FACTORY.getInstance().getFreshTEMP();		
		
		TYPE_CLASS cls = ((TYPE_CLASS) symbol_table_entry.type);
		
		int NumOfFields = cls.getNumOfFields();
		int sizeOfObject = (NumOfFields + 1) * 4;
		IR.getInstance().Add_IRcommand(new IRcommandConstInt(bytes_to_allocate, sizeOfObject));
		IR.getInstance().Add_IRcommand(new IRcommand_Mallocate(instance_pointer, bytes_to_allocate));
		TEMP vt_address = TEMP_FACTORY.getInstance().getFreshTEMP();
		IR.getInstance().Add_IRcommand(new IRcommand_Load_Virtual_Table_Address(vt_address, cls.name));
		IR.getInstance().Add_IRcommand(new IRcommand_Store_Register(vt_address, 0, instance_pointer));
		
		
		
		TYPE_CLIST it = cls.getRelevantDataMembersFields();
		int offset = 4;
		while (it != null) {
			if (it.head != null) {
				 if (it.head.default_value != null) {
					TEMP default_value = it.head.default_value.IRme();
					IR.getInstance().Add_IRcommand(new IRcommand_Store_Register(default_value, offset, instance_pointer));
				} else {
					TEMP default_value = TEMP_FACTORY.getInstance().getFreshTEMP();
					IR.getInstance().Add_IRcommand(new IRcommandConstInt(default_value, 0));
					IR.getInstance().Add_IRcommand(new IRcommand_Store_Register(default_value, offset, instance_pointer));
				}
			}
			offset += 4;
			it = it.tail;
		}	

		return instance_pointer;

	}
}
