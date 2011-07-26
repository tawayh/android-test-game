package fi.loo.VLCRemote;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class MainActivity extends Activity {
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
        
        SeekBar volume  = (SeekBar)findViewById(R.id.volume);
        volume.setOnSeekBarChangeListener(volumeListener);
        
        SeekBar position  = (SeekBar)findViewById(R.id.position);
        position.setOnSeekBarChangeListener(positionListener);
    }
    
    @Override
    public void onResume(){
    	//TODO Create conection
    }
    
    @Override
    public void onPause(){
    	//TODO pause conection
    }
    
    @Override
    public void onDestroy(){
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