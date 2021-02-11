package com.db.creditdecision.service.impl;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.db.creditdecision.bo.ApplicantDetails;
import com.db.creditdecision.bo.CreditScore;
import com.db.creditdecision.bo.LoanSanctionDetails;
import com.db.creditdecision.constants.ApplicationConstant;
import com.db.creditdecision.controller.CreditDecisionController;
import com.db.creditdecision.exception.CreditDecisionException;
import com.db.creditdecision.service.CreditDecisionService;
import com.db.creditdecision.utils.CreditDecisionUtils;

/**
 * This class provide Mock data for credit score
 * 
 * @author Ramesh
 *
 */
@Service
public class CreditDecisionServiceImpl implements CreditDecisionService {

	private static final Logger LOGGER = Logger.getLogger(CreditDecisionServiceImpl.class);

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	CreditDecisionUtils creditDecisionUtils;

	@Value("${app.db.api.getCreditScoreURL}")
	private String getCreditScoreURL;

	/**
	 * Mock Data for the Credit Score
	 * 
	 * @return creditScoreList
	 */
	public List<CreditScore> getCreditScoreData() {
		List<CreditScore> creditScoreList = Arrays.asList(new CreditScore(11231, 200), new CreditScore(11232, 800),
				new CreditScore(11233, 900), new CreditScore(11234, 400), new CreditScore(11235, 700),
				new CreditScore(11223, 900), new CreditScore(11224, 400), new CreditScore(11225, 700),
				new CreditScore(11236, 200), new CreditScore(11237, 500), new CreditScore(11238, 100),
				new CreditScore(11230, 1000)

		);

		return creditScoreList;
	}

	/**
	 * Mock Data of loan sanctioned 30 days for SSN number - Created this instead of
	 * having Database calls -- We can do by presisting the LoanSanctionDetails in a
	 * Separate table and compare those data with the incoming SSN to do the
	 * validation of Applied or Not Applied
	 * 
	 * @return
	 */
	public List<Integer> getLoanSanctionedSSNNumber() {
		List<Integer> loanSanctionedSSNList = Arrays.asList(11235, 11230, 11223);
		return loanSanctionedSSNList;
	}

	/**
	 * getCreditDecisionResponse Method to determine the credit decision response.
	 * 
	 * @throws Exception
	 */
	public ResponseEntity<?> getCreditDecisionResponse(ApplicantDetails applicantDetails)
			throws CreditDecisionException {
		ResponseEntity<?> responseEntity = null;
		CreditScore creditscore = null;
		int sanctionedLoanAmount = 0;
		try {
			if (creditDecisionUtils.validateApplicantDetails(applicantDetails)) {
				if (!creditDecisionUtils.validateLoanSanctionHistory(applicantDetails)) {
					creditscore = getCreditScore(applicantDetails);
					if (creditscore != null) {
						LoanSanctionDetails loanSanctionDetails = new LoanSanctionDetails();
						loanSanctionDetails.setSsnNumber(creditscore.getSsnNumber());
						if (creditscore.getCreditScore() > 700) {
							sanctionedLoanAmount = applicantDetails.getCurrentAnnualIncome() / 2;
							loanSanctionDetails.setEligibility(ApplicationConstant.ELIGIBLE);
							if (applicantDetails.getLoanAmount() <= sanctionedLoanAmount) {
								loanSanctionDetails.setSanctionedAmount(applicantDetails.getLoanAmount());
							} else {
								loanSanctionDetails.setSanctionedAmount(sanctionedLoanAmount);
							}
							loanSanctionDetails.setSanctionedDate(new Date());
							responseEntity = new ResponseEntity<LoanSanctionDetails>(loanSanctionDetails,
									HttpStatus.OK);
						} else {

							loanSanctionDetails.setEligibility(ApplicationConstant.NOT_ELIGIBLE);
							loanSanctionDetails.setSanctionedAmount(sanctionedLoanAmount);
							responseEntity = new ResponseEntity<LoanSanctionDetails>(loanSanctionDetails,
									HttpStatus.OK);
						}
					} else {
						responseEntity = new ResponseEntity<String>(ApplicationConstant.SSN_NOT_FOUND, HttpStatus.OK);

					}

				} else {
					responseEntity = new ResponseEntity<String>(ApplicationConstant.LOAN_SANCTIONED, HttpStatus.OK);
				}

			} else {
				responseEntity = new ResponseEntity<String>(ApplicationConstant.INVALID_DATA, HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			LOGGER.error("Exception Occured inside getCreditDecisionResponse : " + e.getMessage());
			throw new CreditDecisionException(e.getMessage());
		}

		return responseEntity;

	}

	/**
	 * 
	 * @param applicantDetails
	 * @return
	 * @throws CreditDecisionException
	 */
	public CreditScore getCreditScore(ApplicantDetails applicantDetails) throws CreditDecisionException {
		CreditScore creditscore = null;
		try {
			String uri = getCreditScoreURL + applicantDetails.getSsnNumber();
			creditscore = restTemplate.getForObject(uri, CreditScore.class);
		} catch (Exception e) {
			LOGGER.error("Exception Occured inside getCreditScore : " + e.getMessage());
			throw new CreditDecisionException(e.getMessage());
		}
		return creditscore;
	}

}
