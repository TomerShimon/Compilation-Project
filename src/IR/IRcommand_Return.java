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



public class IRcommand_Return extends IRcommand
{
	TEMP ret;
	
	public IRcommand_Return(TEMP ret)
	{
		this.ret = ret;
		this.temp_in1 = ret;
	}
	
	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme()
	{
		MIPSGenerator.getInstance().moveToV0(ret);
	}
	
	
	
}
