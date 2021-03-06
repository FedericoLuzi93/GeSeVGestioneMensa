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
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ORGANO_DIRETTIVO")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrganoDirettivo 
{
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "CODICE_ORGANO_DIRETTIVO")
	private Integer codiceOrganoDirettivo;
	
	@Column(name = "DESCRIZIONE_ORGANO_DIRETTIVO")
	private String descrizioneOrganoDirettivo;
	
	@OneToMany(mappedBy = "organoDirettivo")
	private List<AssDipendenteRuolo> associazioniDipendenteeRuolo;
	
	
}
