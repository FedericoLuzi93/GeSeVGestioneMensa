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
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="TIPO_DIETA")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TipoDieta 
{
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id_tipo_dieta")
	private Integer idTipoDieta;
	
	@Column(name = "descrizione_tipo_dieta")
	private String descrizioneTipoDieta;
	
	@OneToMany(mappedBy= "tipoDieta", fetch = FetchType.LAZY)
	private List<Prenotazione> listaPrenotazioni;
	
	@OneToMany(mappedBy = "tipoDieta", fetch = FetchType.LAZY)
	private List<AssMensaTipoDieta> listaAssMensaTipoDieta;
}


