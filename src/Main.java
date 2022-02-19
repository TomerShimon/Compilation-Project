   
import java.io.*;
import java.io.PrintWriter;
import java_cup.runtime.Symbol;
import AST.*;
import IR.*;
import RA.*;
import MIPS.*;

public class Main
{
	static public void main(String argv[])
	{
		Lexer l;
		Parser p;
		Symbol s;
		AST_PROGRAM AST;
		FileReader file_reader;
		PrintWriter file_writer;
		String inputFilename = argv[0];
		String outputFilename = argv[1];
		
		try
		{
			/********************************/
			/* [1] Initialize a file reader */
			/********************************/
			file_reader = new FileReader(inputFilename);

			/********************************/
			/* [2] Initialize a file writer */
			/********************************/
			file_writer = new PrintWriter(outputFilename);
			
			/******************************/
			/* [3] Initialize a new lexer */
			/******************************/
			l = new Lexer(file_reader);
			
			/*******************************/
			/* [4] Initialize a new parser */
			/*******************************/
			p = new Parser(l,file_writer);

			/***********************************/
			/* [5] 3 ... 2 ... 1 ... Parse !!! */
			/***********************************/
			AST = (AST_PROGRAM) p.parse().value;
			
			/*************************/
			/* [6] Print the AST ... */
			/*************************/
			AST.PrintMe();

			/**************************/
			/* [7] Semant the AST ... */
			/**************************/
			try{
				AST.SemantMe();
			}
			catch(SEMANTIC_ERROR_EXCEPTION e)
			{
				file_writer.println(String.format("ERROR(%s)",e.getMessage()));
				file_writer.close();
				return;
			}
			
			/**************************/
			/* [8] IR the code ... */
			/**************************/
			System.out.println("Started IRme()");
			AST.IRme();
			System.out.println("Ended IRme()");
			
			
			/*************************/
			/* [9] Allocate registers*/
			/*************************/
			
			/*
			IRcommandList all_ir_commands = IR.getInstanceAsList();
			//while(all_ir_commands.tail != null){
			//	System.out.println(all_ir_commands.head);
			//	all_ir_commands = all_ir_commands.tail;
			//}
			System.out.println("------------------------------------------------------------------------\n");
			//register_allocation.allocate(IR.getInstanceAsList());
			register_allocation.allocate(all_ir_commands);
			while(all_ir_commands.tail != null){
				System.out.println(all_ir_commands.head);
				all_ir_commands = all_ir_commands.tail;
			}
			System.out.println("------------------------------------------------------------------------\n");
			all_ir_commands = IR.getInstanceAsList();
			while(all_ir_commands.tail != null){
				System.out.println(all_ir_commands.head);
				all_ir_commands = all_ir_commands.tail;
			}
			*/
			/*************************/
			/* [10] Mips the IR commands*/
			/*************************/
			IR.getInstance().beforeMIPSme();

			IR.getInstance().MIPSme();

			/*************************/
			/* [11] Close output file */
			/*************************/
			

			//file_writer.println("OK");
			//file_writer.close();

			/*************************************/
			/* [12] Finalize AST GRAPHIZ DOT file */
			/*************************************/
			AST_GRAPHVIZ.getInstance().finalizeFile();
			MIPSGenerator.getInstance().finalizeFile();	
			file_writer.close();					
    	}
			     
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}


