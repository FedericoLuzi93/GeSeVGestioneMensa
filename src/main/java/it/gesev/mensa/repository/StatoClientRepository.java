package it.gesev.mensa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import it.gesev.mensa.entity.StatoClient;

public interface StatoClientRepository extends JpaRepository<StatoClient, Integer>{

	@Query("select s from StatoClient s where s.descrizioneStatoClient = :desc")
	public StatoClient findByDesc(@Param("desc") String desc);
}
