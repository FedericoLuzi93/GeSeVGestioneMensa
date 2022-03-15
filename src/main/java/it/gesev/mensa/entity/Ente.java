package it.gesev.mensa.entity;

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

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name="ENTE")
@Getter
@Setter
public class Ente 
{
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="ID_ENTE")
	private Integer idEnte;
	
	@Column(name="CODICE_ACED")
	private String codiceACED;
	
	@Column(name="descrizione_ente")
	private String descrizioneEnte;
	
	@Column(name="indirizzo")
	private String indirizzo;
	
	@Column(name="localita")
	private String localita;
	
	@OneToMany(mappedBy="ente", cascade={CascadeType.PERSIST, CascadeType.DETACH,
		 	CascadeType.MERGE, CascadeType.REFRESH})
	private List<TestataMovimento> testataMovimento;
	
	@ManyToOne( cascade={CascadeType.PERSIST, CascadeType.DETACH,
		 	CascadeType.MERGE, CascadeType.REFRESH})
	@JoinColumn(name = "ente_riferimento")
	private Ente enteRiferimento;
	
	@OneToMany(mappedBy = "enteRiferimento", cascade={CascadeType.PERSIST, CascadeType.DETACH,
		 	CascadeType.MERGE, CascadeType.REFRESH})
	private List<Ente> listaEnti;
	
	@OneToMany(mappedBy = "ente", cascade={CascadeType.PERSIST, CascadeType.DETACH,
		 	CascadeType.MERGE, CascadeType.REFRESH}, fetch = FetchType.LAZY)
	List<Dipendente> listaDipendenti;
	
	@OneToMany(mappedBy = "ente", cascade={CascadeType.PERSIST, CascadeType.DETACH,
		 	CascadeType.MERGE, CascadeType.REFRESH}, fetch = FetchType.LAZY)
	List<Mensa> listaMensa;
	
	@OneToMany(mappedBy = "ente", cascade={CascadeType.PERSIST, CascadeType.DETACH,
		 	CascadeType.MERGE, CascadeType.REFRESH}, fetch = FetchType.LAZY)
	List<Prenotazione> listaPrenotazioni;
	
	public Ente()
	{
		
	}

	/* costruttore con argomennto */
	public Ente(String codiceACED) 
	{
		this.codiceACED = codiceACED;
	}
	
	

}