package it.gesev.mensa.jasper;

public class ReportGeSeV3Jasper 
{
	private String nome;
	private String cognome;
	private String codiceFiscale;
	private String ente;
	private String tipoPersonale;
	private String grado;
	private String colConPrenotazione;
	private String colNoPrenotazione;
	private String praConPrenotazione;
	private String praNoPrenotazione;
	private String cenConPrenotazione;
	private String cenNoPrenotazione;
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
	public String getCodiceFiscale() {
		return codiceFiscale;
	}
	public void setCodiceFiscale(String codiceFiscale) {
		this.codiceFiscale = codiceFiscale;
	}
	public String getEnte() {
		return ente;
	}
	public void setEnte(String ente) {
		this.ente = ente;
	}
	public String getTipoPersonale() {
		return tipoPersonale;
	}
	public void setTipoPersonale(String tipoPersonale) {
		this.tipoPersonale = tipoPersonale;
	}
	public String getGrado() {
		return grado;
	}
	public void setGrado(String grado) {
		this.grado = grado;
	}
	public String getColConPrenotazione() {
		return colConPrenotazione;
	}
	public void setColConPrenotazione(String colConPrenotazione) {
		this.colConPrenotazione = colConPrenotazione;
	}
	public String getColNoPrenotazione() {
		return colNoPrenotazione;
	}
	public void setColNoPrenotazione(String colNoPrenotazione) {
		this.colNoPrenotazione = colNoPrenotazione;
	}
	public String getPraConPrenotazione() {
		return praConPrenotazione;
	}
	public void setPraConPrenotazione(String praConPrenotazione) {
		this.praConPrenotazione = praConPrenotazione;
	}
	public String getPraNoPrenotazione() {
		return praNoPrenotazione;
	}
	public void setPraNoPrenotazione(String praNoPrenotazione) {
		this.praNoPrenotazione = praNoPrenotazione;
	}
	public String getCenConPrenotazione() {
		return cenConPrenotazione;
	}
	public void setCenConPrenotazione(String cenConPrenotazione) {
		this.cenConPrenotazione = cenConPrenotazione;
	}
	public String getCenNoPrenotazione() {
		return cenNoPrenotazione;
	}
	public void setCenNoPrenotazione(String cenNoPrenotazione) {
		this.cenNoPrenotazione = cenNoPrenotazione;
	}
	public ReportGeSeV3Jasper(String nome, String cognome, String codiceFiscale, String ente, String tipoPersonale,
			String grado, String colConPrenotazione, String colNoPrenotazione, String praConPrenotazione,
			String praNoPrenotazione, String cenConPrenotazione, String cenNoPrenotazione) {
		super();
		this.nome = nome;
		this.cognome = cognome;
		this.codiceFiscale = codiceFiscale;
		this.ente = ente;
		this.tipoPersonale = tipoPersonale;
		this.grado = grado;
		this.colConPrenotazione = colConPrenotazione;
		this.colNoPrenotazione = colNoPrenotazione;
		this.praConPrenotazione = praConPrenotazione;
		this.praNoPrenotazione = praNoPrenotazione;
		this.cenConPrenotazione = cenConPrenotazione;
		this.cenNoPrenotazione = cenNoPrenotazione;
	}
	
	public ReportGeSeV3Jasper()
	{
		
	}

}
