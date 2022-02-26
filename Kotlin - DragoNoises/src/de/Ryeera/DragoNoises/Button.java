package de.Ryeera.DragoNoises;

public class Button {
	
	private int x, y;
	private static final Button[][] BUTTONS = new Button[9][9];
	
	static {
		for(int x = 0; x < 9; x++)
			for(int y = 0; y < 9; y++)
				BUTTONS[x][y] = new Button(x, y);
	}
	
	private Button(int x, int y) {
		this.setX(x);
		this.setY(y);
	}
	
	public int getX() {
		return x;
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public int getY() {
		return y;
	}
	
	public void setY(int y) {
		this.y = y;
	}

	@Override
	public String toString() {
		return "Button [x=" + x + ", y=" + y + "]";
	}
	
	public static Button get(int x, int y) {
		return BUTTONS[x][y];
	}
}
