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
@Table(name="pietanza")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Pietanza 
{
	@Id
	@Column(name = "id_pietanza")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer idPietanza;
	
	@Column(name = "descrizione_pietanza")
	private String descrizionePietanza;
	
	@ManyToOne(cascade={CascadeType.PERSIST, CascadeType.DETACH,
		 	CascadeType.MERGE, CascadeType.REFRESH})
	@JoinColumn(name = "menu_fk")
	private Menu menu;
	
	@ManyToOne(cascade={CascadeType.PERSIST, CascadeType.DETACH,
		 	CascadeType.MERGE, CascadeType.REFRESH})
	@JoinColumn(name = "tipo_pietanza_fk")
	private TipoPietanza tipoPietanza;
	
	@ManyToOne(cascade={CascadeType.PERSIST, CascadeType.DETACH,
		 	CascadeType.MERGE, CascadeType.REFRESH})
	@JoinColumn(name = "tipo_pasto_fk")
	private TipoPasto tipoPasto;
}
