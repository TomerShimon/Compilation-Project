package AST;
import TYPES.*;
import SYMBOL_TABLE.*;
import TEMP.*;
import IR.*;

public abstract class AST_VAR extends AST_Node
{
	public TYPE SemantMe() throws Exception {return null;}
	public TYPE SemantMe(TYPE_CLASS type_class) throws Exception {return null;}
	public TYPE SemantMe(TYPE returnType, TYPE_CLASS type_class) throws Exception {return null;}
	
	
	public boolean isVarField(){
		return false;
	}
	public boolean isSUBSCRIPT(){ return false; }
	
	public TEMP IRme() {return null; }

}
