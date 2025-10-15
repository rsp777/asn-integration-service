package com.pawar.asn.integration.constants;

public interface ValidationLogsConstants {
	public static final String ITEM_VALIDATION = "item-validation";
	public static final String RETRIGGER_ITEM_VALIDATION = "retrigger-item-validation";
	public static final String ASN_INTEGRATION_SERVICE = "asn-integration-service";
	public static final String HOST = "Host";
	public static final Integer CREATED = 10;
	public static final Integer IN_PROCESS = 30;
	public static final Integer SUCCESS = 90;
	public static final Integer FAILED = 96;
	public static final Integer CANCELLED = 99;
	public static final Integer VALIDATION_PARTIALLY_FAILED = 97;
	public static final String SUCCESS_MESSAGE_CREATED = "ASN received to Integration Service Database";
	public static final String SUCCESS_ITEM_VALIDATION = "Item Validation Success, Item {} is present in WMS";
	public static final String FAILED_ITEM_VALIDATION = "Item Validation Failed, Item {} is not present in WMS";
	public static final String ITEM_VALIDATION_TYPE = "Item";
}
