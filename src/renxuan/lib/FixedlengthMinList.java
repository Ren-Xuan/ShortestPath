package renxuan.lib;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.PriorityBlockingQueue;

import renxuan.tools.Pair;

public class FixedlengthMinList<T> {//定长链表
	private int capacity;
	private int maxCapacity;
	private List<T> list;
	private final Comparator<? super T> comparator;

	public FixedlengthMinList(int maxCapacity, Comparator<T> comparator) {
		list = new ArrayList();
		this.comparator = comparator;
		if (maxCapacity < 1)
			throw new IllegalArgumentException();
		this.maxCapacity = maxCapacity;
		this.capacity = 0;
	}

	public void insert(T e) {
		if (this.capacity == 0) {
			this.list.add(e);
			this.capacity++;
		} else if (this.capacity < maxCapacity) {
			int i = 0;
			for (i = 0; i < this.capacity; i++) {
				if (this.comparator.compare(e, this.list.get(i)) < 0) {
					this.list.add(i, e);
					break;
				} 
			}
			if(i==this.capacity) {//加到尾部
				this.list.add(e);
			}
			this.capacity++;

		} else {
			if (this.comparator.compare(e, this.list.get(capacity - 1)) > 0)
				return;
			int i = 0;
			for (i = 0; i < this.capacity; i++) {
				if (this.comparator.compare(e, this.list.get(i)) < 0) {
					this.list.add(i, e);
					break;
				}
			}
			if(i==this.capacity) {//加到尾部
				this.list.add(e);
			}
			this.list.remove(capacity);//多余的那个删除
		}

	}
	public List<T> getList() {
		return list;
	}

	public int size() {
		return capacity;
	}

	public int lenth() {
		return capacity;
	}

	public void clear() {
		this.capacity = 0;
		this.list.clear();
	}
	public T get(int index) {
		if(index<0||index>=this.capacity) {
			return null;
		}
		return this.list.get(index);
	}
	public String toString() {
		return list.toString();
	}

	public static void main(String[] args) {
		FixedlengthMinList<Pair<Integer, Integer>> list = new FixedlengthMinList<>(5,
				new Comparator<Pair<Integer, Integer>>() {

					@Override
					public int compare(Pair<Integer, Integer> o1, Pair<Integer, Integer> o2) {
						return o1.getValue() - o2.getValue();
					}
				});
		list.insert(new Pair<Integer, Integer>(1, 6));
		list.insert(new Pair<Integer, Integer>(1, 41));
		list.insert(new Pair<Integer, Integer>(3, 0));
		list.insert(new Pair<Integer, Integer>(4, 0));
		System.out.println(list);
		list.insert(new Pair<Integer, Integer>(5, 1));
		System.out.println(list);
		list.insert(new Pair<Integer, Integer>(6, 65));
		System.out.println(list);
		list.insert(new Pair<Integer, Integer>(7, 2));
		System.out.println(list);
		list.insert(new Pair<Integer, Integer>(8, 5));
		System.out.println(list);
		list.insert(new Pair<Integer, Integer>(8, 5));
		System.out.println(list);
	}

}
