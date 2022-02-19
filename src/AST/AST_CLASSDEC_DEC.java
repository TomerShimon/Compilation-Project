package AST;
import TYPES.*;
import SYMBOL_TABLE.*;
import TEMP.*;
import IR.*;

public class AST_CLASSDEC_DEC extends AST_CLASSDEC
{
	/****************/
	/* DATA MEMBERS */
	/****************/
	public String id;
	public AST_CFIELD_LIST cField_lst;

	/******************/
	/* CONSTRUCTOR(S) */
	/******************/
	public AST_CLASSDEC_DEC(String id, AST_CFIELD_LIST cField_lst, int line)
	{
		/******************************/
		/* SET A UNIQUE SERIAL NUMBER */
		/******************************/
		SerialNumber = AST_Node_Serial_Number.getFresh();

		/***************************************/
		/* PRINT CORRESPONDING DERIVATION RULE */
		/***************************************/
		System.out.print("====================== classDec -> CLASS ID EXTENDS ID LBRACE cFieldList RBRACE\n");

		/*******************************/
		/* COPY INPUT DATA NENBERS ... */
		/*******************************/
		this.id = id;
		this.cField_lst = cField_lst;
		this.line = line;
	}

	/******************************************************/
	/* The printing message for a statement list AST node */
	/******************************************************/
	public void PrintMe()
	{
		/**************************************/
		/* AST NODE TYPE = AST STATEMENT LIST */
		/**************************************/
		System.out.print("AST NODE CLASSDEC_DEC\n");

		/*************************************/
		/* RECURSIVELY PRINT ... */
		/*************************************/
		cField_lst.PrintMe();

		/**********************************/
		/* PRINT to AST GRAPHVIZ DOT file */
		/**********************************/
		AST_GRAPHVIZ.getInstance().logNode(
			SerialNumber,
			String.format("classDec %s\n",id));
		
		/****************************************/
		/* PRINT Edges to AST GRAPHVIZ DOT file */
		/****************************************/
		AST_GRAPHVIZ.getInstance().logEdge(SerialNumber,cField_lst.SerialNumber);

	}

	public TYPE SemantMe() throws Exception
	{
		TYPE prev = SYMBOL_TABLE.getInstance().find(id);
		if(prev != null)
		{
			System.out.format("ERROR name already exists: %s\n",id);
			throw new SEMANTIC_ERROR_EXCEPTION(String.valueOf(this.line));
		}	
		/*************************/
		/* [1] Begin Class Scope */
		/*************************/
		SYMBOL_TABLE.getInstance().beginScope();

		/***************************/
		/* [2] Semant Data Members */
		/***************************/
		TYPE_CLASS t = new TYPE_CLASS(null,id,null);
		symbol_table_entry = SYMBOL_TABLE.getInstance().enter(id,t); 
		for (AST_CFIELD_LIST it = cField_lst; it  != null; it = it.tail)
		{
			TYPE_CLASS_MEMBER typ = it.head.SemantMe(t);


			if(typ!= null)
			{
				t.data_members = new TYPE_CLIST(typ,t.data_members);
				t.field_members = new TYPE_CLIST(typ,t.field_members);
				SYMBOL_TABLE_ENTRY ste = SYMBOL_TABLE.getInstance().enter(typ.name,typ.member_type); 
				ste.cls = t; 
				
			}
			else{
				TYPE_CLASS_MEMBER method = new TYPE_CLASS_MEMBER(((AST_CFIELD_FUNC)it.head).funcdec.name, null);
				t.method_members = new TYPE_CLIST(method,t.method_members);
			}
		}

		/*****************/
		/* [3] End Scope */
		/*****************/
		SYMBOL_TABLE.getInstance().endScope();
		
		// enter class to symbol table
		SYMBOL_TABLE.getInstance().enter(id,t);

		/************************************************/
		/* [4] Enter the Class Type to the Symbol Table */
		/************************************************/

		/*********************************************************/
		/* [5] Return value is irrelevant for class declarations */
		/*********************************************************/
		return null;		
	}
	
	public TEMP IRme() {
		TYPE_CLASS cls_type = (TYPE_CLASS) symbol_table_entry.type;
		TYPE_CLIST all_class_methods = cls_type.getRelevantDataMembersMethods();
		
		IR.getInstance().Add_IRcommand(new IRcommand_Start_virtualTable(cls_type.name));	
		while (all_class_methods != null) {
			String class_name = id;
			String class_method_name = all_class_methods.head.name;
			IR.getInstance().Add_IRcommand(new IRcommand_Add_virtualTable_entry(class_method_name));	
			all_class_methods = all_class_methods.tail;
		}
		IR.getInstance().Add_IRcommand(new IRcommand_startTextSection());
		cField_lst.IRme();
		return null;		
	}
	
	
	
}
