package it.gesev.mensa.dao;

import java.text.ParseException;
import java.util.List;

import it.gesev.mensa.dto.FirmaDTO;
import it.gesev.mensa.entity.AssReportRuoloMensa;
import it.gesev.mensa.entity.Dipendente;
import it.gesev.mensa.entity.Report;
import it.gesev.mensa.entity.TipoReport;

public interface FirmaDAO 
{
	public List<Report> getListaReport(Integer tipoRecord);
	public List<AssReportRuoloMensa> getReportRuolo();
	public List<TipoReport> getTipiReport();
	public void registraFirme(FirmaDTO firma) throws ParseException;
	public List<AssReportRuoloMensa> getRuoliReportByIdReport(String idReport);
	public void modificaFirme(FirmaDTO firma) throws ParseException;
	public List<Report> selectReportInAssociazione();
	 
}
