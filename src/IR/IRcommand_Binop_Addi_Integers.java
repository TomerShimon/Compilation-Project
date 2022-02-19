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

public class IRcommand_Binop_Addi_Integers extends IRcommand
{
	public TEMP dst;
	public TEMP t1;
	int t2;
	
	public IRcommand_Binop_Addi_Integers(TEMP dst,TEMP t1,int t2)
	{
		this.dst = dst;
		this.t1 = t1;
		this.t2 = t2;
		this.temp_out = dst;
		this.temp_in1 = t1;
	}
	/***************/
	/* MIPS me !!! */
	/***************/
	public void MIPSme()
	{
		MIPSGenerator.getInstance().addi(dst,t1,t2);
	}
}
