package AST;
import TYPES.*;
import SYMBOL_TABLE.*;
import TEMP.*;
import IR.*;
import VAR.*;

public class AST_STMT_ASSIGN extends AST_STMT
{
	/***************/
	/*  var := exp */
	/***************/
	public AST_VAR var;
	public AST_EXP exp;

	/*******************/
	/*  CONSTRUCTOR(S) */
	/*******************/
	public AST_STMT_ASSIGN(AST_VAR var,AST_EXP exp, int line)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.print("====================== stmt -> var ASSIGN exp SEMICOLON\n");

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.var = var;
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
		System.out.print("AST NODE ASSIGN STMT\n");

		/***********************************/
		/* RECURSIVELY PRINT VAR + EXP ... */
		/***********************************/
		if (var != null) var.PrintMe();
		if (exp != null) exp.PrintMe();

		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			"ASSIGN\nleft := right\n");
		
		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,var.SerialNumber);
		AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,exp.SerialNumber);
	}

	public TYPE SemantMe(TYPE returnType) throws Exception
	{
		TYPE var_type = var.SemantMe();
		TYPE exp_type = exp.SemantMe();
		if(!exp_type.isinstanceof(var_type))
		{
			System.out.format(">> ERROR incompatible types\n");
			throw new SEMANTIC_ERROR_EXCEPTION(String.valueOf(this.line));			
		}
		return null;
	}

	public TYPE SemantMe(TYPE returnType, TYPE_CLASS type_class) throws Exception
	{
		TYPE var_type = var.SemantMe(returnType,type_class);
		TYPE exp_type = exp.SemantMe(returnType,type_class);
		if(!exp_type.isinstanceof(var_type))
		{
			System.out.format(">> ERROR incompatible types\n");
			throw new SEMANTIC_ERROR_EXCEPTION(String.valueOf(this.line));		
		}
		return null;
	}



	public TEMP IRme(){
		if (var.isVarField()){ 
			TEMP instance = ((AST_VAR_FIELD)var).var.IRme(); 
			
			String valid_ptr = IRcommand.getFreshLabel("ADDR_NOT_NULL");
			IR.getInstance().Add_IRcommand(new IRcommand_Jump_If_Not_Eq_To_Zero(instance, valid_ptr));
			IR.getInstance().Add_IRcommand(new IRcommand_PrintString("Invalid Pointer Dereference"));
			IR.getInstance().Add_IRcommand(new IRcommand_Exit_With_Error());	
			IR.getInstance().Add_IRcommand(new IRcommand_Label(valid_ptr));
			
			int offset = (((AST_VAR_FIELD)var).clsType).getFieldOffset(((AST_VAR_FIELD) var).fieldName);
			IR.getInstance().Add_IRcommand(new IRcommand_Store_Register(exp.IRme(), offset, instance));
		}
		else{
			if(var.isSUBSCRIPT()){ 
				TEMP arrStartPointer = ((AST_VAR_SUBSCRIPT)var).var.IRme(); 
				
				TEMP reqIndex = ((AST_VAR_SUBSCRIPT)var).subscript.IRme();
				TEMP address = TEMP_FACTORY.getInstance().getFreshTEMP();

				String validAccess = IRcommand.getFreshLabel("VALID_ACCESS");
				TEMP arrayLength = TEMP_FACTORY.getInstance().getFreshTEMP();
				IR.getInstance().Add_IRcommand(new IRcommand_LW(arrayLength, 0,arrStartPointer));
				IR.getInstance().Add_IRcommand(new IRcommand_Jump_If_Greater_Then(arrayLength,reqIndex,validAccess));
				IR.getInstance().Add_IRcommand(new IRcommand_PrintString("Access Violation"));
				IR.getInstance().Add_IRcommand(new IRcommand_Exit_With_Error());	
				IR.getInstance().Add_IRcommand(new IRcommand_Label(validAccess));

				IR.getInstance().Add_IRcommand(new IRcommand_Binop_Add_Integers(address,arrStartPointer,reqIndex));
				IR.getInstance().Add_IRcommand(new IRcommand_Binop_Add_Integers(address,address,reqIndex));
				IR.getInstance().Add_IRcommand(new IRcommand_Binop_Add_Integers(address,address,reqIndex));
				IR.getInstance().Add_IRcommand(new IRcommand_Binop_Add_Integers(address,address,reqIndex));
				IR.getInstance().Add_IRcommand(new IRcommand_Binop_Addi_Integers(address,address,4));

				IR.getInstance().Add_IRcommand(new IRcommand_Store_Register(exp.IRme(),0, address));

				return null;
			} else if (var.symbol_table_entry.cls == null) { 
				if (var.symbol_table_entry.scope == 1) 
				{
					IR.getInstance().Add_IRcommand(new IRcommand_Store(var.symbol_table_entry.name, exp.IRme()));
				}
				else 
				{
					VAR var2 = new VAR(var.symbol_table_entry.offset, var.symbol_table_entry.name, var.symbol_table_entry.scope == 1);
					IR.getInstance().Add_IRcommand(new IRcommand_Set_Var(var2, exp.IRme()));
				}
			} else { 
					
				TEMP instance = TEMP_FACTORY.getInstance().getFreshTEMP();
				VAR var2 = new VAR(8, "symbol_table_entry.name", false);
				IR.getInstance().Add_IRcommand(new IRcommand_Load_Var(var2, instance));
				
				int offset = var.symbol_table_entry.cls.getFieldOffset(var.symbol_table_entry.name);
				IR.getInstance().Add_IRcommand(new IRcommand_Store_Register(exp.IRme(), offset, instance));
			}

		}
		return null;
	}
	
}
