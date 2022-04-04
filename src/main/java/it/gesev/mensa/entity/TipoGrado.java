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
@Table(name="TIPO_GRADO")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TipoGrado 
{
	@Id
	@Column(name="id_tipo_grado")
	private String idTipoGrado;
	
	@Column(name = "descrizione_tipo_grado")
	private String descrizioneTipoGrado;
	
	@OneToMany(mappedBy = "tipoGrado")
	private List<Grado> listaGradi;
	
	@OneToMany(mappedBy = "tipoGrado")
	private List<Prenotazione> listaTipiGrado; 
	
}
