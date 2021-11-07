package renxuan.tools;

public class PathTriple {
	private int start;
	private int destination;
	private int cost;
	public PathTriple(int start, int destination, int cost) {
		super();
		this.start = start;
		this.destination = destination;
		this.cost = cost;
	}
	public int getStart() {
		return start;
	}
	public int getDestination() {
		return destination;
	}
	public int getCost() {
		return cost;
	}
	public void setStart(int start) {
		this.start = start;
	}
	public void setDestination(int destination) {
		this.destination = destination;
	}
	public void setCost(int cost) {
		this.cost = cost;
	}
	@Override
	public String toString() {
		return "PathTriple [start=" + start + ", destination=" + destination + ", cost=" + cost + "]";
	};
	
}
