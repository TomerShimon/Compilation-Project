package AST;
import TYPES.*;
import SYMBOL_TABLE.*;
import TEMP.*;
import IR.*;

public class AST_VAR_SUBSCRIPT extends AST_VAR
{
	public AST_VAR var;
	public AST_EXP subscript;
	
	
	
	// i added
	public boolean isSUBSCRIPT(){ return true; }

	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_VAR_SUBSCRIPT(AST_VAR var,AST_EXP subscript, int line)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.print("====================== var -> var [ exp ]\n");

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.var = var;
		this.subscript = subscript;
		this.line = line;
	}

	/*****************************************************/
	/* The printing message for a subscript var AST node */
	/*****************************************************/
	public void PrintMe()
	{
		/*************************************/
		/* AST NODE TYPE = AST SUBSCRIPT VAR */
		/*************************************/
		System.out.print("AST NODE SUBSCRIPT VAR\n");

		/****************************************/
		/* RECURSIVELY PRINT VAR + SUBSRIPT ... */
		/****************************************/
		if (var != null) var.PrintMe();
		if (subscript != null) subscript.PrintMe();
		
		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			"SUBSCRIPT\nVAR\n...[...]");
		
		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (var       != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,var.SerialNumber);
		if (subscript != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,subscript.SerialNumber);
	}

	public TYPE SemantMe() throws Exception
	{
		if(subscript.isNegative()) 
		{
			System.out.format("ERROR negative subscript");
			throw new SEMANTIC_ERROR_EXCEPTION(String.valueOf(this.line));
		}
		TYPE var_type = var.SemantMe();
		if(!var_type.isArray())
		{
			System.out.format("%s not an instance of an array",var_type.name);
			throw new SEMANTIC_ERROR_EXCEPTION(String.valueOf(this.line));
		}
		var_type = (TYPE_ARRAY) var_type;
		TYPE subscript_type = subscript.SemantMe();
		if(subscript_type.name != "int")
		{
			System.out.format("sunscript is not an int");
			throw new SEMANTIC_ERROR_EXCEPTION(String.valueOf(this.line));
		}
		return ((TYPE_ARRAY) var_type).element_type;
	}



	public TYPE SemantMe(TYPE returnType, TYPE_CLASS type_class) throws Exception
	{
		if(subscript.isNegative()) 
		{
			System.out.format("ERROR negative subscript");
			throw new SEMANTIC_ERROR_EXCEPTION(String.valueOf(this.line));
		}
		TYPE var_type = var.SemantMe(returnType, type_class);
		if(!var_type.isArray())
		{
			System.out.format("%s not an instance of an array",var_type.name);
			throw new SEMANTIC_ERROR_EXCEPTION(String.valueOf(this.line));
		}
		var_type = (TYPE_ARRAY) var_type;
		TYPE subscript_type = subscript.SemantMe(returnType, type_class);
		if(subscript_type.name != "int")
		{
			System.out.format("sunscript is not an int");
			throw new SEMANTIC_ERROR_EXCEPTION(String.valueOf(this.line));
		}
		return ((TYPE_ARRAY) var_type).element_type;
	}
	
	
	
	public TEMP IRme() {
		TEMP arrStartPointer = var.IRme(); 
		TEMP reqIndex = subscript.IRme();


		String validAccess = IRcommand.getFreshLabel("VALID_ACCESS");
		TEMP arrayLength = TEMP_FACTORY.getInstance().getFreshTEMP();
		IR.getInstance().Add_IRcommand(new IRcommand_LW(arrayLength, 0,arrStartPointer));
		IR.getInstance().Add_IRcommand(new IRcommand_Jump_If_Greater_Then(arrayLength,reqIndex,validAccess));
		IR.getInstance().Add_IRcommand(new IRcommand_PrintString("Access Violation"));
		IR.getInstance().Add_IRcommand(new IRcommand_Exit_With_Error());		
		IR.getInstance().Add_IRcommand(new IRcommand_Label(validAccess));
		

		TEMP address = TEMP_FACTORY.getInstance().getFreshTEMP();
		TEMP dst = TEMP_FACTORY.getInstance().getFreshTEMP();

		IR.getInstance().Add_IRcommand(new IRcommand_Binop_Add_Integers(address,arrStartPointer,reqIndex));
		IR.getInstance().Add_IRcommand(new IRcommand_Binop_Add_Integers(address,address,reqIndex));
		IR.getInstance().Add_IRcommand(new IRcommand_Binop_Add_Integers(address,address,reqIndex));
		IR.getInstance().Add_IRcommand(new IRcommand_Binop_Add_Integers(address,address,reqIndex));
		IR.getInstance().Add_IRcommand(new IRcommand_Binop_Addi_Integers(address,address,4));


		IR.getInstance().Add_IRcommand(new IRcommand_LW(dst,0,address));

		return dst;
	}
	
	
	
}
