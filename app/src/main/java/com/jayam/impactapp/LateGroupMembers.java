package com.jayam.impactapp;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.jayam.impactapp.adapters.GroupMembersAdapter;
import com.jayam.impactapp.adapters.LateGroupMembersAdapter;
import com.jayam.impactapp.common.AppConstants;
import com.jayam.impactapp.database.RegularDemandsBLTemp;
import com.jayam.impactapp.objects.RegularDemandsDO;
import com.jayam.impactapp.utils.SharedPrefUtils;

import java.util.ArrayList;

public class LateGroupMembers extends Base
{
    private LinearLayout llGroupMembers;
    private ListView lvGroupMembers;
    private String groupnumber;
    private String TxnType;
    private LateGroupMembersAdapter groupMembersAdapter;
    @Override
    public void initialize()
    {
        groupnumber		=	 getIntent().getExtras().getString("groupnumber");

        intializeControlles();
        RegularDemandsBLTemp regularDemandsBL = new RegularDemandsBLTemp();
        ArrayList<RegularDemandsDO> alregularDemandsBL = regularDemandsBL.SelectMemberssLate(groupnumber,LateGroupMembers.this);
        groupMembersAdapter.refresh(alregularDemandsBL);

        ivHome.setOnClickListener(new  View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                setResult(AppConstants.RESULTCODE_HOME);
                finish();
            }
        });

        ivLogout.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent(LateGroupMembers.this,loginActivity.class);
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

        groupMembersAdapter = new LateGroupMembersAdapter(LateGroupMembers.this, null,TxnType);
        lvGroupMembers.setAdapter(groupMembersAdapter);
        svBase.setVisibility(View.GONE);
        llBaseMiddle_lv.setVisibility(View.VISIBLE);
        llBaseMiddle_lv.addView(llGroupMembers, ViewGroup.LayoutParams.FILL_PARENT, ViewGroup.LayoutParams.FILL_PARENT);
        showHomeIcons();
        ivLogout.setVisibility(View.GONE);
        String URL= SharedPrefUtils.getKeyValue(this, AppConstants.pref_name, AppConstants.UrlAddress);
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
