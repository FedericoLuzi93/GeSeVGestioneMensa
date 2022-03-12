package it.gesev.mensa.dto;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DettaglioReportDTO implements Serializable
{
	private static final long serialVersionUID = -1714853450272695547L;
	private List<ReportDTO> listaReport;
//	private List<ReportDTO> listaReportInAssociazioni;
//	private List<AssReportRuoloMensaDTO> listaAssociazioni;
	private List<TipoReportDTO> listaTipiReport;
}
