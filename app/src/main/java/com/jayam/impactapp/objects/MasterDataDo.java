package com.jayam.impactapp.objects;



import java.util.ArrayList;
import java.util.List;


public class MasterDataDo {

	public String TotalUpload;
	public String UploadMembers;

	public List<com.jayam.impactapp.objects.LoanProduct> getLoanProduct() {
		return LoanProduct;
	}

	public void setLoanProduct(List<com.jayam.impactapp.objects.LoanProduct> loanProduct) {
		LoanProduct = loanProduct;
	}

	public List<com.jayam.impactapp.objects.RepaymentDetails> getRepaymentDetails() {
		return RepaymentDetails;
	}

	public void setRepaymentDetails(List<com.jayam.impactapp.objects.RepaymentDetails> repaymentDetails) {
		RepaymentDetails = repaymentDetails;
	}

	private List<RepaymentDetails> RepaymentDetails = new ArrayList<RepaymentDetails>();

	private List<LoanProduct> LoanProduct = new ArrayList<LoanProduct>();


}
