ALTER TABLE ente ADD COLUMN ente_riferimento VARCHAR(256) references ente(codice_aced);
alter table ente add column mensa_fk int references mensa(codice_mensa);

CREATE TABLE dipendente 
(
	codice_dipendente serial4 NOT NULL,
	foto bytea NULL,
	nome varchar(50) NOT NULL,
	cognome varchar(50) NOT NULL,
	codice_fiscale varchar(16) NOT NULL,
	cmd varchar(20) NOT NULL,
	matricola varchar(20) NOT NULL,
	email varchar(100) NOT NULL,
	tipo_personale varchar(2) NOT NULL,
	grado varchar(50) NULL,
	ente_appartenenza int4 NULL,
	data_assunzione_forza date NOT NULL,
	data_perdita_forza date NOT NULL,
	ruolo_giuridico varchar(50) NULL,
	ruolo_funzionale varchar(50) NULL,
	CONSTRAINT dipendente_pkey PRIMARY KEY (codice_dipendente)
);

ALTER TABLE public.dipendente ADD CONSTRAINT ente_fk FOREIGN KEY (ente_appartenenza) REFERENCES ente(id_ente);

CREATE TABLE ORGANO_DIRETTIVO
(
	CODICE_ORGANO_DIRETTIVO SERIAL PRIMARY KEY,	
	DESCRIZIONE_ORGANO_DIRETTIVO VARCHAR(100) NOT NULL
);

CREATE TABLE RUOLO_MENSA
(
	CODICE_RUOLO_MENSA SERIAL PRIMARY KEY,
	DESCRIZIONE_RUOLO_MENSA VARCHAR(100)
);

CREATE TABLE ASS_DIPENDENTE_RUOLO
(
	ASS_DIPENDENTE_RUOLO_ID SERIAL PRIMARY KEY,
	DIPENDENTE_FK INT REFERENCES DIPENDENTE(CODICE_DIPENDENTE) NOT NULL,
	RUOLO_FK INT REFERENCES RUOLO_MENSA(CODICE_RUOLO_MENSA) NOT NULL,
	DATA_INIZIO_RUOLO DATE NOT NULL,
	DATA_FINE_RUOLO DATE NOT NULL,
	ORGANO_DIRETTIVO_FK INT REFERENCES ORGANO_DIRETTIVO(CODICE_ORGANO_DIRETTIVO),
	MENSA_FK INT REFERENCES MENSA(CODICE_MENSA),
	UNIQUE(DIPENDENTE_FK, RUOLO_FK)
);

CREATE TABLE TIPO_FORMA_VETTOVAGLIAMENTO
(
	CODICE_TIPO_FORMA_VETTOVAGLIAMENTO SERIAL PRIMARY KEY,
	DESCRIZIONE VARCHAR(200)
);

CREATE TABLE MENSA
(
	CODICE_MENSA SERIAL PRIMARY KEY,
	DESCRIZIONE_MENSA VARCHAR(100) NOT NULL,
	ORARIO_DAL TIME NOT NULL,
	ORARIO_AL TIME NOT NULL,
	SERVIZIO_FESTIVO VARCHAR(1) NOT NULL,
	AUTORIZZAZIONE_SANITARIA BYTEA,
	NUMERO_AUTORIZZAZIONE_SANITARIA VARCHAR(50), 
	DATA_AUTORIZZAZIONE_SANITARIA DATE,
	AUT_SANITARAIA_RILASCIATA_DA VARCHAR(100),
	ORA_FINE_PRENOTAZIONE TIME NOT NULL,
	VIA VARCHAR(50) NOT NULL,
	NUMERO_CIVICO INT NOT NULL,
	CAP VARCHAR(5) NOT NULL,
	CITTA VARCHAR(50) NOT NULL,
	PROVINCIA VARCHAR(3) NOT NULL,
	TELEFONO VARCHAR(50) NOT NULL,
	FAX VARCHAR(50),
	EMAIL VARCHAR(50),
	DATA_INIZIO_SERVIZIO DATE,
	DATA_FINE_SERVIZIO DATE,
	TIPO_FORMA_VETTOVAGLIAMENTO_FK INT references TIPO_FORMA_VETTOVAGLIAMENTO(CODICE_TIPO_FORMA_VETTOVAGLIAMENTO)
);

