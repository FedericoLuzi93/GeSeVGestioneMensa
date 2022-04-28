package it.gesev.mensa.dto;

public class OperatoreDTO {
	
	private String nome;
	private String cognome;
	private String codiceFiscale;
	private String numeroCMD;
	private String tipoPersonale;
	private String grado;
	private String email;
	
	public OperatoreDTO() {
		super();
	}

	public OperatoreDTO(String nome, String cognome, String codiceFiscale, String numeroCMD, String tipoPersonale,
			String grado, String email) {
		super();
		this.nome = nome;
		this.cognome = cognome;
		this.codiceFiscale = codiceFiscale;
		this.numeroCMD = numeroCMD;
		this.tipoPersonale = tipoPersonale;
		this.grado = grado;
		this.email = email;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCognome() {
		return cognome;
	}

	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

	public String getCodiceFiscale() {
		return codiceFiscale;
	}

	public void setCodiceFiscale(String codiceFiscale) {
		this.codiceFiscale = codiceFiscale;
	}

	public String getNumeroCMD() {
		return numeroCMD;
	}

	public void setNumeroCMD(String numeroCMD) {
		this.numeroCMD = numeroCMD;
	}

	public String getTipoPersonale() {
		return tipoPersonale;
	}

	public void setTipoPersonale(String tipoPersonale) {
		this.tipoPersonale = tipoPersonale;
	}

	public String getGrado() {
		return grado;
	}

	public void setGrado(String grado) {
		this.grado = grado;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
}
