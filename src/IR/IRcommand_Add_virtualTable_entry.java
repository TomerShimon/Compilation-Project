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



public class IRcommand_Add_virtualTable_entry extends IRcommand
{
	String class_method_name;
	public IRcommand_Add_virtualTable_entry(String class_method_name)
	{
		this.class_method_name = class_method_name;
	}
	
	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme()
	{
		MIPSGenerator.getInstance().add_virtualTable_entry(class_method_name);
	}
	
	
	
}
