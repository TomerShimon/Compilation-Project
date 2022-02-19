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

public class IRcommand_Load_Func_Address extends IRcommand
{
	TEMP dst;
	String func_label;
	
	public IRcommand_Load_Func_Address(TEMP dst,String func_label)
	{
		this.dst      = dst;
		this.func_label = func_label;
		this.temp_out = dst;
	}
	
	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme()
	{
		MIPSGenerator.getInstance().load_func_address(dst,func_label);
	}
}
