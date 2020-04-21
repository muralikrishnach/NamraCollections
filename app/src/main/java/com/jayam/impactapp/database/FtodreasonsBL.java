package com.jayam.impactapp.database;

import java.util.ArrayList;

import com.jayam.impactapp.objects.BaseDO;
import com.jayam.impactapp.objects.FtodreasonsDO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

public class FtodreasonsBL  {

	private ArrayList<FtodreasonsDO> allFtodreasons;
	private float amttobecollected;
	

	

	public boolean Insert(BaseDO object, Context context)
	{
		FtodreasonsDO FtodreasonsDO = (FtodreasonsDO) object;

		DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
		SQLiteDatabase _database = databaseHelper.getWritableDatabase();
		//_database.beginTransaction();

				
		try
		{
			ContentValues values = new ContentValues();
			values.put("ID", FtodreasonsDO.ID);
			values.put("FTODReason", FtodreasonsDO.FTODReason);
			_database.insert("FTODs", null, values);
			//_database.setTransactionSuccessful();
		}
		catch (Exception e) 
		{

		}
		finally {
			//_database.endTransaction();
		}
		return false;
	}


	public boolean Update(BaseDO object)
	{
		// TODO Auto-generated method stub
		return false;
	}
	
	


	public boolean Delete(BaseDO object,Context context) {

		DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
		SQLiteDatabase _database = databaseHelper.getWritableDatabase();
		//_database.beginTransaction();
		
		try 
	  {
	  	String query="DELETE  FROM FTODs";
	  	Log.v("","FTODS--"+query);
		  _database.execSQL(query);
		 ////_database.setTransactionSuccessful();
	  } 
	  catch (Exception e) 
	  {  
		 // Log.e("error occured while deleting data from table",e.toString());
	  }
		finally {
			//_database.endTransaction();
		}

		return false;
}

	

	public ArrayList<FtodreasonsDO> SelectAll(Context context)
	{
		ArrayList<FtodreasonsDO> vecRegularDemands = new ArrayList<FtodreasonsDO>();
		DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
		SQLiteDatabase _database = databaseHelper.getReadableDatabase();
		//_database.beginTransaction();
		  Cursor c = null;
		  try 
		  {
		   c= _database.rawQuery("select * from FTODs", null);
		   
		   if(vecRegularDemands != null)
		    vecRegularDemands.clear();
		   
		   if(c.moveToFirst())
		   {
		    do
		    {
		    	FtodreasonsDO obj = 	new FtodreasonsDO();
		    	try
		    	{
		    	obj.ID		 =	c.getString(0);
		    	obj.FTODReason		 =	c.getString(1);
		    	
		    	}
		    	catch(Exception e)
		    	{
		    		Log.e("exception", "while getting from server");
		    	}
		    	
		     
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
		  finally {


			 ////_database.setTransactionSuccessful();
			 ////_database.endTransaction();
			 // _database.close();
		  }
		  return vecRegularDemands;
	}
	public void updateFTODReasons(String Code , String ID, String Reason,String Date,String Type,Context context)
	{
		String query = null;
		if(Type.equalsIgnoreCase("Center")){
			query= "UPDATE RegularDemands SET FtodreasonID= '"+ID+"',Reason = '"+Reason+"',Demisedate='"+Date+"' where CNo = '"+Code+"' and CAST(ODAmount AS INT)=0";
		}else if(Type.equalsIgnoreCase("Group")){
			query = "UPDATE RegularDemands SET FtodreasonID= '"+ID+"',Reason = '"+Reason+"',Demisedate='"+Date+"' where GNo = '"+Code+"' and CAST(ODAmount AS INT)=0";
		}else if(Type.equalsIgnoreCase("Member")){
			query = "UPDATE RegularDemands SET FtodreasonID= '"+ID+"',Reason = '"+Reason+"',Demisedate='"+Date+"' where MLAI_ID = '"+Code+"'";
		}
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
		 finally
			{
				//_database.setTransactionSuccessful();
				//_database.endTransaction();
				//_database.close();
			}
	}
	public void updateFTODReasonsTemp(String MLAID , String ID, String Reason,String Date,Context context)
	{
		String query = "UPDATE RegularDemandsTemp SET FtodreasonID= '"+ID+"',Reason = '"+Reason+"',Demisedate='"+Date+"' where MLAI_ID = '"+MLAID+"'";
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
		 finally
			{
				//_database.setTransactionSuccessful();
				//_database.endTransaction();
				//_database.close();
			}
	}
	public ArrayList<FtodreasonsDO> SelectAl(Context context)
	{
		ArrayList<FtodreasonsDO> vecRegularDemands = new ArrayList<FtodreasonsDO>();
		DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
		SQLiteDatabase _database = databaseHelper.getReadableDatabase();
		//_database.beginTransaction();
		  Cursor c = null;
		  try 
		  {
		   c= _database.rawQuery("SELECT * FROM FTODs where ID not in (1,2)", null);
		   
		   if(vecRegularDemands != null)
		    vecRegularDemands.clear();
		   
		   if(c.moveToFirst())
		   {
		    do
		    {
		    	FtodreasonsDO obj = 	new FtodreasonsDO();
		    	try
		    	{
		    	obj.ID		 	=	c.getString(0);
		    	obj.FTODReason	=	c.getString(1);
		    	
		    	}
		    	catch(Exception e)
		    	{
		    		Log.e("exception", "while getting from server");
		    	}
		    	
		     
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
	
}
