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

public class IRcommand_Jalr extends IRcommand
{
	TEMP reg;
	
	public IRcommand_Jalr(TEMP reg)
	{
		this.reg = reg;
		this.temp_in1 = reg;
	}
	
	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme()
	{
		MIPSGenerator.getInstance().jalr(reg);
	}
}
