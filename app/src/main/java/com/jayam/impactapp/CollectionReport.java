package com.jayam.impactapp;

import java.util.ArrayList;

import com.jayam.impactapp.adapters.CollectionReportsAdapter;
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

public class CollectionReport extends Base
{
	private LinearLayout llCollectionReports;
	private ListView lvCollectionReports;
	private CollectionReportsAdapter adapter;
	private RegularDemandsBL regularDemandsBL;
	private TrnsactionsBL trnsactionsBL;
	
	private ArrayList<RegularDemandsDO> arrayListRegularDemands;
	private ArrayList<RegularDemandsDO> arrayListTransactions;
	@Override
	public void initialize()
	{
		intializeControlles();
		regularDemandsBL = new RegularDemandsBL();
		trnsactionsBL	 = new TrnsactionsBL();
		
		arrayListRegularDemands = regularDemandsBL.SelectAllDistinctCenters(CollectionReport.this);
		arrayListTransactions	= trnsactionsBL.SelectAllCenters_CollectedAmt(CollectionReport.this);
		

			adapter = new CollectionReportsAdapter(CollectionReport.this, arrayListRegularDemands,arrayListTransactions);
			lvCollectionReports.setAdapter(adapter);

		
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
//				Intent i = new Intent(CollectionReport.this,loginActivity.class);
//				startActivity(i);
//				//setResult(AppConstants.RESULTCODE_LOGOUT);
//				//finish();
//			}
//		});
	}
	
	public void intializeControlles()
	{

		llCollectionReports		=	(LinearLayout)inflater.inflate(R.layout.centers, null);
		lvCollectionReports		=	(ListView)llCollectionReports.findViewById(R.id.lvCenters);
		
		svBase.setVisibility(View.GONE);
		llBaseMiddle_lv.setVisibility(View.VISIBLE);
		llBaseMiddle_lv.addView(llCollectionReports, LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		showHomeIcons();
        ivLogout.setVisibility(View.GONE);
		tvHeader.setText("CollectionReports");//
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
