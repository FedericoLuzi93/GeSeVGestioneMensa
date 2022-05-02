package it.gesev.mensa.dto;

import java.util.List;

import it.gesev.mensa.jasper.FirmaDC4Jasper;
import it.gesev.mensa.jasper.Ges2RazJasper;

public class Ges2ComunicazionePastiDTO {

	String anno;
	String mese;
	String mensa;
	String fornitore;
	List<Integer> tabGiorni;
	List<Ges2RazJasper> tabRazioni;
	List<FirmaDC4Jasper> tabFirme;
	
	public Ges2ComunicazionePastiDTO(String anno, String mese, String mensa, String fornitore, List<Integer> tabGiorni,
			List<Ges2RazJasper> tabRazioni, List<FirmaDC4Jasper> tabFirme) {
		super();
		this.anno = anno;
		this.mese = mese;
		this.mensa = mensa;
		this.fornitore = fornitore;
		this.tabGiorni = tabGiorni;
		this.tabRazioni = tabRazioni;
		this.tabFirme = tabFirme;
	}
	
	public Ges2ComunicazionePastiDTO() {
		super();
	}
	
	public String getAnno() {
		return anno;
	}
	public void setAnno(String anno) {
		this.anno = anno;
	}
	public String getMese() {
		return mese;
	}
	public void setMese(String mese) {
		this.mese = mese;
	}
	public String getMensa() {
		return mensa;
	}
	public void setMensa(String mensa) {
		this.mensa = mensa;
	}
	public String getFornitore() {
		return fornitore;
	}
	public void setFornitore(String fornitore) {
		this.fornitore = fornitore;
	}
	public List<Integer> getTabGiorni() {
		return tabGiorni;
	}
	public void setTabGiorni(List<Integer> tabGiorni) {
		this.tabGiorni = tabGiorni;
	}
	public List<Ges2RazJasper> getTabRazioni() {
		return tabRazioni;
	}
	public void setTabRazioni(List<Ges2RazJasper> tabRazioni) {
		this.tabRazioni = tabRazioni;
	}
	public List<FirmaDC4Jasper> getTabFirme() {
		return tabFirme;
	}
	public void setTabFirme(List<FirmaDC4Jasper> tabFirme) {
		this.tabFirme = tabFirme;
	}
	
	
}
