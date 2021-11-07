package renxuan.test;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.Random;

import renxuan.entity.Graph;
import renxuan.lib.MyPriorityQueue;
import renxuan.tools.NameObjectMap;
import renxuan.tools.Pair;

public class Main {
	public static Graph graph2;
	public static int verticeNum;
	public static int edgesNum;
	public static void main(String[] args) throws InterruptedException {
		
	
		System.out.println("=========================================================");
//		Character[] vertices = new Character[10];// ['A','B','C','D','E','F','G','H','I'];
//		Graph<Character> graph = new Graph<>(20);
//		graph.initializeMatrix(10);
//		graph.addVertice('A', 1, 1);
//		graph.addVertice('B', 1, 1);
//		graph.addVertice('C', 1, 1);
//		graph.addVertice('D', 1, 1);
//		graph.addVertice('E', 1, 1);
//		graph.addVertice('F', 1, 1);
//		graph.addVertice('G', 1, 1);
//		graph.addVertice('H', 1, 1);
//		graph.addVertice('I', 1, 1);
//		graph.addEdge('A', 'B', 1);
//		graph.addEdge('A', 'C', 4);
//		graph.addEdge('A', 'D', 6);
//		graph.addEdge('C', 'D', 7);
//		graph.addEdge('C', 'G', 21);
//		graph.addEdge('D', 'G', 15);
//		graph.addEdge('D', 'H', 4);
//		graph.addEdge('B', 'E', 6);
//		graph.addEdge('B', 'F', 8);
//		graph.addEdge('E', 'I', 25);
//		System.out.println("========================DijkstraOneHeap==============================");
//		System.out.println(graph.DijkstraMultiHeap('C','I', 1));
//		System.out.println("========================DijkstraCurse==============================");
//		System.out.println(graph.DijkstraHeapTwoway('C','I'));
//		System.out.println("========================DijkstraTwowayThread==============================");
//		System.out.println(graph.DijkstraHeapTwowayWithThead('C','I'));
//		System.out.println("========================DijkstraMultiHeap==============================");
//		System.out.println(graph.DijkstraMultiHeap('C','I', 5));
//		System.out.println("========================DijkstraMultiThread==============================");
//		System.out.println(graph.DijkstraHeapWtihTreads('C','I', 4, 4));
//		System.out.println("========================Bellman==========================");
//		System.out.println(graph.BellmanFordSPFA('C','I'));
//		System.out.println("=======================AStartHeap===========================");
//		System.out.println(graph.AStartHeap('C', 'I'));
//		System.out.println("=======================AStartMultiHeap===========================");
//		System.out.println(graph.AStartMultiHeap('C', 'I',2,1));
//		System.out.println("=======================AStartHeapTwoway===========================");
//		System.out.println(graph.AStartHeapTwoway('C', 'I',1));
//		System.out.println("==================================Test======================================");
		//�����ǲ���ͼ�ĸ��ֱ����Ƿ����ȷ
//		if(1==1)return;
		/**
		 * ��Ҫ�Լ�����Ķ���
		 */
		int verticeLenth=10000;//ͼ�ĵ����
		int startPoint=0;//���
		int endPoint=verticeLenth/2;//�յ�
		int HeuristicFactor=1;//��������,AStart�㷨�������������HeuristicFactor*ŷ����þ���
		int heapNum=3;
		int threadNum=3;
		int pathProbability=50;
		//end
		verticeNum= verticeLenth;
		edgesNum=0;
		graph2 = new Graph<>(verticeLenth);
		Random random = new Random(System.currentTimeMillis());// ָ����������
		for (int i = 0; i < verticeNum; i++) {
			graph2.addVertice(i, i, random.nextInt(verticeNum)+1);
			/**
			 * ���ͼ�Ķ��㣬���������xΪi��������yΪrandom.nextInt(verticeNum)+1���������֤�����Ե㶼���ص�
			 * ִ����ѭ����ͼ�������һ��α��� verticeNum X verticeNum��С�ĵ���ͼ,ÿ���㶼�����ص�
			 */
		}
		graph2.initializeMatrix(verticeNum );
		/**
		 * ��ʼ���ڽӵľ��󣺳�ʼ����С��ֵαInteger.MAX_VALUE������֮һ��
		 * ȡ����֮һ��ԭ���ǿ�������;����ʱ����̫����int���
		 * 
		 */
		long start = System.currentTimeMillis();
		for (int i = 0; i < (verticeNum * verticeNum - 1) / 2; i++) {
			/**
			 * �����500��������������������ֱ��·���ĸ���
			 * ��ӿ���ͼ�ıߵĴ�С
			 * ����Ϊ2�Ļ�������������1/2�ĸ�����������ʱ�������е㶼ֱ������
			 * ����Ϊ4�Ļ�������������1/4�ĸ�����������ʱһ������ڽӵ��Լ��ͼ�ܵ���Ŀ��һ��
			 * �����СΪͼ�ܶ��������ƽ��
			 * 
			 */
			if (random.nextInt(pathProbability) == 0) {//0��1���
				graph2.linkEdge(random.nextInt(verticeNum), random.nextInt(verticeNum));
			}
		}//ѭ��������Ӵ�ԼverticeNum*verticeNum/4����
		//����������Ϊ�����̶ȱ�֤��㵽�յ���·��
		for(int i=0;i<100;i++) {
			graph2.linkEdge(startPoint, random.nextInt(100));//0�����������0��100�ı�
		}
		for(int i=0;i<100;i++) {
			graph2.linkEdge(endPoint,verticeNum-random.nextInt(100)-1);//�յ�������ӵ�verticeNum��verticeNum-100�ĵ�
		}
		
		long end = System.currentTimeMillis();;
		 System.out.println("=======================���߳���Ӻ�ʱ==============================");
		 System.out.println("runtime: " + (end - start));
//		start = System.currentTimeMillis();;
//		for(int i=0;i<verticeNum/100;i++) {
//			new Thread(()->{
//				for(int j=0;j<verticeNum*50;j++) {
//					if(random.nextInt(2) == 0) {
//						graph2.linkEdge(random.nextInt(verticeNum), random.nextInt(verticeNum));
//					}
//				}
//			}).start();;
//		}
//		 end = System.currentTimeMillis();
//		 System.out.println("=======================���߳���Ӻ�ʱ==============================");
//		System.out.println("runtime: " + (end - start));
//		if(1==1)return;

		boolean compareIt=true;
		System.out.println("runtime: " + (end - start)+"\n"+"��ʼ��������ʼ�㷨,˫���㷨�Ǻ����ַ���Twoway�ĺ�����˫��ᵼ�½����ƫ��");
		System.out.println("���������"+verticeLenth);
		System.out.println("�߸�����"+edgesNum);
		System.out.println("������������"+heapNum);
		System.out.println("��㣺"+startPoint+"  �յ㣺"+endPoint);
		System.out.println("========================Dijkstra==============================");

		start = System.nanoTime()/10000;
		System.out.println(graph2.Dijkstra(startPoint, endPoint));
		end = System.nanoTime()/10000;
		System.out.println("runtime: " + (end - start));

		System.out.println("========================DijkstraSingleueue==============================");
		start = System.nanoTime()/10000;
		System.out.println(graph2.DijkstraHeap(startPoint, endPoint));
		end = System.nanoTime()/10000;
		System.out.println("runtime: " + (end - start));
		System.out.println("========================DijkstraMultiQueue==============================");
		start = System.nanoTime()/10000;
		System.out.println(graph2.DijkstraMultiHeap(startPoint, endPoint,heapNum));
		end = System.nanoTime()/10000;
		System.out.println("runtime: " + (end - start));
		System.out.println("========================DijkstraMultiQueue And MultiThread==============================");
		start = System.nanoTime()/10000;
		System.out.println(graph2.DijkstraHeapWtihTreads(startPoint, endPoint,heapNum, threadNum));
		end = System.nanoTime()/10000;
		System.out.println("runtime: " + (end - start));
		System.out.println("========================DijkstraTwowayWtihHeap==============================");
		start = System.nanoTime()/10000;
		System.out.println(graph2.DijkstraHeapTwoway(startPoint, endPoint));
		end = System.nanoTime()/10000;
		System.out.println("runtime: " + (end - start));
		System.out.println("========================DijkstraTowwayWithHeapAndThread==============================");
		start = System.nanoTime()/10000;
		System.out.println(graph2.DijkstraHeapTwowayWithThead(startPoint, endPoint));
		end = System.nanoTime()/10000;
		System.out.println("runtime: " + (end - start));
		System.out.println("=======================AStart===========================");
		start = System.nanoTime()/10000;
		System.out.println(graph2.AStart(startPoint, endPoint));
		end = System.nanoTime()/10000;
		System.out.println("runtime: " + (end - start));
		System.out.println("=======================AStartSingleQueue===========================");
		start = System.nanoTime()/10000;
		System.out.println(graph2.AStartHeap(startPoint, endPoint));
		end = System.nanoTime()/10000;
		System.out.println("runtime: " + (end - start));
		System.out.println("=======================AStartMultiQueue===========================");
		start = System.nanoTime()/10000;
		System.out.println(graph2.AStartMultiHeap(startPoint, endPoint,threadNum,HeuristicFactor));
		end = System.nanoTime()/10000;
		System.out.println("runtime: " + (end - start));
		
		System.out.println("========================AStartMultiQueueWtihTreads==============================");
		start = System.nanoTime()/10000;
		System.out.println(graph2.AStartHeapWtihTreads(startPoint, endPoint, heapNum, threadNum,HeuristicFactor));
		end = System.nanoTime()/10000;
		System.out.println("runtime: " + (end - start));
		System.out.println("=======================AStartTwowayWtihQueue===========================");
		start = System.nanoTime()/10000;
		System.out.println(graph2.AStartHeapTwoway(startPoint, endPoint,HeuristicFactor));
		end = System.nanoTime()/10000;
		System.out.println("runtime: " + (end - start));
		System.out.println("=======================AStartTwowayWithQueueAndThread===========================");
		start = System.nanoTime()/10000;
		System.out.println(graph2.AStartHeapTwowayWithThead(startPoint, endPoint,HeuristicFactor));
		end = System.nanoTime()/10000;
		System.out.println("runtime: " + (end - start));
		
		
	}

}
