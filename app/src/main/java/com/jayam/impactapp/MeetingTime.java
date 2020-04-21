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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

public class MeetingTime extends Base
{
	private TextView tvStarttime, tvEndtime;
	private static TimePicker pickerstarttime, pickerendtime;
	private Button btnNext, btnCaptureImage;
	private String groupphoto;
	public static boolean isphotCaptured = false;
	public  String groupnumber;
	File sdImageMainDirectory;


	String datetime="";
	 String LastTranScode="";

	String FileName="";
	RegularDemandsBL regularDemandsBL;
	@Override
	public void initialize() 
	{
		regularDemandsBL=new RegularDemandsBL();
		datetime= UtilClass.GetCurrentdateNanoSeconds();
		Log.v("","datetime"+datetime);
		MeetingTime.isphotCaptured = false;
		intializeControlles();
		groupphoto	=	getIntent().getExtras().getString("groupphoto");
		groupnumber =   getIntent().getExtras().getString("groupnumber");
		
		File root = new File(Environment.getExternalStorageDirectory()+ File.separator + AppConstants.FolderName + File.separator);
		if(!root.exists())
			root.mkdirs();
		

		ArrayList<RegularDemandsDO>  vecRegularDemands = regularDemandsBL.SelectAll(groupnumber, "Groups",MeetingTime.this);
		IntialParametrsBL inpbl=new IntialParametrsBL();
		final ArrayList<IntialParametrsDO> alin=inpbl.SelectAll(MeetingTime.this);
		 LastTranScode= StringUtils.getTransactionCode_G(vecRegularDemands.get(0),MeetingTime.this);

		Log.v("","LastTranScode"+LastTranScode);

		FileName=LastTranScode+".JPEG";
		Log.v("","FileName"+FileName);
        sdImageMainDirectory = new File(root, FileName);
        
		Calendar cal=Calendar.getInstance();

		int year=cal.get(Calendar.YEAR);
		int month=cal.get(Calendar.MONTH);
		int day=cal.get(Calendar.DAY_OF_MONTH);
		int hour=cal.get(Calendar.HOUR_OF_DAY);
		int min=cal.get(Calendar.MINUTE);

		pickerendtime.setCurrentHour(hour);
		pickerendtime.setCurrentMinute(min);
		if(min > 10)
		{
			MeetingTime.pickerstarttime.setCurrentHour(hour);
			MeetingTime.pickerstarttime.setCurrentMinute(min-10);
		}
		else
		{
			if(hour > 1)
				MeetingTime.pickerstarttime.setCurrentHour(hour-1);
			else
			{
				MeetingTime.pickerstarttime.setCurrentHour(12);
			}
			MeetingTime.pickerstarttime.setCurrentMinute(59 - min);
		}
		
		
		
		btnCaptureImage.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) 
			{
				Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
	            startActivityForResult(cameraIntent, 111);
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
				Intent i = new Intent(MeetingTime.this,loginActivity.class);
				startActivity(i);
			//	setResult(AppConstants.RESULTCODE_LOGOUT);
			//	finish();
			}
		});
		btnNext.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v) 
			{
				if(alin.get(0).GroupPhoto.equalsIgnoreCase("1") && !MeetingTime.isphotCaptured)
				{
//					Uri outputFileUri = Uri.fromFile(sdImageMainDirectory);
//					Intent cameraIntent = new Intent("android.media.action.IMAGE_CAPTURE");
//					cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
//		            startActivityForResult(cameraIntent, 111);
		            
		            Uri outputFileUri = Uri.fromFile(sdImageMainDirectory);
		            Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
		            intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
                 	startActivityForResult( intent, 111 );
		    		
				}
				else
				{
					int starttime 	= MeetingTime.pickerstarttime.getCurrentHour()*60 + MeetingTime.pickerstarttime.getCurrentMinute();
					int endtime		= MeetingTime.pickerendtime.getCurrentHour()* 60 + MeetingTime.pickerendtime.getCurrentMinute();
					String time_start = (MeetingTime.pickerstarttime.getCurrentHour() < 9 ? ("0"+MeetingTime.pickerstarttime.getCurrentHour()) : MeetingTime.pickerstarttime.getCurrentHour())+":"+(MeetingTime.pickerstarttime.getCurrentMinute() < 9 ? ("0"+MeetingTime.pickerstarttime.getCurrentMinute()):MeetingTime.pickerstarttime.getCurrentMinute());
					String time_end   = (MeetingTime.pickerendtime.getCurrentHour() < 9 ? ("0"+MeetingTime.pickerendtime.getCurrentHour()) : MeetingTime.pickerendtime.getCurrentHour())+":"+(MeetingTime.pickerendtime.getCurrentMinute() < 9 ? ("0"+MeetingTime.pickerendtime.getCurrentMinute()):MeetingTime.pickerendtime.getCurrentMinute());
					if(starttime > endtime)
					{
						showAlertDailog("Start Time should not be greater than end time.");
					}
					else
					{
						// show confrimation screen

						Intent confmScreen = new Intent(MeetingTime.this, ConfirmationScreen.class);
						confmScreen.putExtra("groupnumber", groupnumber);
						startActivityForResult(confmScreen,123);
					}
					KeyValue keyValue_start = new  KeyValue(AppConstants.meetingStartTime, pickerstarttime.getCurrentHour()+":"+pickerstarttime.getCurrentMinute());
					KeyValue keyValue_end = new  KeyValue(AppConstants.meetingEnd, pickerendtime.getCurrentHour()+":"+pickerendtime.getCurrentMinute());
					SharedPrefUtils.setValue(MeetingTime.this, AppConstants.memberDetails_pref, keyValue_start);
					SharedPrefUtils.setValue(MeetingTime.this, AppConstants.memberDetails_pref, keyValue_end);

					Log.d("mfimo", time_start+""+time_end);
					regularDemandsBL.updateMertingTime(groupnumber, time_start+"", time_end+"",datetime,MeetingTime.this);
				}
			}
		});
	}
	
	public void intializeControlles()
	{
		LinearLayout llMeetingTime	=	(LinearLayout)inflater.inflate(R.layout.meetingtime,null);
		tvStarttime					=	(TextView)llMeetingTime.findViewById(R.id.tvStarttime);
		tvEndtime					=	(TextView)llMeetingTime.findViewById(R.id.tvEndtime);
		
		pickerstarttime				=	(TimePicker)llMeetingTime.findViewById(R.id.pickerstarttime);
		pickerendtime				=	(TimePicker)llMeetingTime.findViewById(R.id.pickerendtime);
		btnNext						=	(Button)llMeetingTime.findViewById(R.id.btnNext);
		btnCaptureImage				=	(Button)llMeetingTime.findViewById(R.id.btnCaptureImage);
		llBaseMiddle_lv.setVisibility(View.GONE);
		btnCaptureImage.setVisibility(View.GONE);
		llBaseMiddle.addView(llMeetingTime, LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		showHomeIcons();
        ivLogout.setVisibility(View.GONE);
		tvHeader.setText("Meeting Time");
	}
	
	public static void StoreImage(android.content.Context mContext, Uri imageLoc, File imageDir)
    {
        Bitmap bm = null;
        try 
        {
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
		else if(requestCode == 111 && resultCode == RESULT_OK)
		{
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
						DialogUtils.showAlert(MeetingTime.this,"Image Size Exceed 1 MB Please take once Again");

						//new File(sdImageMainDirectory, FileName).delete();
					}
					else
					{
						showAlertDailog("Photo Captured successfully", "Ok", new CustomDailoglistner() {
							@Override
							public void onPossitiveButtonClick(DialogInterface dialog) {
								int starttime 	= MeetingTime.pickerstarttime.getCurrentHour()*60 + MeetingTime.pickerstarttime.getCurrentMinute();
								int endtime		= MeetingTime.pickerendtime.getCurrentHour()* 60 + MeetingTime.pickerendtime.getCurrentMinute();
								String time_start = MeetingTime.pickerstarttime.getCurrentHour() < 9 ? ("0"+MeetingTime.pickerstarttime.getCurrentHour()) : MeetingTime.pickerstarttime.getCurrentHour()+":"+(MeetingTime.pickerstarttime.getCurrentMinute() < 9 ? ("0"+MeetingTime.pickerstarttime.getCurrentMinute()):MeetingTime.pickerstarttime.getCurrentMinute());
								String time_end   = MeetingTime.pickerendtime.getCurrentHour() < 9 ? ("0"+MeetingTime.pickerendtime.getCurrentHour()) : MeetingTime.pickerendtime.getCurrentHour()+":"+(MeetingTime.pickerendtime.getCurrentMinute() < 9 ? ("0"+MeetingTime.pickerendtime.getCurrentMinute()):MeetingTime.pickerendtime.getCurrentMinute());
								if(starttime > endtime)
								{
									showAlertDailog("Start Time should not be greater than end time.");
								}
								else
								{
									Intent confmScreen = new Intent(MeetingTime.this, ConfirmationScreen.class);
									confmScreen.putExtra("groupnumber", groupnumber);
									startActivityForResult(confmScreen,123);
								}
								KeyValue keyValue_start = new  KeyValue(AppConstants.meetingStartTime, pickerstarttime.getCurrentHour()+":"+pickerstarttime.getCurrentMinute());
								KeyValue keyValue_end = new  KeyValue(AppConstants.meetingEnd, pickerendtime.getCurrentHour()+":"+pickerendtime.getCurrentMinute());
								SharedPrefUtils.setValue(MeetingTime.this, AppConstants.memberDetails_pref, keyValue_start);
								SharedPrefUtils.setValue(MeetingTime.this, AppConstants.memberDetails_pref, keyValue_end);
								RegularDemandsBL regularDemandsBL = new RegularDemandsBL();
								Log.d("mfimo", time_start+""+time_end);
								regularDemandsBL.updateImageName(groupnumber, FileName,MeetingTime.this);
								regularDemandsBL.updateMertingTime(groupnumber, time_start+"", time_end+"",datetime,MeetingTime.this);
							}

							@Override
							public void onNegativeButtonClick(DialogInterface dialog) {
								// TODO Auto-generated method stub

							}
						});
					}

				}
				else if(length.contains("KiB"))
				{

					showAlertDailog("Photo Captured successfully", "Ok", new CustomDailoglistner() {
						@Override
						public void onPossitiveButtonClick(DialogInterface dialog) {
							int starttime 	= MeetingTime.pickerstarttime.getCurrentHour()*60 + MeetingTime.pickerstarttime.getCurrentMinute();
							int endtime		= MeetingTime.pickerendtime.getCurrentHour()* 60 + MeetingTime.pickerendtime.getCurrentMinute();
							String time_start = MeetingTime.pickerstarttime.getCurrentHour() < 9 ? ("0"+MeetingTime.pickerstarttime.getCurrentHour()) : MeetingTime.pickerstarttime.getCurrentHour()+":"+(MeetingTime.pickerstarttime.getCurrentMinute() < 9 ? ("0"+MeetingTime.pickerstarttime.getCurrentMinute()):MeetingTime.pickerstarttime.getCurrentMinute());
							String time_end   = MeetingTime.pickerendtime.getCurrentHour() < 9 ? ("0"+MeetingTime.pickerendtime.getCurrentHour()) : MeetingTime.pickerendtime.getCurrentHour()+":"+(MeetingTime.pickerendtime.getCurrentMinute() < 9 ? ("0"+MeetingTime.pickerendtime.getCurrentMinute()):MeetingTime.pickerendtime.getCurrentMinute());
							if(starttime > endtime)
							{
								showAlertDailog("Start Time should not be greater than end time.");
							}
							else
							{
								Intent confmScreen = new Intent(MeetingTime.this, ConfirmationScreen.class);
								confmScreen.putExtra("groupnumber", groupnumber);
								startActivityForResult(confmScreen,123);
							}
							KeyValue keyValue_start = new  KeyValue(AppConstants.meetingStartTime, pickerstarttime.getCurrentHour()+":"+pickerstarttime.getCurrentMinute());
							KeyValue keyValue_end = new  KeyValue(AppConstants.meetingEnd, pickerendtime.getCurrentHour()+":"+pickerendtime.getCurrentMinute());
							SharedPrefUtils.setValue(MeetingTime.this, AppConstants.memberDetails_pref, keyValue_start);
							SharedPrefUtils.setValue(MeetingTime.this, AppConstants.memberDetails_pref, keyValue_end);
							RegularDemandsBL regularDemandsBL = new RegularDemandsBL();
							Log.d("mfimo", time_start+""+time_end);
							regularDemandsBL.updateImageName(groupnumber, FileName,MeetingTime.this);
							regularDemandsBL.updateMertingTime(groupnumber, time_start+"", time_end+"",datetime,MeetingTime.this);
						}

						@Override
						public void onNegativeButtonClick(DialogInterface dialog) {
							// TODO Auto-generated method stub

						}
					});
				}


				else
				{
					DialogUtils.showAlert(MeetingTime.this,"Image is Not Compressed");
					//new File(sdImageMainDirectory, FileName).delete();
				}
			}
			else
			{
				DialogUtils.showAlert(MeetingTime.this,"Image is Not Compressed");
				//new File(sdImageMainDirectory, FileName).delete();
			}



			Calendar cal=Calendar.getInstance();
			int year=cal.get(Calendar.YEAR);
			int month=cal.get(Calendar.MONTH);
			int day=cal.get(Calendar.DAY_OF_MONTH);
			int hour=cal.get(Calendar.HOUR_OF_DAY);
			int min=cal.get(Calendar.MINUTE);

			pickerendtime.setCurrentHour(hour);
			pickerendtime.setCurrentMinute(min);
			if(min > 10)
			{
				MeetingTime.pickerstarttime.setCurrentHour(hour);
				MeetingTime.pickerstarttime.setCurrentMinute(min-10);
			}
			else
			{
				if(hour > 1)
					MeetingTime.pickerstarttime.setCurrentHour(hour-1);
				else
				{
					MeetingTime.pickerstarttime.setCurrentHour(12);
				}
				MeetingTime.pickerstarttime.setCurrentMinute(59 - min);
			}
			
			try
			{
//				File root = new File(Environment.getExternalStorageDirectory()+ File.separator + "MFIMO_Images" + File.separator);
//				if(!root.exists())
//					root.mkdirs();
//				
//				if(sdImageMainDirectory == null)
//					sdImageMainDirectory = new File(root, groupnumber+"myPicName.JPEG");
//				Uri photoUri=data.getData();
//		        Log.d("mfimo", photoUri.toString());
//				StoreImage(MeetingTime.this, photoUri, sdImageMainDirectory);
			}
			catch (Exception e)
			{
				Log.e("CAM",e.toString());
				e.printStackTrace();
			}
		}
	}
}
