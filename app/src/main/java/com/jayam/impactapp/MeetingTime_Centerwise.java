package com.jayam.impactapp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import com.jayam.impactapp.common.AppConstants;
import com.jayam.impactapp.common.CustomDailoglistner;
import com.jayam.impactapp.database.IntialParametrsBL;
import com.jayam.impactapp.database.RegularDemandsBL;
import com.jayam.impactapp.objects.IntialParametrsDO;
import com.jayam.impactapp.objects.KeyValue;
import com.jayam.impactapp.objects.RegularDemandsDO;
import com.jayam.impactapp.utils.DialogUtils;
import com.jayam.impactapp.utils.ImageCompress;
import com.jayam.impactapp.utils.SharedPrefUtils;
import com.jayam.impactapp.utils.StringUtils;
import com.jayam.impactapp.utils.UtilClass;


import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.MediaStore.Images.Media;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

public class MeetingTime_Centerwise extends Base {
    private TextView tvStarttime, tvEndtime;
    private TimePicker pickerstarttime, pickerendtime;
    private Button btnNext, btnCaptureImage;
    private String groupphoto;
    public boolean isphotCaptured = false;
    public String centernuber;
    File sdImageMainDirectory;
	String datetime="";
	String LastTranScode="";

	String FileName="";
    @Override
    public void initialize() {
		datetime= UtilClass.GetCurrentdateNanoSeconds();
		Log.v("","datetime"+datetime);
	intializeControlles();

	centernuber = getIntent().getExtras().getString("centernuber");
	groupphoto = getIntent().getExtras().getString("groupphoto");
	final RegularDemandsBL regularDemandsBL = new RegularDemandsBL();
	final ArrayList<RegularDemandsDO> alregularDemandsBL = regularDemandsBL.SelectAll(centernuber, "CNo",MeetingTime_Centerwise.this);
	 LastTranScode = StringUtils.getTransactionCode_C(alregularDemandsBL.get(0),MeetingTime_Centerwise.this);
		Log.v("","LastTranScode"+LastTranScode);
		FileName=LastTranScode+".JPEG";
		Log.v("","FileName"+FileName);


		IntialParametrsBL inpbl = new IntialParametrsBL();
	final ArrayList<IntialParametrsDO> alin = inpbl.SelectAll(MeetingTime_Centerwise.this);
	File root = new File(Environment.getExternalStorageDirectory() + File.separator + AppConstants.FolderName + File.separator);
	if (!root.exists()) {
	    root.mkdirs();
	}
	sdImageMainDirectory = new File(root, LastTranScode + ".JPEG");
	Calendar cal = Calendar.getInstance();

	int year = cal.get(Calendar.YEAR);
	int month = cal.get(Calendar.MONTH);
	int day = cal.get(Calendar.DAY_OF_MONTH);
	int hour = cal.get(Calendar.HOUR_OF_DAY);
	int min = cal.get(Calendar.MINUTE);

	pickerendtime.setCurrentHour(hour);
	pickerendtime.setCurrentMinute(min);
	if (min > 10) {
	    pickerstarttime.setCurrentHour(hour);
	    pickerstarttime.setCurrentMinute(min - 10);
	} else {
	    if (hour > 1) {
		pickerstarttime.setCurrentHour(hour - 1);
	    } else {
		pickerstarttime.setCurrentHour(12);
	    }
	    pickerstarttime.setCurrentMinute(59 - min);
	}
	//
	// if(groupphoto != null && groupphoto.equalsIgnoreCase("1"))
	// {
	// btnCaptureImage.setVisibility(View.VISIBLE);
	// }
	// else
	// {
	// btnCaptureImage.setVisibility(View.GONE);
	// }
	//
	btnCaptureImage.setOnClickListener(new OnClickListener() {
	    @Override
	    public void onClick(View v) {
		Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
		startActivityForResult(cameraIntent, 111);
	    }
	});

	ivHome.setOnClickListener(new OnClickListener() {
	    @Override
	    public void onClick(View v) {
		setResult(AppConstants.RESULTCODE_HOME);
		finish();
	    }
	});

	ivLogout.setOnClickListener(new OnClickListener() {
	    @Override
	    public void onClick(View v) {
		Intent i = new Intent(MeetingTime_Centerwise.this, loginActivity.class);
		startActivity(i);
		// setResult(AppConstants.RESULTCODE_LOGOUT);
		// finish();
	    }
	});
	btnNext.setOnClickListener(new OnClickListener() {
	    @Override
	    public void onClick(View v) {
		if (alin.get(0).GroupPhoto.equalsIgnoreCase("1") && !isphotCaptured) {
		    Uri outputFileUri = Uri.fromFile(sdImageMainDirectory);
		    Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
		    intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
		    startActivityForResult(intent, 111);
		} else {
		    int starttime = pickerstarttime.getCurrentHour() * 60 + pickerstarttime.getCurrentMinute();
		    int endtime = pickerendtime.getCurrentHour() * 60 + pickerendtime.getCurrentMinute();
		    String time_start = pickerstarttime.getCurrentHour() < 9 ? ("0" + pickerstarttime.getCurrentHour())
			    : pickerstarttime.getCurrentHour() + ":"
				    + (pickerstarttime.getCurrentMinute() < 9
					    ? ("0" + pickerstarttime.getCurrentMinute())
					    : pickerstarttime.getCurrentMinute())
				    + (pickerstarttime.getCurrentHour() < 12 ? "AM" : "PM");
		    String time_end = pickerendtime.getCurrentHour() < 9 ? ("0" + pickerendtime.getCurrentHour())
			    : pickerendtime.getCurrentHour() + ":"
				    + (pickerendtime.getCurrentMinute() < 9 ? ("0" + pickerendtime.getCurrentMinute())
					    : pickerendtime.getCurrentMinute())
				    + (pickerendtime.getCurrentHour() < 12 ? "AM" : "PM");
		    if (starttime > endtime) {
			showAlertDailog("Start Time should not be greater than end time.");
		    } else {
			// show confrimation screen

			Intent confmScreen = new Intent(MeetingTime_Centerwise.this,
				ConfirmationScreen_Centerwise.class);
			confmScreen.putExtra("centernuber", centernuber);
			startActivityForResult(confmScreen, 123);
		    }
		    KeyValue keyValue_start = new KeyValue(AppConstants.meetingStartTime,
			    pickerstarttime.getCurrentHour() + ":" + pickerstarttime.getCurrentMinute());
		    KeyValue keyValue_end = new KeyValue(AppConstants.meetingEnd,
			    pickerendtime.getCurrentHour() + ":" + pickerendtime.getCurrentMinute());
		    SharedPrefUtils.setValue(MeetingTime_Centerwise.this, AppConstants.memberDetails_pref,
			    keyValue_start);
		    SharedPrefUtils.setValue(MeetingTime_Centerwise.this, AppConstants.memberDetails_pref,
			    keyValue_end);
		    RegularDemandsBL regularDemandsBL = new RegularDemandsBL();
		    Log.d("mfimo", time_start + "" + time_end);
		    regularDemandsBL.updateMertingTime_CenterWise(centernuber, time_start + "", time_end + "",datetime,MeetingTime_Centerwise.this);
		}

	    }
	});

    }

