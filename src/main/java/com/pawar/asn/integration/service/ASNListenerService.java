package com.pawar.asn.integration.service;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.management.relation.RoleNotFoundException;
import javax.validation.Valid;

import org.apache.http.client.ClientProtocolException;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.gson.Gson;
import com.pawar.asn.integration.constants.ValidationLogsConstants;
import com.pawar.asn.integration.entity.ASNData;
import com.pawar.asn.integration.entity.ASNDataDtl;
import com.pawar.asn.integration.entity.ValidationLogs;
import com.pawar.asn.integration.exception.MessageNotSentException;
import com.pawar.asn.integration.repository.ASNDataRepository;
import com.pawar.todo.dto.PermissionDto;
import com.pawar.todo.dto.RoleDto;
import com.pawar.todo.dto.UserDto;

@Service
public class ASNListenerService {
	private static final Logger logger = LoggerFactory.getLogger(ASNListenerService.class);

	@Autowired
	private ValidationLogsService validationLogsService;

	@Autowired
	private ASNProducerService asnProducerService;

	private final ObjectMapper objectMapper;

	private static final String NEW_ASN_DATA_INCOMING = "NEW.ASN.DATA.INCOMING";
	private static final String WMS_ASN_DATA_INCOMING = "WMS.ASN.DATA.INCOMING";

	@Autowired
	private ASNDataRepository asnDataRepository;

