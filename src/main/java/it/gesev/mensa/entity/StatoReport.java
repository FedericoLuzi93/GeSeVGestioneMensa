package it.gesev.mensa.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "STATO_REPORT")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class StatoReport 
{
	@Id
	@Column(name = "CODICE_STATO_REPORT")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer codiceStatoReport;
	
	@Column(name = "DESCRIZIONE_STATO_REPORT")
	private String descrizioneStatoReport;
	
	@OneToMany(mappedBy = "statoReport")
	private List<AssRepInviatiDipStatoRep> listaAssRepInviatiDipStatoRep;

}
