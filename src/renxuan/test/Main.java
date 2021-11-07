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
		//以上是测试图的各种遍历是否均正确
//		if(1==1)return;
		/**
		 * 需要自己定义的东西
		 */
		int verticeLenth=10000;//图的点个数
		int startPoint=0;//起点
		int endPoint=verticeLenth/2;//终点
		int HeuristicFactor=1;//启发因子,AStart算法的启发距离就是HeuristicFactor*欧几里得距离
		int heapNum=3;
		int threadNum=3;
		int pathProbability=50;
		//end
		verticeNum= verticeLenth;
		edgesNum=0;
		graph2 = new Graph<>(verticeLenth);
		Random random = new Random(System.currentTimeMillis());// 指定种子数字
		for (int i = 0; i < verticeNum; i++) {
			graph2.addVertice(i, i, random.nextInt(verticeNum)+1);
			/**
			 * 添加图的顶点，顶点横坐标x为i，纵坐标y为random.nextInt(verticeNum)+1的随机数保证了所以点都不重叠
			 * 执行完循环后图将会呈现一个伪随机 verticeNum X verticeNum大小的点阵图,每个点都不会重叠
			 */
		}
		graph2.initializeMatrix(verticeNum );
		/**
		 * 初始化邻接的矩阵：初始化大小和值伪Integer.MAX_VALUE的三分之一大，
		 * 取三分之一的原因是可能在求和距离的时候数太大导致int溢出
		 * 
		 */
		long start = System.currentTimeMillis();
		for (int i = 0; i < (verticeNum * verticeNum - 1) / 2; i++) {
			/**
			 * 这里的500调整的是任意两个点有直接路径的概率
			 * 间接控制图的边的大小
			 * 比如为2的话任意两个点有1/2的概率项相连此时几乎所有点都直接相连
			 * 比如为4的话任意两个点有1/4的概率项相连此时一个点的邻接点大约有图总点数目的一般
			 * 建议大小为图总顶点个数开平方
			 * 
			 */
			if (random.nextInt(pathProbability) == 0) {//0到1随机
				graph2.linkEdge(random.nextInt(verticeNum), random.nextInt(verticeNum));
			}
		}//循环随机连接大约verticeNum*verticeNum/4条边
		//以下两步是为了最大程度保证起点到终点有路径
		for(int i=0;i<100;i++) {
			graph2.linkEdge(startPoint, random.nextInt(100));//0点连接随机的0到100的边
		}
		for(int i=0;i<100;i++) {
			graph2.linkEdge(endPoint,verticeNum-random.nextInt(100)-1);//终点随机连接到verticeNum到verticeNum-100的点
		}
		
		long end = System.currentTimeMillis();;
		 System.out.println("=======================单线程添加耗时==============================");
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
//		 System.out.println("=======================多线程添加耗时==============================");
//		System.out.println("runtime: " + (end - start));
//		if(1==1)return;

		boolean compareIt=true;
		System.out.println("runtime: " + (end - start)+"\n"+"初始化结束开始算法,双向算法是含有字符串Twoway的函数，双向会导致结果有偏差");
		System.out.println("顶点个数："+verticeLenth);
		System.out.println("边个数："+edgesNum);
		System.out.println("队列最大个数："+heapNum);
		System.out.println("起点："+startPoint+"  终点："+endPoint);
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
