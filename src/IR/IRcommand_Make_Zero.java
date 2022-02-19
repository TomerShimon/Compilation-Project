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



public class IRcommand_Make_Zero extends IRcommand
{
	TEMP src;
	
	public IRcommand_Make_Zero(TEMP src)
	{
		this.src = src;
		this.temp_out = src;
	}
	
	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme()
	{
		MIPSGenerator.getInstance().make_zero(src);
	}
	
	
	
}
