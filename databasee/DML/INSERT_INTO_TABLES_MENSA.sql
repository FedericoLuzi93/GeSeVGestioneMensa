
INSERT INTO organo_direttivo (descrizione_organo_direttivo) VALUES('Organo direttivo 1');
INSERT INTO organo_direttivo (descrizione_organo_direttivo) VALUES('Organo direttivo 2');
INSERT INTO organo_direttivo (descrizione_organo_direttivo) VALUES('Organo direttivo 3');

INSERT INTO ruolo_mensa (descrizione_ruolo_mensa, organo_direttivo_fk) VALUES('Capo servizio commissariato');
INSERT INTO ruolo_mensa (descrizione_ruolo_mensa, organo_direttivo_fk) VALUES('Commissariato');
INSERT INTO ruolo_mensa (descrizione_ruolo_mensa, organo_direttivo_fk) VALUES('Ufficiale Addetto ai servizi di Commissariato');
INSERT INTO ruolo_mensa (descrizione_ruolo_mensa, organo_direttivo_fk) VALUES('Sottufficiale addetto al vettovagliamento');
INSERT INTO ruolo_mensa (descrizione_ruolo_mensa, organo_direttivo_fk) VALUES('Gestore');
INSERT INTO ruolo_mensa (descrizione_ruolo_mensa, organo_direttivo_fk) VALUES('Aiutante del gestore');
INSERT INTO ruolo_mensa (descrizione_ruolo_mensa, organo_direttivo_fk) VALUES('Capo segreteria');
INSERT INTO ruolo_mensa (descrizione_ruolo_mensa, organo_direttivo_fk) VALUES('Rappresentante della ditta appaltatrice');
INSERT INTO ruolo_mensa (descrizione_ruolo_mensa, organo_direttivo_fk) VALUES('Capo Ufficio Maggiorità e Personale');
INSERT INTO ruolo_mensa (descrizione_ruolo_mensa, organo_direttivo_fk) VALUES('Aiutante Maggiore');
INSERT INTO ruolo_mensa (descrizione_ruolo_mensa, organo_direttivo_fk) VALUES('Comandante di Corpo');
INSERT INTO ruolo_mensa (descrizione_ruolo_mensa, organo_direttivo_fk) VALUES('Capo Gestione Patrimoniale');
INSERT INTO ruolo_mensa (descrizione_ruolo_mensa, organo_direttivo_fk) VALUES('Capo Servizio amministrativo');
INSERT INTO ruolo_mensa (descrizione_ruolo_mensa, organo_direttivo_fk) VALUES('Capo Sezione Coordinamento Amministrativo');
INSERT INTO ruolo_mensa (descrizione_ruolo_mensa, organo_direttivo_fk) VALUES('Presidente della commissione amministrativa');

INSERT INTO tipo_locale (descrizione_tipo_locale) VALUES('Spogliatoio');
INSERT INTO tipo_locale (descrizione_tipo_locale) VALUES('Refettorio');
INSERT INTO tipo_locale (descrizione_tipo_locale) VALUES('Punto cottura');
INSERT INTO tipo_locale (descrizione_tipo_locale) VALUES('Lavaggio stoviglie');
INSERT INTO tipo_locale (descrizione_tipo_locale) VALUES('Servizio igienico');
INSERT INTO tipo_locale (descrizione_tipo_locale) VALUES('Cella frigorifera');
INSERT INTO tipo_locale (descrizione_tipo_locale) VALUES('Punto distribuzione');
INSERT INTO tipo_locale (descrizione_tipo_locale) VALUES('Magazzino');
INSERT INTO tipo_locale (descrizione_tipo_locale) VALUES('Altra pertinenza');
INSERT INTO tipo_locale (descrizione_tipo_locale) VALUES('Superficie vetrata');

INSERT INTO stato_report (descrizione_stato_report) VALUES('Inviato in firma');
INSERT INTO stato_report (descrizione_stato_report) VALUES('Firmato');

