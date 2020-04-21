package com.jayam.impactapp;

import java.util.ArrayList;

import com.jayam.impactapp.adapters.CollectionReportsMembers_Adapter;
import com.jayam.impactapp.common.AppConstants;
import com.jayam.impactapp.database.TrnsactionsBL;
import com.jayam.impactapp.objects.RegularDemandsDO;


import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.ListView;

public class Collections_Reports_members extends Base
{
	private LinearLayout llGroups;
	private ListView lvGroups;
	private CollectionReportsMembers_Adapter reportsMembers_Adapter;
	private String groupcode;
	private TrnsactionsBL trnsactionsBL;
	private ArrayList<RegularDemandsDO> alArrayList;
	@Override
	public void initialize()
	{
		groupcode = getIntent().getExtras().getString("groupcode");
		trnsactionsBL = new TrnsactionsBL();
		intializeControlles();
		
		alArrayList = trnsactionsBL.SelectAllMembers_CollectedAmt(groupcode,Collections_Reports_members.this);
		reportsMembers_Adapter = new CollectionReportsMembers_Adapter(Collections_Reports_members.this, alArrayList);
		lvGroups.setAdapter(reportsMembers_Adapter);
		
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
//				Intent i = new Intent(Collections_Reports_members.this,loginActivity.class);
//				startActivity(i);
//
//				//setResult(AppConstants.RESULTCODE_LOGOUT);
//				//finish();
//			}
//		});
	}
	
	public void intializeControlles()
	{
		llGroups		=	(LinearLayout)inflater.inflate(R.layout.centers, null);
		lvGroups		=	(ListView)llGroups.findViewById(R.id.lvCenters);
		svBase.setVisibility(View.GONE);
		llBaseMiddle_lv.setVisibility(View.VISIBLE);
		llBaseMiddle_lv.addView(llGroups, LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		showHomeIcons();
        ivLogout.setVisibility(View.GONE);
		tvHeader.setText("Members Details.");
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

}
