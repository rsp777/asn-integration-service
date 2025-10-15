package com.pawar.asn.integration.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.pawar.asn.integration.entity.ValidationLogs;


@Repository
public interface ValidationLogsRepository extends CrudRepository<ValidationLogs, Integer>{
	
	@Query("SELECT vl FROM ValidationLogs vl WHERE vl.validationType = :validationType AND vl.status = :status")
	List<ValidationLogs> findByValidationTypeAndStatus(@Param("validationType") String validationType, @Param("status") Integer status);
	
	@Query("SELECT DISTINCT vl.asnBrcd FROM ValidationLogs vl WHERE vl.validationType = :validationType AND vl.status = :status")
	List<ValidationLogs> findDistinctAsnBrcdByValidationTypeAndStatus(@Param("validationType") String validationType, @Param("status") Integer status);

	List<ValidationLogs> findByAsnBrcd(String asnBrcd);

}
