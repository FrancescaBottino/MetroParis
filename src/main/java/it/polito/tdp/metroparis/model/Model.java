package it.polito.tdp.metroparis.model;

import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import it.polito.tdp.metroparis.db.MetroDAO;

public class Model {

	//grafo sarà semplice, orientato, non pesato
	
	Graph<Fermata, DefaultEdge> grafo;
	MetroDAO dao;
	
	
	public void creaGrafo() {
		
		this.grafo=new SimpleGraph<>(DefaultEdge.class);
		
		//aggiungo vertici
		
		this.dao=new MetroDAO();
		
		List<Fermata> fermate= dao.getAllFermate();
		
		//for(Fermata f: fermate)
			//this.grafo.addVertex(f);
		
		Graphs.addAllVertices(this.grafo, fermate);
		
		//GRAPHS classe di utilità di metodi statici --> es. addAllVertices anzichè for 
		
		
		//Aggiungo archi
		//data una coppia di vertici, sono collegati o no?
		/*
		for(Fermata f1: this.grafo.vertexSet()) {
			
			for(Fermata f2 : this.grafo.vertexSet()) {
				
				if(!f1.equals(f2) && dao.fermateCollegate(f1, f2))
					this.grafo.addEdge(f1, f2);
				
			}
		}*/
		
		//modo più intelligente --> interrogo direttamente la tabella Connessione
		
		List<Connessione> connessioni=dao.getAllConnessioni(fermate);
		for(Connessione c: connessioni) {
			this.grafo.addEdge(c.getStazP(), c.getStazA());
			
		}
		
		System.out.format("Grafo creato con %d vertici e %d archi\n",
				this.grafo.vertexSet().size(), this.grafo.edgeSet().size()) ;
	}
	
	

}
