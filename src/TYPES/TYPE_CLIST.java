package TYPES;

public class TYPE_CLIST
{
	/****************/
	/* DATA MEMBERS */
	/****************/
	public TYPE_CLASS_MEMBER head;
	public TYPE_CLIST tail;

	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public TYPE_CLIST(TYPE_CLASS_MEMBER head,TYPE_CLIST tail)
	{
		this.head = head;
		this.tail = tail;
	}
}
