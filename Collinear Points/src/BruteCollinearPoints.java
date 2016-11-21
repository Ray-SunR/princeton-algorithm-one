import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Renchen on 2016-11-19.
 */
public class BruteCollinearPoints {

	private ArrayList<LineSegment> mResults;

	private Point minPoint(Point[] pts)
	{
		int minIndex = 0;
		for (int i = 0; i < pts.length; i++)
		{
			if (pts[minIndex].compareTo(pts[i]) > 0)
			{
				minIndex = i;
			}
		}
		return pts[minIndex];
	}

	private Point maxPoint(Point[] pts)
	{
		int maxIndex = 0;
		for (int i = 0;i < pts.length; i++)
		{
			if (pts[maxIndex].compareTo(pts[i]) < 0)
			{
				maxIndex = i;
			}
		}
		return pts[maxIndex];
	}

	public BruteCollinearPoints(Point[] points)    // finds all line segments containing 4 points
	{
		mResults = new ArrayList<>();
		for (int i = 0;i < points.length; i++)
		{
			Point p1 = points[i];
			for (int j = i + 1; j < points.length; j++)
			{
				Point p2 = points[j];
				if (p2.compareTo(p1) == 0) { throw new java.lang.IllegalArgumentException(); }
				for (int k = j + 1; k < points.length; k++)
				{
					Point p3 = points[k];
					if (p1.compareTo(p3) == 0 || p2.compareTo(p3) == 0) { throw new java.lang.IllegalArgumentException(); }
					for (int m = k + 1; m < points.length; m++)
					{
						Point p4 = points[m];
						if (p1.compareTo(p4) == 0 || p2.compareTo(p4) == 0 || p3.compareTo(p4) == 0)
						{ throw new java.lang.IllegalArgumentException(); }
						double s1 = p1.slopeTo(p2);
						double s2 = p1.slopeTo(p3);
						double s3 = p1.slopeTo(p4);

						if (s1 == s2 && s1 ==s3)
						{
							Point[] pts = new Point[4];
							pts[0] = p1; pts[1] = p2; pts[2] = p3; pts[3] = p4;
							Point min = minPoint(pts);
							Point max = maxPoint(pts);
							LineSegment seg = new LineSegment(min, max);
							mResults.add(seg);
						}
					}
				}
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