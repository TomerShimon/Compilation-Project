package AST;
import TYPES.*;
import SYMBOL_TABLE.*;
import TEMP.*;
import IR.*;

public class AST_NEW_EXP_EXP extends AST_NEW_EXP
{
	public AST_TYPE type;
	public AST_EXP exp;

	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_NEW_EXP_EXP(AST_TYPE type, AST_EXP exp, int line)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.print("====================== newExp -> NEW type [exp]\n");

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.type = type;
		this.exp = exp;
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
		System.out.print("AST NODE NEW EXP EXP\n");

		/*****************************/
		/* RECURSIVELY PRINT var ... */
		/*****************************/
		if (type != null) type.PrintMe();
		
		/*********************************/
		/* Print to AST GRAPHIZ DOT file */
		/*********************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			"NEW_EXP\nEXP");

		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,type.SerialNumber);
		AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,exp.SerialNumber);
			
	}

	public TYPE SemantMe() throws Exception
	{
		TYPE t;
		if(exp.isNegative() || exp.isZero()) 
		{
			System.out.format("ERROR negative subscript");
			throw new SEMANTIC_ERROR_EXCEPTION(String.valueOf(this.line));
		}
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
		
		TYPE exp_type = exp.SemantMe();
		if(exp_type.name != "int")
		{
			System.out.format(">> ERROR subscript element is not of type int\n");
			throw new SEMANTIC_ERROR_EXCEPTION(String.valueOf(this.line));
		}
		return new TYPE_ARRAY("$array", t);		
	}

	public TYPE SemantMe(TYPE returnType, TYPE_CLASS type_class) throws Exception
	{
		if(exp.isNegative() || exp.isZero()) 
		{
			System.out.format("ERROR negative subscript");
			throw new SEMANTIC_ERROR_EXCEPTION(String.valueOf(this.line));
		}
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
		
		TYPE exp_type = exp.SemantMe(returnType, type_class);
		if(exp_type.name != "int")
		{
			System.out.format(">> ERROR subscript element is not of type int\n");
			throw new SEMANTIC_ERROR_EXCEPTION(String.valueOf(this.line));
		}
		return new TYPE_ARRAY("$array", t);		
	}
	
	
	
	public TEMP IRme() {
		TEMP instance_pointer = TEMP_FACTORY.getInstance().getFreshTEMP();
		TEMP bytes_to_allocate = TEMP_FACTORY.getInstance().getFreshTEMP();		
		
		TEMP requestedSize = exp.IRme();
		TEMP arrayLengthInBytes = TEMP_FACTORY.getInstance().getFreshTEMP();
		TEMP four = TEMP_FACTORY.getInstance().getFreshTEMP();

		IR.getInstance().Add_IRcommand(new IRcommand_Make_Zero(four));
		IR.getInstance().Add_IRcommand(new IRcommand_Binop_Addi_Integers(four,four,4));
		IR.getInstance().Add_IRcommand(new IRcommand_Binop_Addi_Integers(arrayLengthInBytes,requestedSize,0));
		IR.getInstance().Add_IRcommand(new IRcommand_Binop_Mul_Integers(arrayLengthInBytes,arrayLengthInBytes,four));
		IR.getInstance().Add_IRcommand(new IRcommand_Binop_Addi_Integers(arrayLengthInBytes,arrayLengthInBytes,4));

		IR.getInstance().Add_IRcommand(new IRcommand_Mallocate(instance_pointer,arrayLengthInBytes));

		IR.getInstance().Add_IRcommand(new IRcommand_Store_Register(requestedSize, 0, instance_pointer));

		
		
		return instance_pointer;
	}
	
}
