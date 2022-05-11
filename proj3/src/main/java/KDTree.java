import java.util.List;

public class KDTree {
    private static final boolean DividedByX = true;
    private static final boolean DividedByY = false;

    private class Node implements Comparable<Node>{
        boolean orientation;
        Point point;
        Node left; // also down side
        Node right; // also up side
        public Node (Point p, boolean orientation) {
            this.point = p;
            this.orientation = orientation;
        }
        @Override
        public int compareTo(Node o) {
            if (orientation == DividedByX) {
                return Double.compare(this.point.getX(), o.point.getX());
            } else {
                return Double.compare(this.point.getY(), o.point.getY());
            }
        }
    }
    private Node root;
    public KDTree() {
        root = null;
    }

    public KDTree(List<Point> points) {
        root = null;
        for (Point p : points) {
            insert(p);
        }
    }
    public void insert(Point p) {
        root = insertHelper(root, p, DividedByX);
    }
    private Node insertHelper(Node root, Point p, boolean orientation) {
        if (root == null) {
            return new Node(p, orientation);
        }
        int cmp = root.compareTo(new Node(p, orientation));
        if (cmp > 0) {
            root.left = insertHelper(root.left, p, !orientation);
        } else {
            root.right = insertHelper(root.right, p, !orientation);
        }
        return root;


    }
    public Point nearest(double x, double y) {
        Point goal = new Point(x, y);
        return nearest(root, goal, root).point;
    }

    private Node nearest(Node n, Point goal, Node best) {
        if (n == null) {
            return best;
        }
        if (Double.compare(Point.distance(n.point, goal), Point.distance(best.point, goal)) < 0) {
            best = n;
        }
        int cmp = n.compareTo(new Node(goal, n.orientation));
        Node goodSide = cmp > 0 ? n.left : n.right;
        Node badSide = cmp > 0 ? n.right : n.left;
        best = nearest(goodSide, goal, best);
        if (worthLooking(n, goal, best)) {
            best = nearest(badSide, goal, best);
        }

        return best;
    }
    private boolean worthLooking(Node n, Point goal, Node best) {
        double min;
        if (n.orientation == DividedByX) {
            min = Math.abs(n.point.getX() - goal.getX());
        } else {
            min = Math.abs(n.point.getY() - goal.getY());
        }
        min = Math.pow(min, 2);
        return min < Point.distance(goal, best.point);
    }

}
