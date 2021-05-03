package it.polito.tdp.metroparis.model;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

public class ProvaGrafo {

	public static void main(String[] args) {
		
		Graph<String, DefaultEdge> grafo = new SimpleGraph<>(DefaultEdge.class);
		
		grafo.addVertex("UNO");
		grafo.addVertex("DUE");
		grafo.addVertex("TRE");
		
		System.out.println(grafo); //([UNO, DUE, TRE], []) insieme archi vuoto

		grafo.addEdge("UNO", "DUE");
		
		System.out.println(grafo); // + [{UNO,DUE}]) non orientato

	
		
		
	}


}
