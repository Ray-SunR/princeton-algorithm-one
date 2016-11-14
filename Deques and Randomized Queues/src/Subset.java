import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdRandom;

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
		int count = 0;
		while (!StdIn.isEmpty())
		{
			str = StdIn.readString();
			if (queue.size() == k && !queue.isEmpty())
			{
				int pos = StdRandom.uniform(0, count + 1);
				if (pos >= k)
				{
					count++;
					continue;
				}
				else
				{
					queue.dequeue();
					queue.enqueue(str);
				}
			}
			else
			{
				queue.enqueue(str);
			}
			count++;
		}

		while (k > 0)
		{
			System.out.println(queue.dequeue());
			k--;
		}
	}
}
