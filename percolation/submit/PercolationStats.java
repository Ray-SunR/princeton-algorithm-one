import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.StdRandom;


/**
 * Created by Renchen on 2016-11-05.
 */
public class PercolationStats
{
	private double[] mRets;
	public PercolationStats(int n, int trials)    // perform trials independent experiments on an n-by-n grid
	{
		if (n <= 0 || trials <= 0)
		{
			throw new java.lang.IllegalArgumentException();
		}

		mRets = new double[trials];
		for (int i = 0; i < trials; i++)
		{
			Percolation p = new Percolation(n);
			int count = 0;
			while (!p.percolates())
			{
				int row = StdRandom.uniform(1, n + 1);
				int col = StdRandom.uniform(1, n + 1);
				if (!p.isOpen(row, col))
				{
					p.open(row, col);
					count++;
				}
			}
			mRets[i] = (double)count / (n * n);
		}
	}

	public double mean()                          // sample mean of percolation threshold
	{
		return StdStats.mean(mRets);
	}

	public double stddev()                        // sample standard deviation of percolation threshold
	{
		return StdStats.stddev(mRets);
	}

	public double confidenceLo()                  // low  endpoint of 95% confidence interval
	{
		return mean() - 1.96 * stddev() / Math.sqrt(mRets.length);
	}

	public double confidenceHi()                  // high endpoint of 95% confidence interval
	{
		return mean() + 1.96 * stddev() / Math.sqrt(mRets.length);
	}

	public static void main(String[] args)    // test client (described below)
	{
		PercolationStats stats = new PercolationStats(400, 1000);
		System.out.println("mean = " + stats.mean());
		System.out.println("stddev = " + stats.stddev());
		System.out.println("95% confidence interval = " +  stats.confidenceLo() + ", " + stats.confidenceHi());
	}
}
