package AST;
import TYPES.*;
import SYMBOL_TABLE.*;
import TEMP.*;
import IR.*;

public abstract class AST_STMT extends AST_Node
{
	/*********************************************************/
	/* The default message for an unknown AST statement node */
	/*********************************************************/
	public void PrintMe()
	{
		System.out.print("UNKNOWN AST STATEMENT NODE");
	}

	public TYPE SemantMe() throws Exception {return null;} 

	public TYPE SemantMe(TYPE returnType) throws Exception {return null;}

	public TYPE SemantMe(TYPE returnType, TYPE_CLASS type_class) throws Exception {return null;}
	
	
	public TEMP IRme() {return null;}

}
