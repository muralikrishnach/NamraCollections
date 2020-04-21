package com.jayam.impactapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.jayam.impactapp.objects.BaseDO;
import com.jayam.impactapp.objects.ODDemandsDO;

import java.util.ArrayList;

public class Transaction_OD_BL {
    private String ReCeiptNumber;
    private String Amount;

	private String BANKRRN;
	private String Response;
	private String Paymentmode;
	private String TXNID;
	private String MobileNo;

	private String BankName;
	private String TransactionTime;
	public String datetime="";

	public void Insert(String ReCeiptNumber, String Amount, BaseDO object, String BANKRRN, String Response, String Paymentmode, String TXNID, String MobileNo , String BankName, String TransactionTime, String datetime, Context context) {
		this.ReCeiptNumber = ReCeiptNumber;
		this.Amount = Amount;

		this.BANKRRN = BANKRRN;
		this.Response = Response;
		this.Paymentmode = Paymentmode;
		this.TXNID = TXNID;
		this.MobileNo = MobileNo;
		this.BankName = BankName;
		this.TransactionTime = TransactionTime;
		this.datetime = datetime;


        ODDemandsDO oddmdns = (ODDemandsDO) object;
        ContentValues values = new ContentValues();
        values.put("MemberCode", oddmdns.MMI_Code);
        values.put("MemberName", oddmdns.MMI_Name);
        values.put("MLAIID", oddmdns.MLAI_ID);
        values.put("ReCeiptNumber", ReCeiptNumber); //
        values.put("DemandDate", oddmdns.DemandDate);
        values.put("CollectionAmount", Amount);//
        values.put("GNo", oddmdns.MGI_Code);
        values.put("OSAmount", oddmdns.OSAmt);
        values.put("Centername", oddmdns.MCI_Name);
        values.put("GroupName", oddmdns.MGI_Name);
        values.put("CNo", oddmdns.MCI_Code);
        values.put("TransactionID", BANKRRN);
        values.put("Status",Response);
        values.put("PaymentMode",Paymentmode);
        values.put("TXNID",TXNID);
        values.put("MobileNo",MobileNo);
        values.put("bankName",BankName);
        values.put("TransactionTime",TransactionTime);
        values.put("EntryDate",datetime);


        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase _database = databaseHelper.getWritableDatabase();
        //_database.beginTransaction();

        try {

            _database.insert("TransactionsOD", null, values);
           ////_database.setTransactionSuccessful();
        } catch (Exception e) {
        }
        finally {
           ////_database.endTransaction();
        }
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

	query = "DELETE FROM TransactionsOD ";
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

	public void TruncatetabelServerUploadData(Context context) {
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase _database = databaseHelper.getWritableDatabase();
        //_database.beginTransaction();
		String query = null;

		query = "DELETE FROM CollectionServerUploadData ";
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
	public synchronized void TruncatetabelGroupId(String Groupid,Context context) {
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase _database = databaseHelper.getWritableDatabase();
        //_database.beginTransaction();
		String query = null;

		//query = "DELETE FROM TransactionsOD where GNo='"+Groupid+"' ";
		query = "DELETE FROM TransactionsOD where MLAIID='"+Groupid+"' ";
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
          //  _database.close();
        }
//		if (objSqliteDB != null) {
//			objSqliteDB.close();
//		}

	}

    public ArrayList<ODDemandsDO> SelectAll(Context context) {

	ArrayList<ODDemandsDO> vecRegularDemands = new ArrayList<ODDemandsDO>();
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase _database = databaseHelper.getReadableDatabase();
        //_database.beginTransaction();
	Cursor c = null;
	try {
	    c = _database.rawQuery("select * from TransactionsOD", null);

	    if (vecRegularDemands != null) {
		vecRegularDemands.clear();
	    }

	    if (c.moveToFirst()) {
		do {
		    // String amount = null;
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
			obj.APFlag = c.getString(14);
				obj.TransactionID = c.getString(15);
				obj.Status = c.getString(16);
				obj.MobileNo = c.getString(18);
				obj.PaymentMode = c.getString(19);
				obj.TXNID = c.getString(20);


		    } catch (Exception e) {
			Log.e("exception", "while getting from server");
		    }
		    // CREATE TABLE "TransactionsOD" ("MemberCode"
		    // VARCHAR,"MemberName" VARCHAR,"MLAIID"
		    // VARCHAR,"ReCeiptNumber"
		    // VARCHAR,"DemandDate" VARCHAR,"CollectionAmount" VARCHAR,
		    // "GNo" VARCHAR, "OSAmount" VARCHAR, "Centername"
		    // VARCHAR, "GroupName" VARCHAR)

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


	public synchronized ArrayList<ODDemandsDO> SelectAllFlagWise(Context context) {

		ArrayList<ODDemandsDO> vecRegularDemands = new ArrayList<ODDemandsDO>();
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase _database = databaseHelper.getReadableDatabase();
        //_database.beginTransaction();

		Cursor c = null;
		try {
			String query="";
			// query="select * from TransactionsOD where GNo=(select min(GNo) from TransactionsOD) and (case when UploadFlag is null then '' else UploadFlag end not in ('P')  and case when UploadFlag is null then '' else UploadFlag end not in ('P'))";

			query="select * from TransactionsOD where MLAIID=(select min(MLAIID) from TransactionsOD) and (case when UploadFlag is null then '' else UploadFlag end not in ('P')  and case when UploadFlag is null then '' else UploadFlag end not in ('P'))";

			c = _database.rawQuery(query, null);

			if (vecRegularDemands != null) {
				vecRegularDemands.clear();
			}

			if (c.moveToFirst()) {
				do {
					// String amount = null;
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
						obj.APFlag = c.getString(14);
						obj.TransactionID = c.getString(15);
						obj.Status = c.getString(16);
						obj.MobileNo = c.getString(18);
						obj.PaymentMode = c.getString(19);
						obj.TXNID = c.getString(20);
						obj.DateTime = c.getString(22);


					} catch (Exception e) {
						Log.e("exception", "while getting from server");
					}
					// CREATE TABLE "TransactionsOD" ("MemberCode"
					// VARCHAR,"MemberName" VARCHAR,"MLAIID"
					// VARCHAR,"ReCeiptNumber"
					// VARCHAR,"DemandDate" VARCHAR,"CollectionAmount" VARCHAR,
					// "GNo" VARCHAR, "OSAmount" VARCHAR, "Centername"
					// VARCHAR, "GroupName" VARCHAR)

					vecRegularDemands.add(obj);
				} while (c.moveToNext());

				c.close();


			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally {

            if(c != null )
            {
                c.close();
               ////_database.endTransaction();
            }
		}
		return vecRegularDemands;

	}


    public ArrayList<String> SelectAll(String MLAI_ID,Context context) {
	ArrayList<String> vecRegularDemands = new ArrayList<String>();
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase _database = databaseHelper.getReadableDatabase();
        //_database.beginTransaction();
	Cursor c = null;
	try {
	    c = _database.rawQuery("select CollectionAmount from TransactionsOD where MLAIID = '" + MLAI_ID + "'",
		    null);

	    if (vecRegularDemands != null) {
		vecRegularDemands.clear();
	    }

	    if (c.moveToFirst()) {
		do {
		    String amount = null;
		    try {

			amount = c.getString(0);
		    } catch (Exception e) {
			Log.e("exception", "while getting from server");
		    }

		    vecRegularDemands.add(amount);
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

    public ArrayList<ODDemandsDO> getAllDistinctCenters(Context context) {
	ArrayList<ODDemandsDO> vecRegularDemands = new ArrayList<ODDemandsDO>();
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase _database = databaseHelper.getReadableDatabase();
        //_database.beginTransaction();
	Cursor c = null;
	try {
	    // c= _database.rawQuery("select DISTINCT CNo ,Centername from
	    // TransactionsOD ", null);
	    c = _database.rawQuery(
		    "Select Centername,CNo,SUM(CollectionAmount) from TransactionsOD Group by Centername,CNo ", null);

	    if (vecRegularDemands != null) {
		vecRegularDemands.clear();
	    }

	    if (c.moveToFirst()) {
		do {
		    ODDemandsDO obj = new ODDemandsDO();
		    try {
			obj.MCI_Name = c.getString(0);
			obj.MCI_Code = c.getString(1);
			obj.collectedAmt = c.getString(2);

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

    public ArrayList<ODDemandsDO> getAllGroupsFromCenter(String CNO,Context context) {
	ArrayList<ODDemandsDO> vecRegularDemands = new ArrayList<ODDemandsDO>();
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase _database = databaseHelper.getReadableDatabase();
        //_database.beginTransaction();
	Cursor c = null;
	try {
	    c = _database.rawQuery("select GroupName,GNo,SUM(CollectionAmount) from TransactionsOD where CNo = '" + CNO
		    + "' Group by GroupName,GNo", null);

	    if (vecRegularDemands != null) {
		vecRegularDemands.clear();
	    }

	    if (c.moveToFirst()) {
		do {

		    ODDemandsDO obj = new ODDemandsDO();
		    try {
			obj.MGI_Name = c.getString(0);
			obj.MGI_Code = c.getString(1);
			obj.collectedAmt = c.getString(2);
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

    public String getrowCount(Context context) {
	String count = null;
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase _database = databaseHelper.getReadableDatabase();
        //_database.beginTransaction();
	Cursor c = null;
	try {
	    c = _database.rawQuery("SELECT count(*) from TransactionsOD", null);

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

    public ArrayList<ODDemandsDO> getAllMembersFromGroup(String GNO,Context context) {
	ArrayList<ODDemandsDO> vecRegularDemands = new ArrayList<ODDemandsDO>();
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase _database = databaseHelper.getReadableDatabase();
        //_database.beginTransaction();
	Cursor c = null;
	try {
	    c = _database.rawQuery(
		    "select DISTINCT MemberName,MLAIID,CollectionAmount from TransactionsOD where GNo = '" + GNO + "'",
		    null);

	    if (vecRegularDemands != null) {
		vecRegularDemands.clear();
	    }

	    if (c.moveToFirst()) {
		do {
		    ODDemandsDO obj = new ODDemandsDO();
		    try {
			obj.MMI_Name = c.getString(0);
			obj.MLAI_ID = c.getString(1);
			obj.collectedAmt = c.getString(2);
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

    public ArrayList<ODDemandsDO> GetMemberDetails(String MLAI_ID,Context context) {
	ArrayList<ODDemandsDO> vecRegularDemands = new ArrayList<ODDemandsDO>();

        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase _database = databaseHelper.getReadableDatabase();
        //_database.beginTransaction();
	Cursor c = null;
	try {
	    c = _database.rawQuery(
		    "select DISTINCT MLAIID, Centername, GroupName, MemberName, MemberCode, SUM(CollectionAmount) from TransactionsOD where MLAIID = '"
			    + MLAI_ID + "'",
		    null);

	    if (vecRegularDemands != null) {
		vecRegularDemands.clear();
	    }

	    if (c.moveToFirst()) {
		do {

		    ODDemandsDO obj = new ODDemandsDO();
		    try {
			obj.MLAI_ID = c.getString(0);
			obj.MCI_Name = c.getString(1);
			obj.MGI_Name = c.getString(2);
			obj.MMI_Name = c.getString(3);
			obj.MMI_Code = c.getString(4);
			obj.collectedAmt = c.getString(5);

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

    public ArrayList<String> getDetailsForSummaryReport(Context context) {
	String totCollectedAcounts = null, totalCollectedAmount = null, Demanddate = null;
	ArrayList<String> alSummaryreportDetails = new ArrayList<String>();
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase _database = databaseHelper.getReadableDatabase();
        //_database.beginTransaction();
	Cursor c = null;
	try {
	    c = _database.rawQuery(
		    "select  Distinct Count(MLAIID),  SUM(CollectionAmount) , DemandDate from TransactionsOD", null);

	    if (c.moveToFirst()) {
		do {
		    try {
			totCollectedAcounts = c.getString(0);
			totalCollectedAmount = c.getString(1);
			Demanddate = c.getString(2);
		    } catch (Exception e) {
			Log.e("exception", "while getting from server");
		    }
		    // CREATE TABLE "RegularDemands" ("CNo" VARCHAR PRIMARY KEY
		    // NOT NULL , "CName" VARCHAR, "GNo" VARCHAR,
		    // "EII_EMP_ID" VARCHAR, "GroupName" VARCHAR, "MemberCode"
		    // VARCHAR, "MemberName" VARCHAR, "DemandDate" VARCHAR,
		    // "MLAI_ID" VARCHAR, "OSAmt" VARCHAR, "DemandTotal"
		    // VARCHAR, "ODAmount" VARCHAR, "Attendance" VARCHAR)

		    alSummaryreportDetails.add(totCollectedAcounts);
		    alSummaryreportDetails.add(totalCollectedAmount);
		    alSummaryreportDetails.add(Demanddate);
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
	return alSummaryreportDetails;
    }

    public String updatePrintFlag(String TxnCode, String status,Context context) {
	String count = null;

        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase _database = databaseHelper.getReadableDatabase();
        //_database.beginTransaction();
	Cursor c = null;
	try {
	    c = _database.rawQuery(
		    "UPDATE TransactionsOD SET Print = '" + status + "' where ReCeiptNumber = '" + TxnCode + "'", null);

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

    public String getTotalCollectedAmount(Context context) {
	String totalODAmount = null;
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase _database = databaseHelper.getReadableDatabase();
        //_database.beginTransaction();
	Cursor c = null;
	try {
	    c = _database.rawQuery("select  SUM(CollectionAmount)  from TransactionsOD", null);

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
    }


	public synchronized String getTotalCollectedAmountGroupId(String groupno,Context context) {
		String totalODAmount = null;
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase _database = databaseHelper.getReadableDatabase();
        //_database.beginTransaction();
		Cursor c = null;
		try {
			//String query="select  SUM(CollectionAmount)  from TransactionsOD  where GNo='"+groupno+"'";
			String query="select  SUM(CollectionAmount)  from TransactionsOD  where MLAIID='"+groupno+"'";

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
               ////_database.endTransaction();
            }
        }
		return totalODAmount;
	}

    public String getNumberOfAccouts(Context context) {
	String totalODAmount = null;
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase _database = databaseHelper.getReadableDatabase();
        //_database.beginTransaction();

        Cursor c = null;
	try {
	    c = _database.rawQuery("select  count(*)  from TransactionsOD", null);

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
    }


	public synchronized String getNumberOfAccoutsGroupId(String groupno,Context context) {
		String totalODAmount = null;
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase _database = databaseHelper.getReadableDatabase();
        //_database.beginTransaction();
		Cursor c = null;
		try {
			//String query="select  count(*)  from TransactionsOD where GNo='"+groupno+"'";
			//String query="select  count(*)  from TransactionsOD where MLAIID='"+groupno+"'";
			String query="select  count(*)  from TransactionsOD ";
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
               ////_database.endTransaction();
            }
        }
		return totalODAmount;
	}

    public String updateActualPrintFlag(String TxnCode, String status,Context context) {
	String count = null;
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase _database = databaseHelper.getReadableDatabase();
        //_database.beginTransaction();

        Cursor c = null;
	String query = "UPDATE TransactionsOD SET APFlag = '" + status + "' where ReCeiptNumber = '" + TxnCode + "'";
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
    finally{
        if(c != null )
        {
            c.close();
           ////_database.endTransaction();
        }
    }
	return count;
    }
}
