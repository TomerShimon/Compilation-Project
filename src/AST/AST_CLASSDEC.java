package AST;
import TYPES.*;
import SYMBOL_TABLE.*;
import TEMP.*;
import IR.*;

public abstract class AST_CLASSDEC extends AST_Node
{
	/*********************************************************/
	/* The default message for an unknown AST classDec node */
	/*********************************************************/
	public void PrintMe()
	{
		System.out.print("UNKNOWN AST CLASSDEC NODE");
	}
	
	public TEMP IRme() {return null; }
 
}
