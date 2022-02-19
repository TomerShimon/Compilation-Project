package AST;
import TYPES.*;
import SYMBOL_TABLE.*;
import TEMP.*;
import IR.*;

public class AST_VAR_FIELD extends AST_VAR
{
	public AST_VAR var;
	public String fieldName;
	
	
	
	// i added
	public TYPE_CLASS clsType;
	
	//i added:
	public boolean isVarField(){
		return true;
	}
	
	
	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_VAR_FIELD(AST_VAR var,String fieldName, int line)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.format("====================== var -> var DOT ID( %s )\n",fieldName);

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.var = var;
		this.fieldName = fieldName;
		this.line = line;
	}

	/*************************************************/
	/* The printing message for a field var AST node */
	/*************************************************/
	public void PrintMe()
	{
		/*********************************/
		/* AST NODE TYPE = AST FIELD VAR */
		/*********************************/
		System.out.print("AST NODE FIELD VAR\n");

		/**********************************************/
		/* RECURSIVELY PRINT VAR, then FIELD NAME ... */
		/**********************************************/
		if (var != null) var.PrintMe();
		System.out.format("FIELD NAME( %s )\n",fieldName);

		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			String.format("FIELD\nVAR\n...->%s",fieldName));
		
		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (var != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,var.SerialNumber);
	}

	public TYPE SemantMe() throws Exception
	{
		TYPE t;
		TYPE var_type = var.SemantMe();
		if(!var_type.isClass())
		{
			System.out.format("%s not an instance of a class",var_type.name);
			throw new SEMANTIC_ERROR_EXCEPTION(String.valueOf(this.line));
		}
		
		clsType = (TYPE_CLASS) var_type; 
		
		var_type = (TYPE_CLASS) var_type;
		TYPE field_type = ((TYPE_CLASS) var_type).find_member(fieldName);
		if(field_type == null)
		{
			System.out.format("%s - no member found named %s",var_type.name, fieldName);
			throw new SEMANTIC_ERROR_EXCEPTION(String.valueOf(this.line));
		}
		return field_type;
	}


	public TYPE SemantMe(TYPE returnType, TYPE_CLASS type_class) throws Exception
	{
		TYPE t;
		TYPE var_type = var.SemantMe(returnType, type_class);
		if(!var_type.isClass())
		{
			System.out.format("%s not an instance of a class",var_type.name);
			throw new SEMANTIC_ERROR_EXCEPTION(String.valueOf(this.line));
		}
		
		clsType = (TYPE_CLASS) var_type;
	
		var_type = (TYPE_CLASS) var_type;
		TYPE field_type = ((TYPE_CLASS) var_type).find_member(fieldName);
		if(field_type == null)
		{
			System.out.format("%s - no member found named %s",var_type.name, fieldName);
			throw new SEMANTIC_ERROR_EXCEPTION(String.valueOf(this.line));
		}
		return field_type;
	}
	
	
	
	public TEMP IRme() {

		TEMP instance = var.IRme(); 
		
		String valid_ptr = IRcommand.getFreshLabel("ADDR_NOT_NULL");
		IR.getInstance().Add_IRcommand(new IRcommand_Jump_If_Not_Eq_To_Zero(instance, valid_ptr));
		IR.getInstance().Add_IRcommand(new IRcommand_PrintString("Invalid Pointer Dereference"));
		IR.getInstance().Add_IRcommand(new IRcommand_Exit_With_Error());
		IR.getInstance().Add_IRcommand(new IRcommand_Label(valid_ptr));
		
		int offset = clsType.getFieldOffset(fieldName);
		TEMP result_value = TEMP_FACTORY.getInstance().getFreshTEMP();
		IR.getInstance().Add_IRcommand(new IRcommand_LW(result_value, offset, instance));
		return result_value;
	}	
	
	

}
