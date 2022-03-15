package it.gesev.mensa.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import it.gesev.mensa.entity.IdentificativoSistema;

public interface IdentificativoSistemaRepository extends JpaRepository<IdentificativoSistema, String> {

}
