package com.jayam.impactapp.database;

import java.util.ArrayList;

import com.jayam.impactapp.objects.BaseDO;
import com.jayam.impactapp.objects.ODDemandsDO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

public class ODDemandsBL  {

    private ArrayList<ODDemandsDO> alloddemands;
    private float amttobecollected;



    public void InsertArrayList(ArrayList<ODDemandsDO> alregulardemnads, Context context) {
	this.alloddemands = alregulardemnads;

		DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
		SQLiteDatabase _database = databaseHelper.getWritableDatabase();
		//_database.beginTransaction();
	for (int i = 0; i < alloddemands.size(); i++) {
	    ODDemandsDO oddmdns = alloddemands.get(i);
		ContentValues values = new ContentValues();
		values.put("MCI_Name", oddmdns.MCI_Name);
		values.put("MCI_Code", oddmdns.MCI_Code);
		values.put("MGI_Name", oddmdns.MGI_Name);
		values.put("MGI_Code", oddmdns.MGI_Code);
		values.put("MMI_Name", oddmdns.MMI_Name);
		values.put("MMI_Code", oddmdns.MMI_Code);
		values.put("MLAI_ID", oddmdns.MLAI_ID);
		values.put("ODAmt", oddmdns.ODAmt);
		values.put("DemandDate", oddmdns.DemandDate);
		values.put("OSAmt", oddmdns.OSAmt);
		values.put("SittingOrder", oddmdns.SittingOrder);


		values.put("AAdharNO", oddmdns.AAdharNo);
		values.put("MobileNo", oddmdns.MobileNo);

		values.put("BranchPaymode", oddmdns.BranchPaymode);
		values.put("ProductId", oddmdns.ProductName);
		values.put("LatitudeCenter", oddmdns.LatitudeCenter);
		values.put("LongitudeCenter", oddmdns.LongitudeCenter);
		values.put("LatitudeGroup", oddmdns.LatitudeGroup);
		values.put("LongitudeGroup", oddmdns.LongitudeGroup);
		values.put("LatitudeMember", oddmdns.LatitudeMember);
		values.put("LongitudeMember", oddmdns.LongitudeMember);



		try {

			_database.insert("ODDemands", null, values);
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

    public void Truncatetabel(Context context) {
		DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
		SQLiteDatabase _database = databaseHelper.getWritableDatabase();
		//_database.beginTransaction();
	String query = null;

	query = "DELETE FROM ODDemands";
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
	public synchronized  void TruncatetabelGroupId(String Groupid,Context context) {
		DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
		SQLiteDatabase _database = databaseHelper.getWritableDatabase();
		//_database.beginTransaction();

		String query = null;

		//query = "DELETE FROM ODDemands where MGI_Code='"+Groupid+"' ";
		query = "DELETE FROM ODDemands where MLAI_ID='"+Groupid+"' ";
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
//			if (objSqliteDB != null) {
//				objSqliteDB.close();
//
//			}



	}

    public ArrayList<ODDemandsDO> SelectAll(Context context) {
	ArrayList<ODDemandsDO> vecRegularDemands = new ArrayList<ODDemandsDO>();
		DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
		SQLiteDatabase _database = databaseHelper.getReadableDatabase();
		//_database.beginTransaction();
	Cursor c = null;
	try {
	    c = _database.rawQuery("select * from ODDemands", null);

	    if (vecRegularDemands != null) {
		vecRegularDemands.clear();
	    }

	    if (c.moveToFirst()) {
		do {
		    ODDemandsDO obj = new ODDemandsDO();
		    try {
			obj.MCI_Name = c.getString(0);
			obj.MCI_Code = c.getString(1);
			obj.MGI_Name = c.getString(2);
			obj.MGI_Code = c.getString(3);
			obj.MMI_Name = c.getString(4);
			obj.MMI_Code = c.getString(5);
			obj.MLAI_ID = c.getString(6);
			obj.ODAmt = c.getString(7);
			obj.DemandDate = c.getString(8);
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
			//_database.endTransaction();
		}

	}
	return vecRegularDemands;
    }

    public String SelectCount(Context context) {
	String count = null;
		DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
		SQLiteDatabase _database = databaseHelper.getReadableDatabase();
		//_database.beginTransaction();
	Cursor c = null;
	try {
	    c = _database.rawQuery("select count(*) from ODDemands", null);

	    if (c.moveToFirst()) {
		do {
		    ODDemandsDO obj = new ODDemandsDO();
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
			//_database.endTransaction();
		}

	}
	return count;
    }

    public ArrayList<ODDemandsDO> SelectAllCenters(Context context) {
	ArrayList<ODDemandsDO> vecRegularDemands = new ArrayList<ODDemandsDO>();
		DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
		SQLiteDatabase _database = databaseHelper.getReadableDatabase();
		//_database.beginTransaction();
	Cursor c = null;
	try {
		c = _database.rawQuery("select DISTINCT MCI_Name, MCI_Code,LatitudeCenter,LongitudeCenter,LatitudeGroup,LongitudeGroup,LatitudeMember,LongitudeMember,DemandDate from ODDemands", null);

	    if (vecRegularDemands != null) {
		vecRegularDemands.clear();
	    }

	    if (c.moveToFirst()) {
		do {
		    ODDemandsDO obj = new ODDemandsDO();
		    try {
			obj.MCI_Name = c.getString(0);
			obj.MCI_Code = c.getString(1);
				obj.LatitudeCenter = c.getString(2);
				obj.LongitudeCenter = c.getString(3);
				obj.LatitudeGroup = c.getString(4);
				obj.LongitudeGroup = c.getString(5);
				obj.LatitudeMember = c.getString(6);
				obj.LongitudeMember = c.getString(7);
				obj.DemandDate = c.getString(8);

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

    public ArrayList<ODDemandsDO> SelectAllGroups(String centerCode,Context context) {
	ArrayList<ODDemandsDO> vecRegularDemands = new ArrayList<ODDemandsDO>();
		DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
		SQLiteDatabase _database = databaseHelper.getReadableDatabase();
		//_database.beginTransaction();
	Cursor c = null;
	try {
		c = _database.rawQuery(
				"select DISTINCT MGI_Name, MGI_Code,LatitudeCenter,LongitudeCenter,LatitudeGroup,LongitudeGroup,LatitudeMember,LongitudeMember ,DemandDate from ODDemands where MCI_Code = '" + centerCode + "'", null);


		if (vecRegularDemands != null) {
		vecRegularDemands.clear();
	    }

	    if (c.moveToFirst()) {
		do {
		    ODDemandsDO obj = new ODDemandsDO();
		    try {
			obj.MGI_Name = c.getString(0);
			obj.MGI_Code = c.getString(1);
				obj.LatitudeCenter = c.getString(2);
				obj.LongitudeCenter = c.getString(3);
				obj.LatitudeGroup = c.getString(4);
				obj.LongitudeGroup = c.getString(5);
				obj.LatitudeMember = c.getString(6);
				obj.LongitudeMember = c.getString(7);
				obj.DemandDate = c.getString(8);
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
			//_database.endTransaction();
		}

	}
	return vecRegularDemands;
    }

    public ArrayList<ODDemandsDO> SelectAllMembers(String groupcode,Context context) {
	ArrayList<ODDemandsDO> vecRegularDemands = new ArrayList<ODDemandsDO>();
		DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
		SQLiteDatabase _database = databaseHelper.getReadableDatabase();
		//_database.beginTransaction();
	Cursor c = null;
	try {
	    c = _database.rawQuery("select DISTINCT MMI_Name, MMI_Code,MLAI_ID,DemandDate from ODDemands where MGI_Code = '"
		    + groupcode + "' order by SittingOrder", null);

	    if (vecRegularDemands != null) {
		vecRegularDemands.clear();
	    }

	    if (c.moveToFirst()) {
		do {
		    ODDemandsDO obj = new ODDemandsDO();
		    try {
			obj.MMI_Name = c.getString(0);
			obj.MMI_Code = c.getString(1);
			obj.MLAI_ID = c.getString(2);
			obj.DemandDate = c.getString(3);
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

    public ArrayList<ODDemandsDO> SelectAllMemberDetails(String membercode,Context context) {
	ArrayList<ODDemandsDO> vecRegularDemands = new ArrayList<ODDemandsDO>();

		DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
		SQLiteDatabase _database = databaseHelper.getReadableDatabase();
		//_database.beginTransaction();
	Cursor c = null;
	try {
	    c = _database.rawQuery("select * from ODDemands where MMI_Code = '" + membercode + "'", null);

	    if (vecRegularDemands != null) {
		vecRegularDemands.clear();
	    }

	    if (c.moveToFirst()) {
		do {
		    ODDemandsDO obj = new ODDemandsDO();
		    try {
			obj.MCI_Name = c.getString(0);
			obj.MCI_Code = c.getString(1);
			obj.MGI_Name = c.getString(2);
			obj.MGI_Code = c.getString(3);
			obj.MMI_Name = c.getString(4);
			obj.MMI_Code = c.getString(5);
			obj.MLAI_ID = c.getString(6);
			obj.ODAmt = c.getString(7);
			obj.DemandDate = c.getString(8);
			obj.OSAmt = c.getString(9);
			obj.SittingOrder = c.getString(10);
				obj.AAdharNo = c.getString(11);
				obj.MobileNo = c.getString(12);
				obj.BranchPaymode = c.getString(15);
				obj.ProductName = c.getString(16);

				obj.LatitudeCenter = c.getString(18);
				obj.LongitudeCenter = c.getString(19);
				obj.LatitudeGroup = c.getString(20);
				obj.LongitudeGroup = c.getString(21);
				obj.LatitudeMember = c.getString(22);
				obj.LongitudeMember = c.getString(23);
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
			//_database.endTransaction();
		}

	}

		return vecRegularDemands;
    }

    public ArrayList<String> getDetailsForSummaryReport(Context context) {
	String totalDemands = null, totalODAmount = null;
	ArrayList<String> alSummaryreportDetails = new ArrayList<String>();
		DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
		SQLiteDatabase _database = databaseHelper.getReadableDatabase();
		//_database.beginTransaction();
	Cursor c = null;
	try {
	    c = _database.rawQuery("select Count(MLAI_ID),  SUM(ODAmt) from ODDemands", null);

	    if (c.moveToFirst()) {
		do {
		    try {
			totalDemands = c.getString(0);
			totalODAmount = c.getString(1);

		    } catch (Exception e) {
			Log.e("exception", "while getting from server");
		    }
		    // CREATE TABLE "RegularDemands" ("CNo" VARCHAR PRIMARY KEY
		    // NOT NULL , "CName" VARCHAR, "GNo" VARCHAR,
		    // "EII_EMP_ID" VARCHAR, "GroupName" VARCHAR, "MemberCode"
		    // VARCHAR, "MemberName" VARCHAR, "DemandDate" VARCHAR,
		    // "MLAI_ID" VARCHAR, "OSAmt" VARCHAR, "DemandTotal"
		    // VARCHAR, "ODAmount" VARCHAR, "Attendance" VARCHAR)

		    alSummaryreportDetails.add(totalDemands);
		    alSummaryreportDetails.add(totalODAmount);
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

		return alSummaryreportDetails;
    }

    public String SelectMemberCount(String MLAIID,Context context) {
	String count = null;
	// SQLiteDatabase _database = DatabaseHelper.closedatabase();

		DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
		SQLiteDatabase _database = databaseHelper.getReadableDatabase();
		//_database.beginTransaction();
	Cursor c = null;
	try {
	    c = _database.rawQuery("select count(*) from TransactionsOD where MLAIID='" + MLAIID + "'", null);
	    Log.e("Query:-----", "select count(*) from TransactionsOD where MLAIID='" + MLAIID + "'");

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






	public String SelectCashODrCountOD(String MLAIID,Context context) {
		String count = null;
		// SQLiteDatabase _database = DatabaseHelper.closedatabase();
		DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
		SQLiteDatabase _database = databaseHelper.getReadableDatabase();
		//_database.beginTransaction();
		Cursor c = null;
		try {
			String query="select count(*) from TransactionsOD where MLAIID='" + MLAIID + " and  PaymentMode in('CashLess','Cash')";
			c = _database.rawQuery(query, null);
			Log.v("SelectCashODrCount:-", ""+query);

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

    public String getODAmt(String MLAI_ID,Context context) {
	String ODAmt = null;

		DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
		SQLiteDatabase _database = databaseHelper.getReadableDatabase();
		//_database.beginTransaction();
	Cursor c = null;
	try {
	    c = _database.rawQuery("select ODAmt from ODDemands where MLAI_ID = '" + MLAI_ID + "'", null);

	    if (c.moveToFirst()) {
		do {
		    try {
			ODAmt = c.getString(0);
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

		return ODAmt;
    }

    public ArrayList<ODDemandsDO> SelectAllTransactions(String lastTXN,Context context) {
	// TODO Auto-generated method stub
	ArrayList<ODDemandsDO> vecRegularDemands = new ArrayList<ODDemandsDO>();
		DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
		SQLiteDatabase _database = databaseHelper.getReadableDatabase();
		//_database.beginTransaction();
	Cursor c = null;
	try {
	    c = _database.rawQuery("select * from TransactionsOD where ReCeiptNumber='" + lastTXN + "'", null);
	    Log.e("Query:--", "select * from TransactionsOD where ReCeiptNumber='" + lastTXN + "'");
	    if (vecRegularDemands != null) {
		vecRegularDemands.clear();
	    }
	    if (c.moveToFirst()) {
		do {
		    ODDemandsDO obj = new ODDemandsDO();
		    try {
			obj.MMI_Code = c.getString(0);
			obj.MMI_Name = c.getString(1);
			obj.MLAI_ID = c.getString(2);
			obj.recieptNumber = c.getString(3);
			obj.DemandDate = c.getString(4);
			obj.collectedAmt = c.getString(5);
			obj.MGI_Code = c.getString(6);
			obj.OSAmt = c.getString(7);
			obj.MCI_Name = c.getString(8);
			obj.MGI_Name = c.getString(9);
			obj.MCI_Code = c.getString(10);
			obj.printFlag = c.getString(11);
			obj.DupNO = c.getString(12);
			obj.CollType = c.getString(13);
			obj.APFlag = c.getString(14);
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

}
