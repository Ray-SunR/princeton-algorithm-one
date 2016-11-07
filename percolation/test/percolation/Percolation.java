package test.percolation;


import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation
{
	private boolean[] mGrid;
	private WeightedQuickUnionUF mUnionFind;
	private WeightedQuickUnionUF mUnionFindVirtual;
	private int n;
	public Percolation(int n)                // create n-by-n grid, with all sites blocked
	{
		if (n <= 0) { throw new java.lang.IllegalArgumentException(); }
		mUnionFind = new WeightedQuickUnionUF(n * n + 2);
		mUnionFindVirtual = new WeightedQuickUnionUF(n * n + 1);// This one only has top virtual node and others
		this.n = n;

		mGrid = new boolean[n * n + 2];
		mGrid[0] = true; mGrid[n * n + 1] = true;
		for (int i = 1; i <= n * n; i++)
		{
			if (i >= 1 && i <= n)
			{
				mUnionFind.union(i, 0);
				mUnionFindVirtual.union(i, 0);
			}

			mGrid[i] = false;
		}
	}

	public void open(int row, int col)       // open site (row, col) if it is not open already
	{
		if (row <= 0 || row > n || col <= 0 || col > n) { throw new java.lang.IndexOutOfBoundsException(); }

		int index = index(row, col);
		if (isOpenImpl(index)) { return; } // already open

		mGrid[index] = true; // mark it open

		int[] neighbors = neighbors(row, col);
		for (int i = 0;i < 4; i++)
		{
			if (neighbors[i] == -1 || !isOpenImpl(neighbors[i])) { continue; }
			mUnionFind.union(index, neighbors[i]);
			mUnionFindVirtual.union(index, neighbors[i]);
		}

		if (row == n)
		{
			mUnionFind.union(index, n * n + 1);
		}
	}

	// return neighbors in left top right bottom order
	private int[] neighbors(int row, int col)
	{
		int[] ret = new int[4];
		ret[0] = index(row, col - 1); // left
		ret[1] = index(row - 1, col); // top
		ret[2] = index(row, col + 1); // right
		ret[3] = index(row + 1, col); // bottom
		return ret;
	}

	public boolean isOpen(int row, int col)  // is site (row, col) open?
	{
		if (row <= 0 || row > n || col <= 0 || col > n) { throw new java.lang.IndexOutOfBoundsException(); }
		return isOpenImpl(index(row, col));
	}

	private boolean isOpenImpl(int index)
	{
		return mGrid[index];
	}

	//A full site is an open site that can be connected to an open site in the top row via a chain of neighboring
	// (left, right, up, down) open sites
	public boolean isFull(int row, int col)  // is site (row, col) full?
	{
		if (row <= 0 || row > n || col <= 0 || col > n) { throw new java.lang.IndexOutOfBoundsException(); }
		int[] neighbors = neighbors(row, col);
		for (int i = 0; i < neighbors.length; i++)
		{
			if (isOpenImpl(index(row, col)) && isFullImpl(neighbors[i]))
			{
				return true;
			}
		}
		return false;
	}

	private boolean isFullImpl(int index)
	{
		return index != -1 && isOpenImpl(index) && mUnionFindVirtual.find(index) == mUnionFindVirtual.find(0);
	}

	public boolean percolates()              // does the system percolate?
	{
		return  mUnionFind.find(0) == mUnionFind.find(n * n + 1);
	}

	/*
	x x x x x
	x x x x x
	x x X x x
	x x x x x
	x x x x x
	(3, 3) -> (3 - 1) * 5 + 3 = 13;
	(1, 1) -> (1 - 1) * 5 + 1 = 1;
	 */
	private int index(int row, int col)
	{
		if (row < 0 || col <= 0 || row >= n + 1|| col >= n + 1) { return -1; }
		// row 0 indicate the top virtual node
		if (row == 0) { return 0;}
		return (row - 1) * n + col;
	}

	/* These are my own impl
	private void union(int index1, int index2)
	{
		if (index1 < 0 || index2 < 0 || index1 > n * n + 1 || index2 > n * n + 1) { return; }
		int root1 = root(index1);
		int root2 = root(index2);
		int size1 =	mSize[root1];
		int size2 = mSize[root2];
		if (size1 < size2)
		{
			mGrid[root1] = mGrid[root2];
			mSize[root2] += mSize[root1];
		}
		else
		{
			mGrid[root2] = mGrid[root1];
			mSize[root1] += mSize[root2];
		}
	}

	// equavalent to find
	private int root(int index)
	{
		while (index != mGrid[index])
		{
			mGrid[index] = mGrid[mGrid[index]];
			index = mGrid[index];
		}
		return index;
	}
	 */

	public static void main(String[] args)   // test client (optional)
	{

	}
}