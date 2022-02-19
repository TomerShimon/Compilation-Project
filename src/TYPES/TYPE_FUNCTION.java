package TYPES;

public class TYPE_FUNCTION extends TYPE
{
	/***********************************/
	/* The return type of the function */
	/***********************************/
	public TYPE returnType;

	/*************************/
	/* types of input params */
	/*************************/
	public TYPE_LIST params;
	
	/****************/
	/* CTROR(S) ... */
	/****************/
	public TYPE_FUNCTION(TYPE returnType,String name,TYPE_LIST params)
	{
		this.name = name;
		this.returnType = returnType;
		this.params = params;
	}


	public boolean isFunc(){ return true;}

	public boolean compare_params(TYPE_LIST other_params)
	{
		TYPE_LIST curr1 = params;
		TYPE_LIST curr2 = other_params;

		if(curr1 == null && curr2 == null)
			return true;
		if(curr1 == null || curr2 == null)
			return false;
		if(!curr2.head.isinstanceof(curr1.head)){
			System.out.format("p1: %s p2: %s\n", curr1.head.name, curr2.head.name);
			return false;
		}

		while(curr1.tail!= null && curr2.tail != null)
		{
			curr1 = curr1.tail;
			curr2 = curr2.tail;
			if(!curr2.head.isinstanceof(curr1.head)){
				System.out.format("p1: %s p2: %s\n", curr1.head.name, curr2.head.name);
				return false;
			}
		}
		if(curr1.tail != null || curr2.tail != null)
			return false;
		return true;
	}	
}
