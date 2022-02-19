package RA;

import java.sql.RowId;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Stack;
import IR.*;

public class register_allocation {

	public static void allocate(IRcommandList lst) {
		Control_Flow_Graph[] rows = translate_from_ir_to_cfg(lst);
		IRcommandList lst_start = lst;
		int number_of_rows_in_cfg = rows.length;
		List<HashSet<Integer>> in = new ArrayList<HashSet<Integer>>();
		List<HashSet<Integer>> out = new ArrayList<HashSet<Integer>>();
		List<HashSet<Integer>> prev_in = new ArrayList<HashSet<Integer>>();
		List<HashSet<Integer>> prev_out = new ArrayList<HashSet<Integer>>();
		for (int i = 0; i < number_of_rows_in_cfg; i++) {
			HashSet<Integer> s1 = new HashSet<Integer>();
			HashSet<Integer> s2 = new HashSet<Integer>();
			HashSet<Integer> s3 = new HashSet<Integer>();
			HashSet<Integer> s4 = new HashSet<Integer>();
			in.add(s1);
			out.add(s2);
			prev_in.add(s3);
			prev_out.add(s4);
		}

		boolean reached_a_fixed_point = false;
		boolean first_iteration = true;
		while (!reached_a_fixed_point) {
			for (int i = number_of_rows_in_cfg - 1; i >= 0; i--) {
				for (int successor : rows[i].successors) {
					for (int live_variable : in.get(successor)) {
						out.get(i).add(live_variable);
					}
				}
				HashSet<Integer> s = new HashSet<Integer>();
				if (rows[i].right_temp1 != -1) {
					s.add(rows[i].right_temp1);
				}
				if (rows[i].right_temp2 != -1) {
					s.add(rows[i].right_temp2);
				}
				for (int temp : out.get(i)) {
					if (temp != rows[i].left_temp) {
						s.add(temp);
					}
				}
				in.set(i, s);
			}
			if (!first_iteration) {
				reached_a_fixed_point = comapre_cfg_to_previous_iteration(in, out, prev_in, prev_out);
			}
			if (!reached_a_fixed_point) {
				for (int i = 0; i < number_of_rows_in_cfg; i++) {
					HashSet<Integer> copy_of_out = new HashSet<Integer>();
					HashSet<Integer> copy_of_in = new HashSet<Integer>();
					for (int live_variable : out.get(i)) {
						copy_of_out.add(live_variable);
					}
					for (int live_variable : in.get(i)) {
						copy_of_in.add(live_variable);
					}
					prev_out.set(i, copy_of_out);
					prev_in.set(i, copy_of_in);
				}
			}
			first_iteration = false;
		}
		lst = lst_start;
		List<Integer> from_temp_to_register = color_graph(in, out, lst);
		lst = lst_start;
		while (lst != null) {
			if (lst.head.temp_out != null && lst.head.temp_out.changed_to_register_number == false) {
				lst.head.temp_out.serial = from_temp_to_register.get(lst.head.temp_out.serial);
				lst.head.temp_out.changed_to_register_number = true;
			}
			if (lst.head.temp_in1 != null && lst.head.temp_in1.changed_to_register_number == false) {
				lst.head.temp_in1.serial = from_temp_to_register.get(lst.head.temp_in1.serial);
				lst.head.temp_in1.changed_to_register_number = true;
			}
			if (lst.head.temp_in2 != null && lst.head.temp_in2.changed_to_register_number == false) {
				lst.head.temp_in2.serial = from_temp_to_register.get(lst.head.temp_in2.serial);
				lst.head.temp_in2.changed_to_register_number = true;
			}
			lst = lst.tail;
		}
	}

	public static Control_Flow_Graph[] translate_from_ir_to_cfg(IRcommandList lst) {
		HashSet<String> current_labels = new HashSet<String>();
		int number_of_rows = 0;
		int ir_command_number = 0;
		int row_number = 0;
		String branch = null;
		IRcommandList lst_start = lst;
		while (lst != null) {
			if (lst.head.label == null) {
				number_of_rows++;
			}
			lst = lst.tail;
		}
		lst = lst_start;
		Control_Flow_Graph[] rows = new Control_Flow_Graph[number_of_rows];
		while (lst != null) {
			if (lst.head.label != null) {
				current_labels.add(lst.head.label);
			}
			else {
				int temp_out = -1, temp_in1 = -1, temp_in2 = -1;
				if (lst.head.temp_out != null) {
					temp_out = lst.head.temp_out.getSerialNumber();
				}
				if (lst.head.temp_in1 != null) {
					temp_in1 = lst.head.temp_in1.getSerialNumber();
				}
				if (lst.head.temp_in2 != null) {
					temp_in2 = lst.head.temp_in2.getSerialNumber();
				}
				Control_Flow_Graph row = new Control_Flow_Graph(ir_command_number, temp_out, temp_in1, temp_in2, lst.head.branch);
				for (String label : current_labels) {
					row.related_labels.add(label);
				}
				current_labels.clear();
				rows[row_number] = row;
				row_number++;
			}
			ir_command_number++;
			lst = lst.tail;

		}
		for (int i = 0; i < number_of_rows - 1; i++) {
			if (rows[i].branch != null && rows[i].left_temp == -1 && rows[i].right_temp1 == -1 && rows[i].right_temp2 == -1) {
			}
			else {
				rows[i].successors.add(i + 1);
			}
		}
		for (int i = 0; i < number_of_rows; i++) {
			if (rows[i].branch != null) {
				for (int j = 0; j < number_of_rows; j++) {
					if (rows[j].related_labels.contains(rows[i].branch)) {
						rows[i].successors.add(j);
						break;
					}
				}
			}
		}
		return rows;
	}

