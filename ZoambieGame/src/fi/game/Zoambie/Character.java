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
	public Character(Bitmap bitmap, int x, int y, double speed, int health){
		this.bitmap = bitmap;
		this.height = bitmap.getHeight();
		this.width = bitmap.getWidth();
		this.x = x;
		this.y = y;
		this.speed = speed;
		this.health = health;
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
		if (health < 0)
			health = 0;
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
	
	
	public void checkCorners(int screenWidth, int screenHeight) {
		//Varmistukset ettei pelaaja mene ruudun ulkopuolelle
        if (getX() < 0) {
        	setX(0);
        }
        else if (getX()+getWidth() > screenWidth) {
        	setX(screenWidth-getWidth());
        }
        if (getY() < 0) {
        	setY(0);
        }
        else if (getY()+getHeight() > screenHeight) {
        	setY(screenHeight-getHeight());
        }
	}
	
	
	public boolean collisionPlayer(Character player) {
		boolean collision = false;
		
		float x = getX() - player.getX();
		float y = getY() - player.getY();
		
		if ((x < 30 && x > -30)  &&  (y < 30 && y > -30)) {
			collision = true;
			/*
			if (getX() >= player.getX()){
				if (getY() >= player.getY()) {
					if (getX() <= player.getX()+player.getWidth()  &&  getY() <= player.getY()+player.getHeight()) {
						//player.get_damage(5);
						collision = true;
					}
				}
				else {
					if (getX() <= player.getX()+player.getWidth()  &&  getY() + getHeight()  >= player.getY()) {
						collision = true;
						//player.get_damage(5);
					}
				}
			}
			else {
				if (getY() >= player.getY()) {
					if (getX() + getWidth() >= player.getX()   &&  getY() <= player.getY()+player.getHeight()) {
						collision = true;
						//player.get_damage(5);
					}
				}
				else {
					if (getX() + getWidth() >= player.getX()   &&  getY() + getHeight()  >= player.getY()) {
						collision = true;
						//player.get_damage(5);
					}
				}
			}
			*/
		}
		return collision;
	}
	
	public boolean collisionSaw(Saw saw) {
		
		boolean collision = false;
		
		float x = getX() - saw.getKillPoint().x;
		float y = getY() - saw.getKillPoint().y;
		
		if ((x < 5 && x > -5)  &&  (y < 5 && y > -5)) {
			
			collision = true;
			//get_damage(5);
			setX((float) (this.x + (Math.cos(getDirection()) * (10) )));
			setY((float) (this.y + (Math.sin(getDirection()) * (10) )));
			
			/*
			if (getX() >= saw.getX()){
				if (getY() >= saw.getY()) {
					if (getX() <= saw.getX()+saw.getWidth()  &&  getY() <= saw.getY()+saw.getHeight()) {
						collision = true;
						//get_damage(5);
						setX((float) (this.x + (Math.cos(getDirection()) * (this.speed))*10) );
						setY((float) (this.y + (Math.sin(getDirection()) * (this.speed))*10) );
						
					}
				}
				else {
					if (getX() <= saw.getX()+saw.getWidth()  &&  getY() + getHeight()  >= saw.getY()) {
						collision = true;
						//get_damage(5);
						setX((float) (this.x + (Math.cos(getDirection()) * (this.speed))*10) );
						setY((float) (this.y + (Math.sin(getDirection()) * (this.speed))*10) );
					}
				}
			}
			else {
				if (getY() >= saw.getY()) {
					if (getX() + getWidth() >= saw.getX()   &&  getY() <= saw.getY()+saw.getHeight()) {
						collision = true;
						//get_damage(5);
						setX((float) (this.x + (Math.cos(getDirection()) * (this.speed))*10) );
						setY((float) (this.y + (Math.sin(getDirection()) * (this.speed))*10) );
					}
				}
				else {
					if (getX() + getWidth() >= saw.getX()   &&  getY() + getHeight()  >= saw.getY()) {
						collision = true;
						//get_damage(5);
						setX((float) (this.x + (Math.cos(getDirection()) * (this.speed))*10) );
						setY((float) (this.y + (Math.sin(getDirection()) * (this.speed))*10) );
					}
				}
			}
			*/
		}
		return collision;
	}
	
}