package TYPES;
import AST.AST_EXP;

public abstract class TYPE
{
	/******************************/
	/*  Every type has a name ... */
	/******************************/
	public String name;
	public AST_EXP default_value;


	/*************/
	/* isClass() */
	/*************/
	public boolean isClass(){ return false;}

	/*************/
	/* isArray() */
	/*************/
	public boolean isArray(){ return false;}
	public boolean isFunc(){ return false;}

	public boolean isinherited(String name)
	{
		return false;
	}	

	public boolean isinstanceof(TYPE type)
	{
		if(this.name.equals("nil"))
		{
			if(!(type.isClass() || type.isArray() )) {return false;}
			return true;
		}
		else if(this.isClass() && type.isClass())
		{
			if(!((TYPE_CLASS)this).isinherited(type.name)) {return false;}
			return true;
		}
		if(this.isFunc() && type.isFunc())
		{
			if(!((TYPE_FUNCTION)this).compare_params(((TYPE_FUNCTION)type).params) || !(((TYPE_FUNCTION)this).returnType.name.equals(((TYPE_FUNCTION)type).returnType.name)))
				{return false;}
			return true;
		}
		else if(type.isArray())
		{
		if(!this.isArray()){return false;}
		if(this.name.equals("$array") && ((TYPE_ARRAY)this).element_type.isinstanceof(((TYPE_ARRAY)type).element_type)){return true;}
		if( !this.name.equals(type.name)) {return false;}
		return true;
		}
		else if(!this.name.equals(type.name))
		{
			return false;			
		}
		return true;
	}
}
