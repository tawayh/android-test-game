package fi.game.Zoambie;

import fi.game.Zoambie.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
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

		        // Peli looppi
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
	        
		        // T‰‰ nyt oli monessa esimerkiss‰ mukana
		        // En tii‰ onko pakollinen
		        // T‰t‰ vois k‰ytt‰‰ collisionDetectioniin
		        private void updatePhysics() {
			        player.updateMovement(pressedX, pressedY);
			        
			        if (enemyList.size() != 0) {
			        	for (int i = 0; i < enemyList.size(); i++) {
			        		try {
			        			Character c = enemyList.get(i);

				        		//Jos zombi on 30 px p‰‰ss‰ playerista, zombi kuolee
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
		        	//Alustetaan pelipˆyt‰ piirt‰m‰ll‰ tausta
		        	canvas.drawBitmap(background, 0, 0, null);	
		        	//TODO - piirto metodissa paintissa null (kumpi parempi null/paint)
		        	
		        	//Piirret‰‰n pelaaja
		        	canvas.drawBitmap(player.getBitmap(), player.getX(), player.getY(), null);
		        	
		        	//Piirret‰‰n moottorisaha
		        	
		        	Matrix matrix = new Matrix();

		        	//move image

		        	//matrix.setTranslate(player.getX() + (player.getBitmap().getWidth() / 2), player.getY() + (player.getBitmap().getWidth() / 2));

		        	//rotate image, getXPos, getYPos are x & y coords of the image

		        	Matrix newtest = new Matrix();
		        	newtest.postRotate(-25);
		        	Bitmap testi_bmp = Bitmap.createBitmap(saw.getBitmap(), 0, 0, saw.getBitmap().getWidth(), saw.getBitmap().getHeight(), newtest, true);
		        	newtest.setTranslate(player.getX() + (player.getBitmap().getWidth() / 2), player.getY() + (player.getBitmap().getWidth() / 2));
		        	//matrix.postRotate((float)saw.getDirection(), player.getX() - (saw.getBitmap().getWidth() / 2), (float)player.getY() - (saw.getBitmap().getWidth() / 2));
		        	//canvas.drawBitmap(saw.getBitmap(), matrix, paint);
		        	canvas.drawBitmap(testi_bmp, newtest, paint);
		        	
		        	
		        	
		        	
		        	//Piirret‰‰n zombit
		        	Character c = null;
		        	for (int i = 0; i < enemyList.size(); i++) {
			        	c = (Character) enemyList.get(i);
			        	canvas.drawBitmap(c.getBitmap(), c.getX(), c.getY(), null);
		        	}
		        	c = null;
		        	canvas.restore();
		        }
		        
		        // Metodi mill‰ voidaan pys‰ytt‰‰ threadi
		        public void setRunning(boolean newRunning) {
		            running = newRunning;
		        }
		        
		    } // <---   AnimationThread luokka p‰‰ttyy
			

			
			
	public int screenWidth;
	public int screenHeight;
    private AnimationThread thread;
    private float pressedX;
    private float pressedY;
    private float dpadX;
    private float dpadY;
    //private float lastX;		//Jos ei olla tˆk‰tyy, niin ei liikuta
    //private float lastY;		//Sama juttu
    public Character player;
    public Bitmap background;
    public Saw saw;
    //public Character enemy;
    //lastMeasuredTimell‰ lasketaan milloin zombeja luodaan
    long lastMeasuredTime = System.currentTimeMillis();;
    
    //TODO onko arraylist parhain, vai esim linkitettylista
    //ArrayList<Character> enemyList = new ArrayList<Character>();
    LinkedList<Character> enemyList = new LinkedList<Character>();
    
    // Konstruktori miss‰ luodaan threadi
    public AnimationView(Context context, AttributeSet attrs) {
        super(context, attrs);
        SurfaceHolder holder = getHolder();
        holder.addCallback(this);
        thread = new AnimationThread(holder); //Threadi luodaan, k‰ynnistyy surfaceCreated()
    }
    
    
    
    // Napataan X ja Y koordinaatit kun tˆk‰t‰‰n ruutua
    @Override
    public boolean onTouchEvent(MotionEvent event) {
    	boolean eventHandled = true;
    	try {
    		float x = event.getX();
    		float y = event.getY();
    		if (y > 240 && x < 240)	 {
    			//dpadX = x;
    			//dpadY = y;
    			saw.updateDirection(x, y, 360, 120);
    		}
    		else {
    			pressedX = x;
    			pressedY = y;
    		}
    	}
    	catch (Exception e) {
    		eventHandled = false;
    	}
    	return eventHandled;
    }
    
    
    
    
    public void createZombie() {

    	//Nyt siis tulee sekunni v‰lein mˆrkˆj‰
    	if (System.currentTimeMillis() - lastMeasuredTime > 1000   &&  enemyList.size() < 500) {

    		//ZOmbin random aloitus sijainti
	    	Character enemy;
	    	
	    	List list = randomZombieLocation();
	    	double x = (Double) list.get(0);
	    	double y = (Double) list.get(1);
	    	
	    	enemy = new Character( 
		            BitmapFactory.decodeResource(getResources(), R.drawable.enemy),
		            (int)x,
		            (int)y,
		            0.25
		    );
	    	addEnemy(enemy);
	    	lastMeasuredTime = System.currentTimeMillis();
    	}
    }
    
    public List randomZombieLocation() {
    	List list = new ArrayList();
    	int side = 0;
    	double x = 0;
    	double y = 0;
    	
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
    // Kun surface luodaan, niin threadi aloitetaan t‰ss‰ automaattisesti
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