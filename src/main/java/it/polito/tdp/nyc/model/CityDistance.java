package it.polito.tdp.nyc.model;

public class CityDistance implements Comparable<CityDistance>{
	
	private City c;
	private Double distance;
	
	
	public CityDistance(City c, Double distance) {
		super();
		this.c = c;
		this.distance = distance;
	}
	
	public City getC() {
		return c;
	}
	public void setC(City c) {
		this.c = c;
	}
	public Double getDistance() {
		return distance;
	}
	public void setDistance(Double distance) {
		this.distance = distance;
	}

	@Override
	public int compareTo(CityDistance o) {
		return this.distance.compareTo(o.distance);
	}
	
	

}
