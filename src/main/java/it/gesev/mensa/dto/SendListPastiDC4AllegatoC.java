package it.gesev.mensa.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SendListPastiDC4AllegatoC 
{
	public List<FEPastiDC4USC> listaUFC;
	public List<FEPastiDC4Graduati> listaGraduati;
}
