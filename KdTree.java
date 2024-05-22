/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdDraw;

import java.awt.Color;
import java.util.Comparator;

public class KdTree {
    private static final Comparator<Point2D> BY_X = new Xcomparison();
    private static final Comparator<Point2D> BY_Y = new Ycomparison();
    private static final boolean USE_X = true;
    private static final boolean USE_Y = false;

    private class Node {
        public Point2D point;
        public Node left;
        public Node right;
        public int count;
        public RectHV value;
        public boolean flag;

        Node(Point2D point, boolean flag) {
            this.point = point;
            this.left = null;
            this.right = null;
            this.flag = flag;
            this.count = 1;
            this.value = null;
        }
    }

    private static class Xcomparison implements Comparator<Point2D> {
        public int compare(Point2D o1, Point2D o2) {
            return (int) (o1.x() - o2.x());
        }
    }

    private static class Ycomparison implements Comparator<Point2D> {
        public int compare(Point2D o1, Point2D o2) {
            return (int) (o1.y() - o2.y());
        }
    }

    private Node root;

    public KdTree() {
        root = null;
    }                               // construct an empty set of points

    public boolean isEmpty() {
        return root == null;
    }                      // is the set empty?

    public int size() {
        return size(root);
    } // number of points in the set

    private int size(Node n) {
        if (n == null) {
            return 0;
        }
        return n.count;
    }

    public void insert(Point2D p) {
        root = insert(p, root);
    }// add the point to the set (if it is not already in the set)

    private Node insert(Point2D p, Node n) {
        if (root == null) {
            Node tem = new Node(p, USE_X);
            tem.value = new RectHV(0, 0, 1, 1);
            return tem;
        }
        if (n == null) {
            return new Node(p, USE_X);
        }
        if (USE_X) {
            int cmp = BY_X.compare(p, n.point);
            if (cmp < 0) {
                n.left = insert(p, n.left);
                if (n.left.value == null) {
                    n.left.value = new RectHV(n.value.xmin(), n.value.ymin(), n.point.x(),
                                              n.value.ymax());
                    n.left.flag = USE_Y;
                }

            }
            else {
                n.right = insert(p, n.right);
                if (n.right.value == null) {
                    n.right.value = new RectHV(n.point.x(), n.value.ymin(), n.value.xmax(),
                                               n.value.ymax());
                    n.right.flag = USE_Y;
                }
            }

        }
        else {
            int cmp = BY_Y.compare(p, n.point);
            if (cmp < 0) {
                n.left = insert(p, n.left);
                if (n.left.value == null) {
                    n.left.value = new RectHV(n.value.xmin(), n.value.ymin(), n.value.xmax(),
                                              n.point.y());
                }
            }
            else {
                n.right = insert(p, n.right);
                if (n.right.value == null) {
                    n.right.value = new RectHV(n.value.xmin(), n.point.y(), n.value.xmax(),
                                               n.value.ymax());
                }
            }
        }
        n.count = size(n.right) + size(n.left) + 1;
        return n;
    }

    public boolean contains(Point2D p) {
        Node tem = root;
        boolean flag = false;
        while (tem != null) {
            if (tem.point.equals(p)) {
                flag = true;
                break;
            }
            if (tem.flag == USE_X) {
                int cmp = BY_X.compare(p, tem.point);
                if (cmp < 0) {
                    tem = tem.left;
                }
                else {
                    tem = tem.right;
                }
            }
            else {
                int cmp = BY_Y.compare(p, tem.point);
                if (cmp < 0) {
                    tem = tem.left;
                }
                else {
                    tem = tem.right;
                }
            }
        }
        return flag;
    }            // does the set contain point p?

    public void draw() {

    }                       // draw all points to standard draw

    private void draw(Node tem) {
        if (tem == null) {
            return;
        }
        if (tem.flag == USE_X) {
            StdDraw.setPenColor(Color.red);
            StdDraw.line(tem.point.x(), tem.value.ymin(), tem.point.x(), tem.value.ymax());
        }
        else {
            StdDraw.setPenColor(Color.BLUE);
            StdDraw.line(tem.value.xmin(), tem.point.y(), tem.value.xmax(), tem.point.y());
        }
        draw(tem.left);
        draw(tem.right);
    }

    public Iterable<Point2D> range(RectHV rect) {
        Stack<Point2D> container = new Stack<Point2D>();
        return range(rect, container, root);
    }         // all points that are inside the rectangle (or on the boundary)

    private Stack<Point2D> range(RectHV rect, Stack<Point2D> container, Node n) {
        if (n == null) {
            return container;
        }
        if (rect.contains(n.point)) {
            container.push(n.point);
            container = range(rect, container, n.left);
            container = range(rect, container, n.right);
        }
        else {
            if (n.left.value.intersects(rect)) {
                container = range(rect, container, n.left);
            }
            if (n.right.value.intersects(rect)) {
                container = range(rect, container, n.right);
            }
        }
        return container;
    }

    public Point2D nearest(Point2D p) {
        return nearest(p, root, root.point);
    }             // a nearest neighbor in the set to point p; null if the set is empty

    private Point2D nearest(Point2D query, Node n, Point2D best) {
        if (n == null) {
            return best;
        }
        if (query.distanceTo(best) > query.distanceTo(n.point)) {
            best = n.point;
        }
        int cmp;
        if (n.flag == USE_X) {
            cmp = BY_X.compare(query, n.point);
        }
        else {
            cmp = BY_Y.compare(query, n.point);
        }
        if (cmp < 0) {
            if (n.left.value.distanceTo(query) < query.distanceTo(best)) {
                best = nearest(query, n.left, best);
            }
        }
        else {
            if (n.right.value.distanceTo(query) < query.distanceTo(best)) {
                best = nearest(query, n.right, best);
            }
        }
        return best;
    }

    public static void main(String[] args) {
        KdTree test = new KdTree();
        test.insert(new Point2D(0.7, 0.2));
        test.insert(new Point2D(0.5, 0.4));
        test.insert(new Point2D(0.2, 0.3));
        test.insert(new Point2D(0.4, 0.7));
        test.insert(new Point2D(0.9, 0.6));
        test.draw();
    }                  // unit testing of the methods (optional)
}