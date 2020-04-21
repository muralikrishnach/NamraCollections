package com.jayam.impactapp.database;

import java.util.ArrayList;
import java.util.List;

import com.jayam.impactapp.objects.BaseDO;
import com.jayam.impactapp.objects.IntialParametrsDO;
import com.jayam.impactapp.objects.LoanProduct;
import com.jayam.impactapp.objects.MasterDataDo;
import com.jayam.impactapp.objects.RepaymentDetails;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

public class IntialParametrsBL
{


	public boolean Insert(BaseDO object,Context context)
	{
		IntialParametrsDO parametrsDO = (IntialParametrsDO) object;
		

		
		ContentValues values = new ContentValues();

			values.put("WorkMode", parametrsDO.WorkMode);
			values.put("TerminalID", parametrsDO.TerminalID);
			values.put("MACID", parametrsDO.MACID);
			values.put("ReceiptHeader", parametrsDO.ReceiptHeader);
			values.put("ReceiptFooter", parametrsDO.ReceiptFooter);
			values.put("PrinterBTAddress", parametrsDO.BTPrinterAddress);
			values.put("AgentCopy", parametrsDO.AgentCopy);
			values.put("DaysOffset", parametrsDO.DaysOffSet);
			values.put("MeetingTime", parametrsDO.MeetingTime);
			values.put("TimeOut", parametrsDO.TimeOut);
			values.put("ServerUrl", parametrsDO.ServerUrl);
			values.put("MaxTransactions", parametrsDO.MaxTransactions);
			values.put("MaxAmount", parametrsDO.MaxAmount);
			values.put("Code", parametrsDO.Code);
			values.put("UserName", parametrsDO.UserName);
			values.put("LastTransactionID", parametrsDO.LastTransactionID);
			values.put("PartialPayment", parametrsDO.PartialPayment);
			values.put("AdvPayment", parametrsDO.AdvPayment);
			values.put("AdvDemandDwds", parametrsDO.AdvDemandDwds);
			values.put("MemberAttendance", parametrsDO.MemberAttendance);
			values.put("IndividualReceipts", parametrsDO.IndividualReceipts);
			values.put("GLI", parametrsDO.GLI);
			values.put("Lateness", parametrsDO.Lateness);
			values.put("GroupPhoto", parametrsDO.GroupPhoto);
			values.put("LastTxnCode", parametrsDO.LastTransactionCode);
			values.put("PrintType", parametrsDO.PrintType);
			values.put("InstRequired", parametrsDO.InstRequired);
			values.put("PhotoNos", parametrsDO.PhotoNos);
			values.put("ValidatePrinter", parametrsDO.ValidatePrinter);
			values.put("PrintValidate", parametrsDO.PrintValidate);
			values.put("LogoPrinting", parametrsDO.LogoPrinting);
			values.put("QOM", parametrsDO.qom);
			values.put("ProbInCenter", parametrsDO.probInCenter);
			values.put("GroupDiscipline", parametrsDO.groupDiscipline);
			values.put("CollExp", parametrsDO.collExp);
			values.put("CollExpRMEL", parametrsDO.collExpRMEL);
			values.put("CollPlace", parametrsDO.collPlace);
			values.put("RepaymentMadeBy", parametrsDO.repaymentMadeBy);
			values.put("RmelUser", parametrsDO.rmelUser);

		values.put("BranchName", parametrsDO.BranchName);
		values.put("UserId", parametrsDO.UserID);
			Log.d("mfimo", parametrsDO.qom+"asdfds "+parametrsDO.probInCenter+"adfasf "+parametrsDO.groupDiscipline+"dfdfd "+parametrsDO.collExp+""+parametrsDO.collExpRMEL+""+parametrsDO.collPlace+""+parametrsDO.repaymentMadeBy);
		//edit here
		DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
		SQLiteDatabase _database = databaseHelper.getWritableDatabase();
		//_database.beginTransaction();
		try
		{
			_database.insert("Intialparameters", null, values);
			//_database.setTransactionSuccessful();
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		finally {
			//_database.endTransaction();
	    }
		return false;
	}


	public boolean Update(BaseDO object,Context context)
	{
		return false;
	}

	public void updateReceiptNumber(String reciptNumber,Context context )
	{

		String query = "UPDATE Intialparameters SET LastTransactionID= '"+reciptNumber+"'";
		 Log.e("updatequery", query);
		DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
		SQLiteDatabase _database = databaseHelper.getWritableDatabase();
		//_database.beginTransaction();
		try
		{
			SQLiteStatement sqLiteStatement = _database.compileStatement(query);
			sqLiteStatement.executeInsert();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally {
			//_database.setTransactionSuccessful();
			//_database.endTransaction();
			//_database.close();
		}

	
	}
	
	public void updateLastTransctionCode(String TxnNumber,Context context )
	{

		String query = "UPDATE Intialparameters SET LastTxnCode= '"+TxnNumber+"'";
		 Log.e("updatequery", query);
		DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
		SQLiteDatabase _database = databaseHelper.getWritableDatabase();
		//_database.beginTransaction();
		try
		{
			SQLiteStatement sqLiteStatement = _database.compileStatement(query);
			sqLiteStatement.executeInsert();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally {
			//_database.setTransactionSuccessful();
			//_database.endTransaction();
			//_database.close();
		}
	
	}
	

	public boolean Delete(BaseDO object,Context context)
	{
		


		DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
		SQLiteDatabase _database = databaseHelper.getWritableDatabase();
		//_database.beginTransaction();
		try
		{
			String query="DELETE  FROM Intialparameters";
			Log.v("","queryinit"+query);
			_database.execSQL(query);
			//_database.setTransactionSuccessful();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally {
			//_database.endTransaction();
		}

		return false;
	}


	public synchronized ArrayList<IntialParametrsDO> SelectAll(Context context )
	{

		ArrayList<IntialParametrsDO> vecIntialParams = new ArrayList<IntialParametrsDO>();
		DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
		SQLiteDatabase _database = databaseHelper.getReadableDatabase();
		//_database.beginTransaction();
		  Cursor c = null;
		  try 
		  {
		   c= _database.rawQuery("select * from Intialparameters", null);
		   
		   if(vecIntialParams != null)
		    vecIntialParams.clear();
		   
		   if(c.moveToFirst())
		   {
		    do
		    {
		    	
		    	IntialParametrsDO obj = new IntialParametrsDO();
		    	obj.WorkMode 			= c.getString(0);
		    	obj.TerminalID 			= c.getString(1);
		    	obj.MACID 				= c.getString(2);
		    	obj.ReceiptHeader 		= c.getString(3);
		    	obj.ReceiptFooter 		= c.getString(4);
		    	obj.BTPrinterAddress 	= c.getString(5);
		    	obj.AgentCopy 			= c.getString(6);
		    	obj.DaysOffSet 			= c.getString(7);
		    	obj.MeetingTime 		= c.getString(8);
		    	obj.TimeOut 			= c.getString(9);
		    	obj.ServerUrl 			= c.getString(10);
		    	obj.MaxTransactions 	= c.getString(11);
		    	obj.MaxAmount 			= c.getString(12);
		    	obj.Code 				= c.getString(13);
		    	obj.UserName 			= c.getString(14);
		    	obj.LastTransactionID 	= c.getString(15);
		    	obj.PartialPayment 		= c.getString(16);
		    	obj.AdvPayment 			= c.getString(17);
		    	obj.AdvDemandDwds 		= c.getString(18);
		    	obj.MemberAttendance 	= c.getString(19);
		    	obj.IndividualReceipts 	= c.getString(20);
		    	obj.GLI 				= c.getString(21);
		    	obj.Lateness 			= c.getString(22);
		    	obj.GroupPhoto 			= c.getString(23);
		    	obj.LastTransactionCode = c.getString(24);
		    	obj.PrintType			= c.getString(25);
		    	obj.InstRequired		= c.getString(26);
		    	obj.PhotoNos			= c.getString(27);
		    	obj.ValidatePrinter		= c.getString(28);
		    	obj.PrintValidate		= c.getString(29);
		    	obj.LogoPrinting		= c.getString(30);
		    	obj.qom					= c.getString(31);
		    	obj.probInCenter		= c.getString(32);
		    	obj.groupDiscipline		= c.getString(33);
		    	obj.collExp				= c.getString(34);
		    	obj.collExpRMEL			= c.getString(35);
		    	obj.collPlace			= c.getString(36);
		    	obj.repaymentMadeBy		= c.getString(37);
		    	obj.rmelUser			= c.getString(38);
				obj.BranchName			= c.getString(39);
				obj.UserID			= c.getString(40);
		     vecIntialParams.add(obj);
		    	
		    	
//				CREATE TABLE "Intialparameters" ("WorkMode" VARCHAR, "TerminalID" VARCHAR, "MACID" VARCHAR PRIMARY KEY  NOT NULL ,
//				"ReceiptHeader" VARCHAR, "ReceiptFooter" VARCHAR, "PrinterBTAddress" VARCHAR, "AgentCopy" VARCHAR, 
//				"DaysOffset" VARCHAR, "MeetingTime" VARCHAR, "TimeOut" VARCHAR, "ServerUrl" VARCHAR, "MaxTransactions"
//				VARCHAR, "MaxAmount" VARCHAR, "Code" VARCHAR, "UserName" VARCHAR, "LastTransactionID" VARCHAR, 
//				"PartialPayment" VARCHAR, "AdvPayment" VARCHAR, "AdvDemandDwds" VARCHAR, "MemberAttendance" VARCHAR, 
//				"IndividualReceipts" VARCHAR)
		    }
		    while(c.moveToNext());
		    c.close();
		   }
		  }
		  catch (Exception e)
		  {
		   e.printStackTrace();
		  }
		  
		  finally{
			  if(c != null )
			  {
				  c.close();
				 ////_database.endTransaction();
			  }

		  }
		  return vecIntialParams;
	}

	public ArrayList<RepaymentDetails> SelectAllRepayment(Context context)
	{

		ArrayList<RepaymentDetails> vecIntialParams = new ArrayList<RepaymentDetails>();

		DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
		SQLiteDatabase _database = databaseHelper.getReadableDatabase();
		//_database.beginTransaction();
		Cursor c = null;
		try
		{
			c= _database.rawQuery("select * from RepaymentDetails", null);

			if(vecIntialParams != null)
				vecIntialParams.clear();

			if(c.moveToFirst())
			{
				do
				{

					RepaymentDetails obj = new RepaymentDetails();
					obj.sno 			= c.getString(0);
					obj.PaidDate 			= c.getString(1);
					obj.Principle 				= c.getString(2);
					obj.Interest 		= c.getString(3);
					obj.Total 		= c.getString(4);

					vecIntialParams.add(obj);

				}
				while(c.moveToNext());
				c.close();
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		finally{
			if(c != null )
			{
				c.close();
				//_database.endTransaction();
			}

		}
		return vecIntialParams;
	}
	public void TruncatetabelProduct(Context context) {
		DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
		SQLiteDatabase _database = databaseHelper.getWritableDatabase();
		//_database.beginTransaction();
		String query = null;
		query = "DELETE FROM LoanProduct  ";
		Log.e("query", query);
		try
		{
			SQLiteStatement sqLiteStatement = _database.compileStatement(query);
			sqLiteStatement.executeInsert();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally {
			//_database.setTransactionSuccessful();
			//_database.endTransaction();
			//_database.close();
		}

	}

	public void Loanproducttablesave(MasterDataDo masterDataModel,Context context)
	{
		List<LoanProduct> center=masterDataModel.getLoanProduct();
		for(int l=0;l<center.size();l++)
		{
			LoanProduct centerv=center.get(l);
			Log.v("","centervname"+centerv.name);

			ContentValues values3 = new ContentValues();
			values3.put("id", centerv.id);
			values3.put("name", centerv.name);

			DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
			SQLiteDatabase _database = databaseHelper.getWritableDatabase();
			//_database.beginTransaction();

			try {

				_database.insert("LoanProduct", null, values3);
				//_database.setTransactionSuccessful();
			} catch (Exception e) {
			}
			finally{
				//_database.endTransaction();

			}
		}
	}

	public synchronized ArrayList<BaseDO> getLoanProduct(Context context ) {

		String query = "SELECT  * from LoanProduct ";

		Log.v("", "getLoanProduct"+query);
		DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
		SQLiteDatabase _database = databaseHelper.getWritableDatabase();
		//_database.beginTransaction();


		Cursor c = null;
		ArrayList<BaseDO> alArrayList = new ArrayList<BaseDO>();
		try {
			c = _database.rawQuery(query, null);
			if (c.moveToFirst()) {
				do {
					BaseDO baseDO = new BaseDO();
					baseDO.id = c.getString(0);
					baseDO.name = c.getString(1);


					alArrayList.add(baseDO);


				} while (c.moveToNext());
				c.close();
			}
		} catch (Exception exception) {

		}
		finally{
			if(c != null )
			{
				c.close();
				//_database.endTransaction();
			}

		}
		return alArrayList;
	}



	public void deleteBorrowersearch(Context context) {

		DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
		SQLiteDatabase _database = databaseHelper.getWritableDatabase();
		//_database.beginTransaction();
		String query = null;

		query = "DELETE FROM RepaymentDetails  ";
		Log.e("query", query);
		try
		{
			SQLiteStatement sqLiteStatement = _database.compileStatement(query);
			sqLiteStatement.executeInsert();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally {
			//_database.setTransactionSuccessful();
			//_database.endTransaction();
			//_database.close();
		}
//		if (objSqliteDB != null) {
//			objSqliteDB.close();
//		}

	}


	public void Repaymentsave(MasterDataDo masterDataModel,Context context )
	{
		List<RepaymentDetails> center=masterDataModel.getRepaymentDetails();
		for(int l=0;l<center.size();l++)
		{
			RepaymentDetails centerv=center.get(l);

			ContentValues values3 = new ContentValues();
			values3.put("sno", centerv.sno);
			values3.put("PaidDate", centerv.PaidDate);
			values3.put("Principle", centerv.Principle);
			values3.put("Interest", centerv.Interest);
			values3.put("Total", centerv.Total);

			DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
			SQLiteDatabase _database = databaseHelper.getWritableDatabase();
			//_database.beginTransaction();

			try {

				_database.insert("RepaymentDetails", null, values3);
				//_database.setTransactionSuccessful();
			} catch (Exception e) {
			}
			finally{
				//_database.endTransaction();

			}
		}
	}
}
