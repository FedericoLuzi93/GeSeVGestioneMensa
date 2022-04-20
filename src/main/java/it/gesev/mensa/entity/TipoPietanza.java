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
@Table(name="tipo_pietanza")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TipoPietanza 
{
	@Id
	@Column(name="id_tipo_pietanza")
	private Integer idTipoPietanza;
	
	@Column(name = "descrizione_tipo_pietanza")
	private String descrizioneTipoPietanza;
	
	@OneToMany(mappedBy = "tipoPietanza", fetch = FetchType.LAZY)
	List<Pietanza> listaPietanza;
}
