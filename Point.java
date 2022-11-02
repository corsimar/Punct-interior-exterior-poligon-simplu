public class Point {
	private int x, y;
	
	public Point(int x, int y) {
		this.x = x; this.y = y;
	}
	
	public void setPointPos(int x, int y) {
		this.x = x; this.y = y;
	}
	
	public int[] getPointPos() {
		return new int[] { x, y };
	}
}
