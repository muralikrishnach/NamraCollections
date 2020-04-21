package com.jayam.impactapp.database;

import java.util.ArrayList;

import com.jayam.impactapp.objects.BaseDO;
import com.jayam.impactapp.objects.RegularDemandsDO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

public class RegularDemandsBLTemp
{
	private ArrayList<RegularDemandsDO> alregulardemnads;
	private float amountTobeCollected;
	

	

	public boolean Insert(BaseDO object, Context context)
	{
		RegularDemandsDO parametrsDO = (RegularDemandsDO) object;

		DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
		SQLiteDatabase _database = databaseHelper.getReadableDatabase();
		//_database.beginTransaction();

		
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
			
			float amount_collected = Float.valueOf(parametrsDO.DemandTotal.trim()).floatValue()+Float.valueOf(parametrsDO.ODAmount.trim()).floatValue();
			values.put("Updated", String.valueOf(amount_collected));
			
			
		//CREATE TABLE "RegularDemands" ("CNo" VARCHAR PRIMARY KEY  NOT NULL , "CName" VARCHAR, "GNo" VARCHAR, 
			//"EII_EMP_ID" VARCHAR, "GroupName" VARCHAR, "MemberCode" VARCHAR, "MemberName" VARCHAR, "DemandDate" VARCHAR, 
			//"MLAI_ID" VARCHAR, "OSAmt" VARCHAR, "DemandTotal" VARCHAR, "ODAmount" VARCHAR, "Attendance" VARCHAR)
			
		//edit here


		try
		{

			_database.insert("RegularDemandsTemp", null, values);
			//_database.setTransactionSuccessful();
		}
		catch (Exception e) 
		{}
		finally{
			//_database.endTransaction();

		}
		return false;
	}


	public boolean Update(BaseDO object) 
	{
		return false;
	}

