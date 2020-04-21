package com.jayam.impactapp;

import java.util.ArrayList;

import com.jayam.impactapp.adapters.AdvanceReportsMembers_Adapter;
import com.jayam.impactapp.common.AppConstants;
import com.jayam.impactapp.database.AdvanceDemandBL;
import com.jayam.impactapp.objects.AdvaceDemandDO;


import android.content.Intent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.ListView;

public class AdvanceReports_Members extends Base
{
	private LinearLayout llMembers;
	private ListView lvGroups;
	private AdvanceReportsMembers_Adapter advancereportsmembers_adapter;
	private String groupcode;
	String code;
	AdvaceDemandDO advacedemandDO;
	private AdvanceDemandBL advancedemandBL;
	private ArrayList<AdvaceDemandDO> alArrayList;
	@Override
	public void initialize()
	{

		groupcode = getIntent().getExtras().getString("groupcode");
		advancedemandBL = new AdvanceDemandBL();
		intializeControlles();
		alArrayList = advancedemandBL.SelectAllMembers_CollectedAnt(groupcode,AdvanceReports_Members.this);
		advancereportsmembers_adapter = new AdvanceReportsMembers_Adapter(AdvanceReports_Members.this,null, alArrayList);
		lvGroups.setAdapter(advancereportsmembers_adapter);
	}

	
	@SuppressWarnings("deprecation")
	public void intializeControlles()
	{
		llMembers		=	(LinearLayout)inflater.inflate(R.layout.centers, null);
		lvGroups		=	(ListView)llMembers.findViewById(R.id.lvCenters);
		svBase.setVisibility(View.GONE);
		llBaseMiddle_lv.setVisibility(View.VISIBLE);
		llBaseMiddle_lv.addView(llMembers, LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		showHomeIcons();
        ivLogout.setVisibility(View.GONE);
		tvHeader.setText("Members");
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

		alArrayList = advancedemandBL.SelectAllMembers_CollectedAnt(groupcode,AdvanceReports_Members.this);
		advancereportsmembers_adapter.refresh(alArrayList);
	}
}
