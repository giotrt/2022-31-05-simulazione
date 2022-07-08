package it.polito.tdp.nyc.model;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;

import it.polito.tdp.nyc.model.Event.EventType;

public class Simulatore {
	
//	Dati di ingresso --> grafo, elenco delle città perchè mi dicono il numero di hotspot dentro quella città,
//	quartiere di partenza e il numero di tecnici. Tutti questi dati non cambiano mai durante la simulazione
	

	private Graph<City, DefaultWeightedEdge> grafo; // Mi serve per spostarmi da un quartiere all'altro
	
	private List<City> cities;
	
	private City partenza;
	
	private int N; //numero di tecnici (dal tecnico 0 al tecnico N-1)
	
//	Dati in uscita --> durata totale del processo (numero intero perchè minuti), il numero totale di hotspot revisionato da 
//	ogni tecnico. Per ottenere il seconod output posso usare semplicemente una lista: ad esempio la posizione 2 della lista
//	indica che il tecnico numero due ha lavorato revisionati.get(2)
	
	private int durata;
	
	private List<Integer> revisionati; 
	
//	Modello del mondo --> insieme di informazioni che ci servono per andare avanti
//	dovrò sapere in che quartiere sono, quali sono i quartieri già visitati e quali no e quanti hotspot mi mancano 
//	da revisionare in questo quartiere
	
	private List<City> daVisitare; //quartieri ancora da visitare (escluso currentCity)
	
	private City currentCity; //quartiere in lavorazione
	
	private int hotspotRimanenti; //hotspot ancora da revisionare nel quartiere 
	
	private int tecniciOccupati; //quanti tecnici sono impegnati --> se arriva a 0 cambio quartiere
	
//	Coda degli eventi
	
	private PriorityQueue<Event> queue;
	
//	COSTRUTTORE
	
	public Simulatore(Graph<City, DefaultWeightedEdge> grafo, List<City> cities) {
		this.cities = cities;
		this.grafo = grafo;
	}
	
//	INIZIALIZZAZIONE
	public void init(City partenza, int N) {
		
		this.queue = new PriorityQueue<Event>();
		
		this.partenza = partenza;
		this.N = N;
		
//		inizializzo i dati in uscita
		
		this.durata = 0;
		this.revisionati = new ArrayList<Integer>();
		for(int i=0; i<N; i++) {
			getRevisionati().add(0); //inizialmente tutti i tecnici non ha revisionato nessun hotspot
		}
		
//		inizializzo lo stato del mondo 
		this.currentCity = this.partenza;
		this.daVisitare = new ArrayList<>(this.cities); //inizialmente le citta da visitare sono tutte ma devo toglierci la partenza
		this.daVisitare.remove(this.currentCity);
		//all'inizio gli hotspot rimanenti saranno gli hotspot della citta corrente
		this.hotspotRimanenti = this.currentCity.getnHotspot();
		this.tecniciOccupati = 0; //fra poco li occupo
		
//		caricamento iniziale della coda --> schedulo l'inizio del lavoro per tutti i tecnici stando attenti a quanti hotspot
//		ho e quanti tecnici ho
//		finche ho dei tecnici disponibile e ho almeno un hotspot d revisionare allora assegno
		int i = 0;
		while(this.tecniciOccupati < this.N && this.hotspotRimanenti > 0) {
			//posso assegnare un tecnico ad un hotspot
			Event e = new Event(0, EventType.INIZIO_HS, i); //iniziamo al tempo 0, evento è inizio e il tecnico è i
			this.queue.add(e);
			//aumento i tecnici occupati e decremento gli hotspot rimanenti
			this.tecniciOccupati++;
			this.hotspotRimanenti--;
			i++;
		}
	}
	
//	RUN
	public void run() {
		
		while(!this.queue.isEmpty()) {
			Event e = queue.poll();
			//dato che ci serve la durata della simulazione, tutte le volte che estraggo 
			//un evento estraggo la durata dell'event
			this.durata = e.getTime();
			processEvent(e);
		}
	}

