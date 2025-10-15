package com.pawar.asn.integration.entity;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "asn_data_dtl")
public class ASNDataDtl {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "asn_data_dtl_id")
	private Integer id;

	@Column(name = "lpn_name")
	private String lpn_name;

	@Column(name = "item_name")
	@JsonProperty("itemName")
	private String itemName;

	@Column(name = "quantity")
	@JsonProperty("quantity")
	private int quantity;

	@JsonBackReference
	@ManyToOne
	@JoinColumn(name = "asn_data_id")
	private ASNData asnData;

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

	public ASNDataDtl() {
	}

	public ASNDataDtl(Integer id, String lpn_name, ASNData asnData, LocalDateTime created_dttm,
			LocalDateTime last_updated_dttm, String created_source, String last_updated_source) {
		this.id = id;
		this.lpn_name = lpn_name;
		this.asnData = asnData;
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

	public String getLpn_name() {
		return lpn_name;
	}

	public void setLpn_name(String lpn_name) {
		this.lpn_name = lpn_name;
	}
	
	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public ASNData getAsnData() {
		return asnData;
	}

	public void setAsnData(ASNData asnData) {
		this.asnData = asnData;
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
		return "ASNDataDtl [id=" + id + ", lpn_name=" + lpn_name + ", itemName=" + itemName + ", quantity=" + quantity
				+ ", created_dttm=" + created_dttm + ", last_updated_dttm=" + last_updated_dttm + ", created_source="
				+ created_source + ", last_updated_source=" + last_updated_source + "]";
	}
}