package it.polito.tdp.artsmia.model;

public class Adiacenza {
	
	private int eo1;
	private int eo2;
	private int peso;
	
	public Adiacenza(int eo1, int eo2, int peso) {
		super();
		this.eo1 = eo1;
		this.eo2 = eo2;
		this.peso = peso;
	}
	public int getEo1() {
		return eo1;
	}
	public void setEo1(int eo1) {
		this.eo1 = eo1;
	}
	public int getEo2() {
		return eo2;
	}
	public void setEo2(int eo2) {
		this.eo2 = eo2;
	}
	public int getPeso() {
		return peso;
	}
	public void setPeso(int peso) {
		this.peso = peso;
	}

}
