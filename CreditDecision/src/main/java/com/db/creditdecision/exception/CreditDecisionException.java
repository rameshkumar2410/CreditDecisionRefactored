package com.db.creditdecision.exception;

/**
 * CreditDecisionException
 * 
 * @author Ramesh
 *
 */
public class CreditDecisionException extends Exception {

	private static final long serialVersionUID = 1L;

	private String errorMessage;

	private int errorCode;

	public CreditDecisionException() {
		super();
	}

	public CreditDecisionException(String message) {
		super(message);
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

}
