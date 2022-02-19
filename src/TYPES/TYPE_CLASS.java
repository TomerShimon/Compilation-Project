package TYPES;

public class TYPE_CLASS extends TYPE
{
	/*********************************************************************/
	/* If this class does not extend a father class this should be null  */
	/*********************************************************************/
	public TYPE_CLASS father;

	/**************************************************/
	/* Gather up all data members in one place        */
	/* Note that data members coming from the AST are */
	/* packed together with the class methods         */
	/**************************************************/
	
	
	
	public TYPE_CLIST data_members;
	public TYPE_CLIST method_members;
	public TYPE_CLIST field_members;


	public boolean clist_contains(TYPE_CLIST lst, String func_name){
		TYPE_CLIST it = lst;
		while (it != null){
			if (it.head != null) {
				String[] it_parts = ((TYPE_CLASS_MEMBER)it.head).name.split("_");
				if (it_parts[1].equals(func_name)) {
					return true;
				}
			}
			it = it.tail;
		}
		return false;
	}

	public boolean clist_contains_fields(TYPE_CLIST lst, String field_name){
		TYPE_CLIST it = lst;
		while (it != null){
			if (it.head != null) {
				if (((TYPE_CLASS_MEMBER)it.head).name.equals(field_name)) {
					return true;
				}
			}
			it = it.tail;
		}
		return false;
	}

	
	public TYPE_CLIST getRelevantDataMembersMethods()
	{	
		TYPE_CLIST all_methods = new TYPE_CLIST(null, null);
		TYPE_CLIST it_tmp = all_methods;
		if (father != null) {
			TYPE_CLIST it = father.getRelevantDataMembersMethods();
			while (it != null) {
				if (it_tmp.head == null){
					it_tmp.head = new TYPE_CLASS_MEMBER(it.head.name, null);
				}
				else {
					it_tmp.tail = new TYPE_CLIST(new TYPE_CLASS_MEMBER(it.head.name, null), null);
					it_tmp = it_tmp.tail;
				}
				it = it.tail;
			}
		}

		TYPE_CLIST it = method_members;
		while (it != null) {
			boolean exists = clist_contains(all_methods, ((TYPE_CLASS_MEMBER)it.head).name);
			if (!exists) {
				if (it_tmp.head == null){
					it_tmp.head = new TYPE_CLASS_MEMBER(this.name + "_" + it.head.name, null);
				}
				else {
					it_tmp.tail = new TYPE_CLIST(new TYPE_CLASS_MEMBER(this.name + "_" + it.head.name, null), null);
					it_tmp = it_tmp.tail;
				}
			}
			else{
				TYPE_CLIST it_find_dup = all_methods;
				while (it_find_dup != null){		
					String[] it_find_dup_parts = ((TYPE_CLASS_MEMBER)it_find_dup.head).name.split("_");
					if (((TYPE_CLASS_MEMBER)it.head).name.equals(it_find_dup_parts[1])) {
						it_find_dup.head = new TYPE_CLASS_MEMBER(this.name + "_" + it.head.name, null);
						break;
					}
					it_find_dup = it_find_dup.tail;
				}
			}
			it = it.tail;
		}
		if (all_methods.head == null)
			return null;
			
		return all_methods;
	}

	public TYPE_CLIST getRelevantDataMembersFields()
	{	
		TYPE_CLIST all_fields = new TYPE_CLIST(null, null);
		TYPE_CLIST it_tmp = all_fields;
		if (father != null) {
			TYPE_CLIST it = father.getRelevantDataMembersFields();
			while (it != null) {
				if (it_tmp.head == null){
					it_tmp.head = new TYPE_CLASS_MEMBER(it.head.name, null, it.head.default_value);
				}
				else {
					it_tmp.tail = new TYPE_CLIST(new TYPE_CLASS_MEMBER(it.head.name, null, it.head.default_value), null);
					it_tmp = it_tmp.tail;
				}
				it = it.tail;
			}
		}

		TYPE_CLIST it = field_members;
		while (it != null) {
			boolean exists = clist_contains_fields(all_fields, ((TYPE_CLASS_MEMBER)it.head).name);
			if (!exists) {
				if (it_tmp.head == null){
					it_tmp.head = new TYPE_CLASS_MEMBER(it.head.name, null, it.head.default_value);
				}
				else {
					it_tmp.tail = new TYPE_CLIST(new TYPE_CLASS_MEMBER(it.head.name, null, it.head.default_value), null);
					it_tmp = it_tmp.tail;
				}
			}
			it = it.tail;
		}
		if (all_fields.head == null)
			return null;
			
		return all_fields;
	}
	
	
	/****************/
	/* CTROR(S) ... */
	/****************/
	public TYPE_CLASS(TYPE_CLASS father,String name,TYPE_CLIST data_members)
	{
		this.name = name;
		this.father = father;
		this.data_members = data_members;
	}

	/*************/
	/* isClass() */
	/*************/
	public boolean isClass(){ return true;}
	

	public void addMember(TYPE_CLASS_MEMBER typ)
	{
		this.data_members = new TYPE_CLIST(typ,this.data_members);
	}

	public TYPE find_member(String name)
	{
		if(this.name.equals(name)) {return this;}

		if(data_members == null)
		{
			if(father != null) { return father.find_member(name);}
			else {return null;}
		}

		TYPE_CLIST curr = data_members;
		if(curr.head.name.equals(name))
			return curr.head.member_type;
		while(curr.tail != null)
		{
			curr = curr.tail;
			if(curr.head.name.equals(name))
				return curr.head.member_type;
		}

		if(father != null)
			return father.find_member(name);
		return null;
	}

	public boolean isinherited(String name)
	{
		if(this.name.equals(name)){return true;}
		if(this.father == null){return false;}
		return father.isinherited(name);
	}
	
	
	public int getNumOfFields() {

		int counter = 0;
		TYPE_CLIST curr = getRelevantDataMembersFields();
		

		while(curr != null)
		{
			counter++;
			curr = curr.tail;
		}
		return counter;
		
	}
	
	
	public int getFieldOffset(String name) {
		TYPE_CLIST members = getRelevantDataMembersFields();
		int offset = 0;
		while (members != null){
			offset += 4;
			if (members.head != null && members.head.name.equals(name)) {
				return offset;
			}
			members = members.tail;
		}
		return offset;
	}
	
	
	
	public int getFieldOffsetMethod(String name) {
		TYPE_CLIST all_methods = getRelevantDataMembersMethods();
		int offset = -4;
		while (all_methods != null) {
			offset += 4;
			String[] head_name_parts = all_methods.head.name.split("_");
			if (all_methods.head != null && head_name_parts[1].equals(name)) {
				return offset;
			}
			all_methods = all_methods.tail;
		}
		return offset;
	}
	
	
	
	
	

}
