package fi.loo.VLCRemote;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	private boolean fullscreen;
	private int volumeVal;
	private int positionVal;
	SeekBar volume;
	SeekBar position;
	InputStream connection;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        

        Button play = (Button)findViewById(R.id.play);
        play.setOnClickListener(playListener);
        
        Button pause = (Button)findViewById(R.id.pause);
        pause.setOnClickListener(pauseListener);
        
        Button next = (Button)findViewById(R.id.next);
        next.setOnClickListener(nextListener);
        
        Button previous = (Button)findViewById(R.id.previous);
        previous.setOnClickListener(previousListener);
        
        volume  = (SeekBar)findViewById(R.id.volume);
        volume.setOnSeekBarChangeListener(volumeListener);
        
        position  = (SeekBar)findViewById(R.id.position);
        position.setOnSeekBarChangeListener(positionListener);
    }
    
    @Override
    public void onResume(){
    	super.onResume();
    	
    	try {
    		System.out.println("##################################################");
    		connection = Control.OpenHttpConnection("http://192.168.0.1:39589");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//TODO kysyt‰‰n arvot
    	//TODO Create conection
		//TODO arvojen muuttaminen
    }
    
    @Override
    public void onPause(){
    	super.onPause();
    	/*
    	try {
			connection.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		*/
    	volumeVal = volume.getScrollX();
    	positionVal = position.getScrollX();
    	//Get fullscreen
    	//TODO pause conection
    }
    
    @Override
    public void onDestroy(){
    	super.onDestroy();
    	//TODO destrouy everything
    }
    
    
    private OnClickListener pauseListener = new OnClickListener() {
        public void onClick(View v) {                        
           //TODO
        }
    };  
    
    private OnClickListener playListener = new OnClickListener() {
        public void onClick(View v) {                        
           //TODO

	        try {
	        	URL url = new URL("http://www.cs.helsinki.fi/u/tomikosk/");
	        	   HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
	        	   urlConnection.connect();
	        	   try {
	        	     InputStream in = new BufferedInputStream(urlConnection.getInputStream());
	        	     //readStream(in);
	        	    System.out.println("############################" + urlConnection.getURL());
	        	   }
	        	   finally {
		        	     urlConnection.disconnect();
		        	     urlConnection = null;
	        	   }
				
			} catch (Exception e) {
				System.out.println("########################################### JAJAHJAHA GIGAGAI");
				e.printStackTrace();
			}
        }
    };  
    
    private OnClickListener nextListener = new OnClickListener() {
        public void onClick(View v) {                        
           //TODO
        }
    };  
    
    private OnClickListener previousListener = new OnClickListener() {
        public void onClick(View v) {                        
           //TODO
        }
    }; 
    
    private OnSeekBarChangeListener volumeListener = new OnSeekBarChangeListener() {

		@Override
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			// TODO Auto-generated method stub
			
		}
    }; 
    
    private OnSeekBarChangeListener positionListener = new OnSeekBarChangeListener() {

        @Override
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			// TODO Auto-generated method stub
			
		}
    }; 
}