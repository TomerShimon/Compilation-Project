package RA;

import java.util.HashSet;

public class Control_Flow_Graph {
	public int ir_command_number;
	public HashSet<Integer> successors;
	public int right_temp1;   
	public int right_temp2; 
	public int left_temp; 
	public String branch;
	public HashSet<String> related_labels;
	public Control_Flow_Graph(int ir_command_number,int left_temp, int right_temp1, int right_temp2, String branch) {
		this.ir_command_number = ir_command_number;
		this.left_temp = left_temp;
		this.right_temp1 = right_temp1;
		this.right_temp2 = right_temp2;
		this.related_labels = new HashSet<String>();
		this.successors = new HashSet<Integer>();
		this.branch = branch;
	}
	
	

}
