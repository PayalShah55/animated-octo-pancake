package com.AssignmentTrade.TradeStore.Exception;

public class LowerVersionTradeException extends Exception{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String errorCode;
	private String message;
	
	public LowerVersionTradeException(String errorCode, String message) {
		super();
		this.errorCode = errorCode;
		this.message = message;
	}


	public String getErrorCode() {
		return errorCode;
	}


	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
