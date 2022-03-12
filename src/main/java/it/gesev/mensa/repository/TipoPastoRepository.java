package it.gesev.mensa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import it.gesev.mensa.entity.TipoPasto;

public interface TipoPastoRepository extends JpaRepository<TipoPasto, Integer> {


}
