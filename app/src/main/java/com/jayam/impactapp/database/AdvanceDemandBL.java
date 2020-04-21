package com.jayam.impactapp.database;

import java.util.ArrayList;

import com.jayam.impactapp.objects.AdvaceDemandDO;
import com.jayam.impactapp.objects.BaseDO;
import com.jayam.impactapp.objects.RegularDemandsDO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

public class AdvanceDemandBL {

    private ArrayList<AdvaceDemandDO> alloddemands;

    public void InsertArrayList(ArrayList<AdvaceDemandDO> alregulardemnads, Context context) {
	this.alloddemands = alregulardemnads;

		DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
		SQLiteDatabase _database = databaseHelper.getWritableDatabase();
		//_database.beginTransaction();

	for (int i = 0; i < alloddemands.size(); i++) {
	    AdvaceDemandDO advdmdns = alloddemands.get(i);

		ContentValues values = new ContentValues();
		values.put("CenterName", advdmdns.CenterName);
		values.put("MCI_Code", advdmdns.MCI_Code);
		values.put("MGI_Name", advdmdns.MGI_Name);
		values.put("MGI_Code", advdmdns.MGI_Code);
		values.put("MMI_Name", advdmdns.MMI_Name);
		values.put("MMI_Code", advdmdns.MMI_Code);
		values.put("MLAI_ID", advdmdns.MLAI_ID);
		values.put("OS", advdmdns.OS);
		values.put("OSAmt", advdmdns.OSAmt);
		values.put("DemandDate", advdmdns.DemandDate);
		values.put("SO", advdmdns.SO);
		values.put("RNo", advdmdns.RNo);
		values.put("CenterCnt", advdmdns.CenterCnt);
		values.put("OSInt", advdmdns.OSInt);


		values.put("AAdharNO", advdmdns.AAdharNo);
		values.put("MobileNo", advdmdns.MobileNo);
		values.put("BranchPaymode", advdmdns.BranchPaymode);
		values.put("ProductId", advdmdns.ProductName);
		values.put("LatitudeCenter", advdmdns.LatitudeCenter);
		values.put("LongitudeCenter", advdmdns.LongitudeCenter);
		values.put("LatitudeGroup", advdmdns.LatitudeGroup);
		values.put("LongitudeGroup", advdmdns.LongitudeGroup);
		values.put("LatitudeMember", advdmdns.LatitudeMember);
		values.put("LongitudeMember", advdmdns.LongitudeMember);

		try {

			_database.insert("AdvanceDemands", null, values);
			//_database.setTransactionSuccessful();
		} catch (Exception e) {
		}
		finally{
			//_database.endTransaction();

		}
	}
    }


    public boolean Update(BaseDO object) {
	// TODO Auto-generated method stub
	return false;
    }


    public boolean Delete(BaseDO object) {
	// TODO Auto-generated method stub
	return false;
    }


    public boolean Insert(BaseDO object) {
	// TODO Auto-generated method stub


	return false;

    }

