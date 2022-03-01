package it.gesev.mensa.dao;

import java.util.List;

import it.gesev.mensa.entity.AssReportRuoloMensa;
import it.gesev.mensa.entity.Report;
import it.gesev.mensa.entity.TipoReport;

public interface FirmaDAO 
{
	public List<Report> getListaReport(Integer tipoRecord);
	public List<AssReportRuoloMensa> getReportRuolo();
	public List<TipoReport> getTipiReport();
}
