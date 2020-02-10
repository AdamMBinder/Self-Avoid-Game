public class Coords {

	private int x_pos;
	private int y_pos;

	public Coords(int x, int y) {
		x_pos = x;
		y_pos = y;
	}

	public String toString() {
		String returnString = "";
		returnString += "(" + Integer.toString(x_pos) + ", " + Integer.toString(y_pos) + ")";
		return returnString;
	}

	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Coords)) {
			return false;
		} else {
			Coords otherCoord = (Coords) obj;
			if ((x_pos == otherCoord.getX_pos()) && (y_pos == otherCoord.getY_pos())) {
				return true;
			}
		}
		return false;
	}

	public int getX_pos() {
		return x_pos;
	}

	public int getY_pos() {
		return y_pos;
	}

}
