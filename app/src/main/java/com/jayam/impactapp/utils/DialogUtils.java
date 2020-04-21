package com.jayam.impactapp.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;



import java.security.cert.CertPathValidatorException;
import java.util.List;


public class DialogUtils {
	
	private static ProgressDialog progressDialog;
	private static Builder dialog;
	private static AlertDialog dialogMultiChoice;



	public static boolean showAlert(final Context context, String msg) {
		dialog = new Builder(context);

		dialog.setTitle("");
		dialog.setMessage(msg);
		dialog.setPositiveButton("OK", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		dialog.setCancelable(false);
		dialog.show();

		return false;
	}

	public static boolean showAlert(final Context context, String msg, OnClickListener onClickListener) {
		dialog = new Builder(context);

		dialog.setTitle("");
		dialog.setMessage(msg);
		dialog.setPositiveButton("OK", onClickListener);
		dialog.setCancelable(false);
		dialog.show();

		return false;
	}
	public static void showProgress(Context context, String msg) {
		progressDialog = new ProgressDialog(context);
		progressDialog.setMessage(msg);
		progressDialog.show();
	}





	public static void showProgressManuval(Context context, String msg) {
		progressDialog = new ProgressDialog(context);
		progressDialog.setMessage(msg);
		progressDialog.setCancelable(false);





		try {

			if(!((Activity) context).isFinishing())
			{
				if(progressDialog != null)
				{
					progressDialog.show();
				}
			}

		} catch (Exception e) {
			progressDialog.dismiss();
		}




	}
	public static void dismissProgress() {
		if(progressDialog != null && progressDialog.isShowing())
			progressDialog.dismiss();
	}



	public static ProgressDialog showProgressBarspecific(Context context, String msg) {
		progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(false);
        //progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        //progressDialog.setProgress(0);
//        progressDialog.setMax(100);
        progressDialog.setMessage(msg);
       // progressDialog.setProgressDrawable(context.getResources().getDrawable(R.drawable.progress_bar));
        progressDialog.show();
		return progressDialog;
	}




	public static void showSingleChoiceLIstItems(Context context, String[] itemsId, int checkedItem, String title, OnClickListener listener)
	{
		Builder builder = new Builder(context);
		builder.setSingleChoiceItems(itemsId, checkedItem, listener);
		builder.setTitle(title+"");
		builder.show();
	}


	
	public interface onMultiChoiceListener {
		void onMultiChoice(View v, List<CertPathValidatorException.Reason> list);
	}
}
