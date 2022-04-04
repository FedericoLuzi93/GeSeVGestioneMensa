package it.gesev.mensa.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="struttura_organizzativa")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StrutturaOrganizzativa 
{
	@Id
	@Column(name = "codice_struttura_organizzativa")
	private String codiceStrutturaOrganizzativa;
	
	@Column(name = "descrizione_struttura_organizzativa_b")
	private String descrizioneStrutturaOrganizzativaB;
	
	@Column(name = "descrizione_struttura_organizzativa_r")
	private String descrizioneStrutturaOrganizzativaR;
	
	@Column(name = "numero_ordine")
	private String numero_ordine;
	
	@OneToMany(mappedBy = "strutturaOrganizzativa")
	private List<Prenotazione> listaPrenotazioni;
}
