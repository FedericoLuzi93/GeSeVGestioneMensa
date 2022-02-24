package it.gesev.mensa.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "RUOLO_MENSA")
public class RuoloMensa 
{
	@Id
	@Column(name = "CODICE_RUOLO_MENSA")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer codiceRuoloMensa;
	
	@Column(name = "DESCRIZIONE_RUOLO_MENSA")
	private String descrizioneRuoloMensa;
	
	@OneToOne
	@JoinColumn(name = "ORGANO_DIRETTIVO_FK")
	OrganoDirettivo organoDirettivo;
	
	@OneToMany(mappedBy = "ruolo")
	private List<AssDipendenteRuolo> listaDipendenteRuolo;
	
	@OneToMany(mappedBy = "ruoloMensa")
	private List<AssReportRuoloMensa> listaAssReportRuoloMensa;
	
}
