package com.example.powerofglasses;


import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;


import Catalano.Imaging.FastBitmap;
import Catalano.Imaging.Filters.Grayscale;
import Catalano.Imaging.Filters.Resize;
import Catalano.Imaging.Filters.Threshold;
import Catalano.Imaging.Tools.ImageStatistics;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends FragmentActivity implements OnClickListener {

	String normalPicName ="";
	String rightPicName ="";
	String leftPicName ="";
	
	float normalBlackRatio = 0;
	float leftBlackRatio = 0;
	float rightBlackRatio = 0;
	
	float ExperimentalData[][] = new float[][]{
			  
			  { -10, (float)0.206896551724138},
			  { -9, (float)0.229885057471264},
			  { -8, (float)0.258620689655172},
			  { -7, (float)0.304597701149425},
			  { -6, (float)0.344827586206897},
			  { -5, (float)0.408045977011494},
			  { -4, (float)0.477011494252874},
			  { -3, (float)0.551724137931034},
			  { -2, (float)0.666666666666667},
			  { -1, (float)0.810344827586207},
			  { 0, 1},
			  { 1, (float)1.27011494252874},
			  { 2, (float)1.59770114942529},
			  { 3, (float)1.8448275862069},
			  { 4, (float)2.29166666666667},
			 
			  
			 
			};
	
	Threshold bradley;
	Grayscale g;


		//this.listener=listener;

	//instance of camera
	private Camera mCamera;
	//instance of the surfaceview
	private CameraPreview cameraPreview;
	// the framelayout to plug the surfacepreview
	private FrameLayout frameLayout;
	private ImageButton takePictureButton;
	private TextView textBox;
	private int stepNumber = 1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
			
		bradley = new Threshold();
	    bradley.setValue(25);
	    g = new Grayscale();
		
	    
	    
	    
		//get the button to take picture
		takePictureButton = (ImageButton)findViewById(R.id.take_picture_button);
		takePictureButton.setOnClickListener(this);
	        
	   //get the textview
		textBox = (TextView)findViewById(R.id.textbox);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	/** Check if this device has a camera */
	private boolean checkCameraHardware(Context context) {
	    if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
	        // this device has a camera
	        return true;
	    } else {
	        // no camera on this device
	        return false;
	    }
	}
	
	
	
	
	/** A safe way to get an instance of the Camera object. */
	public static Camera getCameraInstance(){
		Camera c = null;
		
	    try {
	        c = Camera.open(); // attempt to get a Camera instance
	    }
	    catch (Exception e){
	        // Camera is not available (in use or does not exist)
	    }
	    return c; // returns null if camera is unavailable
	}

	
	@Override 
	protected void onResume()
	{
		super.onResume();
		if(mCamera == null)
		{
			//set the camera instance 
			if (checkCameraHardware(this)==false)
				this.finish();
			else if((mCamera=getCameraInstance())==null)
				this.finish();
			else
			{
		
				cameraPreview = new CameraPreview(this, mCamera);
		        frameLayout = (FrameLayout) findViewById(R.id.camera_preview);
		        frameLayout.addView(cameraPreview);
			}
			
		}
	}
	
	
	 @Override
	    protected void onPause() {
	        super.onPause();
	        //releaseCamera();
	        Log.d("", "in pause");

	    }
	 
	 private void releaseCamera(){
	        if (mCamera != null){
	            mCamera.release();        
	            mCamera = null;
	        }
	    }
	 
	 @Override
	 public void onDestroy(){
		 releaseCamera(); 

		 Log.d("", "in ondestroy");
		
		 try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 System.exit(0);
		 
	 }
    
	@Override
	public void onClick(View clickedView) {
		// TODO Auto-generated method stub

		switch(clickedView.getId())
		{
		case R.id.take_picture_button:
			takePicture();
			break;
		}
	}
	
	private void takePicture()
	{
	  SimpleDateFormat dateFormat = new SimpleDateFormat("yyyymmddhhmmss");
   	  String date = dateFormat.format(new Date());
   	  
   	  //check if the left or right pic is being taken to name the 
   	  //image accordingly
   	  String preString = null;
   	  if(stepNumber == 1)
   	  {
   		  preString = "Normal";
   		  normalPicName = preString + date + ".jpg";
   	  }
   	  else if (stepNumber == 2)
   	  {
   		  preString = "Left";
   		leftPicName = preString + date + ".jpg";
   	  }
   	  else if (stepNumber == 3)
   	  {
   		  preString = "Right";
   		rightPicName = preString + date + ".jpg";
   	  }
   	  String file_name = preString + date + ".jpg";
   	 
   	  //take the picture
   	  mCamera.takePicture(null, null,
 		        new PhotoHandler(getApplicationContext(),file_name,(MainActivity)this));
   	     	 
 	  mCamera.startPreview();
	}
	
