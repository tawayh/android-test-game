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
	
	private double distance;	// Zombin et‰isyys pelaajaan (vain zombeille)

	
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
	

	//Getterit ja setterit alempana

	
	public Bitmap getBitmap() {
		return this.bitmap;
	}

	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
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


	// Metodi laskee suunnan ja nopeuden mukaan hahmolle x ja y koordinaatit
	// Kulkee siis "suoraan" kohdetta p‰in
	public void updateMovement(float x, float y) {
		if (this.health > 0) {
			double dy = (double) y - (double) this.y;
			double dx = (double) x - (double) this.x;
			
			// Mul ei oo n‰ist mit‰‰n muistikuvaa, kopioin mun vanhast XNA pelist
			setDirection( Math.atan2(dy, dx)  + (float)Math.PI );
			setX((float) (this.x + (Math.cos(getDirection()) * -this.speed)) );
			setY((float) (this.y + (Math.sin(getDirection()) * -this.speed)) );
		}

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