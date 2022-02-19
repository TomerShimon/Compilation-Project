package RA;

import java.util.HashSet;


public class node {
	public HashSet<Integer> edges;
	public boolean is_valid;
	public node() {
		this.edges = new HashSet<Integer>();
		this.is_valid = true;
	}
	public void copy(node other) {
		for (int edge : other.edges) {
			edges.add(edge);
		}
	}

}
