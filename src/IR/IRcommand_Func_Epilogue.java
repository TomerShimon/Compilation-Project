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


public class IRcommand_Func_Epilogue extends IRcommand
{

	public IRcommand_Func_Epilogue()
	{
	}
	
	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme()
	{		
		
		MIPSGenerator.getInstance().move_sp_to_fp();
		for (int i=0; i<10; i++){
			MIPSGenerator.getInstance().load_sp_offset("$t"+i, -4 + -4*i);
		}
		MIPSGenerator.getInstance().load_sp_offset("$fp", 0);
		MIPSGenerator.getInstance().load_sp_offset("$ra", 4);
		MIPSGenerator.getInstance().addi_reg("$sp", "$sp", 8);
		MIPSGenerator.getInstance().jr_ra();
		
	}

	
	
	
	
}
