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



public class IRcommand_Load_Var extends IRcommand
{
	VAR var;
	TEMP tmp;
	
	public IRcommand_Load_Var(VAR var, TEMP tmp)
	{
		this.var = var;
		this.tmp = tmp;
		this.temp_out = tmp;
		
	}
	
	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme()
	{
		if (var.is_global) {
			
		}
		else {
			MIPSGenerator.getInstance().load_local_fp_offset_temp(tmp, var.offset_fp);

		}
	}
}

