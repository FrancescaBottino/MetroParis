package it.polito.tdp.metroparis.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.event.ConnectedComponentTraversalEvent;
import org.jgrapht.event.EdgeTraversalEvent;
import org.jgrapht.event.TraversalListener;
import org.jgrapht.event.VertexTraversalEvent;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import org.jgrapht.traverse.BreadthFirstIterator;

import it.polito.tdp.metroparis.db.MetroDAO;

public class Model {

	//grafo sarà semplice, orientato, non pesato
	
	Graph<Fermata, DefaultEdge> grafo;
	MetroDAO dao;
	Map<Fermata, Fermata> predecessore;
	
	
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
			
			this.predecessore= new HashMap<>();
			this.predecessore.put(partenza, null);
			
			
			//interfaccia TraversalListener, generata inline
			bfv.addTraversalListener(new TraversalListener<Fermata, DefaultEdge>(){
				

				@Override
				public void connectedComponentFinished(ConnectedComponentTraversalEvent e) {
					
				}

				@Override
				public void connectedComponentStarted(ConnectedComponentTraversalEvent e) {
					
					
				}

				@Override
				public void edgeTraversed(EdgeTraversalEvent<DefaultEdge> e) {
					//Chiamato ogni volta che algoritmo attraversa un nuovo arco 
					//e: parametro passato dal metodo per descrivere cosa è successo, un evento (ho attraversato arco)
					
					
					DefaultEdge arco = e.getEdge();
					Fermata a=grafo.getEdgeSource(arco);
					Fermata b=grafo.getEdgeTarget(arco);
					//cioè ho scoperto a arrivando da b --> se b lo conoscevo già (è una chiave)
					
					if(predecessore.containsKey(b) && !predecessore.containsKey(a)) {
						//b è sorgente 
						predecessore.put(a, b);
						//System.out.println(a+" scoperto da "+b);
					}else if(predecessore.containsKey(a) && !predecessore.containsKey(b)){
						//conoscevo a e quindi ho scoperto b
						predecessore.put(b, a);
						//System.out.println(b+" scoperto da "+a);
					}
					
					
				}

				@Override
				public void vertexTraversed(VertexTraversalEvent<Fermata> e) {
					//System.out.println(e.getVertex());
					
					/*
					Fermata nuova= e.getVertex();
					Fermata precedente = vertice adiacente a 'nuova' che sia già raggiunto (già presente nelle keys della mappa)
					predecessore.put(nuova, precedente);
					*/
				}

				@Override
				public void vertexFinished(VertexTraversalEvent<Fermata> e) {
					
				}
				});
			
			
			
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
		//uguale con
		//DepthFirstIterator<Fermata, DefaultEdge> dfv=new DepthFirstIterator<>(this.grafp, partenza);
	
		public List<Fermata> trovaCammino(Fermata partenza, Fermata arrivo) {
			
			
			//visita del grafo partendo da partenza
			fermateRaggiungibili(partenza); //tutti i vertici del grafo partendo da partenza + si crea mappa dei predecessori
			
			List<Fermata> cammino= new LinkedList<Fermata>();
			
			cammino.add(arrivo); //metto dentro arrivo e torno indietro sui predecessori
			
			Fermata f=arrivo; //segnaposto
			
			while(predecessore.get(f) != null) {
				//finchè predecessore di f non è null
				
				f=predecessore.get(f);
				cammino.add(0,f);
				
				
			}
			
			return cammino; //in orine inverso con solo add, in ordine giusto con add(indice, elemento)
			
		}
	

}