INSERT INTO tipo_frequenza_report (descrizione_tipo_frequenza_report) VALUES('Annuale');
INSERT INTO tipo_frequenza_report (descrizione_tipo_frequenza_report) VALUES('Mensile');
INSERT INTO tipo_frequenza_report (descrizione_tipo_frequenza_report) VALUES('Settimanale');
INSERT INTO tipo_frequenza_report (descrizione_tipo_frequenza_report) VALUES('Giornaliero');

INSERT INTO tipo_report (descrizione_tipo_record) VALUES('Mensa');
INSERT INTO tipo_report (descrizione_tipo_record) VALUES('Magazzino viveri');

INSERT INTO report (codice_report, descrizione_report, tipo_report_fk) VALUES('DC1', 'Report consumati e prenotati', 1);
INSERT INTO report (codice_report, descrizione_report, tipo_report_fk) VALUES('DC2', 'Report ingressi mensa meridiano', 1);
INSERT INTO report (codice_report, descrizione_report, tipo_report_fk) VALUES('DC3', 'Prospetto calcolo coefficiente di determinazione  percentuale  riduzione presenze pasto serale', 1);
INSERT INTO report (codice_report, descrizione_report, tipo_report_fk) VALUES('DC4', 'Report consumati ee prenotati mensile', 1);
INSERT INTO report (codice_report, descrizione_report, tipo_report_fk) VALUES('DC5', 'Registro Giornale di Magazzino', 2);
INSERT INTO report (codice_report, descrizione_report, tipo_report_fk) VALUES('DC6', 'Richiesta di movimento CM/123', 2);
INSERT INTO report (codice_report, descrizione_report, tipo_report_fk) VALUES('DC7', 'Registro di carico e scarico delle derrate', 2);
INSERT INTO report (codice_report, descrizione_report, tipo_report_fk) VALUES('DC8', 'Buono di prelevamento definitivo della mensa', 2);
INSERT INTO report (codice_report, descrizione_report, tipo_report_fk) VALUES('DC9', 'Prospetto riepilogativo della gestione mensa: documento di controllo andamento gestionale del servizio', 1);

--select codice_tipo_report from tipo_report where descrizione_tipo_report = 'Mensa'
INSERT INTO dipendente (foto, nome, cognome, codice_fiscale, cmd, matricola, email, tipo_personale, grado, ente_appartenenza, data_assunzione_forza, data_perdita_forza, ruolo_giuridico, ruolo_funzionale) VALUES(NULL, 'Giuseppe', 'Garibaldi', 'GRBGSP09R15H501Y', '0000000000ASDFGHJKLZ', '0123456789', 'g.garibaldi@storia.it', 'M', 'Generale', '1', '2018-01-01', '9999-12-31', NULL, NULL);
INSERT INTO dipendente (foto, nome, cognome, codice_fiscale, cmd, matricola, email, tipo_personale, grado, ente_appartenenza, data_assunzione_forza, data_perdita_forza, ruolo_giuridico, ruolo_funzionale) VALUES(NULL, 'Giacomo', 'Leopardi', 'LPDGCM09R15H501Y', '1111111111ASDFGHJKLZ', '1111111111', 'g.leopardi@storia.it', 'C', 'Soldato semplice', '2', '2019-01-01', '9999-12-31', NULL, NULL);
INSERT INTO dipendente (foto, nome, cognome, codice_fiscale, cmd, matricola, email, tipo_personale, grado, ente_appartenenza, data_assunzione_forza, data_perdita_forza, ruolo_giuridico, ruolo_funzionale) VALUES(NULL, 'Alessandro', 'Manzoni', 'MNZLSS09R15H501Y', '2222222222ASDFGHJKLZ', '2222222222', 'a.manzoni@storia.it', 'T', 'Caporal maggiore', '3', '2020-01-01', '9999-12-31', NULL, NULL);

INSERT INTO tipo_forma_vettovagliamento (descrizione) VALUES('Gestione diretta');
INSERT INTO tipo_forma_vettovagliamento (descrizione) VALUES('Gestione mista');
INSERT INTO tipo_forma_vettovagliamento (descrizione) VALUES('Gestione indiretta');
INSERT INTO tipo_forma_vettovagliamento (descrizione) VALUES('Fornitura di buono pasto');
INSERT INTO tipo_forma_vettovagliamento (descrizione) VALUES('Fornitura di razione viveri da combattimento');
