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
@Table(name="TIPO_FORMA_VETTOVAGLIAMENTO")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TipoFormaVettovagliamento 
{
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="CODICE_TIPO_FORMA_VETTOVAGLIAMENTO")
	private Integer codiceTipoFormaVettovagliamento;
	
	@Column(name="DESCRIZIONE")
	private String descrizione;
	
	@OneToMany(mappedBy= "tipoFormaVettovagliamento")
	private List<Mensa> mensa;

}
