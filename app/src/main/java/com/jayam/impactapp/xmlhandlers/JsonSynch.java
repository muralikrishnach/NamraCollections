package com.jayam.impactapp.xmlhandlers;


import android.util.Log;

import com.jayam.impactapp.objects.LoanProduct;
import com.jayam.impactapp.objects.MasterDataDo;
import com.jayam.impactapp.objects.RepaymentDetails;


import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by administrator_pc on 28-08-2017.
 */

public class JsonSynch {


    public MasterDataDo getLoanproduct(String data) {
        MasterDataDo rcd = new MasterDataDo();
        ArrayList<LoanProduct> block = new ArrayList<LoanProduct>();
        try {
            JSONArray job = null;
            job = new JSONArray(data);
            JSONObject job1=job.getJSONObject(0);
            JSONArray jBranch=job1.getJSONArray("Product");
            for(int i=0;i<jBranch.length();i++)
            {
                LoanProduct branch= new LoanProduct();
                JSONObject jobs=jBranch.getJSONObject(i);
                String id=jobs.getString("ProductId");
                String value=jobs.getString("ProductName");



                branch.id=id;
                branch.name=value;


                block.add(branch);
            }
            rcd.setLoanProduct(block);

        } catch (Exception e) {
            e.printStackTrace();

        }
        return rcd;
    }



    public MasterDataDo getBorrower(String data) {
        MasterDataDo rcd = new MasterDataDo();
        ArrayList<RepaymentDetails> block = new ArrayList<RepaymentDetails>();
        try {
            JSONArray job = null;
            job = new JSONArray(data);
            JSONObject job1=job.getJSONObject(0);
            JSONArray jBranch=job1.getJSONArray("MainList");
            for(int i=0;i<jBranch.length();i++)
            {
                RepaymentDetails branch= new RepaymentDetails();
                JSONObject jobs=jBranch.getJSONObject(i);
                String sno=jobs.getString("sno");
                String PaidDate=jobs.getString("PaidDate");
                String Principle=jobs.getString("Principle");
                String Interest=jobs.getString("Interest");
                String Total=jobs.getString("Total");



                branch.sno=sno;
                branch.PaidDate=PaidDate;
                branch.Principle=Principle;
                branch.Interest=Interest;
                branch.Total=Total;



                block.add(branch);
            }
            rcd.setRepaymentDetails(block);

        } catch (Exception e) {
            e.printStackTrace();

        }
        return rcd;
    }


}
