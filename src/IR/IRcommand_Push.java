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



public class IRcommand_Push extends IRcommand
{
	TEMP temp;
	
	public IRcommand_Push(TEMP temp)
	{
		this.temp = temp;
		this.temp_in1 = temp;
	}
	
	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme()
	{
		MIPSGenerator.getInstance().change_stack(-4);
		if (temp != null) {
            MIPSGenerator.getInstance().store_local_sp_offset(temp, 0);
        }
		
	}
}
