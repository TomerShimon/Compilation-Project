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

public class IRcommand_Call extends IRcommand
{
	String func_label;
	int num_of_args;
	public IRcommand_Call(String func_label, int num_of_args)
	{
		this.func_label = func_label;
		this.num_of_args = num_of_args;
	}
	
	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme()
	{
	MIPSGenerator.getInstance().label(func_label);
		
	MIPSGenerator.getInstance().change_stack(-8);
        MIPSGenerator.getInstance().store_sp_offset("$ra", 4);
        MIPSGenerator.getInstance().store_sp_offset("$fp", 0);
        MIPSGenerator.getInstance().move_fp_to_sp();
        MIPSGenerator.getInstance().call_function(func_label);
        MIPSGenerator.getInstance().move_sp_to_fp();
        MIPSGenerator.getInstance().load_sp_offset("$ra", 4);
        MIPSGenerator.getInstance().load_sp_offset("$fa", 0);
	MIPSGenerator.getInstance().addi_reg("$sp", "$sp", 8);
	MIPSGenerator.getInstance().jr_ra();
	}
}
