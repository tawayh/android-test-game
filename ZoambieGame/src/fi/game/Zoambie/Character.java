package fi.game.Zoambie;

import android.graphics.*;

public class Character {
	
	private Bitmap bitmap;      // Hahmon kuva
	private float x;			// X-sijainti
	private float y;			// Y-sijainti
	private double speed;		// Hahmon nopeus ??
	//private Rect sourceRect;	// Animaatioissa tullaan tarvitsemaan
	//private Rect destRect;	// Animaatioissa tullaan tarvitsemaan
	private double direction;
	private int health;
	private int height;
	private int width;
	private double distance;	// Zombin etäisyys pelaajaan (vain zombeille)

	
	// Konstruktori
	public Character(Bitmap bitmap, int x, int y, double speed){
		this.bitmap = bitmap;
		this.x = x;
		this.y = y;
		this.speed = speed;
		this.health = 100;
		this.distance = 100;	// debugging reasons
								// Jos ei aseteta, niin zombit kuolevat heti luodessa
	}		
	


	
	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public Bitmap getBitmap() {
		return this.bitmap;
	}

	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
		this.height = bitmap.getHeight();
		this.width = bitmap.getWidth();
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}
	
	public double getSpeed() {
		return speed;
	}
	
	public void setDirection(double direction) {
		this.direction = direction;
	}
	
	public double getDirection() {
		return this.direction;
	}
	
	public double getDistance() {
		return this.distance;
	}
	
	public void setDistance(double distance) {
		this.distance = distance;
	}
	
	
	public int getHealth() {
		return health;
	}


	public void setHealth(int health) {
		this.health = health;
	}


	public void updateMovement(double x, double y) {
		if (this.health > 0) {
			//pelaaja liikutetaan -1 arvoilla
			//Suunta on valmiiksi laskettu SensorEventissä
			if (x != -1 && y != -1) {
				
				//sekoitetaan pakkaa, jotta zombin liike on sekavempaa
				y = y - (Math.random()*100) + (Math.random()*100);
				x = x - (Math.random()*100) + (Math.random()*100);
				double dy = (double) y - (double) this.y;
				double dx = (double) x - (double) this.x;
			
				setDirection( Math.atan2(dy, dx)  + (float)Math.PI );
			}
			setX((float) (this.x + (Math.cos(getDirection()) * -this.speed)) );
			setY((float) (this.y + (Math.sin(getDirection()) * -this.speed)) );
		}

	}
	
	
	
	
	public void setSpeed(double speed) {
		this.speed = speed;
	}




	public void get_damage(int dmg) {
		this.health = health-dmg;
		if(this.health<=0)
			kill_char();
	}

	private void kill_char() {
		//jotain
	}
	
	
	
	public void updateDistance(float x, float y) {
		float dx = x - this.x;
		float dy = y - this.y;
		
		setDistance(   Math.sqrt( Math.pow(dx, 2) + Math.pow(dy, 2) )   );
		
	}
	
	
}