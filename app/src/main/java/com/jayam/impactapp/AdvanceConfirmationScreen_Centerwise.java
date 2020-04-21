package com.jayam.impactapp;

import java.util.ArrayList;

import com.jayam.impactapp.common.AppConstants;
import com.jayam.impactapp.common.CustomDailoglistner;
import com.jayam.impactapp.database.AdvanceDemandBL;
import com.jayam.impactapp.database.IntialParametrsBL;
import com.jayam.impactapp.objects.AdvaceDemandDO;
import com.jayam.impactapp.utils.StringUtils;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AdvanceConfirmationScreen_Centerwise extends Base implements AdvanceUpdatereciptNumbers_Centerwise {
    private LinearLayout llConfirmation;
    private TextView tvAdvConCenterName, tvAdvConCollAmt;
    private String centernumber;
    private Button btnAdvConfirmation;
    private ArrayList<AdvaceDemandDO> vecAdvanceDemands;
    String Name, Amount;
    int LastTaxn;

    @Override
    public void initialize() {
	intialControlles();
	Bundle bundle = getIntent().getExtras();
	Name = bundle.getString("Name");
	Amount = bundle.getString("Amount");
	centernumber = bundle.getString("CenterCode");
	tvAdvConCenterName.setText("" + Name);
	tvAdvConCollAmt.setText("" + Amount);
	AdvanceDemandBL bl = new AdvanceDemandBL();
	vecAdvanceDemands = bl.SelectAllData(centernumber, "CNo",AdvanceConfirmationScreen_Centerwise.this);
	btnAdvConfirmation.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View v) {
		showAlertDailog("Are you sure to submit the Group Detail and sync to server?", "Yes", "No", new CustomDailoglistner() {
		    @Override
		    public void onPossitiveButtonClick(DialogInterface dialog) {
			dialog.dismiss();
			ShowLoader();
			UpdatereciptNumbers updatereciptNumbers = new UpdatereciptNumbers(vecAdvanceDemands,
				AdvanceConfirmationScreen_Centerwise.this);
			updatereciptNumbers.start();
		    }

		    @Override
		    public void onNegativeButtonClick(DialogInterface dialog) {
			dialog.dismiss();
		    }
		});
	    }
	});

	ivHome.setOnClickListener(new OnClickListener() {
	    @Override
	    public void onClick(View v) {
		setResult(AppConstants.RESULTCODE_HOME);
		finish();
	    }
	});

	ivLogout.setOnClickListener(new OnClickListener() {
	    @Override
	    public void onClick(View v) {
		Intent i = new Intent(AdvanceConfirmationScreen_Centerwise.this, loginActivity.class);
		startActivity(i);
		// setResult(AppConstants.RESULTCODE_LOGOUT);
		// finish();
	    }
	});
    }

    public void intialControlles() {
	llConfirmation = (LinearLayout) inflater.inflate(R.layout.advanceconfirmation, null);
	tvAdvConCenterName = (TextView) llConfirmation.findViewById(R.id.tvAdvConCenterName);
	tvAdvConCollAmt = (TextView) llConfirmation.findViewById(R.id.tvAdvConCollAmt);

	btnAdvConfirmation = (Button) llConfirmation.findViewById(R.id.btnAdvConfirmation);

	showHomeIcons();
        ivLogout.setVisibility(View.GONE);
	llBaseMiddle.addView(llConfirmation, LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
	tvHeader.setText("Final Confirmation");

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	super.onActivityResult(requestCode, resultCode, data);

	if (resultCode == AppConstants.RESULTCODE_LOGOUT) {
	    setResult(resultCode);
	    finish();
	} else if (resultCode == AppConstants.RESULTCODE_HOME) {
	    setResult(resultCode);
	    finish();
	}
    }

    class UpdatereciptNumbers extends Thread {
	private ArrayList<AdvaceDemandDO> alArrayList;
	private AdvanceUpdatereciptNumbers_Centerwise listner;

	public UpdatereciptNumbers(ArrayList<AdvaceDemandDO> alArrayList,
		AdvanceUpdatereciptNumbers_Centerwise listner) {
	    this.alArrayList = alArrayList;
	    this.listner = listner;
	}

	@Override
	public void run() {
	    super.run();

	    for (int i = 0; i < alArrayList.size(); i++) {
		AdvanceDemandBL advanceDemandBL = new AdvanceDemandBL();
		advanceDemandBL.updateReciptNumbers(alArrayList.get(i).MLAI_ID, StringUtils.getRecieptNumberForAdvance(alArrayList.get(i),AdvanceConfirmationScreen_Centerwise.this
		),AdvanceConfirmationScreen_Centerwise.this);
	    }

	    runOnUiThread(new Runnable() {
		@Override
		public void run() {
		    listner.onUpdateCompleted();
		}
	    });
	}
    }

    @Override
    public void onUpdateCompleted() {
	new Thread(new Runnable() {
	    @Override
	    public void run() {
		AdvanceDemandBL advanceDemandBL = new AdvanceDemandBL();

		vecAdvanceDemands = advanceDemandBL.SelectAllCollectedData(centernumber, "CenterCode",AdvanceConfirmationScreen_Centerwise.this);

		// updateTransactionTable(vecAdvanceDemands);
		final IntialParametrsBL intialParametrsBL = new IntialParametrsBL();
		final String LastTranScode = StringUtils.getTransactionCodeForAdvance_C(vecAdvanceDemands.get(0),AdvanceConfirmationScreen_Centerwise.this);
		LastTaxn = Integer.parseInt(intialParametrsBL.SelectAll(AdvanceConfirmationScreen_Centerwise.this).get(0).LastTransactionCode);
		LastTaxn = LastTaxn + 1;
		for (AdvaceDemandDO obj : vecAdvanceDemands) {
		    String[] id = StringUtils.getRecieptNumberForAdvance(obj,AdvanceConfirmationScreen_Centerwise.this).split("-");

		    float AmntC = Float.valueOf(obj.CollectedAmt);
		    if (obj.CollectedAmt != null && AmntC > 0)

		    {
			Log.e("Inserted", "saved");
			intialParametrsBL.updateReceiptNumber(
				String.valueOf(Integer.parseInt(id[id.length - 1]) + 1).toString(),AdvanceConfirmationScreen_Centerwise.this);
			advanceDemandBL.Insert(obj, StringUtils.getRecieptNumberForAdvance(obj,AdvanceConfirmationScreen_Centerwise.this), LastTranScode,AdvanceConfirmationScreen_Centerwise.this);
		    } else if (obj.CollectedAmt == null) {
			Log.e("Inserted", "notsaved");
			intialParametrsBL.updateReceiptNumber(
				String.valueOf(Integer.parseInt(id[id.length - 1]) + 1).toString(),AdvanceConfirmationScreen_Centerwise.this);
			advanceDemandBL.Insert(obj, StringUtils.getRecieptNumberForAdvance(obj,AdvanceConfirmationScreen_Centerwise.this), LastTranScode,AdvanceConfirmationScreen_Centerwise.this);
		    }
		    advanceDemandBL.updateCollectedAmt(obj.MLAI_ID, obj.CollectedAmt,AdvanceConfirmationScreen_Centerwise.this);
		}

		intialParametrsBL.updateLastTransctionCode(LastTaxn + "",AdvanceConfirmationScreen_Centerwise.this);
		runOnUiThread(new Runnable() {
		    @Override
		    public void run() {
			HideLoader();

			Intent intent = new Intent(AdvanceConfirmationScreen_Centerwise.this,
				AdvTxnack_CenterWise.class);
			intent.putExtra("LastTranScode", LastTranScode);
			intent.putExtra("centernumber", centernumber);
			startActivityForResult(intent, 1234);
		    }
		});
	    }
	}).start();

    }

    @Override
    public void transactionAdded() {
	// TODO Auto-generated method stub

    }

}

interface AdvanceUpdatereciptNumbers_Centerwise {
    public abstract void onUpdateCompleted();

    public void transactionAdded();
}
