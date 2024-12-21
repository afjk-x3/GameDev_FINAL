package utilz;

public class Constants {
	
	public static class Directions{
		public static final int LEFT = 0;
		public static final int UP = 1;
		public static final int RIGHT = 2;
		public static final int DOWN = 3;
	}
	
	public static class PlayerConstants{
		public static final int ATTACK_1 = 0;
		public static final int ATTACK_1_LEFT = 1;
		public static final int RUNNING = 2;
		public static final int RUNNINGLEFT = 3;
		public static final int PUGAY = 4;
		public static final int PUGAY_LEFT = 5;
		public static final int JUMP = 6;
		public static final int JUMP_LEFT = 7;
		public static final int JUMP_ATTK = 8;
		public static final int JUMP_ATTK_LEFT = 9;
		public static final int IDLE = 10;
		public static final int IDLE_LEFT = 11;
		public static final int DOWN_BLOCK = 12;
		public static final int DOWN_BLOCK_LEFT = 13;
		public static final int DOWN_ATTK = 14;
		public static final int DOWN_ATTK_LEFT = 15;
		public static final int CROUCH = 16;
		public static final int CROUCH_LEFT = 17;
		public static final int UP_BLOCK = 18;
		public static final int UP_BLOCK_LEFT = 19;
		
		public static int GetSpriteAmounts(int player_action) {
			
			switch(player_action) {
			
			case RUNNING:
				return 12;
			case IDLE:
				return 6;
			case JUMP:
				return 6;
			case DOWN_BLOCK:
				return 1;
//			case GROUND:
			case DOWN_ATTK:
				return 5;
			case ATTACK_1:
				return 5;
			case JUMP_ATTK:
				return 6;
			default:
				return 1;
			}
		}
	}

}
