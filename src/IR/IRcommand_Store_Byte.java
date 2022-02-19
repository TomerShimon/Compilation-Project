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



public class IRcommand_Store_Byte extends IRcommand
{
	TEMP address;
	TEMP src;
	
	public IRcommand_Store_Byte(TEMP src, TEMP address)
	{
		this.address = address;
		this.src = src;
		this.temp_in1 = src;
		this.temp_in2 = address;
	}
	
	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme()
	{
		MIPSGenerator.getInstance().store_byte(src, address);
	}	
}
