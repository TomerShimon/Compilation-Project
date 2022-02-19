/***********/
/* PACKAGE */
/***********/
package MIPS;

/*******************/
/* GENERAL IMPORTS */
/*******************/
import java.io.PrintWriter;

/*******************/
/* PROJECT IMPORTS */
/*******************/
import TEMP.*;

public class MIPSGenerator
{
	private int WORD_SIZE=4;
	/***********************/
	/* The file writer ... */
	/***********************/
	private PrintWriter fileWriter;

	/***********************/
	/* The file writer ... */
	/***********************/
	public void finalizeFile()
	{
		fileWriter.print("\tli $v0,10\n");
		fileWriter.print("\tsyscall\n");
		fileWriter.close();
	}
	public void print_int(TEMP t)
	{
		int idx=t.getSerialNumber();
		fileWriter.format("\tmove $a0,$t%d\n",idx);
		fileWriter.format("\tli $v0,1\n");
		fileWriter.format("\tsyscall\n");
		fileWriter.format("\tli $a0,32\n");
		fileWriter.format("\tli $v0,11\n");
		fileWriter.format("\tsyscall\n");
	}
	
	public void print_string_temp(TEMP t)
	{
		int idx=t.getSerialNumber();
		fileWriter.format("\tmove $a0,$t%d\n",idx);
		fileWriter.format("\tli $v0,4\n");
		fileWriter.format("\tsyscall\n");
	}
	public void allocate(String var_name)
	{
		fileWriter.format(".data\n");
		fileWriter.format("\tglobal_%s: .word 721\n",var_name); 
		fileWriter.format(".text\n");
	}
	public void allocateString(String str_label, String str) 
	{
		fileWriter.format(".data\n");
		fileWriter.format("\t%s: .asciiz %s\n", str_label, str);
		fileWriter.format(".text\n");
	}
	
	public void load(TEMP dst,String var_name)
	{
		int idxdst=dst.getSerialNumber();
		fileWriter.format("\tlw $t%d,global_%s\n",idxdst,var_name);
	}
	
	public void load_byte(TEMP dst, TEMP address) 
	{
		int idxdst=dst.getSerialNumber();
		int idxaddress=address.getSerialNumber();
		
		fileWriter.format("\tlb $t%d,0($t%d)\n",idxdst,idxaddress);
	}
	
	
	public void print_string(String s, int i) { 
		fileWriter.format(".data\n");
		fileWriter.format("myLovelyStr_%d: .asciiz \"%s\"\n", i, s);
		fileWriter.format(".text\n");
		fileWriter.format("\tla $a0, myLovelyStr_%d\n", i);
		fileWriter.format("\tli $v0, 4\n");
		fileWriter.format("\tsyscall\n");
		fileWriter.format(".text\n");
	}
	public void exit_program() { 
		fileWriter.format("\tli $v0, 10\n");
		fileWriter.format("\tsyscall\n");
	}
	
	
	public void change_stack(int bytes){ 
		fileWriter.format("\taddi $sp, $sp, %d\n", bytes);
	}	
	public void store_local_sp_offset(TEMP src, int offset){ 
		int idxsrc=src.getSerialNumber();
		fileWriter.format("\tsw $t%d, %d($sp)\n", idxsrc, offset);
	}
	public void load_local_sp_offset(TEMP src, int offset){ 
		int idxsrc=src.getSerialNumber();
		fileWriter.format("\tlw $t%d, %d($sp)\n", idxsrc, offset);
	}
	
	public void store_sp_offset(String reg, int offset){ 
		fileWriter.format("\tsw %s, %d($sp)\n", reg, offset);
	}
	public void load_sp_offset(String reg, int offset){ 
		fileWriter.format("\tlw %s, %d($sp)\n", reg, offset);
	}
	
	public void store_local_fp_offset_temp(int offset, TEMP tmp) {
		int idxtmp=tmp.getSerialNumber();
		fileWriter.format("\tsw $t%d, %d($fp)\n", idxtmp, offset);
	}
	
	
	
	public void load_local_fp_offset_temp(TEMP tmp, int offset) {
		int idxtmp=tmp.getSerialNumber();
		fileWriter.format("\tlw $t%d, %d($fp)\n", idxtmp, offset);
	}
	
	public void load_general(TEMP dst, int offset, TEMP src){ 
		int dstidx=dst.getSerialNumber();
		int srcidx=src.getSerialNumber();
		fileWriter.format("\tlw $t%d, %d($t%d)\n", dstidx, offset, srcidx);
	}

	
	public void load_func_address(TEMP dst,String label) 
	{
		int idxdst=dst.getSerialNumber();
		fileWriter.format("\tla $t%d, func_%s\n",idxdst,label);
		
	}
	public void load_string_address(TEMP dst,String label)
	{
		int idxdst=dst.getSerialNumber();
		fileWriter.format("\tla $t%d, %s\n",idxdst,label);
		
	}