	public static boolean comapre_cfg_to_previous_iteration(List<HashSet<Integer>> in, List<HashSet<Integer>> out, List<HashSet<Integer>> prev_in, List<HashSet<Integer>> prev_out) {
		for (int i = 0; i < in.size(); i++) {
			if (!in.get(i).equals(prev_in.get(i))) {
				return false;
			}
			if (!out.get(i).equals(prev_out.get(i))) {
				return false;
			}
		}
		return true;
	}

	public static int count_temps(IRcommandList lst) {
		HashSet<Integer> all_temps = new HashSet<Integer>();
		while (lst != null) {
			if (lst.head.temp_out != null) {
				all_temps.add(lst.head.temp_out.serial);
			}
			if (lst.head.temp_in1 != null) {
				all_temps.add(lst.head.temp_in1.serial);
			}
			if (lst.head.temp_in2 != null) {
				all_temps.add(lst.head.temp_in2.serial);
			}
			lst = lst.tail;
		}
		int max = 0;
		for (int temp : all_temps){
			if(temp>max){
				max = temp;
			}
		}
		return max+1;
	}

	public static List<Integer> color_graph(List<HashSet<Integer>> in, List<HashSet<Integer>> out, IRcommandList lst) {
		int number_of_temps = count_temps(lst);
		List<node> nodes_in_graph = new ArrayList<node>();
		for (int i = 0; i < number_of_temps; i++) {
			node v = new node();
			nodes_in_graph.add(v);
		}
		for (int i = 0; i < in.size(); i++) {
			Object[] all_pairs = in.get(i).toArray();
			for (int j = 0; j < all_pairs.length; j++) {
				for (int k = 0; k < all_pairs.length; k++) {
					if (k != j && !nodes_in_graph.get((int) all_pairs[j]).edges.contains((int) all_pairs[k])) {
						nodes_in_graph.get((int) all_pairs[j]).edges.add((int) all_pairs[k]);
					}
				}
			}
		}
		for (int i = 0; i < out.size(); i++) {
			Object[] all_pairs = out.get(i).toArray();
			for (int j = 0; j < all_pairs.length; j++) {
				for (int k = 0; k < all_pairs.length; k++) {
					if (k != j && !nodes_in_graph.get((int) all_pairs[j]).edges.contains((int) all_pairs[k])) {
						nodes_in_graph.get((int) all_pairs[j]).edges.add((int) all_pairs[k]);
					}
				}
			}
		}
		List<node> original_graph = new ArrayList<node>();
		for (int i = 0; i < nodes_in_graph.size(); i++) {
			node v = new node();
			v.copy(nodes_in_graph.get(i));
			original_graph.add(v);
		}
		Stack<Integer> my_stack = new Stack<Integer>();
		while (my_stack.size() < nodes_in_graph.size()) {
			int pos = 0;
			while (pos < nodes_in_graph.size()) {
				if (nodes_in_graph.get(pos).is_valid && nodes_in_graph.get(pos).edges.size() < 10) {
					nodes_in_graph.get(pos).is_valid = false;
					my_stack.add(pos);
					for (int v : nodes_in_graph.get(pos).edges) {
						nodes_in_graph.get(v).edges.remove(pos);
					}
				}
				pos++;
			}
		}
		List<Integer> color = new ArrayList<Integer>();
		for (int i = 0; i < number_of_temps; i++) {
			color.add(-1);
		}
		for (int i = 0; i < nodes_in_graph.size(); i++) {
			int node_number = my_stack.pop();
			boolean color_is_legal = true;
			for (int j = 0; j < 10; j++) {
				for (int v : original_graph.get(node_number).edges) {
					if (color.get(v) == j) {
						color_is_legal = false;
					}
				}
				if (color_is_legal) {
					color.set(node_number, j);
					break;
				}
				color_is_legal = true;
			}
		}
		return color;
	}
}
