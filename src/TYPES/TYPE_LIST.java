package TYPES;

public class TYPE_LIST
{
	/****************/
	/* DATA MEMBERS */
	/****************/
	public TYPE head;
	public TYPE_LIST tail;
	
	
	public int get_list_length() {
		if (this.head == null) { return 0; }
		
		int list_len = 0;
		TYPE_LIST curr = this;
		while (curr != null) {
			list_len++;
			curr = this.tail;
		}
		return list_len;
	}

	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public TYPE_LIST(TYPE head,TYPE_LIST tail)
	{
		this.head = head;
		this.tail = tail;
	}
}
