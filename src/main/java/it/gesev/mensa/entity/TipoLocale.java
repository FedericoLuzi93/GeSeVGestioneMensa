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
@Table(name="TIPO_LOCALE")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TipoLocale 
{
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="CODICE_TIPO_LOCALE")
	private Integer codiceTipoLocale;
	
	@Column(name="DESCRIZIONE_TIPO_LOCALE")
	private String descrizioneTipoLocale;
	
	@OneToMany(mappedBy= "tipoLocale")
	private List<AssDipendenteRuolo> listaAssDipendenteRuolo;

}
