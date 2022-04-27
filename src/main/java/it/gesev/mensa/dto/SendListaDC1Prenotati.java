package it.gesev.mensa.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SendListaDC1Prenotati 
{
	String colonnaUno;
	String descrizioneEnte;
	Integer aventiDiritto;
	Integer aventiDirittoMilitari;
	
	Integer ordColMil;
	Integer ordPraMil;
	Integer ordCenMil;
	Integer medColMil;
	Integer medPraMil;
	Integer medCenMil;
	Integer pesColMil;
	Integer pesPraMil;
	Integer pesCenMil;
	Integer cbtMil;
	
	Integer ordColTg;
	Integer ordPraTg;
	Integer ordCenTg;
	Integer medColTg;
	Integer medPraTg;
	Integer medCenTg;
	Integer pesColTg;
	Integer pesPraTg;
	Integer pesCenTg;
	Integer cbtTg;
	
	Integer ordColTo;
	Integer ordPraTo;
	Integer ordCenTo;
	Integer medColTo;
	Integer medPraTo;
	Integer medCenTo;
	Integer pesColTo;
	Integer pesPraTo;
	Integer pesCenTo;
	Integer cbtTo;

	
	Integer specchioMil;
	Integer colazioneObblMil;
	Integer specchioTg;
	Integer colazioneObblTg;
	Integer specchioTo;
	Integer colazioneObblTo;
	
	
}
