package renxuan.tools;

public class PathPair {
	private int start;
	private int destination;
	public PathPair(int start, int destination) {
		super();
		this.start = start;
		this.destination = destination;
	}
	public int getStart() {
		return start;
	}
	public int getDestination() {
		return destination;
	}
	public void setStart(int start) {
		this.start = start;
	}
	public void setDestination(int destination) {
		this.destination = destination;
	}
	@Override
	public String toString() {
		return "PathPair [start=" + start + ", destination=" + destination + "]";
	}
	
}
