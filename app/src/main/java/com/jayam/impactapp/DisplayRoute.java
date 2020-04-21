package com.jayam.impactapp;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.util.Log;

/**
 * Created by administrator_pc on 24-05-2018.
 */

public class DisplayRoute extends Activity {
    GPSTrackevalue gps;
    String latitude, langitude;
    String deslatitude, deslangitude;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(this.getIntent().getExtras()!=null && this.getIntent().getExtras().containsKey("Lognitude"))
        {
            langitude=getIntent().getExtras().getString("Lognitude");
        }

        if(this.getIntent().getExtras()!=null && this.getIntent().getExtras().containsKey("Latitude"))
        {
            latitude=getIntent().getExtras().getString("Latitude");
        }


        if(this.getIntent().getExtras()!=null && this.getIntent().getExtras().containsKey("DESLognitude"))
        {
            deslangitude=getIntent().getExtras().getString("DESLognitude");
        }

        if(this.getIntent().getExtras()!=null && this.getIntent().getExtras().containsKey("DESLatitude"))
        {
            deslatitude=getIntent().getExtras().getString("DESLatitude");
        }

//        gps=new GPSTrackevalue(DisplayRoute.this);
        setContentView(R.layout.maps);


//        if (gps.canGetLocation()) {
//            latitude = gps.getLatitude() + "";
//            langitude = gps.getLongitude() + "";
//            Log.v("mfimo", "latitude" + latitude);
//            Log.v("mfimo", "langitude" + langitude);
//             Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + langitude, Toast.LENGTH_LONG).show();
//
//
//
//        } else {
//            gps.showSettingsAlert();
//        }

        if(langitude!=null && !langitude.isEmpty())
        {
            langitude=langitude.trim();
        }

        if(latitude!=null && !latitude.isEmpty())
        {
            latitude=latitude.trim();
        }

        String urladdress="http://maps.google.com/maps?saddr="+latitude+","+langitude+"&daddr="+deslatitude+","+deslangitude;
        Log.v("","urladdress"+urladdress);
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(urladdress));

//        Uri.parse("http://maps.google.com/maps?saddr='"+latitude+"','"+langitude+"'&daddr=17.4474,78.3762"));

        startActivity(intent);
    }
}
