package com.jayam.impactapp;

import java.util.ArrayList;

import com.jayam.impactapp.adapters.FtodreasonsAdaptercenterwise;
import com.jayam.impactapp.common.AppConstants;
import com.jayam.impactapp.database.FtodreasonsBL;
import com.jayam.impactapp.objects.FtodreasonsDO;


import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.ListView;

public class Ftodreasonscenterwise extends Base
{
	private LinearLayout llftodreasons;
	private ListView lvftodreasons;

	private FtodreasonsAdaptercenterwise ftodreasonsAdapter;
	private ArrayList<FtodreasonsDO> alFtodreasonsDO;
	FtodreasonsBL ftodreasonsBL;
	String CGCode,Type;

	@Override
	public void initialize() 
	{
		CGCode 	= getIntent().getExtras().getString("CCode");
		Type	= getIntent().getExtras().getString("Type");
		Log.e("Center/Group Code",CGCode);
		
		intializeControlles();
		
		ftodreasonsBL = new FtodreasonsBL();
		alFtodreasonsDO = ftodreasonsBL.SelectAl(Ftodreasonscenterwise.this);
		ftodreasonsAdapter = new  FtodreasonsAdaptercenterwise(Ftodreasonscenterwise.this,alFtodreasonsDO,CGCode,Type);

		lvftodreasons.setAdapter(ftodreasonsAdapter);
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
				Intent i = new Intent(Ftodreasonscenterwise.this,loginActivity.class);
				startActivity(i);
				//setResult(AppConstants.RESULTCODE_LOGOUT);
				//finish();
			}
		});
			
	}
	

	public void intializeControlles()
	{
		llftodreasons		=	(LinearLayout)inflater.inflate(R.layout.ftodreasons, null);
		lvftodreasons		=	(ListView)llftodreasons.findViewById(R.id.lvftodreasons);
		
		svBase.setVisibility(View.GONE);
		llBaseMiddle_lv.setVisibility(View.VISIBLE);
		llBaseMiddle_lv.addView(llftodreasons, LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		showHomeIcons();
        ivLogout.setVisibility(View.GONE);
		tvHeader.setText("Reasons");
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
	
	@Override
	protected void onResume()
	{
		super.onResume();
		alFtodreasonsDO = ftodreasonsBL.SelectAll(Ftodreasonscenterwise.this);

	}
	
	

}
