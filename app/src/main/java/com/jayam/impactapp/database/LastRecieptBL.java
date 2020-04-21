package com.jayam.impactapp.database;

import java.util.ArrayList;

import com.jayam.impactapp.objects.AdvaceDemandDO;
import com.jayam.impactapp.objects.LastReceiptDO;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

public class LastRecieptBL 
{
	public void insert(String LastTxnId, String Print, String Type , Context context)
	{
		DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
		SQLiteDatabase _database = databaseHelper.getReadableDatabase();
		//_database.beginTransaction();
		String query = null;
		query = "INSERT INTO LastReciept VALUES ('"+LastTxnId+"','"+Print+"','"+Type+"')";
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
	


	public ArrayList<LastReceiptDO> SelectAll(Context context) {
		ArrayList<LastReceiptDO> vecRegularDemands = new ArrayList<LastReceiptDO>();
		DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
		SQLiteDatabase _database = databaseHelper.getReadableDatabase();
		//_database.beginTransaction();
		  Cursor c = null;
		  try 
		  {

		   c= _database.rawQuery("SELECT *  FROM LastReciept order by LastTxnId desc limit 1", null);
		   Log.e("Query:--","SELECT *  FROM LastReciept order by LastTxnId desc limit 1");
		   
		   if(vecRegularDemands != null)
		    vecRegularDemands.clear();
		   
		   if(c.moveToFirst())
		   {
		    do
		    {
		    	LastReceiptDO obj = 	new LastReceiptDO();
		    	try
		    	{
	    		obj.LastTxnId		=	c.getString(0);
	    		obj.Print		=	c.getString(1);
	    		obj.Type		=	c.getString(2);
	    		
		    	
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
	public void delete()
	{
		/*SQLiteDatabase objSqliteDB = null;
		objSqliteDB = DatabaseHelper.openDataBase();
		String query = null;
		query = "DELETE FROM LastReciept";
	   try
		{
			objSqliteDB.beginTransaction();	
			SQLiteStatement sqLiteStatement = objSqliteDB.compileStatement(query);
			sqLiteStatement.executeInsert();
			objSqliteDB.setTransactionSuccessful();
			objSqliteDB.endTransaction();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}*/
	}
	public String  updateLastReceiptPrintFlag(String TxnCode, String status,Context context)
	{
		String count = null ;
		DatabaseHelper databaseHelper = DatabaseHelper.getInstance(context);
		SQLiteDatabase _database = databaseHelper.getWritableDatabase();
		//_database.beginTransaction();
		  Cursor c = null;
		  try 
		  {
		   c= _database.rawQuery("UPDATE LastReciept SET Print = '"+status+"' where LastTxnId = '"+TxnCode+"'", null);
		   
		   
		   if(c.moveToFirst())
		   {
		    do
		    {
		    	try
		    	{
		    		count = c.getString(0);
		    	}
		    	catch(Exception e)
		    	{
		    		Log.e("exception", "while getting from server");
		    	}
		    	
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
		  return count;

	}
}
