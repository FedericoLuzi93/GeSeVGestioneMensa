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
@Table(name = "TIPO_REPORT")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TipoReport 
{
	@Id
	@Column(name = "CODICE_TIPO_REPORT")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer codiceTipoReport;
	
	@Column(name = "DESCRIZIONE_TIPO_REPORT")
	private String descrizioneTipoRecord;
	
	@OneToMany(mappedBy = "tipoReport")
	private List<Report> listaReport;
	

}
