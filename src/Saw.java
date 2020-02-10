import java.util.LinkedHashMap;
import java.util.Map;

public class Saw {

	private LinkedHashMap<String, Coords> coordinateSet = new LinkedHashMap<String, Coords>();
	private Coords startPoint;
	private Coords endPoint;

	public Saw() {

	}

	public Saw(int x, int y) {
		Coords Coord = new Coords(x, y);

		endPoint = Coord;
		coordinateSet.put(Coord.toString(), Coord);
	}

	public void addCoord(int x, int y) {
		Coords newCoord = new Coords(x, y);
		coordinateSet.put(newCoord.toString(), newCoord = new Coords(x, y));
		endPoint = newCoord;
	}

	public boolean hasCoord(int x, int y) {
		Coords coord = new Coords(x, y);
		if (coordinateSet.containsKey(coord.toString())) {
			return true;
		}
		return false;
	}

	public boolean isSAP(int x, int y) {
		Coords newCoord = new Coords(x, y);
		if (startPoint.equals(newCoord)) {
			return true;
		}
		return false;
	}

	public Coords getEndPoint() {
		return endPoint;
	}

	public void setStartPoint(int x, int y) {
		Coords newCoord = new Coords(x, y);
		coordinateSet.put(newCoord.toString(), newCoord = new Coords(x, y));
		startPoint = newCoord;
	}

	public Coords getStartPoint() {
		return startPoint;
	}

	public int getSawLength() {
		return coordinateSet.size();
	}

	public void clear() {
		coordinateSet.clear();
	}

	public String toString() {
		String printString = "[";
		for (Map.Entry<String, Coords> entry : coordinateSet.entrySet()) {
			printString = printString + entry.getKey() + ", ";
		}
		printString = printString + "]";
		return printString;
	}

}
