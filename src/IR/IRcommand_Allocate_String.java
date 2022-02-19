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

public class IRcommand_Allocate_String extends IRcommand
{
	String str_label;
	String str;
	
	public IRcommand_Allocate_String(String str_label, String str)
	{
		this.str_label = str_label;
		this.str = str;
	}
	
	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme()
	{
		MIPSGenerator.getInstance().allocateString(str_label, str);
	}
}
