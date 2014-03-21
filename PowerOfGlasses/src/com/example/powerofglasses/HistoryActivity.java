package com.example.powerofglasses;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.AdapterView;
import android.widget.TextView;

public class HistoryActivity extends Activity implements AdapterView.OnItemSelectedListener{
	
	private Spinner spin;
	private DatabaseHelper dbHelper;
	private TextView nameTextView;
	private TextView leftTextView;
	private TextView rightTextView;
	private ArrayList<String> dates;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.history_layout);
		
		dbHelper = DatabaseHelper.getInstance(getApplicationContext());
		dates = dbHelper.getDateAndTime();
		String[] dateString = new String[dates.size()];
		dates.toArray(dateString);
		spin=(Spinner)findViewById(R.id.spinner);
	    spin.setOnItemSelectedListener(this);
	    
	    nameTextView = (TextView)findViewById(R.id.history__name_Text_View );
	    leftTextView = (TextView)findViewById(R.id.historyLeftPowerDisplay);
	    rightTextView = (TextView)findViewById(R.id.historyRightPowerDisplay);
	    
	    ArrayAdapter<String> aa=new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item,
                dateString);   
	}

	  @Override
	  public void onItemSelected(AdapterView<?> parent,
	                                View v, int position, long id) {
	   String dateAndTime = dates.get(position);
	   String[] date = dateAndTime.split(" ");
	   
	   String name = dbHelper.getName(date[0],date[1]);
	   String leftPower = dbHelper.getLeftPower(date[0],date[1]);
	   String rightPower = dbHelper.getRightPower(date[0],date[1]);
	   
	   nameTextView.setText(name);
	   leftTextView.setText(leftPower);
	   rightTextView.setText(rightPower);
	  }
	  
	  @Override
	  public void onNothingSelected(AdapterView<?> parent) {
	    
	  }
}
