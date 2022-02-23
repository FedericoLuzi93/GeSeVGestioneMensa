package it.gesev.mensa.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
	
	@Column(name = "DESCRIZIONE_TIPO_RECORD")
	private String descrizioneTipoRecord;

}
