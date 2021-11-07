package renxuan.entity;

import java.util.AbstractQueue;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


import renxuan.lib.FixedlengthMinList;
import renxuan.lib.MyPriorityQueue;
import renxuan.test.Main;
import renxuan.tools.MyMath;
import renxuan.tools.NameObjectMap;
import renxuan.tools.Pair;
import renxuan.tools.PathPair;
import renxuan.tools.PathTriple;
import renxuan.tools.Vertice;

public class Graph<VerticeType> {
	public static boolean returnAble = true;
	public static boolean showPath = false;
	public static final int MAXVALUE = Integer.MAX_VALUE / 3;
	private Map<VerticeType, Integer> nameVerticeIndexMap;
	private List<Vertice> vertices;
	private List<PathTriple> edges;
	private List<List<Integer>> adjList;
	private int aboutSize;
	private  int[][] matrix;
	private  int i;
	private int verticeSize;
	// 多线程所需的东西
	public static MyPriorityQueue<Pair<Integer, Integer>>[] myPriorityQueueArr;
	public static int indexThread = 0;
	public static int costThread = Integer.MAX_VALUE;
	public static int minIndexThread = 0;
	// 双向dijkstra多线程版本所需
	public static int currentNodeIndexToThread;
	public static int currentNodeIndexFromThread;
	public static int[] disToThread;
	public static int[] disFromThread;
	public static int[] preToThread;
	public static int[] preFromThread;
	public static boolean[] isVisitedFrom;
	public static boolean[] isVisitedTo;

	public static int costTotalThreadFrom=-1;
	public static int costTotalThreadTo=-1;
	public Graph(int aboutSize) {
		super();
		this.nameVerticeIndexMap = new HashMap<VerticeType, Integer>();
		this.vertices = new ArrayList<>(aboutSize);
		this.edges = new ArrayList<>(aboutSize);
		this.adjList = new ArrayList<>(aboutSize);
		this.aboutSize = aboutSize;
		this.verticeSize = 0;
	}

	public Map<VerticeType, Integer> getNameVerticeIndexMap() {
		return nameVerticeIndexMap;
	}

	public List<Vertice> getVertices() {
		return vertices;
	}

	public List<PathTriple> getEdges() {
		return edges;
	}

	public List<List<Integer>> getAdjList() {
		return adjList;
	}

	public int getVerticeSize() {
		return verticeSize;
	}

	public void setNameVerticeIndexMap(Map<VerticeType, Integer> nameVerticeIndexMap) {
		this.nameVerticeIndexMap = nameVerticeIndexMap;
	}

	public void setVertices(List<Vertice> vertices) {
		this.vertices = vertices;
	}

	public void setEdges(List<PathTriple> edges) {
		this.edges = edges;
	}

	public void setAdjList(List<List<Integer>> adjList) {
		this.adjList = adjList;
	}

	public int[][] getMatrix() {
		return matrix;
	}

	public void setMatrix(int[][] matrix) {
		this.matrix = matrix;
	}

	public int getCost(int start, int end) {

		return this.matrix[start][end];
	}

	public void setNameVerticeIndex(VerticeType key, Integer value) {
		this.nameVerticeIndexMap.put(key, value);
	}

