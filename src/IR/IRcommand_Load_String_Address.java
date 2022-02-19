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

public class IRcommand_Load_String_Address extends IRcommand
{
	String str_label;
	TEMP dst;
	public IRcommand_Load_String_Address(TEMP dst, String str_label)
	{
		this.dst = dst;
		this.str_label = str_label;
		this.temp_out = dst;
	}
	
	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme()
	{
		MIPSGenerator.getInstance().load_string_address(dst, str_label);
	}
}

