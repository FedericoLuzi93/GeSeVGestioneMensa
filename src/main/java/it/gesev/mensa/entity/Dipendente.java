package it.gesev.mensa.entity;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "DIPENDENTE")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Dipendente 
{
	@Id
	@Column(name = "CODICE_DIPENDENTE")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer codiceDipendente;
	
	@Column(name = "FOTO" /*, columnDefinition = "BLOB"*/)
	//@Lob
	private byte[] foto;
	
	@Column(name = "NOME")
	private String nome;
	
	@Column(name = "COGNOME")
	private String cognome;
	
	@Column(name = "CODICE_FISCALE")
	private String codiceFiscale;
	
	@Column(name = "CMD")
	private String cmd;
	
	@Column(name = "MATRICOLA")
	private String matricola;
	
	@Column(name = "EMAIL")
	private String email;
	
	@Column(name = "TIPO_PERSONALE")
	private String tipoPersonale;
	
	@Column(name = "GRADO")
	private String grado;
	
	@ManyToOne(cascade={CascadeType.PERSIST, CascadeType.DETACH,
		 	CascadeType.MERGE, CascadeType.REFRESH})
	@JoinColumn(name = "ENTE_APPARTENENZA")
	private Ente ente;
	
	@Column(name = "DATA_ASSUNZIONE_FORZA")
	private Date dataEssunzioneForza;
	
	@Column(name = "DATA_PERDITA_FORZA")
	private Date dataPerditaForza;
	
	@Column(name = "RUOLO_GIURIDICO")
	private String ruoloGiuridico;
	
	@Column(name = "RUOLO_FUNZIONALE")
	private String ruoloFunzionale;
	
	@OneToMany(mappedBy = "dipendente", fetch = FetchType.LAZY)
	private List<AssDipendenteRuolo> listaDipendenteRuolo;
	
	@OneToMany(mappedBy = "dipendente", fetch = FetchType.LAZY)
	private List<AssRepInviatiDipStatoRep> listaAssRepInviatiDipStatoRep;
	
	
}