	private void processEvent(Event e) {
//		estraiamo i vari campi dell'evento --> ricordati fai sempre cosi
		int time = e.getTime();
		EventType type = e.getType();
		int tecnico = e.getTecnico();
		
//		quando processo un evento devo vedere quali dati di uscita aggiornare e anche lo stato del mondo
		
//		ora vedo che tipo di evento è con lo switch
		switch(type) {
		case INIZIO_HS: //il tecnico prende in carico il lavoro e decide quando finirà
			
			this.getRevisionati().set(tecnico, this.getRevisionati().get(tecnico)+1); //aumento il numero dei revisionati di quel tecnico
			if(Math.random()<0.1) { //dura 25
				//a questo punto schedulo l'evento di FINE_HS
				queue.add(new Event(time + 25, EventType.FINE_HS, tecnico));
			}else {//dura 10
				queue.add(new Event(time + 10, EventType.FINE_HS, tecnico));
			}
			
			break;
		case FINE_HS:
			
			// ho finito il mio hotspot vado a lavorare sul successivo se c'è
			
			// ci sono quindi tre casi possibili:
			//1) caso normale, finisco di lavorare e ho un altro hs
			//2) finisco di lavorare, non ho un altro hotspot ma i miei colleghi stanno ancora lavorando
			//3) sono l'ultimo che finisce di lavorare allora cerchiamo l'altro hotspot
			
			this.tecniciOccupati--; //decremento i tecnici occupati
			//vedo se c'è un hotspot libero
			if(this.hotspotRimanenti>0) { 
				//mi sposto
				
				int spostamento = (int)(Math.random()*11) + 10;
				this.tecniciOccupati++;
				this.hotspotRimanenti--;
				queue.add(new Event(time + spostamento, EventType.INIZIO_HS, tecnico));
				
			}else if(this.tecniciOccupati > 0) {
				
				//non ci sono hotspot rimanenti ma ci sono altri tecnici che stanno lavorando quinid non fare nulla
				
			}else if(this.daVisitare.size()>0){//vedere se c'è un quartiere libero e qual è quello più vicino
				//tutti cambiamo quartiere
				City destinazione = piuVicino(this.currentCity, this.daVisitare);
				//calcolo il tempo di destinazione
				int spostamento = (int)(this.grafo.getEdgeWeight(this.grafo.getEdge(this.currentCity, destinazione))/50.0*60.0);
				//la citta corrente diventa quella trovata
				this.currentCity = destinazione;
				//e la elimino da quella ancora da visitare
				this.daVisitare.remove(destinazione);
				//il numero di hotspot rimanenti saranno quelli della nuova destinazione da visitare
				this.hotspotRimanenti = this.currentCity.getnHotspot();
				
				//posso schedulare l'evento
				
				this.queue.add(new Event(time + spostamento, EventType.NUOVO_QUARTIERE, -1)); //campo tecnico non serve
				
			}else { //non c'è un quartiere libero
				//fine simulazione :)
			}

			break;
		case NUOVO_QUARTIERE:
			//qui assegno i tecnici come ho fatto al tempo zero
			int i = 0;
			while(this.tecniciOccupati < this.N && this.hotspotRimanenti > 0) {
				Event ee = new Event(time, EventType.INIZIO_HS, i);
				queue.add(ee);
				this.tecniciOccupati++;
				this.hotspotRimanenti--;
				i++;
			}
			break;
		}
	}

	private City piuVicino(City currentCity, List<City> vicine) {
		double min = 100000.0;
		City destinazione = null;
		for(City v : vicine) {
			double peso = this.grafo.getEdgeWeight(this.grafo.getEdge(currentCity, v));
			if(peso < min) {
				min = peso;
				destinazione = v;
			}
		}
		return destinazione;
	}

	public int getDurata() {
		return durata;
	}

	public List<Integer> getRevisionati() {
		return revisionati;
	}

}
