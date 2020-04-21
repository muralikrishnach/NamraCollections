package com.jayam.impactapp;

import java.util.ArrayList;

import com.jayam.impactapp.adapters.CollectionReport_Groups_Adapter;
import com.jayam.impactapp.common.AppConstants;
import com.jayam.impactapp.database.RegularDemandsBL;
import com.jayam.impactapp.database.TrnsactionsBL;
import com.jayam.impactapp.objects.RegularDemandsDO;


import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.ListView;

public class CollectionReport_Groups extends Base
{
	private LinearLayout llGroupsSummary;
	private ListView lvGroupsSummary;
	private CollectionReport_Groups_Adapter report_Groups_Adapter;
	private String CNO ;
	private TrnsactionsBL trnsactionsBL;
	private RegularDemandsBL regularDemandsBL;
	private ArrayList<RegularDemandsDO> alArrayList;
	private ArrayList<RegularDemandsDO> alArrayListRegularDemands;
	@Override
	public void initialize()
	{
		CNO		=	getIntent().getExtras().getString("CNO");
		
		intializeControlles();
		
		trnsactionsBL = new TrnsactionsBL();
		regularDemandsBL = new RegularDemandsBL();
		
		alArrayList =  trnsactionsBL.SelectAllGroups_CollectedAmt(CNO,CollectionReport_Groups.this);
		alArrayListRegularDemands = regularDemandsBL.SelectGroups(CNO,CollectionReport_Groups.this);
		
		report_Groups_Adapter = new CollectionReport_Groups_Adapter(CollectionReport_Groups.this, alArrayListRegularDemands,alArrayList);
		lvGroupsSummary.setAdapter(report_Groups_Adapter);
		
		ivHome.setOnClickListener(new  OnClickListener() 
		{
			@Override
			public void onClick(View v)
			{
				setResult(AppConstants.RESULTCODE_HOME);
				finish();
			}
		});
		
		ivLogout.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v)
			{
				Intent i = new Intent(CollectionReport_Groups.this,loginActivity.class);
				startActivity(i);
				//setResult(AppConstants.RESULTCODE_LOGOUT);
				//finish();
			}
		});
	}
	
	public void intializeControlles()
	{
		llGroupsSummary		=	(LinearLayout)inflater.inflate(R.layout.centers, null);
		lvGroupsSummary		=	(ListView)llGroupsSummary.findViewById(R.id.lvCenters);
		svBase.setVisibility(View.GONE);
		llBaseMiddle_lv.setVisibility(View.VISIBLE);
		llBaseMiddle_lv.addView(llGroupsSummary, LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		showHomeIcons();
        ivLogout.setVisibility(View.GONE);
		tvHeader.setText("Groups Summary");
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
