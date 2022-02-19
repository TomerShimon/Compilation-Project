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

public class IRcommand_Load_Byte extends IRcommand
{
	TEMP dst;
	TEMP address;
	
	public IRcommand_Load_Byte(TEMP dst,TEMP address)
	{
		this.dst     = dst;
		this.address = address;
		this.temp_out = dst;
		this.temp_in1 = address;
	}
	
	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme()
	{
		MIPSGenerator.getInstance().load_byte(dst,address);
	}
}

