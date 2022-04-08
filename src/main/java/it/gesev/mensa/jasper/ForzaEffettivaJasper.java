package it.gesev.mensa.jasper;

public class ForzaEffettivaJasper 
{
	private String colazioneEF;
	private String pranzoEF;
	private String cenaEF;
	
	public String getColazioneEF() {
		return colazioneEF;
	}
	public void setColazioneEF(String colazioneEF) {
		this.colazioneEF = colazioneEF;
	}
	public String getPranzoEF() {
		return pranzoEF;
	}
	public void setPranzoEF(String pranzoEF) {
		this.pranzoEF = pranzoEF;
	}
	public String getCenaEF() {
		return cenaEF;
	}
	public void setCenaEF(String cenaEF) {
		this.cenaEF = cenaEF;
	}
	public ForzaEffettivaJasper(String colazioneEF, String pranzoEF, String cenaEF) {
		this.colazioneEF = colazioneEF;
		this.pranzoEF = pranzoEF;
		this.cenaEF = cenaEF;
	}
	public ForzaEffettivaJasper() {
	}
	
	
	

}
