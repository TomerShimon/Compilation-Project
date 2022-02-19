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



public class IRcommand_Load_Virtual_Table_Address extends IRcommand
{
	TEMP dst;
	String class_name;
	
	public IRcommand_Load_Virtual_Table_Address(TEMP dst, String class_name)
	{
		this.dst      = dst;
		this.class_name = class_name;
		this.temp_in1 = dst;
	}
	
	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme()
	{
		MIPSGenerator.getInstance().load_virtual_table(dst, class_name);
	}
	
	
	
}