CREATE TABLE TIPO_LOCALE(
	CODICE_TIPO_LOCALE SERIAL PRIMARY KEY,
	DESCRIZIONE_TIPO_LOCALE VARCHAR(200) NOT NULL
);

CREATE TABLE ASS_MENSA_TIPO_LOCALE
(
	ASS_MENSA_TIPO_LOCALE_ID SERIAL PRIMARY KEY,
	CODICE_MENSA_FK INT REFERENCES MENSA(CODICE_MENSA) NOT NULL,
	CODICE_TIPO_LOCALE_FK INT REFERENCES TIPO_LOCALE(CODICE_TIPO_LOCALE) NOT NULL,
	DATA_INIZIO DATE NOT NULL,
	DATA_FINE DATE NOT NULL,
	SUPERFICIE INT NOT NULL,
	NUMERO_LOCALI INT NOT NULL,
	NOTE VARCHAR(200)
);

CREATE TABLE TIPO_REPORT
(
	CODICE_TIPO_REPORT SERIAL PRIMARY KEY,
	DESCRIZIONE_TIPO_REPORT VARCHAR(100) NOT NULL
);

CREATE TABLE REPORT
(
	CODICE_REPORT VARCHAR(20) PRIMARY KEY,
	DESCRIZIONE_REPORT VARCHAR(200) NOT NULL,
	TIPO_REPORT_FK INT REFERENCES TIPO_REPORT(CODICE_TIPO_REPORT)
);

CREATE TABLE ASS_REPORT_RUOLO_MENSA
(
	ASS_REPORT_RUOLO_MENSA_ID SERIAL PRIMARY KEY,
	REPORT_FK VARCHAR(20) REFERENCES REPORT(CODICE_REPORT) NOT NULL,
	RUOLO_FK INT REFERENCES RUOLO_MENSA(CODICE_RUOLO_MENSA) NOT NULL,
	DATA_INIZIO DATE NOT NULL,
	DATA_FINE DATE NOT NULL,
	ORDINE_FIRMA INT NOT NULL 
);

CREATE TABLE TIPO_FREQUENZA_REPORT
(
	CODICE_TIPO_FREQUENZA_REPORT SERIAL PRIMARY KEY,
	DESCRIZIONE_TIPO_FREQUENZA_REPORT VARCHAR(100) NOT NULL -- (Annuale, Mensile, Settimanale, Giornaliero)
);

CREATE TABLE REPORT_INVIATO
(
	CODICE_REPORT_INVIATO SERIAL PRIMARY KEY,
	DATA_CREAZIONE DATE NOT NULL,
	DOCUMENTO_INVIATO BYTEA NOT NULL,
	DOCUMENTO_FIRMATO BYTEA,
	GIORNO_REPORT INT,
	MESE_REPORT INT, 
	SETTIMANA_REPORT INT,
	ANNO_REPORT INT,
	REPORT_FK VARCHAR(20) REFERENCES REPORT(CODICE_REPORT) NOT NULL,
	TIPO_FREQUENZA_REPORT_FK INT REFERENCES TIPO_FREQUENZA_REPORT(CODICE_TIPO_FREQUENZA_REPORT) NOT NULL
);

CREATE TABLE STATO_REPORT
(
	CODICE_STATO_REPORT SERIAL PRIMARY KEY,
	DESCRIZIONE_STATO_REPORT VARCHAR(100)  NOT NULL -- (Inviato in firma, firmato)
);

CREATE TABLE ASS_REP_INVIATI_DIP_STATO_REP
(
	ASS_REP_INVIATI_DIP_STATO_REP_ID SERIAL PRIMARY KEY,
	DIPENDENTE_FK INT REFERENCES DIPENDENTE(CODICE_DIPENDENTE) NOT NULL,
	REPORT_INVIATO_FK INT REFERENCES REPORT_INVIATO(CODICE_REPORT_INVIATO) NOT NULL,
	STATO_REPORT_FK INT REFERENCES STATO_REPORT(CODICE_STATO_REPORT) NOT NULL,
	DATA_FIRMA TIMESTAMP
);
