package com.db.creditdecision.controller;

import javax.ws.rs.core.MediaType;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.db.creditdecision.bo.ApplicantDetails;
import com.db.creditdecision.constants.ApplicationConstant;
import com.db.creditdecision.exception.CreditDecisionException;
import com.db.creditdecision.service.CreditDecisionService;

/**
 * This class is for CreditDecision
 * 
 * @author Ramesh
 *
 */
@RestController
@RequestMapping("/creditdecision")
public class CreditDecisionController {

	private static final Logger LOGGER = Logger.getLogger(CreditDecisionController.class);

	@Autowired
	CreditDecisionService creditDecisionService;

	/**
	 * Method to calculate the Loan amount for the SSN Number
	 * 
	 * @param applicantDetails
	 * @return ResponseEntity of LoanSanctioned Details
	 */
	@PostMapping(path = "/calculateLoanAmount/", produces = MediaType.APPLICATION_JSON)
	public ResponseEntity<?> calculateLoanAmount(@RequestBody final ApplicantDetails applicantDetails) {
		ResponseEntity<?> responseEntity = null;
		try {
			responseEntity = creditDecisionService.getCreditDecisionResponse(applicantDetails);
		} catch (CreditDecisionException e) {
			LOGGER.error("Exception Occured inside calculateLoanAmount : " + e.getMessage());
			responseEntity = new ResponseEntity<String>(ApplicationConstant.EXCEPTION, HttpStatus.NOT_FOUND);

		}
		return responseEntity;

	}

}
