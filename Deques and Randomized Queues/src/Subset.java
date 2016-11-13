import edu.princeton.cs.algs4.StdIn;

/**
 * Created by Renchen on 2016-11-13.
 */
public class Subset
{
	public static void main(String[] args)
	{
		int k = Integer.parseInt(args[0]);
		RandomizedQueue<String> queue = new RandomizedQueue<>();
		String str;
		while (!StdIn.isEmpty())
		{
			str = StdIn.readString();
			queue.enqueue(str);
		}

		while (k > 0)
		{
			System.out.println(queue.dequeue());
			k--;
		}
	}
}
