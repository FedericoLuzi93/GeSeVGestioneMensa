package it.gesev.mensa.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RuoloDTO implements Serializable
{
	private Integer codiceRuoloMensa;
	private String descrizioneRuoloMensa;
	
	
}
