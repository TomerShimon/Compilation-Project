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

public class IRcommand_PrintString extends IRcommand
{
	String str;
	static int counter = 0;
	public IRcommand_PrintString(String str)
	{
		this.str = str;
		
	}
	
	/***************/
	/* MIPS me !!! */
	/***************/
	
	public void MIPSme()
	{
		MIPSGenerator.getInstance().print_string(str, counter++);
	}
}