	public boolean addVertice(VerticeType vertice, int positionX, int positionY) {
		try {
			this.setNameVerticeIndex(vertice, this.verticeSize);
			int index = this.getVerticeSize();
			this.getVertices().add(new Vertice(index, positionX, positionY));
			this.getAdjList().add(new ArrayList<>(this.aboutSize / 2));
			this.verticeSize++;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	public int initializeMatrix(int size) throws InterruptedException {
		int maxSize = size > this.getVerticeSize() ? size : this.getVerticeSize();
		this.matrix = new int[maxSize][maxSize];
		boolean finish=true;
		Random random = new Random(System.currentTimeMillis());// 指定种子数字
		List<Thread> threads=new ArrayList<>();
		long start = System.currentTimeMillis();
//		for (i = 0; i < maxSize; i++) {// i和matrix已经声明为static
//			Thread t=new Thread(() -> {
//				for (int j = 0; j < maxSize; j++) {
//					this.matrix[i][j] = MAXVALUE;
//				}
//			});
//			threads.add(t);
//			t.start();
//		}
//		for(Thread t:threads) {
//			if(t!=null)t.join();
//		}
		long end = System.currentTimeMillis();


//		for(int i=0;i<100;i++) {
//			if(this.matrix[random.nextInt(maxSize)][random.nextInt(maxSize)]!=MAXVALUE) {
//				finish=false;
//				break;
//			}
//		}
//		if(finish) {
//			System.out.println("多线程initializeMatrix finished");
//		}else {
//			System.out.println("多线程initializeMatrix failed");
//		}
//
//		System.out.println("=======================多线程添加耗时==============================");
//		System.out.println("runtime: " + (end - start));
		start = System.currentTimeMillis();
	
		for (int i = 0; i < maxSize; i++) {// i和matrix已经声明为static
			for (int j = 0; j < maxSize; j++) {
				this.matrix[i][j] = MAXVALUE;
			}
		}
		end = System.currentTimeMillis();
		finish=true;
		for(int i=0;i<100;i++) {
			if(this.matrix[random.nextInt(maxSize)][random.nextInt(maxSize)]!=MAXVALUE) {
				finish=false;
				break;
			}
		}
		if(finish) {
			System.out.println("单线程initializeMatrix finished");
		}else {
			System.out.println("单线程initializeMatrix failed");
		}
		System.out.println("=======================单线程添加耗时==============================");
		System.out.println("runtime: " + (end - start));
		
		return maxSize;
	}

	public boolean addEdge(VerticeType from, VerticeType to, int cost) {

		int startIndex = this.getNameVerticeIndexMap().get(from);
		int endIndex = this.getNameVerticeIndexMap().get(to);
		this.getAdjList().get(startIndex).add(endIndex);
		this.getAdjList().get(endIndex).add(startIndex);
		this.getMatrix()[startIndex][endIndex] = cost;
		this.getMatrix()[endIndex][startIndex] = cost;
		this.getEdges().add(new PathTriple(startIndex, endIndex, cost));
		this.getEdges().add(new PathTriple(endIndex, startIndex, cost));

		return true;
	}

	public boolean addEdge(VerticeType from, VerticeType to) {

		int startIndex = this.getNameVerticeIndexMap().get(from);
		int endIndex = this.getNameVerticeIndexMap().get(to);
		this.getAdjList().get(startIndex).add(endIndex);
		this.getAdjList().get(endIndex).add(startIndex);
		int fromX = this.getVertices().get(startIndex).getPositionX();
		int fromY = this.getVertices().get(startIndex).getPositionY();
		int toX = this.getVertices().get(endIndex).getPositionX();
		int toY = this.getVertices().get(endIndex).getPositionY();
		int cost = MyMath.Width(fromX, fromY, toX, toY);
		this.getMatrix()[startIndex][endIndex] = cost;
		this.getMatrix()[endIndex][startIndex] = cost;
		this.getEdges().add(new PathTriple(startIndex, endIndex, cost));
		this.getEdges().add(new PathTriple(endIndex, startIndex, cost));

		return true;
	}

	// 用来连接任意两个点
	public boolean linkEdge(VerticeType from, VerticeType to) {

		int startIndex = this.getNameVerticeIndexMap().get(from);
		int endIndex = this.getNameVerticeIndexMap().get(to);
		if (this.getMatrix()[startIndex][endIndex] < MAXVALUE) {
			// 已经存在边了，不在操作
			return false;
		} else {
			this.getAdjList().get(startIndex).add(endIndex);
			this.getAdjList().get(endIndex).add(startIndex);
			int fromX = this.getVertices().get(startIndex).getPositionX();
			int fromY = this.getVertices().get(startIndex).getPositionY();
			int toX = this.getVertices().get(endIndex).getPositionX();
			int toY = this.getVertices().get(endIndex).getPositionY();
			int cost = MyMath.Width(fromX, fromY, toX, toY);
			this.getMatrix()[startIndex][endIndex] = cost;
			this.getMatrix()[endIndex][startIndex] = cost;
			this.getEdges().add(new PathTriple(startIndex, endIndex, cost));
			this.getEdges().add(new PathTriple(endIndex, startIndex, cost));
			Main.edgesNum++;
		}

		return true;
	}

	public NameObjectMap Dijkstra(VerticeType startPoint, VerticeType endPoint) {
		int vStart = this.getNameVerticeIndexMap().get(startPoint);
		int vEnd = this.getNameVerticeIndexMap().get(endPoint);
		int[] preTable = new int[this.getVerticeSize()];
		boolean[] finalArr = new boolean[this.getVerticeSize()];
		int[] shortPathTable = new int[this.getVerticeSize()];
		int currenPointIndex = vStart;
		int prePointIndex = vStart;
		for (int i = 0; i < this.verticeSize; i++) {
			finalArr[i] = false;
			shortPathTable[i] = this.getCost(vStart, i);
			if (shortPathTable[i] < MAXVALUE) {
				preTable[i] = vStart;
			} else {
				preTable[i] = -1;
			}
		}
		shortPathTable[vStart] = 0;
		finalArr[vStart] = true;
		preTable[vStart] = vStart;
		int min;
		for (int i = 1; i < this.getVerticeSize(); i++) {
			min = MAXVALUE;
			for (int index = 0; index < this.getVerticeSize(); index++) {
				if (!finalArr[index]) {
					if (this.getCost(prePointIndex, index) < MAXVALUE)
						if (shortPathTable[index] < min) {
							currenPointIndex = index;
							min = shortPathTable[index];
						}
				}
			}
			finalArr[currenPointIndex] = true;
			int temp = 0;
			for (int index = 0; index < this.getVerticeSize(); index++) {
				if (!finalArr[index]) {
					temp = min + this.getCost(currenPointIndex, index);
					if (temp < shortPathTable[index]) {
						shortPathTable[index] = temp;
						if (this.getCost(index, currenPointIndex) < MAXVALUE)
							preTable[index] = currenPointIndex;
					}
				}
			}
			prePointIndex = currenPointIndex;
		}

		NameObjectMap resultMap = new NameObjectMap();
		if (returnAble) {
			resultMap.put("pre", preTable);
			resultMap.put("dis", shortPathTable);
			resultMap.put("cost", shortPathTable[vEnd]);
		}

		return resultMap;

	}

	public NameObjectMap BellmanFord(VerticeType source, VerticeType destination) {
		int vStart = this.nameVerticeIndexMap.get(source);
		int vEnd = this.nameVerticeIndexMap.get(destination);
		int[] distance = new int[this.getVerticeSize()]; // 用来记录从原节点 source 到某个节点的最短路径估计值
		int[] predecessor = new int[this.getVerticeSize()];// 用来记录某个节点的前驱节点

		// 第一步: 初始化图
		for (int index = 0; index < this.getVerticeSize(); index++) {
			distance[index] = MAXVALUE; // 初始化最短估计距离 默认无穷大
			predecessor[index] = -1; // 初始化前驱节点 默认为空
		}
		distance[vStart] = 0; // 将源节点的最短路径估计距离 初始化为0
		predecessor[vStart] = vStart;
		// 第二步: 重复松弛边
		for (int i = 1, len = this.getVerticeSize() - 1; i < len; i++) {
			for (PathTriple e : this.edges) {
				if (distance[e.getStart()] + e.getCost() < distance[e.getDestination()]) {
					distance[e.getDestination()] = distance[e.getStart()] + e.getCost();
					predecessor[e.getDestination()] = e.getStart();

				}
			}
		}
		// 第三步: 检查是否有负权回路 第三步必须在第二步后面
		for (PathTriple e : this.edges) {
			if (distance[e.getStart()] + e.getCost() < distance[e.getDestination()])
				return null; // 返回null表示包涵负权回路
		}
		NameObjectMap nameObjectMap = new NameObjectMap();
		if (returnAble) {
			nameObjectMap.put("cost", distance[vEnd]);
//			nameObjectMap.put("dis", distance);
//			nameObjectMap.put("pre", predecessor);
		}

		return nameObjectMap;
	}

	public NameObjectMap AStart(VerticeType source, VerticeType destination) {
		int vStart = this.getNameVerticeIndexMap().get(source);
		int vEnd = this.getNameVerticeIndexMap().get(destination);
		List<Pair<Integer, Integer>> container = new ArrayList<>();
		boolean[] visited = new boolean[this.getVerticeSize()];
		int[] cost = new int[this.getVerticeSize()]; // cost of the point source to index
		int[] pre = new int[this.getVerticeSize()];
		for (int index = 0; index < this.getVerticeSize(); index++) {
			cost[index] = MAXVALUE;
			pre[index] = -1;
			visited[index] = false;
		}
		container.add(new Pair(vStart, 0));
		visited[vStart] = true;
		cost[vStart] = 0;
		pre[vStart] = vStart;
		int curPoint = vStart;
		int min = Integer.MAX_VALUE;
		int newCost = MAXVALUE;
		int heuristic = 0;
		int delPointIndex = -1;
		while (container.size() != 0) {
			min = Integer.MAX_VALUE;
			for (int cnt = 0; cnt < container.size(); cnt++) {
				if (container.get(cnt).getValue() < min) {
					curPoint = container.get(cnt).getKey();
					min = container.get(cnt).getValue();
					delPointIndex = cnt;
				}
			}
			container.remove(delPointIndex);
			if (curPoint == vEnd) {
				break;
			}
			visited[curPoint] = true;
			for (Integer index : this.getAdjList().get(curPoint)) {
				newCost = cost[curPoint] + this.getCost(curPoint, index);
				if (!visited[index]) {
					if (newCost < cost[index]) {
//						visited[index] = true;
						cost[index] = newCost;
						heuristic = MyMath.Width(this.getVertices().get(index).getPositionX(),
								this.getVertices().get(index).getPositionY(),
								this.getVertices().get(vEnd).getPositionX(),
								this.getVertices().get(vEnd).getPositionY());// 欧氏距离
						container.add(new Pair<Integer, Integer>(index, newCost + heuristic));
						pre[index] = curPoint;
					}
				}
			}
		}
		NameObjectMap nameObjectMap = new NameObjectMap();
		if (returnAble) {
//		nameObjectMap.put("pre", pre);
//		nameObjectMap.put("dis", cost);
			nameObjectMap.put("cost", cost[vEnd]);
		}

		return nameObjectMap;
	}

	public NameObjectMap DijkstraHeap(VerticeType startPoint, VerticeType endPoint) {
		return this.DijkstraHeap(startPoint, endPoint, 0);
	}

	public NameObjectMap AStartHeap(VerticeType startPoint, VerticeType endPoint) {
		return this.DijkstraHeap(startPoint, endPoint, 1);
	}

	private NameObjectMap DijkstraHeap(VerticeType startPoint, VerticeType endPoint, double HeuristicFactor) {
		// descibe:"还是每次找基点，更新邻点。
		// 为什么这种就可以是eloge，而上面那种是n^2呢。
		// 原因在于下面这种用堆来优化了每次找离起点最近的点的时间复杂度。
		// 并且用邻接表优化了对于每一个基点，更新它的所有邻边的时间复杂度（上面那个就是1扫到n，很花时间）
		// 使用stl自带的堆priority_queue，大大降低了编程复杂度。
		// 不过如果是手写二叉堆的话，那么时间复杂度会好一个常数，是elogn。
		// 原因在于stl的堆不能更改堆中的节点，而二叉堆可以直接改堆中的节点，即松弛操作。
		// 不过编程复杂度太高了，也不会有人考场真的手写二叉堆hhhh

		// 总结一下呢就是，dj算法是一个优秀的最短路算法，在堆优化的情况下可以做到eloge。
		// ",
		int vStart = this.nameVerticeIndexMap.get(startPoint);
		int vEnd = this.nameVerticeIndexMap.get(endPoint);
		boolean[] finalArr = new boolean[this.getVerticeSize()];

		int[] dis = new int[this.getVerticeSize()];
		int[] pre = new int[this.getVerticeSize()];
		for (int i = 0; i < this.vertices.size(); i++) {
			dis[i] = MAXVALUE;
			pre[i] = -1;
			finalArr[i] = false;
		}
		MyPriorityQueue<Pair<Integer, Integer>> myPriorityQueue = new MyPriorityQueue<>(verticeSize,
				new Comparator<Pair<Integer, Integer>>() {

					@Override
					public int compare(Pair<Integer, Integer> o1, Pair<Integer, Integer> o2) {

						return o1.getValue() - o2.getValue();
					}
				});
		myPriorityQueue.add(new Pair<Integer, Integer>(vStart, 0));
		dis[vStart] = 0;
		pre[vStart] = vStart;
		finalArr[vStart] = true;
		Pair<Integer, Integer> nodePair;
		int currentNodeIndex = vStart;
		int cost = Integer.MAX_VALUE;
		while (myPriorityQueue.size() != 0) {
			nodePair = (Pair<Integer, Integer>) myPriorityQueue.poll();
			currentNodeIndex = nodePair.getKey();
			finalArr[currentNodeIndex] = true;
			/*
			 * 已经寻找到终点了 即finalArr[终点]=true 凡是finalArr为true的点都不会再更新他的dis值 因为已经找到最短了
			 */
			if (currentNodeIndex == vEnd)
				break;
			for (Integer index : this.getAdjList().get(currentNodeIndex)) {
				if (!finalArr[index]) {
					cost = dis[currentNodeIndex] + this.getCost(currentNodeIndex, index);
					if (cost < dis[index]) {
						dis[index] = cost;
						pre[index] = currentNodeIndex;
						if (HeuristicFactor != 0) {
							cost += HeuristicFactor * MyMath.Width(this.getVertices().get(index).getPositionX(),
									this.getVertices().get(index).getPositionY(),
									this.getVertices().get(vEnd).getPositionX(),
									this.getVertices().get(vEnd).getPositionY());// 是否加上启发因子
						}

						myPriorityQueue.add(new Pair<Integer, Integer>(index, cost));
					}
				}
			}
		}

		NameObjectMap nameObjectMap = new NameObjectMap();
		if (returnAble) {
//		nameObjectMap.put("dis", dis);
//		nameObjectMap.put("pre", pre);
			nameObjectMap.put("cost", dis[vEnd]);
		}

		return nameObjectMap;

	}

	public NameObjectMap AStartHeapTwoway(VerticeType startPoint, VerticeType endPoint, double HeuristicFactor) {
		return this.DijkstraHeapTwoway(startPoint, endPoint, HeuristicFactor);
	}

	public NameObjectMap DijkstraHeapTwoway(VerticeType startPoint, VerticeType endPoint) {
		return this.DijkstraHeapTwoway(startPoint, endPoint, 0);
	}

	private NameObjectMap DijkstraHeapTwoway(VerticeType startPoint, VerticeType endPoint, double HeuristicFactor) {
		int vStart = this.nameVerticeIndexMap.get(startPoint);
		int vEnd = this.nameVerticeIndexMap.get(endPoint);
		boolean[] finalArrFrom = new boolean[this.getVerticeSize()];
		boolean[] finalArrTo = new boolean[this.getVerticeSize()];
		int costTotalFrom = -1;
		int costTotalTo = -1;
		int[] disFrom = new int[this.getVerticeSize()];
		int[] preFrom = new int[this.getVerticeSize()];
		int[] disTo = new int[this.getVerticeSize()];
		int[] preTo = new int[this.getVerticeSize()];
		for (int i = 0; i < this.vertices.size(); i++) {
			disFrom[i] = MAXVALUE;
			preFrom[i] = -1;
			disTo[i] = MAXVALUE;
			preTo[i] = -1;
			finalArrFrom[i] = false;
			finalArrTo[i] = false;
		}
		MyPriorityQueue<Pair<Integer, Integer>> myPriorityQueueFrom = new MyPriorityQueue<>(verticeSize,
				new Comparator<Pair<Integer, Integer>>() {

					@Override
					public int compare(Pair<Integer, Integer> o1, Pair<Integer, Integer> o2) {

						return o1.getValue() - o2.getValue();
					}
				});
		MyPriorityQueue<Pair<Integer, Integer>> myPriorityQueueTo = new MyPriorityQueue<>(verticeSize,
				new Comparator<Pair<Integer, Integer>>() {

					@Override
					public int compare(Pair<Integer, Integer> o1, Pair<Integer, Integer> o2) {

						return o1.getValue() - o2.getValue();
					}
				});
		myPriorityQueueFrom.add(new Pair<Integer, Integer>(vStart, 0));
		myPriorityQueueTo.add(new Pair<Integer, Integer>(vEnd, 0));
		disFrom[vStart] = 0;
		preFrom[vStart] = vStart;
		disTo[vEnd] = 0;
		preTo[vEnd] = vEnd;
		finalArrFrom[vStart] = true;
		finalArrTo[vEnd] = true;
		Pair<Integer, Integer> nodePairFrom;
		Pair<Integer, Integer> nodePairTo;
		int currentNodeIndexFrom = vStart;
		int currentNodeIndexTo = vEnd;
		int costFrom = MAXVALUE;
		int costTo = MAXVALUE;
		while (myPriorityQueueFrom.size() != 0 && myPriorityQueueTo.size() != 0) {
			nodePairFrom = (Pair<Integer, Integer>) myPriorityQueueFrom.peek();
			nodePairTo = (Pair<Integer, Integer>) myPriorityQueueTo.peek();
			currentNodeIndexFrom = nodePairFrom.getKey();
			currentNodeIndexTo = nodePairTo.getKey();

			if (finalArrFrom[currentNodeIndexTo] || finalArrTo[currentNodeIndexFrom]) {// 另一个final表中已经访问，说明前后接头了
				costTotalFrom = disFrom[vEnd] = disTo[vEnd]  = disFrom[currentNodeIndexTo]+ disTo[currentNodeIndexTo];
				costTotalTo=disFrom[currentNodeIndexFrom] + disTo[currentNodeIndexFrom];
				break;
			}

			finalArrFrom[currentNodeIndexFrom] = true;
			finalArrTo[currentNodeIndexTo] = true;
			myPriorityQueueFrom.poll();
			myPriorityQueueTo.poll();
			/**
			 * 试想加入尾部向前的那条路和头部向尾部那条路中间接头在mid点 由于串行性，两条路的头部可以看作同时被赋值finalArr为true
			 * 
			 * 
			 */
			for (Integer index : this.getAdjList().get(currentNodeIndexFrom)) {
				if (!finalArrFrom[index]) {
					costFrom = disFrom[currentNodeIndexFrom] + this.getCost(currentNodeIndexFrom, index);
					if (costFrom < disFrom[index]) {
						disFrom[index] = costFrom;
						preFrom[index] = currentNodeIndexFrom;
						if (HeuristicFactor != 0) {
							costFrom += MyMath.Width(this.getVertices().get(index).getPositionX(),
									this.getVertices().get(index).getPositionY(),
									this.getVertices().get(vEnd).getPositionX(), // 向后启发
									this.getVertices().get(vEnd).getPositionY());
						}
						myPriorityQueueFrom.add(new Pair<Integer, Integer>(index, costFrom));
					}
				}
			}
			for (Integer index : this.getAdjList().get(currentNodeIndexTo)) {
				if (!finalArrTo[index]) {
					costTo = disTo[currentNodeIndexTo] + this.getCost(currentNodeIndexTo, index);
					if (costTo < disTo[index]) {
						disTo[index] = costTo;
						preTo[index] = currentNodeIndexTo;
						if (HeuristicFactor != 0) {
							costTo += MyMath.Width(this.getVertices().get(index).getPositionX(),
									this.getVertices().get(index).getPositionY(),
									this.getVertices().get(vStart).getPositionX(), // 向前启发
									this.getVertices().get(vStart).getPositionY());
						}
						myPriorityQueueTo.add(new Pair<Integer, Integer>(index, costTo));
					}
				}
			}
		}

		NameObjectMap nameObjectMap = new NameObjectMap();
		if (returnAble) {
//		nameObjectMap.put("dis", dis);
//		nameObjectMap.put("pre", pre);

			nameObjectMap.put("cost", Integer.min(costTotalFrom, costTotalTo));
		}

		return nameObjectMap;

	}

	public NameObjectMap DijkstraHeapTwowayWithThead(VerticeType startPoint, VerticeType endPoint)
			throws InterruptedException {
		return this.DijkstraHeapTwowayWithThead(startPoint, endPoint, 0);
	}

	public NameObjectMap AStartHeapTwowayWithThead(VerticeType startPoint, VerticeType endPoint, double HeuristicFactor)
			throws InterruptedException {
		return this.DijkstraHeapTwowayWithThead(startPoint, endPoint, HeuristicFactor);

	}

	private NameObjectMap DijkstraHeapTwowayWithThead(VerticeType startPoint, VerticeType endPoint,
			double HeuristicFactor) throws InterruptedException {
		int vStart = this.nameVerticeIndexMap.get(startPoint);
		int vEnd = this.nameVerticeIndexMap.get(endPoint);
		int size = this.vertices.size();
		ThreadDijkstraFrom threadDijkstraFrom = new ThreadDijkstraFrom(vStart, vEnd, size, this.adjList, this.vertices,
				this.matrix);
		ThreadDijkstraTo threadDijkstraTo = new ThreadDijkstraTo(vStart, vEnd, size, this.adjList, this.vertices,
				this.matrix);
		threadDijkstraFrom.setHeuristicFactor(HeuristicFactor);
		threadDijkstraTo.setHeuristicFactor(HeuristicFactor);
		threadDijkstraFrom.start();
		threadDijkstraTo.start();
		threadDijkstraFrom.join();
		threadDijkstraTo.join();
		NameObjectMap nameObjectMap = new NameObjectMap();
		if (returnAble) {
			nameObjectMap.put("cost", Integer.min(Graph.costTotalThreadFrom,Graph.costTotalThreadTo));
		}
		return nameObjectMap;

	}
//	public NameObjectMap AStartHeapTwowayWithThead(VerticeType startPoint, VerticeType endPoint)
//			throws InterruptedException {
//		int vStart = this.nameVerticeIndexMap.get(startPoint);
//		int vEnd = this.nameVerticeIndexMap.get(endPoint);
//		int size = this.vertices.size();
//		ThreadAStartFrom threadAStartFrom = new ThreadAStartFrom(vStart, vEnd, size, this.adjList, this.matrix);
//		ThreadAStartTo threadAStartTo = new ThreadAStartTo(vStart, vEnd, size, this.adjList, this.matrix);
//		threadAStartFrom.start();
//		threadAStartTo.start();
//		threadAStartFrom.join();
//		threadAStartTo.join();
//		NameObjectMap nameObjectMap = new NameObjectMap();
//		if (returnAble) {
//			nameObjectMap.put("cost", Graph.costTotalThreadShared);
//		}
//		return nameObjectMap;
//
//	}

	/**
	 * 
	 * 这种多优先队列的优化方法主要针对需要建立一个巨大的队列的情况
	 * 将这这个巨大的队列分为queueNums个小队列来操作，节省了每次入队和出队的时候最小堆的上浮下沉时间
	 * queueNums=1的情况就是关于堆优化Dijkstra算法的原型
	 * 
	 * @param startPoint
	 * @param endPoint
	 * @param queueNums
	 * @return
	 */
	public NameObjectMap DijkstraMultiHeap(VerticeType startPoint, VerticeType endPoint, int queueNums) {
		return this.DijkstraMultiHeap(startPoint, endPoint, queueNums, 0.0);
	}

	private NameObjectMap DijkstraMultiHeap(VerticeType startPoint, VerticeType endPoint, int queueNums,
			double HeuristicFactor) {
		// descibe:"还是每次找基点，更新邻点。
		// 为什么这种就可以是eloge，而上面那种是n^2呢。
		// 原因在于下面这种用堆来优化了每次找离起点最近的点的时间复杂度。
		// 并且用邻接表优化了对于每一个基点，更新它的所有邻边的时间复杂度（上面那个就是1扫到n，很花时间）
		// 使用stl自带的堆priority_queue，大大降低了编程复杂度。
		// 不过如果是手写二叉堆的话，那么时间复杂度会好一个常数，是elogn。
		// 原因在于stl的堆不能更改堆中的节点，而二叉堆可以直接改堆中的节点，即松弛操作。
		// 不过编程复杂度太高了，也不会有人考场真的手写二叉堆hhhh

		// 总结一下呢就是，dj算法是一个优秀的最短路算法，在堆优化的情况下可以做到eloge。
		// ",
		if (queueNums <= 0)
			queueNums = 1;
		else if (queueNums > this.getVerticeSize() / 3)
			queueNums = 2;// 这里待定需要实验来看什么时候最优
		int vStart = this.nameVerticeIndexMap.get(startPoint);
		int vEnd = this.nameVerticeIndexMap.get(endPoint);
		boolean[] finalArr = new boolean[this.getVerticeSize()];

		int[] dis = new int[this.getVerticeSize()];
		int[] pre = new int[this.getVerticeSize()];
		for (int i = 0; i < this.vertices.size(); i++) {
			dis[i] = MAXVALUE;
			pre[i] = -1;
			finalArr[i] = false;
		}
		dis[vStart] = 0;
		pre[vStart] = vStart;
		finalArr[vStart] = true;
		Pair<Integer, Integer> nodePair;
		int currentNodeIndex = vStart;
		int cost = MAXVALUE;
		MyPriorityQueue<Pair<Integer, Integer>>[] myPriorityQueueArr = new MyPriorityQueue[queueNums];
		for (int i = 0; i < queueNums; i++) {
			myPriorityQueueArr[i] = new MyPriorityQueue<>(verticeSize,new Comparator<Pair<Integer, Integer>>() {
				@Override
				public int compare(Pair<Integer, Integer> o1, Pair<Integer, Integer> o2) {
					return o1.getValue() - o2.getValue();
				}
			});

		}
		// 默认第一个队列作为最开始的入队
		myPriorityQueueArr[0].add(new Pair<Integer, Integer>(vStart, 0));
		// 多堆的所需变量
		int minIndexToDequeue = 0;// 堆的列表中对头最小的那个index;
		int minIndexToEnqueue = 0;// 堆的列表中size最小的那个index;
		int minQueueValue = MAXVALUE;
		int minSizeValue = MAXVALUE;
		boolean queueIsNoAlltNull = false;
		while (true) {
//			nodePair = myPriorityQueue.poll();
			minQueueValue = MAXVALUE;
			queueIsNoAlltNull = false;
			for (int i = 0; i < queueNums; i++) {
				queueIsNoAlltNull = queueIsNoAlltNull || myPriorityQueueArr[i].size() != 0 ? true : false;
				/**
				 * 
				 * 选myPriorityQueueArr中对头最小的出队 选myPriorityQueueArr中size最小的入队 所有优先队列都为空了说明循环可以结束了
				 * 对于每个优先队列的大小不放入一个最小堆中的原因是每次while循环都要判断至少有一个堆不为空
				 * 并且每次需要查找所有优先队列中最小的那一个，以及查找队列size最小的哪个,两者都要遍历所有优先队列，两者的动作高度重合
				 * 所以作为队列的size不设新的优先队列来查找最小值;
				 * 
				 */
				if (myPriorityQueueArr[i].size() > 0 && ((Pair<Integer, Integer>) myPriorityQueueArr[i].peek()).getValue() < minQueueValue) {
					minIndexToDequeue = i;
					minQueueValue = ((Pair<Integer, Integer>) myPriorityQueueArr[i].peek()).getValue();
				}
				if (myPriorityQueueArr[i].size() < minSizeValue) {// 选size最小的那个优先队列
					minIndexToEnqueue = i;
					minSizeValue = myPriorityQueueArr[i].size();
				}
			}
			if (!queueIsNoAlltNull)
				break;
			nodePair = (Pair<Integer, Integer>) myPriorityQueueArr[minIndexToDequeue].poll();
			// 更新minIndex的indexPriorityQueue
			// curren是当前最小的cost的结点
			currentNodeIndex = nodePair.getKey();
//			if (finalArr[currentNodeIndex]) continue;
			finalArr[currentNodeIndex] = true;
			/*
			 * 已经寻找到终点了 即finalArr[终点]=true 凡是finalArr为true的点都不会再更新他的dis值 因为已经找到最短了
			 */
			if (currentNodeIndex == vEnd)
				break;

			for (Integer index : this.getAdjList().get(currentNodeIndex)) {
				if (!finalArr[index]) {
					cost = dis[currentNodeIndex] + this.getCost(currentNodeIndex, index);
					if (cost < dis[index]) {// 这里是更新不是选择最小，理论上可以多线程，因为线程之间无关
						dis[index] = cost;
						pre[index] = currentNodeIndex;
						if (HeuristicFactor != 0) {
							cost += MyMath.Width(this.getVertices().get(index).getPositionX(),
									this.getVertices().get(index).getPositionY(),
									this.getVertices().get(vEnd).getPositionX(),
									this.getVertices().get(vEnd).getPositionY());
						}
						myPriorityQueueArr[minIndexToEnqueue].add(new Pair<Integer, Integer>(index, cost));
						// 选择size最小的堆加入，为了保持平衡

					}
				}
			}

		}

		NameObjectMap nameObjectMap = new NameObjectMap();
		if (returnAble) {
//		nameObjectMap.put("dis", dis);
//		nameObjectMap.put("pre", pre);			
			nameObjectMap.put("cost", dis[vEnd]);
		}

		return nameObjectMap;

	}

	public NameObjectMap AStartMultiHeap(VerticeType source, VerticeType destination, int queueNums,
			double HeuristicFactor) {
		return this.DijkstraMultiHeap(source, destination, queueNums, HeuristicFactor);
	}

	public NameObjectMap AStartHeapWtihTreads(VerticeType source, VerticeType destination, int queueNums,
			int threadNums, double HeuristicFactor) throws InterruptedException {
		return this.DijkstraHeapWtihTreads(source, destination, queueNums, threadNums, HeuristicFactor);
	}

	public NameObjectMap DijkstraHeapWtihTreads(VerticeType startPoint, VerticeType endPoint, int queueNums,
			int threadNums) throws InterruptedException {
		return this.DijkstraHeapWtihTreads(startPoint, endPoint, queueNums, threadNums, 0.0);
	}

	private NameObjectMap DijkstraHeapWtihTreads(VerticeType startPoint, VerticeType endPoint, int queueNums,
			int threadNums, double HeuristicFactor) throws InterruptedException {

		if (queueNums <= 0)
			queueNums = 1;
		int vStart = this.nameVerticeIndexMap.get(startPoint);
		int vEnd = this.nameVerticeIndexMap.get(endPoint);
		boolean[] finalArr = new boolean[this.getVerticeSize()];

		int[] dis = new int[this.getVerticeSize()];
		int[] pre = new int[this.getVerticeSize()];
		for (int i = 0; i < this.vertices.size(); i++) {
			dis[i] = MAXVALUE;
			pre[i] = -1;
			finalArr[i] = false;
		}
		long anthorCost = 0;
		dis[vStart] = 0;
		pre[vStart] = vStart;
		finalArr[vStart] = true;
		Pair<Integer, Integer> nodePair;
		int currentNodeIndex = vStart;
		int cost = MAXVALUE;
		// 多堆的所需变量
		int minIndexToDequeue = 0;// 堆的列表中对头最小的那个index;
		int enqueueArrLenth = Integer.min(threadNums, queueNums);
		// 多线程需要的变量初始化
		Graph.myPriorityQueueArr = new MyPriorityQueue[queueNums];
		for (int i = 0; i < queueNums; i++) {
			Graph.myPriorityQueueArr[i] = new MyPriorityQueue<>(verticeSize,new Comparator<Pair<Integer, Integer>>() {
				@Override
				public int compare(Pair<Integer, Integer> o1, Pair<Integer, Integer> o2) {
					return o1.getValue() - o2.getValue();
				}
			});
		}
		// 默认第一个队列作为最开始的入队
		Graph.myPriorityQueueArr[0].add(new Pair<Integer, Integer>(vStart, 0));

		// 这是一个定长为enqueueArrLenth的有序表
		FixedlengthMinList<Pair<Integer, Integer>> minIndexToEnqueue = new FixedlengthMinList<>(enqueueArrLenth,
				new Comparator<Pair<Integer, Integer>>() {
					@Override
					public int compare(Pair<Integer, Integer> o1, Pair<Integer, Integer> o2) {
						return o1.getValue() - o2.getValue();
					}
				});

		int minQueueValue = MAXVALUE;
		int minSizeValue = MAXVALUE;
		boolean queueIsNoAlltNull = false;
		while (true) {
			minQueueValue = MAXVALUE;
			queueIsNoAlltNull = false;
			for (int i = 0; i < queueNums; i++) {
				queueIsNoAlltNull = queueIsNoAlltNull || Graph.myPriorityQueueArr[i].size() != 0 ? true : false;
				/**
				 * 选myPriorityQueueArr中对头最小的出队 选myPriorityQueueArr中size最小的入队 所有优先队列都为空了说明循环可以结束了
				 * 对于每个优先队列的大小不放入一个最小堆中的原因是每次while循环都要判断至少有一个堆不为空
				 * 并且每次需要查找所有优先队列中队头最小的那一个，以及查找队列size最小的哪个,两者都要遍历所有优先队列，两者的动作高度重合
				 * 所以作为队列的size不设新的优先队列来查找最小值;
				 * 
				 */
				if (Graph.myPriorityQueueArr[i].size() > 0
						&& ((Pair<Integer, Integer>) Graph.myPriorityQueueArr[i].peek()).getValue() < minQueueValue) {// 选取队列头部最小的那个index
					minQueueValue = ((Pair<Integer, Integer>) Graph.myPriorityQueueArr[i].peek()).getValue();
					minIndexToDequeue = i;
				}
				minIndexToEnqueue.insert(new Pair<Integer, Integer>(i, Graph.myPriorityQueueArr[i].size()));
				// 选enqueueArrLenth个size最小的优先队列

			}

			if (!queueIsNoAlltNull) {
				break;
			}
			nodePair = (Pair<Integer, Integer>) Graph.myPriorityQueueArr[minIndexToDequeue].poll();
			// 更新minIndex的indexPriorityQueue
			currentNodeIndex = nodePair.getKey();
			finalArr[currentNodeIndex] = true;
			/*
			 * 已经寻找到终点了 即finalArr[终点]=true 凡是finalArr为true的点都不会再更新他的dis值 因为已经找到最短了
			 */
			if (currentNodeIndex == vEnd) {
				break;
			}
			int index;
			for (int cnt = 0; cnt < this.getAdjList().get(currentNodeIndex).size(); /* cnt++ */) {
				List<Thread> threads = new ArrayList<>();
				for (int queueArrIndex = 0; queueArrIndex < enqueueArrLenth; queueArrIndex++) {
					index = this.getAdjList().get(currentNodeIndex).get(cnt);
					if (!finalArr[index]) {
						cost = dis[currentNodeIndex] + this.getCost(currentNodeIndex, index);
						if (cost < dis[index]) {// 这里是更新不是选择最小，理论上可以多线程，因为线程之间无关
							dis[index] = cost;
							pre[index] = currentNodeIndex;
							if (HeuristicFactor != 0) {
								cost += MyMath.Width(this.getVertices().get(index).getPositionX(),
										this.getVertices().get(index).getPositionY(),
										this.getVertices().get(vEnd).getPositionX(),
										this.getVertices().get(vEnd).getPositionY());
							}
							threads.add(new ThreadWtihParametersTriple(minIndexToEnqueue.get(queueArrIndex).getKey(),
									index, cost));
							/**
							 * 这里优化的是添加入优先队列的上浮下沉操作时间 先创建再执行，创建
							 * 
							 */
						}
					}
					cnt++;
					if (cnt >= this.getAdjList().get(currentNodeIndex).size())
						break;
				}
//				ExecutorService fixedThreadPool = Executors.newCachedThreadPool();
//				fixedThreadPool.execute();
				for (int i = 0; i < threads.size(); i++) {
					threads.get(i).start();
//					fixedThreadPool.execute(threads.get(i));
				}
//				 fixedThreadPool.shutdown();
//				  while (!fixedThreadPool.isTerminated()); //等待所有任务都执行结束
//				for (int i = 0; i < threads.size(); i++) {
//					threads.get(i).join();
//				}

			}
			minIndexToEnqueue.clear();
		}

		NameObjectMap nameObjectMap = new NameObjectMap();
		if (returnAble) {
//		nameObjectMap.put("dis", dis);
//		nameObjectMap.put("pre", pre);	
			nameObjectMap.put("cost", dis[vEnd]);
		}

		return nameObjectMap;

	}

	public NameObjectMap BellmanFordSPFA(VerticeType source, VerticeType destination) {
		int vStart = this.nameVerticeIndexMap.get(source);
		int vEnd = this.nameVerticeIndexMap.get(destination);
		List<Integer> queue = new ArrayList<>();
		int[] pre = new int[this.getVerticeSize()];
		boolean[] finalArr = new boolean[this.getVerticeSize()];
		int[] dis = new int[this.getVerticeSize()];
		for (int index = 0; index < this.getVerticeSize(); index++) {
			dis[index] = MAXVALUE;
			pre[index] = -1;
			finalArr[index] = false;

		}
		queue.add(vStart);
		pre[vStart] = vStart;
		dis[vStart] = 0;
		finalArr[vStart] = true;
		int currentNodeIndex = vStart;
		int cost = MAXVALUE;
		while (queue.size() > 0) {
			currentNodeIndex = queue.get(0);
			if (currentNodeIndex == vEnd) {
				break;
			}
			queue.remove(0);
			for (Integer index : this.getAdjList().get(currentNodeIndex)) {
				cost = dis[currentNodeIndex] + this.getCost(currentNodeIndex, index);
				if (cost < dis[index]) {
					dis[index] = cost;
					pre[index] = currentNodeIndex;
				}
				if (!finalArr[index]) {
					if (queue.size() > 0 && dis[index] < dis[queue.get(0)]) {
						queue.add(0, index);
					} else {
						queue.add(index);
					}
					finalArr[index] = true;
				}
			}
		}
		NameObjectMap nameObjectMap = new NameObjectMap();
		if (returnAble) {
			nameObjectMap.put("cost", dis[vEnd]);
//		nameObjectMap.put("pre", pre);
//		nameObjectMap.put("dis", dis);		
		}

		return nameObjectMap;
	}

//	public NameObjectMap AStartHeap(VerticeType source, VerticeType destination, int queueNums) {
//		int vStart = this.nameVerticeIndexMap.get(source);
//		int vEnd = this.nameVerticeIndexMap.get(destination);
//		boolean[] finalArr = new boolean[this.getVerticeSize()];
//		int[] dis = new int[this.getVerticeSize()]; // cost of the point source to index
//		int[] pre = new int[this.getVerticeSize()];
//		for (int index = 0; index < this.getVerticeSize(); index++) {
//			dis[index] = MAXVALUE;
//			pre[index] = vStart;
//			finalArr[index] = false;
//		}
//		finalArr[vStart] = true;
//		dis[vStart] = 0;
//		MyPriorityQueue<Pair<Integer, Integer>>[] myPriorityQueueArr = new MyPriorityQueue[queueNums];
//		for (int i = 0; i < queueNums; i++) {
//			myPriorityQueueArr[i] = new MyPriorityQueue<>(new Comparator<Pair<Integer, Integer>>() {
//				@Override
//				public int compare(Pair<Integer, Integer> o1, Pair<Integer, Integer> o2) {
//					return o1.getValue() - o2.getValue();
//				}
//			});
//
//		}
//		// 默认第一个队列作为最开始的入队
//		myPriorityQueueArr[0].add(new Pair<Integer, Integer>(vStart, 0));
//		int currentNodeIndex = vStart;
//		int newCost = MAXVALUE;
//		int heuristic = 1;
//		Pair<Integer, Integer> tempPair;
//		// 多堆 的临时变量
//		int minIndexToDequeue = 1;// 堆的列表中对头最小的那个index;
//		int minIndexToEnqueue = 1;// 堆的列表中size最小的那个index;
//		int minQueueValue = MAXVALUE;
//		int minSizeValue = MAXVALUE;
//		boolean queueIsNoAlltNull = false;
//		while (true) {
//			minQueueValue = MAXVALUE;
//			queueIsNoAlltNull = false;
//			for (int i = 0; i < queueNums; i++) {
//				queueIsNoAlltNull = queueIsNoAlltNull || myPriorityQueueArr[i].size() != 0 ? true : false;
//				/**
//				 * 
//				 * 选myPriorityQueueArr中对头最小的出队 选myPriorityQueueArr中size最小的入队 所有优先队列都为空了说明循环可以结束了
//				 * 对于每个优先队列的大小不放入一个最小堆中的原因是每次while循环都要判断至少有一个堆不为空
//				 * 并且每次需要查找所有优先队列中最小的那一个，以及查找队列size最小的哪个,两者都要遍历所有优先队列，两者的动作高度重合
//				 * 所以作为队列的size不设新的优先队列来查找最小值;
//				 * 
//				 */
//				if (myPriorityQueueArr[i].size() > 0 && myPriorityQueueArr[i].peek().getValue() < minQueueValue) {
//					minIndexToDequeue = i;
//					minQueueValue = myPriorityQueueArr[i].peek().getValue();
//				}
//				if (myPriorityQueueArr[i].size() < minSizeValue) {
//					minIndexToEnqueue = i;
//					minSizeValue = minQueueValue = myPriorityQueueArr[i].peek().getValue();
//				}
//			}
//			if (!queueIsNoAlltNull)
//				break;
//			tempPair = myPriorityQueueArr[minIndexToDequeue].poll();
//
//			currentNodeIndex = tempPair.getKey();
//			finalArr[currentNodeIndex]=true;
//			if (currentNodeIndex == vEnd)
//				break;
//			for (Integer index : this.getAdjList().get(currentNodeIndex)) {
//				if (!finalArr[index]) {
//					newCost = dis[currentNodeIndex] + this.getCost(currentNodeIndex, index);
//					if (newCost < dis[index]) {// 这里是更新不是选择最小，理论上可以多线程，因为线程之间无关
////						finalArr[index] = true;
//						dis[index] = newCost;
//						heuristic = 
//								MyMath.Width(this.getVertices().get(index).getPositionX(),
//										this.getVertices().get(index).getPositionY(),
//										 this.getVertices().get(vEnd).getPositionX(),
//										this.getVertices().get(vEnd).getPositionY());
//						myPriorityQueueArr[minIndexToEnqueue].add(new Pair(index, newCost + heuristic));
//						pre[index] = currentNodeIndex;
//					}
//				}
//			}
//
//		}
//		NameObjectMap nameObjectMap = new NameObjectMap();
//		if (returnAble) {
////		nameObjectMap.put("pre", pre);
////		nameObjectMap.put("dis", dis);
//			nameObjectMap.put("cost", dis[vEnd]);
//		}
//
//		return nameObjectMap;
//	}

//	public NameObjectMap AStartOneHeap(VerticeType source, VerticeType destination) {
//		int vStart = this.nameVerticeIndexMap.get(source);
//		int vEnd = this.nameVerticeIndexMap.get(destination);
//		MyPriorityQueue<Pair<Integer, Integer>> myPriorityQueue = new MyPriorityQueue<>(
//				new Comparator<Pair<Integer, Integer>>() {
//
//					@Override
//					public int compare(Pair<Integer, Integer> o1, Pair<Integer, Integer> o2) {
//
//						return o1.getValue() - o2.getValue();
//					}
//				});
//		boolean[] finalArr = new boolean[this.getVerticeSize()];
//		int[] dis = new int[this.getVerticeSize()]; // cost of the point source to index
//		int[] pre = new int[this.getVerticeSize()];
//		for (int index = 0; index < this.getVerticeSize(); index++) {
//			dis[index] = MAXVALUE;
//			pre[index] = vStart;
//			finalArr[index] = false;
//		}
//		myPriorityQueue.add(new Pair<Integer, Integer>(vStart, 0));
//		finalArr[vStart] = true;
//		dis[vStart] = 0;
//		pre[vStart] = vStart;
//		int currentNodeIndex = vStart;
//		int min = MAXVALUE;
//		int newCost = MAXVALUE;
//		int heuristic = 1;
//		Pair<Integer, Integer> tempPair;
//		while (myPriorityQueue.size() != 0) {
//			tempPair = myPriorityQueue.poll();
//			currentNodeIndex = tempPair.getKey();
//			finalArr[currentNodeIndex]=true;
//			if (currentNodeIndex == vEnd)
//				break;
//			for (Integer index : this.getAdjList().get(currentNodeIndex)) {
//				if (!finalArr[index]) {
//					newCost = dis[currentNodeIndex] + this.getCost(currentNodeIndex, index);
//					if (newCost < dis[index]) {
////						finalArr[index] = true;
//						dis[index] = newCost;
//						heuristic = MyMath.Width(this.getVertices().get(index).getPositionX(),
//								this.getVertices().get(index).getPositionY(),
//								 this.getVertices().get(vEnd).getPositionX(),
//								this.getVertices().get(vEnd).getPositionY());
//						myPriorityQueue.add(new Pair(index, newCost + heuristic));
//						pre[index] = currentNodeIndex;
//					}
//				}
//			}
//
//		}
//		NameObjectMap nameObjectMap = new NameObjectMap();
//		if (returnAble) {
////		nameObjectMap.put("pre", pre);
////		nameObjectMap.put("dis", dis);
//			nameObjectMap.put("cost", dis[vEnd]);
//		}
//
//		return nameObjectMap;
//	}
//	public NameObjectMap AStartHeapTwoway(VerticeType startPoint, VerticeType endPoint) {
//		int vStart = this.nameVerticeIndexMap.get(startPoint);
//		int vEnd = this.nameVerticeIndexMap.get(endPoint);
//		MyPriorityQueue<Pair<Integer, Integer>> myPriorityQueueFrom = new MyPriorityQueue<>(
//				new Comparator<Pair<Integer, Integer>>() {
//
//					@Override
//					public int compare(Pair<Integer, Integer> o1, Pair<Integer, Integer> o2) {
//
//						return o1.getValue() - o2.getValue();
//					}
//				});
//		MyPriorityQueue<Pair<Integer, Integer>> myPriorityQueueTo = new MyPriorityQueue<>(
//				new Comparator<Pair<Integer, Integer>>() {
//
//					@Override
//					public int compare(Pair<Integer, Integer> o1, Pair<Integer, Integer> o2) {
//
//						return o1.getValue() - o2.getValue();
//					}
//				});
//		boolean[] finalArrFrom = new boolean[this.getVerticeSize()];
//		boolean[] finalArrTo = new boolean[this.getVerticeSize()];
//		int[] disFrom = new int[this.getVerticeSize()]; // cost of the point source to index
//		int[] preFrom = new int[this.getVerticeSize()];
//		int[] disTo = new int[this.getVerticeSize()]; // cost of the point source to index
//		int[] preTo = new int[this.getVerticeSize()];
//		for (int index = 0; index < this.getVerticeSize(); index++) {
//			disFrom[index] = MAXVALUE;
//			preFrom[index] = vStart;
//			finalArrFrom[index] = false;
//			disTo[index] = MAXVALUE;
//			preTo[index] = vStart;
//			finalArrTo[index] = false;
//		}
//		myPriorityQueueFrom.add(new Pair<Integer, Integer>(vStart, 0));
//		myPriorityQueueTo.add(new Pair<Integer, Integer>(vEnd, 0));
//		finalArrFrom[vStart] = true;
//		finalArrTo[vEnd] = true;
//		disFrom[vStart] = 0;
//		disTo[vEnd] = 0;
//		preFrom[vStart] = vStart;
//		preTo[vEnd] = vEnd;
//		int currentNodeIndexFrom = vStart;
//		int currentNodeIndexTo = vEnd;
//		int newCost = MAXVALUE;
//		int heuristic = 1;
//		Pair<Integer, Integer> tempPairFrom;
//		Pair<Integer, Integer> tempPairTo;
//		int costTotal = -1;
//		while (myPriorityQueueFrom.size() != 0 && myPriorityQueueTo.size() != 0) {
//			tempPairFrom = myPriorityQueueFrom.peek();
//			tempPairTo = myPriorityQueueTo.peek();
//			currentNodeIndexFrom = tempPairFrom.getKey();
//			currentNodeIndexTo = tempPairTo.getKey();
//			if (finalArrFrom[currentNodeIndexTo] || finalArrTo[currentNodeIndexFrom]) {// 另一个final表中已经访问，说明前后接头了
//				System.out.println("To");
//				costTotal = disFrom[vEnd] = disTo[vEnd] = disTo[currentNodeIndexTo] = disFrom[currentNodeIndexTo]
//						+ disTo[currentNodeIndexTo];
//				costTotal = Integer.min(disFrom[currentNodeIndexFrom] + disTo[currentNodeIndexFrom], costTotal);
//				break;
//			}
//			finalArrFrom[currentNodeIndexFrom] = true;
//			finalArrTo[currentNodeIndexTo] = true;
//			myPriorityQueueFrom.poll();
//			myPriorityQueueTo.poll();
//			for (Integer index : this.getAdjList().get(currentNodeIndexFrom)) {
//				if (!finalArrFrom[index]) {
//					newCost = disFrom[currentNodeIndexFrom] + this.getCost(currentNodeIndexFrom, index);
//					if (newCost < disFrom[index]) {
////						finalArrFrom[index] = true;
//						disFrom[index] = newCost;
//						heuristic = MyMath.Width(this.getVertices().get(index).getPositionX(),
//								this.getVertices().get(index).getPositionY(),
//								this.getVertices().get(vEnd).getPositionX(),
//								this.getVertices().get(vEnd).getPositionY());
//						heuristic = 0;
//						myPriorityQueueFrom.add(new Pair(index, newCost + heuristic));
//						preFrom[index] = currentNodeIndexFrom;
//					}
//				}
//			}
//			for (Integer index : this.getAdjList().get(currentNodeIndexTo)) {
//				if (!finalArrTo[index]) {
//					newCost = disTo[currentNodeIndexTo] + this.getCost(currentNodeIndexTo, index);
//					if (newCost < disTo[index]) {
////						finalArrTo[index] = true;
//						disTo[index] = newCost;
//						heuristic = MyMath.Width(this.getVertices().get(index).getPositionX(),
//								this.getVertices().get(index).getPositionY(),
//								this.getVertices().get(vEnd).getPositionX(),
//								this.getVertices().get(vEnd).getPositionY());// 这里的值为虚假的
//						heuristic = 0;
//						myPriorityQueueTo.add(new Pair(index, newCost + heuristic));
//						preTo[index] = currentNodeIndexTo;
//					}
//				}
//			}
//
//		}
//
//		NameObjectMap nameObjectMap = new NameObjectMap();
//		if (returnAble) {
////		nameObjectMap.put("dis", dis);
////		nameObjectMap.put("pre", pre);
//
//			nameObjectMap.put("cost", costTotal);
//		}
//
//		return nameObjectMap;
//
//	}

}