    public String SelectCount(Context context) {
	String count = null;
	// SQLiteDatabase _database = DatabaseHelper.closedatabase();
		DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
		SQLiteDatabase _database = databaseHelper.getReadableDatabase();
		//_database.beginTransaction();
	Cursor c = null;
	try {
	    c = _database.rawQuery("select count(*) from AdvanceDemands", null);
	    if (c.moveToFirst()) {
		do {
		    try {
			count = c.getString(0);
		    } catch (Exception e) {
			Log.e("exception", "while getting from server");
		    }
		} while (c.moveToNext());
		c.close();
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	}
	finally{
		if(c != null )
		{
			c.close();
			//_database.endTransaction();
		}

	}
	return count;
    }

    public String SelectMemberCount(String MCode,Context context) {
	String count = null;
	// SQLiteDatabase _database = DatabaseHelper.closedatabase();
		DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
		SQLiteDatabase _database = databaseHelper.getReadableDatabase();
		//_database.beginTransaction();
	Cursor c = null;
	try {
	    c = _database.rawQuery("select count(*) from AdvanceTransactions where MemeberCode='" + MCode + "'", null);
	    if (c.moveToFirst()) {
		do {
		    try {
			count = c.getString(0);
		    } catch (Exception e) {
			Log.e("exception", "while getting from server");
		    }
		} while (c.moveToNext());
		c.close();
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	}
	finally{
		if(c != null )
		{
			c.close();
			//_database.endTransaction();
		}

	}
	return count;
    }






	public String SelectCASHCountADVTransaction(String MCode,Context context) {
		String count = null;
		// SQLiteDatabase _database = DatabaseHelper.closedatabase();
		DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
		SQLiteDatabase _database = databaseHelper.getReadableDatabase();
		//_database.beginTransaction();

		Cursor c = null;
		try {

			String query="select count(*) from AdvanceDemands where MMI_Code='" + MCode + "' and PaymentMode in('CashLess','Cash')";
			c = _database.rawQuery(query, null);

			Log.v("","SelectCASHCountADVTransaction"+query);
			if (c.moveToFirst()) {
				do {
					try {
						count = c.getString(0);
					} catch (Exception e) {
						Log.e("exception", "while getting from server");
					}
				} while (c.moveToNext());
				c.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			if(c != null )
			{
				c.close();
				//_database.endTransaction();
			}

		}

		return count;
	}
    public ArrayList<AdvaceDemandDO> SelectAllCenters(Context context) {
	ArrayList<AdvaceDemandDO> vecRegularDemands = new ArrayList<AdvaceDemandDO>();

		DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
		SQLiteDatabase _database = databaseHelper.getReadableDatabase();
		//_database.beginTransaction();

		Cursor c = null;
	try {
		c = _database.rawQuery("select DISTINCT CenterName,MCI_Code,LatitudeCenter,LongitudeCenter,LatitudeGroup,LongitudeGroup,LatitudeMember,LongitudeMember,DemandDate   from AdvanceDemands", null);
	    Log.e("query", "select DISTINCT CenterName,MCI_Code   from AdvanceDemands");
	    if (vecRegularDemands != null) {
		vecRegularDemands.clear();
	    }

	    if (c.moveToFirst()) {
		do {
		    AdvaceDemandDO obj = new AdvaceDemandDO();
		    obj.CenterName = c.getString(0);
		    obj.MCI_Code = c.getString(1);
			obj.LatitudeCenter = c.getString(2);
			obj.LongitudeCenter = c.getString(3);
			obj.LatitudeGroup = c.getString(4);
			obj.LongitudeGroup = c.getString(5);
			obj.LatitudeMember = c.getString(6);
			obj.LongitudeMember = c.getString(7);
			obj.DemandDate = c.getString(8);

		    // Log.e("added", "item");
		    vecRegularDemands.add(obj);
		} while (c.moveToNext());

		c.close();
	    }
	} catch (Exception e) {
	    Log.e("error", "getting from database");
	    e.printStackTrace();
	}

	finally{
		if(c != null )
		{
			c.close();
			//_database.endTransaction();
		}

	}
	return vecRegularDemands;
    }

    public ArrayList<AdvaceDemandDO> SelectAll(String param, String type,Context context) {
	ArrayList<AdvaceDemandDO> vecRegularDemands = new ArrayList<AdvaceDemandDO>();
		DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
		SQLiteDatabase _database = databaseHelper.getReadableDatabase();
		//_database.beginTransaction();
	Cursor c = null;
	try {
	    if (type.equalsIgnoreCase("CenterCode")) {
		c = _database.rawQuery("select * from AdvanceDemands where MCI_Code ='" + param + "'", null);
	    } else if ((type.equalsIgnoreCase("Groups"))) {
		c = _database.rawQuery("select * from AdvanceDemands where MGI_Code ='" + param + "'", null);
	    } else if ((type.equalsIgnoreCase("memeber"))) {
		c = _database.rawQuery("select * from AdvanceDemands where MLAI_ID ='" + param + "'", null);
	    }
	    Log.e("query", "select * from AdvanceDemands where MCI_Code ='" + param + "'");
	    if (vecRegularDemands != null) {
		vecRegularDemands.clear();
	    }

	    if (c.moveToFirst()) {
		do {
		    AdvaceDemandDO obj = new AdvaceDemandDO();
		    obj.CenterName = c.getString(0);
		    obj.MCI_Code = c.getString(1);
		    obj.MGI_Name = c.getString(2);
		    obj.MGI_Code = c.getString(3);
		    obj.MMI_Name = c.getString(4);
		    obj.MMI_Code = c.getString(5);
		    obj.MLAI_ID = c.getString(6);
		    obj.OS = c.getString(7);
		    obj.OSAmt = c.getString(8);
		    obj.DemandDate = c.getString(9);
		    obj.SO = c.getString(10);
		    obj.RNo = c.getString(11);
		    obj.CenterCnt = c.getString(12);
		    obj.MType = c.getString(13);
		    obj.OSInt = c.getString(14);
		    obj.CollectedAmt = c.getString(15);
		    obj.previousAmt = c.getString(16);
		    obj.ReceiptNumber = c.getString(17);

			obj.ReceiptNumber = c.getString(17);
			obj.ReceiptNumber = c.getString(17);

			obj.AAdharNo = c.getString(18);
			obj.MobileNo = c.getString(19);
			obj.TransactionID = c.getString(20);
			obj.Status = c.getString(21);

			obj.BranchPaymode = c.getString(24);
			obj.ProductName = c.getString(25);

			obj.LatitudeMember = c.getString(31);
			obj.LongitudeMember = c.getString(32);
		    Log.e("added", "item");
		    vecRegularDemands.add(obj);
		} while (c.moveToNext());

		c.close();
	    }
	} catch (Exception e) {
	    Log.e("error", "getting from database");
	    e.printStackTrace();
	}

	finally{
		if(c != null )
		{
			c.close();
			//_database.endTransaction();
		}

	}
	return vecRegularDemands;
    }
	public ArrayList<AdvaceDemandDO> SelectAllAdvanceDemands(Context context) {
		ArrayList<AdvaceDemandDO> vecRegularDemands = new ArrayList<AdvaceDemandDO>();
		DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
		SQLiteDatabase _database = databaseHelper.getReadableDatabase();
		//_database.beginTransaction();
		Cursor c = null;
		try {
			c = _database.rawQuery("select * from AdvanceDemands ", null);

			if (vecRegularDemands != null) {
				vecRegularDemands.clear();
			}

			if (c.moveToFirst()) {
				do {
					AdvaceDemandDO obj = new AdvaceDemandDO();
					obj.CenterName = c.getString(0);
					obj.MCI_Code = c.getString(1);
					obj.MGI_Name = c.getString(2);
					obj.MGI_Code = c.getString(3);
					obj.MMI_Name = c.getString(4);
					obj.MMI_Code = c.getString(5);
					obj.MLAI_ID = c.getString(6);
					obj.OS = c.getString(7);
					obj.OSAmt = c.getString(8);
					obj.DemandDate = c.getString(9);
					obj.SO = c.getString(10);
					obj.RNo = c.getString(11);
					obj.CenterCnt = c.getString(12);
					obj.MType = c.getString(13);
					obj.OSInt = c.getString(14);
					obj.CollectedAmt = c.getString(15);
					obj.previousAmt = c.getString(16);
					obj.ReceiptNumber = c.getString(17);
					obj.TransactionID = c.getString(20);
					obj.Status = c.getString(21);
					obj.PaymentMode = c.getString(22);
					Log.e("added", "item");
					vecRegularDemands.add(obj);
				} while (c.moveToNext());

				c.close();
			}
		} catch (Exception e) {
			Log.e("error", "getting from database");
			e.printStackTrace();
		}
		finally{
			if(c != null )
			{
				c.close();
				//_database.endTransaction();
			}

		}
		//_database.close();
		return vecRegularDemands;
	}
    public ArrayList<AdvaceDemandDO> SelectAllCollectedData(String param, String type,Context context) {
	ArrayList<AdvaceDemandDO> vecRegularDemands = new ArrayList<AdvaceDemandDO>();
		DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
		SQLiteDatabase _database = databaseHelper.getReadableDatabase();
		//_database.beginTransaction();
	Cursor c = null;
	try {
	    if (type.equalsIgnoreCase("CenterCode")) {
		c = _database.rawQuery(
			"select * from AdvanceDemands where MCI_Code ='" + param + "' and CollectedAmt>0", null);
	    } else if ((type.equalsIgnoreCase("Groups"))) {
		c = _database.rawQuery(
			"select * from AdvanceDemands where MGI_Code ='" + param + "' and CollectedAmt>0", null);
	    } else if ((type.equalsIgnoreCase("memeber"))) {
		c = _database.rawQuery("select * from AdvanceDemands where MLAI_ID ='" + param + "' and CollectedAmt>0",
			null);
	    }
	    Log.e("query", "select * from AdvanceDemands where MCI_Code ='" + param + "'");
	    if (vecRegularDemands != null) {
		vecRegularDemands.clear();
	    }

	    if (c.moveToFirst()) {
		do {
		    AdvaceDemandDO obj = new AdvaceDemandDO();
		    obj.CenterName = c.getString(0);
		    obj.MCI_Code = c.getString(1);
		    obj.MGI_Name = c.getString(2);
		    obj.MGI_Code = c.getString(3);
		    obj.MMI_Name = c.getString(4);
		    obj.MMI_Code = c.getString(5);
		    obj.MLAI_ID = c.getString(6);
		    obj.OS = c.getString(7);
		    obj.OSAmt = c.getString(8);
		    obj.DemandDate = c.getString(9);
		    obj.SO = c.getString(10);
		    obj.RNo = c.getString(11);
		    obj.CenterCnt = c.getString(12);
		    obj.MType = c.getString(13);
		    obj.OSInt = c.getString(14);
		    obj.CollectedAmt = c.getString(15);
		    obj.previousAmt = c.getString(16);
		    obj.ReceiptNumber = c.getString(17);
			obj.TransactionID = c.getString(20);
			obj.Status = c.getString(21);
			obj.PaymentMode = c.getString(22);

			obj.BankName = c.getString(33);
			obj.TransactionTime = c.getString(34);
			obj.datetime = c.getString(35);
		    Log.e("added", "item");
		    vecRegularDemands.add(obj);
		} while (c.moveToNext());

		c.close();
	    }
	} catch (Exception e) {
	    Log.e("error", "getting from database");
	    e.printStackTrace();
	}

	finally{
		if(c != null )
		{
			c.close();
			//_database.endTransaction();
		}

	}
	return vecRegularDemands;
    }

    public ArrayList<AdvaceDemandDO> SelectGroups(String param,Context context) {
	ArrayList<AdvaceDemandDO> vecRegularDemands = new ArrayList<AdvaceDemandDO>();
		DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
		SQLiteDatabase _database = databaseHelper.getReadableDatabase();
		//_database.beginTransaction();
	Cursor c = null;
	try {
		c = _database.rawQuery(
				"select DISTINCT MGI_Name,MGI_Code ,LatitudeCenter,LongitudeCenter,LatitudeGroup,LongitudeGroup,LatitudeMember,LongitudeMember,DemandDate from AdvanceDemands where MCI_Code = '" + param + "'", null);
	    Log.e("query", "select DISTINCT MGI_Name,MGI_Code from AdvanceDemands where MCI_Code = " + param + "'");
	    if (vecRegularDemands != null) {
		vecRegularDemands.clear();
	    }

	    if (c.moveToFirst()) {
		do {
		    AdvaceDemandDO obj = new AdvaceDemandDO();
		    obj.MGI_Name = c.getString(0);
		    obj.MGI_Code = c.getString(1);
			obj.LatitudeCenter = c.getString(2);
			obj.LongitudeCenter = c.getString(3);
			obj.LatitudeGroup = c.getString(4);
			obj.LongitudeGroup = c.getString(5);
			obj.LatitudeMember = c.getString(6);
			obj.LongitudeMember = c.getString(7);
			obj.DemandDate = c.getString(8);

		    Log.e("added", "item");
		    vecRegularDemands.add(obj);
		} while (c.moveToNext());

		c.close();
	    }
	} catch (Exception e) {
	    Log.e("error", "getting from database");
	    e.printStackTrace();
	}

	finally{
		if(c != null )
		{
			c.close();
			//_database.endTransaction();
		}

	}
	return vecRegularDemands;
    }

    public ArrayList<AdvaceDemandDO> SelectMembers(String param,Context context) {
	ArrayList<AdvaceDemandDO> vecRegularDemands = new ArrayList<AdvaceDemandDO>();
		DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
		SQLiteDatabase _database = databaseHelper.getReadableDatabase();
		//_database.beginTransaction();
	Cursor c = null;
	try {
	    c = _database.rawQuery("select DISTINCT MMI_Name,MMI_Code from AdvanceDemands where MGI_Code = '" + param
		    + "' order by SO", null);
	    Log.e("query", "select DISTINCT MMI_Name,MMI_Code from AdvanceDemands where MGI_Code = '" + param
		    + "' order by SO");
	    if (vecRegularDemands != null) {
		vecRegularDemands.clear();
	    }

	    if (c.moveToFirst()) {
		do {
		    AdvaceDemandDO obj = new AdvaceDemandDO();
		    obj.MMI_Name = c.getString(0);
		    obj.MMI_Code = c.getString(1);

		    Log.e("added", "item");
		    vecRegularDemands.add(obj);
		} while (c.moveToNext());

		c.close();
	    }
	} catch (Exception e) {
	    Log.e("error", "getting from database");
	    e.printStackTrace();
	}

	finally{
		if(c != null )
		{
			c.close();
			//_database.endTransaction();
		}

	}
	return vecRegularDemands;
    }
//
//    public void updateSavedAmt(String MLAI_ID, String amt, String Type) {
//	SQLiteDatabase objSqliteDB = null;
//	objSqliteDB = DatabaseHelper.openDataBase();
//	String query = null;
//
//	query = "UPDATE AdvanceDemands SET CollectedAmt='" + amt + "',MType='" + Type + "' where MLAI_ID ='" + MLAI_ID
//		+ "'";
//	Log.e("query", query);
//	try {
//	    objSqliteDB.beginTransaction();
//	    SQLiteStatement sqLiteStatement = objSqliteDB.compileStatement(query);
//	    sqLiteStatement.executeInsert();
//	    objSqliteDB.setTransactionSuccessful();
//	    objSqliteDB.endTransaction();
//
//	} catch (Exception e) {
//	    e.printStackTrace();
//	}
//	if (objSqliteDB != null) {
//	    objSqliteDB.close();
//	}
//    }


	public void updateSavedAmtFING(String MLAI_ID, String amt, String Type,String Bankrrn,String response,String Paymentmode,String TXN_ID,String bankName,String TransactionTime,String EntryDate,Context context ) {
		SQLiteDatabase objSqliteDB = null;
		DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
		SQLiteDatabase _database = databaseHelper.getWritableDatabase();
		//_database.beginTransaction();
		String query = null;

		query = "UPDATE AdvanceDemands SET CollectedAmt='" + amt + "',MType='" + Type + "' ,TransactionID='" + Bankrrn + "'  ,Status='" + response + "',PaymentMode = '" + Paymentmode + "' ,TXNID = '" + TXN_ID + "',bankName = '" + bankName + "',TransactionTime = '" + TransactionTime + "',EntryDate = '" + EntryDate + "' where MLAI_ID ='" + MLAI_ID
				+ "'";
		Log.v("query","updateSavedAmtFING"+ query);
		try {
			SQLiteStatement sqLiteStatement = _database.compileStatement(query);
			sqLiteStatement.executeInsert();

		} catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			//_database.setTransactionSuccessful();
			//_database.endTransaction();
			//_database.close();
		}
	}

    public void updateCollectedAmt(String MLAI_ID, String amt,Context context) {
	SQLiteDatabase objSqliteDB = null;
		DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
		SQLiteDatabase _database = databaseHelper.getWritableDatabase();
		//_database.beginTransaction();
	String query = null;

	query = "UPDATE AdvanceDemands SET previousAmt = '" + amt + "' where MLAI_ID ='" + MLAI_ID + "'";
	Log.e("query", query);
	try {
		SQLiteStatement sqLiteStatement = _database.compileStatement(query);
		sqLiteStatement.executeInsert();

	} catch (Exception e) {
	    e.printStackTrace();
	}
	finally {
		//_database.setTransactionSuccessful();
		//_database.endTransaction();
		//_database.close();
	}
    }



    public String getTOTALCollectedAmount(String para, String type,Context context) {
	String totalCollAmount = "";
		DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
		SQLiteDatabase _database = databaseHelper.getWritableDatabase();
		//_database.beginTransaction();
	Cursor c = null;
	try {
	    if (type.equals("Center")) {
		c = _database.rawQuery("Select SUM(CollectedAmt) from AdvanceDemands where MCI_Code='" + para + "'",
			null);
	    } else if (type.equals("Group")) {
		c = _database.rawQuery("Select SUM(CollectedAmt) from AdvanceDemands where MGI_Code='" + para + "'",
			null);
	    }
	    if (c.moveToFirst()) {
		do {
		    try {
			totalCollAmount = c.getString(0);
		    } catch (Exception e) {
			Log.e("exception", "while getting from server");
		    }

		} while (c.moveToNext());
		c.close();
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	}
	finally{
		if(c != null )
		{
			c.close();
			//_database.endTransaction();
		}

	}
	return totalCollAmount;

    }

    public String getTOTALDemandAmountForAdv(Context context) {
	String totalODAmount = null;
		DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
		SQLiteDatabase _database = databaseHelper.getWritableDatabase();
		//_database.beginTransaction();
	Cursor c = null;
	try {
	    c = _database.rawQuery("select  sum(CollectionAmt)  from AdvanceTransactions", null);

	    if (c.moveToFirst()) {
		do {
		    try {
			totalODAmount = c.getString(0);
		    } catch (Exception e) {
			Log.e("exception", "while getting from server");
		    }
		    // CREATE TABLE "RegularDemands" ("CNo" VARCHAR PRIMARY KEY
		    // NOT NULL , "CName" VARCHAR, "GNo" VARCHAR,
		    // "EII_EMP_ID" VARCHAR, "GroupName" VARCHAR, "MemberCode"
		    // VARCHAR, "MemberName" VARCHAR, "DemandDate" VARCHAR,
		    // "MLAI_ID" VARCHAR, "OSAmt" VARCHAR, "DemandTotal"
		    // VARCHAR, "ODAmount" VARCHAR, "Attendance" VARCHAR)

		} while (c.moveToNext());
		c.close();
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	}
	finally{
		if(c != null )
		{
			c.close();
			//_database.endTransaction();
		}

	}
	return totalODAmount;

	// select SUM(DemandTotal+ODAmount) from RegularDemands
    }

	public synchronized String getTOTALDemandAmountForAdvGroupid(String groupno,Context context) {
		String totalODAmount = null;
		DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
		SQLiteDatabase _database = databaseHelper.getWritableDatabase();
		//_database.beginTransaction();
		Cursor c = null;
		try {
			//String query="select  sum(CollectionAmt)  from AdvanceTransactions where GroupCode='"+groupno+"'";
			String query="select  sum(CollectionAmt)  from AdvanceTransactions where LoanNo='"+groupno+"'";

			c = _database.rawQuery(query, null);

			if (c.moveToFirst()) {
				do {
					try {
						totalODAmount = c.getString(0);
					} catch (Exception e) {
						Log.e("exception", "while getting from server");
					}
					// CREATE TABLE "RegularDemands" ("CNo" VARCHAR PRIMARY KEY
					// NOT NULL , "CName" VARCHAR, "GNo" VARCHAR,
					// "EII_EMP_ID" VARCHAR, "GroupName" VARCHAR, "MemberCode"
					// VARCHAR, "MemberName" VARCHAR, "DemandDate" VARCHAR,
					// "MLAI_ID" VARCHAR, "OSAmt" VARCHAR, "DemandTotal"
					// VARCHAR, "ODAmount" VARCHAR, "Attendance" VARCHAR)

				} while (c.moveToNext());
				c.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			if(c != null )
			{
				c.close();
				//_database.endTransaction();
			}

		}
		return totalODAmount;

		// select SUM(DemandTotal+ODAmount) from RegularDemands
	}

    public synchronized  String getTOTALAdvAccounts(Context context) {
	String totalODAmount = null;
		DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
		SQLiteDatabase _database = databaseHelper.getWritableDatabase();
		//_database.beginTransaction();
	Cursor c = null;
	try {
		String query="select  Count(*)  from AdvanceTransactions";
	    c = _database.rawQuery(query, null);

	    if (c.moveToFirst()) {
		do {
		    try {
			totalODAmount = c.getString(0);
		    } catch (Exception e) {
			Log.e("exception", "while getting from server");
		    }
		    // CREATE TABLE "RegularDemands" ("CNo" VARCHAR PRIMARY KEY
		    // NOT NULL , "CName" VARCHAR, "GNo" VARCHAR,
		    // "EII_EMP_ID" VARCHAR, "GroupName" VARCHAR, "MemberCode"
		    // VARCHAR, "MemberName" VARCHAR, "DemandDate" VARCHAR,
		    // "MLAI_ID" VARCHAR, "OSAmt" VARCHAR, "DemandTotal"
		    // VARCHAR, "ODAmount" VARCHAR, "Attendance" VARCHAR)

		} while (c.moveToNext());
		c.close();
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	}
	finally{
		if(c != null )
		{
			c.close();
			//_database.endTransaction();
		}

	}
	return totalODAmount;

	// select SUM(DemandTotal+ODAmount) from RegularDemands
    }






    public ArrayList<AdvaceDemandDO> SelectAllData(String param, String type,Context context) {
	ArrayList<AdvaceDemandDO> vecRegularDemands = new ArrayList<AdvaceDemandDO>();
		DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
		SQLiteDatabase _database = databaseHelper.getReadableDatabase();
		//_database.beginTransaction();

		Cursor c = null;
	try {
	    if (type.equalsIgnoreCase("CNo")) {
		c = _database.rawQuery(
			"select * from AdvanceDemands where MCI_Code ='" + param + "' and CollectedAmt>0", null);
	    } else if ((type.equalsIgnoreCase("Groups"))) {
		c = _database.rawQuery(
			"select * from  AdvanceDemands where MGI_Code ='" + param + "' and CollectedAmt>0", null);
	    }

	    Log.e("query", "select * from AdvanceDemands where MCI_Code ='" + param + "' and CollectedAmt>0");
	    if (vecRegularDemands != null) {
		vecRegularDemands.clear();
	    }

	    if (c.moveToFirst()) {
		do {
		    AdvaceDemandDO obj = new AdvaceDemandDO();
		    obj.CenterName = c.getString(0);
		    obj.MCI_Code = c.getString(1);
		    obj.MGI_Name = c.getString(2);
		    obj.MGI_Code = c.getString(3);
		    obj.MMI_Name = c.getString(4);
		    obj.MMI_Code = c.getString(5);
		    obj.MLAI_ID = c.getString(6);
		    obj.OS = c.getString(7);
		    obj.OSAmt = c.getString(8);
		    obj.DemandDate = c.getString(9);
		    obj.SO = c.getString(10);
		    obj.RNo = c.getString(11);
		    obj.CenterCnt = c.getString(12);
		    obj.MType = c.getString(13);
		    obj.OSInt = c.getString(14);
		    obj.CollectedAmt = c.getString(15);
		    obj.previousAmt = c.getString(16);
		    obj.ReceiptNumber = c.getString(17);


			obj.TransactionID = c.getString(20);
			obj.Status = c.getString(21);

			obj.BankName = c.getString(33);
			obj.TransactionTime = c.getString(34);

		    Log.e("added", "item");
		    vecRegularDemands.add(obj);
		} while (c.moveToNext());

		c.close();
	    }
	} catch (Exception e) {
	    Log.e("error", "getting from database");
	    e.printStackTrace();
	}

	finally{
		if(c != null )
		{
			c.close();
			//_database.endTransaction();
		}

	}
	return vecRegularDemands;
    }

    public boolean Insert(BaseDO object, String RecieptNo, String TransactionCode,Context context) {
	AdvaceDemandDO parametrsDO = (AdvaceDemandDO) object;



	ContentValues values = new ContentValues();
	values.put("CenterName", parametrsDO.CenterName);
	values.put("CenterCode", parametrsDO.MCI_Code);
	values.put("GrouprName", parametrsDO.MGI_Name);
	values.put("GroupCode", parametrsDO.MGI_Code);
	values.put("MemeberCode", parametrsDO.MMI_Code);
	values.put("MemberName", parametrsDO.MMI_Name);
	values.put("Date", parametrsDO.DemandDate);
	values.put("LoanNo", parametrsDO.MLAI_ID);
	values.put("CollectionAmt", parametrsDO.CollectedAmt);
	values.put("Receipt", RecieptNo);
	values.put("Flag", parametrsDO.MType);
	values.put("TransactionCode", TransactionCode);
	values.put("DupNo", "0");
	values.put("CollType", "0");
	values.put("PrintFlag", "0");
	values.put("APFlag", "N");
		values.put("PaymentMode", parametrsDO.PaymentMode);

		values.put("TransactionID", parametrsDO.TransactionID);
		values.put("Status", parametrsDO.Status);

		values.put("bankName", parametrsDO.BankName);
		values.put("TransactionTime", parametrsDO.TransactionTime);
		values.put("EntryDate", parametrsDO.datetime);

		DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
		SQLiteDatabase _database = databaseHelper.getWritableDatabase();
		//_database.beginTransaction();
	try {

	    _database.insert("AdvanceTransactions", null, values);
		//_database.setTransactionSuccessful();
	} catch (Exception e) {
	    Log.e("Eror", "While Inserting Into AdvanceTransactions table");
	}
	finally {
		//_database.endTransaction();
	}

		return true;
    }

    public void updateReciptNumbers(String MALID, String recpNumber,Context context) {
		DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
		SQLiteDatabase _database = databaseHelper.getWritableDatabase();
		//_database.beginTransaction();
	String query = null;

	query = "UPDATE AdvanceDemands SET ReceiptNumber='" + recpNumber + "' where MLAI_ID = '" + MALID + "'";
	Log.e("query", query);
	try {
		SQLiteStatement sqLiteStatement = _database.compileStatement(query);
		sqLiteStatement.executeInsert();

	} catch (Exception e) {
	    e.printStackTrace();
	}
	finally {
		//_database.setTransactionSuccessful();
		//_database.endTransaction();
		//_database.close();
	}
    }





    public ArrayList<AdvaceDemandDO> SelectReportsData(String ID, String Type,Context context) {
	ArrayList<AdvaceDemandDO> vecRegularDemands = new ArrayList<AdvaceDemandDO>();
		DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
		SQLiteDatabase _database = databaseHelper.getReadableDatabase();
		//_database.beginTransaction();
	Cursor c = null;
	try {
	    if (Type.equalsIgnoreCase("Center")) {
		c = _database.rawQuery("select * from AdvanceDemands where MCI_Code = '" + ID + "' and previousAmt>0",
			null);
	    } else if ((Type.equalsIgnoreCase("Group"))) {
		c = _database.rawQuery("select * from AdvanceDemands where MGI_Code = '" + ID + "' and previousAmt>0",
			null);
	    } else if ((Type.equalsIgnoreCase("Memeber"))) {
		c = _database.rawQuery("select * from AdvanceDemands where MLAI_ID = '" + ID + "' and previousAmt>0",
			null);
	    } else if ((Type.equalsIgnoreCase("Summary"))) {
		c = _database.rawQuery("select * from AdvanceDemands", null);
	    }
	    Log.e("Query:--", "select * from AdvanceDemands where MLAI_ID = '" + ID + "' and previousAmt>0");

	    if (vecRegularDemands != null) {
		vecRegularDemands.clear();
	    }

	    if (c.moveToFirst()) {
		do {
		    AdvaceDemandDO obj = new AdvaceDemandDO();
		    try {
			obj.CenterName = c.getString(0);
			obj.MCI_Code = c.getString(1);
			obj.MGI_Name = c.getString(2);
			obj.MGI_Code = c.getString(3);
			obj.MMI_Name = c.getString(4);
			obj.MMI_Code = c.getString(5);
			obj.MLAI_ID = c.getString(6);
			obj.OS = c.getString(7);
			obj.OSAmt = c.getString(8);
			obj.DemandDate = c.getString(9);
			obj.SO = c.getString(10);
			obj.RNo = c.getString(11);
			obj.CenterCnt = c.getString(12);
			obj.MType = c.getString(13);
			obj.OSInt = c.getString(14);
			obj.CollectedAmt = c.getString(15);
			obj.previousAmt = c.getString(16);
			obj.ReceiptNumber = c.getString(17);
				obj.TransactionID = c.getString(20);
				obj.Status = c.getString(21);
		    } catch (Exception e) {
			Log.e("exception", "while getting from server");
		    }

		    vecRegularDemands.add(obj);
		} while (c.moveToNext());
		c.close();
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	}
	finally {
		//_database.endTransaction();
	}

		return vecRegularDemands;
    }

    public ArrayList<AdvaceDemandDO> Selectgroups(String CNO,Context context) {
	ArrayList<AdvaceDemandDO> vecRegularDemands = new ArrayList<AdvaceDemandDO>();

		DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
		SQLiteDatabase _database = databaseHelper.getReadableDatabase();
		//_database.beginTransaction();
	Cursor c = null;
	try {
	    c = _database.rawQuery(
		    "Select MGI_Code,MGI_Name,sum(previousAmt) from AdvanceDemands where previousAmt>0 and MCI_Code='"
			    + CNO + "' group by MGI_Code",
		    null);
	    Log.e("query",
		    "Select MGI_Code,MGI_Name,sum(previousAmt) from AdvanceDemands where previousAmt>0 and MCI_Code="
			    + CNO + " group by MGI_Code");
	    if (vecRegularDemands != null) {
		vecRegularDemands.clear();
	    }

	    if (c.moveToFirst()) {
		do {
		    AdvaceDemandDO obj = new AdvaceDemandDO();
		    obj.MGI_Code = c.getString(0);
		    obj.MGI_Name = c.getString(1);
		    obj.previousAmt = c.getString(2);

		    Log.e("added", "item");
		    vecRegularDemands.add(obj);
		} while (c.moveToNext());

		c.close();
	    }
	} catch (Exception e) {
	    Log.e("error", "getting from database");
	    e.printStackTrace();
	}

	finally{
		if(c != null )
		{
			c.close();
			//_database.endTransaction();
		}

	}
	return vecRegularDemands;
    }

    public ArrayList<AdvaceDemandDO> SelectAllMembers_CollectedAnt(String code,Context context) {
	ArrayList<AdvaceDemandDO> vecRegularDemands = new ArrayList<AdvaceDemandDO>();
		DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
		SQLiteDatabase _database = databaseHelper.getReadableDatabase();
		//_database.beginTransaction();
	Cursor c = null;
	try {

	    c = _database.rawQuery(
		    "Select MMI_Code,MMI_Name,sum(previousAmt),MLAI_ID from AdvanceDemands where previousAmt>0 and MGI_Code='"
			    + code + "' group by MMI_Code",
		    null);

	    if (vecRegularDemands != null) {
		vecRegularDemands.clear();
	    }

	    if (c.moveToFirst()) {
		do {
		    AdvaceDemandDO obj = new AdvaceDemandDO();
		    try {
			obj.MMI_Code = c.getString(0);
			obj.MMI_Name = c.getString(1);
			obj.previousAmt = c.getString(2);
			obj.MLAI_ID = c.getString(3);
		    } catch (Exception e) {
			Log.e("exception", "while getting from server");
		    }

		    vecRegularDemands.add(obj);
		} while (c.moveToNext());
		c.close();
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	}
	finally{
		if(c != null )
		{
			c.close();
			//_database.endTransaction();
		}

	}
	return vecRegularDemands;
    }

    public String SelectCountcenters(Context context) {
	String count = null;
	// SQLiteDatabase _database = DatabaseHelper.closedatabase();
		DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
		SQLiteDatabase _database = databaseHelper.getReadableDatabase();
		//_database.beginTransaction();
	Cursor c = null;
	try {
	    c = _database.rawQuery("SELECT Count(*)  Count from AdvanceDemands where previousAmt>0", null);
	    if (c.moveToFirst()) {
		do {
		    try {
			count = c.getString(0);
		    } catch (Exception e) {
			Log.e("exception", "while getting from server");
		    }
		} while (c.moveToNext());
		c.close();
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	}
	finally{
		if(c != null )
		{
			c.close();
			//_database.endTransaction();
		}

	}
	return count;
    }

    public ArrayList<AdvaceDemandDO> SelectReports_Centers(Context context) {
	ArrayList<AdvaceDemandDO> vecRegularDemands = new ArrayList<AdvaceDemandDO>();
		DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
		SQLiteDatabase _database = databaseHelper.getReadableDatabase();
		//_database.beginTransaction();
	Cursor c = null;
	try {
	    c = _database.rawQuery(
		    "Select MCI_Code,Centername,sum(previousAmt) from AdvanceDemands where previousAmt>0 group by MCI_Code",
		    null);
	    Log.e("query",
		    "Select MCI_Code,Centername,sum(previousAmt) from AdvanceDemands where previousAmt>0 group by MCI_Code");
	    if (vecRegularDemands != null) {
		vecRegularDemands.clear();
	    }

	    if (c.moveToFirst()) {
		do {
		    AdvaceDemandDO obj = new AdvaceDemandDO();
		    obj.MCI_Code = c.getString(0);
		    obj.CenterName = c.getString(1);
		    obj.previousAmt = c.getString(2);

		    // Log.e("added", "item");
		    vecRegularDemands.add(obj);
		} while (c.moveToNext());

		c.close();
	    }
	} catch (Exception e) {
	    Log.e("error", "getting from database");
	    e.printStackTrace();
	}

	finally{
		if(c != null )
		{
			c.close();
			//_database.endTransaction();
		}

	}
	return vecRegularDemands;
    }



    public String getrowCount(Context context) {
	String totalODAmount = null;
		DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
		SQLiteDatabase _database = databaseHelper.getReadableDatabase();
		//_database.beginTransaction();
	Cursor c = null;
	try {
	    c = _database.rawQuery("select count (*)  from AdvanceTransactions", null);

	    if (c.moveToFirst()) {
		do {
		    try {
			totalODAmount = c.getString(0);
		    } catch (Exception e) {
			Log.e("exception", "while getting from server");
		    }

		} while (c.moveToNext());
		c.close();
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	}
	finally{
		if(c != null )
		{
			c.close();
			//_database.endTransaction();
		}
	}
	return totalODAmount;

    }

    public String CollectionRecords(Context context) {
	String count = null;
	// SQLiteDatabase _database = DatabaseHelper.closedatabase();

		DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
		SQLiteDatabase _database = databaseHelper.getReadableDatabase();
		//_database.beginTransaction();
	Cursor c = null;
	try {
	    c = _database.rawQuery("select count(*) from AdvanceTransactions", null);
	    if (c.moveToFirst()) {
		do {
		    try {
			count = c.getString(0);
		    } catch (Exception e) {
			Log.e("exception", "while getting from server");
		    }
		} while (c.moveToNext());
		c.close();
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	}
	finally{
		if(c != null )
		{
			c.close();
			//_database.endTransaction();
		}
	}
	return count;
    }


    public ArrayList<AdvaceDemandDO> SelectAll(Context context) {
	ArrayList<AdvaceDemandDO> vecRegularDemands = new ArrayList<AdvaceDemandDO>();
		DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
		SQLiteDatabase _database = databaseHelper.getReadableDatabase();
		//_database.beginTransaction();
	Cursor c = null;
	try {

	    c = _database.rawQuery("select * from AdvanceTransactions", null);
	    Log.e("Query:--", "select * from AdvanceTransactions");

	    if (vecRegularDemands != null) {
		vecRegularDemands.clear();
	    }

	    if (c.moveToFirst()) {
		do {
		    AdvaceDemandDO obj = new AdvaceDemandDO();
		    try {
			obj.CenterName = c.getString(0);
			obj.MCI_Code = c.getString(1);
			obj.MGI_Name = c.getString(2);
			obj.MGI_Code = c.getString(3);
			obj.MMI_Code = c.getString(4);
			obj.MMI_Name = c.getString(5);
			obj.DemandDate = c.getString(6);
			obj.MLAI_ID = c.getString(7);
			obj.previousAmt = c.getString(8);
			obj.ReceiptNumber = c.getString(9);
			obj.MType = c.getString(10);
			obj.CenterCnt = c.getString(11);
			obj.printFlag = c.getString(14);
			obj.APFlag = c.getString(15);

				obj.TransactionID = c.getString(18);
				obj.Status = c.getString(19);
				obj.PaymentMode = c.getString(20);

		    } catch (Exception e) {
			Log.e("exception", "while getting from server");
		    }

		    vecRegularDemands.add(obj);
		} while (c.moveToNext());
		c.close();
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	}
	finally{
		if(c != null )
		{
			c.close();
			//_database.endTransaction();
		}
	}
	return vecRegularDemands;
    }

    public ArrayList<AdvaceDemandDO> SelectAllTransactions(String TxnCode,Context context) {
	ArrayList<AdvaceDemandDO> vecRegularDemands = new ArrayList<AdvaceDemandDO>();
		DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
		SQLiteDatabase _database = databaseHelper.getReadableDatabase();
		//_database.beginTransaction();

		Cursor c = null;
	try {
	    c = _database.rawQuery("select * from AdvanceTransactions where TransactionCode='" + TxnCode + "'", null);
	    Log.e("Query:--", "select * from AdvanceTransactions where TransactionCode='" + TxnCode + "'");
	    if (vecRegularDemands != null) {
		vecRegularDemands.clear();
	    }
	    if (c.moveToFirst()) {
		do {
		    AdvaceDemandDO obj = new AdvaceDemandDO();
		    try {
			obj.CenterName = c.getString(0);
			obj.MCI_Code = c.getString(1);
			obj.MGI_Name = c.getString(2);
			obj.MGI_Code = c.getString(3);
			obj.MMI_Code = c.getString(4);
			obj.MMI_Name = c.getString(5);
			obj.DemandDate = c.getString(6);
			obj.MLAI_ID = c.getString(7);
			obj.previousAmt = c.getString(8);
			obj.ReceiptNumber = c.getString(9);
			obj.MType = c.getString(10);
			obj.CenterCnt = c.getString(11);
		    } catch (Exception e) {
			Log.e("exception", "while getting from server");
		    }

		    vecRegularDemands.add(obj);
		} while (c.moveToNext());
		c.close();
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	}
	finally{
		if(c != null )
		{
			c.close();
			//_database.endTransaction();
		}
	}

		return vecRegularDemands;
    }

    public ArrayList<AdvaceDemandDO> SelectAllGroupTransactions(String TxnCode, String GroupCode,Context context) {
	ArrayList<AdvaceDemandDO> vecRegularDemands = new ArrayList<AdvaceDemandDO>();

		DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
		SQLiteDatabase _database = databaseHelper.getReadableDatabase();
		//_database.beginTransaction();

		Cursor c = null;
	try {

	    c = _database.rawQuery("select * from AdvanceTransactions where TransactionCode='" + TxnCode
		    + "' and GroupCode='" + GroupCode + "'", null);
	    Log.e("Query:--", "select * from AdvanceTransactions where TransactionCode='" + TxnCode
		    + "' and GroupCode='" + GroupCode + "'");

	    if (vecRegularDemands != null) {
		vecRegularDemands.clear();
	    }

	    if (c.moveToFirst()) {
		do {
		    AdvaceDemandDO obj = new AdvaceDemandDO();
		    try {
			obj.CenterName = c.getString(0);
			obj.MCI_Code = c.getString(1);
			obj.MGI_Name = c.getString(2);
			obj.MGI_Code = c.getString(3);
			obj.MMI_Code = c.getString(4);
			obj.MMI_Name = c.getString(5);
			obj.DemandDate = c.getString(6);
			obj.MLAI_ID = c.getString(7);
			obj.previousAmt = c.getString(8);
			obj.ReceiptNumber = c.getString(9);
			obj.MType = c.getString(10);
			obj.CenterCnt = c.getString(11);

		    } catch (Exception e) {
			Log.e("exception", "while getting from server");
		    }

		    vecRegularDemands.add(obj);
		} while (c.moveToNext());
		c.close();
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	}
	finally{
		if(c != null )
		{
			c.close();
			//_database.endTransaction();
		}
	}
	return vecRegularDemands;
    }

    public void Truncatetabel(Context context) {
	SQLiteDatabase objSqliteDB = null;
		DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
		SQLiteDatabase _database = databaseHelper.getWritableDatabase();
		//_database.beginTransaction();
	String query = null;

	query = "DELETE FROM AdvanceDemands ";
	Log.e("query", query);
	try {
		SQLiteStatement sqLiteStatement = _database.compileStatement(query);
		sqLiteStatement.executeInsert();

	} catch (Exception e) {
	    e.printStackTrace();
	}
	finally {
		//_database.setTransactionSuccessful();
		//_database.endTransaction();
		//_database.close();
	}

    }

    public String updatePrintFlag(String TxnCode, String status,Context context) {
	String count = null;

		DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
		SQLiteDatabase _database = databaseHelper.getWritableDatabase();
		//_database.beginTransaction();
	Cursor c = null;
	try {
	    c = _database.rawQuery("UPDATE AdvanceTransactions SET PrintFlag = '" + status
		    + "' where TransactionCode = '" + TxnCode + "'", null);
	    if (c.moveToFirst()) {
		do {
		    try {
			count = c.getString(0);
		    } catch (Exception e) {
			Log.e("exception", "while getting from server");
		    }
		} while (c.moveToNext());
		c.close();
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	}
	finally{
		if(c != null )
		{
			c.close();
			//_database.endTransaction();
		}
	}
	return count;
    }



    public ArrayList<AdvaceDemandDO> SelectDistinctGroupsFromCenter(String CNO,Context context) {
	ArrayList<AdvaceDemandDO> vecRegularDemands = new ArrayList<AdvaceDemandDO>();

		DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
		SQLiteDatabase _database = databaseHelper.getReadableDatabase();
		//_database.beginTransaction();
	Cursor c = null;
	try {

	    c = _database.rawQuery("select  Distinct MGI_Code from AdvanceDemands where MCI_Code = '" + CNO + "'",
		    null);

	    if (vecRegularDemands != null) {
		vecRegularDemands.clear();
	    }

	    if (c.moveToFirst()) {
		do {
		    AdvaceDemandDO obj = new AdvaceDemandDO();
		    try {
			obj.MGI_Code = c.getString(0);
		    } catch (Exception e) {
			Log.e("exception", "while getting from server");
		    }
		    vecRegularDemands.add(obj);
		} while (c.moveToNext());
		c.close();
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	}
	finally{
		if(c != null )
		{
			c.close();
			//_database.endTransaction();
		}
	}
	return vecRegularDemands;

    }

    public ArrayList<AdvaceDemandDO> SelectDistinctTransactioncodeFromCenter(String CODE, String Type,Context context) {
	ArrayList<AdvaceDemandDO> vecRegularDemands = new ArrayList<AdvaceDemandDO>();
		DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
		SQLiteDatabase _database = databaseHelper.getReadableDatabase();
		//_database.beginTransaction();
	Cursor c = null;
	try {
	    if (Type.equals("Center")) {
		c = _database.rawQuery(
			"select  Distinct TransactionCode from AdvanceTransactions where CenterCode='" + CODE + "'",
			null);
	    } else if (Type.equals("Group")) {
		c = _database.rawQuery(
			"select  Distinct TransactionCode from AdvanceTransactions where GroupCode='" + CODE + "'",
			null);
	    } else if (Type.equals("Member")) {
		c = _database.rawQuery(
			"select  Distinct TransactionCode from AdvanceTransactions where LoanNo='" + CODE + "'", null);
	    }

	    if (vecRegularDemands != null) {
		vecRegularDemands.clear();
	    }

	    if (c.moveToFirst()) {
		do {
		    AdvaceDemandDO obj = new AdvaceDemandDO();
		    try {
			obj.TransactionCode = c.getString(0);
		    } catch (Exception e) {
			Log.e("exception", "while getting from server");
		    }
		    vecRegularDemands.add(obj);
		} while (c.moveToNext());
		c.close();
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	}
	finally{
		if(c != null )
		{
			c.close();
			//_database.endTransaction();
		}
	}
	return vecRegularDemands;

    }

    public String getDupNoforMember(String MLAIID, String Txncode,Context context) {
	String count = null;

		DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
		SQLiteDatabase _database = databaseHelper.getReadableDatabase();
		//_database.beginTransaction();
	Cursor c = null;
	try {
	    c = _database.rawQuery("SELECT DupNo  from AdvanceTransactions where LoanNo = '" + MLAIID
		    + "' and TransactionCode='" + Txncode + "'", null);
	    Log.e("Query----", "SELECT DupNo  from AdvanceTransactions where LoanNo = '" + MLAIID
		    + "' and TransactionCode='" + Txncode + "'");

	    if (c.moveToFirst()) {
		do {
		    try {
			count = c.getString(0);
		    } catch (Exception e) {
			Log.e("exception", "while getting from server");
		    }
		} while (c.moveToNext());
		c.close();
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	}
	finally{
		if(c != null )
		{
			c.close();
			//_database.endTransaction();
		}
	}
	return count;
    }

    public void updateDupNO(String DupNo, String MALID, String Txncode,Context context) {
		DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
		SQLiteDatabase _database = databaseHelper.getWritableDatabase();
		//_database.beginTransaction();
	String query = null;

	query = "UPDATE AdvanceTransactions SET DupNo='" + DupNo + "' where LoanNo = '" + MALID
		+ "' and TransactionCode='" + Txncode + "'";
	Log.e("query", query);
	try {
		SQLiteStatement sqLiteStatement = _database.compileStatement(query);
		sqLiteStatement.executeInsert();

	} catch (Exception e) {
	    e.printStackTrace();
	}
	finally {
		//_database.endTransaction();
	}

	}



    public String getTxncode(String code,Context context) {
	String totalCollAmount = null;
		DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
		SQLiteDatabase _database = databaseHelper.getReadableDatabase();
		//_database.beginTransaction();

		Cursor c = null;
	try {
	    c = _database.rawQuery("select TransactionCode AdvanceTransactions where LoanNo = '" + code + "'", null);
	    if (c.moveToFirst()) {
		do {
		    try {
			totalCollAmount = c.getString(0);
		    } catch (Exception e) {
			Log.e("exception", "while getting from server");
		    }
		} while (c.moveToNext());
		c.close();
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	}
	finally{
		if(c != null )
		{
			c.close();
			//_database.endTransaction();
		}
	}
	return totalCollAmount;
    }

    public String SelectCenterCode(String txnCode,Context context) {
	String count = null;
		DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
		SQLiteDatabase _database = databaseHelper.getReadableDatabase();
		//_database.beginTransaction();
	Cursor c = null;
	try {
	    c = _database.rawQuery(
		    "select Distinct(CenterCode) from AdvanceTransactions where TransactionCode='" + txnCode + "'",
		    null);
	    if (c.moveToFirst()) {
		do {
		    try {
			count = c.getString(0);
		    } catch (Exception e) {
			Log.e("exception", "while getting from server");
		    }
		} while (c.moveToNext());
		c.close();
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	}
	finally{
		if(c != null )
		{
			c.close();
			//_database.endTransaction();
		}
	}
	return count;
    }

    public void updateDupNoingroup(String Code, String Txncode, String Type,Context context) {
		DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
		SQLiteDatabase _database = databaseHelper.getWritableDatabase();
		//_database.beginTransaction();
	String query = null;
	if (Type.equals("Center")) {
	    query = "UPDATE AdvanceTransactions SET DupNo=DupNo+1 where CenterCode = '" + Code
		    + "' and TransactionCode='" + Txncode + "'";
	} else if (Type.equals("Group")) {
	    query = "UPDATE AdvanceTransactions SET DupNo=DupNo+1 where GroupCode = '" + Code
		    + "' and TransactionCode='" + Txncode + "'";
	}
	Log.e("query", query);
	try {
		SQLiteStatement sqLiteStatement = _database.compileStatement(query);
		sqLiteStatement.executeInsert();
	} catch (Exception e) {
	    e.printStackTrace();
	}
	finally {
		//_database.endTransaction();
	}

	}

    public String SelectMinDupNo(String txnCode, String code, String Type,Context context) {
	String count = null;
		DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
		SQLiteDatabase _database = databaseHelper.getWritableDatabase();
		//_database.beginTransaction();
	Cursor c = null;
	try {
	    if (Type.equals("Center")) {
		c = _database.rawQuery("select MIN(DupNo) from AdvanceTransactions where TransactionCode='" + txnCode
			+ "' and CenterCode='" + code + "'", null);
	    } else if (Type.equals("Group")) {
		c = _database.rawQuery("select MIN(DupNo) from AdvanceTransactions where TransactionCode='" + txnCode
			+ "' and GroupCode='" + code + "'", null);
	    }

	    if (c.moveToFirst()) {
		do {
		    try {
			count = c.getString(0);
		    } catch (Exception e) {
			Log.e("exception", "while getting from server");
		    }
		} while (c.moveToNext());
		c.close();
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	}
	finally {
		//_database.endTransaction();
	}

		return count;
    }

    public String MinDupNo(String code, String Type,Context context) {
	String count = null;
		DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
		SQLiteDatabase _database = databaseHelper.getWritableDatabase();
		//_database.beginTransaction();
	Cursor c = null;
	try {
	    if (Type.equals("Center")) {
		c = _database.rawQuery("select MIN(DupNo) from AdvanceTransactions where CenterCode='" + code + "'",
			null);
	    } else if (Type.equals("Group")) {
		c = _database.rawQuery("select MIN(DupNo) from AdvanceTransactions where GroupCode='" + code + "'",
			null);
	    }

	    if (c.moveToFirst()) {
		do {
		    try {
			count = c.getString(0);
		    } catch (Exception e) {
			Log.e("exception", "while getting from server");
		    }
		} while (c.moveToNext());
		c.close();
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	}
	finally {
		//_database.endTransaction();
	}
	return count;
    }

    public String updateActualPrintFlag(String TxnCode, String status,Context context) {
	String count = null;

		DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
		SQLiteDatabase _database = databaseHelper.getWritableDatabase();
		//_database.beginTransaction();
	Cursor c = null;
	String query = "UPDATE AdvanceTransactions SET APFlag = '" + status + "' where TransactionCode = '" + TxnCode
		+ "'";
	Log.d("mfimo", query);
	try {
	    c = _database.rawQuery(query, null);

	    if (c.moveToFirst()) {
		do {
		    try {
			count = c.getString(0);
		    } catch (Exception e) {
			Log.e("exception", "while getting from server");
		    }

		} while (c.moveToNext());
		c.close();
	    }
	} catch (Exception e) {
	    e.printStackTrace();
	}
	finally {
		//_database.endTransaction();
	}
	return count;
    }


	public synchronized ArrayList<AdvaceDemandDO> SelectAllFlagwise(Context context) {
		ArrayList<AdvaceDemandDO> vecRegularDemands = new ArrayList<AdvaceDemandDO>();
		DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
		SQLiteDatabase _database = databaseHelper.getReadableDatabase();
		//_database.beginTransaction();
		Cursor c = null;
		try {

			//String query="select * from AdvanceTransactions where GroupCode=(select min(GroupCode) from AdvanceTransactions) and (case when UploadFlag is null then '' else UploadFlag end not in ('P')  and case when UploadFlag is null then '' else UploadFlag end not in ('P'))";
			String query="select * from AdvanceTransactions where LoanNo=(select min(LoanNo) from AdvanceTransactions) and (case when UploadFlag is null then '' else UploadFlag end not in ('P')  and case when UploadFlag is null then '' else UploadFlag end not in ('P'))";
			c = _database.rawQuery(query, null);
			Log.e("Query:--", "select * from AdvanceTransactions");

			if (vecRegularDemands != null) {
				vecRegularDemands.clear();
			}

			if (c.moveToFirst()) {
				do {
					AdvaceDemandDO obj = new AdvaceDemandDO();
					try {
						obj.CenterName = c.getString(0);
						obj.MCI_Code = c.getString(1);
						obj.MGI_Name = c.getString(2);
						obj.MGI_Code = c.getString(3);
						obj.MMI_Code = c.getString(4);
						obj.MMI_Name = c.getString(5);
						obj.DemandDate = c.getString(6);
						obj.MLAI_ID = c.getString(7);
						obj.previousAmt = c.getString(8);
						obj.ReceiptNumber = c.getString(9);
						obj.MType = c.getString(10);
						obj.CenterCnt = c.getString(11);
						obj.printFlag = c.getString(14);
						obj.APFlag = c.getString(15);
						obj.MobileNo = c.getString(17);
						obj.TransactionID = c.getString(18);
						obj.Status = c.getString(19);
						obj.PaymentMode = c.getString(20);
						obj.DateTime = c.getString(23);

						obj.TransactionTime = c.getString(27);


					} catch (Exception e) {
						Log.e("exception", "while getting from server");
					}

					vecRegularDemands.add(obj);
				} while (c.moveToNext());
				c.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			//_database.endTransaction();
		}
		return vecRegularDemands;
	}

	public synchronized void TruncatetabelGroupId(String GroupId,Context context) {
		DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
		SQLiteDatabase _database = databaseHelper.getWritableDatabase();
		//_database.beginTransaction();
		String query = null;

		//query = "DELETE FROM AdvanceDemands where MGI_Code='"+GroupId+"'  ";
        query = "DELETE FROM AdvanceDemands where MLAI_ID='"+GroupId+"'  ";
		Log.e("query", query);
		try {
			SQLiteStatement sqLiteStatement = _database.compileStatement(query);
			sqLiteStatement.executeInsert();

		} catch (Exception e) {
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
	public synchronized  void TruncatetabeGroupId(String GroupId ,Context context) {
		DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
		SQLiteDatabase _database = databaseHelper.getWritableDatabase();
		//_database.beginTransaction();
		String query = null;

		//query = "DELETE FROM AdvanceTransactions  where GroupCode='"+GroupId+"' ";
        query = "DELETE FROM AdvanceTransactions  where LoanNo='"+GroupId+"' ";
		Log.e("query", query);
		try {
			SQLiteStatement sqLiteStatement = _database.compileStatement(query);
			sqLiteStatement.executeInsert();

		} catch (Exception e) {
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
}