	public void move_fp_to_sp(){
		fileWriter.format("\tmove $fp, $sp\n");
	}
	public void move_sp_to_fp(){
		fileWriter.format("\tmove $sp, $fp\n");
	}
	
	public void call_function(String function_label){ 
		fileWriter.format("\tjal %s\n", function_label);
	}

	public void store(String var_name,TEMP src)
	{
		int idxsrc=src.getSerialNumber();
		fileWriter.format("\tsw $t%d,global_%s\n",idxsrc,var_name);		
	}	
	public void store_to_register(TEMP dst, int offset, TEMP src) 
	{
		int dstidx=dst.getSerialNumber();
		int srcidx=src.getSerialNumber();

		fileWriter.format("\tsw $t%d, %d($t%d)\n",dstidx, offset, srcidx);		
	}	
	
	public void store_byte(TEMP src,TEMP address) 
	{
		int idxsrc=src.getSerialNumber();
		int idxaddress=address.getSerialNumber();

		fileWriter.format("\tsb $t%d,0($t%d)\n",idxsrc,idxaddress);		
	}
	
	public void li(TEMP t,int value)
	{
		int idx=t.getSerialNumber();
		fileWriter.format("\tli $t%d,%d\n",idx,value);
	}
	
	public void make_zero(TEMP dst) { 
		int dstidx=dst.getSerialNumber();
		fileWriter.format("\tadd $t%d,$zero,$zero\n",dstidx);
	}

	public void move(TEMP dst,TEMP src)
	{
		int dstidx=dst.getSerialNumber();
		int srcidx=src.getSerialNumber();

		fileWriter.format("\tmove $t%d,$t%d\n",dstidx,srcidx);
	}
	
	
	
	public void takeFromV0(TEMP t) 
	{
		int tidx=t.getSerialNumber();
		fileWriter.format("\tmove $t%d,$v0\n",tidx);
	}
	public void moveToV0(TEMP t) 
	{
		int tidx=t.getSerialNumber();
		fileWriter.format("\tmove $v0,$t%d\n",tidx);
	}
	public void move_v0(TEMP t) {
		int idx=t.getSerialNumber();
		fileWriter.format("\tmove $t%d, $v0\n",idx);
	}
	

	public void add(TEMP dst,TEMP oprnd1,TEMP oprnd2)
	{
		int i1 =oprnd1.getSerialNumber();
		int i2 =oprnd2.getSerialNumber();
		int dstidx=dst.getSerialNumber();

		fileWriter.format("\tadd $t%d,$t%d,$t%d\n",dstidx,i1,i2);
	}
	public void addi(TEMP dst,TEMP oprnd1,int oprnd2) 
	{
		int i1 =oprnd1.getSerialNumber();
		int dstidx=dst.getSerialNumber();

		fileWriter.format("\taddi $t%d,$t%d,%d\n",dstidx,i1,oprnd2);
	}	
	public void addi_reg(String reg_dst, String t, int num) 
	{
		fileWriter.format("\taddi %s, %s,%d\n", reg_dst, t, num);
	}
	
	public void sub(TEMP dst,TEMP oprnd1,TEMP oprnd2) 
	{
		int i1 =oprnd1.getSerialNumber();
		int i2 =oprnd2.getSerialNumber();
		int dstidx=dst.getSerialNumber();

		fileWriter.format("\tsub $t%d,$t%d,$t%d\n",dstidx,i1,i2);
	}
	
	
	public void mallocate(TEMP dst,TEMP bytes) 
	{
		int bytesidx =bytes.getSerialNumber();
		int dstidx   =dst.getSerialNumber();


		fileWriter.format("\tadd $a0,$t%d,$zero\n",bytesidx);
		fileWriter.format("\tli $v0, 9\n");
		fileWriter.format("\tsyscall\n");
		fileWriter.format("\tmove $t%d,$v0\n",dstidx);		
	}
	

	public void jr_ra() 
	{
		fileWriter.format("\tjr $ra\n");
	}
	public void jump(String inlabel)
	{
		fileWriter.format("\tj %s\n",inlabel);
	}	
	public void jal(String label){ 
		fileWriter.format("\tjal func_%s\n", label);
	}
	public void jalr(TEMP reg){ 
		int regidx   =reg.getSerialNumber();
		fileWriter.format("\tjalr $t%d\n", regidx);
	}
	
	

