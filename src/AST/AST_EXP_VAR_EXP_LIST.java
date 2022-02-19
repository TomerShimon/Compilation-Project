package AST;
import TYPES.*;
import SYMBOL_TABLE.*;
import TEMP.*;
import IR.*;

public class AST_EXP_VAR_EXP_LIST extends AST_EXP
{
	public AST_VAR var;
	public String id;
	public AST_EXP_LIST list;
	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_EXP_VAR_EXP_LIST(AST_VAR var, String id, AST_EXP_LIST list, int line)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.format("====================== exp -> var '.' ID '('[exp[','exp]*')'\n");

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.var = var;
		this.id = id;
		this.list = list;
		this.line = line;
	}

	/*************************************************/
	/* The printing message for - */
	/*************************************************/
	public void PrintMe()
	{
		String isList = "null_list";
		if (list != null) {
			isList = "not_null_list";
		}
		/*********************************/
		/* AST NODE TYPE = */
		/*********************************/
		System.out.print("AST_EXP_VAR_EXP_LIST\n");

		/**********************************************/
		/* RECURSIVELY PRINT  ... */
		/**********************************************/
		if (list != null) list.PrintMe();
		System.out.format("printed exp_list\n");
		if (var != null) var.PrintMe();


		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			String.format("exp_var_exp_list\nID(%s)\n%s\n",id ,isList));
		
		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (var != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,var.SerialNumber);
		if (list != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,list.SerialNumber);

	}

	public TYPE SemantMe() throws Exception
	{
		TYPE var_type = var.SemantMe();
		if(!var_type.isClass())
		{
			System.out.format(">> ERROR not a class\n");
			throw new SEMANTIC_ERROR_EXCEPTION(String.valueOf(this.line));
		}
		var_type = (TYPE_CLASS) var_type;
		TYPE t = ((TYPE_CLASS) var_type).find_member(this.id);
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
		if(this.list == null)
			is_identical = ((TYPE_FUNCTION) t).compare_params(null);
		else
		{
			TYPE t1;
			TYPE_LIST type_list = null;
			for (AST_EXP_LIST it = list; it  != null; it = it.tail)
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
			System.out.format(">> ERROR incompatible parameters\n");
			throw new SEMANTIC_ERROR_EXCEPTION(String.valueOf(this.line));
		}
		return ((TYPE_FUNCTION) t).returnType;
	}

	public TYPE SemantMe(TYPE_CLASS type_class) throws Exception
	{
		System.out.format("ERROR, only constant assignments allowed within class");
		throw new SEMANTIC_ERROR_EXCEPTION(String.valueOf(this.line));
	}

	public TYPE SemantMe(TYPE returnType, TYPE_CLASS type_class) throws Exception
	{
		TYPE var_type = var.SemantMe(returnType, type_class);
		if(!var_type.isClass())
		{
			System.out.format(">> ERROR not a class\n");
			throw new SEMANTIC_ERROR_EXCEPTION(String.valueOf(this.line));
		}
		var_type = (TYPE_CLASS) var_type;
		TYPE t = ((TYPE_CLASS) var_type).find_member(this.id);
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
		if(this.list == null)
			is_identical = ((TYPE_FUNCTION) t).compare_params(null);
		else
		{
			TYPE t1;
			TYPE_LIST type_list = null;
			for (AST_EXP_LIST it = list; it  != null; it = it.tail)
			{
				t1 = it.head.SemantMe(returnType, type_class);
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
			System.out.format(">> ERROR incompatible parameters\n");
			throw new SEMANTIC_ERROR_EXCEPTION(String.valueOf(this.line));
		}
		return ((TYPE_FUNCTION) t).returnType;
	}
	
	
	
	public TEMP IRme() {
		AST_EXP_LIST arg_list = list;
		TEMP instance = var.IRme();
		

		TEMP error_temp = TEMP_FACTORY.getInstance().getFreshTEMP();
		String valid_ptr = IRcommand.getFreshLabel("ADDR_NOT_NULL");
		IR.getInstance().Add_IRcommand(new IRcommand_Jump_If_Not_Eq_To_Zero(instance, valid_ptr));
		IR.getInstance().Add_IRcommand(new IRcommand_PrintString("Invalid Pointer Dereference"));
		IR.getInstance().Add_IRcommand(new IRcommand_Exit_With_Error());
		IR.getInstance().Add_IRcommand(new IRcommand_Label(valid_ptr));
		
		int number_of_arguments = 0;

		while (arg_list != null){
			TEMP t = arg_list.head.IRme();
			IR.getInstance().Add_IRcommand(new IRcommand_Push(t));
			number_of_arguments++;
			arg_list = arg_list.tail;
		}
		IR.getInstance().Add_IRcommand(new IRcommand_Push(instance));
		number_of_arguments++;
		
		
		TEMP virtual_address_pointer = TEMP_FACTORY.getInstance().getFreshTEMP();
		IR.getInstance().Add_IRcommand(new IRcommand_LW(virtual_address_pointer, 0, instance));
		
		TEMP method_pointer = TEMP_FACTORY.getInstance().getFreshTEMP();
		
		int offset = ((TYPE_CLASS) var.symbol_table_entry.type).getFieldOffsetMethod(id);
		
		
		IR.getInstance().Add_IRcommand(new IRcommand_LW(method_pointer, offset, virtual_address_pointer));
		IR.getInstance().Add_IRcommand(new IRcommand_Jalr(method_pointer));
		IR.getInstance().Add_IRcommand(new IRcommand_Add_SP(number_of_arguments*4));

		TEMP return_value = TEMP_FACTORY.getInstance().getFreshTEMP();
		IR.getInstance().Add_IRcommand(new IRcommand_takeFromV0(return_value));
		return return_value;
		
	}
	
}
