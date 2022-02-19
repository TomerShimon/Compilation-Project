package AST;
import TYPES.*;
import SYMBOL_TABLE.*;
import TEMP.*;
import IR.*;

public abstract class AST_EXP extends AST_Node
{
	public int moish;

	public TYPE SemantMe() throws Exception {return null;}
	public TYPE SemantMe(TYPE_CLASS type_class) throws Exception {return null;}
	public TYPE SemantMe(TYPE returnType, TYPE_CLASS type_class) throws Exception {return null;}

	public boolean isNegative(){return false;}	
	public boolean isZero(){return false;}
	
	public TEMP IRme() {return null;};
	
	public TEMP IRme(boolean isGlobal) {return null;};

}
