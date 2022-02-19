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

public class IRcommand_takeFromV0 extends IRcommand
{
	TEMP t;
	
	public IRcommand_takeFromV0(TEMP t)
	{
		this.t = t;
		this.temp_out = t;
	}
	
	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme()
	{
		MIPSGenerator.getInstance().takeFromV0(t);
	}
}
