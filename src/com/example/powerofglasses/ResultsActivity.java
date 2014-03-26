package com.example.powerofglasses;



import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ResultsActivity extends Activity implements OnClickListener{
	
	private double[] results = null;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.results_layout);
		
		//Get the bundle
	    Bundle bundle = getIntent().getExtras();

	    //Extract the data…
	    results = bundle.getDoubleArray("results");   
	   TextView textBoxLeft = (TextView)findViewById(R.id.resultstextboxleft);
	   TextView textBoxRight = (TextView)findViewById(R.id.resultstextboxright);
	   textBoxLeft.setText(String.valueOf(results[0]));
	   textBoxRight.setText(String.valueOf(results[1]));
	   
	   //start listening to the button click of saveresults
	   Button saveButton = (Button)findViewById(R.id.savebutton);
	   saveButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId())
		{
		case R.id.savebutton : 
			Intent i = new Intent(this, LogActivity.class);
			Bundle bundle = new Bundle();
			bundle.putDoubleArray("results", results);
			i.putExtras(bundle);
			startActivity(i);
			break;
		default:
			break;
		}
	}

}
