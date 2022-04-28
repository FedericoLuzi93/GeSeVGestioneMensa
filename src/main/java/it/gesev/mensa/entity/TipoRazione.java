package it.gesev.mensa.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="TIPO_RAZIONE")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TipoRazione 
{
	@Id
	@Column(name="id_tipo_razione")
	private String idTipoRazione;
	
	@Column(name = "descrizione_tipo_razione")
	private String descrizioneTipoRazione;
	
	@OneToMany(mappedBy= "tipoRazione", fetch = FetchType.LAZY)
	private List<Prenotazione> listaPrenotazioni;
	
	@OneToMany(mappedBy= "tipoRazione", fetch = FetchType.LAZY)
	private List<PastiConsumati> listaPastiConsumati;
}
