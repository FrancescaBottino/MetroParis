package it.polito.tdp.metroparis.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import org.jgrapht.traverse.BreadthFirstIterator;

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
		
		
		/*GRAFO NON ORIENTATO
		Fermata f;
		Set<DefaultEdge> archi=this.grafo.edgesOf(f);
		
		/*
		for(DefaultEdge e: archi) {
			
			Fermata f1=this.grafo.getEdgeSource(e);
			//oppure (A-B) (B-A)
			Fermata f2=this.grafo.getEdgeTarget(e); 
			
			if(f1.equals(f)) {
				//f2 è quello che mi serve
			}else
			{
				//f1 è quello che mi serve
			}
			
			
			//OPPURE FACCIO 
			f1= Graphs.getOppositeVertex(this.grafo, e, f); //prende grafo, vertice, arco associato ad e --> vertice opposto
	}
			
	
			List<Fermata> fermateAdiacenti=Graphs.successorListOf(this.grafo, f); //meglio ancora 
			
			//PER GRAFO ORIENTATO
			
			Graphs.predecessorListOf(this.grafo, f); //anche 
			*/
		
	}
	
	//ALGORITMO VISITA IN AMPIEZZA
		public List<Fermata> fermateRaggiungibili(Fermata partenza){
			
			BreadthFirstIterator<Fermata, DefaultEdge> bfv= new BreadthFirstIterator<>(this.grafo, partenza);
			
			
			List<Fermata> result= new ArrayList<>();
			
			while(bfv.hasNext()) {
				
				Fermata f= bfv.next();
				result.add(f);
			}
			
			return result;
			
			
		}
		
		public Fermata trovaFermata(String nome) {
			
			for(Fermata f: this.grafo.vertexSet()) {
				if(f.getNome().equals(nome))
					return f;
			}
			
			return null;
		}
		
		
		//ALGORITMO VISITA IN PROFONDITA
		//DepthFirstIterator<Fermata, DefaultEdge> dfv=new DepthFirstIterator<>(this.grafp, partenza);
	
	

}
