package com.pawar.asn.integration.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFutureCallback;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.common.util.concurrent.ListenableFuture;
import com.pawar.asn.integration.entity.ASNData;
import com.pawar.asn.integration.entity.ASNDataDtl;
import com.pawar.asn.integration.exception.MessageNotSentException;
import com.pawar.inventory.entity.ASNDto;
import com.pawar.inventory.entity.Category;
import com.pawar.inventory.entity.Item;
import com.pawar.inventory.entity.LpnDto;

@Service
public class ASNProducerService {

	private static final Logger logger = LoggerFactory.getLogger(ASNProducerService.class);

	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;

	private final ObjectMapper objectMapper;

	public ASNProducerService() {
		objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());
	}

	public void sendMessage(String topic, ASNData asnData) throws MessageNotSentException {
		try {
			String asnDataJsonPayload = createJsonASNDataPayload(asnData);
			kafkaTemplate.send(topic, asnDataJsonPayload).get();
			logger.info("Message : {} sent to Topic : {}", asnData, topic);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Error sending message", e.getMessage());
		}
	}

	public String createJsonASNDataPayload(ASNData asnData) throws JsonProcessingException {
		ASNDto asnDto = setASNDtoData(asnData);
		String jsonpayload = objectMapper.writeValueAsString(asnDto);
		return jsonpayload;
	}
	
	public ASNDto setASNDtoData(ASNData asnData) {
		ASNDto asnDto = new ASNDto();
		asnDto.setAsnBrcd(asnData.getAsnBrcd());
		asnDto.setTotalQuantity(asnData.getTotalQuantity());
		asnDto.setLpns(setLpnDtoData(asnData.getLpns()));
		return asnDto;		
	}
	
	public List<LpnDto> setLpnDtoData(List<ASNDataDtl> asnDataDtls) {
		List<LpnDto> lpnDtos = new ArrayList<>();
		for (ASNDataDtl asnDataDtl : asnDataDtls) {
			LpnDto lpnDto = new LpnDto();
			lpnDto.setLpn_name(asnDataDtl.getLpn_name());
			lpnDto.setQuantity(asnDataDtl.getQuantity());
			lpnDto.setItem(setItemObj(asnDataDtl.getItemName()));
			lpnDtos.add(lpnDto);
		}
		return lpnDtos;
	}
	
	public Item setItemObj(String itemName){
		Item item = new Item();
		item.setItemName(itemName);
		return item;
	}

}