//call back received from photohandler 
// can prompt the user to proceed to next step
//or repeat the same step	
public void pictureTaken()
{
	FragmentManager manager = this.getSupportFragmentManager();
	 new PictureConfirmationDialogue().show(manager,"confirmation");
            
}
//called from the modal dailog on 
//the user selecting to proceed
public void proceedToNextStep()
{
	switch(stepNumber)
	{
	
	case 1:
		textBox.setText(getString(R.string.left_lens_measurement));
		stepNumber++;
		break;
	case 2:
		textBox.setText(getString(R.string.right_lens_measurement));
		stepNumber++;
		break;
	case 3:
		ProgressDialog Asycdialog;
		Asycdialog = ProgressDialog.show(MainActivity.this, "",
                "Calculating .. Please wait ...");
		normalBlackRatio = getBlackRatio(normalPicName);
		
		leftBlackRatio = getBlackRatio(leftPicName);
		
		rightBlackRatio = getBlackRatio(rightPicName);
		//new processImage().execute("");
		double[] results = {getPower(leftBlackRatio/normalBlackRatio),getPower(rightBlackRatio/normalBlackRatio)};//getResults();
		Intent i = new Intent(this, ResultsActivity.class);
		
		Bundle bundle = new Bundle();
		bundle.putDoubleArray("results",results);  
	    i.putExtras(bundle);
	    Asycdialog.hide();
	    //Fire the second activity
	    startActivity(i);
		break;
	default :
		break;
	}
	
}
//called from the modal dialog on
//the user selecting to repeat the same step
public void repeatStep()
{
//do nothing	
}

private void getResults()
{

}





protected float getBlackRatio(String filename)
{
	 Log.d("",filename);
	 Log.d("","inside function");
	 BitmapFactory.Options options = new BitmapFactory.Options();
	 options.inSampleSize = 3;
    options.inPreferredConfig = Bitmap.Config.ARGB_8888;
    
    File pictureFileDir = getDir();


    filename = pictureFileDir.getPath() + File.separator + filename;

    
    Bitmap bm = BitmapFactory.decodeFile(filename, options);
  Log.d("","after getting image");
    int width = bm.getWidth();
    int height = bm.getHeight();
    int scaleWidth =  width/4;
    int scaleHeight = height/4;
    // CREATE A MATRIX FOR THE MANIPULATION
    Matrix matrix = new Matrix();     
    matrix.postRotate(90);
    bm =Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
    Resize r = new Resize(scaleWidth, scaleHeight);

    FastBitmap fb = new FastBitmap(bm);
    r.applyInPlace(fb);

    g.applyInPlace(fb);
		 // Apply Bradley local threshold

    bradley.applyInPlace(fb);
	 ImageStatistics ms = new ImageStatistics(fb);
		 
	float blackRatio =(1- ms.Mean(fb)) ;
	return blackRatio;
}



private File getDir() {
    File sdDir = Environment
      .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
    return new File(sdDir, getApplicationContext().getString(R.string.foldername));
  }


private float getPower(float ratio){
	
	float min_diff=10;
	float min_power = 0;
	float diff =0;
	for(int i=0;i<ExperimentalData.length;i++)
	{
		diff = Math.abs(ExperimentalData[i][1] - ratio);
		if(diff< min_diff)
		{
			min_diff = diff;
			min_power = ExperimentalData[i][0];
		}
	}
	
	return min_power;
	
}


}

	




