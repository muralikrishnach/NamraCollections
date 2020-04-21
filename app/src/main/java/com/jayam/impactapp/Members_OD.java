package com.jayam.impactapp;

import java.util.ArrayList;

import com.jayam.impactapp.adapters.MembersAdapter_OD;
import com.jayam.impactapp.common.AppConstants;
import com.jayam.impactapp.database.ODDemandsBL;
import com.jayam.impactapp.objects.ODDemandsDO;


import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.ListView;

public class Members_OD extends Base
{
	private LinearLayout llCenters;
	private ListView lvCenters;
	private MembersAdapter_OD adapter_OD;
	@Override
	public void initialize()
	{
		intializeControlles();
		String groupcode = getIntent().getExtras().getString("groupcode");
		
		ODDemandsBL  bl = new ODDemandsBL();
		
		ArrayList<ODDemandsDO> alArrayList = bl.SelectAllMembers(groupcode,Members_OD.this);
		
		adapter_OD.refresh(alArrayList);
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
			Intent i = new Intent(Members_OD.this,loginActivity.class);
			startActivity(i);
				/*setResult(AppConstants.RESULTCODE_LOGOUT);
				String logut = Integer.toString(AppConstants.RESULTCODE_LOGOUT);
				Log.v("resultcode", "logt"+logut);
				finish();*/
			}
		});
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
		adapter_OD = new MembersAdapter_OD(Members_OD.this, null);
		lvCenters.setAdapter(adapter_OD);
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
