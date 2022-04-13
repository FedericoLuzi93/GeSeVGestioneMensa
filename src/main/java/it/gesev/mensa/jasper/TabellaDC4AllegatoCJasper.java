package it.gesev.mensa.jasper;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TabellaDC4AllegatoCJasper 
{
	public List<NumeroPastiUFCJasper> listaUFC;
	public List<NumeroPastiGraduatiJasper> listaGraduati;
	public List<GiornoJasper> listaGiorni;
}