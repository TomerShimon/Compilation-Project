/***********/
/* PACKAGE */
/***********/
package IR;

/*******************/
/* GENERAL IMPORTS */
/*******************/

/*******************/
/* PROJECT IMPORTS */
/*******************/
import TEMP.*;
import MIPS.*;

public class IRcommand_Jump_If_Greater_Then extends IRcommand
{
	TEMP t1;
	TEMP t2;
	String label_name;
	
	public IRcommand_Jump_If_Greater_Then(TEMP t1, TEMP t2, String label_name)
	{
		this.t1          = t1;
		this.t2          = t2;
		this.label_name = label_name;
		this.temp_in1 = t1;
		this.temp_in2 = t2;
		this.branch = label_name;
	}
	
	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme()
	{
		MIPSGenerator.getInstance().bge(t1, t2, label_name);
	}
}


