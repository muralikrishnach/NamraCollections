package com.jayam.impactapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.matm.matmsdk.Bluetooth.BluetoothActivity;
import com.matm.matmsdk.MPOS.PosActivity;
import com.matm.matmsdk.Utils.MATMSDKConstant;
import com.matm.matmsdk.aepsmodule.AEPSHomeActivity;
import com.matm.matmsdk.aepsmodule.utils.AepsSdkConstants;

public class MainActivity extends AppCompatActivity {

   // private TextView aeps_view,micro_view;
    private EditText amnt_txt, loan_id;
    private Button aeps_submit_btn,matm_submit_btn,btnPair,upi_btn;
    RadioGroup rg_trans_type;
    RadioButton rb_cw, rb_be;
    String strTransType = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        amnt_txt = findViewById(R.id.amnt_txt);
        loan_id = findViewById(R.id.loan_id);
        aeps_submit_btn = findViewById(R.id.aeps_submit_btn);
        matm_submit_btn = findViewById(R.id.matm_submit_btn);
        rb_cw = findViewById(R.id.rb_cw);
        rb_be = findViewById(R.id.rb_be);
        rg_trans_type = findViewById(R.id.rg_trans_type);
       // micro_view = findViewById(R.id.micro_view);
        btnPair = findViewById(R.id.btnPair);
        upi_btn = findViewById(R.id.upi_btn);


        rg_trans_type.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_cw:
                        strTransType = "Cash Withdrawal";
                        amnt_txt.setText("");
                        amnt_txt.setEnabled(true);
                        amnt_txt.setVisibility(View.VISIBLE);
                        amnt_txt.setInputType(InputType.TYPE_CLASS_NUMBER);

                        break;
                    case R.id.rb_be:
                        strTransType = "Balance Enquiry";
                        amnt_txt.setVisibility(View.GONE);
                        amnt_txt.setClickable(false);
                        amnt_txt.setEnabled(false);
                        break;
                }
            }
        });

        matm_submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                MATMSDKConstant.transactionAmount = amnt_txt.getText().toString().trim();
                if(rb_cw.isChecked()){
                    MATMSDKConstant.transactionType = MATMSDKConstant.cashWithdrawal;
                    MATMSDKConstant.transactionAmount = amnt_txt.getText().toString();

                }if(rb_be.isChecked()){
                    MATMSDKConstant.transactionType= MATMSDKConstant.balanceEnquiry;
                    MATMSDKConstant.transactionAmount = "0";
                }

                MATMSDKConstant.paramA = "12345678";
                MATMSDKConstant.paramB = "Branch1";
                MATMSDKConstant.paramC = "loanID1234";
                MATMSDKConstant.loginID = "aepsTestR";
                MATMSDKConstant.encryptedData = "cssC%2BcHGxugRFLTjpk%2BJN2Hbbo%2F%2BDokPsBwb9uFdXebdGg%2FEaqOvFXBEoU7ve%2FAPTvmZ48yn4JCqcQ5Z6H09U%2B8x0I80PAnJMXPAFqayZ2Ojh6mRYKWc7D2YuCQC9hAR";
                Intent intent = new Intent(MainActivity.this, PosActivity.class);
                startActivityForResult(intent, MATMSDKConstant.REQUEST_CODE);
            }
        });

        aeps_submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AepsSdkConstants.transactionAmount = amnt_txt.getText().toString().trim();
                if (rb_cw.isChecked()){
                    AepsSdkConstants.transactionType = AepsSdkConstants.cashWithdrawal;
                    AepsSdkConstants.transactionAmount = amnt_txt.getText().toString();

                }

                if(rb_be.isChecked()){
                    AepsSdkConstants.transactionType = AepsSdkConstants.balanceEnquiry;
                    AepsSdkConstants.transactionAmount = "1";

                }
                AepsSdkConstants.paramA = "annapurna";
                AepsSdkConstants.paramB = "BLS1";
                AepsSdkConstants.paramC = "loanID";//loan_id.getText().toString().trim();
                AepsSdkConstants.loginID = "aepsTestR";
                AepsSdkConstants.encryptedData = "TYqmJRyB%2B4Mb39MQf%2BPqVhCbMYnW%2Bk4%2BiCnH24lkKjr4El8%2BnjuFhZw5lT26iLqno3cxOh6wUl5r4YDSECZYVg%3D%3D";

                Intent intent = new Intent(MainActivity.this, AEPSHomeActivity.class);
                startActivityForResult(intent, AepsSdkConstants.REQUEST_CODE);
            }
        });

        btnPair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity.this, BluetoothActivity.class);
                intent.putExtra("user_id","488");
                if (checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_DENIED) {
                    requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.BLUETOOTH, Manifest.permission.BLUETOOTH_ADMIN, Manifest.permission.BLUETOOTH_PRIVILEGED}, 1001);
                    Toast.makeText(getApplicationContext(),"Please Grant all the permissions", Toast.LENGTH_LONG).show();
                } else {
                    startActivity(intent);
                }
            }
        });
//        upi_btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(MainActivity.this,UPItransactionActivity.class);
//                startActivity(intent);
//            }
//        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data !=null & resultCode==RESULT_OK){
            if(requestCode == AepsSdkConstants.REQUEST_CODE){
                String response = data.getStringExtra(AepsSdkConstants.responseData);
                System.out.println("Response:" +response);
            }else if(requestCode == MATMSDKConstant.REQUEST_CODE){
                String response = data.getStringExtra(MATMSDKConstant.responseData);
                System.out.println("Response:" +response);
            }
    }
 }
}
