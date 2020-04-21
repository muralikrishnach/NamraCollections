package com.jayam.impactapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.jayam.impactapp.objects.BaseDO;
import com.jayam.impactapp.objects.MasterDataDo;
import com.jayam.impactapp.objects.RegularDemandsDO;
import com.jayam.impactapp.utils.StringUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class TrnsactionsBL  {




    public boolean Insert(BaseDO object, String RecieptNo, String TransactionCode, String CollType,Context context) {
	RegularDemandsDO parametrsDO = (RegularDemandsDO) object;

		ContentValues values = new ContentValues();

		values.put("MemberCode", parametrsDO.MemberCode);
		values.put("MemberName", parametrsDO.MemberName);
		values.put("MLAIID", parametrsDO.MLAI_ID);
		values.put("ReCeiptNumber", RecieptNo);/// StringUtils.getRecieptNumber(alArrayList.get(i))
		values.put("DemandDate", parametrsDO.DemandDate);
		values.put("Attendance", parametrsDO.Attendance);
		values.put("CollectionAmount", parametrsDO.collectedAmount);
		values.put("GNo", parametrsDO.GNo);
		values.put("OSAmount", parametrsDO.OSAmt);
		values.put("Centername", parametrsDO.CName);
		values.put("GroupName", parametrsDO.GroupName);
		values.put("CNo", parametrsDO.CNo);
		values.put("GLI", parametrsDO.GLI);
		values.put("Lateness", parametrsDO.Lateness);
		values.put("InstallNo", parametrsDO.InstallNo);
		values.put("TransactionCode", TransactionCode);
		values.put("CollType", CollType);
		values.put("MeetingStartTime", parametrsDO.meetingStartTime);
		values.put("MeetingEndTime", parametrsDO.meetingEndTime);
		values.put("FtodreasonID", parametrsDO.FTODID);
		values.put("Reason", parametrsDO.FTODReason);
		values.put("Demisedate", parametrsDO.DemiseDate);
		values.put("APFlag", "N");
		values.put("DupNo", "0");
		values.put("QOM", parametrsDO.qom);
		values.put("ProbInCenter", parametrsDO.probInCenter);
		values.put("GroupDisc", parametrsDO.groupDiscipline);
		values.put("CollExp", parametrsDO.collExp);
		values.put("CollExpRmel", parametrsDO.collExpRMEL);
		values.put("CollPlace", parametrsDO.collPlace);
		values.put("RepayBy", parametrsDO.repaymentMadeBy);
		values.put("RenewFeed", parametrsDO.RenewFeed);
		values.put("RepayBy", parametrsDO.repaymentMadeBy);
		values.put("RenewFeed", parametrsDO.RenewFeed);
		values.put("TransactionID", parametrsDO.TransactionID);
		values.put("Status", parametrsDO.Status);
		values.put("PaymentMode", parametrsDO.PaymentMode);
		values.put("TXNID", parametrsDO.TXNID);
		values.put("Datetime", parametrsDO.DateTime);
		values.put("MobileNo", parametrsDO.MobileNo);
		values.put("ImageName", parametrsDO.ImageName);
		values.put("bankName", parametrsDO.BankName);
		values.put("TransactionTime", parametrsDO.TransactionTime);
		values.put("EntryDate", parametrsDO.DateTime);

        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase _database = databaseHelper.getWritableDatabase();
       // //_database.beginTransaction();

		try {
			_database.insert("Transactions", null, values);
			////_database.setTransactionSuccessful();
		} catch (Exception e) {
			Log.e("Eror", "while inserting onto transaction table");
		}
        finally {
          // ////_database.endTransaction();
        }

        return true;
    }


	public boolean InsertLate(BaseDO object, String RecieptNo, String TransactionCode, String CollType,Context context) {
		RegularDemandsDO parametrsDO = (RegularDemandsDO) object;

		ContentValues values = new ContentValues();


		Log.v("","BranchPaymodelate"+parametrsDO.BranchPaymode);
		Log.v("","ProductName"+parametrsDO.ProductName);




		values.put("MemberCode", parametrsDO.MemberCode);
		values.put("MemberName", parametrsDO.MemberName);
		values.put("MLAIID", parametrsDO.MLAI_ID);
		values.put("ReCeiptNumber", RecieptNo);/// StringUtils.getRecieptNumber(alArrayList.get(i))
		values.put("DemandDate", parametrsDO.DemandDate);
		values.put("Attendance", parametrsDO.Attendance);
		values.put("CollectionAmount", parametrsDO.collectedAmount);
		values.put("GNo", parametrsDO.GNo);
		values.put("OSAmount", parametrsDO.OSAmt);
		values.put("Centername", parametrsDO.CName);
		values.put("GroupName", parametrsDO.GroupName);
		values.put("CNo", parametrsDO.CNo);
		values.put("GLI", parametrsDO.GLI);
		values.put("Lateness", parametrsDO.Lateness);
		values.put("InstallNo", parametrsDO.InstallNo);
		values.put("TransactionCode", TransactionCode);
		values.put("CollType", CollType);
		values.put("MeetingStartTime", parametrsDO.meetingStartTime);
		values.put("MeetingEndTime", parametrsDO.meetingEndTime);
		values.put("FtodreasonID", parametrsDO.FTODID);
		values.put("Reason", parametrsDO.FTODReason);
		values.put("Demisedate", parametrsDO.DemiseDate);
		values.put("APFlag", "N");
		values.put("DupNo", "0");
		values.put("QOM", parametrsDO.qom);
		values.put("ProbInCenter", parametrsDO.probInCenter);
		values.put("GroupDisc", parametrsDO.groupDiscipline);
		values.put("CollExp", parametrsDO.collExp);
		values.put("CollExpRmel", parametrsDO.collExpRMEL);
		values.put("CollPlace", parametrsDO.collPlace);
		values.put("RepayBy", parametrsDO.repaymentMadeBy);
		values.put("RenewFeed", parametrsDO.RenewFeed);
		values.put("RepayBy", parametrsDO.repaymentMadeBy);
		values.put("RenewFeed", parametrsDO.RenewFeed);
		values.put("TransactionID", parametrsDO.TransactionID);
		values.put("Status", parametrsDO.Status);
		values.put("PaymentMode", parametrsDO.PaymentMode);
		values.put("TXNID", parametrsDO.TXNID);
		values.put("Datetime", parametrsDO.DateTime);
		values.put("MobileNo", parametrsDO.MobileNo);
		values.put("ImageName", parametrsDO.ImageName);
		values.put("bankName", parametrsDO.BankName);
		values.put("TransactionTime", parametrsDO.TransactionTime);
		values.put("EntryDate", parametrsDO.DateTime);

		//

		values.put("DemandDate", parametrsDO.DemandDate);
		values.put("DemandTotal", parametrsDO.DemandTotal);
		values.put("ODAmount", parametrsDO.ODAmount);
		values.put("LatitudeCenter", parametrsDO.LatitudeCenter);
		values.put("LongitudeCenter", parametrsDO.LongitudeCenter);
		values.put("LatitudeGroup", parametrsDO.LatitudeGroup);
		values.put("LongitudeGroup", parametrsDO.LongitudeGroup);
		values.put("LatitudeMember", parametrsDO.LatitudeMember);
		values.put("LongitudeMember", parametrsDO.LongitudeMember);
		values.put("CenterAmt", parametrsDO.CenterAmt);
		values.put("CenterMeeting", parametrsDO.CenterMeeting);
		values.put("GroupAmt", parametrsDO.GroupAmt);
		values.put("GroupMeeting", parametrsDO.GroupMeeting);

		values.put("ProductId", parametrsDO.ProductName);
		values.put("BranchPaymode", parametrsDO.BranchPaymode);
		values.put("Lateconformation","No");

        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase _database = databaseHelper.getWritableDatabase();
       // //_database.beginTransaction();

		try {
			_database.insert("LateTransactions", null, values);
			//_database.setTransactionSuccessful();
		} catch (Exception e) {
			Log.e("Eror", "while inserting onto transaction table");
		}
        finally {
            ////_database.endTransaction();
        }

        return true;
	}

    // insert lang and lat

    public void Insertlanglat(String langitude, String latitude, String unqid, String Type,Context context) {
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase _database = databaseHelper.getWritableDatabase();
        //_database.beginTransaction();
	String query = null;
	if (Type.equals("Center")) {
	    query = "UPDATE Transactions SET latitude='" + latitude + "' , langitude='" + langitude + "'  where CNo = '"
		    + unqid + "'";
	} else if (Type.equals("Group")) {
	    query = "UPDATE Transactions SET latitude='" + latitude + "' , langitude='" + langitude + "' where GNo = '"
		    + unqid + "'";
	}
	Log.d("mfimo", query);

	try {
        SQLiteStatement sqLiteStatement = _database.compileStatement(query);
        sqLiteStatement.executeInsert();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    finally {
       ////_database.setTransactionSuccessful();
       ////_database.endTransaction();
       // _database.close();
    }

    }



	public void UpdateLateAmount(String amount,String MLAID,String Imagepath,Context context) {
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase _database = databaseHelper.getWritableDatabase();
        //_database.beginTransaction();
		String query = null;
		query = "UPDATE LateTransactions SET Lateamount='" + amount + "' ,Lateconformation='yes', ImageName='" + Imagepath + "' where MLAIID = '" + MLAID + "'";
		Log.v("","UpdateLateAmount"+query);
		try {
            SQLiteStatement sqLiteStatement = _database.compileStatement(query);
            sqLiteStatement.executeInsert();
		} catch (Exception e) {
			e.printStackTrace();
		}
        finally {
           ////_database.setTransactionSuccessful();
           ////_database.endTransaction();
           // _database.close();
        }

	}

    public boolean InsertNoPaymnet(ArrayList<RegularDemandsDO> alArrayList,Context context) {

		DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
		SQLiteDatabase _database = databaseHelper.getWritableDatabase();
		//_database.beginTransaction();

	for (RegularDemandsDO parametrsDO : alArrayList) {
	    ContentValues values = new ContentValues();

	    values.put("MemberCode", parametrsDO.MemberCode);
	    values.put("MemberName", parametrsDO.MemberName);
	    values.put("MLAIID", parametrsDO.MLAI_ID);
	    values.put("ReCeiptNumber", "0");/// StringUtils.getRecieptNumber(alArrayList.get(i))
	    values.put("DemandDate", parametrsDO.DemandDate);
	    values.put("Attendance", parametrsDO.Attendance);
	    values.put("CollectionAmount", "0");
	    values.put("GNo", parametrsDO.GNo);
	    values.put("OSAmount", parametrsDO.OSAmt);
	    values.put("Centername", parametrsDO.CName);
	    values.put("GroupName", parametrsDO.GroupName);
	    values.put("CNo", parametrsDO.CNo);
	    values.put("GLI", parametrsDO.GLI);
	    values.put("Lateness", parametrsDO.Lateness);
	    values.put("InstallNo", parametrsDO.InstallNo);
	    values.put("Print", "N");
	    values.put("TransactionCode", "0");
	    values.put("CollType", "R");
	    values.put("MeetingStartTime", "0");
	    values.put("MeetingEndTime", "0");
	    values.put("FtodreasonID", parametrsDO.FTODID);
	    values.put("Reason", parametrsDO.FTODReason);
	    values.put("Demisedate", parametrsDO.DemiseDate);
	    values.put("QOM", parametrsDO.qom);
	    values.put("ProbInCenter", parametrsDO.probInCenter);
	    values.put("GroupDisc", parametrsDO.groupDiscipline);
	    values.put("CollExp", parametrsDO.collExp);
	    values.put("CollExpRmel", parametrsDO.collExpRMEL);
	    values.put("CollPlace", parametrsDO.collPlace);
	    values.put("RepayBy", parametrsDO.repaymentMadeBy);
	    values.put("RenewFeed", parametrsDO.RenewFeed);
		values.put("Datetime", parametrsDO.DateTime);
		values.put("MobileNo", parametrsDO.MobileNo);


	    try {

		_database.insert("Transactions", null, values);
			//_database.setTransactionSuccessful();
	    } catch (Exception e) {
		Log.e("Eror", "while inserting onto transaction table");
	    }
        finally {
           ////_database.endTransaction();
        }
	}
	return true;
    }


    public boolean Update(BaseDO object) {
	return false;
    }


    public boolean Delete(BaseDO object) {
	return false;
    }

    public void Truncatetabel(Context context) {

        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase _database = databaseHelper.getWritableDatabase();
        //_database.beginTransaction();
	String query = null;

	query = "DELETE FROM Transactions ";
	Log.e("query", query);
	try {
        SQLiteStatement sqLiteStatement = _database.compileStatement(query);
        sqLiteStatement.executeInsert();

	} catch (Exception e) {
	    e.printStackTrace();
	}
    finally {
       ////_database.setTransactionSuccessful();
       ////_database.endTransaction();
       // _database.close();
    }

    }




    public ArrayList<RegularDemandsDO> SelectAll(Context context) {
	ArrayList<RegularDemandsDO> vecRegularDemands = new ArrayList<RegularDemandsDO>();
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase _database = databaseHelper.getReadableDatabase();
        //_database.beginTransaction();
	Cursor c = null;
	try {
	    c = _database.rawQuery("select * from Transactions", null);

	    if (vecRegularDemands != null) {
		vecRegularDemands.clear();
	    }

	    if (c.moveToFirst()) {
		do {
		    RegularDemandsDO obj = new RegularDemandsDO();
		    try {
			obj.MemberCode = c.getString(0);
			obj.MemberName = c.getString(1);
			obj.MLAI_ID = c.getString(2);
			obj.ReciptNumber = c.getString(3);
			obj.DemandDate = c.getString(4);
			obj.Attendance = c.getString(5);
			obj.collectedAmount = c.getString(6);
			obj.GNo = c.getString(7);
			obj.OSAmt = c.getString(8);
			obj.CName = c.getString(9);
			obj.GroupName = c.getString(10);
			obj.CNo = c.getString(11);
			obj.GLI = c.getString(12);

			obj.Lateness = c.getString(13);
			obj.InstallNo = c.getString(14);
			obj.Print = c.getString(15);
			obj.TransactionCode = c.getString(16);
			obj.CollType = c.getString(17);
			obj.meetingStartTime = c.getString(18);
			obj.meetingEndTime = c.getString(19);
			obj.FTODID = c.getString(20);
			obj.FTODReason = c.getString(21);
			obj.DemiseDate = c.getString(22);
			obj.APFlag = c.getString(23);
			obj.qom = c.getString(25);
			obj.probInCenter = c.getString(26);
			obj.groupDiscipline = c.getString(27);
			obj.collExp = c.getString(28);
			obj.collExpRMEL = c.getString(29);
			obj.collPlace = c.getString(30);
			obj.repaymentMadeBy = c.getString(31);
			obj.RenewFeed = c.getString(32);
			obj.langitude = c.getString(33);
			obj.latitude = c.getString(34);

				obj.TransactionID = c.getString(35);
				obj.Status = c.getString(36);
				obj.MobileNo = c.getString(38);
				obj.PaymentMode = c.getString(39);
				obj.DateTime = c.getString(42);
		    } catch (Exception e) {
			Log.e("exception", "while getting from server");
		    }
		    // CREATE TABLE "RegularDemands" ("CNo" VARCHAR PRIMARY KEY
		    // NOT NULL , "CName" VARCHAR, "GNo" VARCHAR,
		    // "EII_EMP_ID" VARCHAR, "GroupName" VARCHAR, "MemberCode"
		    // VARCHAR, "MemberName" VARCHAR, "DemandDate" VARCHAR,
		    // "MLAI_ID" VARCHAR, "OSAmt" VARCHAR, "DemandTotal"
		    // VARCHAR, "ODAmount" VARCHAR, "Attendance" VARCHAR)

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
           ////_database.endTransaction();
        }
    }
	return vecRegularDemands;
    }

    public ArrayList<RegularDemandsDO> SelectAll(String TxnCode, String Gno, String Type,Context context) {
	ArrayList<RegularDemandsDO> vecRegularDemands = new ArrayList<RegularDemandsDO>();
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase _database = databaseHelper.getReadableDatabase();
        //_database.beginTransaction();
	Cursor c = null;
	try {
	    if (Type.equals("Group")) {
		c = _database.rawQuery("select * from Transactions where TransactionCode = '" + TxnCode + "'", null);
	    } else if (Type.equals("Center")) {
		c = _database.rawQuery(
			"select * from Transactions where TransactionCode = '" + TxnCode + "' and GNo='" + Gno + "'",
			null);
	    } else if (Type.equals("Member")) {
		c = _database.rawQuery(
			"select * from Transactions where TransactionCode = '" + TxnCode + "' and MLAIID='" + Gno + "'",
			null);
	    }

	    if (vecRegularDemands != null) {
		vecRegularDemands.clear();
	    }

	    if (c.moveToFirst()) {
		do {
		    RegularDemandsDO obj = new RegularDemandsDO();
		    try {
			obj.MemberCode = c.getString(0);
			obj.MemberName = c.getString(1);
			obj.MLAI_ID = c.getString(2);
			obj.ReciptNumber = c.getString(3);
			obj.DemandDate = c.getString(4);
			obj.Attendance = c.getString(5);
			obj.collectedAmount = c.getString(6);
			obj.GNo = c.getString(7);
			obj.OSAmt = c.getString(8);
			obj.CName = c.getString(9);
			obj.GroupName = c.getString(10);
			obj.CNo = c.getString(11);
			obj.GLI = c.getString(12);
			obj.Lateness = c.getString(12);
			obj.InstallNo = c.getString(14);
		    } catch (Exception e) {
			Log.e("exception", "while getting from server");
		    }
		    // CREATE TABLE "RegularDemands" ("CNo" VARCHAR PRIMARY KEY
		    // NOT NULL , "CName" VARCHAR, "GNo" VARCHAR,
		    // "EII_EMP_ID" VARCHAR, "GroupName" VARCHAR, "MemberCode"
		    // VARCHAR, "MemberName" VARCHAR, "DemandDate" VARCHAR,
		    // "MLAI_ID" VARCHAR, "OSAmt" VARCHAR, "DemandTotal"
		    // VARCHAR, "ODAmount" VARCHAR, "Attendance" VARCHAR)

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
           ////_database.endTransaction();
        }
    }
	return vecRegularDemands;
    }



    // select Centername, sum(CollectionAmount) from RegularDemandsTemp group by
    // CNo

    public ArrayList<RegularDemandsDO> SelectAllCenters_CollectedAmt(Context context) {
	ArrayList<RegularDemandsDO> vecRegularDemands = new ArrayList<RegularDemandsDO>();
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase _database = databaseHelper.getReadableDatabase();
        //_database.beginTransaction();
	Cursor c = null;
	try {
	    c = _database.rawQuery("select  CNo,Centername, sum(CollectionAmount)  from Transactions group by CNo",
		    null);

	    if (vecRegularDemands != null) {
		vecRegularDemands.clear();
	    }

	    if (c.moveToFirst()) {
		do {
		    RegularDemandsDO obj = new RegularDemandsDO();
		    try {
			// obj.MemberCode = c.getString(0);
			// obj.MemberName = c.getString(1);
			// obj.MLAI_ID = c.getString(2);
			// obj.ReciptNumber = c.getString(3);
			// obj.DemandDate = c.getString(4);
			// obj.Attendance = c.getString(5);
			obj.collectedAmount = c.getString(2);
			// obj.GNo = c.getString(7);
			// obj.OSAmt = c.getString(8);
			obj.CName = c.getString(1);
			obj.CNo = c.getString(0);
			// obj.GroupName = c.getString(10);
		    } catch (Exception e) {
			Log.e("exception", "while getting from server");
		    }
		    // CREATE TABLE "RegularDemands" ("CNo" VARCHAR PRIMARY KEY
		    // NOT NULL , "CName" VARCHAR, "GNo" VARCHAR,
		    // "EII_EMP_ID" VARCHAR, "GroupName" VARCHAR, "MemberCode"
		    // VARCHAR, "MemberName" VARCHAR, "DemandDate" VARCHAR,
		    // "MLAI_ID" VARCHAR, "OSAmt" VARCHAR, "DemandTotal"
		    // VARCHAR, "ODAmount" VARCHAR, "Attendance" VARCHAR)

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
           ////_database.endTransaction();
        }
    }
	return vecRegularDemands;
    }

    public ArrayList<RegularDemandsDO> SelectAllGroups(String CNo,Context context) {
	ArrayList<RegularDemandsDO> vecRegularDemands = new ArrayList<RegularDemandsDO>();
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase _database = databaseHelper.getReadableDatabase();
        //_database.beginTransaction();
	Cursor c = null;
	try {
	    c = _database.rawQuery("select * from Transactions where CNo = '" + CNo + "'", null);
	    Log.e("exception", "select * from Transactions where CNo = '" + CNo + "'");
	    if (vecRegularDemands != null) {
		vecRegularDemands.clear();
	    }

	    if (c.moveToFirst()) {
		do {
		    RegularDemandsDO obj = new RegularDemandsDO();
		    try {
			obj.MemberCode = c.getString(0);
			obj.MemberName = c.getString(1);
			obj.MLAI_ID = c.getString(2);
			obj.ReciptNumber = c.getString(3);
			obj.DemandDate = c.getString(4);
			obj.Attendance = c.getString(5);
			obj.collectedAmount = c.getString(6);
			obj.GNo = c.getString(7);
			obj.OSAmt = c.getString(8);
			obj.CName = c.getString(9);
			obj.GroupName = c.getString(10);
			obj.CNo = c.getString(11);
		    } catch (Exception e) {
			Log.e("exception", "while getting from server");
		    }
		    // CREATE TABLE "RegularDemands" ("CNo" VARCHAR PRIMARY KEY
		    // NOT NULL , "CName" VARCHAR, "GNo" VARCHAR,
		    // "EII_EMP_ID" VARCHAR, "GroupName" VARCHAR, "MemberCode"
		    // VARCHAR, "MemberName" VARCHAR, "DemandDate" VARCHAR,
		    // "MLAI_ID" VARCHAR, "OSAmt" VARCHAR, "DemandTotal"
		    // VARCHAR, "ODAmount" VARCHAR, "Attendance" VARCHAR)

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
           ////_database.endTransaction();
        }
    }
	return vecRegularDemands;
    }

    public ArrayList<RegularDemandsDO> SelectAllGroups_CollectedAmt(String CNO,Context context) {
	ArrayList<RegularDemandsDO> vecRegularDemands = new ArrayList<RegularDemandsDO>();
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase _database = databaseHelper.getReadableDatabase();
        //_database.beginTransaction();
	Cursor c = null;
	try {

	    c = _database.rawQuery("select  GroupName,GNo, sum(CollectionAmount)  from Transactions where CNo = '" + CNO
		    + "' group by (GNo)", null);

	    if (vecRegularDemands != null) {
		vecRegularDemands.clear();
	    }

	    if (c.moveToFirst()) {
		do {
		    RegularDemandsDO obj = new RegularDemandsDO();
		    try {
			// obj.MemberCode = c.getString(0);
			// obj.MemberName = c.getString(1);
			// obj.MLAI_ID = c.getString(2);
			// obj.ReciptNumber = c.getString(3);
			// obj.DemandDate = c.getString(4);
			// obj.Attendance = c.getString(5);
			obj.collectedAmount = c.getString(2);
			// obj.GNo = c.getString(7);
			// obj.OSAmt = c.getString(8);
			obj.GNo = c.getString(1);
			obj.GroupName = c.getString(0);
			// obj.GroupName = c.getString(10);
		    } catch (Exception e) {
			Log.e("exception", "while getting from server");
		    }
		    // CREATE TABLE "RegularDemands" ("CNo" VARCHAR PRIMARY KEY
		    // NOT NULL , "CName" VARCHAR, "GNo" VARCHAR,
		    // "EII_EMP_ID" VARCHAR, "GroupName" VARCHAR, "MemberCode"
		    // VARCHAR, "MemberName" VARCHAR, "DemandDate" VARCHAR,
		    // "MLAI_ID" VARCHAR, "OSAmt" VARCHAR, "DemandTotal"
		    // VARCHAR, "ODAmount" VARCHAR, "Attendance" VARCHAR)

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
           ////_database.endTransaction();
        }
    }
	return vecRegularDemands;
    }

    public ArrayList<RegularDemandsDO> SelectAllMembers_CollectedAmt(String GNO,Context context) {
	ArrayList<RegularDemandsDO> vecRegularDemands = new ArrayList<RegularDemandsDO>();
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase _database = databaseHelper.getReadableDatabase();
        //_database.beginTransaction();
	Cursor c = null;
	try {

	    c = _database
		    .rawQuery("select  MLAIID ,  MemberName ,SUM(CollectionAmount)  from Transactions  where  GNo = '"
			    + GNO + "' group by (MemberCode )", null);

	    if (vecRegularDemands != null) {
		vecRegularDemands.clear();
	    }

	    if (c.moveToFirst()) {
		do {
		    RegularDemandsDO obj = new RegularDemandsDO();
		    try {
			// obj.MemberCode = c.getString(0);
			obj.MemberName = c.getString(1);
			obj.MLAI_ID = c.getString(0);
			// obj.ReciptNumber = c.getString(3);
			// obj.DemandDate = c.getString(4);
			// obj.Attendance = c.getString(5);
			obj.collectedAmount = c.getString(2);
			// obj.GNo = c.getString(7);
			// obj.OSAmt = c.getString(8);
			// obj.GNo = c.getString(1);
			// obj.GroupName = c.getString(0);
			// obj.GroupName = c.getString(10);
		    } catch (Exception e) {
			Log.e("exception", "while getting from server");
		    }
		    // CREATE TABLE "RegularDemands" ("CNo" VARCHAR PRIMARY KEY
		    // NOT NULL , "CName" VARCHAR, "GNo" VARCHAR,
		    // "EII_EMP_ID" VARCHAR, "GroupName" VARCHAR, "MemberCode"
		    // VARCHAR, "MemberName" VARCHAR, "DemandDate" VARCHAR,
		    // "MLAI_ID" VARCHAR, "OSAmt" VARCHAR, "DemandTotal"
		    // VARCHAR, "ODAmount" VARCHAR, "Attendance" VARCHAR)

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
           ////_database.endTransaction();
        }
    }
	return vecRegularDemands;

	// select MemberCode , MemberName ,SUM(CollectionAmount) from
	// Transactions where GNo = 'DSH005-84-2-2' group by (MemberCode )
    }

    public ArrayList<RegularDemandsDO> SelectDistinctGroupsFromCenter(String CNO,Context context) {
	ArrayList<RegularDemandsDO> vecRegularDemands = new ArrayList<RegularDemandsDO>();
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase _database = databaseHelper.getReadableDatabase();
        //_database.beginTransaction();
	Cursor c = null;
	try {

	    c = _database.rawQuery("select  Distinct GNo from RegularDemands where CNo = '" + CNO + "'", null);

	    if (vecRegularDemands != null) {
		vecRegularDemands.clear();
	    }

	    if (c.moveToFirst()) {
		do {
		    RegularDemandsDO obj = new RegularDemandsDO();
		    try {
			obj.GNo = c.getString(0);
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
           ////_database.endTransaction();
        }
    }
	return vecRegularDemands;

    }

    public String getCollectedAmt(String GNO,Context context) {
	String collectedAmt = null;
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase _database = databaseHelper.getReadableDatabase();
        //_database.beginTransaction();
	Cursor c = null;
	try {

	    c = _database.rawQuery("select  SUM(CollectionAmount)  from Transactions  where  GNo = '" + GNO + "'",
		    null);

	    if (c.moveToFirst()) {
		do {
		    RegularDemandsDO obj = new RegularDemandsDO();
		    try {
			collectedAmt = c.getString(0);
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
           ////_database.endTransaction();
        }
    }
	return collectedAmt;
	// select SUM(CollectionAmount) from Transactions where GNo =
	// 'DSH005-84-2-2'
    }

    public String getCollectedAmtForMember(String MLAIID,Context context) {
	String collectedAmt = null;
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase _database = databaseHelper.getReadableDatabase();
        //_database.beginTransaction();
	Cursor c = null;
	try {

	    c = _database.rawQuery("select  SUM(CollectionAmount)  from Transactions  where  MLAIID= '" + MLAIID + "'",
		    null);

	    if (c.moveToFirst()) {
		do {
		    try {
			collectedAmt = c.getString(0);
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
           ////_database.endTransaction();
        }
    }

        return collectedAmt;
	// select SUM(CollectionAmount) from Transactions where MLAIID= '112756'
    }

    public String getCountofCollectionAccounts(Context context) {
	String totalODAmount = null;
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase _database = databaseHelper.getReadableDatabase();
        //_database.beginTransaction();
	Cursor c = null;
	try {
	    c = _database.rawQuery("select  Count(Distinct MLAIID)  from Transactions", null);

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
           ////_database.endTransaction();
        }
    }
	return totalODAmount;

	// select Count(Distinct MLAIID) from Transactions
    }

    public String getTotalCollectionAmount(Context context) {
	String totalODAmount = null;
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase _database = databaseHelper.getReadableDatabase();
        //_database.beginTransaction();
	Cursor c = null;
	try {
	    c = _database.rawQuery("select SUM(CollectionAmount)  from Transactions", null);

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
           ////_database.endTransaction();
        }
    }
	return totalODAmount;

	// select SUM(CollectionAmount) from Transactions
    }

    public String getTotalAttendense(Context context) {
	String totalODAmount = null;
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase _database = databaseHelper.getReadableDatabase();
        //_database.beginTransaction();

        Cursor c = null;
	try {
	    c = _database.rawQuery("select Count(Attendance)  from Transactions where Attendance = '1'", null);

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
           ////_database.endTransaction();
        }
    }
	return totalODAmount;

	// select Count(Attendance) from Transactions where Attendance = '1'
    }

    public String getMemberCount(String Mlaid,Context context) {
	String count = null;
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase _database = databaseHelper.getReadableDatabase();
        //_database.beginTransaction();
	Cursor c = null;
	try {
	    c = _database.rawQuery("select Count(*)  from Transactions where MLAIID=" + Mlaid + "", null);

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
           ////_database.endTransaction();
        }
    }
	return count;
    }

    public String getTransactionCount(String CNO,Context context) {
	String count = null;
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase _database = databaseHelper.getReadableDatabase();
        //_database.beginTransaction();
	Cursor c = null;
	try {
	    c = _database.rawQuery("select Count(*)  from Transactions where CNo='" + CNO + "'", null);

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
           ////_database.endTransaction();
        }
    }
	return count;
    }

    public String getGroupCount(String Code, String Type,Context context) {
	String count = null;
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase _database = databaseHelper.getReadableDatabase();
        //_database.beginTransaction();
	Cursor c = null;
	try {
	    if (Type.equalsIgnoreCase("Group")) {
		c = _database.rawQuery(
			"SELECT count(Distinct(TransactionCode)) FROM Transactions where GNo='" + Code + "'", null);
	    } else if (Type.equalsIgnoreCase("Center")) {
		c = _database.rawQuery(
			"SELECT count(Distinct(TransactionCode)) FROM Transactions where CNo='" + Code + "'", null);
	    }
	    Log.e("Groupcount", "SELECT count(Distinct(TransactionCode)) FROM Transactions where CNo='" + Code + "'");

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
           ////_database.endTransaction();
        }
    }
	return count;
    }






	public String getCashCountRegular(String Code, String Type,Context context) {
		String count = null;
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase _database = databaseHelper.getReadableDatabase();
        //_database.beginTransaction();
		Cursor c = null;
		String query="";
		try {

			// query="SELECT count(Distinct(TransactionCode)) FROM Transactions where MLAIID='" + Code + "'  and PaymentMode='CashLess'";
			query="SELECT count(Distinct(TransactionID)) FROM RegularDemands where MLAI_ID='" + Code + "'  and PaymentMode in('CashLess','Cash')";
			c = _database.rawQuery(query, null);

			Log.v("getCashCount", ""+query);

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
               ////_database.endTransaction();
            }
        }
		return count;
	}
    public String getMemberCollAmt(String Mlaid,Context context) {
	String count = null;
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase _database = databaseHelper.getReadableDatabase();
        //_database.beginTransaction();
	Cursor c = null;
	try {
	    c = _database.rawQuery("select SUM(CollectionAmount)  from Transactions where MLAIID='" + Mlaid + "'",
		    null);
	    Log.e("Queru:----", "select SUM(CollectionAmount)  from Transactions where MLAIID='" + Mlaid + "'");

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
           ////_database.endTransaction();
        }
    }
	return count;
    }

    public String getTotalCollAmt(String Txncode,Context context) {
	String count = null;

        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase _database = databaseHelper.getReadableDatabase();
        //_database.beginTransaction();
	Cursor c = null;
	try {
	    c = _database.rawQuery(
		    "select SUM(CollectionAmount)  from Transactions where TransactionCode='" + Txncode + "'", null);
	    Log.e("Query:----",
		    "select SUM(CollectionAmount)  from Transactions where TransactionCode='" + Txncode + "'");

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
           ////_database.endTransaction();
        }
    }
	return count;
    }



    // SELECT count(*) from Transactions



    public String checkGroupAvailbleIntransactiontable(String GroupNumber,Context context) {
	String count = null;

        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase _database = databaseHelper.getReadableDatabase();
        //_database.beginTransaction();
	Cursor c = null;
	try {
	    c = _database.rawQuery("SELECT count(*)  from Transactions where GNo = '" + GroupNumber + "'", null);

	    if (c.moveToFirst()) {
		do {
		    try {
			count = c.getString(0);
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
           ////_database.endTransaction();
        }
    }
	return count;

	// select Count(Attendance) from Transactions where Attendance = '1'
    }

    public String checkCenterAvailbleIntransactiontable(String CenterNumber,Context context) {
	String count = null;
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase _database = databaseHelper.getReadableDatabase();
        //_database.beginTransaction();
	Cursor c = null;
	try {
	    c = _database.rawQuery("SELECT count(*)  from Transactions where CNo = '" + CenterNumber + "'", null);

	    if (c.moveToFirst()) {
		do {
		    try {
			count = c.getString(0);
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
           ////_database.endTransaction();
        }
    }
	return count;

	// select Count(Attendance) from Transactions where Attendance = '1'
    }

    public String updatePrintFlag(String TxnCode, String status,Context context) {
	String count = null;
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase _database = databaseHelper.getReadableDatabase();
        //_database.beginTransaction();
	Cursor c = null;
	try {
	    c = _database.rawQuery(
		    "UPDATE Transactions SET Print = '" + status + "' where TransactionCode = '" + TxnCode + "'", null);

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
           ////_database.endTransaction();
        }
    }

        return count;

    }

    public void updateFTODReasons(String MALID,Context context) {
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase _database = databaseHelper.getWritableDatabase();
        //_database.beginTransaction();
	String query = null;

	query = "UPDATE Transactions SET FtodreasonID='0',Reason='0',Demisedate='null' where MLAIID = '" + MALID + "'";
	Log.e("query", query);
	try {
        SQLiteStatement sqLiteStatement = _database.compileStatement(query);
        sqLiteStatement.executeInsert();

	} catch (Exception e) {
	    e.printStackTrace();
	}
    finally {
       ////_database.setTransactionSuccessful();
       ////_database.endTransaction();
        //_database.close();
    }
    }

    public ArrayList<RegularDemandsDO> SelectDistinctTransactioncodeFromCenter(String CODE, String Type,Context context) {
	ArrayList<RegularDemandsDO> vecRegularDemands = new ArrayList<RegularDemandsDO>();
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase _database = databaseHelper.getReadableDatabase();
        //_database.beginTransaction();
	Cursor c = null;
	try {
	    if (Type.equals("Center")) {
		c = _database.rawQuery("select  Distinct TransactionCode from Transactions where CNo='" + CODE + "'",
			null);
	    } else if (Type.equals("Group")) {
		c = _database.rawQuery("select  Distinct TransactionCode from Transactions where GNo='" + CODE + "'",
			null);
	    } else if (Type.equals("Member")) {
		c = _database.rawQuery("select  Distinct TransactionCode from Transactions where MLAIID='" + CODE + "'",
			null);
	    }

	    if (vecRegularDemands != null) {
		vecRegularDemands.clear();
	    }

	    if (c.moveToFirst()) {
		do {
		    RegularDemandsDO obj = new RegularDemandsDO();
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
           ////_database.endTransaction();
        }
    }
	return vecRegularDemands;

    }

    public ArrayList<RegularDemandsDO> SelectDistinctGroupsCodefromtxn(String TxnCode,Context context) {
	ArrayList<RegularDemandsDO> vecRegularDemands = new ArrayList<RegularDemandsDO>();
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase _database = databaseHelper.getReadableDatabase();
        //_database.beginTransaction();
	Cursor c = null;
	try {

	    c = _database.rawQuery("select  Distinct GNo from Transactions where TransactionCode = '" + TxnCode + "'",
		    null);

	    if (vecRegularDemands != null) {
		vecRegularDemands.clear();
	    }

	    if (c.moveToFirst()) {
		do {
		    RegularDemandsDO obj = new RegularDemandsDO();
		    try {
			obj.GNo = c.getString(0);
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
           ////_database.endTransaction();
        }
    }
	return vecRegularDemands;

    }

    public void updateDupNO(String DupNo, String MALID, String Txncode,Context context) {
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase _database = databaseHelper.getWritableDatabase();
        //_database.beginTransaction();
	String query = null;

	query = "UPDATE Transactions SET DupNo='" + DupNo + "' where MLAIID = '" + MALID + "' and TransactionCode='"
		+ Txncode + "'";
	Log.e("query", query);
	try {
        SQLiteStatement sqLiteStatement = _database.compileStatement(query);
        sqLiteStatement.executeInsert();

	} catch (Exception e) {
	    e.printStackTrace();
	}
    finally {
       ////_database.setTransactionSuccessful();
       ////_database.endTransaction();
       // _database.close();
    }
    }



    public String getDupNoforMember(String Txncode, String MLAIID,Context context) {
	String count = null;
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase _database = databaseHelper.getReadableDatabase();
        //_database.beginTransaction();
	Cursor c = null;
	try {
	    c = _database.rawQuery("SELECT DupNo  from Transactions where MLAIID = '" + MLAIID
		    + "' and TransactionCode='" + Txncode + "'", null);

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
           ////_database.endTransaction();
        }
    }

        return count;

    }

    public String SelectCenterCode(String txnCode,Context context) {
	String count = null;
	// SQLiteDatabase _database = DatabaseHelper.closedatabase();
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase _database = databaseHelper.getReadableDatabase();
        //_database.beginTransaction();
	Cursor c = null;
	try {
	    c = _database.rawQuery("select Distinct(CNo) from Transactions where TransactionCode='" + txnCode + "'",
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
           ////_database.endTransaction();
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
	    query = "UPDATE Transactions SET DupNo=DupNo+1 where CNo = '" + Code + "' and TransactionCode='" + Txncode
		    + "'";
	} else if (Type.equals("Group")) {
	    query = "UPDATE Transactions SET DupNo=DupNo+1 where GNo = '" + Code + "' and TransactionCode='" + Txncode
		    + "'";
	}
	Log.e("query", query);
	try {
        SQLiteStatement sqLiteStatement = _database.compileStatement(query);
        sqLiteStatement.executeInsert();
	} catch (Exception e) {
	    e.printStackTrace();
	}
    finally {
       ////_database.setTransactionSuccessful();
       ////_database.endTransaction();
       // _database.close();
    }
    }

    public String SelectMinDupNo(String txnCode, String code, String Type,Context context) {
	String count = null;
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase _database = databaseHelper.getReadableDatabase();
        //_database.beginTransaction();
	Cursor c = null;
	try {
	    if (Type.equals("Center")) {
		c = _database.rawQuery("select MIN(DupNo) from Transactions where TransactionCode='" + txnCode
			+ "' and CNo='" + code + "'", null);
	    } else if (Type.equals("Group")) {
		c = _database.rawQuery("select MIN(DupNo) from Transactions where TransactionCode='" + txnCode
			+ "' and GNo='" + code + "'", null);
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
    finally{
        if(c != null )
        {
            c.close();
           ////_database.endTransaction();
        }
    }
	return count;
    }



    public String MinDupNo(String code, String Type,Context context) {
	String count = null;
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase _database = databaseHelper.getReadableDatabase();
        //_database.beginTransaction();
	Cursor c = null;
	try {
	    if (Type.equals("Center")) {
		c = _database.rawQuery("select MIN(DupNo) from Transactions where  CNo='" + code + "'", null);
	    } else if (Type.equals("Group")) {
		c = _database.rawQuery("select MIN(DupNo) from Transactions where  GNo='" + code + "'", null);
	    } else if (Type.equals("Member")) {
		c = _database.rawQuery("select MIN(DupNo) from Transactions where  MLAIID='" + code + "'", null);
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
    finally{
        if(c != null )
        {
            c.close();
           ////_database.endTransaction();
        }
    }
	return count;
    }

    public String updateActualPrintFlag(String TxnCode, String status, Context context) {
	String count = null;
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase _database = databaseHelper.getWritableDatabase();
        //_database.beginTransaction();
	Cursor c = null;
	String query = "UPDATE Transactions SET APFlag = '" + status + "' where TransactionCode = '" + TxnCode + "'";
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
       ////_database.endTransaction();
    }

        return count;
    }

    public String updatePIC(String CNO, String status,Context context) {
	String count = null;
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase _database = databaseHelper.getWritableDatabase();
        //_database.beginTransaction();
	Cursor c = null;
	String query = "UPDATE Transactions SET ProbInCenter = '" + status + "' where CNo = '" + CNO + "'";
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
       ////_database.endTransaction();
    }
	return count;
    }
	public synchronized  void TruncatetabelGroupId(String groupid,Context context) {
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase _database = databaseHelper.getWritableDatabase();
        //_database.beginTransaction();
		String query = null;

		query = "DELETE FROM Transactions where GNo='"+groupid+"' ";
		Log.e("query", query);
		try {
            SQLiteStatement sqLiteStatement = _database.compileStatement(query);
            sqLiteStatement.executeInsert();

		} catch (Exception e) {
			e.printStackTrace();
		}
        finally {
           ////_database.setTransactionSuccessful();
           ////_database.endTransaction();
           // _database.close();
        }

	}
	public synchronized  void TruncatetabelGroupIdLate(String groupid,Context context) {
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase _database = databaseHelper.getWritableDatabase();
        //_database.beginTransaction();
		String query = null;

		query = "DELETE FROM LateTransactions where GNo='"+groupid+"' and Lateconformation='yes'";

		try {
            SQLiteStatement sqLiteStatement = _database.compileStatement(query);
            sqLiteStatement.executeInsert();

		} catch (Exception e) {
			e.printStackTrace();
		}
        finally {
           ////_database.setTransactionSuccessful();
           ////_database.endTransaction();
          //  _database.close();
        }

	}

	public synchronized ArrayList<RegularDemandsDO> SelectAllFlagwise(Context context) {
		ArrayList<RegularDemandsDO> vecRegularDemands = new ArrayList<RegularDemandsDO>();
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase _database = databaseHelper.getWritableDatabase();
        ////_database.beginTransaction();
		Cursor c = null;

		try {

			String quety="select * from Transactions where GNo=(select min(GNo) from Transactions) and (case when UploadFlag is null then '' else UploadFlag end not in ('P')  and case when UploadFlag is null then '' else UploadFlag end not in ('P'))";

			Log.v("","SelectAllFlagwise"+quety);
			c = _database.rawQuery(quety, null);

			if (vecRegularDemands != null) {
				vecRegularDemands.clear();
			}

			if (c.moveToFirst()) {
				do {
					RegularDemandsDO obj = new RegularDemandsDO();
					try {
						obj.MemberCode = c.getString(0);
						obj.MemberName = c.getString(1);
						obj.MLAI_ID = c.getString(2);
						obj.ReciptNumber = c.getString(3);
						obj.DemandDate = c.getString(4);
						obj.Attendance = c.getString(5);
						obj.collectedAmount = c.getString(6);
						obj.GNo = c.getString(7);
						obj.OSAmt = c.getString(8);
						obj.CName = c.getString(9);
						obj.GroupName = c.getString(10);
						obj.CNo = c.getString(11);
						obj.GLI = c.getString(12);

						obj.Lateness = c.getString(13);
						obj.InstallNo = c.getString(14);
						obj.Print = c.getString(15);
						obj.TransactionCode = c.getString(16);
						obj.CollType = c.getString(17);
						obj.meetingStartTime = c.getString(18);
						obj.meetingEndTime = c.getString(19);
						obj.FTODID = c.getString(20);
						obj.FTODReason = c.getString(21);
						obj.DemiseDate = c.getString(22);
						obj.APFlag = c.getString(23);
						obj.qom = c.getString(25);
						obj.probInCenter = c.getString(26);
						obj.groupDiscipline = c.getString(27);
						obj.collExp = c.getString(28);
						obj.collExpRMEL = c.getString(29);
						obj.collPlace = c.getString(30);
						obj.repaymentMadeBy = c.getString(31);
						obj.RenewFeed = c.getString(32);
						obj.langitude = c.getString(33);
						obj.latitude = c.getString(34);

						obj.TransactionID = c.getString(35);
						obj.Status = c.getString(36);
						obj.MobileNo = c.getString(38);
						obj.PaymentMode = c.getString(39);
						obj.DateTime = c.getString(42);
						obj.ImageName = c.getString(45);



					} catch (Exception e) {
						Log.e("exception", "while getting from server");
					}
					// CREATE TABLE "RegularDemands" ("CNo" VARCHAR PRIMARY KEY
					// NOT NULL , "CName" VARCHAR, "GNo" VARCHAR,
					// "EII_EMP_ID" VARCHAR, "GroupName" VARCHAR, "MemberCode"
					// VARCHAR, "MemberName" VARCHAR, "DemandDate" VARCHAR,
					// "MLAI_ID" VARCHAR, "OSAmt" VARCHAR, "DemandTotal"
					// VARCHAR, "ODAmount" VARCHAR, "Attendance" VARCHAR)

					vecRegularDemands.add(obj);
				} while (c.moveToNext());

				c.close();


			}
		} catch (Exception e) {
			e.printStackTrace();
		}
        finally {
            ////_database.endTransaction();
        }
		return vecRegularDemands;
	}


	public synchronized ArrayList<RegularDemandsDO> SelectAllFlagwiseLate(Context context) {
		ArrayList<RegularDemandsDO> vecRegularDemands = new ArrayList<RegularDemandsDO>();
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase _database = databaseHelper.getWritableDatabase();
        //_database.beginTransaction();
		Cursor c = null;

		try {

			String quety="select * from LateTransactions where Lateconformation='yes'  and GNo=(select min(GNo) from LateTransactions) and (case when UploadFlag is null then '' else UploadFlag end not in ('P')  and case when UploadFlag is null then '' else UploadFlag end not in ('P'))";

			Log.v("","SelectAllFlagwiseLate"+quety);
			c = _database.rawQuery(quety, null);

			if (vecRegularDemands != null) {
				vecRegularDemands.clear();
			}

			if (c.moveToFirst()) {
				do {
					RegularDemandsDO obj = new RegularDemandsDO();
					try {
						obj.MemberCode = c.getString(0);
						obj.MemberName = c.getString(1);
						obj.MLAI_ID = c.getString(2);
						obj.ReciptNumber = c.getString(3);
						obj.DemandDate = c.getString(4);
						obj.Attendance = c.getString(5);
						obj.GNo = c.getString(7);
						obj.OSAmt = c.getString(8);
						obj.CName = c.getString(9);
						obj.GroupName = c.getString(10);
						obj.CNo = c.getString(11);
						obj.GLI = c.getString(12);

						obj.Lateness = c.getString(13);
						obj.InstallNo = c.getString(14);
						obj.Print = c.getString(15);
						obj.TransactionCode = c.getString(16);
						obj.CollType = c.getString(17);
						obj.meetingStartTime = c.getString(18);
						obj.meetingEndTime = c.getString(19);
						obj.FTODID = c.getString(20);
						obj.FTODReason = c.getString(21);
						obj.DemiseDate = c.getString(22);
						obj.APFlag = c.getString(23);
						obj.qom = c.getString(25);
						obj.probInCenter = c.getString(26);
						obj.groupDiscipline = c.getString(27);
						obj.collExp = c.getString(28);
						obj.collExpRMEL = c.getString(29);
						obj.collPlace = c.getString(30);
						obj.repaymentMadeBy = c.getString(31);
						obj.RenewFeed = c.getString(32);
						obj.langitude = c.getString(33);
						obj.latitude = c.getString(34);

						obj.TransactionID = c.getString(35);
						obj.Status = c.getString(36);
						obj.MobileNo = c.getString(38);
						obj.PaymentMode = c.getString(39);
						obj.DateTime = c.getString(42);
						obj.ImageName = c.getString(45);

						obj.collectedAmount = c.getString(62);



					} catch (Exception e) {
						Log.e("exception", "while getting from server");
					}
					// CREATE TABLE "RegularDemands" ("CNo" VARCHAR PRIMARY KEY
					// NOT NULL , "CName" VARCHAR, "GNo" VARCHAR,
					// "EII_EMP_ID" VARCHAR, "GroupName" VARCHAR, "MemberCode"
					// VARCHAR, "MemberName" VARCHAR, "DemandDate" VARCHAR,
					// "MLAI_ID" VARCHAR, "OSAmt" VARCHAR, "DemandTotal"
					// VARCHAR, "ODAmount" VARCHAR, "Attendance" VARCHAR)

					vecRegularDemands.add(obj);
				} while (c.moveToNext());

				c.close();


			}
		} catch (Exception e) {
			e.printStackTrace();
		}
        finally {
           ////_database.endTransaction();
        }
		return vecRegularDemands;
	}

	public synchronized ArrayList<RegularDemandsDO> SelectAllTransaction(Context context) {
		ArrayList<RegularDemandsDO> vecRegularDemands = new ArrayList<RegularDemandsDO>();
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase _database = databaseHelper.getWritableDatabase();
        //_database.beginTransaction();
		Cursor c = null;

		try {

			String quety="select * from Transactions ";

			Log.v("","SelectAllFlagwise"+quety);
			c = _database.rawQuery(quety, null);

			if (vecRegularDemands != null) {
				vecRegularDemands.clear();
			}

			if (c.moveToFirst()) {
				do {
					RegularDemandsDO obj = new RegularDemandsDO();
					try {
						obj.MemberCode = c.getString(0);
						obj.MemberName = c.getString(1);
						obj.MLAI_ID = c.getString(2);
						obj.ReciptNumber = c.getString(3);
						obj.DemandDate = c.getString(4);
						obj.Attendance = c.getString(5);
						obj.collectedAmount = c.getString(6);
						obj.GNo = c.getString(7);
						obj.OSAmt = c.getString(8);
						obj.CName = c.getString(9);
						obj.GroupName = c.getString(10);
						obj.CNo = c.getString(11);
						obj.GLI = c.getString(12);

						obj.Lateness = c.getString(13);
						obj.InstallNo = c.getString(14);
						obj.Print = c.getString(15);
						obj.TransactionCode = c.getString(16);
						obj.CollType = c.getString(17);
						obj.meetingStartTime = c.getString(18);
						obj.meetingEndTime = c.getString(19);
						obj.FTODID = c.getString(20);
						obj.FTODReason = c.getString(21);
						obj.DemiseDate = c.getString(22);
						obj.APFlag = c.getString(23);
						obj.qom = c.getString(25);
						obj.probInCenter = c.getString(26);
						obj.groupDiscipline = c.getString(27);
						obj.collExp = c.getString(28);
						obj.collExpRMEL = c.getString(29);
						obj.collPlace = c.getString(30);
						obj.repaymentMadeBy = c.getString(31);
						obj.RenewFeed = c.getString(32);
						obj.langitude = c.getString(33);
						obj.latitude = c.getString(34);

						obj.TransactionID = c.getString(35);
						obj.Status = c.getString(36);
						obj.MobileNo = c.getString(38);
						obj.PaymentMode = c.getString(39);
						obj.DateTime = c.getString(42);
						obj.ImageName = c.getString(45);



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
           ////_database.endTransaction();
        }
		return vecRegularDemands;
	}



	public synchronized void UpdateRegCollection(String id,String flag,String time,Context context) {
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase _database = databaseHelper.getWritableDatabase();
        //_database.beginTransaction();
		try {
			String where = "GNo=?";
			String whereArgs[] = new String[] {id};
			try {
				ContentValues contentValues = new ContentValues();
				contentValues.put("UploadFlag",flag);
				contentValues.put("UploadTime",time);
                _database.update("Transactions ", contentValues, where, whereArgs);
               ////_database.setTransactionSuccessful();
			} catch (Exception e) {
				e.printStackTrace();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
        finally {
           ////_database.setTransactionSuccessful();
           ////_database.endTransaction();
          //  _database.close();
        }
	}
	public synchronized void UpdateRegCollectionLate(String id,String flag,String time,Context context) {
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase _database = databaseHelper.getWritableDatabase();
        //_database.beginTransaction();
		try {
			String where = "GNo=?";
			String whereArgs[] = new String[] {id};
			try {
				ContentValues contentValues = new ContentValues();
				contentValues.put("UploadFlag",flag);
				contentValues.put("UploadTime",time);
                _database.update("LateTransactions ", contentValues, where, whereArgs);
               ////_database.setTransactionSuccessful();
			} catch (Exception e) {
				e.printStackTrace();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
        finally {
           ////_database.setTransactionSuccessful();
           ////_database.endTransaction();
            //_database.close();
        }
	}

	public synchronized  void UpdateODCollection(String id,String flag,String time,Context context) {
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase _database = databaseHelper.getWritableDatabase();
        //_database.beginTransaction();
		try {

			//String where = "GNo=?";
			String where = "MLAIID=?";
			String whereArgs[] = new String[] {id};
			try {

				ContentValues contentValues = new ContentValues();

				contentValues.put("UploadFlag",flag);
				contentValues.put("UploadTime",time);

                _database.update("TransactionsOD ", contentValues, where, whereArgs);
               ////_database.setTransactionSuccessful();
			} catch (Exception e) {
				e.printStackTrace();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		finally {
           ////_database.setTransactionSuccessful();
           ////_database.endTransaction();
           // _database.close();
		}
	}

	public synchronized void UpdateAdvCollection(String id,String flag,String time,Context context) {
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase _database = databaseHelper.getWritableDatabase();
        //_database.beginTransaction();
		try {

			String where = "GroupCode=?";
			String whereArgs[] = new String[] {id};
			try {

				ContentValues contentValues = new ContentValues();

				contentValues.put("UploadFlag",flag);
				contentValues.put("UploadTime",time);

                _database.update("AdvanceTransactions ", contentValues, where, whereArgs);
               ////_database.setTransactionSuccessful();
			} catch (Exception e) {
				e.printStackTrace();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		finally {
           ////_database.setTransactionSuccessful();
           ////_database.endTransaction();
           // _database.close();
		}
	}

	public void  UpdateFlagTimeReg(Context context){
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase _database = databaseHelper.getWritableDatabase();
        //_database.beginTransaction();
		try{
			String query = "update Transactions set UploadFlag=null and UploadTime=null where UploadFlag='P'";

            SQLiteStatement sqLiteStatement = _database.compileStatement(query);
            sqLiteStatement.executeInsert();


		}catch (Exception e){
			e.printStackTrace();
		}
        finally {
           ////_database.setTransactionSuccessful();
           ////_database.endTransaction();
           // _database.close();
        }
	}
	public void  UpdateFlagTimeLate(Context context){
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase _database = databaseHelper.getWritableDatabase();
        //_database.beginTransaction();
		try{
			String query = "update LateTransactions set UploadFlag=null and UploadTime=null where UploadFlag='P'";

            SQLiteStatement sqLiteStatement = _database.compileStatement(query);
            sqLiteStatement.executeInsert();

		}catch (Exception e){
			e.printStackTrace();
		}
		finally {
           ////_database.setTransactionSuccessful();
           ////_database.endTransaction();
           // _database.close();
		}
	}

	public void  UpdateFlagTimeOD(Context context){
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase _database = databaseHelper.getWritableDatabase();
        //_database.beginTransaction();
		try{

			String query = "update TransactionsOD set UploadFlag=null and UploadTime=null where UploadFlag='P'";


            SQLiteStatement sqLiteStatement = _database.compileStatement(query);
            sqLiteStatement.executeInsert();

		}catch (Exception e){
			e.printStackTrace();
		}
		finally {
           ////_database.setTransactionSuccessful();
           ////_database.endTransaction();
           // _database.close();
		}
	}


	public void  UpdateFlagTimeAdvance(Context context){
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase _database = databaseHelper.getWritableDatabase();
        //_database.beginTransaction();
		try{

			String query = "update AdvanceTransactions set UploadFlag=null and UploadTime=null where UploadFlag='P'";

            SQLiteStatement sqLiteStatement = _database.compileStatement(query);
            sqLiteStatement.executeInsert();


        }catch (Exception e){
			e.printStackTrace();
		}
		finally {
           ////_database.setTransactionSuccessful();
           ////_database.endTransaction();
           // _database.close();
		}
	}

	public synchronized void InsertServerCollectionUploadData(String Modulename ,String Totalmembers,String UploadRecords,String UploadData,Context context
    ) {

		String date=GetDate();
		Log.v("","InsertServerUploadDate"+date);

        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase _database = databaseHelper.getWritableDatabase();
        //_database.beginTransaction();
		try {
			ContentValues contentValues =  new ContentValues();
			contentValues.put("ModuleName", Modulename);
			contentValues.put("TotalMembers", Totalmembers);
			contentValues.put("TotalUpload", UploadRecords);
			contentValues.put("UploadData", UploadData);
			contentValues.put("DateValue", date);

            _database.insert("CollectionServerUploadData", null, contentValues);
			//_database.setTransactionSuccessful();
		}catch(Exception e){
			e.printStackTrace();
		}
        finally {
           ////_database.endTransaction();
        }

    }

	public ArrayList<BaseDO>  offlineSavecountCollectionRG(String modulename,Context context)
	{
		String income="";
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase _database = databaseHelper.getReadableDatabase();
        //_database.beginTransaction();

        ArrayList<BaseDO> masterlist=new ArrayList<BaseDO>();
		Cursor c = null;
		try
		{
			String query="";
			query="Select * from CollectionServerUploadData where ModuleName='"+modulename+"'";
			if(query!=null&&!query.isEmpty())
			{
				Log.v("","TotalFamily"+query);
				c= _database.rawQuery(query, null);
				if(c.moveToFirst())
				{
					do
					{
						BaseDO master=new BaseDO();
						master.UploadMembers    =  c.getString(1);
						master.TotalUpload    =  c.getString(2);
						masterlist.add(master);
					}
					while(c.moveToNext());
					c.close();
				}

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
		return masterlist;
	}

	public ArrayList<BaseDO>  offlineSavecountCollectionLate(String modulename,Context context)
	{
		String income="";
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase _database = databaseHelper.getReadableDatabase();
        //_database.beginTransaction();
		ArrayList<BaseDO> masterlist=new ArrayList<BaseDO>();
		Cursor c = null;
		try
		{
			String query="";
			query="Select * from CollectionServerUploadData where ModuleName='"+modulename+"'";
			if(query!=null&&!query.isEmpty())
			{
				Log.v("","TotalFamily"+query);
				c= _database.rawQuery(query, null);
				if(c.moveToFirst())
				{
					do
					{
						BaseDO master=new BaseDO();
						master.UploadMembers    =  c.getString(1);
						master.TotalUpload    =  c.getString(2);
						masterlist.add(master);
					}
					while(c.moveToNext());
					c.close();
				}

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
		return masterlist;
	}

	public ArrayList<BaseDO>  offlineSavecountCollectionOD(String modulename,Context context)
	{
		String income="";
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase _database = databaseHelper.getReadableDatabase();
        //_database.beginTransaction();

		ArrayList<BaseDO> masterlist=new ArrayList<BaseDO>();
		Cursor c = null;
		try
		{
			String query="";

			query="Select * from CollectionServerUploadData where ModuleName='"+modulename+"' ";

			if(query!=null&&!query.isEmpty())
			{
				Log.v("","TotalFamily"+query);
				c= _database.rawQuery(query, null);
				if(c.moveToFirst())
				{
					do
					{
						BaseDO master=new BaseDO();
						master.UploadMembers    =  c.getString(1);
						master.TotalUpload    =  c.getString(2);
						masterlist.add(master);
					}
					while(c.moveToNext());
					c.close();
				}

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
		return masterlist;
	}
    public ArrayList<BaseDO>  offlineSavecountCollectionADV(String modulename,Context context)
    {
        String income="";
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase _database = databaseHelper.getReadableDatabase();
        //_database.beginTransaction();

        ArrayList<BaseDO> masterlist=new ArrayList<BaseDO>();
        Cursor c = null;
        try
        {
            String query="";
            query="Select * from CollectionServerUploadData where ModuleName='"+modulename+"' ";


            if(query!=null&&!query.isEmpty())
            {
                Log.v("","TotalFamily"+query);
                c= _database.rawQuery(query, null);
                if(c.moveToFirst())
                {
                    do
                    {
                        BaseDO master=new BaseDO();
                        master.UploadMembers    =  c.getString(1);
                        master.TotalUpload    =  c.getString(2);
                        masterlist.add(master);
                    }
                    while(c.moveToNext());
                    c.close();
                }

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
        return masterlist;
    }


	public void TruncatetabelLateTransaction(Context context) {

		DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
		SQLiteDatabase _database = databaseHelper.getWritableDatabase();
		//_database.beginTransaction();
		String query = null;

		query = "DELETE FROM LateTransactions ";
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

	public String GetDate()
	{
		Date endsd = new Date();
		String dateString = "";
		SimpleDateFormat sdfr = new SimpleDateFormat("yyyy-MM-dd");
		try {
			dateString = sdfr.format(endsd);
		} catch (Exception ex) {
			System.out.println(ex);
		}
		return  dateString;
	}
}
