package com.bangalorewest.bagtracker.constants;

/**
 * @author sudhanshu.singh
 *
 */
public enum MessageBuilderConstants {

	START_BSM("BSM"),

	START_BPM("BPM"),

	FWD_SLASH("/"),

	LOCAL_BAG("1L"),
	
	TRANSFER_BAG("1T"),
	
	TERMINATE_BAG("1X"),

	DOT_V(".V/"),

	DOT_N(".N/"),

	DOT_P(".P/"),

	DOT_O(".O/"),

	DOT_F(".F/"),
	
	DOT_I(".I/"),
	
	DOT_S(".S/"),
	
	DOT_J(".J/"),
	
	DOT_X(".X/"),

	DOT_U(".U/"),

	DOT_B(".B/"),

	END_BSM("ENDBSM"),

	END_BPM("ENDBPM"),

	HASH("#"),
	
	HYPHEN("-"),
	
	INBOUND("INBOUND"),
	
	OUTBOUND("OUTBOUND"),
	
	ELEMENT_F("ELEMENT_F"),
	
	ELEMENT_I("ELEMENT_I"),
	
	ELEMENT_O("ELEMENT_O"),
	
	BPM_MESSAGE_TYPE("BPM"),
	
	BSM_MESSAGE_TYPE("BSM"),
	
	BOARDED("Y//B"),
	
	CHECKEDIN("Y//C"),

	CABIN_CLASS_Y("Y");

	private String msg;

	private MessageBuilderConstants(String msg) {
		this.msg = msg;
	}

	public String getMessage() {
		return this.msg;
	}

}
