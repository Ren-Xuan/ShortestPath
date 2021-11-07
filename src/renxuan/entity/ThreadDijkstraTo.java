package renxuan.entity;

import java.util.Comparator;
import java.util.List;

import renxuan.lib.MyPriorityQueue;
import renxuan.tools.MyMath;
import renxuan.tools.Pair;
import renxuan.tools.Vertice;

public class ThreadDijkstraTo extends Thread {//从尾部开始向头部方向执行的dijskra线程对象
	int vStart;
	int vEnd;
	MyPriorityQueue<Pair<Integer, Integer>> myPriorityQueueTo;
	Pair<Integer, Integer> nodePairTo;
	int preNodeIndexTo;
	int costTo;
	List<Vertice> vertices;
	List<List<Integer>> adjList;
	int[][] costList;
	double HeuristicFactor=0;
	public void setHeuristicFactor(double HeuristicFactor) {
		this.HeuristicFactor=HeuristicFactor;
	}
	public ThreadDijkstraTo(int vStart, int vEnd, int size, List<List<Integer>> adjList,List<Vertice> vertices ,int[][] costList) {
		super();
		this.vStart = vStart;
		this.vEnd = vEnd;
		this.adjList = adjList;
		this.vertices=vertices;
		this.costList = costList;
		costTo = Graph.MAXVALUE;
		Graph.disToThread = new int[size];
		Graph.preToThread = new int[size];
		Graph.isVisitedTo = new boolean[size];
		for (int i = 0; i < size; i++) {
			Graph.disToThread[i] = Graph.MAXVALUE;
			Graph.preToThread[i] = -1;
			Graph.isVisitedTo[i] = false;
		}
		preNodeIndexTo = vEnd;
		myPriorityQueueTo = new MyPriorityQueue<>(size,new Comparator<Pair<Integer, Integer>>() {
			@Override
			public int compare(Pair<Integer, Integer> o1, Pair<Integer, Integer> o2) {
				return o1.getValue() - o2.getValue();
			}
		});
		myPriorityQueueTo.add(new Pair<Integer, Integer>(vEnd, 0));
		Graph.disToThread[vEnd] = 0;
		Graph.preToThread[vEnd] = vEnd;
		Graph.isVisitedTo[vEnd] = true;
	}

	public void run() {
		while (myPriorityQueueTo.size() != 0) {
			nodePairTo = (Pair<Integer, Integer>) myPriorityQueueTo.peek();
			Graph.currentNodeIndexToThread = nodePairTo.getKey();
			synchronized (this) {
				if (Graph.isVisitedFrom[Graph.currentNodeIndexToThread] == true) {
					Graph.costTotalThreadTo = Graph.disToThread[Graph.currentNodeIndexToThread]
							+ Graph.disFromThread[Graph.currentNodeIndexToThread];
					break;
				}
			}
			Graph.isVisitedTo[Graph.currentNodeIndexToThread] = true;
			myPriorityQueueTo.poll();
			for (Integer index : adjList.get(Graph.currentNodeIndexToThread)) {
				if (Graph.isVisitedTo[index] != true) {
//					Graph.isVisitedTo[index] = false;
					costTo = Graph.disToThread[Graph.currentNodeIndexToThread]
							+ costList[Graph.currentNodeIndexToThread][index];
					if (costTo < Graph.disToThread[index]) {
						Graph.disToThread[index] = costTo;
						Graph.preToThread[index] = Graph.currentNodeIndexToThread;
						if (HeuristicFactor != 0) {
							costTo+= MyMath.Width(this.vertices.get(index).getPositionX(),
									this.vertices.get(index).getPositionY(),
									this.vertices.get(vStart).getPositionX(),
									this.vertices.get(vStart).getPositionY());
						}
						myPriorityQueueTo.add(new Pair<Integer, Integer>(index, costTo));
					}
				}
			}

		}
		//
	}

}
