package renxuan.entity;

import java.util.Comparator;
import java.util.List;

import renxuan.lib.MyPriorityQueue;
import renxuan.tools.MyMath;
import renxuan.tools.Pair;
import renxuan.tools.Vertice;

public class ThreadDijkstraFrom extends Thread {//从头开始向尾部方向执行的dijskra线程对象

	int vStart;
	int vEnd;
	MyPriorityQueue<Pair<Integer, Integer>> myPriorityQueueFrom;
	Pair<Integer, Integer> nodePairFrom;
	int preNodeIndexFrom;
	int costFrom;
	List<Vertice> vertices;
	List<List<Integer>> adjList;
	int[][] costList;
	double HeuristicFactor=0;
	public void setHeuristicFactor(double HeuristicFactor) {
		this.HeuristicFactor=HeuristicFactor;
	}
	public ThreadDijkstraFrom(int vStart, int vEnd, int size, List<List<Integer>> adjList,List<Vertice> vertices, int[][] costList) {
		super();
		this.vStart = vStart;
		this.vEnd = vEnd;
		this.adjList = adjList;
		this.costList = costList;
		this.vertices=vertices;
		costFrom = Graph.MAXVALUE;
		Graph.disFromThread = new int[size];
		Graph.preFromThread = new int[size];
		Graph.isVisitedFrom = new boolean[size];
		for (int i = 0; i < size; i++) {
			Graph.disFromThread[i] = Graph.MAXVALUE;
			Graph.preFromThread[i] = -1;
			Graph.isVisitedFrom[i] = false;
		}
		preNodeIndexFrom = vStart;
		myPriorityQueueFrom = new MyPriorityQueue<>(size,new Comparator<Pair<Integer, Integer>>() {
			@Override
			public int compare(Pair<Integer, Integer> o1, Pair<Integer, Integer> o2) {
				return o1.getValue() - o2.getValue();
			}
		});
		myPriorityQueueFrom.add(new Pair<Integer, Integer>(vStart, 0));
		Graph.disFromThread[vStart] = 0;
		Graph.preFromThread[vStart] = vStart;
		Graph.isVisitedFrom[vStart] = true;
	}

	public void run() {
		while (myPriorityQueueFrom.size() != 0) {
			nodePairFrom = (Pair<Integer, Integer>) myPriorityQueueFrom.peek();
			Graph.currentNodeIndexFromThread = nodePairFrom.getKey();
			synchronized (this) {
				if (Graph.isVisitedTo[Graph.currentNodeIndexFromThread] == true) {
					Graph.costTotalThreadFrom  = Graph.disFromThread[Graph.currentNodeIndexFromThread]
							+ Graph.disToThread[Graph.currentNodeIndexFromThread];
					break;
				}
			}

			Graph.isVisitedFrom[Graph.currentNodeIndexFromThread] = true;
			myPriorityQueueFrom.poll();
			for (Integer index : adjList.get(Graph.currentNodeIndexFromThread)) {
				if (Graph.isVisitedFrom[index] != true) {
//					Graph.isVisitedFrom[index] = false;
					costFrom = Graph.disFromThread[Graph.currentNodeIndexFromThread]
							+ costList[Graph.currentNodeIndexFromThread][index];
					if (costFrom < Graph.disFromThread[index]) {
						Graph.disFromThread[index] = costFrom;
						Graph.preFromThread[index] = Graph.currentNodeIndexFromThread;
						if (HeuristicFactor != 0) {
							costFrom += MyMath.Width(this.vertices.get(index).getPositionX(),
									this.vertices.get(index).getPositionY(),
									this.vertices.get(vEnd).getPositionX(),
									this.vertices.get(vEnd).getPositionY());
						}
						myPriorityQueueFrom.add(new Pair<Integer, Integer>(index, costFrom));
					}
				}
			}

		}
	}

}
