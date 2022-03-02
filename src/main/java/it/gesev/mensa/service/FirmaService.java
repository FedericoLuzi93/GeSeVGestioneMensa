package it.gesev.mensa.service;

import java.text.ParseException;

import it.gesev.mensa.dto.DettaglioReportDTO;
import it.gesev.mensa.dto.FirmaDTO;
import it.gesev.mensa.dto.TipoReportDTO;

public interface FirmaService 
{
	public DettaglioReportDTO getDettaglioReport(TipoReportDTO tipoReport);
	public void registraFirme(FirmaDTO firma) throws ParseException;
}
