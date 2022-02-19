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

public class IRcommand_PrintStringTEMP extends IRcommand
{
	TEMP t;
	
	public IRcommand_PrintStringTEMP(TEMP t)
	{
		this.t = t;
		this.temp_in1 = t;
	}
	
	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme()
	{
		MIPSGenerator.getInstance().print_string_temp(t);
	}
}
