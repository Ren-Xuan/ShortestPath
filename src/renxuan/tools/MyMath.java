package renxuan.tools;

public class MyMath {
	public static int Width(int fromX,int fromY,int toX,int toY) {//��ŷ����þ���ĺ���
		return (int) Math.pow((fromX-toX)*(fromX-toX)+(fromY-toY)*(fromY-toY), 0.5);
	}
	public static void main(String[]args) {
		System.out.println(Width(0, 0, 5, 5));
		System.out.println(Math.pow(25, 0.5));
	}
}
