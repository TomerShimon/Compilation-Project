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



public class IRcommand_Start_virtualTable extends IRcommand
{
	String class_name;
	
	public IRcommand_Start_virtualTable(String class_name)
	{
		this.class_name = class_name;
		this.label = class_name;
	}
	
	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme()
	{		
		MIPSGenerator.getInstance().create_virtualTable_label(class_name);
	}
	
	
	
}
