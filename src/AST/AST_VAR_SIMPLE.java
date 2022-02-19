package AST;
import TYPES.*;
import SYMBOL_TABLE.*;
import VAR.*;
import TEMP.*;
import IR.*;


public class AST_VAR_SIMPLE extends AST_VAR
{
	/************************/
	/* simple variable name */
	/************************/
	public String name;
	
	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_VAR_SIMPLE(String name,int line)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();
	
		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.format("====================== var -> ID( %s )\n",name);

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.name = name;
		this.line = line;
	}

	/**************************************************/
	/* The printing message for a simple var AST node */
	/**************************************************/
	public void PrintMe()
	{
		/**********************************/
		/* AST NODE TYPE = AST SIMPLE VAR */
		/**********************************/
		System.out.format("AST NODE SIMPLE VAR( %s )\n",name);

		/*********************************/
		/* Print to AST GRAPHIZ DOT file */
		/*********************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			String.format("SIMPLE\nVAR\n(%s)",name));
	}
	
	
	
	
	public TYPE SemantMe() throws Exception
	{
		TYPE t = SYMBOL_TABLE.getInstance().find(this.name);
		
		symbol_table_entry = SYMBOL_TABLE.getInstance().findEntry(name); 
		
		if (t == null)
		{
			System.out.format(">> ERROR non existing type %s\n",name);
			throw new SEMANTIC_ERROR_EXCEPTION(String.valueOf(this.line));
		}
		return t;
	}

	public TYPE SemantMe(TYPE returnType, TYPE_CLASS type_class) throws Exception
	{
		TYPE t = SYMBOL_TABLE.getInstance().find_in_func(this.name);
		
		symbol_table_entry = SYMBOL_TABLE.getInstance().findEntry(name); 

		
		if(t == null)
		{
			t = type_class.find_member(this.name);
			if(t == null)
			{
				t = SYMBOL_TABLE.getInstance().find(this.name);
				if(t == null)
				{
					System.out.format(">> ERROR non existing type %s\n",this.name);
					throw new SEMANTIC_ERROR_EXCEPTION(String.valueOf(this.line));
				}
			}
		}
		return t;
	}
	
	
	public TEMP IRme(){
		
		if (symbol_table_entry.scope == 1) {
			TEMP result = TEMP_FACTORY.getInstance().getFreshTEMP();
			IR.getInstance().Add_IRcommand(new IRcommand_Load(result,symbol_table_entry.name));
			return result;
		}

		else if (symbol_table_entry.cls == null) {
			TEMP result = TEMP_FACTORY.getInstance().getFreshTEMP();
			VAR var = new VAR(symbol_table_entry.offset, symbol_table_entry.name, false);
			IR.getInstance().Add_IRcommand(new IRcommand_Load_Var(var, result));

			return result;
		}

		else {
			TEMP instance = TEMP_FACTORY.getInstance().getFreshTEMP();
			TEMP result = TEMP_FACTORY.getInstance().getFreshTEMP();

			VAR var = new VAR(8, symbol_table_entry.name, false);
			IR.getInstance().Add_IRcommand(new IRcommand_Load_Var(var, instance));
			

			int offset = symbol_table_entry.cls.getFieldOffset(symbol_table_entry.name);
			IR.getInstance().Add_IRcommand(new IRcommand_LW(result, offset, instance));

			return result;
		}

	}
	
}
