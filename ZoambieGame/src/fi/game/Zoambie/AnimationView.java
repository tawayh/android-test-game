package fi.game.Zoambie;

import fi.game.Zoambie.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

class AnimationView extends SurfaceView implements SurfaceHolder.Callback {
    
			//Luodaan oma threadi
			class AnimationThread extends Thread {
				private boolean running;
		        private SurfaceHolder surfaceHolder;
		        private Paint paint;

		        // Konstruktori
		        public AnimationThread(SurfaceHolder newSurfaceHolder) {
		            surfaceHolder = newSurfaceHolder;
		            background = BitmapFactory.decodeResource(getResources(), R.drawable.ground);
		            paint = new Paint();
		            player = new Character( 
		            	BitmapFactory.decodeResource(getResources(), R.drawable.player),
		            	200,
		            	400,
		            	2
		            );
		            saw = new Saw(
		            		BitmapFactory.decodeResource(getResources(), R.drawable.saw), 
		            		player.getX(),
		            		player.getY()
		            	);
		        }

		        // Pelilooppi
		        @Override
		        public void run() {
		            while (running) {
		                Canvas canvas = null;
		                try {
		                    canvas = surfaceHolder.lockCanvas(null);
		                    synchronized (surfaceHolder) {
		                    	updatePhysics();
		                        doDraw(canvas);
		                    }
		                }finally {
		                    if (canvas != null) {
		                        surfaceHolder.unlockCanvasAndPost(canvas);
		                    }
		                }
		            }
		        }
	        
		        private void updatePhysics() {
			        player.updateMovement(-1, -1);
			        
			        //Varmistukset ettei pelaaja mene ruudun ulkopuolelle
			        if (player.getX() < 0) {
			        	player.setX(0);
			        }
			        else if (player.getX()+player.getWidth() > screenWidth) {
			        	player.setX(screenWidth-player.getWidth());
			        }
			        if (player.getY() < 0) {
			        	player.setY(0);
			        }
			        else if (player.getY()+player.getHeight() > screenHeight) {
			        	player.setY(screenHeight-player.getHeight());
			        }
			        
			        if (enemyList.size() != 0) {
			        	for (int i = 0; i < enemyList.size(); i++) {
			        		try {
			        			Character c = enemyList.get(i);

				        		//Jos zombi on 30 px päässä playerista, zombi kuolee
				        		if (c.getDistance() < 30) {
				        			c.get_damage(200);
				        			if (c.getHealth() <= 0) {
				        				synchronized (surfaceHolder) {
				        					enemyList.remove(c);
				        					createZombie();
				        				}
				        			}
				        		}
				        		c.updateMovement(player.getX(), player.getY());
				        		c.updateDistance(player.getX(), player.getY());
			        		}
			        		catch (Exception e) {
			        			System.out.println("SHIT");
			        			setRunning(false);
			        		}
			        	}
			        }
		            createZombie();
		        }
		        
		        // Piirto-metodi
		        private void doDraw(Canvas canvas) {
		        	canvas.drawBitmap(background, 0, 0, null);	
		        	canvas.drawBitmap(player.getBitmap(), player.getX(), player.getY(), null);
		        	
		        	//Piirretään moottorisaha
		        	//Edelleenkään ei skulaa, setRotaten pivotit pitäis saada kuntoon tjs.
		        	Matrix newtest = new Matrix();
		        	newtest.setRotate((float)saw.getDirection(), (float)25, (float)20);
		        	Bitmap testi_bmp = Bitmap.createBitmap(saw.getBitmap(), 0, 0, saw.getBitmap().getWidth(), saw.getBitmap().getHeight(), newtest, true);
		        	newtest.setTranslate(player.getX()+(player.getWidth()/2), player.getY()+(player.getHeight()/2));
		        	canvas.drawBitmap(testi_bmp, newtest, paint);
		        	
		        	
		        	
		        	
		        	//Piirretään zombit
		        	Character c = null;
		        	for (int i = 0; i < enemyList.size(); i++) {
			        	c = (Character) enemyList.get(i);
			        	canvas.drawBitmap(c.getBitmap(), c.getX(), c.getY(), null);
		        	}
		        	c = null;
		        	
		        	
		        	canvas.restore();
		        }
		        
		        // Metodi millä voidaan pysäyttää threadi
		        public void setRunning(boolean newRunning) {
		            running = newRunning;
		        }
		        
		    } // <---   AnimationThread luokka päättyy
			

			
	public SensorManager sensorManager;
	public int screenWidth;
	public int screenHeight;
    private AnimationThread thread;
    public Character player;
    public Bitmap background;
    public Saw saw;
    long lastMeasuredTime = System.currentTimeMillis();;
    LinkedList<Character> enemyList = new LinkedList<Character>();
    private final SensorEventListener orientationSensorEventListener = new SensorEventListener() {
		public void onSensorChanged(SensorEvent event) {
            updateOrientation(//event.values[0],
                              event.values[1],
                              event.values[2]
                              //true
                              );
		}
        public void onAccuracyChanged(Sensor sensor, int accuracy) {
        }
    };
    
    
    
