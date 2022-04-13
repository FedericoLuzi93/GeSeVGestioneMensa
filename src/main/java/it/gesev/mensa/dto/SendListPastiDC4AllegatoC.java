package it.gesev.mensa.dto;

import java.util.List;

import it.gesev.mensa.jasper.GiornoJasper;
import it.gesev.mensa.jasper.NumeroPastiGraduatiJasper;
import it.gesev.mensa.jasper.NumeroPastiUFCJasper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SendListPastiDC4AllegatoC 
{
	public List<NumeroPastiUFCJasper> listaUFC;
	public List<NumeroPastiGraduatiJasper> listaGraduati;
	public List<GiornoJasper> listaGiorni;
}
