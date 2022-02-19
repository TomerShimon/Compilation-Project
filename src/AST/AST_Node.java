package AST;
import TYPES.*;
import SYMBOL_TABLE.*;
import TEMP.*;
import IR.*;

public abstract class AST_Node
{
	/*******************************************/
	/* The serial number is for debug purposes */
	/* In particular, it can help in creating  */
	/* a graphviz dot format of the AST ...    */
	/*******************************************/
	public int SerialNumber;
	public int line;
	
	public int counter_number;
	
	public SYMBOL_TABLE_ENTRY symbol_table_entry; // i added
	
	/***********************************************/
	/* The default message for an unknown AST node */
	/***********************************************/
	public void PrintMe()
	{
		System.out.print("AST NODE UNKNOWN\n");
	}

	public TYPE SemantMe() throws Exception {return null;} 

	public TYPE SemantMe(TYPE returnType) throws Exception {return null;}

	public TYPE SemantMe(TYPE returnType, TYPE_CLASS type_class) throws Exception {return null;}
}
