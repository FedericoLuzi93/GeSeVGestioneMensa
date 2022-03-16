package it.gesev.mensa.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ASS_MENSA_TIPO_DIETA")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AssMensaTipoDieta 
{
	@Id
	@Column(name = "id_ass_mensa_tipo_dieta")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer idAssMensaTipoDieta;
	
	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH})
	@JoinColumn(name = "mensa_fk")
	private Mensa mensa;
	
	@ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH})
	@JoinColumn(name = "tipo_dieta_fk")
	private TipoDieta tipoDieta;
}
