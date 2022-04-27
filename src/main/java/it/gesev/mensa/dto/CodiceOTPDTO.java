package it.gesev.mensa.dto;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CodiceOTPDTO implements Serializable {
	
	private static final long serialVersionUID = -3766635195646816626L;
	
	String codice;
	Integer codiceMensa;

}
