package it.gesev.mensa.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "RUOLO_MENSA")
@Data
public class RuoloMensa 
{
	@Id
	@Column(name = "CODICE_RUOLO_MENSA")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer codiceRuoloMensa;
	
	@Column(name = "DESCRIZIONE_RUOLO_MENSA")
	private String descrizioneRuoloMensa;
	
	@Column(name = "flag_personale_esterno")
	private String flagPersonaleEsterno;
	
	@Column(name = "flag_personale_interno")
	private String flagPersonaleInterno;
	
	@OneToMany(mappedBy = "ruolo")
	private List<AssDipendenteRuolo> listaDipendenteRuolo;
	
	@OneToMany(mappedBy = "ruoloMensa")
	private List<AssReportRuoloMensa> listaAssReportRuoloMensa;
	
}
