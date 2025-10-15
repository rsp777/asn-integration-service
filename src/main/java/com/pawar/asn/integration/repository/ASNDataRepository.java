package com.pawar.asn.integration.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.pawar.asn.integration.entity.ASNData;


@Repository
public interface ASNDataRepository extends CrudRepository<ASNData, Integer>{
	ASNData findByAsnBrcd(String asnBrcd);
}
