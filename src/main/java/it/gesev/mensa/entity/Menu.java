package it.gesev.mensa.entity;

import java.util.Date;
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

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "menu")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Menu 
{
	@Id
	@Column(name = "id_menu")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer idMenu;
	
	@Column(name = "data_menu")
	private Date dataMenu;
	
	@ManyToOne(cascade={CascadeType.PERSIST, CascadeType.DETACH,
		 	CascadeType.MERGE, CascadeType.REFRESH})
	@JoinColumn(name = "mensa_fk")
	private Mensa mensa;
	
	@ManyToOne(cascade={CascadeType.PERSIST, CascadeType.DETACH,
		 	CascadeType.MERGE, CascadeType.REFRESH})
	@JoinColumn(name = "tipo_dieta_fk")
	private TipoDieta tipoDieta;
	
	@OneToMany(mappedBy = "menu", fetch = FetchType.LAZY)
	List<Pietanza> listaPietanze;
	
}
