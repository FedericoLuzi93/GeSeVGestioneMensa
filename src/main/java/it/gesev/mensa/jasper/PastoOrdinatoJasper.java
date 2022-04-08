package it.gesev.mensa.jasper;

public class PastoOrdinatoJasper
{
	private String colazionePO;
	private String pranzoPO;
	private String cenaPO;
	
	
	
	public String getColazionePO() {
		return colazionePO;
	}



	public void setColazionePO(String colazionePO) {
		this.colazionePO = colazionePO;
	}



	public String getPranzoPO() {
		return pranzoPO;
	}



	public void setPranzoPO(String pranzoPO) {
		this.pranzoPO = pranzoPO;
	}



	public String getCenaPO() {
		return cenaPO;
	}



	public void setCenaPO(String cenaPO) {
		this.cenaPO = cenaPO;
	}



	public PastoOrdinatoJasper(String colazionePO, String pranzoPO, String cenaPO) {
		this.colazionePO = colazionePO;
		this.pranzoPO = pranzoPO;
		this.cenaPO = cenaPO;
	}



	public PastoOrdinatoJasper() {
	}
	

	
	

}
