package test;

import java.util.Date;

public class PolicyHolderVO {
	
	String policyCode;
	String reqLoanAmt;
	String accPremiumAmt;
	String pan;
	Date startDate;
	String period;
	String netPremAmt;
	
	public String getPolicyCode() {
		return policyCode;
	}
	public void setPolicyCode(String policyCode) {
		this.policyCode = policyCode;
	}
	public String getReqLoanAmt() {
		return reqLoanAmt;
	}
	public void setReqLoanAmt(String reqLoanAmt) {
		this.reqLoanAmt = reqLoanAmt;
	}
	public String getAccPremiumAmt() {
		return accPremiumAmt;
	}
	public void setAccPremiumAmt(String accPremiumAmt) {
		this.accPremiumAmt = accPremiumAmt;
	}
	public String getPan() {
		return pan;
	}
	public void setPan(String pan) {
		this.pan = pan;
	}
	public Date getStartDate() {
		return startDate;
	}
	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}
	public String getPeriod() {
		return period;
	}
	public void setPeriod(String period) {
		this.period = period;
	}
	public String getNetPremAmt() {
		return netPremAmt;
	}
	public void setNetPremAmt(String netPremAmt) {
		this.netPremAmt = netPremAmt;
	}
	
}
