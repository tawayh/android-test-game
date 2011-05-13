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
		//float direction = (centerX - centerY) / (touchX - touchY);
		/*
		 * if x2 = x1 then 
        if y2 >= y1 then 
            result 90 
        elsif y2 < y1 then 
            result 270 
        end if 
    else 
        if x2 > x1 and y2 >= y1 then %QUAD 1 
            result (arctand ((y2 - y1) / (x2 - x1))) 
        elsif x2 > x1 and y2 < y1 then %QUAD 2 
            result 360 + (arctand ((y2 - y1) / (x2 - x1))) 
        elsif x2 < x1 and y2 < y1 then %QUAD 3 
            result 180 + (arctand ((y2 - y1) / (x2 - x1))) 
        elsif x2 < x1 and y2 >= y1 then %QUAD 4 
            result 180 + (arctand ((y2 - y1) / (x2 - x1))) 
        end if 
		 */
		/*
		if (x2 > x1 && y2 >= y1)
			direction = (float) Math.atan2((y2 - y1) / (x2 - x1));
		else if (x2 > x1 && y2 < y1)
			direction = (float) (360 + Math.atan2((y2 - y1) / (x2 - x1)));
		else if (x2 < x1 && y2 < y1)
			direction = (float) (180 + Math.atan2((y2 - y1) / (x2 - x1)));
		else if (x2 < x1 && y2 >= y1)
			direction = (float) (180 + Math.atan2((y2 - y1) / (x2 - x1)));
		*/
		//float y = y2 - y1;
		//float x = x2 - x1;
		//float radians = (float)Math.atan2(y, x);
	    //float angle = radians * (float)(180/Math.PI);
		
		//float direction = (float) Math.atan((touchY-centerY)/(touchX-centerX))*(float)(180/Math.PI);
		//x1 - y1
		//x2 - y2
		direction = (float) Math.toDegrees( Math.atan2(y2-y1, x2-x1) );
		
		this.direction = direction + 90;
		
	}
}