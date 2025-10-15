package com.pawar.asn.integration.service;

import java.io.IOException;
import java.net.Inet4Address;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pawar.asn.integration.entity.ValidationLogs;
import com.pawar.asn.integration.repository.ValidationLogsRepository;

import jakarta.transaction.Transactional;

@Service
public class ValidationLogsService {

	private static final Logger logger = LoggerFactory.getLogger(ValidationLogsService.class);

	@Autowired
	private ValidationLogsRepository validationLogsRepository;

	private final HttpClient httpClient;

	public ValidationLogsService() {
		httpClient = HttpClients.createDefault();
	}

	@Transactional
	public void logValidationResult(String asnBrcd, String lpnNumber, String itemName, String message, Integer status,
			String itemValidationType, LocalDateTime created_dttm, LocalDateTime last_updated_dttm,
			String created_source, String last_updated_source) {
		ValidationLogs log = new ValidationLogs(asnBrcd, lpnNumber, itemName, message, status, itemValidationType,
				created_dttm, last_updated_dttm, created_source, last_updated_source);
		logger.info("Save Validation Log : {} ", log);
		validationLogsRepository.save(log);
	}

	@Transactional
	public void logValidationResult(String asnBrcd, String lpnNumber, String itemName, String successMessageCreated,
			Integer created, LocalDateTime created_dttm, LocalDateTime last_updated_dttm, String created_source,
			String last_updated_source) {
		ValidationLogs log = new ValidationLogs(asnBrcd, lpnNumber, itemName, successMessageCreated, created,
				created_dttm, last_updated_dttm, created_source, last_updated_source);
		logger.info("Save Validation Log : {} ", log);
		validationLogsRepository.save(log);
	}

	@Transactional
	public void updateValidationResult(ValidationLogs validationLog) {
		validationLogsRepository.save(validationLog);
	}

	@Transactional
	public boolean validateItemData(String itemName) throws ClientProtocolException, IOException {
		String url = "";
		if (itemName != null) {
			String item = "";
			if (itemName.contains(" ")) {
				item = itemName.replaceAll(" ", "%20");
				logger.info("Item with %20: " + itemName);
				url = "http://" + Inet4Address.getLocalHost().getHostAddress() + ":8085/items/list/by-name/" + item;

			} else {
				url = "http://" + Inet4Address.getLocalHost().getHostAddress() + ":8085/items/list/by-name/" + itemName;
			}
			logger.info("Validating Item : {}", itemName);
			logger.info("URL : {} ", url);

			HttpGet request = new HttpGet(url);
			request.setConfig(RequestConfig.custom().setConnectTimeout(5000) // 5 seconds
					.setSocketTimeout(10000) // 10 seconds
					.build());
			try (CloseableHttpResponse response = (CloseableHttpResponse) httpClient.execute(request)) {
				logger.info("response.getStatusLine() : {}", response.getStatusLine().getStatusCode());
				int responseStatus = response.getStatusLine().getStatusCode();
				if (responseStatus == 404) {
					return false;
				} else {
					return true;
				}
			}
		} else {
			logger.error("Item Name is null : {}", itemName);
			return false;
		}
	}

	@Transactional
	public List<ValidationLogs> findByValidationTypeStatus(Integer failed, String validationType) {
		return validationLogsRepository.findByValidationTypeAndStatus(validationType, failed);
	}

	@Transactional
	public List<ValidationLogs> findDistinctAsnBrcdByValidationTypeAndStatus(Integer failed, String validationType) {
		return validationLogsRepository.findDistinctAsnBrcdByValidationTypeAndStatus(validationType, failed);
	}

	public List<ValidationLogs> findByAsnBrcd(String asnBrcd) {
		// TODO Auto-generated method stub
		return validationLogsRepository.findByAsnBrcd(asnBrcd);
	}

}
