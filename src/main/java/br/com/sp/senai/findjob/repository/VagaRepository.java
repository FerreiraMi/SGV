package br.com.sp.senai.findjob.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.sp.senai.findjob.model.Vaga;

@Repository
public interface VagaRepository extends JpaRepository<Vaga, Long> {
	
	
}
