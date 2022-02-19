/***********/
/* PACKAGE */
/***********/
package IR;
import RA.*;

/*******************/
/* GENERAL IMPORTS */
/*******************/

/*******************/
/* PROJECT IMPORTS */
/*******************/

public class IR
{

	private IRcommand head= new IRcommand_Empty();
	private IRcommandList tail= new IRcommandList(new IRcommand_Empty(),null);
	
	
	
	public IRcommandList mainLabel;

	private IRcommand shouldBeBeforeMainHead = new IRcommand_Empty();
	public  IRcommandList shouldBeBeforeMainTail = new IRcommandList(shouldBeBeforeMainHead,null);
	public void Add_IRcommandBeforeMain(IRcommand cmd)
	{
		IRcommandList it = shouldBeBeforeMainTail;
		while ((it != null) && (it.tail != null))
		{
			it = it.tail;
		}
		it.tail = new IRcommandList(cmd,null);
	}



	/******************/
	/* Add IR command */
	/******************/
	public void Add_IRcommand(IRcommand cmd)
	{
		if ((head == null) && (tail == null))
		{
			this.head = cmd;
		}
		else if ((head != null) && (tail == null))
		{
			this.tail = new IRcommandList(cmd,null);
		}
		else
		{
			IRcommandList it = tail;
			while ((it != null) && (it.tail != null))
			{
				it = it.tail;
			}
			it.tail = new IRcommandList(cmd,null);
		}
	}
	
	public IRcommandList getEndOfListIRCommands() {
		IRcommandList cmd_lst = tail;
		while (cmd_lst.tail != null){
			cmd_lst = cmd_lst.tail;
		}
		return cmd_lst;
	}
	
	/***************/
	/* MIPS me !!! */
	/***************/

	
	
	public void beforeMIPSme()
	{
		IRcommandList after_main = mainLabel.tail;
		mainLabel.tail = shouldBeBeforeMainTail;
		
		IRcommandList cmd_lst = shouldBeBeforeMainTail;
		while (cmd_lst.tail != null){
			cmd_lst = cmd_lst.tail;
		}
		cmd_lst.tail = after_main;
		
	}
	
	public void MIPSme()
	{
		if (head != null) head.MIPSme();
		if (tail != null) tail.MIPSme();
	}


	/**************************************/
	/* USUAL SINGLETON IMPLEMENTATION ... */
	/**************************************/
	private static IR instance = null;

	/*****************************/
	/* PREVENT INSTANTIATION ... */
	/*****************************/
	protected IR() {}

	/******************************/
	/* GET SINGLETON INSTANCE ... */
	/******************************/
	public static IR getInstance()
	{
		if (instance == null)
		{
			/*******************************/
			/* [0] The instance itself ... */
			/*******************************/
			instance = new IR();
		}
		return instance;
	}
	
	public static IRcommandList getInstanceAsList()
	{
		if (instance == null)
		{
			return null;
		}
		return new IRcommandList(instance.head, instance.tail);
	}
}
