package fi.game.Zoambie;

import android.graphics.*;

public class Block {
        
        private Rect rect;      			// Alueen rajat
        private boolean playerInThisBlock;  // Tieto, että onko pelaaja alueen sisällä
        private double direction;            // Alueen suunta pelaaja kohden
        
        public Block(Rect rect) {
        	this.rect = rect;
        	playerInThisBlock = false;
        	direction = 0f;
        }

		public Rect getRect() {
			return rect;
		}

		public void setRect(Rect rect) {
			this.rect = rect;
		}

		public boolean isPlayerInThisBlock() {
			return playerInThisBlock;
		}

		public void setPlayerInThisBlock(boolean playerInThisBlock) {
			this.playerInThisBlock = playerInThisBlock;
		}

		public double getDirection() {
			return direction;
		}

		public void setDirection(double direction) {
			this.direction = direction;
		}
        
        
		
		public void updateDirection(Character player) {
			double dy = (double) player.getY() - (double) this.rect.centerY();
			double dx = (double) player.getX() - (double) this.rect.centerX();
		
			setDirection( Math.atan2(dy, dx)  + (float)Math.PI );
		}
}