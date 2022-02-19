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
import VAR.*;


public class IRcommand_Set_Var extends IRcommand
{
	VAR var;
	TEMP value;
	
	public IRcommand_Set_Var(VAR var, TEMP value)
	{
		this.var = var;
		this.value = value;
		this.temp_in1 = value;
		
	}
	
	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme()
	{
		if (var.is_global) {
			
		}
		else {
			MIPSGenerator.getInstance().store_local_fp_offset_temp(var.offset_fp, value);

		}
		
	}
	
	
}

