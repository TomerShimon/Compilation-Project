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

public class IRcommand_Move extends IRcommand
{
	TEMP dst;
	TEMP src;
	
	public IRcommand_Move(TEMP dst, TEMP src)
	{
		this.dst = dst;
		this.src = src;
		this.temp_out = dst;
		this.temp_in1 = src;
	}
	
	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme()
	{
		MIPSGenerator.getInstance().move(dst,src);
	}
}
