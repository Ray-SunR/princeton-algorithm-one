import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import java.util.ArrayList;

/**
 * Created by Renchen on 2016-12-03.
 */
public class KdTree
{
	private class Node
	{
		public Node(Point2D key)
		{
			mKey = key;
			mLeft = null;
			mRight = null;
			mRect = null;
			mVertical = true;
		}

		public Point2D mKey;
		public Node mLeft;
		public Node mRight;
		public RectHV mRect;
		// Indicate the splitting line is horizontal or vertical
		// vertical -> based on x coordinate, otherwise based on y coordinate
		public boolean mVertical;
	}

	private Node mRoot;
	private int mSize;

	public KdTree()                               // construct an empty set of points
	{
		mRoot = null;
		mSize = 0;
	}

	public boolean isEmpty()                      // is the set empty?
	{
		return mRoot == null;
	}

	public int size()                         // number of points in the set
	{
		return mSize;
	}

	private Node insertHelper(Node root, Node toinsert, boolean vertical, double xmin, double ymin, double xmax, double ymax)
	{
		if (root == null)
		{
			root = toinsert;
			root.mVertical = vertical;
			root.mRect = new RectHV(xmin, ymin, xmax, ymax);
			return root;
		}
		else
		{
			if (root.mVertical)
			{
				if (root.mKey.x() > toinsert.mKey.x()) // go left
				{
					root.mLeft = insertHelper(root.mLeft, toinsert, false, root.mRect.xmin(), root.mRect.ymin(), root.mKey.x(), root.mRect.ymax());
				}
				else // go right
				{
					root.mRight = insertHelper(root.mRight, toinsert, false, root.mKey.x(), root.mRect.ymin(), root.mRect.xmax(), root.mRect.ymax());
				}
			}
			else
			{
				if (root.mKey.y() > toinsert.mKey.y()) // go left
				{
					root.mLeft = insertHelper(root.mLeft, toinsert, true, root.mRect.xmin(), root.mRect.ymin(), root.mRect.xmax(), root.mKey.y());
				}
				else // go right
				{
					root.mRight = insertHelper(root.mRight, toinsert, true, root.mRect.xmin(), root.mKey.y(), root.mRect.xmax(), root.mRect.ymax());
				}
			}
		}
		return root;
	}

	public void insert(Point2D p)              // add the point to the set (if it is not already in the set)
	{
		if (contains(p)) { return; }
		mSize++;
		Node newnode = new Node(p);
		mRoot = insertHelper(mRoot, newnode, true, 0, 0, 1, 1);
	}

	private boolean containsHelper(Node root, Point2D p)
	{
		if (root == null) { return false; }
		int comresult = root.mKey.compareTo(p);
		if (comresult == 0) { return true; }
		else if (root.mVertical)
		{
			if (root.mKey.x() > p.x()) // go left
			{
				return containsHelper(root.mLeft, p);
			}
			else // go right
			{
				return containsHelper(root.mRight, p);
			}
		}
		else
		{
			if (root.mKey.y() > p.y()) // go left
			{
				return containsHelper(root.mLeft, p);
			}
			else // go right
			{
				return containsHelper(root.mRight, p);
			}
		}
	}

	public boolean contains(Point2D p)            // does the set contain point p?
	{
		if (p == null) { throw new java.lang.NullPointerException(); }
		return containsHelper(mRoot, p);
	}

	private void drawHelper(Node node)
	{
		if (node == null) { return; }
		StdDraw.setPenColor(StdDraw.BLACK);
		if (node.mVertical)
		{
			StdDraw.setPenColor(StdDraw.RED);
			StdDraw.setPenRadius(0.001);
			StdDraw.line(node.mKey.x(), node.mRect.ymin(), node.mKey.x(), node.mRect.ymax());
		}
		else
		{
			StdDraw.setPenColor(StdDraw.BLUE);
			StdDraw.setPenRadius(0.001);
			StdDraw.line(node.mRect.xmin(), node.mKey.y(), node.mRect.xmax(), node.mKey.y());
		}
		StdDraw.filledCircle(node.mKey.x(), node.mKey.y(), 0.01);
		drawHelper(node.mLeft);
		drawHelper(node.mRight);
	}

	public void draw()                         // draw all points to standard draw
	{
		drawHelper(mRoot);
	}

	private void rangeHelper(RectHV rect, Node node, ArrayList<Point2D> result)
	{
		if (node == null) { return; }
		// early out if it doesn't intersect
		if (!rect.intersects(node.mRect)) { return; }
		if (rect.contains(node.mKey))
		{
			result.add(node.mKey);
			rangeHelper(rect, node.mLeft, result);
			rangeHelper(rect, node.mRight, result);
		}
		else if (node.mVertical)
		{
			if (rect.xmax() < node.mKey.x())	// on the left of the point
			{
				rangeHelper(rect, node.mLeft, result);
			}
			else if (rect.xmin() > node.mKey.x())// on the right of the point (strictly)
			{
				rangeHelper(rect, node.mRight, result);
			}
			else
			{
				rangeHelper(rect, node.mLeft, result);
				rangeHelper(rect, node.mRight, result);
			}
		}
		else
		{
			if (rect.ymax() < node.mKey.y()) // on the left of the point (below)
			{
				rangeHelper(rect, node.mLeft, result);
			}
			else if (rect.ymin() > node.mKey.y()) // on the right of the point (above)
			{
				rangeHelper(rect, node.mRight, result);
			}
			else
			{
				rangeHelper(rect, node.mLeft, result);
				rangeHelper(rect, node.mRight, result);
			}
		}
	}

	public Iterable<Point2D> range(RectHV rect)             // all points that are inside the rectangle
	{
		ArrayList<Point2D> result = new ArrayList<>();
		rangeHelper(rect, mRoot, result);
		return result;
	}

	private Point2D nearestHelper(Node node, Point2D p, Point2D curret, double min_dis)
	{
		if (node == null) { return curret; }

		// no need to explore its subtrees if the distance between the rectangle defined by node and hte query point
		// is bigger than the nearest found point so far
		if (node.mRect.distanceSquaredTo(p) > min_dis) { return curret; }
		double dis = node.mKey.distanceSquaredTo(p);
		if (dis < min_dis)
		{
			curret = node.mKey;
			min_dis = dis;
		}
		if (node.mVertical)
		{
			if (node.mKey.x() > p.x()) // on the left of the vertical splitting line, go left first
			{
				Point2D leftret = nearestHelper(node.mLeft, p, curret, min_dis);
				return nearestHelper(node.mRight, p, leftret, p.distanceSquaredTo(leftret));
			}
			else // go right first
			{
				Point2D rightret = nearestHelper(node.mRight, p, curret, min_dis);
				return nearestHelper(node.mLeft, p, rightret, p.distanceSquaredTo(rightret));
			}
		}
		else
		{
			if (node.mKey.y() > p.y()) // on the left of the horizontal splitting line, go left first
			{
				Point2D leftret = nearestHelper(node.mLeft, p, curret, min_dis);
				return nearestHelper(node.mRight, p, leftret, p.distanceSquaredTo(leftret));
			}
			else // go right first
			{
				Point2D rightret = nearestHelper(node.mRight, p, curret, min_dis);
				return nearestHelper(node.mLeft, p, rightret, p.distanceSquaredTo(rightret));
			}
		}
	}

	public Point2D nearest(Point2D p)             // a nearest neighbor in the set to point p; null if the set is empty
	{
		if (p == null) { throw new java.lang.NullPointerException(); }
		return nearestHelper(mRoot, p, null, Double.MAX_VALUE);
	}
}
