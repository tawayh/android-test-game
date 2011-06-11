package fi.game.Zoambie;

import fi.game.Zoambie.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
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

		        // Konstruktori, luodaan pelaaja, saha ja fps countteri
		        public AnimationThread(SurfaceHolder newSurfaceHolder) {
		            surfaceHolder = newSurfaceHolder;
		            background = BitmapFactory.decodeResource(getResources(), R.drawable.ground);
		            paint = new Paint();
		            player = new Character( 
		            	BitmapFactory.decodeResource(getResources(), R.drawable.player),
		            	200,	//x
		            	400,	//y
		            	2,		//Speed
		            	200,	//Health
		            	4,		//Number of frames
			            40,		//Width
			            50,		//Height
			            0		//type
		            );
		            saw = new Saw(
		            		BitmapFactory.decodeResource(getResources(), R.drawable.saw), 
		            		player.getX(),
		            		player.getY()
		            	);
		            
		            nextCount = System.currentTimeMillis() + 1000;
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
		        	//Pelaajan sijainti, onko ruudun ulkopuolella ja sahan tappopisteen päivitys
			        player.updateMovement(-1, -1);
			        player.checkCorners(screenWidth, screenHeight);
			        saw.setKillPoint(player.getX()+ (player.getWidth()/2) , player.getY() + (player.getHeight()/2));
			        saw.updateDirection();
			        
			        //Päivitetään pelaajan animaatiota
        			player.updateAnimation(System.currentTimeMillis());
			        
			    	//Päivitetään lohkojen suunta
			        for (int i = 0; i<8; i++) {
			        	gameArea[i].updateDirection(player);
			        }
			        
        			//Päivitetään tarvittaessa pelaajan lohko toiseen
        			if (!gameArea[player.getBlockNumber()].getRect().contains((int)player.getX(), (int)player.getY())){
        				for (int j = 0; j<8; j++) {
        					if (gameArea[j].getRect().contains((int)player.getX(), (int)player.getY())){
        						player.setBlockNumber(j);
        						break;
        					}
        				}
        			}
			        
			        frames ++;
			        
			        //FPS laskenta
			        if (System.currentTimeMillis() >= nextCount) {
			        	fps = frames;
			        	frames = 0;
			        	nextCount = System.currentTimeMillis()+1000;
			        	
			        }
			        
			        //Maksimi zombi määrän laskenta fps:n mukaan
			        if (fps < 30) {
			        	maxZombies = enemyList.size();
		        	}
		        	if (fps > 30 && maxZombies < 1000) {
		        		maxZombies += 1;
		        	}
			        
		        	
		        	//Käydään zombi lista läpi
			        if (enemyList.size() != 0) {
			        	for (int i = 0; i < enemyList.size(); i++) {
			        		try {
			        			Character c = enemyList.get(i);
			        				
			        			//Päivitetään tarvittaessa zombin lohko toiseen
			        			if (!gameArea[c.getBlockNumber()].getRect().contains((int)c.getX(), (int)c.getY())){
			        				for (int j = 0; j<8; j++) {
			        					if (gameArea[j].getRect().contains((int)c.getX(), (int)c.getY())){
			        						c.setBlockNumber(j);
			        						break;
			        					}
			        				}
			        			}
			        			
			        			//Päivitetään zombin animaatiota
			        			c.updateAnimation(System.currentTimeMillis());
			        			
			        			//Katsotaan onko pelaaja samassa lohkossa kuin zombi
			        			if (player.getBlockNumber() == c.getBlockNumber())
			        				c.updateMovement(player.getX(), player.getY());
			        			else { 
			        				c.setDirection(gameArea[c.getBlockNumber()].getDirection());
			        				c.updateMovement(-1, -1);
			        			}
				        		if (c.collisionPlayer(player)) {
				        			if (c.getType() == 1) {
				        				player.get_damage(5);
				        				player.setX((float) (player.getX() + (Math.cos(c.getDirection()) * (-5))) );
				        				player.setY((float) (player.getY() + (Math.sin(c.getDirection()) * (-5))) );
				        			
				        			}
				        			else if (c.getType() == 2) {
				        				player.get_damage(1);
				        				player.setX((float) (player.getX() + (Math.cos(c.getDirection()) * (-2))) );
				        				player.setY((float) (player.getY() + (Math.sin(c.getDirection()) * (-2))) );
				        			
				        			}
				        			else if (c.getType() == 3) {
				        				player.get_damage(10);
				        				player.setX((float) (player.getX() + (Math.cos(c.getDirection()) * (-10))) );
				        				player.setY((float) (player.getY() + (Math.sin(c.getDirection()) * (-10))) );
				        			
				        			}
				        		
				        			
				        			
				        			//vanhaa shaissee, piti piirtää ruutu punaiseksi kun player sai hittii
				        			//ColorMatrix cm = new ColorMatrix();
						        	//cm.setScale (5f, 0f, 0f, 0f);
						        	//ColorMatrixColorFilter cmcf = new ColorMatrixColorFilter(cm);
						        	//paint.setColorFilter(cmcf);
				        		}
				        		//else {
				        		//	paint = new Paint();
				        		//}
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
		        	
		        	//canvas.d
		        	canvas.drawBitmap(background, 0, 0, paint);
		        	player.draw(canvas);
		        	
		        	//Piirretään zombit
		        	Character c = null;
		        	for (int i = 0; i < enemyList.size(); i++) {
			        	c = (Character) enemyList.get(i);
			        	c.draw(canvas);
		        	}
		        	saw.draw(canvas, player);		        	
		        	Control.drawDpad(canvas, screenWidth, screenHeight);
		        	Control.drawHealth(canvas, screenWidth, player);	        	
		        	
		        	//GAME OVER MAN, GAME OVER
		        	if (player.getHealth() <= 0) {
		        		Control.drawGameOver(canvas);
			        	setRunning(false);
		        	}
		        	Control.drawGUI(canvas, enemyList, killedZombies, fps);
		        }
		        
		        // Metodi millä voidaan pysäyttää threadi
		        public void setRunning(boolean newRunning) {
		            running = newRunning;
		        }
		        
		    } // <---   AnimationThread luokka päättyy
			
			
			
	public int frames;
	public int fps;
	public int wanted_fps = 25;
	public long nextCount;
	public Block[] gameArea = new Block[8];
	public int zombieSpawn = 2000;
	public int killedZombies = 0;
	public int maxZombies = 1000;
	public SensorManager sensorManager;
	public int screenWidth;
	public int screenHeight;
    private AnimationThread thread;
    public Character player;
    public Bitmap background;
    public Bitmap enemyBmp;
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
        enemyBmp = Bitmap.createBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.enemy));
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
    			saw.updateTargetDirection(x, y, screenHeight/4, (screenHeight/2 + screenHeight/4));
    		}
    		else if (y > screenHeight/2 && x > (screenWidth-(screenHeight/2)))	 {
    			saw.updateTargetDirection(x, y, screenWidth-(screenHeight/4), (screenHeight/2 + screenHeight/4));
    		}
    	}
    	catch (Exception e) {
    		eventHandled = false;
    	}
    	return eventHandled;
    }
    
    
    public void createZombie() {
    	//Zombeja syntyy kiihtyvästi zombieSpawn numeron mukaan
    	if (System.currentTimeMillis() - lastMeasuredTime > zombieSpawn   &&  enemyList.size() < maxZombies) {

    		//Zombien spawnaus
	    	Character enemy = Control.createZombie(enemyBmp, screenWidth, screenHeight);
	    	addEnemy(enemy);
	    	lastMeasuredTime = System.currentTimeMillis();
	    	if (zombieSpawn > 100)
	    		zombieSpawn = zombieSpawn - 50;
	    	else if (zombieSpawn < 100 && zombieSpawn > 10)
	    		zombieSpawn = zombieSpawn - 1;
    	}
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
            gameArea = Control.createGameArea();
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