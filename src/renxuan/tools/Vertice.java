package renxuan.tools;

public class Vertice {
	private int pointIndex;
	private int positionX;
	private int positionY;
	public Vertice(int pointIndex, int positionX, int positionY) {
		super();
		this.pointIndex = pointIndex;
		this.positionX = positionX;
		this.positionY = positionY;
	}
	public int getPointIndex() {
		return pointIndex;
	}
	public int getPositionX() {
		return positionX;
	}
	public int getPositionY() {
		return positionY;
	}
	public void setPointIndex(int pointIndex) {
		this.pointIndex = pointIndex;
	}
	public void setPositionX(int positionX) {
		this.positionX = positionX;
	}
	public void setPositionY(int positionY) {
		this.positionY = positionY;
	}
	@Override
	public String toString() {
		return "Vertice [pointIndex=" + pointIndex + ", positionX=" + positionX + ", positionY=" + positionY + "]";
	}
	
}
