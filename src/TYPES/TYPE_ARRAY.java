package TYPES;

public class TYPE_ARRAY extends TYPE
{
	public TYPE element_type;
	public TYPE_ARRAY(String name, TYPE type)	
	{
		this.name = name;
		this.element_type = type;
	}

	public boolean isArray(){ return true;}

}
