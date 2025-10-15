package com.pawar.asn.integration.entity;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "asn_data")
public class ASNData {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "asn_data_id")
	private Integer id;

	@Column(name = "asn_brcd")
	private String asnBrcd;

	@JsonManagedReference
	@OneToMany(mappedBy = "asnData", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ASNDataDtl> lpns;
	
	@Column(name = "total_quantity")
	@JsonProperty("totalQuantity")
	private Integer totalQuantity;
	
	@Column(name = "status")
	private Integer status;
	
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
	@JsonProperty("created_dttm")
	@Column(name = "created_dttm")
	private LocalDateTime created_dttm;

	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'", timezone = "UTC")
	@JsonProperty("last_updated_dttm")
	@Column(name = "last_updated_dttm")
	private LocalDateTime last_updated_dttm;

	@Column(name = "created_source")
	private String created_source;

	@Column(name = "last_updated_source")
	private String last_updated_source;

	public ASNData() {
	}

	public ASNData(Integer id, String asnBrcd, List<ASNDataDtl> lpns, LocalDateTime created_dttm,
			LocalDateTime last_updated_dttm, String created_source, String last_updated_source) {
		this.id = id;
		this.asnBrcd = asnBrcd;
		this.lpns = lpns;
		this.created_dttm = created_dttm;
		this.last_updated_dttm = last_updated_dttm;
		this.created_source = created_source;
		this.last_updated_source = last_updated_source;
	}

	public ASNData(Integer id, String asnBrcd, List<ASNDataDtl> lpns, Integer totalQuantity, Integer status,
			LocalDateTime created_dttm, LocalDateTime last_updated_dttm, String created_source,
			String last_updated_source) {
		super();
		this.id = id;
		this.asnBrcd = asnBrcd;
		this.lpns = lpns;
		this.totalQuantity = totalQuantity;
		this.status = status;
		this.created_dttm = created_dttm;
		this.last_updated_dttm = last_updated_dttm;
		this.created_source = created_source;
		this.last_updated_source = last_updated_source;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getAsnBrcd() {
		return asnBrcd;
	}

	public void setAsnBrcd(String asnBrcd) {
		this.asnBrcd = asnBrcd;
	}

	public List<ASNDataDtl> getLpns() {
		return lpns;
	}

	public void setLpns(List<ASNDataDtl> lpns) {
		this.lpns = lpns;
	}
	
	public Integer getTotalQuantity() {
		return totalQuantity;
	}

	public void setTotalQuantity(Integer totalQuantity) {
		this.totalQuantity = totalQuantity;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public LocalDateTime getCreated_dttm() {
		return created_dttm;
	}

	public void setCreated_dttm(LocalDateTime created_dttm) {
		this.created_dttm = created_dttm;
	}

	public LocalDateTime getLast_updated_dttm() {
		return last_updated_dttm;
	}

	public void setLast_updated_dttm(LocalDateTime last_updated_dttm) {
		this.last_updated_dttm = last_updated_dttm;
	}

	public String getCreated_source() {
		return created_source;
	}

	public void setCreated_source(String created_source) {
		this.created_source = created_source;
	}

	public String getLast_updated_source() {
		return last_updated_source;
	}

	public void setLast_updated_source(String last_updated_source) {
		this.last_updated_source = last_updated_source;
	}

	@Override
	public String toString() {
		return "ASNData [id=" + id + ", asnBrcd=" + asnBrcd + ", lpns=" + lpns + ", totalQuantity=" + totalQuantity
				+ ", status=" + status + ", created_dttm=" + created_dttm + ", last_updated_dttm=" + last_updated_dttm
				+ ", created_source=" + created_source + ", last_updated_source=" + last_updated_source + "]";
	}
}
