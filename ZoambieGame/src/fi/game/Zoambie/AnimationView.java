package fi.game.Zoambie;

import fi.game.Zoambie.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.Iterator;

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
		            
		        }
		        
		        
		        // Peli looppi
		        // T‰‰ runko on just sellanen mit‰ n‰kee joka esimerkiss‰
		        // En ymm‰rr‰ kunnol t‰t, mut se on tehokas ja toimii
		        @Override
		        public void run() {
		            while (running) {
		                Canvas canvas = null;
		                try {
		                    canvas = surfaceHolder.lockCanvas(null);
		                    synchronized (surfaceHolder) {
		                    	removeKilledZombies();
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

			        	for (Iterator<Character> it = enemyList.iterator(); it.hasNext();) {
			        		try {
			        			
			        			
				        		Character c = (Character) it.next();
				        		
				        		
				        		//Jos zombi on 30 px p‰‰ss‰ playerista, zombi kuolee
				        		if (c.getDistance() < 30) {
				        			c.get_damage(200);
				        			killedZombies.add(c);
				        			//Character temp = c;
				        			if (it.hasNext())
				        				c = it.next();
				        			
				        		}
				        		
				        		c.updateMovement(player.getX(), player.getY());
				        		c.updateDistance(player.getX(), player.getY());
				        		
				        		
			        		}
			        		catch (Exception e) {
			        			System.out.println("SHIT");
			        		}
			        	}
			        }
			        
		            
		            createZombie();
			            
		            					
		            
		        }
		        
		        // Piirto-metodi
		        private void doDraw(Canvas canvas) {
		        	canvas.drawBitmap(background, 0, 0, null);	//Alustetaan piirt‰m‰ll‰ tausta
		        	//TODO - tarviiko alla oleva painttia vai toimisiko nopeammin nullilla
		        	canvas.drawBitmap(player.getBitmap(), player.getX(), player.getY(), paint);
		        	//TODO piirr‰ kaikki enemyt listasta
		        	for (Iterator<Character> it = enemyList.iterator(); it.hasNext();) {
			        	Character c = (Character) it.next();
			        	canvas.drawBitmap(c.getBitmap(), c.getX(), c.getY(), paint);
		        	}
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
    //private float lastX;		//Jos ei olla tˆk‰tyy, niin ei liikuta
    //private float lastY;		//Sama juttu
    public Character player;
    public Bitmap background;
    //public Character enemy;
    //lastMeasuredTimell‰ lasketaan milloin zombeja luodaan
    long lastMeasuredTime = System.currentTimeMillis();;
    
    //TODO onko arraylist parhain, vai esim linkitettylista
    ArrayList<Character> enemyList = new ArrayList<Character>();
    //TODO killedZombie listasta pit‰‰ p‰‰st‰ eroon
    ArrayList<Character> killedZombies = new ArrayList<Character>();
    
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
    		pressedX = event.getX();
    		pressedY = event.getY();
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
	    	double y = 0;
	    	double x = (Math.random() * screenWidth+100) - 50;
	    	if (x < 0)
	    		y = (Math.random()*screenHeight+100) - 50;
	    	else if (x > 850)
	    		y = (Math.random()*screenHeight+100) - 50;
	    	else {
	    		double rand = Math.random();
	    		if (rand < 0.5) 
	    			y = screenWidth+50;
	    		else
	    			y = -50;
	    	}
	    	
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
    
    
    
    
    public void addEnemy(Character c) {
    	enemyList.add(c);
    }
    
    public void addKill(Character c) {
    	killedZombies.add(c);
    }
    
    public void removeEnemy(Character c) {
    	int i = enemyList.indexOf(c);
    	enemyList.remove(i);
    }
    
    
    // TODO t‰st‰ metodista pit‰is p‰‰st‰ eroon
    public void removeKilledZombies() {
    	for (Iterator<Character> it = killedZombies.iterator() ; it.hasNext();) {
    		Character c = (Character)it.next();
    		if (enemyList.contains(c)) {
    			enemyList.remove(c);
    			c = null;
    			System.out.println("KILLED");
    		}
    	}
    	killedZombies = null;
    	killedZombies = new ArrayList<Character>();
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