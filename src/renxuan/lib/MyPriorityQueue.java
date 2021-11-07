package renxuan.lib;


import java.util.AbstractQueue;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.SortedSet;
import java.util.Spliterator;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.function.Consumer;
import java.util.function.Predicate;



@SuppressWarnings("unchecked")
public  class   MyPriorityQueue<E> extends PriorityBlockingQueue{
	/**
	 * 复用官方的工具类，写成这样继承的原因是为了方便更改当前使用的优先队列，不用去更改其他文件
	 * 
	 * @param initialCapacity
	 * @param comparator
	 */
	public  MyPriorityQueue(int initialCapacity,Comparator<E> comparator) {
		super(initialCapacity,comparator);
	}
}