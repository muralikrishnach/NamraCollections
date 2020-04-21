package com.jayam.impactapp;

import java.util.ArrayList;

import com.jayam.impactapp.adapters.CollectionReportFor_MemberDetails_OD_Adapter;
import com.jayam.impactapp.common.AppConstants;
import com.jayam.impactapp.database.Transaction_OD_BL;
import com.jayam.impactapp.objects.ODDemandsDO;


import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.ListView;

public class CollectionReports_Members_OD extends Base
{
	private LinearLayout llCenters;
	private ListView lvCenters;
	private CollectionReportFor_MemberDetails_OD_Adapter adapter;
	private Transaction_OD_BL transaction_OD_BL;
	private ArrayList<ODDemandsDO> alArrayList;
	private String GNO;
	
	@Override
	public void initialize()
	{
		GNO = getIntent().getExtras().getString("groupcode");
		intializeControlles();
		transaction_OD_BL = new Transaction_OD_BL();
		alArrayList = transaction_OD_BL.getAllMembersFromGroup(GNO,CollectionReports_Members_OD.this);
		adapter = new CollectionReportFor_MemberDetails_OD_Adapter(CollectionReports_Members_OD.this, alArrayList);
		lvCenters.setAdapter(adapter);
		

		ivHome.setOnClickListener(new  OnClickListener() 
		{
			@Override
			public void onClick(View v)
			{
				setResult(AppConstants.RESULTCODE_HOME);
				finish();
			}
		});
//		ivLogout.setOnClickListener(new OnClickListener()
//		{
//			@Override
//			public void onClick(View v)
//			{
//				Intent i = new Intent(CollectionReports_Members_OD .this,loginActivity.class);
//				startActivity(i);
//				//setResult(AppConstants.RESULTCODE_LOGOUT);
//			//	finish();
//			}
//		});
	}
	
	public void intializeControlles()
	{
		llCenters		=	(LinearLayout)inflater.inflate(R.layout.centers, null);
		lvCenters		=	(ListView)llCenters.findViewById(R.id.lvCenters);
		svBase.setVisibility(View.GONE);
		llBaseMiddle_lv.setVisibility(View.VISIBLE);
		llBaseMiddle_lv.addView(llCenters, LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		showHomeIcons();
        ivLogout.setVisibility(View.GONE);
		tvHeader.setText("Members");
	}
	
	

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode == AppConstants.RESULTCODE_LOGOUT)
		{
			setResult(resultCode);
			finish();
		}
		else if(resultCode == AppConstants.RESULTCODE_HOME)
		{
			setResult(resultCode);
			finish();
		}
			
	}

}
