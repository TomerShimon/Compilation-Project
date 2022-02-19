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



public class IRcommand_Store_Register extends IRcommand
{
	TEMP dst;
	int offset;
	TEMP src;
	
	public IRcommand_Store_Register(TEMP dst, int offset ,TEMP src)
	{
		this.dst = dst;
		this.offset = offset;
		this.src = src;
		this.temp_in1 = dst;
		this.temp_in2 = src;
	}
	
	/***************/
	/* MIPS me !!! */
	/***************/
	
	public void MIPSme()
	{
		MIPSGenerator.getInstance().store_to_register(dst,offset,src);
	}
	
	
	
}

