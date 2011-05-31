package fi.game.Zoambie;

import fi.game.Zoambie.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

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
		            	2,
		            	200
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
			        player.checkCorners(screenWidth, screenHeight);
			        saw.setKillPoint(player.getX()+ (player.getWidth()/2) , player.getY() + (player.getHeight()/2));
			        
			        if (enemyList.size() != 0) {
			        	for (int i = 0; i < enemyList.size(); i++) {
			        		try {
			        			Character c = enemyList.get(i);

				        		c.updateMovement(player.getX(), player.getY());
				        		if (c.collisionPlayer(player)) {
				        			player.get_damage(1);
				        			player.setX((float) (player.getX() + (Math.cos(c.getDirection()) * (-10))) );
				        			player.setY((float) (player.getY() + (Math.sin(c.getDirection()) * (-10))) );
				        			ColorMatrix cm = new ColorMatrix();
						        	cm.setScale (5f, 0f, 0f, 0f);
						        	ColorMatrixColorFilter cmcf = new ColorMatrixColorFilter(cm);
						        	paint.setColorFilter(cmcf);
				        		}
				        		else {
				        			paint = new Paint();
				        		}
				        		if (c.collisionSaw(saw))
				        			c.get_damage(5);

				        		if (c.getHealth() <= 0) {
				        			synchronized (surfaceHolder) {
				        				enemyList.remove(c);
				        				createZombie();
				        				killedZombies ++;
				        			}
				        		}
			        		}
			        		catch (Exception e) {
			        			System.out.println("SHIT");
			        			//setRunning(false);
			        		}
			        	}
			        }
		            createZombie();
		        }
		        
		        // Piirto-metodi
		        private void doDraw(Canvas canvas) {
		        	canvas.drawBitmap(background, 0, 0, paint);	

		        	//Piirretään pelaaja
		        	canvas.drawBitmap(player.getBitmap(), player.getX(), player.getY(), paint);
		        	
		        	//Piirretään zombit
		        	Character c = null;
		        	for (int i = 0; i < enemyList.size(); i++) {
			        	c = (Character) enemyList.get(i);
			        	canvas.drawBitmap(c.getBitmap(), c.getX(), c.getY(), paint);
		        	}
		        	c = null;
		        	
		        	
		        	//Piirretään moottorisaha
		        	Matrix m = new Matrix();
		        	Bitmap bmp = Bitmap.createBitmap(saw.getBitmap(), 0, 0, saw.getWidth(), saw.getHeight());
		        	m.reset();
		        	m.setRotate((float)saw.getDirection(), 50f, 20f);
		        	m.postTranslate(player.getX()-30, player.getY()+5);
		        	canvas.drawBitmap(bmp, m, paint);
		        	
		        	saw.setX(player.getX()-30);
		        	saw.setY(player.getY()+5);
		        	
		        	
		        	//Piirretään D-Padin rajat ja healtit
		        	Paint p = new Paint();
		        	p.setColor(Color.BLACK);
		        	p.setStrokeWidth(2f);
		        	canvas.drawLine(0, screenHeight/2, screenHeight/2, screenHeight/2, p);
		        	canvas.drawLine(screenHeight/2, screenHeight/2, screenHeight/2, screenHeight, p);
		        	
		        	p.setColor(Color.GREEN);
		        	p.setStrokeWidth(10f);
		        	p.setAlpha(100);
		        	canvas.drawLine(20, 25, (screenWidth*((float)player.getHealth()/200f))-20, 25, p);
		        	
		        	
		        	if (player.getHealth() <= 0) {
		        		canvas.drawColor(Color.RED);
		        		p.setColor(Color.BLACK);
		        		p.setTextSize(75);
		        		float[] pos = {50,300,  125,300,  200,300,  275,300,  350,300,  425,300,  500,300,  575,300,  650,300};
			        	canvas.drawPosText("GAME OVER", pos, p);
		        	
		        	}
		        	
		        	
		        	p.setColor(Color.WHITE);
		        	p.setStrokeWidth(10f);
		        	p.setTextSize(20);
		        	p.setTextScaleX(1.2f);
		        
		        	//Tapetut zombiet
		        	float[] pos = {50,50,60,50,70,50,80,50};
		        	canvas.drawPosText(""+killedZombies, pos, p);
		        	
		        	//Zombien määrä kentällä
		        	float [] pos2 = {700,50,710,50,720,50,730,50};
		        	canvas.drawPosText(""+enemyList.size(), pos2, p);
		        	
		        	canvas.restore();
		        }
		        
		        // Metodi millä voidaan pysäyttää threadi
		        public void setRunning(boolean newRunning) {
		            running = newRunning;
		        }
		        
		    } // <---   AnimationThread luokka päättyy
			

	public int zombieSpawn = 2000;
	public int killedZombies = 0;
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
		//Saadaan sopiva kallistelu herkkyys
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
    		if (y > screenHeight/2 && x < screenHeight/2)	 {
    			//dpadX = x;
    			//dpadY = y;
    			saw.updateDirection(x, y, screenHeight/4, (screenHeight/2 + screenHeight/4));
    		}
    	}
    	catch (Exception e) {
    		eventHandled = false;
    	}
    	return eventHandled;
    }
    
    
    public void createZombie() {

    	//Zombeja syntyy kiihtyvästi zombieSpawn numeron mukaan
    	if (System.currentTimeMillis() - lastMeasuredTime > zombieSpawn   &&  enemyList.size() < 1000) {
    		//Zombien spawnaus
	    	Character enemy;
	    	
	    	Point point = randomZombieLocation();
	    	int x = point.x;
	    	int y = point.y;
	    	
	    	double speed = 0.3 * Math.random();
	    	if (speed < 0.05)
	    		speed = 0.05;
	    	
	    	enemy = new Character( 
		            BitmapFactory.decodeResource(getResources(), R.drawable.enemy),
		            x,
		            y,
		            speed,
		            20
		    );
	    	addEnemy(enemy);
	    	lastMeasuredTime = System.currentTimeMillis();
	    	if (zombieSpawn > 100)
	    		zombieSpawn = zombieSpawn - 100;
	    	else if (zombieSpawn < 100 && zombieSpawn > 10)
	    		zombieSpawn = zombieSpawn - 10;
    	}
    }
    
    public Point randomZombieLocation() {
    	Point point = new Point();
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
    		point.set(x, y);
    	}
    	else if (side == 2) {
    		x = 0;
    		y = (int) (screenHeight*Math.random());
    		point.set(x, y);
    	}
    	else if (side == 3) {
    		y = 0;
    		x = (int) (screenWidth*Math.random());
    		point.set(x, y);
    	}
    	else if (side == 4) {
    		y = screenHeight;
    		x = (int) (screenWidth*Math.random());
    		point.set(x, y);
    	}
    	
    	return point;
    	
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