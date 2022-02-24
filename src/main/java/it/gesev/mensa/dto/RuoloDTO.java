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
	private static final long serialVersionUID = -2179490999299682488L;
	private Integer codiceRuoloMensa;
	private String descrizioneRuoloMensa;
	
	
}
