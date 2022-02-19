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

public class IRcommand_Mallocate extends IRcommand
{
	TEMP dst;
	TEMP bytes;
	
	public IRcommand_Mallocate(TEMP dst, TEMP bytes)
	{
		this.dst = dst;
		this.bytes = bytes;
		this.temp_out = dst;
		this.temp_in1 = bytes;
	}
	
	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme()
	{
		MIPSGenerator.getInstance().mallocate(dst,bytes);
		
	}
}
