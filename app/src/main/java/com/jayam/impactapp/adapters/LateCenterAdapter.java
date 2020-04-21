package com.jayam.impactapp.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jayam.impactapp.CenterDetails;
import com.jayam.impactapp.DisplayRoute;
import com.jayam.impactapp.Groups;
import com.jayam.impactapp.LateGroups;
import com.jayam.impactapp.R;
import com.jayam.impactapp.database.RegularDemandsBL;
import com.jayam.impactapp.objects.BaseDO;
import com.jayam.impactapp.objects.RegularDemandsDO;
import com.jayam.impactapp.utils.DialogUtils;

import java.util.List;

public class LateCenterAdapter extends GenericAdapter {
    private String type;
    public String langitude, latitude;
    public	String dlatitude, dlangitude;
    public LateCenterAdapter(Context context, List<? extends BaseDO> listItems, String type, String latitude, String langitude) {
        super(context, listItems);
        this.type = type;
        this.langitude=langitude;
        this.latitude=latitude;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        RegularDemandsDO regularDemandsDO = (RegularDemandsDO) getList().get(position);
        convertView = getLayoutInflater().inflate(R.layout.center_cell, null);
        TextView tvCenterName = (TextView) convertView.findViewById(R.id.tvCenterName);

        TextView demandamount = (TextView) convertView.findViewById(R.id.tvdmamt);
        TextView maetingdate = (TextView) convertView.findViewById(R.id.tvmdate);
        ImageView imgConfirmed = (ImageView) convertView.findViewById(R.id.imgConfirmed);
        ImageView Location = (ImageView) convertView.findViewById(R.id.location);

        imgConfirmed.setVisibility(View.INVISIBLE);


        RegularDemandsBL bl = new RegularDemandsBL();
//        String fullpaymnet = bl.CheckForFullpayMent_Centerwise(regularDemandsDO.CNo);
//
//
//
//        if (fullpaymnet != null && fullpaymnet.equalsIgnoreCase("0")) {
//            imgConfirmed.setVisibility(View.VISIBLE);
//        } else {
//            imgConfirmed.setVisibility(View.GONE);
//        }

        tvCenterName.setText("" + regularDemandsDO.CName + "_" + regularDemandsDO.CNo);
        demandamount.setText( regularDemandsDO.CenterAmt);
        maetingdate.setText( regularDemandsDO.CenterMeeting);

        dlangitude=regularDemandsDO.LongitudeCenter;
        dlatitude=regularDemandsDO.LatitudeCenter;
        Log.v("","dlangitude"+dlangitude);
        Log.v("","dlatitude"+dlatitude);
        Log.e("position", "" + position);


        convertView.setTag(regularDemandsDO);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.v("","centernuberclick"+regularDemandsDO.CNo);
                    RegularDemandsDO regularDemandsDO = (RegularDemandsDO) v.getTag();
                    Intent intent = new Intent(mContext, LateGroups.class);
                    intent.putExtra("centercode", regularDemandsDO.CNo);
                    ((Activity) (mContext)).startActivityForResult(intent, 0);


            }
        });



        Location.setTag(regularDemandsDO);
        Location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(langitude!=null && !langitude.isEmpty() && latitude!=null && !latitude.isEmpty()) {

                    if(langitude!=null && !langitude.isEmpty())
                    {
                        langitude=langitude.trim();
                    }

                    if(latitude!=null && !latitude.isEmpty())
                    {
                        latitude=latitude.trim();
                    }
                    Intent intent = new Intent(mContext, DisplayRoute.class);
                    intent.putExtra("Lognitude", langitude);
                    intent.putExtra("Latitude", latitude);
                    intent.putExtra("DESLognitude", dlangitude);
                    intent.putExtra("DESLatitude", dlatitude);
                    ((Activity) (mContext)).startActivity(intent);
                }
                else
                {
                    DialogUtils.showAlert(mContext,"Latitude and Longitude not capture wait");
                }

            }
        });
        return convertView;
    }

}

