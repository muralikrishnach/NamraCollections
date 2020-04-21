package com.jayam.impactapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.jayam.impactapp.objects.AdvaceDemandDO;
import com.jayam.impactapp.objects.BaseDO;
import com.jayam.impactapp.objects.ODDemandsDO;
import com.jayam.impactapp.objects.RegularDemandsDO;
import com.jayam.impactapp.objects.cbsearchgrid;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class RegularDemandsBL  {
	private ArrayList<RegularDemandsDO> alregulardemnads;
	private float amountTobeCollected;
	private ConcurrentHashMap firstgrid;




	public boolean Insert(BaseDO object, Context context) {
		RegularDemandsDO parametrsDO = (RegularDemandsDO) object;



		Log.v("","pmember"+parametrsDO.MemberName);
		Log.v("","CNamep"+parametrsDO.CName);

		ContentValues values = new ContentValues();
		values.put("CNo", parametrsDO.CNo);
		values.put("CName", parametrsDO.CName);
		values.put("GNo", parametrsDO.GNo);
		values.put("EII_EMP_ID", parametrsDO.EII_EMP_ID);
		values.put("GroupName", parametrsDO.GroupName);
		values.put("MemberCode", parametrsDO.MemberCode);
		values.put("MemberName", parametrsDO.MemberName);
		values.put("DemandDate", parametrsDO.DemandDate);
		values.put("MLAI_ID", parametrsDO.MLAI_ID);
		values.put("OSAmt", parametrsDO.OSAmt);
		values.put("DemandTotal", parametrsDO.DemandTotal);
		values.put("ODAmount", parametrsDO.ODAmount);
		values.put("Attendance", parametrsDO.Attendance);
		values.put("GLI", parametrsDO.GLI);
		values.put("Lateness", parametrsDO.Lateness);
		values.put("InstallNo", parametrsDO.InstallNo);
		values.put("SittingOrder", parametrsDO.SittingOrder);
		values.put("collectedamount", "0");
		values.put("SO", parametrsDO.SO);
		values.put("NextRepayDate", parametrsDO.NextRepayDate);
		values.put("Renew", parametrsDO.Renew);

		values.put("AAdharNO", parametrsDO.AAdharNo);
		values.put("MobileNo", parametrsDO.MobileNo);

		values.put("BranchPaymode", parametrsDO.BranchPaymode);
		values.put("ProductId", parametrsDO.ProductName);
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

        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase _database = databaseHelper.getWritableDatabase();
        //_database.beginTransaction();


		try {
			_database.insert("RegularDemands", null, values);
			//_database.setTransactionSuccessful();
		} catch (Exception e) {
		}
        finally{
           ////_database.endTransaction();

        }
		return false;
	}


	public boolean Update(BaseDO object) {
		return false;
	}

	public void update(ArrayList<RegularDemandsDO> alArrayList,Context context) {
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase _database = databaseHelper.getWritableDatabase();
        //_database.beginTransaction();
		RegularDemandsDO obj;
		String query = null;
		for (int i = 0; i < alArrayList.size(); i++) {
			obj = alArrayList.get(i);

			query = "UPDATE RegularDemands SET Confirmed = 'CONFIRMED', collectedamount = '" + obj.updated + "',"
					+ "Attendance ='" + obj.Attendance + "', GLI = '" + obj.GLI + "', Lateness = '" + obj.Lateness + "'"
					+ " where MLAI_ID = '" + obj.MLAI_ID + "'";
			Log.e("query", query);
			try {
                SQLiteStatement sqLiteStatement = _database.compileStatement(query);
                sqLiteStatement.executeInsert();

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	public void updateReciptNumbers(String MALID, String recpNumber,Context context) {

        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase _database = databaseHelper.getWritableDatabase();
        //_database.beginTransaction();
		String query = null;

		query = "UPDATE RegularDemands SET ReceiptNumber = '" + recpNumber + "' where MLAI_ID = '" + MALID + "'";
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

	public void updateCollectedAmt(String MALID, float Amt,Context context) {
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase _database = databaseHelper.getWritableDatabase();
        //_database.beginTransaction();
		String query = null;

		query = "UPDATE RegularDemands SET collectedamount = '" + Amt + "' where MLAI_ID = '" + MALID + "'";
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

	public String SelectCount(Context context) {
		String count = null;

        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase _database = databaseHelper.getReadableDatabase();
        //_database.beginTransaction();
		// _database = DatabaseHelper.openDataBase();
		Cursor c = null;
		try {
			c = _database.rawQuery("select count(*) from RegularDemands", null);
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

	public String SelectCountLate(Context context) {
		String count = null;
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase _database = databaseHelper.getReadableDatabase();
        //_database.beginTransaction();
		// _database = DatabaseHelper.openDataBase();
		Cursor c = null;
		try {
			c = _database.rawQuery("select count(*) from LateTransactions", null);
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


	public boolean Delete(BaseDO object) {
		return false;
	}

	public void Truncatetabel(Context context) {

        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase _database = databaseHelper.getWritableDatabase();
        //_database.beginTransaction();
		String query = null;

		query = "DELETE FROM RegularDemands ";
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

	}








	public ArrayList<RegularDemandsDO> SelectAll(Context context) {
		ArrayList<RegularDemandsDO> vecRegularDemands = new ArrayList<RegularDemandsDO>();
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase _database = databaseHelper.getReadableDatabase();
        //_database.beginTransaction();
		Cursor c = null;
		try {
			c = _database.rawQuery("select * from RegularDemands", null);
			if (vecRegularDemands != null) {
				vecRegularDemands.clear();
			}
			if (c.moveToFirst()) {
				do {
					RegularDemandsDO obj = new RegularDemandsDO();
					try {
						obj.CNo = c.getString(0);
						obj.CName = c.getString(1);
						obj.GNo = c.getString(2);
						obj.EII_EMP_ID = c.getString(3);
						obj.GroupName = c.getString(4);
						obj.MemberCode = c.getString(5);
						obj.MemberName = c.getString(6);
						obj.DemandDate = c.getString(7);
						obj.MLAI_ID = c.getString(8);
						obj.OSAmt = c.getString(9);
						obj.DemandTotal = c.getString(10);
						obj.ODAmount = c.getString(11);
						obj.Attendance = c.getString(12);
						obj.GLI = c.getString(13);
						obj.Lateness = c.getString(13);
						obj.collectedAmount = c.getString(15);
						obj.Confirmed = c.getString(16);
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




	public ArrayList<RegularDemandsDO> SelectAllMemberGrid(String membercode,Context context) {
		ArrayList<RegularDemandsDO> vecRegularDemands = new ArrayList<RegularDemandsDO>();
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase _database = databaseHelper.getReadableDatabase();
        //_database.beginTransaction();
		Cursor c = null;
		try {

			String query="select * from Transactions  where PaymentMode='CashLess' and Status!='Request Completed'  and MemberCode='"+membercode+"'";


			Log.v("","SelectAllMemberGrid"+query);

			c = _database.rawQuery(query, null);

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
						obj.PaymentMode = c.getString(39);
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



	public ArrayList<ODDemandsDO> SelectAllMeberdetailsOD(String memberid,Context context) {
		ArrayList<ODDemandsDO> vecRegularDemands = new ArrayList<ODDemandsDO>();
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase _database = databaseHelper.getReadableDatabase();
        //_database.beginTransaction();
		Cursor c = null;
		try {

			String query="select * from TransactionsOD  where PaymentMode='CashLess' and Status!='Request Completed'  and MemberCode='"+memberid+"'";


			Log.v("","SelectAllMeberdetailsOD"+query);

			c = _database.rawQuery(query, null);

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
						obj.APFlag = c.getString(14);
						obj.TransactionID = c.getString(15);
						obj.Status = c.getString(16);
						obj.PaymentMode = c.getString(19);
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

	//SelectAllMeberdetailsAdvance//

    public ArrayList<AdvaceDemandDO> SelectAllMeberdetailsAdvance(String memberid,Context context) {
        ArrayList<AdvaceDemandDO> vecRegularDemands = new ArrayList<AdvaceDemandDO>();
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase _database = databaseHelper.getReadableDatabase();
        //_database.beginTransaction();
        Cursor c = null;
        try {

            String query="select * from AdvanceTransactions  where PaymentMode='CashLess' and Status!='Request Completed'  and MemeberCode='"+memberid+"'";


            Log.v("","SelectAllMeberdetailsOD"+query);

            c = _database.rawQuery(query, null);

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

	public ArrayList<RegularDemandsDO> SelectAllCenterCashlesspending(Context context) {
		ArrayList<RegularDemandsDO> vecRegularDemands = new ArrayList<RegularDemandsDO>();
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase _database = databaseHelper.getReadableDatabase();
        //_database.beginTransaction();
		Cursor c = null;
		try {

			String query="select CNo,Centername from Transactions  where PaymentMode='CashLess' and Status!='Request Completed' ";

			Log.v("","SelectAllCenterCashlesspending"+query);
			c = _database.rawQuery(query, null);

			if (vecRegularDemands != null) {
				vecRegularDemands.clear();
			}

			if (c.moveToFirst()) {
				do {
					RegularDemandsDO obj = new RegularDemandsDO();
					try {
						obj.CNo = c.getString(0);
						obj.CName = c.getString(1);

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

	public ArrayList<ODDemandsDO> SelectAllCenterCashlesspendingOD(Context context) {
		ArrayList<ODDemandsDO> vecRegularDemands = new ArrayList<ODDemandsDO>();
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase _database = databaseHelper.getReadableDatabase();
        //_database.beginTransaction();
		Cursor c = null;
		try {

			String query="select CNo ,Centername from TransactionsOD  where PaymentMode='CashLess' and Status!='Request Completed' ";

			Log.v("","SelectAllCenterCashlesspending"+query);
			c = _database.rawQuery(query, null);

			if (vecRegularDemands != null) {
				vecRegularDemands.clear();
			}

			if (c.moveToFirst()) {
				do {
					ODDemandsDO obj = new ODDemandsDO();
					try {
						obj.MCI_Code = c.getString(0);
						obj.MCI_Name = c.getString(1);

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
//advancecashlesspending//


    public ArrayList<AdvaceDemandDO> SelectAllAdvanceCashlesspending(Context context) {
        ArrayList<AdvaceDemandDO> vecRegularDemands = new ArrayList<AdvaceDemandDO>();
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase _database = databaseHelper.getReadableDatabase();
        //_database.beginTransaction();
        Cursor c = null;
        try {

            String query="select CenterCode ,CenterName from AdvanceTransactions  where PaymentMode='CashLess' and Status!='Request Completed' ";

            Log.v("","SelectAllCenterCashlesspending"+query);
            c = _database.rawQuery(query, null);

            if (vecRegularDemands != null) {
                vecRegularDemands.clear();
            }

            if (c.moveToFirst()) {
                do {
                    AdvaceDemandDO obj = new AdvaceDemandDO();
                    try {
                        obj.MCI_Code = c.getString(0);
                        obj.CenterName = c.getString(1);

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
	public ArrayList<RegularDemandsDO> SelectAllGroupCashlesspending(String gno,Context context) {
		ArrayList<RegularDemandsDO> vecRegularDemands = new ArrayList<RegularDemandsDO>();
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase _database = databaseHelper.getReadableDatabase();
        //_database.beginTransaction();
		Cursor c = null;
		try {

			String query="select GNo,GroupName from Transactions  where PaymentMode='CashLess' and Status!='Request Completed'  and CNo='"+gno+"'";

			Log.v("","SelectAllGroupCashlesspending"+query);
			c = _database.rawQuery(query, null);

			if (vecRegularDemands != null) {
				vecRegularDemands.clear();
			}

			if (c.moveToFirst()) {
				do {
					RegularDemandsDO obj = new RegularDemandsDO();
					try {
						obj.GNo = c.getString(0);

						obj.GroupName = c.getString(1);

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
	public ArrayList<ODDemandsDO> SelectAllGroupCashlesspendingOD(String gno,Context context) {
		ArrayList<ODDemandsDO> vecRegularDemands = new ArrayList<ODDemandsDO>();
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase _database = databaseHelper.getReadableDatabase();
        //_database.beginTransaction();
		Cursor c = null;
		try {

			String query="select GNo,GroupName from TransactionsOD  where PaymentMode='CashLess' and Status!='Request Completed'  and CNo='"+gno+"'";

			Log.v("","SelectAllGroupCashlesspending"+query);
			c = _database.rawQuery(query, null);

			if (vecRegularDemands != null) {
				vecRegularDemands.clear();
			}

			if (c.moveToFirst()) {
				do {
					ODDemandsDO obj = new ODDemandsDO();
					try {
						obj.MGI_Code = c.getString(0);

						obj.MGI_Name = c.getString(1);

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
	//SelectAllGroupCashlesspendingadvance//
    public ArrayList<AdvaceDemandDO> SelectAllGroupCashlesspendingadvance(String gno,Context context) {
        ArrayList<AdvaceDemandDO> vecRegularDemands = new ArrayList<AdvaceDemandDO>();
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase _database = databaseHelper.getReadableDatabase();
        //_database.beginTransaction();
        Cursor c = null;
        try {

            String query="select GroupCode,GrouprName from AdvanceTransactions  where PaymentMode='CashLess' and Status!='Request Completed'  and CenterCode='"+gno+"'";

            Log.v("","SelectAllGroupCashlesspending"+query);
            c = _database.rawQuery(query, null);

            if (vecRegularDemands != null) {
                vecRegularDemands.clear();
            }

            if (c.moveToFirst()) {
                do {
                    AdvaceDemandDO obj = new AdvaceDemandDO();
                    try {
                        obj.MGI_Code = c.getString(0);

                        obj.MGI_Name = c.getString(1);

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
	public ArrayList<RegularDemandsDO> SelectAllMemberCashlesspending(String gno,Context context) {
		ArrayList<RegularDemandsDO> vecRegularDemands = new ArrayList<RegularDemandsDO>();
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase _database = databaseHelper.getReadableDatabase();
        //_database.beginTransaction();
		Cursor c = null;
		try {

			String query="select MemberCode,MemberName from Transactions  where PaymentMode='CashLess' and Status!='Request Completed'  and GNo='"+gno+"'";

			Log.v("","SelectAllMemberCashlesspending"+query);
			c = _database.rawQuery(query, null);

			if (vecRegularDemands != null) {
				vecRegularDemands.clear();
			}

			if (c.moveToFirst()) {
				do {
					RegularDemandsDO obj = new RegularDemandsDO();
					try {
						obj.MemberCode = c.getString(5);
						obj.MemberName = c.getString(6);

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





	public ArrayList<ODDemandsDO> SelectAllMemberCashlesspendingODMEMBER(String gno,Context context) {
		ArrayList<ODDemandsDO> vecRegularDemands = new ArrayList<ODDemandsDO>();

        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase _database = databaseHelper.getReadableDatabase();
        //_database.beginTransaction();

        Cursor c = null;
		try {

			String query="select MemberCode,MemberName from TransactionsOD  where PaymentMode='CashLess' and Status!='Request Completed'  and GNo='"+gno+"'";

			Log.v("","SelectAllMemberCashlesspending"+query);
			c = _database.rawQuery(query, null);

			if (vecRegularDemands != null) {
				vecRegularDemands.clear();
			}

			if (c.moveToFirst()) {
				do {
					ODDemandsDO obj = new ODDemandsDO();
					try {
						obj.MMI_Code = c.getString(0);
						obj.MMI_Name = c.getString(1);

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
	//SelectAllMemberCashlesspendingadvanceMEMBER//

    public ArrayList<AdvaceDemandDO> SelectAllMemberCashlesspendingadvanceMEMBER(String gno,Context context) {
        ArrayList<AdvaceDemandDO> vecRegularDemands = new ArrayList<AdvaceDemandDO>();
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase _database = databaseHelper.getReadableDatabase();
        //_database.beginTransaction();
        Cursor c = null;
        try {

            String query="select MemeberCode,MemberName from AdvanceTransactions  where PaymentMode='CashLess' and Status!='Request Completed'  and GroupCode='"+gno+"'";

            Log.v("","SelectAllMemberCashlesspending"+query);
            c = _database.rawQuery(query, null);

            if (vecRegularDemands != null) {
                vecRegularDemands.clear();
            }

            if (c.moveToFirst()) {
                do {
                    AdvaceDemandDO obj = new AdvaceDemandDO();
                    try {
                        obj.MMI_Code = c.getString(0);
                        obj.MMI_Name = c.getString(1);

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

	public ArrayList<String> SelectAllDemandDates(Context context) {
		ArrayList<String> vecRegularDemands = new ArrayList<String>();
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase _database = databaseHelper.getReadableDatabase();
        //_database.beginTransaction();
		String demanddate = null;
		Cursor c = null;
		try {
			c = _database.rawQuery("select DISTINCT DemandDate  from RegularDemands", null);

			if (vecRegularDemands != null) {
				vecRegularDemands.clear();
			}

			if (c.moveToFirst()) {
				do {
					try {
						demanddate = c.getString(0);
					} catch (Exception e) {
						Log.e("exception", "while getting from server");
					}
					// CREATE TABLE "RegularDemands" ("CNo" VARCHAR PRIMARY KEY
					// NOT NULL , "CName" VARCHAR, "GNo" VARCHAR,
					// "EII_EMP_ID" VARCHAR, "GroupName" VARCHAR, "MemberCode"
					// VARCHAR, "MemberName" VARCHAR, "DemandDate" VARCHAR,
					// "MLAI_ID" VARCHAR, "OSAmt" VARCHAR, "DemandTotal"
					// VARCHAR, "ODAmount" VARCHAR, "Attendance" VARCHAR)

					vecRegularDemands.add(demanddate);
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

	public ArrayList<RegularDemandsDO> SelectAll(String param, String type,Context context) {
		ArrayList<RegularDemandsDO> vecRegularDemands = new ArrayList<RegularDemandsDO>();
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase _database = databaseHelper.getReadableDatabase();
        //_database.beginTransaction();
		Cursor c = null;
		try {
			if (type.equalsIgnoreCase("CNo")) {
				c = _database.rawQuery("select * from RegularDemands where CNo ='" + param + "'", null);
			} else if ((type.equalsIgnoreCase("Groups"))) {
				c = _database.rawQuery("select * from  RegularDemands where GNo ='" + param + "'", null);
			} else if ((type.equalsIgnoreCase("memeber"))) {
				c = _database.rawQuery("select * from RegularDemands where MLAI_ID ='" + param + "'", null);
			}
			Log.e("query", "select * from RegularDemands where MLAI_ID ='" + param + "'");
			if (vecRegularDemands != null) {
				vecRegularDemands.clear();
			}

			if (c.moveToFirst()) {
				do {
					RegularDemandsDO obj = new RegularDemandsDO();
					obj.CNo = c.getString(0);
					obj.CName = c.getString(1);
					obj.GNo = c.getString(2);
					obj.EII_EMP_ID = c.getString(3);
					obj.GroupName = c.getString(4);
					obj.MemberCode = c.getString(5);
					obj.MemberName = c.getString(6);
					obj.DemandDate = c.getString(7);
					obj.MLAI_ID = c.getString(8);
					obj.OSAmt = c.getString(9);
					obj.DemandTotal = c.getString(10);
					obj.ODAmount = c.getString(11);
					obj.Attendance = c.getString(12);
					obj.GLI = c.getString(13);
					obj.Lateness = c.getString(14);
					obj.collectedAmount = c.getString(15);
					obj.ReciptNumber = c.getString(17);

					obj.Confirmed = c.getString(16);
					obj.RecieptGenerated = c.getString(18);
					obj.savedAmt = c.getString(19);
					obj.meetingStartTime = c.getString(20);
					obj.meetingEndTime = c.getString(21);
					obj.InstallNo = c.getString(23);
					obj.SittingOrder = c.getString(24);
					obj.FTODID = c.getString(25);
					obj.FTODReason = c.getString(26);
					obj.DemiseDate = c.getString(27);
					obj.SO = c.getString(28);
					obj.NextRepayDate = c.getString(29);
					obj.qom = c.getString(30);
					obj.probInCenter = c.getString(31);
					obj.groupDiscipline = c.getString(32);
					obj.collExp = c.getString(33);
					obj.collExpRMEL = c.getString(34);
					obj.collPlace = c.getString(35);
					obj.repaymentMadeBy = c.getString(36);
					obj.Renew = c.getString(37);
					obj.RenewFeed = c.getString(38);

					obj.TransactionID = c.getString(40);
					obj.Status = c.getString(41);
					obj.MobileNo = c.getString(43);
					obj.PaymentMode = c.getString(44);
					obj.TXNID = c.getString(45);
					obj.BranchPaymode = c.getString(46);
					obj.ProductName = c.getString(47);
					obj.DateTime = c.getString(48);

					obj.LatitudeCenter = c.getString(50);
					obj.LongitudeCenter = c.getString(51);
					obj.LatitudeGroup = c.getString(52);
					obj.LongitudeGroup = c.getString(53);
					obj.LatitudeMember = c.getString(54);
					obj.LongitudeMember = c.getString(55);
					obj.ImageName = c.getString(56);
					obj.BankName		= c.getString(57);
					obj.TransactionTime		= c.getString(58);
					obj.DateTime		= c.getString(59);
					obj.CenterAmt		= c.getString(60);
					obj.CenterMeeting		= c.getString(61);
					obj.GroupAmt		= c.getString(62);
					obj.GroupMeeting		= c.getString(63);
					obj.LateCollection		= c.getString(64);
					Log.e("insatllnumber", "" + obj.InstallNo);
					// CREATE TABLE "RegularDemands" ("CNo" VARCHAR PRIMARY KEY
					// NOT NULL , "CName" VARCHAR, "GNo" VARCHAR,
					// "EII_EMP_ID" VARCHAR, "GroupName" VARCHAR, "MemberCode"
					// VARCHAR, "MemberName" VARCHAR, "DemandDate" VARCHAR,
					// "MLAI_ID" VARCHAR, "OSAmt" VARCHAR, "DemandTotal"
					// VARCHAR, "ODAmount" VARCHAR, "Attendance" VARCHAR)

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
               ////_database.endTransaction();
            }
        }
		return vecRegularDemands;
	}

	public ArrayList<RegularDemandsDO> SelectAllCenters(String param,Context context) {
		ArrayList<RegularDemandsDO> vecRegularDemands = new ArrayList<RegularDemandsDO>();
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase _database = databaseHelper.getReadableDatabase();
        //_database.beginTransaction();
		Cursor c = null;
		try {
			c = _database.rawQuery("select DISTINCT CNo,CName,LatitudeCenter,LongitudeCenter,LatitudeGroup,LongitudeGroup,LatitudeMember,LongitudeMember,CenterAmt,CenterMeeting   from RegularDemands where DemandDate ='" + param + "' and NoPaymnet is null   ", null);
			Log.e("query", "select * from RegularDemands where CNo ='" + param + "'");
			if (vecRegularDemands != null) {
				vecRegularDemands.clear();
			}

			if (c.moveToFirst()) {
				do {
					RegularDemandsDO obj = new RegularDemandsDO();
					obj.CNo = c.getString(0);
					obj.CName = c.getString(1);

					obj.LatitudeCenter = c.getString(2);
					obj.LongitudeCenter = c.getString(3);
					obj.LatitudeGroup = c.getString(4);
					obj.LongitudeGroup = c.getString(5);
					obj.LatitudeMember = c.getString(6);
					obj.LongitudeMember = c.getString(7);
					obj.CenterAmt = c.getString(8);
					obj.CenterMeeting = c.getString(9);
					// CREATE TABLE "RegularDemands" ("CNo" VARCHAR PRIMARY KEY
					// NOT NULL , "CName" VARCHAR, "GNo" VARCHAR,
					// "EII_EMP_ID" VARCHAR, "GroupName" VARCHAR, "MemberCode"
					// VARCHAR, "MemberName" VARCHAR, "DemandDate" VARCHAR,
					// "MLAI_ID" VARCHAR, "OSAmt" VARCHAR, "DemandTotal"
					// VARCHAR, "ODAmount" VARCHAR, "Attendance" VARCHAR)

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
               ////_database.endTransaction();
            }
        }
		return vecRegularDemands;
	}

	public ArrayList<RegularDemandsDO> SelectAllCentersLate(Context context) {
		ArrayList<RegularDemandsDO> vecRegularDemands = new ArrayList<RegularDemandsDO>();
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase _database = databaseHelper.getReadableDatabase();
        //_database.beginTransaction();
		Cursor c = null;
		try {

			String query="select DISTINCT CNo,Centername,LatitudeCenter,LongitudeCenter,LatitudeGroup,LongitudeGroup,LatitudeMember,LongitudeMember,CenterAmt,CenterMeeting from LateTransactions";
			Log.v("","SelectAllCentersLate"+query);
			c = _database.rawQuery(query, null);
			Log.e("query", "select * from RegularDemands ");
			if (vecRegularDemands != null) {
				vecRegularDemands.clear();
			}

			if (c.moveToFirst()) {
				do {
					RegularDemandsDO obj = new RegularDemandsDO();
					obj.CNo = c.getString(0);
					obj.CName = c.getString(1);
					obj.LatitudeCenter = c.getString(2);
					obj.LongitudeCenter = c.getString(3);
					obj.LatitudeGroup = c.getString(4);
					obj.LongitudeGroup = c.getString(5);
					obj.LatitudeMember = c.getString(6);
					obj.LongitudeMember = c.getString(7);
					obj.CenterAmt = c.getString(8);
					obj.CenterMeeting = c.getString(9);
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
               ////_database.endTransaction();
            }
        }

		return vecRegularDemands;
	}

	public ArrayList<RegularDemandsDO> SelectGroups(String param,Context context) {
		ArrayList<RegularDemandsDO> vecRegularDemands = new ArrayList<RegularDemandsDO>();
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase _database = databaseHelper.getReadableDatabase();
        //_database.beginTransaction();
		Cursor c = null;
		try {
			String query="select DISTINCT GroupName,GNo,Confirmed,LatitudeCenter,LongitudeCenter,LatitudeGroup,LongitudeGroup,LatitudeMember,LongitudeMember,GroupAmt,GroupMeeting from RegularDemands where CNo = '" + param + "'and NoPaymnet is null";

			Log.v("","SelectGroups"+query);
			c = _database.rawQuery(query, null);


			if (vecRegularDemands != null) {
				vecRegularDemands.clear();
			}

			if (c.moveToFirst()) {
				do {
					RegularDemandsDO obj = new RegularDemandsDO();
					obj.GroupName = c.getString(0);
					obj.GNo = c.getString(1);
					obj.Confirmed = c.getString(2);
					obj.LatitudeCenter = c.getString(3);
					obj.LongitudeCenter = c.getString(4);
					obj.LatitudeGroup = c.getString(5);
					obj.LongitudeGroup = c.getString(6);
					obj.LatitudeMember = c.getString(7);
					obj.LongitudeMember = c.getString(8);

					obj.GroupAmt = c.getString(9);
					obj.GroupMeeting = c.getString(10);


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
               ////_database.endTransaction();
            }
        }
		return vecRegularDemands;
	}


	public ArrayList<RegularDemandsDO> SelectGroupsLate(String param,Context context) {
		ArrayList<RegularDemandsDO> vecRegularDemands = new ArrayList<RegularDemandsDO>();
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase _database = databaseHelper.getReadableDatabase();
        //_database.beginTransaction();
		Cursor c = null;
		try {
			String query="select DISTINCT GroupName,GNo,LatitudeCenter,LongitudeCenter,LatitudeGroup,LongitudeGroup,LatitudeMember,LongitudeMember,GroupAmt,GroupMeeting from LateTransactions where CNo = '" + param +"' ";
			//String query="select DISTINCT GroupName,GNo from LateTransactions where CNo = '" + param + "'";
			Log.v("","SelectGroupsLate"+query);
			c = _database.rawQuery(query, null);


			if (vecRegularDemands != null) {
				vecRegularDemands.clear();
			}

			if (c.moveToFirst()) {
				do {
					RegularDemandsDO obj = new RegularDemandsDO();
					obj.GroupName = c.getString(0);
					obj.GNo = c.getString(1);

					obj.LatitudeCenter = c.getString(2);
					obj.LongitudeCenter = c.getString(3);
					obj.LatitudeGroup = c.getString(4);
					obj.LongitudeGroup = c.getString(5);
					obj.LatitudeMember = c.getString(6);
					obj.LongitudeMember = c.getString(7);
					obj.GroupAmt = c.getString(8);
					obj.GroupMeeting = c.getString(9);


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
               ////_database.endTransaction();
            }
        }

        return vecRegularDemands;
	}





	public String getGroupName(String groupNumber,Context context) {
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase _database = databaseHelper.getReadableDatabase();
        //_database.beginTransaction();
		String CenterName = null;
		Cursor c = null;
		try {
			c = _database.rawQuery("SELECT DISTINCT GroupName  FROM RegularDemands where GNo = '" + groupNumber + "'",
					null);
			if (c.moveToFirst()) {
				CenterName = c.getString(0);
			}

			return CenterName;
		} catch (Exception e) {
			// TODO: handle exception
		}
        finally{
            if(c != null )
            {
                c.close();
               ////_database.endTransaction();
            }
        }
		return CenterName;
	}

	public boolean isAvailable(String date,Context context) {
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase _database = databaseHelper.getReadableDatabase();
        //_database.beginTransaction();
		Cursor c = null;
		String dateC = "";
		try {
			c = _database.rawQuery("select DemandDate from RegularDemands", null);

			c.moveToFirst();
			dateC = c.getString(0);

			Log.e("dateC", "" + dateC);
			Log.e("date", "" + date);
		} catch (Exception e) {

		}
        finally{
            if(c != null )
            {
                c.close();
               ////_database.endTransaction();
            }
        }

		if (dateC.equalsIgnoreCase(date)) {
			return true;
		} else {
			return false;
		}

	}



	// update RegularDemands set NoPaymnet = 1 where GNo = ''

	public void updateNoPaymnetStatus(String groupNumber,Context context) {

        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase _database = databaseHelper.getWritableDatabase();
        //_database.beginTransaction();
		String query = null;

		query = "update RegularDemands set NoPaymnet = 1 where GNo = '" + groupNumber + "'";
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

	public void updateNoPaymnetStatus_Center(String centerNumber,Context context) {
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase _database = databaseHelper.getWritableDatabase();
        //_database.beginTransaction();
		String query = null;

		query = "update RegularDemands set NoPaymnet = 1 where CNo = '" + centerNumber + "'";
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

	public void updateSavedAmt(String MLAI_ID, String amt,Context context) {
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase _database = databaseHelper.getWritableDatabase();
       // //_database.beginTransaction();
		String query = null;

		query = "UPDATE RegularDemands SET AmountSaved = '" + amt + "' where MLAI_ID ='" + MLAI_ID + "'";
		Log.e("query", query);
		try {
            SQLiteStatement sqLiteStatement = _database.compileStatement(query);
            sqLiteStatement.executeInsert();

		} catch (Exception e) {
			e.printStackTrace();
		}
        finally {
         // ////_database.setTransactionSuccessful();
            ////_database.endTransaction();
          //  _database.close();
        }
	}

	public void updateLate(String MLAI_ID, String amt,Context context) {
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase _database = databaseHelper.getWritableDatabase();
        //_database.beginTransaction();
		String query = null;

		query = "UPDATE RegularDemands SET LateCollection = '" + amt + "' where MLAI_ID ='" + MLAI_ID + "'";
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

	public void updateCashless(String MLAI_ID, String transaction,String Status,String Paymentmode,String TXNID,String bankName,String TransactionTime,String EntryDate,Context context ) {

		//String Status=String.valueOf(status);
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase _database = databaseHelper.getWritableDatabase();
        //_database.beginTransaction();
		String query = null;

		query = "UPDATE RegularDemands SET TransactionID = '" + transaction + "',Status = '" + Status + "',PaymentMode = '" + Paymentmode + "' ,TXNID = '" + TXNID + "',bankName = '" + bankName + "',TransactionTime = '" + TransactionTime + "',EntryDate = '" + EntryDate + "' where MLAI_ID ='" + MLAI_ID + "'";
		Log.v("query","updateCashless"+ query);
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


	public void updateImageName(String groupNumber, String ImageName,Context context) {
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase _database = databaseHelper.getWritableDatabase();
        //_database.beginTransaction();
		String query = null;

		query = "UPDATE RegularDemands SET ImageName = '" + ImageName + "'  where GNo ='" + groupNumber + "'";

		Log.e("query","updateImageName"+ query);
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
	public void updateMertingTime(String groupNumber, String meetingStart, String meetingEnd,String datetime,Context context) {
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase _database = databaseHelper.getWritableDatabase();
        //_database.beginTransaction();
		String query = null;

		query = "UPDATE RegularDemands SET MeetingStartTime = '" + meetingStart + "' , MeetingEndTime =' " + meetingEnd + "' , Datetime =' " + datetime + "' where GNo ='" + groupNumber + "'";

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

	public void updateMertingTime_CenterWise(String centerNumber, String meetingStart, String meetingEnd,String datetime,Context context) {
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase _database = databaseHelper.getWritableDatabase();
        //_database.beginTransaction();
		String query = null;

		query = "UPDATE RegularDemands SET MeetingStartTime = '" + meetingStart + "' , MeetingEndTime =' " + meetingEnd + "' , Datetime =' " + datetime + "' where CNo ='" + centerNumber + "'";
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


	public void updateGroupphotoCenterWise(String centerNumber, String imagename,Context context) {
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase _database = databaseHelper.getWritableDatabase();
        //_database.beginTransaction();
		String query = null;

		query = "UPDATE RegularDemands SET ImageName = '" + imagename + "'  where CNo ='" + centerNumber + "'";

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

	public void updateAttendense(ArrayList<RegularDemandsDO> alArrayList,Context context) {

        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase _database = databaseHelper.getWritableDatabase();
        //_database.beginTransaction();
		for (int i = 0; i < alArrayList.size(); i++) {
			RegularDemandsDO obj = alArrayList.get(i);
			String query = "UPDATE RegularDemands SET Attendance= '" + obj.Attendance + "'" + ", GLI= '" + obj.GLI + "'"
					+ ", Lateness= '" + obj.Lateness + "'" + " where MLAI_ID = '" + obj.MLAI_ID + "'";

			try {
                SQLiteStatement sqLiteStatement = _database.compileStatement(query);
                sqLiteStatement.executeInsert();

			} catch (Exception e) {
				e.printStackTrace();
			}


		}
	}

	public void updateAttendense_Centerwise(String centerNumber,Context context) {
		String query = "UPDATE RegularDemands SET Attendance= '1'" + ", GLI= '0'" + ", Lateness= '0'" + " where CNo = '"
				+ centerNumber + "'";
		Log.e("updatequery", query);

        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase _database = databaseHelper.getWritableDatabase();
        //_database.beginTransaction();
		try {
            SQLiteStatement sqLiteStatement = _database.compileStatement(query);
            sqLiteStatement.executeInsert();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
           ////_database.setTransactionSuccessful();
           ////_database.endTransaction();
           // _database.close();
		}



	}

	public ArrayList<RegularDemandsDO> SelectAllDistinctCenters(Context context) {
		ArrayList<RegularDemandsDO> vecRegularDemands = new ArrayList<RegularDemandsDO>();

        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase _database = databaseHelper.getReadableDatabase();
        //_database.beginTransaction();
		Cursor c = null;
		try {
			c = _database.rawQuery("select DISTINCT CNo, CName  from RegularDemands", null);

			if (vecRegularDemands != null) {
				vecRegularDemands.clear();
			}

			if (c.moveToFirst()) {
				do {
					RegularDemandsDO obj = new RegularDemandsDO();
					try {
						obj.CNo = c.getString(0);
						obj.CName = c.getString(1);
						// obj.GNo = c.getString(2);
						// obj.EII_EMP_ID = c.getString(3);
						// obj.GroupName = c.getString(4);
						// obj.MemberCode = c.getString(5);
						// obj.MemberName = c.getString(6);
						// obj.DemandDate = c.getString(7);
						// obj.MLAI_ID = c.getString(8);
						// obj.OSAmt = c.getString(9);
						// obj.DemandTotal = c.getString(10);
						// obj.ODAmount = c.getString(11);
						// obj.Attendance = c.getString(12);
						// obj.GLI = c.getString(13);
						// obj.Lateness = c.getString(13);
						// obj.collectedAmount = c.getString(15);
						// obj.Confirmed = c.getString(16);
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

	public ArrayList<RegularDemandsDO> SelectCenterNumber(String GNo,Context context) {
		ArrayList<RegularDemandsDO> vecRegularDemands = new ArrayList<RegularDemandsDO>();
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase _database = databaseHelper.getReadableDatabase();
        //_database.beginTransaction();
		Cursor c = null;
		String query = "select distinct CNo from RegularDemands where GNO='" + GNo + "'";
		Log.d("mfimo", query);
		try {
			c = _database.rawQuery(query, null);

			if (vecRegularDemands != null) {
				vecRegularDemands.clear();
			}

			if (c.moveToFirst()) {
				do {
					RegularDemandsDO obj = new RegularDemandsDO();
					try {
						obj.CNo = c.getString(0);
						// obj.CName = c.getString(1);
						// obj.GNo = c.getString(2);
						// obj.EII_EMP_ID = c.getString(3);
						// obj.GroupName = c.getString(4);
						// obj.MemberCode = c.getString(5);
						// obj.MemberName = c.getString(6);
						// obj.DemandDate = c.getString(7);
						// obj.MLAI_ID = c.getString(8);
						// obj.OSAmt = c.getString(9);
						// obj.DemandTotal = c.getString(10);
						// obj.ODAmount = c.getString(11);
						// obj.Attendance = c.getString(12);
						// obj.GLI = c.getString(13);
						// obj.Lateness = c.getString(13);
						// obj.collectedAmount = c.getString(15);
						// obj.Confirmed = c.getString(16);
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



	public String getTotalODAcounts(Context context) {
		String totalODAmount = null;
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase _database = databaseHelper.getReadableDatabase();
        //_database.beginTransaction();
		Cursor c = null;
		try {
			c = _database.rawQuery("select  COUNT(MLAI_ID)  from RegularDemands where ODAmount <> '0.00'", null);

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
               ////_database.endTransaction();
            }
        }
		return totalODAmount;

	}

	public String getDistinctCount(String cno,Context context) {
		String totalODAmount = null;
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase _database = databaseHelper.getReadableDatabase();
        //_database.beginTransaction();
		Cursor c = null;
		try {
			c = _database.rawQuery(
					"SELECT COUNT(*) FROM (SELECT DISTINCT GroupName  FROM RegularDemands where Confirmed is NULL and NoPaymnet is NULL and CNo='"
							+ cno + "')",
					null);

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
               ////_database.endTransaction();
            }
        }
		return totalODAmount;

	}



	public String getMemberAtt(String param,Context context) {

        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase _database = databaseHelper.getReadableDatabase();
        //_database.beginTransaction();
		String CenterName = null;
		Cursor c = null;
		try {
			c = _database.rawQuery(
					"select count(*) from RegularDemands  where MLAI_ID = '" + param + "' and Attendance =1", null);
			if (c.moveToFirst()) {
				CenterName = c.getString(0);
			}

			return CenterName;
		} catch (Exception e) {
			// TODO: handle exception
		}
        finally{
            if(c != null )
            {
                c.close();
               ////_database.endTransaction();
            }
        }
		return CenterName;
	}

	public String getCompletedCount(String memid, String type,Context context) {
		String totalODAmount = null, query, str;
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase _database = databaseHelper.getReadableDatabase();
        //_database.beginTransaction();
		Cursor c = null;
		str = "SELECT COUNT(*) FROM RegularDemands where SaveFB is NULL";
		try {
			if (type.equalsIgnoreCase("group")) {
				query = str + " and GNo='" + memid + "'";
			} else if (type.equalsIgnoreCase("center")) {
				query = str + " and CNo='" + memid + "'";
			} else {
				query = str + " and MLAI_ID='" + memid + "'";
			}
			Log.e("exception", query);
			c = _database.rawQuery(query, null);

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
               ////_database.endTransaction();
            }
        }
		return totalODAmount;
	}

	public String getODMembers(String Code, String Type,Context context) {
		String totalODAmount = null;
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase _database = databaseHelper.getReadableDatabase();
        //_database.beginTransaction();
		Cursor c = null;
		try {
			if (Type.equals("Center")) {
				c = _database.rawQuery(
						"select COUNT(*)  from RegularDemands where ODAmount == '0.00' and CNo='" + Code + "'", null);
			} else if (Type.equals("Group")) {
				c = _database.rawQuery(
						"select COUNT(*)  from RegularDemands where ODAmount == '0.00' and GNo='" + Code + "'", null);
			}
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
               ////_database.endTransaction();
            }
        }
		return totalODAmount;

	}

	public String getSumOfODAcounts(Context context) {
		String totalODAmount = null;

        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase _database = databaseHelper.getReadableDatabase();
        //_database.beginTransaction();

        Cursor c = null;
		try {
			c = _database.rawQuery("select  SUM(ODAmount)  from RegularDemands", null);

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

		// select SUM(ODAmount ) from RegularDemands
	}

	public String getTOTALDemandAmount(Context context) {
		String totalODAmount = null;

        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase _database = databaseHelper.getReadableDatabase();
        //_database.beginTransaction();
		Cursor c = null;
		try {
			c = _database.rawQuery("select  SUM(DemandTotal+ODAmount)  from RegularDemands", null);

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
               ////_database.endTransaction();
            }
        }
		return totalODAmount;

		// select SUM(DemandTotal+ODAmount) from RegularDemands
	}



	public String getTOTALDemandAmountForRegular(Context context) {
		String totalODAmount = null;
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase _database = databaseHelper.getReadableDatabase();
        //_database.beginTransaction();
		Cursor c = null;
		try {
			c = _database.rawQuery("select  sum(collectedamount)  from RegularDemands", null);

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

		// select SUM(DemandTotal+ODAmount) from RegularDemands
	}


	public synchronized  String getTOTALDemandAmountForRegularGroupId(String groupno,Context context) {
		String totalODAmount = null;

        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase _database = databaseHelper.getReadableDatabase();
        //_database.beginTransaction();

        Cursor c = null;
		try {
			c = _database.rawQuery("select  sum(CollectionAmount)  from Transactions where GNo='"+groupno+"' ", null);

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
		return totalODAmount;

		// select SUM(DemandTotal+ODAmount) from RegularDemands
	}
	public synchronized  String getTOTALDemandAmountForRegularGroupIdLate(String groupno,Context context) {
		String totalODAmount = null;

        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase _database = databaseHelper.getReadableDatabase();
        //_database.beginTransaction();

        Cursor c = null;
		try {
			String query="select  sum(Lateamount)  from LateTransactions where GNo='"+groupno+"'  and Lateconformation='yes' ";
			Log.v("","getTOTALDemandAmountForRegularGroupIdLate"+query);
			c = _database.rawQuery(query, null);

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
               ////_database.endTransaction();
            }
        }
		return totalODAmount;

		// select SUM(DemandTotal+ODAmount) from RegularDemands
	}
	public String getTOTALRegularAccounts(Context context) {
		String totalODAmount = null;

        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase _database = databaseHelper.getReadableDatabase();
        //_database.beginTransaction();
		Cursor c = null;
		try {
			c = _database.rawQuery("select  Count(*)  from RegularDemands", null);

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

		// select SUM(DemandTotal+ODAmount) from RegularDemands
	}

	public synchronized String getTOTALRegularAccountsGroupId(String groupno,Context context) {
		String totalODAmount = null;
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase _database = databaseHelper.getReadableDatabase();
        //_database.beginTransaction();
		Cursor c = null;
		try {
			c = _database.rawQuery("select  Count(*)  from Transactions where GNo='"+groupno+"'", null);

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
               ////_database.endTransaction();
            }
        }
		return totalODAmount;
	}

	public synchronized String getTOTALRegularAccountsGroupIdLate(String groupno,Context context) {
		String totalODAmount = null;
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase _database = databaseHelper.getReadableDatabase();
        //_database.beginTransaction();
		Cursor c = null;
		try {
			String query="select  Count(*)  from LateTransactions where GNo='"+groupno+"' and Lateconformation='yes' ";
			Log.v("","getTOTALRegularAccountsGroupIdLate"+query);
			c = _database.rawQuery(query, null);
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
               ////_database.endTransaction();
            }
        }
		return totalODAmount;
	}
	public String getNoOfODCollectedAcounts(Context context) {
		String query = " Select Count(*) from RegularDemands RD Join " + "("
				+ "Select MLAIID,SUM(CollectionAmount) CollectionAmount from Transactions Group BY MLAIID"
				+ ") Txn On RD.MLAI_ID=Txn.MLAIID " + "Where Txn.CollectionAmount > RD.ODAmount";

		String totalODAmount = null;
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase _database = databaseHelper.getReadableDatabase();
        //_database.beginTransaction();
		Cursor c = null;
		try {
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

		// Select Count(*)
		// from RegularDemands RD
		// Join
		// (
		// Select MLAIID,SUM(CollectionAmount) CollectionAmount from
		// Transactions Group BY MLAIID
		// ) Txn On RD.MLAI_ID=Txn.MLAIID
		// Where Txn.CollectionAmount > RD.ODAmount

	}



	// select count (*) NoPaymnet from RegularDemands where CNo ='DSH005-104-8'
	// and NoPaymnet <> 1
	public String CheckCenter_NoPayment(Context context) {
		String totalODAmount = null;
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase _database = databaseHelper.getReadableDatabase();
        //_database.beginTransaction();
		Cursor c = null;
		try {
			c = _database.rawQuery("select count (*) NoPaymnet  from RegularDemands where   NoPaymnet  is null", null);

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

		// select SUM(ODAmount ) from RegularDemands
	}

	public String getrowCount(Context context) {
		String totalODAmount = null;
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase _database = databaseHelper.getReadableDatabase();
        //_database.beginTransaction();
		Cursor c = null;
		try {
			c = _database.rawQuery("select count (*)  from RegularDemands ", null);

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

			////_database.endTransaction();
        }
		return totalODAmount;

		// select SUM(ODAmount ) from RegularDemands
	}



	// select (count(distinct R.GNo) - count( distinct TS.GNo) )from
	// RegularDemands as R ,Transactions as TS

	public String CheckForFullpayMent(String Gno,Context context) {
		String ODAmt = null;

        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase _database = databaseHelper.getReadableDatabase();
        //_database.beginTransaction();
		Cursor c = null;
		try {
			// c= _database.rawQuery("select SUM(Rd .ODAmount+Rd .DemandTotal) -
			// SUM(T.CollectionAmount) from RegularDemands Rd, Transactions T
			// where Rd.GNo = '"+Gno+"' and T.GNo = '"+Gno+"'", null);
			c = _database.rawQuery(
					"select SUM(ODAmount+DemandTotal)  - SUM(collectedamount) from RegularDemands where GNo = '" + Gno
							+ "'",
					null);
			Log.e("FULL PAYMENT QUERY",
					"select SUM(ODAmount+DemandTotal)  - SUM(collectedamount) from RegularDemands where GNo = '" + Gno
							+ "'");

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
               ////_database.endTransaction();
            }
        }
		return ODAmt;
	}

	public void updateSavingsdAmt(Context context) {

        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase _database = databaseHelper.getWritableDatabase();
        //_database.beginTransaction();
		String query = null;

		// query = "UPDATE RegularDemands SET AmountSaved='0'";
		query = "UPDATE RegularDemands SET AmountSaved='0',FtodreasonID='0',Reason='0',Demisedate='null'";
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

	public String CheckForFullpayMent_Centerwise(String Cno,Context context) {
		String ODAmt = null;
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase _database = databaseHelper.getReadableDatabase();
        //_database.beginTransaction();
		Cursor c = null;
		try {
			// c= _database.rawQuery("select SUM(Rd .ODAmount+Rd .DemandTotal) -
			// SUM(T.CollectionAmount) from RegularDemands Rd, Transactions T
			// where Rd.CNo = '"+Cno+"' and T.CNo = '"+Cno+"'", null);
			c = _database
					.rawQuery("select SUM(ODAmount+DemandTotal)- SUM(collectedamount) from RegularDemands where CNo = '"
							+ Cno + "'", null);
			Log.e("FULL PAYMENT QUERY",
					"select SUM(ODAmount+DemandTotal)- SUM(collectedamount) from RegularDemands where CNo = '" + Cno
							+ "'");

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
               ////_database.endTransaction();
            }
        }
		return ODAmt;
	}

	public void updateQom(String memrating, String memberCode,Context context) {
		// TODO Auto-generated method stub
		String query = "UPDATE RegularDemands SET Qom= '" + memrating + "'" + " where MLAI_ID = '" + memberCode + "'";
		Log.e("updatequery", query);

        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase _database = databaseHelper.getWritableDatabase();
        //_database.beginTransaction();
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

	public void updatePic(String num, String pim, String type,Context context) {
		// TODO Auto-generated method stub

        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase _database = databaseHelper.getWritableDatabase();
        //_database.beginTransaction();
		String query;
		if (type.equalsIgnoreCase("Center")) {
			query = "UPDATE RegularDemands SET ProbInCenter= '" + pim + "'" + " where CNo = '" + num + "'";
		} else {
			query = "UPDATE RegularDemands SET ProbInCenter= '" + pim + "'" + " where GNo = '" + num + "'";
		}
		Log.e("updatequery", query);
		SQLiteDatabase objSqliteDB = null;
		try {
            SQLiteStatement sqLiteStatement = _database.compileStatement(query);
            sqLiteStatement.executeInsert();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
           ////_database.setTransactionSuccessful();
           ////_database.endTransaction();
           // _database.close();
        }
	}

	public void updateDisc(String groupnumber, String problem, String type,Context context) {
		// TODO Auto-generated method stub

        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase _database = databaseHelper.getWritableDatabase();
        //_database.beginTransaction();

		String query;
		if (type.equalsIgnoreCase("Center")) {
			query = "UPDATE RegularDemands SET GroupDiscipline= '" + problem + "'" + " where CNo = '" + groupnumber
					+ "'";
		} else {
			query = "UPDATE RegularDemands SET GroupDiscipline= '" + problem + "'" + " where GNo = '" + groupnumber
					+ "'";
		}
		Log.e("updatequery", query);
		SQLiteDatabase objSqliteDB = null;
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

	public void updateCollExp(String centernumber, String problem, String type,Context context) {
		// TODO Auto-generated method stub
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase _database = databaseHelper.getWritableDatabase();
        //_database.beginTransaction();

		String query;
		if (type.equalsIgnoreCase("Center")) {
			query = "UPDATE RegularDemands SET CollExp= '" + problem + "'" + " where CNo = '" + centernumber + "'";
		} else if (type.equalsIgnoreCase("Group")) {
			query = "UPDATE RegularDemands SET CollExp= '" + problem + "'" + " where GNo = '" + centernumber + "'";
		} else {
			query = "UPDATE RegularDemands SET CollExp= '" + problem + "'" + " where MLAI_ID = '" + centernumber + "'";
		}
		Log.e("updatequery", query);
		SQLiteDatabase objSqliteDB = null;
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

	public void updateCollExpR(String groupnumber, String problem, String type,Context context) {
		// TODO Auto-generated method stub
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase _database = databaseHelper.getWritableDatabase();
        //_database.beginTransaction();
		String query;
		if (type.equalsIgnoreCase("Center")) {
			query = "UPDATE RegularDemands SET CollExpRmel= '" + problem + "'" + " where CNo = '" + groupnumber + "'";
		} else if (type.equalsIgnoreCase("Group")) {
			query = "UPDATE RegularDemands SET CollExpRmel= '" + problem + "'" + " where GNo = '" + groupnumber + "'";
		} else {
			query = "UPDATE RegularDemands SET CollExpRmel= '" + problem + "'" + " where MLAI_ID = '" + groupnumber
					+ "'";
		}
		Log.e("updatequery", query);
		SQLiteDatabase objSqliteDB = null;
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

	public void updateCollPlace(String groupnumber, String problem, String type,Context context) {
		// TODO Auto-generated method stub

        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase _database = databaseHelper.getWritableDatabase();
        //_database.beginTransaction();
		String query;
		if (type.equalsIgnoreCase("Center")) {
			query = "UPDATE RegularDemands SET CollPlace= '" + problem + "'" + " where CNo = '" + groupnumber + "'";
		} else if (type.equalsIgnoreCase("Group")) {
			query = "UPDATE RegularDemands SET CollPlace= '" + problem + "'" + " where GNo = '" + groupnumber + "'";
		} else {
			query = "UPDATE RegularDemands SET CollPlace= '" + problem + "'" + " where MLAI_ID = '" + groupnumber + "'";
		}
		Log.e("updatequery", query);
		SQLiteDatabase objSqliteDB = null;
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

	public void updateRepay(String groupnumber, String problem, String type,Context context) {
		// TODO Auto-generated method stub
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase _database = databaseHelper.getWritableDatabase();
        //_database.beginTransaction();
		String query;
		if (type.equalsIgnoreCase("Center")) {
			query = "UPDATE RegularDemands SET Repayby= '" + problem + "'" + " where CNo = '" + groupnumber + "'";
		} else if (type.equalsIgnoreCase("Group")) {
			query = "UPDATE RegularDemands SET Repayby= '" + problem + "'" + " where GNo = '" + groupnumber + "'";
		} else {
			query = "UPDATE RegularDemands SET Repayby= '" + problem + "'" + " where MLAI_ID = '" + groupnumber + "'";
		}
		Log.e("updatequery", query);
		SQLiteDatabase objSqliteDB = null;
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

	public void updateRenew(String groupnumber, String problem, String type,Context context) {
		// TODO Auto-generated method stub

        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase _database = databaseHelper.getWritableDatabase();
        //_database.beginTransaction();
		String query;
		if (type.equalsIgnoreCase("Center")) {
			query = "UPDATE RegularDemands SET RenewFeed= '" + problem + "'" + " where CNo = '" + groupnumber + "'";
		} else {
			query = "UPDATE RegularDemands SET RenewFeed= '" + problem + "'" + " where GNo = '" + groupnumber + "'";
		}
		Log.e("updatequery", query);
		SQLiteDatabase objSqliteDB = null;
		try {
            SQLiteStatement sqLiteStatement = _database.compileStatement(query);
            sqLiteStatement.executeInsert();
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
           ////_database.setTransactionSuccessful();
           ////_database.endTransaction();
           // _database.close();
        }
	}

	public void updatesavefb(String groupnumber, String type,Context context) {
		// TODO Auto-generated method stub
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase _database = databaseHelper.getWritableDatabase();
        //_database.beginTransaction();
		String query;
		if (type.equalsIgnoreCase("Center")) {
			query = "UPDATE RegularDemands SET SaveFB= '" + "Y" + "'" + " where CNo = '" + groupnumber + "'";
		} else if (type.equalsIgnoreCase("Group")) {
			query = "UPDATE RegularDemands SET SaveFB= '" + "Y" + "'" + " where GNo = '" + groupnumber + "'";
		} else {
			query = "UPDATE RegularDemands SET SaveFB= '" + "Y" + "'" + " where MLAI_ID = '" + groupnumber + "'";
		}
		Log.e("updatequery", query);
		SQLiteDatabase objSqliteDB = null;
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



	public synchronized  void  TruncatetabelGroupId(String groupid,Context context) {
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase _database = databaseHelper.getWritableDatabase();
        //_database.beginTransaction();
		String query = null;
		query = "DELETE FROM RegularDemands where GNo='"+groupid+"'";
		Log.v("query","TruncatetabelGroupId"+ query);
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



	public void deletecbsearchfirstchange() {
	}

	public void GroupcbsearchfirstSave(ArrayList<cbsearchgrid> result) {
	}

	public ConcurrentHashMap getfirstgrid() {
		return firstgrid;
	}

	// select SUM(Rd .ODAmount+Rd .DemandTotal) - SUM(T.CollectionAmount) from
	// RegularDemands Rd, Transactions T where Rd.GNo = 'DSH005-104-8-3' and
	// T.GNo = 'DSH005-104-8-3'
	public synchronized RegularDemandsDO SelectAllTransactionLate(String mlai_id,Context context) {
		RegularDemandsDO obj = new RegularDemandsDO();
        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase _database = databaseHelper.getReadableDatabase();
        //_database.beginTransaction();

        Cursor c = null;
		try {
			String quety="select * from LateTransactions where MLAIID ='"+mlai_id+"' ";
			Log.v("","SelectAllTransactionLate"+quety);
			c = _database.rawQuery(quety, null);
			if (c.moveToFirst()) {
				do {
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
						obj.AAdharNo = c.getString(37);
						obj.MobileNo = c.getString(38);
						obj.PaymentMode = c.getString(39);
						obj.ProductName = c.getString(41);
						obj.DateTime = c.getString(42);
						obj.ImageName = c.getString(45);
						obj.DemandTotal = c.getString(49);
						obj.ODAmount = c.getString(50);
						obj.LatitudeMember = c.getString(55);
						obj.LongitudeMember = c.getString(56);
						obj.BranchPaymode = c.getString(61);
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
		return obj;
	}

	public synchronized ArrayList<RegularDemandsDO> SelectAllTransactionLatetotal(Context context) {
		ArrayList<RegularDemandsDO> vecRegularDemands = new ArrayList<RegularDemandsDO>();

        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase _database = databaseHelper.getReadableDatabase();
        //_database.beginTransaction();
		Cursor c = null;
		try {
			String quety="select * from LateTransactions  ";
			Log.v("","SelectAllTransactionLate"+quety);
			c = _database.rawQuery(quety, null);
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
						obj.AAdharNo = c.getString(37);
						obj.MobileNo = c.getString(38);
						obj.PaymentMode = c.getString(39);
						obj.ProductName = c.getString(41);
						obj.DateTime = c.getString(42);
						obj.ImageName = c.getString(45);
						obj.DemandTotal = c.getString(49);
						obj.ODAmount = c.getString(50);
						obj.BranchPaymode = c.getString(61);
						vecRegularDemands.add(obj);
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
		return vecRegularDemands;
	}

	public synchronized ArrayList<RegularDemandsDO> SelectAllTransactionLatetotalSave(Context context) {
		ArrayList<RegularDemandsDO> vecRegularDemands = new ArrayList<RegularDemandsDO>();

        DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
        SQLiteDatabase _database = databaseHelper.getReadableDatabase();
        //_database.beginTransaction();
		Cursor c = null;
		try {
			String quety="select * from LateTransactions where Lateconformation='yes'";
			Log.v("","SelectAllTransactionLate"+quety);
			c = _database.rawQuery(quety, null);
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
						obj.AAdharNo = c.getString(37);
						obj.MobileNo = c.getString(38);
						obj.PaymentMode = c.getString(39);
						obj.ProductName = c.getString(41);
						obj.DateTime = c.getString(42);
						obj.ImageName = c.getString(45);
						obj.DemandTotal = c.getString(49);
						obj.ODAmount = c.getString(50);
						obj.BranchPaymode = c.getString(61);
						obj.LateCollection = c.getString(62);
						vecRegularDemands.add(obj);
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
		return vecRegularDemands;
	}




	public void TruncatetabelOD(Context context) {

		DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
		SQLiteDatabase _database = databaseHelper.getWritableDatabase();
		//_database.beginTransaction();
		String query = null;

		query = "DELETE FROM ODDemands ";
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



	public void ODDemandsSAVE(Context context) {

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
			//_database.setTransactionSuccessful();
			//_database.endTransaction();
			//_database.close();
		}

	}


	public void TruncatetabelAdvance(Context context) {

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

	public void TruncatetabelAdvanceSave(Context context) {

		DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
		SQLiteDatabase _database = databaseHelper.getWritableDatabase();
		//_database.beginTransaction();
		String query = null;

		query = "DELETE FROM AdvanceTransactions ";
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
}