    public void intializeControlles() {
	LinearLayout llMeetingTime = (LinearLayout) inflater.inflate(R.layout.meetingtime, null);
	tvStarttime = (TextView) llMeetingTime.findViewById(R.id.tvStarttime);
	tvEndtime = (TextView) llMeetingTime.findViewById(R.id.tvEndtime);

	pickerstarttime = (TimePicker) llMeetingTime.findViewById(R.id.pickerstarttime);
	pickerendtime = (TimePicker) llMeetingTime.findViewById(R.id.pickerendtime);
	btnNext = (Button) llMeetingTime.findViewById(R.id.btnNext);
	btnCaptureImage = (Button) llMeetingTime.findViewById(R.id.btnCaptureImage);
	llBaseMiddle_lv.setVisibility(View.GONE);
	btnCaptureImage.setVisibility(View.GONE);
	llBaseMiddle.addView(llMeetingTime, LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
	showHomeIcons();
	tvHeader.setText("Meeting Time");
        ivLogout.setVisibility(View.GONE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	super.onActivityResult(requestCode, resultCode, data);

	if (resultCode == AppConstants.RESULTCODE_LOGOUT) {
	    setResult(resultCode);
	    finish();
	} else if (resultCode == AppConstants.RESULTCODE_HOME) {
	    setResult(resultCode);
	    finish();
	}
	else if (requestCode == 111 && resultCode == RESULT_OK) {

		ImageCompress bmp= new ImageCompress();

		File  root = new File(Environment.getExternalStorageDirectory()+ File.separator + AppConstants.FolderName + File.separator);
		String image=   bmp.compressImage(root,FileName);

		String length=bmp.getFileSize(sdImageMainDirectory);
		Log.v("","length"+length);
        String repdata="";
        double val=0;
        if(length!=null&&!length.isEmpty())
        {
            if(length.contains("MiB"))
            {
                System.out.println("if"+length);
                repdata=length.replaceAll("MiB", "");
                try
                {
                    val=Double.parseDouble(repdata);
                }catch (NumberFormatException n)
                {
                    n.printStackTrace();
                }
                if(val>1)
                {
                    DialogUtils.showAlert(MeetingTime_Centerwise.this,"Image Size Exceed 1 MB Please take once Again");

                    new File(sdImageMainDirectory, FileName).delete();
                }
                else
                {
                    showAlertDailog("Photo Captured successfully", "OK", new CustomDailoglistner() {
                        @Override
                        public void onPossitiveButtonClick(DialogInterface dialog) {
                            int starttime = pickerstarttime.getCurrentHour() * 60 + pickerstarttime.getCurrentMinute();
                            int endtime = pickerendtime.getCurrentHour() * 60 + pickerendtime.getCurrentMinute();
                            String time_start = pickerstarttime.getCurrentHour() < 9 ? ("0" + pickerstarttime.getCurrentHour())
                                    : pickerstarttime.getCurrentHour() + ":"
                                    + (pickerstarttime.getCurrentMinute() < 9
                                    ? ("0" + pickerstarttime.getCurrentMinute())
                                    : pickerstarttime.getCurrentMinute())
                                    + (pickerstarttime.getCurrentHour() < 12 ? "AM" : "PM");
                            String time_end = pickerendtime.getCurrentHour() < 9 ? ("0" + pickerendtime.getCurrentHour())
                                    : pickerendtime.getCurrentHour() + ":"
                                    + (pickerendtime.getCurrentMinute() < 9 ? ("0" + pickerendtime.getCurrentMinute())
                                    : pickerendtime.getCurrentMinute())
                                    + (pickerendtime.getCurrentHour() < 12 ? "AM" : "PM");
                            if (starttime > endtime) {
                                showAlertDailog("Start Time should not be greater than end time.");
                            } else {
                                // show confrimation screen
                                Intent confmScreen = new Intent(MeetingTime_Centerwise.this,
                                        ConfirmationScreen_Centerwise.class);
                                confmScreen.putExtra("centernuber", centernuber);
                                startActivityForResult(confmScreen, 123);
                            }
                            KeyValue keyValue_start = new KeyValue(AppConstants.meetingStartTime,
                                    pickerstarttime.getCurrentHour() + ":" + pickerstarttime.getCurrentMinute());
                            KeyValue keyValue_end = new KeyValue(AppConstants.meetingEnd,
                                    pickerendtime.getCurrentHour() + ":" + pickerendtime.getCurrentMinute());
                            SharedPrefUtils.setValue(MeetingTime_Centerwise.this, AppConstants.memberDetails_pref,
                                    keyValue_start);
                            SharedPrefUtils.setValue(MeetingTime_Centerwise.this, AppConstants.memberDetails_pref,
                                    keyValue_end);
                            RegularDemandsBL regularDemandsBL = new RegularDemandsBL();
                            Log.d("mfimo", time_start + "" + time_end);
                            regularDemandsBL.updateGroupphotoCenterWise(centernuber, FileName,MeetingTime_Centerwise.this);
                            regularDemandsBL.updateMertingTime_CenterWise(centernuber, time_start + "", time_end + "",datetime,MeetingTime_Centerwise.this);
                        }

                        @Override
                        public void onNegativeButtonClick(DialogInterface dialog) {

                        }
                    });
                }

            }
            else if(length.contains("KiB"))
            {

                showAlertDailog("Photo Captured successfully", "OK", new CustomDailoglistner() {
                    @Override
                    public void onPossitiveButtonClick(DialogInterface dialog) {
                        int starttime = pickerstarttime.getCurrentHour() * 60 + pickerstarttime.getCurrentMinute();
                        int endtime = pickerendtime.getCurrentHour() * 60 + pickerendtime.getCurrentMinute();
                        String time_start = pickerstarttime.getCurrentHour() < 9 ? ("0" + pickerstarttime.getCurrentHour())
                                : pickerstarttime.getCurrentHour() + ":"
                                + (pickerstarttime.getCurrentMinute() < 9
                                ? ("0" + pickerstarttime.getCurrentMinute())
                                : pickerstarttime.getCurrentMinute())
                                + (pickerstarttime.getCurrentHour() < 12 ? "AM" : "PM");
                        String time_end = pickerendtime.getCurrentHour() < 9 ? ("0" + pickerendtime.getCurrentHour())
                                : pickerendtime.getCurrentHour() + ":"
                                + (pickerendtime.getCurrentMinute() < 9 ? ("0" + pickerendtime.getCurrentMinute())
                                : pickerendtime.getCurrentMinute())
                                + (pickerendtime.getCurrentHour() < 12 ? "AM" : "PM");
                        if (starttime > endtime) {
                            showAlertDailog("Start Time should not be greater than end time.");
                        } else {
                            // show confrimation screen
                            Intent confmScreen = new Intent(MeetingTime_Centerwise.this,
                                    ConfirmationScreen_Centerwise.class);
                            confmScreen.putExtra("centernuber", centernuber);
                            startActivityForResult(confmScreen, 123);
                        }
                        KeyValue keyValue_start = new KeyValue(AppConstants.meetingStartTime,
                                pickerstarttime.getCurrentHour() + ":" + pickerstarttime.getCurrentMinute());
                        KeyValue keyValue_end = new KeyValue(AppConstants.meetingEnd,
                                pickerendtime.getCurrentHour() + ":" + pickerendtime.getCurrentMinute());
                        SharedPrefUtils.setValue(MeetingTime_Centerwise.this, AppConstants.memberDetails_pref,
                                keyValue_start);
                        SharedPrefUtils.setValue(MeetingTime_Centerwise.this, AppConstants.memberDetails_pref,
                                keyValue_end);
                        RegularDemandsBL regularDemandsBL = new RegularDemandsBL();
                        Log.d("mfimo", time_start + "" + time_end);
                        regularDemandsBL.updateGroupphotoCenterWise(centernuber, FileName,MeetingTime_Centerwise.this);
                        regularDemandsBL.updateMertingTime_CenterWise(centernuber, time_start + "", time_end + "",datetime,MeetingTime_Centerwise.this);
                    }

                    @Override
                    public void onNegativeButtonClick(DialogInterface dialog) {

                    }
                });
            }


            else
            {
                DialogUtils.showAlert(MeetingTime_Centerwise.this,"Image is Not Compressed");
                new File(sdImageMainDirectory, FileName).delete();
            }
        }
        else
        {
            DialogUtils.showAlert(MeetingTime_Centerwise.this,"Image is Not Compressed");
            new File(sdImageMainDirectory, FileName).delete();
        }





	    // StoreImage(MeetingTime_Centerwise.this, Uri.parse(data.toURI()),
	    // sdImageMainDirectory);
	}
    }

    public static void StoreImage(android.content.Context mContext, Uri imageLoc, File imageDir) {
	Bitmap bm = null;
	try {
	    bm = Media.getBitmap(mContext.getContentResolver(), imageLoc);
	    FileOutputStream out = new FileOutputStream(imageDir);
	    bm.compress(Bitmap.CompressFormat.JPEG, 100, out);
	    bm.recycle();

	} catch (FileNotFoundException e) {
	    e.printStackTrace();
	} catch (IOException e) {
	    e.printStackTrace();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

}
