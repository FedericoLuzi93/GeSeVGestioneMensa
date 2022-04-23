package it.gesev.mensa.entity;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "codice_otp")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CodiceOTP 
{
	@Id
	@Column(name = "codice")
	private String codice;
	
	@ManyToOne
	@JoinColumn(name = "mensa_fk")
	private Mensa mensa;
	
	@OneToMany(mappedBy = "codiceOtp")
	private List<AttestazioneClient> listaAttestazioni;
	
}
