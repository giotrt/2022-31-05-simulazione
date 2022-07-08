package it.polito.tdp.nyc.model;

public class Event implements Comparable<Event>{
	
	public enum EventType{
		INIZIO_HS, //il tecnico inizia a lavorare su un hotspot
		FINE_HS, //il tecnico termina un lavoro su un hotspot
		NUOVO_QUARTIERE // la squadra si sposta 
	}
	
//	NUOVO_QUARTIERE --> quando un tecnico si libera e il numero di tecnici liberi è uguale al numero di tecnici totali

	private int time;
	
	private EventType type;
	
	private int tecnico; //numero corrispondete al tecnico

	
	
	public Event(int time, EventType type, int tecnico) {
		super();
		this.time = time;
		this.type = type;
		this.tecnico = tecnico;
	}



	public int getTime() {
		return time;
	}



	public EventType getType() {
		return type;
	}



	public int getTecnico() {
		return tecnico;
	}



	@Override
	public int compareTo(Event o) {
		return this.time - o.time;
	}
	
	
}
