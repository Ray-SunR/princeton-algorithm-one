import java.util.Iterator;

/**
 * Created by Renchen on 2016-11-12.
 */
public class Deque<Item> implements Iterable<Item>
{
	private class Node
	{
		Item mData = null;
		Node mNext = null;
		Node mPre = null;
	}

	private class DequeIterator implements Iterator<Item>
	{
		Node save = mFront;
		public boolean hasNext()
		{
			return save != null;
		}

		public Item next()
		{
			if (save == null)
			{
				throw new java.util.NoSuchElementException();
			}
			Item ret = save.mData;
			save = save.mNext;
			return ret;
		}
	}

	private Node mFront = null;
	private Node mTail = null;
	private int mSize = 0;

	public Deque()                           // construct an empty deque
	{

	}

	public boolean isEmpty()                 // is the deque empty?
	{
		return mSize == 0;
	}

	public int size()                        // return the number of items on the deque
	{
		return mSize;
	}

	public void addFirst(Item item)          // add the item to the front
	{
		if (item == null) { throw new java.lang.NullPointerException(); }
		Node newnode = new Node();
		newnode.mData = item;
		if (mFront == null) { mFront = newnode; mTail = newnode; }
		else
		{
			newnode.mNext = mFront;
			mFront.mPre = newnode;
			mFront = newnode;
		}
		mSize++;
	}

	public void addLast(Item item)           // add the item to the end
	{
		if (item == null) { throw new java.lang.NullPointerException(); }
		Node newnode = new Node();
		newnode.mData = item;
		if (mTail == null) { mTail = newnode; mFront = newnode; }
		else
		{
			mTail.mNext = newnode;
			newnode.mPre = mTail;
			mTail = newnode;
		}
		mSize++;
	}
	public Item removeFirst()                // remove and return the item from the front
	{
		if (mFront == null)
		{
			throw new java.util.NoSuchElementException();
		}

		Item ret = mFront.mData;
		Node next = mFront.mNext;

		if (next != null) { next.mPre = null; }

		mFront = next;

		if (mSize == 1) { mTail = null; }

		mSize--;
		return ret;
	}

	public Item removeLast()                 // remove and return the item from the end
	{
		if (mTail == null)
		{
			throw new java.util.NoSuchElementException();
		}

		Item ret = mTail.mData;
		Node pre = mTail.mPre;
		if (pre != null) { pre.mNext = null; }

		mTail = pre;

		if (mSize == 1) { mFront = null; }

		mSize--;
		return ret;
	}


	public Iterator<Item> iterator()         // return an iterator over items in order from front to end
	{
		return new DequeIterator();
	}

	private static void Print(Deque<Integer> queue)
	{
		Iterator<Integer> iterator = queue.iterator();
		while (iterator.hasNext())
		{
			System.out.println(iterator.next());
		}
	}

	public static void main(String[] args)
	{
		Deque<Integer> queue = new Deque<Integer>();
		System.out.println(queue.size());
		queue.addFirst(1);
		queue.addFirst(2);
		queue.addFirst(3);
		queue.addLast(4);
		queue.addLast(5);

		Print(queue);

		queue.addLast(6);
		queue.addLast(6);
		queue.addLast(6);

		queue.removeFirst();
		queue.removeLast();
		queue.removeFirst();
		queue.removeLast();
		queue.removeFirst();
		queue.removeLast();
		queue.removeFirst();
		queue.removeLast();
		//queue.removeLast();

		System.out.println(queue.size());
		Print(queue);

	}
}
