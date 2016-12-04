import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;

import java.util.ArrayList;

/**
 * Created by Renchen on 2016-12-03.
 */

public class PointSET {
	private SET<Point2D> mSet;

	public  PointSET()                               // construct an empty set of points
	{
		mSet = new SET<Point2D>();
	}

	public boolean isEmpty()                      // is the set empty?
	{
		return mSet.isEmpty();
	}

	public int size()                         // number of points in the set
	{
		return mSet.size();
	}

	public void insert(Point2D p)              // add the point to the set (if it is not already in the set)
	{
		if (p == null) { throw new java.lang.NullPointerException(); }
		mSet.add(p);
	}

	public boolean contains(Point2D p)            // does the set contain point p?
	{
		if (p == null) { throw new java.lang.NullPointerException(); }
		return mSet.contains(p);
	}

	public void draw()                         // draw all points to standard draw
	{
		for (Point2D point : mSet)
		{
			point.draw();
		}
	}

	public Iterable<Point2D> range(RectHV rect)             // all points that are inside the rectangle
	{
		if (rect == null) { throw new java.lang.NullPointerException(); }
		ArrayList<Point2D> result = new ArrayList<>();
		for (Point2D point : mSet)
		{
			if (rect.contains(point))
			{
				result.add(point);
			}
		}
		return result;
	}

	public Point2D nearest(Point2D p)             // a nearest neighbor in the set to point p; null if the set is empty
	{
		if (p == null) { throw new java.lang.NullPointerException(); }
		double min_dis = Double.MAX_VALUE;
		Point2D ret = null;
		for (Point2D point : mSet)
		{
			double dis = p.distanceSquaredTo(point);
			if (dis < min_dis)
			{
				min_dis = dis;
				ret = point;
			}
		}
		return ret;
	}

	public static void main(String[] args)                  // unit testing of the methods (optional)
	{
		PointSET set = new PointSET();
		set.insert(new Point2D(0, 0.5));
		set.insert(new Point2D(0.5, 0));
		set.insert(new Point2D(1, 0.5));
		set.insert(new Point2D(0.5, 1));

		RectHV rect = new RectHV(0,0,0.5,1);
		Iterable<Point2D> iter = set.range(rect);
		for (Point2D point : iter)
		{
			System.out.println(point);
		}

		System.out.println(set.nearest(new Point2D(0, 0.25)));
	}
}
