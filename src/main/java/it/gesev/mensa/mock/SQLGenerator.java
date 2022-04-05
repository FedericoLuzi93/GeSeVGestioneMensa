package it.gesev.mensa.mock;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.util.ResourceUtils;

public class SQLGenerator {

	public static void main(String[] args) throws IOException, ParseException 
	{
		String sqlQuery = "INSERT INTO dipendente (foto, nome, cognome, codice_fiscale, cmd, matricola, email, tipo_personale, grado, ente_appartenenza, data_assunzione_forza, data_perdita_forza, ruolo_giuridico, ruolo_funzionale) " +
				                        "VALUES(null, '%s', '%s', '%s', '%s', '%s', '%s', '%s', '%s', %d, '%s', '%s', null, null);\n";
		
		List<String> listaGRadi = Arrays.asList("Caporale", "Caporal maggiore", "Sottotenente", "Tenente", "Capitano");
		List<Integer> listaEnti = Arrays.asList(1, 2, 3);
		
		File mockFile = ResourceUtils.getFile("classpath:MOCK_DATA.csv");
		
		BufferedWriter writer = new BufferedWriter(new FileWriter("INSERT_DIPEENDENTI.sql"));
		BufferedReader reader = new BufferedReader(new FileReader(mockFile));
		String formatIn = "dd/MM/yyyy";
		String formatOut = "yyyy-MM-dd";
		SimpleDateFormat formatterIn = new SimpleDateFormat(formatIn);
		SimpleDateFormat formatterOut = new SimpleDateFormat(formatOut);
		
		String line = reader.readLine();
		while(line != null)
		{
			String[] splitted = line.split(",");
			String tipoPersonale = RandomUtils.nextInt(0, 2) == 0 ? "M" : "C";
			Date data = formatterIn.parse(splitted[3]);
			String row = String.format(sqlQuery, splitted[0], splitted[1], 
					                   RandomStringUtils.random(16, true, true).toUpperCase(), //CF
					                   RandomStringUtils.random(10, true, true).toUpperCase(), //CMD
					                   RandomStringUtils.random(10, true, true).toUpperCase(), //MAT,
					                   splitted[2], //mail
					                   tipoPersonale,
					                   tipoPersonale.equals("M") ? listaGRadi.get(RandomUtils.nextInt(0, listaGRadi.size())) : null,
					                   listaEnti.get(RandomUtils.nextInt(0, listaEnti.size())),
					                   formatterOut.format(data),
					                   "9999-12-31");
			writer.write(row);
			line = reader.readLine();
		}
		
		writer.close();
		reader.close();
		
		System.out.println("Fine");
	}

}
