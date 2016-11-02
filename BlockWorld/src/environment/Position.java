package environment;

public class Position {
	private int x;
	private int y;
	
	public Position(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	//	GETTERS
	public int getX() { return x; }
	
	public int getY() { return y; }
	
	//	SETTERS
	public void setX(int x) { this.x = x; }
	
	public void setY(int y) { this.y = y; }
	
	//	METHODES
	public void setPosition(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null || getClass() != obj.getClass())
			return false;
		else {
			Position position = (Position) obj;
			return (x == position.x && y == position.y);
		}
	}
	
	@Override
	public String toString() {
		StringBuilder ch = new StringBuilder("(");
		ch.append(x);
		ch.append(", ");
		ch.append(y);
		ch.append(")");
		return ch.toString();
	}
}
