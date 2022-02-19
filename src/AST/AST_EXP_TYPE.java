package AST;
import TYPES.*;
import SYMBOL_TABLE.*;
import IR.*;
import VAR.VAR;
import TEMP.*;


public class AST_EXP_TYPE extends AST_EXP
{
	public int op;
	public String type;
	public int num;
	public String string_s;
	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_EXP_TYPE(int op, int num, int line)
	{
		this.op = op;
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.format("====================== exp -> ['-'] INT | NIL | STRING\n");

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.string_s = null;
		this.line = line;
		switch(op) {
			case 0: {this.type = "INT"; this.num = num; break;}
			case 1: {this.type = "-INT"; this.num = -1*num; break;}
		}
	}
	public AST_EXP_TYPE(int op, String s, int line)
	{
		this.num = -10;
		this.op = op;
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.format("====================== exp -> ['-'] INT | NIL | STRING\n");

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.line = line;
		switch(op) {
			case 2: {this.type = "NIL"; this.string_s = s; break;}
			case 3: {this.type = "STRING"; this.string_s = s; break;}
		}
	}

	public boolean isZero(){if(this.num == 0) {return true;} return false;}

	public boolean isNegative(){if(this.type.equals("-INT")) {return true;} return false;}

	/*************************************************/
	/* The printing message for - */
	/*************************************************/
	public void PrintMe()
	{
		/*********************************/
		/* AST NODE TYPE = */
		/*********************************/
		System.out.print("AST_EXP_TYPE ([-]INT|NIL|STRING)\n");

		/**********************************************/
		/* RECURSIVELY PRINT  ... */
		/**********************************************/


		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		
		if (op == 0 || op == 1) { 
			AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			String.format("AST_EXP_TYPE\nINT(%d)\n", this.num));
		}
		else if (op == 2) {
			AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			String.format("AST_EXP_TYPE\nNIL\n"));
		}
		else {
			AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			String.format("AST_EXP_TYPE\nSTRING\n"));
		}

		
		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
	}

	public TYPE SemantMe() throws Exception
	{
		switch(this.type)
		{
			case("INT"):{return TYPE_INT.getInstance();}
			case("-INT"):{return TYPE_INT.getInstance();}
			case("STRING"):{return TYPE_STRING.getInstance();}
			case("NIL"):{return TYPE_NIL.getInstance();}
		}
		return null;	
	}

	public TYPE SemantMe(TYPE_CLASS type_class) throws Exception
	{
		return this.SemantMe();
	}

	public TYPE SemantMe(TYPE returnType, TYPE_CLASS type_class) throws Exception
	{
		return this.SemantMe();	
	}
	
	
	
	public TEMP IRme()
	{
		if (this.type == "INT") // op=0
		{
			TEMP t = TEMP_FACTORY.getInstance().getFreshTEMP();
			IR.getInstance().Add_IRcommand( new IRcommandConstInt(t,num) );
			return t;
		}
		else if (this.type == "-INT") // op=1
		{
			TEMP t = TEMP_FACTORY.getInstance().getFreshTEMP();
			IR.getInstance().Add_IRcommand( new IRcommandConstInt(t, num) );
			return t;
		}
		else if (this.type == "NIL") // op=2
		{
			TEMP nil_temp = TEMP_FACTORY.getInstance().getFreshTEMP();
			IR.getInstance().Add_IRcommand( new IRcommandConstInt(nil_temp, 0) );
			return nil_temp;
		}
		else if (this.type == "STRING") // op=3
		{
			TEMP dst = TEMP_FACTORY.getInstance().getFreshTEMP();
			String var_name = "str_" + dst.getSerialNumber();
			IR.getInstance().Add_IRcommand(new IRcommand_Allocate_String(var_name, string_s));
			IR.getInstance().Add_IRcommand(new IRcommand_Load_String_Address(dst, var_name));
			return dst;			
		}
		return null;
	}


	public TEMP IRme(boolean isGlobal)
	{
		if (this.type == "INT") // op=0
		{
			TEMP t = TEMP_FACTORY.getInstance().getFreshTEMP();
			IR.getInstance().Add_IRcommandBeforeMain( new IRcommandConstInt(t,num) );
			return t;
		}
		else if (this.type == "-INT") // op=1
		{
			TEMP t = TEMP_FACTORY.getInstance().getFreshTEMP();
			IR.getInstance().Add_IRcommandBeforeMain( new IRcommandConstInt(t, num) );
			return t;
		}
		else if (this.type == "NIL") // op=2
		{
			TEMP nil_temp = TEMP_FACTORY.getInstance().getFreshTEMP();
			IR.getInstance().Add_IRcommandBeforeMain( new IRcommandConstInt(nil_temp, 0) );
			return nil_temp;
		}
		else if (this.type == "STRING") // op=3
		{
			
			TEMP dst = TEMP_FACTORY.getInstance().getFreshTEMP();
			String var_name = "str_" + dst.getSerialNumber();
			IR.getInstance().Add_IRcommand(new IRcommand_Allocate_String(var_name, string_s));
			IR.getInstance().Add_IRcommandBeforeMain(new IRcommand_Load_String_Address(dst, var_name));
			return dst;			
		}
		return null;
	}
}
