package it.polito.tdp.nyc.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import com.javadocmd.simplelatlng.LatLngTool;
import com.javadocmd.simplelatlng.util.LengthUnit;

import it.polito.tdp.nyc.db.NYCDao;

public class Model {
	
	private Graph<City, DefaultWeightedEdge> grafo;
	
	private List<String> providers;
	
	private List<City> vertici;
	
	private NYCDao dao;
	
	private int durata;
	private List<Integer> revisionati;
	
	public Model() {
		  
		this.dao = new NYCDao();
		
		this.providers = dao.getAllProviders();
		
	}
	
	public String creaGrafo(String p) {
		
		this.grafo = new SimpleWeightedGraph<City, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		
		this.vertici = dao.getAllVertici(p);
		
		Graphs.addAllVertices(this.grafo, this.vertici);
		
		for(City c1 : this.vertici) {
			for(City c2 : this.vertici) {
				if(!c1.equals(c2)) {
					double distanza = LatLngTool.distance(c1.getPosizione(), c2.getPosizione(), LengthUnit.KILOMETER);
					Graphs.addEdgeWithVertices(this.grafo, c1, c2, distanza);
				}
			}
		}
		
		String result = "GRAFO CREATO!\n" + "#VERTICI: " + this.grafo.vertexSet().size() + "\n" + "#ARCHI: " + this.grafo.edgeSet().size()+"\n";
		return result;
	}
	
	public List<CityDistance> getAdiacenti(City c){
		List<CityDistance> adiacenti = new ArrayList<CityDistance>();
		
		for(City a : Graphs.neighborListOf(this.grafo, c)) {
			DefaultWeightedEdge e = this.grafo.getEdge(c, a);
			double distance = this.grafo.getEdgeWeight(e);
			CityDistance cd = new CityDistance(a, distance);
			adiacenti.add(cd);
		}
		Collections.sort(adiacenti);
		return adiacenti;
	}

	public List<String> getProviders() {
		return providers;
	}

	public List<City> getVertici() {
		return vertici;
	}
	
	
	public boolean isCreato() {
		if(this.grafo == null)
			return false;
		else
			return true;
	}
	
	public void Simula(City partenza, int N) {
		Simulatore s = new Simulatore(this.grafo, this.vertici);
		s.init(partenza, N);
		s.run();
		this.durata = s.getDurata();
		this.revisionati = s.getRevisionati();
	}

	public int getDurata() {
		return durata;
	}

	public List<Integer> getRevisionati() {
		return revisionati;
	}
	

	
}
