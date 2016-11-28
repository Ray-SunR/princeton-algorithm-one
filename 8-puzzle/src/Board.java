import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Renchen on 2016-11-26.
 */
public class Board {
	private int[][] mBoard;
	private int mDimension;

	private class BoardNeighborIterator implements Iterable<Board>
	{
		private ArrayList<Board> mNeighbors;

		private int[][] cloneBoard()
		{
			int[][] board = new int[mDimension][mDimension];
			for (int i = 0; i < mDimension; i++)
			{
				board[i] = mBoard[i].clone();
			}
			return board;
		}

		public BoardNeighborIterator()
		{
			mNeighbors = new ArrayList<>();
			for (int i = 0; i < mDimension; i++)
			{
				for (int j = 0; j < mDimension; j++)
				{
					if (mBoard[i][j] == 0)
					{
						// left
						if (j - 1 >= 0)
						{
							int[][] clone = cloneBoard();
							int tmp = clone[i][j - 1];
							clone[i][j - 1] = 0;
							clone[i][j] = tmp;
							mNeighbors.add(new Board(clone));
						}

						// right
						if (j + 1 < mDimension)
						{
							int[][] clone = cloneBoard();
							int tmp = clone[i][j + 1];
							clone[i][j + 1] = 0;
							clone[i][j] = tmp;
							mNeighbors.add(new Board(clone));
						}

						// top
						if (i - 1 >= 0)
						{
							int[][] clone = cloneBoard();
							int tmp = clone[i - 1][j];
							clone[i - 1][j] = 0;
							clone[i][j] = tmp;
							mNeighbors.add(new Board(clone));
						}

						// bottom
						if (i + 1 < mDimension)
						{
							int[][] clone = cloneBoard();
							int tmp = clone[i + 1][j];
							clone[i + 1][j] = 0;
							clone[i][j] = tmp;
							mNeighbors.add(new Board(clone));
						}
						return;
					}
				}
			}
		}

		public Iterator<Board> iterator()
		{
			return mNeighbors.iterator();
		}
	}

	public Board(int[][] blocks)           // construct a board from an n-by-n array of blocks
	{
		mDimension = blocks.length;
		mBoard = new int[mDimension][mDimension];
		for (int i = 0; i < mDimension; i++)
		{
			mBoard[i] = blocks[i].clone();
		}
	}

	// (where blocks[i][j] = block in row i, column j)
	public int dimension()                 // board dimension n
	{
		return mDimension;
	}

	public int hamming()                   // number of blocks out of place
	{
		int ret = 0;
		for (int i = 0; i < mDimension; i++)
		{
			for (int j = 0;j < mDimension; j++)
			{
				if (mBoard[i][j] == 0) { continue; }
				if (mBoard[i][j] != i * mDimension + j + 1)
				{
					ret++;
				}
			}
		}
		return ret;
	}

	public int manhattan()                 // sum of Manhattan distances between blocks and goal
	{
		int ret = 0;
		for (int i = 0;i < mDimension; i++)
		{
			for (int j = 0;j < mDimension; j++)
			{
				if (mBoard[i][j] == 0) { continue; }
				int target_x = (mBoard[i][j] - 1) / mDimension;
				int target_y = (mBoard[i][j] - 1) % mDimension;
				ret += Math.abs(target_x - i) + Math.abs(target_y - j);
			}
		}
		return ret;
	}

	public boolean isGoal()                // is this board the goal board?
	{
		for (int i = 0;i < mDimension; i++)
		{
			for (int j = 0; j < mDimension; j++)
			{
				if (mBoard[i][j] == 0) { continue; }
				if (mBoard[i][j] != i * mDimension + j + 1)
				{
					return false;
				}
			}
		}
		return true;
	}

	public Board twin()                    // a board that is obtained by exchanging any pair of blocks
	{
		int pos1_x = -1; int pos1_y = -1;
		int pos2_x = -1; int pos2_y = -1;
		for (int i = 0; i < mDimension; i++)
		{
			for (int j = 0; j < mDimension; j++)
			{
				if (mBoard[i][j] == 0) { continue; }
				if (pos1_x == -1) { pos1_x = i; pos1_y = j; }
				else if (pos2_x == -1) {  pos2_x = i; pos2_y = j; }
				else
				{
					// Swap
					int tmp = mBoard[pos1_x][pos1_y];
					mBoard[pos1_x][pos1_y] = mBoard[pos2_x][pos2_y];
					mBoard[pos2_x][pos2_y] = tmp;

					Board ret = new Board(mBoard);

					// Swap back
					tmp = mBoard[pos1_x][pos1_y];
					mBoard[pos1_x][pos1_y] = mBoard[pos2_x][pos2_y];
					mBoard[pos2_x][pos2_y] = tmp;
					return ret;
				}
			}
		}

		// Unexpected
		assert false;
		return null;
	}

	public boolean equals(Object y)        // does this board equal y?
	{
		if (y == null) { return false; }
		return toString().equals(y.toString());
	}

	public Iterable<Board> neighbors()     // all neighboring boards
	{
		return new BoardNeighborIterator();
	}

	public String toString()               // string representation of this board (in the output format specified below)
	{
		StringBuilder builder = new StringBuilder();
		builder.append(mDimension);
		builder.append('\n');
		for (int i = 0; i < mDimension; i++)
		{
			for (int j = 0; j < mDimension; j++)
			{
				builder.append(mBoard[i][j]);
				builder.append(' ');
			}
			builder.append('\n');
		}
		return builder.toString();
	}

	public static void main(String[] args) // unit tests (not graded)
	{
		int[][] data = {{1, 2, 3}, {4, 0, 6}, {7, 8, 5}};
		Board board = new Board(data);
		System.out.println(board.toString());
		System.out.println("harming: " + board.hamming());
		System.out.println("mahattan: " + board.manhattan());
		System.out.println("twin: " + board.twin());
		System.out.println("is goal: " + board.isGoal());
		Iterable<Board> it = board.neighbors();
		System.out.println("neighbors");
		for (Board item : it)
		{
			System.out.println(item);
			System.out.println(item.hamming());
			System.out.println(item.manhattan());
		}
	}
}

