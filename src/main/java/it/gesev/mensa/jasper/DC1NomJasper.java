package it.gesev.mensa.jasper;

public class DC1NomJasper 
{
	private String ente;
	private String unitaFunzionale;
	private String tipoPagamento;
	private String grado;
	private String nome;
	private String cognome;
	private String personaleEsterno;
	
	public String getEnte() {
		return ente;
	}
	public void setEnte(String ente) {
		this.ente = ente;
	}
	public String getUnitaFunzionale() {
		return unitaFunzionale;
	}
	public void setUnitaFunzionale(String unitaFunzionale) {
		this.unitaFunzionale = unitaFunzionale;
	}
	public String getTipoPagamento() {
		return tipoPagamento;
	}
	public void setTipoPagamento(String tipoPagamento) {
		this.tipoPagamento = tipoPagamento;
	}
	public String getGrado() {
		return grado;
	}
	public void setGrado(String grado) {
		this.grado = grado;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getCognome() {
		return cognome;
	}
	public void setCognome(String cognome) {
		this.cognome = cognome;
	}
	public String getPersonaleEsterno() {
		return personaleEsterno;
	}
	public void setPersonaleEsterno(String personaleEsterno) {
		this.personaleEsterno = personaleEsterno;
	}
	
	public DC1NomJasper(String ente, String unitaFunzionale, String tipoPagamento, String grado, String nome,
			String cognome, String personaleEsterno) {
		this.ente = ente;
		this.unitaFunzionale = unitaFunzionale;
		this.tipoPagamento = tipoPagamento;
		this.grado = grado;
		this.nome = nome;
		this.cognome = cognome;
		this.personaleEsterno = personaleEsterno;
	}
	
	public DC1NomJasper()
	{
		
	}
	

	
	
	
	
	
	
}
