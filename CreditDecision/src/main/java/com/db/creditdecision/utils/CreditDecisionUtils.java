package com.db.creditdecision.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.db.creditdecision.bo.ApplicantDetails;
import com.db.creditdecision.service.CreditDecisionService;

/**
 * class for Credit Decision Utility methods
 * 
 * @author Ramesh
 *
 */
@Component
public class CreditDecisionUtils {

	@Autowired
	CreditDecisionService creditScoreService;

	/**
	 * 
	 * @param applicantDetails
	 * @return
	 */
	public boolean validateApplicantDetails(ApplicantDetails applicantDetails) {
		boolean flag = false;
		if (null == applicantDetails) {
			flag = false;
		} else {
			if (applicantDetails.getSsnNumber() != 0  && !StringUtils.isEmpty(applicantDetails.getLoanAmount())
					&& !StringUtils.isEmpty(applicantDetails.getCurrentAnnualIncome())) {
				flag = true;
			}
		}

		return flag;
	}

	/**
	 * validateLoanSanctionHistory
	 * 
	 * @param applicantDetails
	 * @return true if SSN number already applied loan
	 */
	public boolean validateLoanSanctionHistory(ApplicantDetails applicantDetails) {
		boolean flag = false;
		if (creditScoreService.getLoanSanctionedSSNNumber().stream()
				.anyMatch(ssn -> ssn.equals(applicantDetails.getSsnNumber()))) {
			flag = true;
		}
		return flag;
	}

}
