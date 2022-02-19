package AST;
import TYPES.*;
import SYMBOL_TABLE.*;
import TEMP.*;
import IR.*;

public class AST_TYPE extends AST_Node
{
	public String type_name;
	public AST_TYPE(int type, String id_name, int line)
	{
	SerialNumber = AST_Node_Serial_Number.getFresh();
	switch(type){
	case(0):
	{this.type_name = "int";System.out.format("====================== TYPE -> INT"); break;}
	case(1):
	{this.type_name = "string";System.out.format("====================== TYPE -> STRING"); break;}
	case(2):
	{this.type_name = "void";System.out.format("====================== TYPE -> VOID"); break;}
	case(3):
	{this.type_name = id_name;System.out.format("====================== TYPE -> ID"); break;}
	}

	this.line = line;
	}
	
	public boolean isId()
	{
		if(!this.type_name.equals("int") && !this.type_name.equals("string") && !this.type_name.equals("void") )
			return true;
		return false;
	}
	
	/*************************************************/
	/* The printing message for a varDec assign AST node */
	/*************************************************/
	public void PrintMe()
	{
		/*********************************/
		/* AST NODE TYPE = varDec assign */
		/*********************************/
		System.out.print("AST NODE TYPE\n");


		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			String.format("TYPE(%s)\n",type_name));

	}
}
