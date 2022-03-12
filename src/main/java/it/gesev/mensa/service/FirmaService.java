package it.gesev.mensa.service;

import java.text.ParseException;
import java.util.List;

import it.gesev.mensa.dto.AssReportRuoloMensaDTO;
import it.gesev.mensa.dto.DettaglioReportDTO;
import it.gesev.mensa.dto.DipendenteDTO;
import it.gesev.mensa.dto.FirmaDTO;
import it.gesev.mensa.dto.TipoReportDTO;

public interface FirmaService 
{
	public DettaglioReportDTO getDettaglioReport(TipoReportDTO tipoReport);
	public void registraFirme(FirmaDTO firma) throws ParseException;
	public List<AssReportRuoloMensaDTO> getRuoliReportByIdReport(String idReport);
	public void modificaFirme(FirmaDTO firma) throws ParseException;
}