	public void updateregularDemandForMemeber(String MLAID , String amount, String savedAmt,Context context)
	{
		DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
		SQLiteDatabase _database = databaseHelper.getWritableDatabase();
		//_database.beginTransaction();
		String query = "UPDATE RegularDemandsTemp SET Updated= '"+amount+"',SavedaAmt = '"+savedAmt+"' where MLAI_ID = '"+MLAID+"'";
		 Log.e("updatequery", query);
		 SQLiteDatabase objSqliteDB = null;
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
			////_database.setTransactionSuccessful();
			////_database.endTransaction();
			// _database.close();
		 }
	}

	public void updateregulartempLate(String MLAID , String amount,Context context)
	{

		DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
		SQLiteDatabase _database = databaseHelper.getWritableDatabase();
		//_database.beginTransaction();

		String query = "UPDATE RegularDemandsTemp SET LateCollection= '"+amount+"' where MLAI_ID = '"+MLAID+"'";
		Log.e("updatequery", query);
		SQLiteDatabase objSqliteDB = null;
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
	public void updateCashlessMemeberTemp(String MLAID , String transaction,String status ,String Paymentmode,String TXNID,String bankName,String TransactionTime,String EntryDate,Context context)
	{
		DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
		SQLiteDatabase _database = databaseHelper.getWritableDatabase();
		//_database.beginTransaction();
		String query = "UPDATE RegularDemandsTemp SET TransactionID = '" + transaction + "',Status = '" + status + "',PaymentMode = '" + Paymentmode + "' ,TXNID = '" + TXNID + "',bankName = '" + bankName + "',TransactionTime = '" + TransactionTime + "',EntryDate = '" + EntryDate + "' where MLAI_ID = '"+MLAID+"'";

		SQLiteDatabase objSqliteDB = null;
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
	public void updateAmt(String MLAID,float amount,Context context)
	{
		DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
		SQLiteDatabase _database = databaseHelper.getWritableDatabase();
		//_database.beginTransaction();
		String query = "UPDATE RegularDemandsTemp SET Updated= '"+amount+"' where MLAI_ID = '"+MLAID+"'";
		 Log.e("updatequery", query);
		 SQLiteDatabase objSqliteDB = null;
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
			////_database.setTransactionSuccessful();
			////_database.endTransaction();
			// _database.close();
		 }
	}
	
	


	public void updateSavingsAmount(Context context)
	{
		DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
		SQLiteDatabase _database = databaseHelper.getWritableDatabase();
		//_database.beginTransaction();
//		String query = "UPDATE RegularDemandsTemp SET SavedaAmt='0'";
		String query = "UPDATE RegularDemandsTemp SET SavedaAmt='0',FtodreasonID='0',Reason='0',Demisedate='null'";
		 Log.e("updatequery", query);
		 SQLiteDatabase objSqliteDB = null;
		 try
			{
				SQLiteStatement sqLiteStatement = _database.compileStatement(query);
				sqLiteStatement.executeInsert();
				
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		 finally
			{
				//_database.setTransactionSuccessful();
				//_database.endTransaction();
				//_database.close();
			}
	}
	
	public void updateAttendense(ArrayList<RegularDemandsDO> alArrayList,Context context)
	{
		DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
		SQLiteDatabase _database = databaseHelper.getWritableDatabase();
		//_database.beginTransaction();
		for(int i=0; i< alArrayList.size(); i++)
		{
			RegularDemandsDO obj = alArrayList.get(i);
			String query = "UPDATE RegularDemandsTemp SET Attendance= '"+obj.Attendance+"'"+", GLI= '"+obj.GLI+"'"+", Lateness= '"+obj.Lateness+"'"+" where MLAI_ID = '"+obj.MLAI_ID+"'";
			 Log.e("updatequery", query);
			 SQLiteDatabase objSqliteDB = null;
			 	try
				{
					SQLiteStatement sqLiteStatement = _database.compileStatement(query);
					sqLiteStatement.executeInsert();
					
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
				finally
				{
//					if(objSqliteDB != null)
//					{
//						objSqliteDB.setTransactionSuccessful();
//						objSqliteDB.endTransaction();
//					}
				}
			 
			// objSqliteDB.close();
		}
		
	}
	
	public void updateAttendense_Centerwise(String centerNumber,Context context)
	{
			String query = "UPDATE RegularDemandsTemp SET Attendance= '1'"+", GLI= '0'"+", Lateness= '0'"+" where CNo = '"+centerNumber+"'";
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

	public boolean Delete(BaseDO object)
	{
		return false;
	}
	
	public void Truncatetabel(Context context)
	{
		DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
		SQLiteDatabase _database = databaseHelper.getWritableDatabase();
		//_database.beginTransaction();
			String query = null;
				
				query = "DELETE FROM RegularDemandsTemp ";
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


	public synchronized void TruncatetabelGroupId(String groupid,Context context)
	{
		DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
		SQLiteDatabase _database = databaseHelper.getWritableDatabase();
		//_database.beginTransaction();
		String query = null;

		query = "DELETE FROM RegularDemandsTemp where GNo='"+groupid+"'";
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
//		if(objSqliteDB != null)
//		{
//			objSqliteDB.close();
//		}


	}


	public ArrayList<RegularDemandsDO> SelectAll(Context context)
	{
		DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
		SQLiteDatabase _database = databaseHelper.getReadableDatabase();
		//_database.beginTransaction();
		ArrayList<RegularDemandsDO> vecRegularDemands = new ArrayList<RegularDemandsDO>();

		  Cursor c = null;
		  try 
		  {
		   c= _database.rawQuery("select * from RegularDemandsTemp", null);
		   
		   if(vecRegularDemands != null)
		    vecRegularDemands.clear();
		   
		   if(c.moveToFirst())
		   {
		    do
		    {
		    	RegularDemandsDO obj = 	new RegularDemandsDO();
		    	try
		    	{
		    	obj.CNo				 =	c.getString(0);
		    	obj.CName		     =	c.getString(1);
		    	obj.GNo				 =	c.getString(2);
		    	obj.EII_EMP_ID		 =	c.getString(3);
		    	obj.GroupName		 =	c.getString(4);
		    	obj.MemberCode		 =	c.getString(5);
		    	obj.MemberName		 =	c.getString(6);
		    	obj.DemandDate		 =	c.getString(7);
		    	obj.MLAI_ID			 =	c.getString(8);
		    	obj.OSAmt			 =	c.getString(9);
		    	obj.DemandTotal		 =	c.getString(10);
		    	obj.ODAmount		 =	c.getString(11);
		    	obj.Attendance		 =	c.getString(12);
		    	}
		    	catch(Exception e)
		    	{
		    		Log.e("exception", "while getting from server");
		    	}
		    	//CREATE TABLE "RegularDemands" ("CNo" VARCHAR PRIMARY KEY  NOT NULL , "CName" VARCHAR, "GNo" VARCHAR, 
				//"EII_EMP_ID" VARCHAR, "GroupName" VARCHAR, "MemberCode" VARCHAR, "MemberName" VARCHAR, "DemandDate" VARCHAR, 
				//"MLAI_ID" VARCHAR, "OSAmt" VARCHAR, "DemandTotal" VARCHAR, "ODAmount" VARCHAR, "Attendance" VARCHAR)
				
		     
		     vecRegularDemands.add(obj);
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

		  return vecRegularDemands;
	}
	public ArrayList<RegularDemandsDO> SelectAll(String param, String type,Context context)
	{
		ArrayList<RegularDemandsDO> vecRegularDemands = new ArrayList<RegularDemandsDO>();
		DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
		SQLiteDatabase _database = databaseHelper.getReadableDatabase();
		//_database.beginTransaction();
		  Cursor c = null;
		  try 
		  {
			 if(type.equalsIgnoreCase("CNo"))
				  c= _database.rawQuery("select * from RegularDemandsTemp where CNo ='"+param+"'", null);
			 else if((type.equalsIgnoreCase("Groups")))
				 c= _database.rawQuery("select * from  RegularDemandsTemp where GNo ='"+param+"' order by SittingOrder", null);
				 
			  Log.e("query", "select *------ from RegularDemands where GNo ='"+param+"' order by SittingOrder");
		   if(vecRegularDemands != null)
		    vecRegularDemands.clear();
		   
		   if(c.moveToFirst())
		   {
		    do
		    {
		    	RegularDemandsDO obj = 	new RegularDemandsDO();
		    	obj.CNo				 =	c.getString(0);
		    	obj.CName		     =	c.getString(1);
		    	obj.GNo				 =	c.getString(2);
		    	obj.EII_EMP_ID		 =	c.getString(3);
		    	obj.GroupName		 =	c.getString(4);
		    	obj.MemberCode		 =	c.getString(5);
		    	obj.MemberName		 =	c.getString(6);
		    	obj.DemandDate		 =	c.getString(7);
		    	obj.MLAI_ID			 =	c.getString(8);
		    	obj.OSAmt			 =	c.getString(9);
		    	obj.DemandTotal		 =	c.getString(10);
		    	obj.ODAmount		 =	c.getString(11);
		    	obj.Attendance		 =	c.getString(12);
		    	obj.GLI		 		 =	c.getString(13);
		    	obj.Lateness		 =	c.getString(14);
		    	obj.updated		 	 =	c.getString(15);
		    	obj.qom					= c.getString(24);
		    	obj.probInCenter		= c.getString(25);
		    	obj.groupDiscipline		= c.getString(26);
		    	obj.collExp				= c.getString(27);
		    	obj.collExpRMEL			= c.getString(28);
		    	obj.collPlace			= c.getString(29);
		    	obj.repaymentMadeBy		= c.getString(30);
				obj.MobileNo		= c.getString(36);
				obj.DateTime		= c.getString(41);
				obj.ImageName		= c.getString(49);
				obj.BankName		= c.getString(50);
				obj.TransactionTime		= c.getString(51);
				obj.DateTime		= c.getString(52);
		    	 Log.e("insatllnumber", ""+obj.InstallNo);		    	
		    	//CREATE TABLE "RegularDemands" ("CNo" VARCHAR PRIMARY KEY  NOT NULL , "CName" VARCHAR, "GNo" VARCHAR, 
				//"EII_EMP_ID" VARCHAR, "GroupName" VARCHAR, "MemberCode" VARCHAR, "MemberName" VARCHAR, "DemandDate" VARCHAR, 
				//"MLAI_ID" VARCHAR, "OSAmt" VARCHAR, "DemandTotal" VARCHAR, "ODAmount" VARCHAR, "Attendance" VARCHAR)
				
		     Log.e("added", "item");
		     vecRegularDemands.add(obj);
		    }
		    while(c.moveToNext());
		    
		    c.close();
		   }
		  }
		  catch (Exception e)
		  {
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
	
	
	public RegularDemandsDO SelectMemberDetails_MALIID(String mlai_id,Context context)
	{
		RegularDemandsDO obj = new RegularDemandsDO();
		DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
		SQLiteDatabase _database = databaseHelper.getReadableDatabase();
		//_database.beginTransaction();

		Cursor c = null;
		  try 
		  {
				  c= _database.rawQuery("select * from RegularDemandsTemp where MLAI_ID ='"+mlai_id+"'", null);
				  Log.e("quert","select * from RegularDemandsTemp where MLAI_ID ='"+mlai_id+"'");
		   if(c.moveToFirst())
		   {
		    	obj.CNo				 =	c.getString(0);
		    	obj.CName		     =	c.getString(1);
		    	obj.GNo				 =	c.getString(2);
		    	obj.EII_EMP_ID		 =	c.getString(3);
		    	obj.GroupName		 =	c.getString(4);
		    	obj.MemberCode		 =	c.getString(5);
		    	obj.MemberName		 =	c.getString(6);
		    	obj.DemandDate		 =	c.getString(7);
		    	obj.MLAI_ID			 =	c.getString(8);
		    	obj.OSAmt			 =	c.getString(9);
		    	obj.DemandTotal		 =	c.getString(10);
		    	obj.ODAmount		 =	c.getString(11);
		    	obj.Attendance		 =	c.getString(12);
		    	obj.GLI		 		 =	c.getString(13);
		    	obj.Lateness		 =	c.getString(14);
		    	obj.updated		 	 =	c.getString(15);
		    	
		    	obj.savedAmt		 =	c.getString(16);
		    	obj.InstallNo		 =	c.getString(17);


               obj.AAdharNo		 =	c.getString(35);
               obj.MobileNo		 =	c.getString(36);

			   obj.BranchPaymode		 =	c.getString(39);
		    	
		    	//CREATE TABLE "RegularDemands" ("CNo" VARCHAR PRIMARY KEY  NOT NULL , "CName" VARCHAR, "GNo" VARCHAR, 
				//"EII_EMP_ID" VARCHAR, "GroupName" VARCHAR, "MemberCode" VARCHAR, "MemberName" VARCHAR, "DemandDate" VARCHAR, 
				//"MLAI_ID" VARCHAR, "OSAmt" VARCHAR, "DemandTotal" VARCHAR, "ODAmount" VARCHAR, "Attendance" VARCHAR)
				
		     Log.e("added", "item");
		    }
		  }
		  catch (Exception e)
		  {
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
		  return obj;
	}
	


	
	public ArrayList<RegularDemandsDO> SelectGroups(String param,Context context)
	{
		ArrayList<RegularDemandsDO> vecRegularDemands = new ArrayList<RegularDemandsDO>();
		DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
		SQLiteDatabase _database = databaseHelper.getReadableDatabase();
		//_database.beginTransaction();
		  Cursor c = null;
		  try 
		  {
				 c= _database.rawQuery("select DISTINCT GroupName,GNo from RegularDemandsTemp where CNo = '"+param+"'", null);
			  Log.e("query", "select * from RegularDemands where CNo ='"+param+"'");
		   if(vecRegularDemands != null)
		    vecRegularDemands.clear();
		   
		   if(c.moveToFirst())
		   {
		    do
		    {
		    	RegularDemandsDO obj = 	new RegularDemandsDO();
		    	obj.GroupName				 =	c.getString(0);
		    	obj.GNo				 =	c.getString(1);
		    	//CREATE TABLE "RegularDemands" ("CNo" VARCHAR PRIMARY KEY  NOT NULL , "CName" VARCHAR, "GNo" VARCHAR, 
				//"EII_EMP_ID" VARCHAR, "GroupName" VARCHAR, "MemberCode" VARCHAR, "MemberName" VARCHAR, "DemandDate" VARCHAR, 
				//"MLAI_ID" VARCHAR, "OSAmt" VARCHAR, "DemandTotal" VARCHAR, "ODAmount" VARCHAR, "Attendance" VARCHAR)
				
		     Log.e("added", "item");
		     vecRegularDemands.add(obj);
		    }
		    while(c.moveToNext());
		    
		    c.close();
		   }
		  }
		  catch (Exception e)
		  {
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
	
	
	public String getCenterName(String groupNumber,Context context)
	{
		DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
		SQLiteDatabase _database = databaseHelper.getReadableDatabase();
		//_database.beginTransaction();
		 String CenterName = null;
		  Cursor c = null;
		  try 
		  {
			  c= _database.rawQuery("SELECT DISTINCT CName  FROM RegularDemandsTemp where GNo = '"+groupNumber+"'", null);
			  if(c.moveToFirst())
			   {
				  CenterName = c.getString(0);
			   }
			 
			 return CenterName;
		  }
		  catch (Exception e)
		  {
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
	
	public String getCenterName_Centerwise(String centernumber,Context context)
	{
		DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
		SQLiteDatabase _database = databaseHelper.getReadableDatabase();
		//_database.beginTransaction();

		String CenterName = null;
		  Cursor c = null;
		  try 
		  {
			  c= _database.rawQuery("SELECT DISTINCT CName  FROM RegularDemandsTemp where CNo = '"+centernumber+"'", null);
			  if(c.moveToFirst())
			   {
				  CenterName = c.getString(0);
			   }
			 
			 return CenterName;
		  }
		  catch (Exception e)
		  {
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
	
	public String getAttandense_Centerwise(String centernumber,Context context)
	{
		DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
		SQLiteDatabase _database = databaseHelper.getReadableDatabase();
		//_database.beginTransaction();
		 String CenterName = null;
		  Cursor c = null;
		  try 
		  {
			  c= _database.rawQuery("select count(Attendance) from RegularDemandsTemp  where CNo = '"+centernumber+"' and Attendance =1", null);
			  if(c.moveToFirst())
			   {
				  CenterName = c.getString(0);
			   }
			 
			 return CenterName;
		  }
		  catch (Exception e)
		  {
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
	
	public String getTotalMemebes_Centerwise(String centernumber,Context context)
	{

		DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
		SQLiteDatabase _database = databaseHelper.getReadableDatabase();
		//_database.beginTransaction();
		 String CenterName = null;
		  Cursor c = null;
		  try 
		  {
			  c= _database.rawQuery("select count(MLAI_ID) from RegularDemandsTemp where CNo = '"+centernumber+"'", null);
			  if(c.moveToFirst())
			   {
				  CenterName = c.getString(0);
			   }
			 
			 return CenterName;
		  }
		  catch (Exception e)
		  {
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
	
	public String getGroupName(String groupNumber,Context context)
	{
		DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
		SQLiteDatabase _database = databaseHelper.getReadableDatabase();
		//_database.beginTransaction();
		 String CenterName = null;
		  Cursor c = null;
		  try 
		  {
			  c= _database.rawQuery("SELECT DISTINCT GroupName  FROM RegularDemandsTemp where GNo = '"+groupNumber+"'", null);
			 if( c.moveToFirst())
			 {
				 CenterName = c.getString(0);
			 }
			 
			 return CenterName;
		  }
		  catch (Exception e)
		  {
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
	
	
	


	public void updateQom(String qomrat, String memberid,Context context) {
		// TODO Auto-generated method stub

		DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
		SQLiteDatabase _database = databaseHelper.getWritableDatabase();
		//_database.beginTransaction();
		String query = "UPDATE RegularDemandsTemp SET Qom= '"+qomrat+"'"+" where MLAI_ID = '"+memberid+"'";
		Log.e("updatequery", query);
		SQLiteDatabase objSqliteDB = null;
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
	 
	// objSqliteDB.close();
	}
	
	public void updatePic(String num, String pim,String type,Context context) {
		// TODO Auto-generated method stub

		DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
		SQLiteDatabase _database = databaseHelper.getWritableDatabase();
		//_database.beginTransaction();
		String query;
		if(type.equalsIgnoreCase("Center"))
			query = "UPDATE RegularDemandsTemp SET ProbInCenter= '"+pim+"'"+" where CNo = '"+num+"'";
		else
			query = "UPDATE RegularDemandsTemp SET ProbInCenter= '"+pim+"'"+" where GNo = '"+num+"'";
		Log.e("updatequery", query);
		SQLiteDatabase objSqliteDB = null;
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
		//	_database.close();
		}
	}

	public void updateDisc(String groupnumber, String problem, String type,Context context) {
		// TODO Auto-generated method stub
		DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
		SQLiteDatabase _database = databaseHelper.getWritableDatabase();
		//_database.beginTransaction();
		String query;
		if(type.equalsIgnoreCase("Center"))
			query = "UPDATE RegularDemandsTemp SET GroupDiscipline= '"+problem+"'"+" where CNo = '"+groupnumber+"'";
		else
			query = "UPDATE RegularDemandsTemp SET GroupDiscipline= '"+problem+"'"+" where GNo = '"+groupnumber+"'";
		Log.e("updatequery", query);
		SQLiteDatabase objSqliteDB = null;
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

	public void updateCollExp(String groupnumber, String problem, String type,Context context) {
		// TODO Auto-generated method stub

		DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
		SQLiteDatabase _database = databaseHelper.getWritableDatabase();
		//_database.beginTransaction();
		String query;
		if(type.equalsIgnoreCase("Center"))
			query = "UPDATE RegularDemandsTemp SET CollExp= '"+problem+"'"+" where CNo = '"+groupnumber+"'";
		else if(type.equalsIgnoreCase("Group"))
			query = "UPDATE RegularDemandsTemp SET CollExp= '"+problem+"'"+" where GNo = '"+groupnumber+"'";
		else 
			query = "UPDATE RegularDemandsTemp SET CollExp= '"+problem+"'"+" where MLAI_ID = '"+groupnumber+"'";
		Log.e("updatequery", query);
		SQLiteDatabase objSqliteDB = null;
	 	try
		{
			SQLiteStatement sqLiteStatement = _database.compileStatement(query);
			sqLiteStatement.executeInsert();
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			//_database.setTransactionSuccessful();
			//_database.endTransaction();
			//_database.close();
		}
	 

	}

	public void updateCollExpR(String centernumber, String problem,	String type,Context context) {
		// TODO Auto-generated method stub

		DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
		SQLiteDatabase _database = databaseHelper.getWritableDatabase();
		//_database.beginTransaction();
		String query;
		if(type.equalsIgnoreCase("Center"))
			query = "UPDATE RegularDemandsTemp SET CollExpRmel= '"+problem+"'"+" where CNo = '"+centernumber+"'";
		else if(type.equalsIgnoreCase("Group"))
		query = "UPDATE RegularDemandsTemp SET CollExpRmel= '"+problem+"'"+" where GNo = '"+centernumber+"'";
		else
		query = "UPDATE RegularDemandsTemp SET CollExpRmel= '"+problem+"'"+" where MLAI_ID = '"+centernumber+"'";
		Log.e("updatequery", query);
		SQLiteDatabase objSqliteDB = null;
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

	public void updateCollPlace(String centernumber, String problem,
			String type,Context context) {
		// TODO Auto-generated method stub

		DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
		SQLiteDatabase _database = databaseHelper.getWritableDatabase();
		//_database.beginTransaction();
		String query;
		if(type.equalsIgnoreCase("Center"))
			query = "UPDATE RegularDemandsTemp SET CollPlace= '"+problem+"'"+" where CNo = '"+centernumber+"'";
		else if(type.equalsIgnoreCase("Group"))
			query = "UPDATE RegularDemandsTemp SET CollPlace= '"+problem+"'"+" where GNo = '"+centernumber+"'";
		else
			query = "UPDATE RegularDemandsTemp SET CollPlace= '"+problem+"'"+" where MLAI_ID = '"+centernumber+"'";
		Log.e("updatequery", query);
		SQLiteDatabase objSqliteDB = null;
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

	public void updateRepay(String centernumber, String problem, String type,Context context) {
		// TODO Auto-generated method stub

		DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
		SQLiteDatabase _database = databaseHelper.getWritableDatabase();
		//_database.beginTransaction();
		String query;
		if(type.equalsIgnoreCase("Center"))
			query = "UPDATE RegularDemandsTemp SET RepayBy= '"+problem+"'"+" where CNo = '"+centernumber+"'";
		else if(type.equalsIgnoreCase("Group"))
			query = "UPDATE RegularDemandsTemp SET RepayBy= '"+problem+"'"+" where GNo = '"+centernumber+"'";
		else
			query = "UPDATE RegularDemandsTemp SET RepayBy= '"+problem+"'"+" where MLAI_ID = '"+centernumber+"'";
		Log.e("updatequery", query);
		SQLiteDatabase objSqliteDB = null;
	 	try
		{
			SQLiteStatement sqLiteStatement = _database.compileStatement(query);
			sqLiteStatement.executeInsert();
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			//_database.setTransactionSuccessful();
			//_database.endTransaction();
			//_database.close();
		}
	 

	}

	public void updateRenew(String groupnumber, String problem, String type,Context context) {
		// TODO Auto-generated method stub


		DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
		SQLiteDatabase _database = databaseHelper.getWritableDatabase();
		//_database.beginTransaction();
		String query;
		if(type.equalsIgnoreCase("Center"))
			query = "UPDATE RegularDemandsTemp SET RenewFeed= '"+problem+"'"+" where CNo = '"+groupnumber+"'";
		else
			query = "UPDATE RegularDemandsTemp SET RenewFeed= '"+problem+"'"+" where GNo = '"+groupnumber+"'";
		Log.e("updatequery", query);
		SQLiteDatabase objSqliteDB = null;
	 	try
		{
			SQLiteStatement sqLiteStatement = _database.compileStatement(query);
			sqLiteStatement.executeInsert();
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			//_database.setTransactionSuccessful();
			//_database.endTransaction();
			//_database.close();
		}
	 

	}

	public void updateMobilenoregtemp(String MLAID,String amount,Context context)
	{

		DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
		SQLiteDatabase _database = databaseHelper.getWritableDatabase();
		//_database.beginTransaction();
		String query = "UPDATE RegularDemandsTemp SET MobileNo= '"+amount+"' where MLAI_ID = '"+MLAID+"'";
		Log.e("updatequery", query);
		SQLiteDatabase objSqliteDB = null;
		try
		{
			SQLiteStatement sqLiteStatement = _database.compileStatement(query);
			sqLiteStatement.executeInsert();

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			//_database.setTransactionSuccessful();
			//_database.endTransaction();
			//_database.close();
		}
	}


	public void updateMobilenoreg(String MLAID,String amount,Context context)
	{

		DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
		SQLiteDatabase _database = databaseHelper.getWritableDatabase();
		//_database.beginTransaction();

		String query = "UPDATE RegularDemands SET MobileNo= '"+amount+"' where MLAI_ID = '"+MLAID+"'";
		Log.e("updatequery", query);
		SQLiteDatabase objSqliteDB = null;
		try
		{
			SQLiteStatement sqLiteStatement = _database.compileStatement(query);
			sqLiteStatement.executeInsert();

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			//_database.setTransactionSuccessful();
			//_database.endTransaction();
		//	_database.close();
		}
	}
	public ArrayList<RegularDemandsDO> SelectMemberssLate(String param,Context context) {
		ArrayList<RegularDemandsDO> vecRegularDemands = new ArrayList<RegularDemandsDO>();
		DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
		SQLiteDatabase _database = databaseHelper.getReadableDatabase();
		//_database.beginTransaction();
		Cursor c = null;
		try {
			String query="select DISTINCT MLAIID,MemberName,LatitudeCenter,LongitudeCenter,LatitudeGroup,LongitudeGroup,LatitudeMember,LongitudeMember,GroupAmt,GroupMeeting from LateTransactions where GNo = '" + param +"' ";
			//String query="select DISTINCT GroupName,GNo from LateTransactions where CNo = '" + param + "'";
			Log.v("","SelectMemberssLate"+query);
			c = _database.rawQuery(query, null);


			if (vecRegularDemands != null) {
				vecRegularDemands.clear();
			}

			if (c.moveToFirst()) {
				do {
					RegularDemandsDO obj = new RegularDemandsDO();
					obj.MLAI_ID = c.getString(0);
					obj.MemberName = c.getString(1);

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
				//_database.endTransaction();
			}
		}
		return vecRegularDemands;
	}

}
