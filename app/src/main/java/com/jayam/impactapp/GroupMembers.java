package com.jayam.impactapp;

import java.util.ArrayList;

import com.jayam.impactapp.adapters.GroupMembersAdapter;
import com.jayam.impactapp.common.AppConstants;
import com.jayam.impactapp.database.RegularDemandsBLTemp;
import com.jayam.impactapp.objects.RegularDemandsDO;
import com.jayam.impactapp.utils.SharedPrefUtils;


import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.ListView;

public class GroupMembers extends Base
{
	private LinearLayout llGroupMembers;
	private ListView lvGroupMembers;
	private String groupnumber;
	private String TxnType;
	private GroupMembersAdapter groupMembersAdapter;
	@Override
	public void initialize()
	{
		groupnumber		=	 getIntent().getExtras().getString("groupnumber");
		TxnType			=	 getIntent().getExtras().getString("txntype");
		Log.e("Txntype:---",TxnType);
		intializeControlles();
		RegularDemandsBLTemp regularDemandsBL = new RegularDemandsBLTemp();
		ArrayList<RegularDemandsDO> alregularDemandsBL = regularDemandsBL.SelectAll(groupnumber, "Groups",GroupMembers.this);
		groupMembersAdapter.refresh(alregularDemandsBL);

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
				Intent i = new Intent(GroupMembers.this,loginActivity.class);
				startActivity(i);
				//setResult(AppConstants.RESULTCODE_LOGOUT);
				//finish();
			}
		});
	}
	
	public void intializeControlles()
	{
		llGroupMembers		=	(LinearLayout)inflater.inflate(R.layout.centers, null);
		lvGroupMembers		=	(ListView)llGroupMembers.findViewById(R.id.lvCenters);
		
		groupMembersAdapter = new GroupMembersAdapter(GroupMembers.this, null,TxnType);
		lvGroupMembers.setAdapter(groupMembersAdapter);
		svBase.setVisibility(View.GONE);
		llBaseMiddle_lv.setVisibility(View.VISIBLE);
		llBaseMiddle_lv.addView(llGroupMembers, LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		showHomeIcons();
        ivLogout.setVisibility(View.GONE);
		String URL=SharedPrefUtils.getKeyValue(this, AppConstants.pref_name, AppConstants.UrlAddress);
		  if(URL.equals("Yes"))
		  {
			  tvHeader.setText("SHG Members");
		  }
		  else
		  {
			  tvHeader.setText("Group Members");	  
		  }
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);
		
		if(resultCode == AppConstants.RESULTCODE_GROPDETAILS)
		{
			finish();
		}
		else if(resultCode == AppConstants.RESULTCODE_LOGOUT)
		{
			setResult(resultCode);
			finish();
		}
		else if(resultCode == AppConstants.RESULTCODE_HOME)
		{
			setResult(resultCode);
			finish();
		}
		else if(resultCode == AppConstants.RESULTCODE_CENTERDETAILS)
		{
			setResult(resultCode);
			finish();
		}
		
		
	}
	
	

}