    // Konstruktori missä luodaan threadi ja listataan sensorit
    public AnimationView(Context context, AttributeSet attrs) {
        super(context, attrs);
        SurfaceHolder holder = getHolder();
        holder.addCallback(this);
        thread = new AnimationThread(holder); //Threadi luodaan, käynnistyy surfaceCreated()
    
    
        sensorManager = (SensorManager) getContext().getSystemService(Context.SENSOR_SERVICE);

        
        List<Sensor> sensorList;
        sensorList = sensorManager.getSensorList(Sensor.TYPE_ORIENTATION);
        try {
        	sensorManager.registerListener(orientationSensorEventListener, sensorList.get(0), SensorManager.SENSOR_DELAY_GAME);
        } catch(IndexOutOfBoundsException e) {
        	//Tarvitaan try-catch emulaattorilla ajamista varten
        }
    }
    
    
    
    
    
	private void updateOrientation( float pitch, float roll ) {
		double direction;
		double speed = 0;
		
		double dy = (double) 0 - (double) roll;
		double dx = (double) 0 - (double) pitch;
		
		direction = ( Math.atan2(-dy, dx)  + (float)Math.PI );
		if (roll < 0) 
			speed += -roll; 
		else
			speed += roll;
		if (pitch < 0)
			speed += -pitch;
		else
			speed += pitch;
		
		//Alempi arvo on saatu kokeilun kautta, se on hyvä
		speed = speed / 10;
		
		if (speed > 2)
			speed = 2;
		
		player.setDirection(direction);
		player.setSpeed(speed);
	}
    
    
    
    // Napataan X ja Y koordinaatit kun tökätään ruutua
    @Override
    public boolean onTouchEvent(MotionEvent event) {
    	boolean eventHandled = true;
    	try {
    		float x = event.getX();
    		float y = event.getY();
    		if (y > 240 && x < 240)	 {
    			//dpadX = x;
    			//dpadY = y;
    			saw.updateDirection(x, y, 120, 360);
    		}
    	}
    	catch (Exception e) {
    		eventHandled = false;
    	}
    	return eventHandled;
    }
    
    
    public void createZombie() {

    	//Nyt siis tulee sekunni välein mörköjä
    	if (System.currentTimeMillis() - lastMeasuredTime > 1000   &&  enemyList.size() < 500) {

    		//Zombien spawnaus
	    	Character enemy;
	    	
	    	List<Integer> list = randomZombieLocation();
	    	int x = (int) list.get(0);
	    	int y = (int) list.get(1);
	    	
	    	double speed = 0.3 * Math.random();
	    	if (speed < 0.05)
	    		speed = 0.05;
	    	
	    	enemy = new Character( 
		            BitmapFactory.decodeResource(getResources(), R.drawable.enemy),
		            x,
		            y,
		            speed
		    );
	    	addEnemy(enemy);
	    	lastMeasuredTime = System.currentTimeMillis();
    	}
    }
    
    public List<Integer> randomZombieLocation() {
    	List<Integer> list = new ArrayList<Integer>();
    	int side = 0;
    	int x = 0;
    	int y = 0;
    	
    	//Randomly choose the side of the screen where the zombie is going to spawn
    	double random = Math.random();
    	if(random <= 0.25) {
    		side = 1;
    	}
    	else if(random > 0.25 && random <= 0.50) {
    		side = 2;
    	}
    	else if(random > 0.50 && random <=0.75) {
    		side = 3;
    	}
    	else if(random > 0.75) {
    		side = 4;
    	}
    	
    	//Randomly choose the spot where the zombie is going to spawn on the specific side
    	if (side == 1) {
    		x = screenWidth;
    		y = (int) (screenHeight*Math.random());
    		list.add(x);
    		list.add(y);
    	}
    	else if (side == 2) {
    		x = 0;
    		y = (int) (screenHeight*Math.random());
    		list.add(x);
    		list.add(y);
    	}
    	else if (side == 3) {
    		y = 0;
    		x = (int) (screenWidth*Math.random());
    		list.add(x);
    		list.add(y);
    	}
    	else if (side == 4) {
    		y = screenHeight;
    		x = (int) (screenWidth*Math.random());
    		list.add(x);
    		list.add(y);
    	}
    	
    	return list;
    	
    }
    
    public void addEnemy(Character c) {
    	enemyList.add(c);
    }
    
    public void removeEnemy(Character c) {
    	int i = enemyList.indexOf(c);
    	enemyList.remove(i);
    }
    
    
    
    
    
    // Alla pakollisia metodeja kun implementoidaan SurfaceHolder
    
    // Callback invoked when the surface dimensions change.
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
    	
    	// samanaikaisesti asetetaan arvot screenwidth, -height ja
    	// muokataan background bitmappi oikean kokoiseksi
        synchronized (holder) {
            screenWidth = width;
            screenHeight = height;

            background = Bitmap.createScaledBitmap(background, width, height, true);
        }
    	
    }
    
    // Callback invoked when the Surface has been created and is ready to be used.
    // Kun surface luodaan, niin threadi aloitetaan tässä automaattisesti
    public void surfaceCreated(SurfaceHolder holder) {
        thread.setRunning(true);
        thread.start();
    }
    
    // Callback invoked when the Surface has been destroyed and must no longer
    // be touched. WARNING: after this method returns, the Surface/Canvas must
    // never be touched again!
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        thread.setRunning(false);
        while (retry) {
            try {
                thread.join();
                retry = false;
            } catch (InterruptedException e) {
            }
        }
    }
}