	public ASNListenerService() {
		objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());
	}

	@Transactional
	@KafkaListener(topics = NEW_ASN_DATA_INCOMING, groupId = "consumer_group4")
	public void consumeIncomingASN(ConsumerRecord<String, String> consumerRecord, Acknowledgment ack) {

		try {
			boolean postASNToTopic = false;
			String key = consumerRecord.key();
			String value = consumerRecord.value();
			int partition = consumerRecord.partition();
			ASNData asnData = objectMapper.readValue(value, ASNData.class);

			logger.info("value : {}", value);
			logger.info("Consumed message : " + asnData + " with key : " + key + " from partition : " + partition);
			publishASNToWMS(asnData, postASNToTopic);
		} catch (MessageNotSentException | IOException e) {
			logger.error("Error processing Kafka message: {}", e.getMessage());
		} catch (Exception e) {
			logger.error("Error processing Kafka message: {}", e.getMessage());
		}
	}

	@Transactional
	public void publishASNToWMS(ASNData asnData, boolean postASNToTopic)
			throws ClientProtocolException, IOException, MessageNotSentException {
		if (asnData != null) {
			logger.info("{}",asnData);
			saveASN(asnData);
			List<ASNDataDtl> lpns = asnData.getLpns();
			postASNToTopic = validateItemFromLpns(lpns, asnData);
			publishASNToTopic(postASNToTopic, asnData);

		} else {
			logger.warn("Received null value from Kafka topic. {}", NEW_ASN_DATA_INCOMING);
		}
	}

	@Transactional
	public void publishASNToTopic(boolean postASNToTopic, ASNData asnData) throws MessageNotSentException {
		if (postASNToTopic) {
			logger.info(" Post ASN To Topic {} : {} ", WMS_ASN_DATA_INCOMING, postASNToTopic);
			asnProducerService.sendMessage(WMS_ASN_DATA_INCOMING, asnData);
		} else {
			logger.info(" Post ASN To Topic {} : {} ", WMS_ASN_DATA_INCOMING, postASNToTopic);
		}
	}

	@Transactional
	public void saveASN(ASNData asnData) {
		for (ASNDataDtl asnDataDtl : asnData.getLpns()) {
			asnDataDtl.setAsnData(asnData);
			String itemName = asnDataDtl.getItemName();
			logResult(asnData, asnDataDtl, itemName, ValidationLogsConstants.SUCCESS_MESSAGE_CREATED,
					ValidationLogsConstants.CREATED, null, LocalDateTime.now(), LocalDateTime.now(),
					ValidationLogsConstants.HOST, ValidationLogsConstants.HOST);
		}
		asnData.setStatus(ValidationLogsConstants.CREATED);
		asnDataRepository.save(asnData);
		logger.info("ASN saved to database : {}", asnData);
	}

	@Transactional
	public boolean validateItemFromLpns(List<ASNDataDtl> lpns, ASNData asnData)
			throws ClientProtocolException, IOException {
		int validItem = 0;
		Integer status = 0;
		boolean postASNToTopic = false;
		logger.info("validateItemFromLpns method : {}",asnData);
		for (ASNDataDtl asnDataDtl : lpns) {
			String itemName = asnDataDtl.getItemName();
			boolean isValidItem = validationLogsService.validateItemData(itemName);
			if (isValidItem) {
				logger.info("Item is present in WMS : {}", isValidItem);
				logResult(asnData, asnDataDtl, itemName, ValidationLogsConstants.SUCCESS_ITEM_VALIDATION,
						ValidationLogsConstants.SUCCESS, ValidationLogsConstants.ITEM_VALIDATION_TYPE,
						LocalDateTime.now(), LocalDateTime.now(), ValidationLogsConstants.ITEM_VALIDATION,
						ValidationLogsConstants.ITEM_VALIDATION);
				validItem++;

			} else {
				logger.error("Item is not present in WMS : {}", isValidItem);
				logResult(asnData, asnDataDtl, itemName, ValidationLogsConstants.FAILED_ITEM_VALIDATION,
						ValidationLogsConstants.FAILED, ValidationLogsConstants.ITEM_VALIDATION_TYPE,
						LocalDateTime.now(), LocalDateTime.now(), ValidationLogsConstants.ITEM_VALIDATION,
						ValidationLogsConstants.ITEM_VALIDATION);
			}
		}
		if (validItem == lpns.size()) {
			status = ValidationLogsConstants.SUCCESS;
			postASNToTopic = true;
		} else if (validItem > 0 && validItem < lpns.size()) {
			status = ValidationLogsConstants.VALIDATION_PARTIALLY_FAILED;
			postASNToTopic = false;
		} else {
			status = ValidationLogsConstants.FAILED;
			logger.info("validItem {} == {} lpns.size()",validItem,lpns.size());
			postASNToTopic = false;
		}
		logger.info("ASNData : {} ", asnData);
		asnData.setStatus(status);
		asnData.setLast_updated_source(ValidationLogsConstants.ITEM_VALIDATION);
		asnData.setLast_updated_dttm(LocalDateTime.now());
		asnDataRepository.save(asnData);
		return postASNToTopic;

	}

	public void logResult(ASNData asnData, ASNDataDtl asnDataDtl, String itemName, String message, Integer status,
			String validationType, LocalDateTime createdDttm, LocalDateTime lastUpdatedDttm, String createdSource,
			String lastUpdatedSource) {

		if (validationType == null) {
			validationLogsService.logValidationResult(asnData.getAsnBrcd(), asnDataDtl.getLpn_name(),
					asnDataDtl.getItemName(), ValidationLogsConstants.SUCCESS_MESSAGE_CREATED,
					ValidationLogsConstants.CREATED, LocalDateTime.now(), LocalDateTime.now(),
					ValidationLogsConstants.HOST, ValidationLogsConstants.HOST);
		}
		else {
			validationLogsService.logValidationResult(asnData.getAsnBrcd(), asnDataDtl.getLpn_name(), itemName, message,
					status, validationType, createdDttm, lastUpdatedDttm, createdSource, lastUpdatedSource);
		}

	}

	@Scheduled(fixedRate = 100000)
	@Transactional
	public void retriggerFailedItemValidations() throws ClientProtocolException, IOException, MessageNotSentException {
		List<ValidationLogs> validationLogs = validationLogsService.findByValidationTypeStatus(
				ValidationLogsConstants.CREATED, ValidationLogsConstants.ITEM_VALIDATION_TYPE);
		int validItemCount = 0;
		int invalidItemCount = 0;
		ASNData asnData = null;
		logger.info("Item Validation Retriggering : {} ", validationLogs.toString());
		if (validationLogs != null) {
			String asnBrcd = validationLogs.stream().map(ValidationLogs::getAsnBrcd).findFirst().orElse(null);
			asnData = asnDataRepository.findByAsnBrcd(asnBrcd);

			for (ValidationLogs validationLog : validationLogs) {
				String itemName = validationLog.getItemName();
				boolean isValidItem = validationLogsService.validateItemData(itemName);
				int validationLogStatus = 0;
				String validationLogMessage = "";

				if (isValidItem) {
					logger.info("Item is now valid: {}", itemName);
					validationLogMessage = ValidationLogsConstants.SUCCESS_ITEM_VALIDATION;
					validationLogStatus = ValidationLogsConstants.SUCCESS;
					validItemCount++;
				} else {
					logger.error("Item is still invalid: {}", itemName);
					validationLogMessage = ValidationLogsConstants.FAILED_ITEM_VALIDATION;
					validationLogStatus = ValidationLogsConstants.FAILED;
					invalidItemCount++;
				}
				validationLog.setStatus(validationLogStatus);
				validationLog.setMessage(validationLogMessage);
				validationLog.setLast_updated_source(ValidationLogsConstants.RETRIGGER_ITEM_VALIDATION);
				validationLog.setLast_updated_dttm(LocalDateTime.now());
				validationLogsService.updateValidationResult(validationLog);
			}

			List<ValidationLogs> allValidationLogs = validationLogsService.findByAsnBrcd(asnBrcd);
			updateASNStatus(allValidationLogs, asnData);

		}
	}
	public void updateASNStatus(List<ValidationLogs> allValidationLogs,ASNData asnData) throws MessageNotSentException {
		int allValidItemCount = 0;
		int allInvalidItemCount = 0;

		for (ValidationLogs log : allValidationLogs) {
			if (log.getStatus() == ValidationLogsConstants.SUCCESS) {
				allValidItemCount++;
			} else if (log.getStatus() == ValidationLogsConstants.FAILED) {
				allInvalidItemCount++;
			}
		}

		int status = 0;
		if (asnData != null) {
			if (allInvalidItemCount == 0) {
				status = ValidationLogsConstants.SUCCESS;
				publishASNToTopic(true, asnData);
			} else if (allValidItemCount == 0) {
				status = ValidationLogsConstants.FAILED;

			} else {
				status = ValidationLogsConstants.VALIDATION_PARTIALLY_FAILED;

			}

			asnData.setStatus(status);
			asnData.setLast_updated_source(ValidationLogsConstants.RETRIGGER_ITEM_VALIDATION);
			asnData.setLast_updated_dttm(LocalDateTime.now());
			asnDataRepository.save(asnData);
		}
	}

}
