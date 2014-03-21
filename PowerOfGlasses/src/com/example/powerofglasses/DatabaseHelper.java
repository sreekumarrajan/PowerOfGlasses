package com.example.powerofglasses;

import java.util.ArrayList;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
	private static DatabaseHelper mInstance = null;
  private static final String DATABASE_NAME="constants.db";
  private static final int SCHEMA=1;
  static final String NAME="name";
  static final String DATE="date";
  static final String TIME="time";
  static final String LEFTPOWER = "leftpower";
  static final String RIGHTPOWER = "rightpower";
  //static final String VALUE="value";
  static final String TABLE="constants";

  public static DatabaseHelper getInstance(Context ctx) {
      /** 
       * use the application context as suggested by CommonsWare.
       * this will ensure that you dont accidentally leak an Activitys
       * context (see this article for more information: 
       * http://developer.android.com/resources/articles/avoiding-memory-leaks.html)
       */
      if (mInstance == null) {
          mInstance = new DatabaseHelper(ctx.getApplicationContext());
      }
      return mInstance;
  }
  
  private DatabaseHelper(Context context) {
    super(context, DATABASE_NAME, null, SCHEMA);
  }

  @Override
  public void onCreate(SQLiteDatabase db) {
    db.execSQL("CREATE TABLE constants (_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, date TEXT, time TEXT, leftpower TEXT, rightpower TEXT);");
   
  }

  public void addData(String nameString, String dateString,
		  				String timeString, String leftPowerString,
		  										String rightPowerString )
  {
	  ContentValues cv=new ContentValues();

	    cv.put(NAME, nameString);
	    cv.put(DATE, dateString);
	    cv.put(TIME, timeString);
	    cv.put(LEFTPOWER, leftPowerString);
	    cv.put(RIGHTPOWER, rightPowerString);
	    
	    getWritableDatabase().insert(TABLE, NAME, cv);
	    //getWritableDatabase().insert(TABLE,DATE, cv);
	    
	   
	    //getWritableDatabase().insert(TABLE, TIME, cv);
	  
	    
	    //getWritableDatabase().insert(TABLE, LEFTPOWER, cv);
	    
	    
	    //getWritableDatabase().insert(TABLE, RIGHTPOWER, cv);
	    
  }
  
  public ArrayList<String> getNames()
  {
	  ArrayList<String> searchItems = new ArrayList<String>();
	 // Cursor result=
			//  getReadableDatabase().rawQuery("SELECT  TITLE FROM TABLE ", null);
	  
	  String query=
		        String.format("SELECT %s FROM %s",
		                      NAME,TABLE);

	Cursor result =  getReadableDatabase().rawQuery(query, null);
			  while (result.moveToNext()) {
			  String name = result.getString(0);
			  searchItems.add(name);
			  // do something useful with these
			  }
			  result.close();
			  return searchItems;
  }
  
  public ArrayList<String> getDateAndTime()
  {
	  ArrayList<String> searchItems = new ArrayList<String>();
		 // Cursor result=
				//  getReadableDatabase().rawQuery("SELECT  TITLE FROM TABLE ", null);
		  
		  String query=
			        String.format("SELECT %s, %s FROM %s",
			                      DATE,TIME,TABLE);

		Cursor result =  getReadableDatabase().rawQuery(query, null);
				  while (result.moveToNext()) {
				  if(result.getString(0) != null && result.getString(1)!= null)
				  {
				  String name = result.getString(0)+ " "+ result.getString(1);
				  searchItems.add(name);
				  }
				  // do something useful with these
				  }
				  result.close();
				  return searchItems;
  }
  
  public String getName(String date,String time)
  {
	   // Cursor result=
				//  getReadableDatabase().rawQuery("SELECT  TITLE FROM TABLE ", null);
		  
		  String query=
			        String.format("SELECT %s FROM %s WHERE %s = %s AND %s = %s",
			                      NAME,TABLE,DATE,date,TIME,time);

		Cursor result =  getReadableDatabase().rawQuery(query, null);
				  
	    String name = result.getString(0);
	 
	    result.close();
		 return name;
  }
  public String getLeftPower(String date,String time)
  {
	   	  
		  String query=
			        String.format("SELECT %s FROM %s WHERE %s = %s AND %s = %s",
			                      LEFTPOWER,TABLE,DATE,date,TIME,time);

		Cursor result =  getReadableDatabase().rawQuery(query, null);
				  
	    String name = result.getString(0);
	 
	    result.close();
		 return name;
  }
  
  public String getRightPower(String date,String time)
  {
	   	  
		  String query=
			        String.format("SELECT %s FROM %s WHERE %s = %s AND %s = %s",
			                      RIGHTPOWER,TABLE,DATE,date,TIME,time);

		Cursor result =  getReadableDatabase().rawQuery(query, null);
				  
	    String name = result.getString(0);
	 
	    result.close();
		 return name;
  }
  
  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion,
                        int newVersion) {
    throw new RuntimeException("not for this project");
  }
}