	public void label(String inlabel)
	{
		if (inlabel.equals("main"))
		{
			fileWriter.format(".text\n");
			fileWriter.format("%s:\n",inlabel);
		}
		else
		{
			fileWriter.format("%s:\n",inlabel);
		}
	}	
	public void create_virtualTable_label(String class_name){ 
		fileWriter.format(".data\n");
		fileWriter.format("\tvt_%s:\n", class_name);
	}
	public void add_virtualTable_entry(String class_method_name){ 
		fileWriter.format("\t.word %s\n", class_method_name);
	}
	public void load_virtual_table(TEMP dst,String cls_name){ 
		int idxdst=dst.getSerialNumber();
		fileWriter.format("\tla $t%d, vt_%s\n",idxdst,cls_name);
	}



	public void div(TEMP dst,TEMP oprnd1,TEMP oprnd2) 
	{
		int i1 =oprnd1.getSerialNumber();
		int i2 =oprnd2.getSerialNumber();
		int dstidx=dst.getSerialNumber();

		fileWriter.format("\tdiv $t%d,$t%d,$t%d\n",dstidx,i1,i2);
	}
	public void mul(TEMP dst,TEMP oprnd1,TEMP oprnd2)
	{
		int i1 =oprnd1.getSerialNumber();
		int i2 =oprnd2.getSerialNumber();
		int dstidx=dst.getSerialNumber();

		fileWriter.format("\tmul $t%d,$t%d,$t%d\n",dstidx,i1,i2);
	}
	public void blt(TEMP oprnd1,TEMP oprnd2,String label)
	{
		int i1 =oprnd1.getSerialNumber();
		int i2 =oprnd2.getSerialNumber();
		
		fileWriter.format("\tblt $t%d,$t%d,%s\n",i1,i2,label);				
	}
	public void bge(TEMP oprnd1,TEMP oprnd2,String label)
	{
		int i1 =oprnd1.getSerialNumber();
		int i2 =oprnd2.getSerialNumber();
		
		fileWriter.format("\tbge $t%d,$t%d,%s\n",i1,i2,label);				
	}
	public void bne(TEMP oprnd1,TEMP oprnd2,String label)
	{
		int i1 =oprnd1.getSerialNumber();
		int i2 =oprnd2.getSerialNumber();
		
		fileWriter.format("\tbne $t%d,$t%d,%s\n",i1,i2,label);				
	}
	public void beq(TEMP oprnd1,TEMP oprnd2,String label)
	{
		int i1 =oprnd1.getSerialNumber();
		int i2 =oprnd2.getSerialNumber();
		
		fileWriter.format("\tbeq $t%d,$t%d,%s\n",i1,i2,label);				
	}
	public void startTextSection() {
		fileWriter.format(".text\n");
		} 
	public void bnez(TEMP oprnd1,String label) 
	{
		int i1 =oprnd1.getSerialNumber();
		fileWriter.format("\tbne $t%d,$zero,%s\n",i1,label);
	}	
	public void beqz(TEMP oprnd1,String label)
	{
		int i1 =oprnd1.getSerialNumber();
				
		fileWriter.format("\tbeq $t%d,$zero,%s\n",i1,label);				
	}
	
	/**************************************/
	/* USUAL SINGLETON IMPLEMENTATION ... */
	/**************************************/
	private static MIPSGenerator instance = null;

	/*****************************/
	/* PREVENT INSTANTIATION ... */
	/*****************************/
	protected MIPSGenerator() {}

	/******************************/
	/* GET SINGLETON INSTANCE ... */
	/******************************/
	public static MIPSGenerator getInstance()
	{
		if (instance == null)
		{
			/*******************************/
			/* [0] The instance itself ... */
			/*******************************/
			instance = new MIPSGenerator();

			try
			{
				/*********************************************************************************/
				/* [1] Open the MIPS text file and write data section with error message strings */
				/*********************************************************************************/
				String dirname="./output/";
				String filename=String.format("MIPS.txt");

				/***************************************/
				/* [2] Open MIPS text file for writing */
				/***************************************/
				instance.fileWriter = new PrintWriter(dirname+filename);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}

			/*****************************************************/
			/* [3] Print data section with error message strings */
			/*****************************************************/
			instance.fileWriter.print(".data\n");
			instance.fileWriter.print("string_access_violation: .asciiz \"Access Violation\"\n");
			instance.fileWriter.print("string_illegal_div_by_0: .asciiz \"Illegal Division By Zero\"\n");
			instance.fileWriter.print("string_invalid_ptr_dref: .asciiz \"Invalid Pointer Dereference\"\n");
			instance.fileWriter.print(".text\n");
		}
		return instance;
	}
}
