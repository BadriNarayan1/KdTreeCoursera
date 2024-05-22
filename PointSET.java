/* *****************************************************************************
 *  Name: Badri Narayan
 *  Date: 20th may
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.Stack;

import java.util.TreeSet;

public class PointSET {
    private TreeSet<Point2D> pointSet;

    public PointSET() {
        pointSet = new TreeSet<Point2D>();
    }                               // construct an empty set of points

    public boolean isEmpty() {
        return pointSet.isEmpty();
    }                      // is the set empty?

    public int size() {
        return pointSet.size();
    }                         // number of points in the set

    public void insert(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        pointSet.add(p);
    }              // add the point to the set (if it is not already in the set)

    public boolean contains(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException();
        }
        return pointSet.contains(p);
    }            // does the set contain point p?

    public void draw() {
        for (Point2D p : pointSet) {
            p.draw();
        }
    }                         // draw all points to standard draw

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) {
            throw new IllegalArgumentException();
        }
        if (isEmpty()) {
            return null;
        }
        Stack<Point2D> stack = new Stack<Point2D>();
        for (Point2D p : pointSet) {
            if (rect.contains(p)) {
                stack.push(p);
            }
        }
        return stack;
    }             // all points that are inside the rectangle (or on the boundary)

    public Point2D nearest(Point2D p) {
        if (isEmpty()) {
            return null;
        }
        double min = Double.POSITIVE_INFINITY;
        Point2D nearestNeighbor = null;
        for (Point2D point : pointSet) {
            if (!p.equals(point)) {
                if (p.distanceTo(point) < min) {
                    min = p.distanceTo(point);
                    nearestNeighbor = point;
                }
            }
        }
        return nearestNeighbor;
    }             // a nearest neighbor in the set to point p; null if the set is empty

    public static void main(String[] args) {

    }                  // unit testing of the methods (optional)
}
