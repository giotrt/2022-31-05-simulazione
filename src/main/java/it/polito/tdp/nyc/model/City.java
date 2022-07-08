package it.polito.tdp.nyc.model;

import java.util.Objects;

import com.javadocmd.simplelatlng.LatLng;

public class City {

	private String nome;
	private int nHotspot;
	private LatLng posizione;
	
	public City(String nome, int nHotspot, LatLng posizione) {
		super();
		this.nome = nome;
		this.nHotspot = nHotspot;
		this.posizione = posizione;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public int getnHotspot() {
		return nHotspot;
	}

	public void setnHotspot(int nHotspot) {
		this.nHotspot = nHotspot;
	}

	public LatLng getPosizione() {
		return posizione;
	}

	public void setPosizione(LatLng posizione) {
		this.posizione = posizione;
	}

	@Override
	public int hashCode() {
		return Objects.hash(nome);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		City other = (City) obj;
		return Objects.equals(nome, other.nome);
	}

	@Override
	public String toString() {
		return  nome;
	}
	
//	DATO CHE ANDRANNO A FINIRE NEL GRAFO DEVO AGGIUNGERE HASH CODE E EQUALS 
	
	
	
	
}
