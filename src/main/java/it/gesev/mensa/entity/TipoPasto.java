package it.gesev.mensa.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
@Table(name="TIPO_PASTO")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TipoPasto 
{
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="codice_tipo_pasto")
	private Integer codiceTipoPasto;
	
	@Column(name="descrizione")
	private String descrizione;
	
	@OneToMany(mappedBy= "tipoPasto", fetch = FetchType.LAZY)
	private List<AssTipoPastoMensa> assTipoPastoMensa;
	
	@OneToMany(mappedBy= "tipoPasto", fetch = FetchType.LAZY)
	private List<Prenotazione> listaPrenotazioni;
	
	@OneToMany(mappedBy= "tipoPasto")
	private List<PastiConsumati> listaPastiConsumati;
	
	@OneToMany(mappedBy= "tipoPasto")
	private List<Pietanza> listaPIetanze;

}
