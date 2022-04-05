package it.geesev.mensa.mock;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.util.ResourceUtils;

public class PrenotazioniGenerator 
{
	static class Persona 
	{
		private String nome;
		private String cognome;
		private String cf;
		private String tipoPersonale;
		
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
		public String getCf() {
			return cf;
		}
		public void setCf(String cf) {
			this.cf = cf;
		}

		public String getTipoPersonale() {
			return tipoPersonale;
		}

		public void setTipoPersonale(String tipoPersonale) {
			this.tipoPersonale = tipoPersonale;
		}
		
		
	}
	
	private static List<Persona> letturaPersone(boolean selezioneDipendenti) throws IOException
	{
		List<Persona> listaPersone = new ArrayList<>();
		
		if(selezioneDipendenti)
		{
			File mockFile = ResourceUtils.getFile("classpath:dipendente.csv");
			BufferedReader reader = new BufferedReader(new FileReader(mockFile));
			
			String line = reader.readLine();
			while(line != null)
			{
				String[] elements = line.split(",");
				Persona persona = new Persona();
				persona.setNome(elements[0]);
				persona.setCognome(elements[1]);
				persona.setCf(elements[2]);
				persona.setTipoPersonale(elements[3]);
				
				listaPersone.add(persona);
				
				line = reader.readLine();
			}
		}
		
		else
		{
			File mockFile = ResourceUtils.getFile("classpath:MOCK_DATA.csv");
			BufferedReader reader = new BufferedReader(new FileReader(mockFile));
			
			String line = reader.readLine();
			while(line != null)
			{
				String[] elements = line.split(",");
				Persona persona = new Persona();
				persona.setNome(elements[0]);
				persona.setCognome(elements[1]);
				persona.setCf(RandomStringUtils.random(16, true, true).toUpperCase());
				
				listaPersone.add(persona);
				
				line = reader.readLine();
			}
			
		}
		
		return listaPersone;
	}
	
	private static List<String> letturaRighe(String nomeFile) throws IOException
	{
		List<String> listaRighe = new ArrayList<>();
		File mockFile = ResourceUtils.getFile("classpath:" + nomeFile);
		BufferedReader reader = new BufferedReader(new FileReader(mockFile));
		
		String line = reader.readLine();
		while(line != null)
		{
			listaRighe.add(line);
			line = reader.readLine();
		}
		
		return listaRighe;
	}
	
	private static Set<Integer> getRandomNumbers(int numElements, int init, int endExcluded)
	{
		Set<Integer> listaInteri = new HashSet<>();
		
		if(numElements > 0)
		{
			while(listaInteri.size() < numElements)
			{
				Integer tempInt = RandomUtils.nextInt(init, endExcluded);
				if(!listaInteri.contains(tempInt))
					listaInteri.add(tempInt);
			}
		}
		
		return listaInteri;
	}
	
