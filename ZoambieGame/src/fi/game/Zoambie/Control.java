package fi.game.Zoambie;

import java.util.LinkedList;

import android.graphics.*;

public class Control {
        private static Paint p = new Paint();	
        public static void drawDpad(Canvas canvas, int screenWidth, int screenHeight) {
        	p.setColor(Color.BLACK);
        	p.setStrokeWidth(2f);
        	canvas.drawLine(0, screenHeight/2, screenHeight/2, screenHeight/2, p);
        	canvas.drawLine(screenHeight/2, screenHeight/2, screenHeight/2, screenHeight, p);
        	
        	canvas.drawLine(screenWidth-(screenHeight/2), screenHeight/2, screenWidth, screenHeight/2, p);
        	canvas.drawLine(screenWidth-(screenHeight/2), screenHeight/2, screenWidth-(screenHeight/2), screenHeight, p);
        }
        
        public static void drawHealth(Canvas canvas, int screenWidth, Character player) {
        	p.setColor(Color.GREEN);
        	p.setStrokeWidth(10f);
        	p.setAlpha(100);
        	canvas.drawLine(20, 25, (screenWidth*((float)player.getHealth()/200f))-20, 25, p);
        }
        
        public static void drawGUI(Canvas canvas, LinkedList<Character> enemyList, int killedZombies, int fps) {
        	
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
        	
        	//Piirretään fps
        	float [] pos3 = {350,50,360,50,370,50,380,50};
        	canvas.drawPosText(""+fps, pos3, p);	     
        	
        }
        
        public static void drawGameOver(Canvas canvas) {
        	canvas.drawColor(Color.RED);
    		p.setColor(Color.BLACK);
    		p.setTextSize(75);
    		float[] pos = {50,300,  125,300,  200,300,  275,300,  350,300,  425,300,  500,300,  575,300,  650,300};
        	canvas.drawPosText("GAME OVER", pos, p);
        }
        
        
        
        
        
        
        public static Character createZombie(Bitmap bmp, int screenWidth, int screenHeight) {
        	Character enemy = null;
        	Point point = randomZombieLocation(screenWidth, screenHeight);
	    	int x = point.x;
	    	int y = point.y;
	    	
	    	double typeCount = Math.random();
		    int type = 1;
	    	if (typeCount < 0.75)
		    	type = 1;
	    	else if (typeCount < 0.9 && typeCount >= 0.75)
	    		type = 2;
	    	else if (typeCount >= 0.9)
	    		type = 3;
	    	
	    	if (type == 1) {
		    	double speed = 0.5 * Math.random();
		    	if (speed < 0.1)
		    		speed = 0.1;
		    	
		    	enemy = new Character( 
			            bmp,
			            x,
			            y,
			            speed,
			            20,		//Health
			            4,		//Number of frames
			            40,		//Width
			            50,		//Height
			            1		//type
			    );
	    	}
	    	
	    	else if (type == 2) {
		    	double speed = 2.5 * Math.random();
		    	if (speed < 1.5)
		    		speed = 1.5;
		    	
		    	enemy = new Character( 
			            bmp,
			            x,
			            y,
			            speed,
			            10,		//Health
			            4,		//Number of frames
			            40,		//Width
			            50,		//Height
			            type	//type
			    );
	    	}
	    	
	    	else if (type == 3) {
		    	double speed = 0.2 * Math.random();
		    	if (speed < 0.1)
		    		speed = 0.1;
		    	
		    	enemy = new Character( 
			            bmp,
			            x,
			            y,
			            speed,
			            1000,	//Health
			            4,		//Number of frames
			            40,		//Width
			            50,		//Height
			            type	//type
			    );
	    	}
	    	
	    	
	    	return enemy;
        }
        
        
        public static Point randomZombieLocation(int screenWidth, int screenHeight) {
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

}
