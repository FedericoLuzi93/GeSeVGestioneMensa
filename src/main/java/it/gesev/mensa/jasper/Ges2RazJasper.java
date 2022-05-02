package it.gesev.mensa.jasper;

public class Ges2RazJasper {
	
	Integer norColazione;
	Integer norPranzo;
	Integer norCena;
	Integer medColazione;
	Integer medPranzo;
	Integer medCena;
	Integer pesColazione;
	Integer pesPranzo;
	Integer pesCena;
	
	public Ges2RazJasper(Integer norColazione, Integer norPranzo, Integer norCena, Integer medColazione,
			Integer medPranzo, Integer medCena, Integer pesColazione, Integer pesPranzo, Integer pesCena) {
		super();
		this.norColazione = norColazione;
		this.norPranzo = norPranzo;
		this.norCena = norCena;
		this.medColazione = medColazione;
		this.medPranzo = medPranzo;
		this.medCena = medCena;
		this.pesColazione = pesColazione;
		this.pesPranzo = pesPranzo;
		this.pesCena = pesCena;
	}
	
	public Ges2RazJasper() {
		super();
	}
	
	public Integer getNorColazione() {
		return norColazione;
	}
	public void setNorColazione(Integer norColazione) {
		this.norColazione = norColazione;
	}
	public Integer getNorPranzo() {
		return norPranzo;
	}
	public void setNorPranzo(Integer norPranzo) {
		this.norPranzo = norPranzo;
	}
	public Integer getNorCena() {
		return norCena;
	}
	public void setNorCena(Integer norCena) {
		this.norCena = norCena;
	}
	public Integer getMedColazione() {
		return medColazione;
	}
	public void setMedColazione(Integer medColazione) {
		this.medColazione = medColazione;
	}
	public Integer getMedPranzo() {
		return medPranzo;
	}
	public void setMedPranzo(Integer medPranzo) {
		this.medPranzo = medPranzo;
	}
	public Integer getMedCena() {
		return medCena;
	}
	public void setMedCena(Integer medCena) {
		this.medCena = medCena;
	}
	public Integer getPesColazione() {
		return pesColazione;
	}
	public void setPesColazione(Integer pesColazione) {
		this.pesColazione = pesColazione;
	}
	public Integer getPesPranzo() {
		return pesPranzo;
	}
	public void setPesPranzo(Integer pesPranzo) {
		this.pesPranzo = pesPranzo;
	}
	public Integer getPesCena() {
		return pesCena;
	}
	public void setPesCena(Integer pesCena) {
		this.pesCena = pesCena;
	}
	
}
