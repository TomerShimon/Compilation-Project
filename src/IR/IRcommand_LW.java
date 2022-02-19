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



public class IRcommand_LW extends IRcommand
{
	TEMP dst;
	int offset;
	TEMP src;

	public IRcommand_LW(TEMP dst, int offset, TEMP src)
	{
		this.dst     = dst;
		this.offset  = offset;
		this.src	 = src;
		this.temp_out = dst;
		this.temp_in1 = src;
	}
	
	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme()
	{
		
		MIPSGenerator.getInstance().load_general(dst, offset, src);
	}	
}


