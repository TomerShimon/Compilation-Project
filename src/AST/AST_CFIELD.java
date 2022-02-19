package AST;
import TYPES.*;
import SYMBOL_TABLE.*;
import TEMP.*;
import IR.*;

public class AST_CFIELD extends AST_Node
{
	/*********************************************************/
	/* The default message for an unknown AST cField node */
	/*********************************************************/
	public void PrintMe()
	{
		System.out.print("UNKNOWN AST CFIELD NODE");
	}

	public TYPE_CLASS_MEMBER SemantMe(TYPE_CLASS t) throws Exception {return null;}
	
	

	public TEMP IRme() {
		return null;
	}
}
