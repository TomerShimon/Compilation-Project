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



public class IRcommand_Func_Prologue extends IRcommand
{
	String full_label; 
	String class_name;
	
	public IRcommand_Func_Prologue(String label, String class_name)
	{

		if (class_name == null){ 
			if (label.equals("main"))
				this.full_label = label;
			else			
				this.full_label = "func_" + label;
		}
		else { 
			this.full_label = class_name + "_" + label;
		}
	
		this.class_name = class_name;
	}
	
	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme()
	{
	MIPSGenerator.getInstance().label(full_label);
		
	MIPSGenerator.getInstance().change_stack(-8);
        MIPSGenerator.getInstance().store_sp_offset("$ra", 4);
        MIPSGenerator.getInstance().store_sp_offset("$fp", 0);
        MIPSGenerator.getInstance().move_fp_to_sp();
	for (int i=0; i<10; i++){
		MIPSGenerator.getInstance().change_stack(-4);
		MIPSGenerator.getInstance().store_sp_offset("$t"+i, 0);
	}
	MIPSGenerator.getInstance().change_stack(-100);

	
	}
}
