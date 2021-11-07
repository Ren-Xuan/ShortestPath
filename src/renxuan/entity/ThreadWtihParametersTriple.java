package renxuan.entity;

import renxuan.tools.Pair;

public class ThreadWtihParametersTriple extends Thread {//加入到优先队列的线程对象
	int myPriorityQueueArrIndex;
	int cost;
	int next;

	public ThreadWtihParametersTriple(int myPriorityQueueArrIndex, int next, int cost) {
		this.myPriorityQueueArrIndex = myPriorityQueueArrIndex;
		this.cost = cost;
		this.next = next;
	}

	public int getMyPriorityQueueArrIndex() {
		return myPriorityQueueArrIndex;
	}

	public int getCost() {
		return cost;
	}

	public int getNext() {
		return next;
	}

	public void setMyPriorityQueueArrIndex(int myPriorityQueueArrIndex) {
		this.myPriorityQueueArrIndex = myPriorityQueueArrIndex;
	}

	public void setCost(int cost) {
		this.cost = cost;
	}

	public void setNext(int next) {
		this.next = next;
	}

	public void run() {
		Graph.myPriorityQueueArr[myPriorityQueueArrIndex].add(new Pair<Integer, Integer>(next, cost));
	}

}
