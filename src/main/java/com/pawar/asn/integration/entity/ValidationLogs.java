package com.pawar.asn.integration.entity;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "validation_logs")
public class ValidationLogs {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "validation_logs_id")
	private Integer id;

	@Column(name = "asn_brcd")
	private String asnBrcd;

	@Column(name = "lpn_number")
	private String lpnNumber;

	@Column(name = "item_name")
	private String itemName;

	@Column(name = "message")
	private String message;

	@Column(name = "status")
	private Integer status;

	@Column(name = "validation_type")
	private String validationType;

	@Column(name = "payload")
	private String payload;

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

	public ValidationLogs() {
	}

	public ValidationLogs(Integer id, String asnBrcd, String lpnNumber, String itemName, String message,
			Integer status) {
		this.id = id;
		this.asnBrcd = asnBrcd;
		this.lpnNumber = lpnNumber;
		this.itemName = itemName;
		this.message = message;
		this.status = status;
	}

	public ValidationLogs(Integer id, String asnBrcd, String lpnNumber, String itemName, String message, Integer status,
			LocalDateTime created_dttm, LocalDateTime last_updated_dttm, String created_source,
			String last_updated_source) {
		super();
		this.id = id;
		this.asnBrcd = asnBrcd;
		this.lpnNumber = lpnNumber;
		this.itemName = itemName;
		this.message = message;
		this.status = status;
		this.created_dttm = created_dttm;
		this.last_updated_dttm = last_updated_dttm;
		this.created_source = created_source;
		this.last_updated_source = last_updated_source;
	}

	public ValidationLogs(String asnBrcd, String lpnNumber, String itemName, String message, Integer status,
			String validationType, LocalDateTime last_updated_dttm, String last_updated_source) {
		this.asnBrcd = asnBrcd;
		this.lpnNumber = lpnNumber;
		this.itemName = itemName;
		this.message = message;
		this.status = status;
		this.validationType = validationType;
		this.last_updated_dttm = last_updated_dttm;
		this.last_updated_source = last_updated_source;
	}

	public ValidationLogs(String asnBrcd, String lpnNumber, String itemName, String message, Integer status,
			String validationType, LocalDateTime created_dttm, LocalDateTime last_updated_dttm, String created_source,
			String last_updated_source) {
		this.asnBrcd = asnBrcd;
		this.lpnNumber = lpnNumber;
		this.itemName = itemName;
		this.message = message;
		this.status = status;
		this.validationType = validationType;
		this.created_dttm = created_dttm;
		this.last_updated_dttm = last_updated_dttm;
		this.created_source = created_source;
		this.last_updated_source = last_updated_source;
	}

	public ValidationLogs(String asnBrcd, String lpnNumber, String itemName, String message, Integer status,
			LocalDateTime created_dttm, LocalDateTime last_updated_dttm, String created_source,
			String last_updated_source) {
		this.asnBrcd = asnBrcd;
		this.lpnNumber = lpnNumber;
		this.itemName = itemName;
		this.message = message;
		this.status = status;
		this.created_dttm = created_dttm;
		this.last_updated_dttm = last_updated_dttm;
		this.created_source = created_source;
		this.last_updated_source = last_updated_source;
	}

	public ValidationLogs(String asnBrcd, String lpnNumber, String itemName, String message, Integer status,
			String validationType, String payload, LocalDateTime created_dttm, LocalDateTime last_updated_dttm,
			String created_source, String last_updated_source) {
		super();
		this.id = id;
		this.asnBrcd = asnBrcd;
		this.lpnNumber = lpnNumber;
		this.itemName = itemName;
		this.message = message;
		this.status = status;
		this.validationType = validationType;
		this.payload = payload;
		this.created_dttm = created_dttm;
		this.last_updated_dttm = last_updated_dttm;
		this.created_source = created_source;
		this.last_updated_source = last_updated_source;
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

	public String getLpnNumber() {
		return lpnNumber;
	}

	public void setLpnNumber(String lpnNumber) {
		this.lpnNumber = lpnNumber;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getValidationType() {
		return validationType;
	}

	public void setValidationType(String validationType) {
		this.validationType = validationType;
	}

	public String getPayload() {
		return payload;
	}

	public void setPayload(String payload) {
		this.payload = payload;
	}

	@Override
	public String toString() {
		return "ValidationLogs [id=" + id + ", asnBrcd=" + asnBrcd + ", lpnNumber=" + lpnNumber + ", itemName="
				+ itemName + ", message=" + message + ", status=" + status + ", validationType=" + validationType
				+ ", payload=" + payload + ", created_dttm=" + created_dttm + ", last_updated_dttm=" + last_updated_dttm
				+ ", created_source=" + created_source + ", last_updated_source=" + last_updated_source + "]";
	}
}