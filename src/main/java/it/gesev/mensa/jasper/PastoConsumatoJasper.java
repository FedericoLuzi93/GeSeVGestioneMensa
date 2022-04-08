package it.gesev.mensa.jasper;

public class PastoConsumatoJasper 
{
	private String colazionePC;
	private String pranzoPC;
	private String cenaPC;
	public String getColazionePC() {
		return colazionePC;
	}
	public void setColazionePC(String colazionePC) {
		this.colazionePC = colazionePC;
	}
	public String getPranzoPC() {
		return pranzoPC;
	}
	public void setPranzoPC(String pranzoPC) {
		this.pranzoPC = pranzoPC;
	}
	public String getCenaPC() {
		return cenaPC;
	}
	public void setCenaPC(String cenaPC) {
		this.cenaPC = cenaPC;
	}
	public PastoConsumatoJasper(String colazionePC, String pranzoPC, String cenaPC) {
		this.colazionePC = colazionePC;
		this.pranzoPC = pranzoPC;
		this.cenaPC = cenaPC;
	}
	public PastoConsumatoJasper() {
	}
	
	
}
