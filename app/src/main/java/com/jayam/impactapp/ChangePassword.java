package com.jayam.impactapp;

import com.jayam.impactapp.businesslayer.GetDataBL;
import com.jayam.impactapp.common.AppConstants;
import com.jayam.impactapp.common.DataListner;
import com.jayam.impactapp.objects.KeyValue;
import com.jayam.impactapp.utils.NetworkUtility;
import com.jayam.impactapp.utils.SharedPrefUtils;


import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

public class ChangePassword extends Base implements DataListner{

	LinearLayout llChangePassword ;
	EditText txtoldpwd,tvnewpwd,tvconfirmpwd;
	Button btnsubmit;
	@SuppressWarnings("deprecation")
	@Override
	public void initialize() 
	{
		llChangePassword = (LinearLayout)getLayoutInflater().inflate(R.layout.changepwd, null);
		
		initializeControls();
		
		btnsubmit.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) 
			{
				
				if(NetworkUtility.isNetworkConnectionAvailable(ChangePassword.this))
				{
					String message = passwordvalidation();
					
					
					if(message.equalsIgnoreCase(""))
					{
						String userId = SharedPrefUtils.getKeyValue(ChangePassword.this, AppConstants.pref_name, AppConstants.username, "");
						new GetDataBL(ChangePassword.this, ChangePassword.this).changePassword(userId,txtoldpwd.getText().toString(), tvnewpwd.getText().toString());
					}
					else
					{
						showAlertDailog(message);
					}
				}
				else
				{
					showAlertDailog("No Network Available.");
				}
				
			}
		});
		

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
				Intent i = new Intent(ChangePassword.this,loginActivity.class);
				startActivity(i);
				//setResult(AppConstants.RESULTCODE_LOGOUT);
				//finish();
			}
		});
		
		
		llBaseMiddle.addView(llChangePassword,new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		showHomeIcons();
        ivLogout.setVisibility(View.GONE);
		tvHeader.setText("Change Password");
	}

	
	public String passwordvalidation()
	{
		String message = "";
		if(txtoldpwd.getText().toString().equalsIgnoreCase(""))
		{
			message = "Please enter old password.";
		}
		else if(tvnewpwd.getText().toString().equalsIgnoreCase(""))
		{
			message = "Please enter new password.";
		}
		else if(tvconfirmpwd.getText().toString().equalsIgnoreCase(""))
		{
			message = "Please enter confirm new password.";
		}
		else if(!tvconfirmpwd.getText().toString().equalsIgnoreCase(tvconfirmpwd.getText().toString()))
		{
			message = "Confirm Password not matched with Nrew password.";
		}
		
		return message;
	}
	
	
	public void initializeControls()
	{
		txtoldpwd = (EditText)llChangePassword.findViewById(R.id.txtoldpwd);
		tvnewpwd = (EditText)llChangePassword.findViewById(R.id.tvnewpwd);
		tvconfirmpwd = (EditText)llChangePassword.findViewById(R.id.tvconfirmpwd);
		btnsubmit = (Button)llChangePassword.findViewById(R.id.btnsubmit);
	}


	@Override
	public void onDataretrieved(Object objs) 
	{
		String result =(String)objs ;
		if(result != null && !result.equalsIgnoreCase(""))
		{
			KeyValue keyValue_password = new KeyValue(AppConstants.password, tvnewpwd.getText().toString());
			SharedPrefUtils.setValue(ChangePassword.this, AppConstants.pref_name, keyValue_password);
			Toast.makeText(ChangePassword.this, result, Toast.LENGTH_SHORT).show();
			finish();
		}
		
		
	}


	@Override
	public void onDataretrievalFailed(String errorMessage) 
	{
		String result = errorMessage ;
		if(result != null && !result.equalsIgnoreCase(""))
		{
			Toast.makeText(ChangePassword.this, result, Toast.LENGTH_SHORT).show();
		}
	}

}
