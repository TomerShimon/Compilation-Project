package AST;
import TYPES.*;
import SYMBOL_TABLE.*;
import IR.*;
import VAR.VAR;
import TEMP.*;

public class AST_EXP_BINOP extends AST_EXP
{
	int OP;
	public AST_EXP left;
	public AST_EXP right;
	TYPE left_type;
	TYPE right_type;
	
	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_EXP_BINOP(AST_EXP left,AST_EXP right,int OP, int line)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.print("====================== exp -> exp BINOP exp\n");

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.left = left;
		this.right = right;
		this.OP = OP;
		this.line = line;
	}
	
	/*************************************************/
	/* The printing message for a binop exp AST node */
	/*************************************************/
	public void PrintMe()
	{
		String sOP="";
		
		/*********************************/
		/* CONVERT OP to a printable sOP */
		/*********************************/
		switch(OP)
		{
		case(0):{sOP = "+";break;}
		case(1):{sOP = "-";break;}
		case(2):{sOP = "*";break;}
		case(3):{sOP = "/";break;}
		case(4):{sOP = "<";break;}
		case(5):{sOP = ">";break;}
		case(6):{sOP = "=";break;}
		}
		/*************************************/
		/* AST NODE TYPE = AST BINOP EXP */
		/*************************************/
		System.out.print("AST NODE BINOP EXP\n");

		/**************************************/
		/* RECURSIVELY PRINT left + right ... */
		/**************************************/
		if (left != null) left.PrintMe();
		if (right != null) right.PrintMe();
		
		/***************************************/
		/* PRINT Node to AST GRAPHVIZ DOT file */
		/***************************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			String.format("BINOP(%s)",sOP));
		
		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		if (left  != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,left.SerialNumber);
		if (right != null) AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,right.SerialNumber);
	}

	public boolean equal_test(TYPE left, TYPE right)
	{
		if(left.isClass() && right.isClass())
		{
			if(left.isinherited(right.name) || right.isinherited(left.name) ) {return true;}
			return false;
		}
		if(left.name.equals("nil"))
		{
			if(!(right.isClass() || right.isArray())) {return false;}
			return true;
		}
		if(right.name.equals("nil"))
		{
			if(!(left.isClass() || left.isArray())) {return false;}
			return true;
		}
		if(right.name.equals(left.name)) {return true;}
		return false;
	}

	public TYPE SemantMe() throws Exception
	{
		left_type = left.SemantMe(); 
		right_type = right.SemantMe(); 
		switch(OP)
		{
		case(0):
			{
			if(left_type.name == "int" && right_type.name == "int")
			{
				return TYPE_INT.getInstance();
			}
			if(left_type.name == "string" && right_type.name == "string")
			{
				return TYPE_STRING.getInstance();
			}
			throw new SEMANTIC_ERROR_EXCEPTION(String.valueOf(this.line));
			}
		case(1):{}
		case(2):{}
		case(3):
			{
				if( right.isZero() && OP==3)
				{
					System.out.format("ERROR zero division error");
					throw new SEMANTIC_ERROR_EXCEPTION(String.valueOf(this.line));
				}
			}
		case(4):{}
		case(5):
			{
			if(left_type.name != "int" || right_type.name != "int")
			{
				System.out.format("incompatible types %s , %s",left_type.name, right_type.name);
				throw new SEMANTIC_ERROR_EXCEPTION(String.valueOf(this.line));
			}
			break;}
		case(6):
			{
			if(!equal_test(left_type, right_type) ) 
			{
				System.out.format("1-incompatible types %s , %s",left_type.name, right_type.name);
				throw new SEMANTIC_ERROR_EXCEPTION(String.valueOf(this.line));
			}
			break;
			}
		}
		return TYPE_INT.getInstance();
	}

	public TYPE SemantMe(TYPE_CLASS type_class) throws Exception
	{
		System.out.format("ERROR, only constant assignments allowed within class");
		throw new SEMANTIC_ERROR_EXCEPTION(String.valueOf(this.line));
	}

	public TYPE SemantMe(TYPE returnType, TYPE_CLASS type_class) throws Exception
	{
		left_type = left.SemantMe(returnType, type_class); 
		right_type = right.SemantMe(returnType, type_class); 
		switch(OP)
		{
		case(0):
			{
			if(left_type.name == "int" && right_type.name == "int")
			{
				return TYPE_INT.getInstance();
			}
			if(left_type.name == "string" && right_type.name == "string")
			{
				return TYPE_STRING.getInstance();
			}
			System.out.format("incompatible types %s , %s",left_type.name, right_type.name);
			throw new SEMANTIC_ERROR_EXCEPTION(String.valueOf(this.line));
			}
		case(1):{}
		case(2):{}
		case(3):{
				if( right.isZero() && OP==3)
				{
					System.out.format("ERROR zero division error");
					throw new SEMANTIC_ERROR_EXCEPTION(String.valueOf(this.line));
				}
			}
		case(4):{}
		case(5):
			{
			if(left_type.name != "int" || right_type.name != "int")
			{
				System.out.format("incompatible types %s , %s",left_type.name, right_type.name);
				throw new SEMANTIC_ERROR_EXCEPTION(String.valueOf(this.line));
			}
			break;}
		case(6):
			{
			if(!equal_test(left_type, right_type) ) 
			{
				System.out.format("1-incompatible types %s , %s",left_type.name, right_type.name);
				throw new SEMANTIC_ERROR_EXCEPTION(String.valueOf(this.line));
			}
			break;
			}
		}
		return TYPE_INT.getInstance();
	}
	
	
	private void addRangeCheck(TEMP dst) {
		TEMP upper_bound_temp = TEMP_FACTORY.getInstance().getFreshTEMP();
		TEMP lower_bound_temp = TEMP_FACTORY.getInstance().getFreshTEMP();
		TEMP correct_upper_bound_temp = TEMP_FACTORY.getInstance().getFreshTEMP();
		TEMP correct_lower_bound_temp = TEMP_FACTORY.getInstance().getFreshTEMP();
		
		IR.getInstance().Add_IRcommand(new IRcommandConstInt(upper_bound_temp, 32768));
		IR.getInstance().Add_IRcommand(new IRcommandConstInt(lower_bound_temp, -32769));
		
		IR.getInstance().Add_IRcommand(new IRcommand_Binop_LT_Integers(correct_upper_bound_temp, dst, upper_bound_temp));
		IR.getInstance().Add_IRcommand(new IRcommand_Binop_LT_Integers(correct_lower_bound_temp, lower_bound_temp, dst));
		
		String correct_upper_bound_label = IRcommand.getFreshLabel("CORRECT_UPPER_BOUND");
		IR.getInstance().Add_IRcommand(new IRcommand_Jump_If_Not_Eq_To_Zero(correct_upper_bound_temp, correct_upper_bound_label));
		IR.getInstance().Add_IRcommand(new IRcommandConstInt(dst, 32767));
		IR.getInstance().Add_IRcommand(new IRcommand_Label(correct_upper_bound_label));
		String correct_lower_bound_label = IRcommand.getFreshLabel("CORRECT_LOWER_BOUND");
		IR.getInstance().Add_IRcommand(new IRcommand_Jump_If_Not_Eq_To_Zero(correct_lower_bound_temp, correct_lower_bound_label));
		IR.getInstance().Add_IRcommand(new IRcommandConstInt(dst, -32768));
		IR.getInstance().Add_IRcommand(new IRcommand_Label(correct_lower_bound_label));
	}

	
	public TEMP IRme()
	{
		TEMP t1 = TEMP_FACTORY.getInstance().getFreshTEMP();
		if (left != null) { t1 = left.IRme(); }
		TEMP t2 = TEMP_FACTORY.getInstance().getFreshTEMP();
		if (right != null) { t2 = right.IRme(); }
		TEMP dst = TEMP_FACTORY.getInstance().getFreshTEMP();
		
	
		if (OP == 0) // +
		{
			if(left_type.name == "int")
			{
				IR.getInstance().Add_IRcommand(new IRcommand_Binop_Add_Integers(dst,t1,t2));
				addRangeCheck(dst);
			}
			else if(left_type.name == "string")
			{
				
				TEMP totalLength = TEMP_FACTORY.getInstance().getFreshTEMP();
				TEMP ptrCopy = TEMP_FACTORY.getInstance().getFreshTEMP();
				TEMP currentByte = TEMP_FACTORY.getInstance().getFreshTEMP();

				String length_str1 = IRcommand.getFreshLabel("length_str1");
				String end_length_str1 = IRcommand.getFreshLabel("end_length_str1");
				String length_str2 = IRcommand.getFreshLabel("length_str2");
				String end_length_str2 = IRcommand.getFreshLabel("end_length_str2");
				String copy_str1 = IRcommand.getFreshLabel("copy_str1");
				String end_copy_str1 = IRcommand.getFreshLabel("end_copy_str1");
				String copy_str2 = IRcommand.getFreshLabel("copy_str2");
				String end_copy_str2 = IRcommand.getFreshLabel("end_copy_str2");


				IR.getInstance().Add_IRcommand(new IRcommandConstInt(totalLength,1));

				IR.getInstance().Add_IRcommand(new IRcommand_Move(ptrCopy,t1));
				IR.getInstance().Add_IRcommand(new IRcommand_Label(length_str1));
					IR.getInstance().Add_IRcommand(new IRcommand_Load_Byte(currentByte,ptrCopy));
					IR.getInstance().Add_IRcommand(new IRcommand_Jump_If_Eq_To_Zero(currentByte,end_length_str1));
					IR.getInstance().Add_IRcommand(new IRcommand_Binop_Addi_Integers(ptrCopy,ptrCopy,1));
					IR.getInstance().Add_IRcommand(new IRcommand_Binop_Addi_Integers(totalLength,totalLength,1));
					IR.getInstance().Add_IRcommand(new IRcommand_Jump_Label(length_str1));
				IR.getInstance().Add_IRcommand(new IRcommand_Label(end_length_str1));

				IR.getInstance().Add_IRcommand(new IRcommand_Move(ptrCopy,t2));
				IR.getInstance().Add_IRcommand(new IRcommand_Label(length_str2));
					IR.getInstance().Add_IRcommand(new IRcommand_Load_Byte(currentByte,ptrCopy));
					IR.getInstance().Add_IRcommand(new IRcommand_Jump_If_Eq_To_Zero(currentByte,end_length_str2));
					IR.getInstance().Add_IRcommand(new IRcommand_Binop_Addi_Integers(ptrCopy,ptrCopy,1));
					IR.getInstance().Add_IRcommand(new IRcommand_Binop_Addi_Integers(totalLength,totalLength,1));
					IR.getInstance().Add_IRcommand(new IRcommand_Jump_Label(length_str2));
				IR.getInstance().Add_IRcommand(new IRcommand_Label(end_length_str2));

				IR.getInstance().Add_IRcommand(new IRcommand_Mallocate(dst,totalLength));

				IR.getInstance().Add_IRcommand(new IRcommand_Move(ptrCopy,t1));
				IR.getInstance().Add_IRcommand(new IRcommand_Label(copy_str1));
					IR.getInstance().Add_IRcommand(new IRcommand_Load_Byte(currentByte,ptrCopy));
					IR.getInstance().Add_IRcommand(new IRcommand_Jump_If_Eq_To_Zero(currentByte,end_copy_str1));
					IR.getInstance().Add_IRcommand(new IRcommand_Store_Byte(currentByte, dst));
					IR.getInstance().Add_IRcommand(new IRcommand_Binop_Addi_Integers(ptrCopy,ptrCopy,1));
					IR.getInstance().Add_IRcommand(new IRcommand_Binop_Addi_Integers(dst,dst,1));
					IR.getInstance().Add_IRcommand(new IRcommand_Jump_Label(copy_str1));
				IR.getInstance().Add_IRcommand(new IRcommand_Label(end_copy_str1));

				IR.getInstance().Add_IRcommand(new IRcommand_Move(ptrCopy,t2));
				IR.getInstance().Add_IRcommand(new IRcommand_Label(copy_str2));
					IR.getInstance().Add_IRcommand(new IRcommand_Load_Byte(currentByte,ptrCopy));
					IR.getInstance().Add_IRcommand(new IRcommand_Jump_If_Eq_To_Zero(currentByte,end_copy_str2));
					IR.getInstance().Add_IRcommand(new IRcommand_Store_Byte(currentByte, dst));
					IR.getInstance().Add_IRcommand(new IRcommand_Binop_Addi_Integers(ptrCopy,ptrCopy,1));
					IR.getInstance().Add_IRcommand(new IRcommand_Binop_Addi_Integers(dst,dst,1));
					IR.getInstance().Add_IRcommand(new IRcommand_Jump_Label(copy_str2));
				IR.getInstance().Add_IRcommand(new IRcommand_Label(end_copy_str2));
				

				IR.getInstance().Add_IRcommand(new IRcommand_Store_Byte(currentByte, dst));

				IR.getInstance().Add_IRcommand(new IRcommand_takeFromV0(dst));
				return dst;					
			}
			
		}
		else if (OP == 1)  // -
		{
			IR.getInstance().Add_IRcommand( new IRcommand_Binop_Sub_Integers(dst,t1,t2) );
			addRangeCheck(dst);	
		}
		else if (OP == 2) // *
		{
			IR.getInstance().Add_IRcommand( new IRcommand_Binop_Mul_Integers(dst,t1,t2) );
			addRangeCheck(dst);
		}
		else if (OP == 3) // /
		{			
			String ValidDivisionLabel = IRcommand.getFreshLabel("VALID_DIVISION");
			IR.getInstance().Add_IRcommand( new IRcommand_Jump_If_Not_Eq_To_Zero(t2,ValidDivisionLabel) );
			IR.getInstance().Add_IRcommand( new IRcommand_PrintString("Division By Zero") );
			IR.getInstance().Add_IRcommand( new IRcommand_Exit_With_Error() );
			
			IR.getInstance().Add_IRcommand( new IRcommand_Label(ValidDivisionLabel) );
			IR.getInstance().Add_IRcommand( new IRcommand_Binop_Div_Integers(dst,t1,t2) );
			addRangeCheck(dst);

		}
		else if (OP == 4) // <
		{
			IR.getInstance().Add_IRcommand( new IRcommand_Binop_LT_Integers(dst,t1,t2) );
		}
		else if (OP == 5) // >
		{
			IR.getInstance().Add_IRcommand( new IRcommand_Binop_LT_Integers(dst,t2,t1) );
		}
		else if (OP == 6) // =
		{
			if ( !left_type.name.equals("string") ) 
			{
				IR.getInstance().Add_IRcommand( new IRcommand_Binop_EQ_Integers(dst,t1,t2) );
			}
			else
			{

				TEMP crt_left_char_temp = TEMP_FACTORY.getInstance().getFreshTEMP();
				TEMP crt_right_char_temp  = TEMP_FACTORY.getInstance().getFreshTEMP();
				
				TEMP chars_equal_temp  = TEMP_FACTORY.getInstance().getFreshTEMP();
				
				TEMP crt_indx_left_temp = TEMP_FACTORY.getInstance().getFreshTEMP();
				IR.getInstance().Add_IRcommand(new IRcommand_Binop_Addi_Integers(crt_indx_left_temp, t1, 0));

				
				TEMP crt_indx_right_temp = TEMP_FACTORY.getInstance().getFreshTEMP();
				IR.getInstance().Add_IRcommand(new IRcommand_Binop_Addi_Integers(crt_indx_right_temp, t2, 0));


				String loopLabel = IRcommand.getFreshLabel("LOOP");
				String notEqualLabel = IRcommand.getFreshLabel("NOT_EQUAL");
				String equalLabel = IRcommand.getFreshLabel("EQUAL");
				String endLabel = IRcommand.getFreshLabel("END");
				
				IR.getInstance().Add_IRcommand(new IRcommand_Label(loopLabel));
				IR.getInstance().Add_IRcommand(new IRcommand_Load_Byte(crt_left_char_temp, crt_indx_left_temp));
				IR.getInstance().Add_IRcommand(new IRcommand_Load_Byte(crt_right_char_temp, crt_indx_right_temp));
				IR.getInstance().Add_IRcommand(new IRcommand_Binop_EQ_Integers(chars_equal_temp, crt_left_char_temp, crt_right_char_temp));


				IR.getInstance().Add_IRcommand(new IRcommand_Jump_If_Eq_To_Zero(chars_equal_temp, notEqualLabel));
				IR.getInstance().Add_IRcommand(new IRcommand_Jump_If_Eq_To_Zero(crt_left_char_temp, equalLabel)); 
				
				IR.getInstance().Add_IRcommand(new IRcommand_Binop_Addi_Integers(crt_indx_right_temp, crt_indx_right_temp, 1));
				IR.getInstance().Add_IRcommand(new IRcommand_Binop_Addi_Integers(crt_indx_left_temp, crt_indx_left_temp, 1));

				IR.getInstance().Add_IRcommand(new IRcommand_Jump_Label(loopLabel));
				
				IR.getInstance().Add_IRcommand(new IRcommand_Label(equalLabel));
				IR.getInstance().Add_IRcommand(new IRcommandConstInt(dst, 1));
				IR.getInstance().Add_IRcommand(new IRcommand_Jump_Label(endLabel));
				IR.getInstance().Add_IRcommand(new IRcommand_Label(notEqualLabel));
				IR.getInstance().Add_IRcommand(new IRcommandConstInt(dst, 0));
				IR.getInstance().Add_IRcommand(new IRcommand_Label(endLabel));

			}
		}

		return dst;
	}
	
}