	public static void main(String[] args) throws IOException, ParseException 
	{
		List<Persona> dipendenti = letturaPersone(true);
		List<Persona> esterni = letturaPersone(false);
		List<String> ufficiali = letturaRighe("ufficiali.csv");
		List<String> sottoUfficiali = letturaRighe("sottoufficiali.csv");
		List<String> graduatiTruppe = letturaRighe("graduati_truppa.csv");
		List<String> struttureOrganizzative = letturaRighe("struttura_organizzativa.csv");
		List<String> tipoRazione = Arrays.asList("O", "M", "P", "C");
		
		
		Integer idMensa = 265;
		String idSistema = "GPS";
		Integer idTipoPasto = 3;
		
		SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(formatter.parse("01-03-2022"));
		
		BufferedWriter writer = new BufferedWriter(new FileWriter("prenotazioni_" + idTipoPasto + ".csv"));
		
		while(calendar.get(Calendar.MONTH) == Calendar.MARCH)
		{
			
			System.out.println(formatter.format(calendar.getTime()));
			
			/* aggiunta dipendenti */
			Set<Integer> idDipendenti = getRandomNumbers(20, 0, dipendenti.size());
			for(Integer dip : idDipendenti)
			{
				StringBuffer buffer = new StringBuffer();
				
				buffer.append(idSistema + ",");
				buffer.append(idMensa + ",");
				buffer.append(formatter.format(calendar.getTime()) + ",");
				
				Persona currentPersona = dipendenti.get(dip);
				buffer.append(currentPersona.getCf() + ",");
				buffer.append(currentPersona.getNome() + ",");
				buffer.append(currentPersona.getCognome() + ",");
				
				String tipoPersonale = currentPersona.getTipoPersonale();
				buffer.append(tipoPersonale + ",");
				
				if(tipoPersonale.equals("M"))
				{
					Integer indexMilitare = RandomUtils.nextInt(0, 3);
					switch(indexMilitare)
					{
						case 0:
							int indexUfficiale = RandomUtils.nextInt(0, ufficiali.size());
							buffer.append(ufficiali.get(indexUfficiale) + ",");
							buffer.append("UF,");
							break;
							
						case 1:
							int indexSottoUfficiale = RandomUtils.nextInt(0, sottoUfficiali.size());
							buffer.append(sottoUfficiali.get(indexSottoUfficiale) + ",");
							buffer.append("SU,");
							break;
							
						default:
							int insexGraduatiTruppe = RandomUtils.nextInt(0, graduatiTruppe.size());
							buffer.append(graduatiTruppe.get(insexGraduatiTruppe) + ",");
							buffer.append("GT,");
							
					}
				}
				
				else
					buffer.append(",,");
				
				buffer.append(struttureOrganizzative.get(RandomUtils.nextInt(0, struttureOrganizzative.size())) + ",");
				buffer.append("Unita funzionale,");
				
				/* commensale esterno */
				buffer.append("N,");
				
				/* tipo pagamento */
				buffer.append("TG,");
				
				/* tipo pasto */
				buffer.append(idTipoPasto + ",");
				
				/* flag cestino */
				buffer.append(RandomUtils.nextBoolean() ? "Y," : "N,");
				
				/* tipo dieta */
				buffer.append(RandomUtils.nextInt(1, 7) + ",");	
				
				/* tipo razione */
				buffer.append(tipoRazione.get(RandomUtils.nextInt(0, tipoRazione.size())));
				
				buffer.append("\n");
				
				writer.write(buffer.toString());
				
			}
			
			/* aggiunta esterni */
			Set<Integer> idEsterni = getRandomNumbers(10, 0, esterni.size());
			for(Integer dip : idEsterni)
			{
				StringBuffer buffer = new StringBuffer();
				
				buffer.append(idSistema + ",");
				buffer.append(idMensa + ",");
				buffer.append(formatter.format(calendar.getTime()) + ",");
				
				Persona currentPersona = esterni.get(dip);
				buffer.append(currentPersona.getCf() + ",");
				buffer.append(currentPersona.getNome() + ",");
				buffer.append(currentPersona.getCognome() + ",");
				
				/* tipo personale, grado, tipo grado */
				buffer.append(",,,");
				
				buffer.append(",");
				buffer.append(",");
				
				/* commensale esterno */
				buffer.append("Y,");
				
				/* tipo pagamento */
				buffer.append("TO,");
				
				/* tipo pasto */
				buffer.append(idTipoPasto + ",");
				
				/* flag cestino */
				buffer.append(RandomUtils.nextBoolean() ? "Y," : "N,");
				
				/* tipo dieta */
				buffer.append(RandomUtils.nextInt(1, 7) + ",");	
				
				/* tipo razione */
				buffer.append(tipoRazione.get(RandomUtils.nextInt(0, tipoRazione.size())));
				
				buffer.append("\n");
				
				writer.write(buffer.toString());
				
			}
			
			calendar.add(Calendar.DATE, 1);
			
			
		}
		
		writer.close();
		
		System.out.println("Fine");

	}

}
