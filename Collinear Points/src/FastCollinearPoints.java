import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class FastCollinearPoints {

	private ArrayList<LineSegment> mResults;
	private Point[] mPoints;
	private HashMap<Double, ArrayList<Point>> mMap; //map between slope and end points

	public FastCollinearPoints(Point[] points)     // finds all line segments containing 4 or more points
	{
		mResults = new ArrayList<>();
		mMap = new HashMap<>();
		if (points.length <= 1) { return; }
		mPoints = points.clone();
		// Sort based on natual order
		Arrays.sort(mPoints);

		if (mPoints.length == 2)
		{
			if (mPoints[0].compareTo(mPoints[1]) == 0) { throw new java.lang.IllegalArgumentException(); }
		}
		else if (mPoints.length == 3)
		{
			if (mPoints[0].compareTo(mPoints[1]) == 0 || mPoints[0].compareTo(mPoints[2]) == 0 ||
					mPoints[1].compareTo(mPoints[2]) == 0) { throw new java.lang.IllegalArgumentException(); }
		}

		int i = 0;
		for (; i < mPoints.length - 3; i++)
		{
			Point base = mPoints[i];
			Point[] sub = Arrays.copyOfRange(mPoints, i + 1, mPoints.length);
			Arrays.sort(sub, base.slopeOrder());
			int start = 0;

			while (start < sub.length)
			{
				double slope = sub[start].slopeTo(base);
				if (slope == Double.NEGATIVE_INFINITY)
				{
					throw new java.lang.IllegalArgumentException();
				}

				int count = 0;
				while (start < sub.length && sub[start].slopeTo(base) == slope)
				{
					start++;
					count++;
				}

				if (start < sub.length && sub[start].slopeTo(base) == Double.NEGATIVE_INFINITY)
				{
					throw new java.lang.IllegalArgumentException();
				}

				if (count >= 3)
				{
					ArrayList<Point> ret = mMap.get(slope);
					if (ret == null || !ret.contains(sub[start - 1]))
					{
						mResults.add(new LineSegment(base, sub[start - 1]));
						if (ret == null)
						{
							ArrayList<Point> newitem = new ArrayList<>();
							newitem.add(sub[start - 1]);
							mMap.put(slope, newitem);
						}
						else
						{
							ret.add(sub[start - 1]);
						}
					}
				}
			}
		}

		if (mPoints.length >= 3 && i >= mPoints.length - 3)
		{
			if (mPoints[i].compareTo(mPoints[i + 1]) == 0 || mPoints[i].compareTo(mPoints[i + 2]) == 0 || mPoints[i + 1].compareTo(mPoints[i + 2]) == 0)
			{
				throw new java.lang.IllegalArgumentException();
			}
		}
	}

	public int numberOfSegments()        // the number of line segments
	{
		return mResults.size();
	}

	public LineSegment[] segments()                // the line segments
	{
		return mResults.toArray(new LineSegment[mResults.size()]);
	}
}