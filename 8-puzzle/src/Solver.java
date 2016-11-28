import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.net.MulticastSocket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Created by Renchen on 2016-11-26.
 */
public class Solver {
	private class Node implements Comparable<Node>
	{
		public Board mCurrent;
		public Node mPrevious;
		public int mNumMoves;

		public int compareTo(Node other)
		{
			int weight = mCurrent.manhattan() + mNumMoves; //mCurrent.hamming() + mCurrent.manhattan() + mNumMoves + mNumMoves;
			int other_weight = other.mCurrent.manhattan() + other.mNumMoves;// other.mCurrent.hamming() + other.mCurrent.manhattan() + other.mNumMoves + other.mNumMoves;
			return weight - other_weight;
		}
	}

	private MinPQ<Node> mPQ;
	private MinPQ<Node> mHelperPQ; // for determining whether the board is solvable
	private Board mInitialBoard;
	public int mInsertCount;
	public int mMaxPQSize;

	public Solver(Board initial)           // find a solution to the initial board (using the A* algorithm)
	{
		if (initial == null) { throw new java.lang.NullPointerException(); }
		mInitialBoard = initial;
		mInsertCount = 0;
		mMaxPQSize = 0;
	}

	private void Init()
	{
		mPQ = new MinPQ<>();
		mHelperPQ = new MinPQ<>();

		Node node = new Node();
		node.mCurrent = mInitialBoard;
		node.mPrevious = null;
		node.mNumMoves = 0;
		mPQ.insert(node);

		Node othernode = new Node();
		othernode.mCurrent = mInitialBoard.twin();
		node.mPrevious = null;
		node.mNumMoves = 0;
		mHelperPQ.insert(othernode);
		mInsertCount += 2;
		mMaxPQSize = 2;
	}

	private void Reset()
	{
		mPQ = null;
		mHelperPQ = null;
	}

	public boolean isSolvable()            // is the initial board solvable?
	{
		Iterable<Board> solution = solution();
		if (solution != null)
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	public int moves()                     // min number of moves to solve initial board; -1 if unsolvable
	{
		Iterable<Board> solution = solution();
		if (solution != null)
		{
			int count = 0;
			for (Board item : solution)
			{
				count++;
			}
			Reset();
			return count - 1;
		}
		else
		{
			Reset();
			return -1;
		}
	}

	private void Helper(ArrayList<Board> result, Node node)
	{
		if (node == null) { return; }
		Helper(result, node.mPrevious);
		result.add(node.mCurrent);
	}

	public Iterable<Board> solution()      // sequence of boards in a shortest solution; null if unsolvable
	{
		Init();
		Node minNode = null;
		Node minNodeHelper = null;
		ArrayList<Board> solution = new ArrayList<>();
		while (!mPQ.isEmpty() && !mHelperPQ.isEmpty())
		{
			minNode = mPQ.delMin();
			minNodeHelper = mHelperPQ.delMin();
			if (minNode.mCurrent.isGoal())
			{
				Helper(solution, minNode);
				break;
			}

			if (minNodeHelper.mCurrent.isGoal())
			{
				break;
			}

			Iterable<Board> neighbors = minNode.mCurrent.neighbors();
			for (Board item : neighbors)
			{
				if (minNode.mPrevious != null && item.equals(minNode.mPrevious.mCurrent)) { continue; }
				Node toInsert = new Node();
				toInsert.mCurrent = item;
				toInsert.mPrevious = minNode;
				toInsert.mNumMoves = minNode.mNumMoves + 1;
				mPQ.insert(toInsert);
				mInsertCount++;
			}

			neighbors = minNodeHelper.mCurrent.neighbors();
			for (Board item : neighbors)
			{
				if (minNodeHelper.mPrevious != null && item.equals(minNodeHelper.mPrevious.mCurrent)) { continue; }

				Node toInsert = new Node();
				toInsert.mCurrent = item;
				toInsert.mPrevious = minNodeHelper;
				toInsert.mNumMoves = minNodeHelper.mNumMoves + 1;
				mHelperPQ.insert(toInsert);
				//mInsertCount++;
			}
			mMaxPQSize = mPQ.size() + mHelperPQ.size() > mMaxPQSize ? mPQ.size() + mHelperPQ.size() : mMaxPQSize;
		}

		if (minNode.mCurrent.isGoal())
		{
			Reset();
			return solution;
		}
		else if (minNodeHelper.mCurrent.isGoal())
		{
			Reset();
			return null;
		}

		assert false;
		return null;
	}

	public static void main(String[] args) // solve a slider puzzle (given below)
	{
		// create initial board from file
		In in = new In(args[0]);
		int n = in.readInt();
		int[][] blocks = new int[n][n];
		for (int i = 0; i < n; i++)
			for (int j = 0; j < n; j++)
				blocks[i][j] = in.readInt();
		Board initial = new Board(blocks);

		// solve the puzzle
		Solver solver = new Solver(initial);

		// print solution to standard output
		if (!solver.isSolvable())
			StdOut.println("No solution possible");
		else {
			StdOut.println("Minimum number of moves = " + solver.moves());
			for (Board board : solver.solution())
				StdOut.println(board);
		}
		System.out.println(solver.mInsertCount);
		System.out.println(solver.mMaxPQSize);
	}
}