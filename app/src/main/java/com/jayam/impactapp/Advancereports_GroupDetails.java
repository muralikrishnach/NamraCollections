package com.jayam.impactapp;

import java.util.ArrayList;

import com.jayam.impactapp.adapters.AdvanceReport_Groups_Adapter;
import com.jayam.impactapp.common.AppConstants;
import com.jayam.impactapp.database.AdvanceDemandBL;
import com.jayam.impactapp.objects.AdvaceDemandDO;


import android.content.Intent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.ListView;

public class Advancereports_GroupDetails extends Base
{
	private LinearLayout llGroupsSummary;
	private ListView lvGroupsSummary;
	private AdvanceReport_Groups_Adapter advancereport_groups_adapter;
	private String CenterCode ;
	private AdvanceDemandBL advancedemandBL;
	private ArrayList<AdvaceDemandDO> alArrayList;
	
	
	@Override
	public void initialize()
	{
		CenterCode		=	getIntent().getExtras().getString("CenterCode");
		intializeControlles();
		advancedemandBL=new AdvanceDemandBL();
		alArrayList =  advancedemandBL.Selectgroups(CenterCode,Advancereports_GroupDetails.this);
		advancereport_groups_adapter = new AdvanceReport_Groups_Adapter(Advancereports_GroupDetails.this, null,alArrayList);
		lvGroupsSummary.setAdapter(advancereport_groups_adapter);
	}
	
	@SuppressWarnings("deprecation")
	public void intializeControlles()
	{
		llGroupsSummary		=	(LinearLayout)inflater.inflate(R.layout.centers, null);
		lvGroupsSummary		=	(ListView)llGroupsSummary.findViewById(R.id.lvCenters);
		svBase.setVisibility(View.GONE);
		llBaseMiddle_lv.setVisibility(View.VISIBLE);
		llBaseMiddle_lv.addView(llGroupsSummary, LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		showHomeIcons();
		tvHeader.setText("Groups");
	}
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
	protected void onResume()
	{
		
		super.onResume();

		alArrayList =  advancedemandBL.Selectgroups(CenterCode,Advancereports_GroupDetails.this);
		advancereport_groups_adapter.refresh(alArrayList);
	}
}
