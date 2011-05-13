package fi.game.Zoambie;

import android.graphics.*;

public class Saw {
	
	private Bitmap bitmap;      // Hahmon kuva
	private float x;			// X-sijainti
	private float y;			// Y-sijainti
	//private Rect sourceRect;	// Animaatioissa tullaan tarvitsemaan
	//private Rect destRect;	// Animaatioissa tullaan tarvitsemaan
	private double direction;
	


	
	// Konstruktori
	public Saw(Bitmap saw, float x, float y){
		this.bitmap = saw;
		this.x = x;
		this.y = y;
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
	
	public void setDirection(double direction) {
		this.direction = direction;
	}
	
	public double getDirection() {
		return this.direction;
	}
	
	public void updateDirection(float x1, float y1, float x2, float y2) {
		float direction = 0;
		direction = (float) Math.toDegrees( Math.atan2(y2-y1, x2-x1) );
		
		this.direction = direction;
		
	}
}