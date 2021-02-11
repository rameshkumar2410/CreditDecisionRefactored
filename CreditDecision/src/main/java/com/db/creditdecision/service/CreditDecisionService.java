package com.db.creditdecision.service;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.db.creditdecision.bo.ApplicantDetails;
import com.db.creditdecision.bo.CreditScore;
import com.db.creditdecision.exception.CreditDecisionException;


/**
 * Service for Credit Decision
 * @author Ramesh
 *
 */
@Service
public interface CreditDecisionService {

	public List<CreditScore> getCreditScoreData();

	public List<Integer> getLoanSanctionedSSNNumber();

	public ResponseEntity<?> getCreditDecisionResponse(ApplicantDetails applicantDetails) throws CreditDecisionException;

}
