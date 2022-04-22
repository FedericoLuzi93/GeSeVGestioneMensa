package it.gesev.mensa.jasper;

public class DC1NomNumericaJasper 
{
	private String sistemaGestione;
	private Integer ufficiali;
	private Integer sottoUfficiali;
	private Integer civili;
	private Integer graduati;
	private Integer cestini;
	private Integer totale;
	
	public String getSistemaGestione() {
		return sistemaGestione;
	}
	public void setSistemaGestione(String sistemaGestione) {
		this.sistemaGestione = sistemaGestione;
	}
	public Integer getUfficiali() {
		return ufficiali;
	}
	public void setUfficiali(Integer ufficiali) {
		this.ufficiali = ufficiali;
	}
	public Integer getSottoUfficiali() {
		return sottoUfficiali;
	}
	public void setSottoUfficiali(Integer sottoUfficiali) {
		this.sottoUfficiali = sottoUfficiali;
	}
	public Integer getCivili() {
		return civili;
	}
	public void setCivili(Integer civili) {
		this.civili = civili;
	}
	public Integer getGraduati() {
		return graduati;
	}
	public void setGraduati(Integer graduati) {
		this.graduati = graduati;
	}
	public Integer getCestini() {
		return cestini;
	}
	public void setCestini(Integer cestini) {
		this.cestini = cestini;
	}
	public Integer getTotale() {
		return totale;
	}
	public void setTotale(Integer totale) {
		this.totale = totale;
	}
	
	public DC1NomNumericaJasper(String sistemaGestione, Integer ufficiali, Integer sottoUfficiali, Integer civili,
			Integer graduati, Integer cestini, Integer totale) {
		this.sistemaGestione = sistemaGestione;
		this.ufficiali = ufficiali;
		this.sottoUfficiali = sottoUfficiali;
		this.civili = civili;
		this.graduati = graduati;
		this.cestini = cestini;
		this.totale = totale;
	}
	
	public DC1NomNumericaJasper()
	{
		
	}
	
	
	

}
