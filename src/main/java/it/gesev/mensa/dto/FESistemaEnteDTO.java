package it.gesev.mensa.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FESistemaEnteDTO 
{
	private List<EnteDTO> listaEnti;
	private List<IdentificativoSistemaDTO> listaSistemi;

}
