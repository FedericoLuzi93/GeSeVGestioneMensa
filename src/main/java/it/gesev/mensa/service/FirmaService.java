package it.gesev.mensa.service;

import it.gesev.mensa.dto.DettaglioReportDTO;
import it.gesev.mensa.dto.TipoReportDTO;

public interface FirmaService 
{
	public DettaglioReportDTO getDettaglioReport(TipoReportDTO tipoReport);
}
