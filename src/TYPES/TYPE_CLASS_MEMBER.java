package TYPES;
import AST.*;

public class TYPE_CLASS_MEMBER extends TYPE
{
	public TYPE member_type;
	public TYPE_CLASS_MEMBER(String name, TYPE type)	
	{
		this.name = name;
		this.member_type = type;
	}
	
	public TYPE_CLASS_MEMBER(String name, TYPE type, AST_EXP default_value)	
	{
		this.name = name;
		this.member_type = type;
		this.default_value = default_value;
	}

}
