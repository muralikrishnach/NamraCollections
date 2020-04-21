package com.jayam.impactapp.database;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.jayam.impactapp.common.AppConstants;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper 
{
	private int openConnections = 0;
    public static SQLiteDatabase _database;
	private static DatabaseHelper instance;
    private final Context myContext;
    public static String apstorphe = "'";
	public static String sep = ",";
	private SQLiteDatabase database;
	
	public DatabaseHelper(Context context) 
    {	 
    	super(context, AppConstants.DATABASE_NAME, null, 1);
        this.myContext = context;
    }





	public static synchronized DatabaseHelper getInstance(Context context) {
		// Use the application context, which will ensure that you
		// don't accidentally leak an Activity's context.
		// See this article for more information: http://bit.ly/6LRzfx
		if (instance == null) {
			instance = new DatabaseHelper(context.getApplicationContext());
		}
		return instance;
	}

	/**
     * Creates a empty database on the system and rewrites it with your own database.
     * */
    public void createDataBase() throws IOException
    {
       	boolean dbExist = checkDataBase();

       	Log.v("","dbExist---"+dbExist);


	//	copyDataBase();
    	if(!dbExist)
    	{
    		//By calling this method and empty database will be created into the default system path
            //of your application so we are gonna be able to overwrite that database with our database.
    		try
    		{
    			copyDataBase();
    		}
    		catch (IOException e)
    		{
        		throw new Error("Error copying database");
         	}
    	}
    }
    
    
    /**
     * Check if the database already exist to avoid re-copying the file each time you open the application.
     * @return true if it exists, false if it doesn't
     */
    private boolean checkDataBase()
    {	 
    	SQLiteDatabase checkDB = null;
    	try
    	{
    		String myPath = AppConstants.DATABASE_PATH + AppConstants.DATABASE_NAME;
			File file = new File(myPath);
			if (file.exists() && !file.isDirectory())
    		checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
			Log.v("","checkDBval--"+checkDB);
    	}
    	catch(SQLiteException e)
    	{
    		//database does't exist yet.	 
    	}
    	if(checkDB != null)
    	{	 
    		checkDB.close();	 
    	}
 
    	return checkDB != null ? true : false;
    }


	public static SQLiteDatabase newopenDataBase() throws SQLException
	{
		try
		{
			if(_database == null)
			{
				_database = SQLiteDatabase.openDatabase(AppConstants.DATABASE_PATH + AppConstants.DATABASE_NAME, null, SQLiteDatabase.OPEN_READWRITE
						| SQLiteDatabase.CREATE_IF_NECESSARY);
			}
			else if(!_database.isOpen())
			{
				_database = SQLiteDatabase.openDatabase(AppConstants.DATABASE_PATH + AppConstants.DATABASE_NAME, null, SQLiteDatabase.OPEN_READWRITE
						| SQLiteDatabase.CREATE_IF_NECESSARY);
			}
			return _database;
		}
		catch(Exception e)
		{
			Log.e("DatabaseHelper", "openDataBase() - "+e.toString());
			return _database;
		}

	}

    public void copyDataBase() throws IOException
    {
     Log.v("","copyDataBasemethod");
    	//Open your local db as the input stream
    	InputStream myInput = myContext.getAssets().open(AppConstants.DATABASE_NAME);
 
    	// Path to the just created empty db
		//DatabaseHelper helper = new DatabaseHelper(myContext);
		DatabaseHelper databaseHelper = DatabaseHelper.getInstance(myContext);

		SQLiteDatabase database = databaseHelper.getReadableDatabase();
		String outFileName = AppConstants.DATABASE_PATH + AppConstants.DATABASE_NAME;

		Log.v("","outFileName--"+outFileName);
		database.close();


    	//Open the empty db as the output stream
    	OutputStream myOutput = new FileOutputStream(outFileName);
 
    	//transfer bytes from the inputfile to the outputfile
    	byte[] buffer = new byte[2048];
    	int length;
    	while ((length = myInput.read(buffer))>0)
    	{
    		myOutput.write(buffer, 0, length);
    	}
 
    	//Close the streams
    	myOutput.flush();
    	myOutput.close();
    	myInput.close();
 
    }
    



	@Override
	public void onCreate(SQLiteDatabase db) 
	{
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
	{		

	}



	@Override
	public synchronized SQLiteDatabase getWritableDatabase() {
		try
		{
			if(_database == null)
			{
				_database = SQLiteDatabase.openDatabase(AppConstants.DATABASE_PATH + AppConstants.DATABASE_NAME, null, SQLiteDatabase.OPEN_READWRITE
						| SQLiteDatabase.CREATE_IF_NECESSARY);
			}
			else if(!_database.isOpen())
			{
				_database = SQLiteDatabase.openDatabase(AppConstants.DATABASE_PATH + AppConstants.DATABASE_NAME, null, SQLiteDatabase.OPEN_READWRITE
						| SQLiteDatabase.CREATE_IF_NECESSARY);
			}
			return _database;
		}
		catch(Exception e)
		{
			Log.e("DatabaseHelper", "openDataBase() - "+e.toString());
			return _database;
		}
	}

	@Override
	public synchronized SQLiteDatabase getReadableDatabase() {
		try
		{
			if(_database == null)
			{
				_database = SQLiteDatabase.openDatabase(AppConstants.DATABASE_PATH + AppConstants.DATABASE_NAME, null, SQLiteDatabase.OPEN_READONLY
						| SQLiteDatabase.CREATE_IF_NECESSARY);
			}
			else if(!_database.isOpen())
			{
				_database = SQLiteDatabase.openDatabase(AppConstants.DATABASE_PATH + AppConstants.DATABASE_NAME, null, SQLiteDatabase.OPEN_READONLY
						| SQLiteDatabase.CREATE_IF_NECESSARY);
			}
			return _database;
		}
		catch(Exception e)
		{
			Log.e("DatabaseHelper", "openDataBase() - "+e.toString());
			return _database;
		}


	}
	
}
