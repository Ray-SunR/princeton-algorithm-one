import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;

/**
 * Created by Renchen on 2016-11-12.
 */
public class RandomizedQueue<Item> implements Iterable<Item>
{
	private Item[] mData = null;
	private int mCapacity = 0;
	private int mSize = 0;

	private class RandomizedQueueIterator implements Iterator<Item>
	{
		private Item[] mSavedData = (Item[]) new Object[mSize];
		private int mIterPos = 0;

		private void swap(int pos1, int pos2)
		{
			Item tmp = mSavedData[pos1];
			mSavedData[pos1] = mSavedData[pos2];
			mSavedData[pos2] = tmp;
		}

		public RandomizedQueueIterator()
		{
			int count = 0;
			for (int i = 0; i < mData.length; i++)
			{
				if (count == mSize) { return; }
				if (mData[i] != null)
				{
					mSavedData[count++] = mData[i];
					int pos = StdRandom.uniform(0, count);
					swap(count - 1, pos);
				}
			}
		}

		public boolean hasNext()
		{
			return mIterPos < mSavedData.length;
		}

		public Item next()
		{
			if (!hasNext()) { throw new java.util.NoSuchElementException(); }
			return mSavedData[mIterPos++];
		}
	}

	public RandomizedQueue()                 // construct an empty randomized queue
	{
		mData = (Item[])new Object[1];
		mCapacity = 1;
	}

	public boolean isEmpty()                 // is the queue empty?
	{
		return mSize == 0;
	}

	public int size()                        // return the number of items on the queue
	{
		return mSize;
	}

	private void resize(int size)
	{
		Item[] newdata = (Item[])new Object[size];
		int count = 0;
		for (int i = 0;i < mData.length; i++)
		{
			if (mData[i] != null)
			{
				newdata[count++] = mData[i];
			}
		}

		mData = newdata;
	}

	private void swap(int pos1, int pos2)
	{
		Item tmp = mData[pos1];
		mData[pos1] = mData[pos2];
		mData[pos2] = tmp;
	}

	public void enqueue(Item item)           // add the item
	{
		if (item == null) { throw new java.lang.NullPointerException(); }
		if (mSize == mCapacity)
		{
			mCapacity *= 2;
			resize(mCapacity);
		}

		mData[mSize++] = item;
		int pos = StdRandom.uniform(0, mSize);
		swap(pos, mSize - 1);
	}

	public Item dequeue()                    // remove and return a random item
	{
		if (mSize == 0) { throw new java.util.NoSuchElementException(); }

		Item ret = mData[mSize - 1];
		mData[mSize - 1] = null;
		mSize--;
		if (mSize <= mCapacity / 4)
		{
			mCapacity = mCapacity >= 2 ? mCapacity /= 2 : mCapacity;
			resize(mCapacity);
		}
		return ret;
	}

	public Item sample()                     // return (but do not remove) a random item
	{
		if (isEmpty()) { throw new java.util.NoSuchElementException(); }
		int pos = StdRandom.uniform(0, mSize);
		while (mData[pos] == null)
		{
			pos = StdRandom.uniform(0, mSize);
		}

		return mData[pos];
	}

	public Iterator<Item> iterator()         // return an independent iterator over items in random order
	{
		return new RandomizedQueueIterator();
	}

	public static void main(String[] args)   // unit testing
	{
		RandomizedQueue<Integer> queue = new RandomizedQueue<Integer>();
		System.out.println(queue.isEmpty());
		queue.enqueue(1);
		System.out.println(queue.isEmpty());
		queue.enqueue(5);
		queue.enqueue(6);
		queue.enqueue(7);
		queue.enqueue(8);
		queue.enqueue(9);
		System.out.println(queue.dequeue() + " dequeued");
		System.out.println(queue.dequeue() + " dequeued");
		System.out.println(queue.dequeue() + " dequeued");

		queue.enqueue(10);
		queue.enqueue(11);
		System.out.println(queue.dequeue() + " dequeued");
		queue.enqueue(11);
		System.out.println(queue.dequeue() + " dequeued");
		queue.enqueue(11);
		System.out.println(queue.dequeue() + " dequeued");
		queue.enqueue(11);
		System.out.println(queue.dequeue() + " dequeued");

		System.out.println("First iterator");
		Iterator<Integer> it = queue.iterator();
		while (it.hasNext())
		{
			System.out.println(it.next());
		}

		System.out.println("Second iterator");
		Iterator<Integer> second = queue.iterator();
		while (second.hasNext())
		{
			System.out.println(second.next());
		}

		/*
		RandomizedQueue<Integer> queue = new RandomizedQueue<>();
		for (int i = 1; i <= 128; i++)
		{
			queue.enqueue(i);
		}

		queue.enqueue(129);
		for (int i = 0;i < 63; i++)
		{
			queue.dequeue();
		}
		queue.dequeue();
		*/
	}
}
