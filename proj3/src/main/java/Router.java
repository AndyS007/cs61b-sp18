import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * This class provides a shortestPath method for finding routes between two points
 * on the map. Start by using Dijkstra's, and if your code isn't fast enough for your
 * satisfaction (or the autograder), upgrade your implementation by switching it to A*.
 * Your code will probably not be fast enough to pass the autograder unless you use A*.
 * The difference between A* and Dijkstra's is only a couple of lines of code, and boils
 * down to the priority you use to order your vertices.
 */
public class Router {
    /**
     * Return a List of longs representing the shortest path from the node
     * closest to a start location and the node closest to the destination
     * location.
     * @param g The graph to use.
     * @param stlon The longitude of the start location.
     * @param stlat The latitude of the start location.
     * @param destlon The longitude of the destination location.
     * @param destlat The latitude of the destination location.
     * @return A list of node id's in the order visited on the shortest path.
     */
    public static List<Long> shortestPath(GraphDB g, double stlon, double stlat,
                                          double destlon, double destlat) {
        PriorityQueue<SearchNode> pq = new PriorityQueue<>();
        long source = g.closest(stlon, stlat);
        long target = g.closest(destlon, destlat);
        Map<Long, Double> best = initializeBest(g);
        best.put(source, 0.0);
        pq.add(new SearchNode(source, 0, 0, null));
        while (!pq.isEmpty() && pq.peek().id != target) {
            SearchNode current = pq.remove();
            for (long next : g.adjacent(current.id)) {
                double distance = best.get(current.id) + g.distance(current.id, next);
                if (best.get(next) > distance) {
                    best.put(next, distance);
                    double priority = distance + g.distance(next, target);
                    pq.add(new SearchNode(next, distance, priority, current));
                }
            }
        }
        return path(pq.peek());
    }
    private static Map<Long, Double> initializeBest(GraphDB g) {
        Map<Long, Double> bestMap = new HashMap<>();
        for (long nodeID : g.vertices()) {
            bestMap.put(nodeID, Double.MAX_VALUE);
        }
        return bestMap;
    }
    private static List<Long> path(SearchNode target) {
        Deque<Long> stack = new LinkedList<>();
        List<Long> path = new ArrayList<>();
        SearchNode tmp = target;
        while (tmp != null) {
            stack.push(tmp.id);
            tmp = tmp.previous;
        }
        while (!stack.isEmpty()) {
            path.add(stack.pop());
        }
        return path;
    }
    static class SearchNode implements Comparable<SearchNode> {
        long id;
        double distanceFromSource;
        double priority;
        SearchNode previous;
        SearchNode(long id, double distanceFromSource,
                          double priority, SearchNode previous) {
            this.id = id;
            this.distanceFromSource = distanceFromSource;
            this.priority = priority;
            this.previous = previous;
        }
        @Override
        public int compareTo(SearchNode o) {
            return Double.compare(this.priority, o.priority);
        }
    }
    /**
     * Create the list of directions corresponding to a route on the graph.
     * @param g The graph to use.
     * @param route The route to translate into directions. Each element
     *              corresponds to a node from the graph in the route.
     * @return A list of NavigatiionDirection objects corresponding to the input
     * route.
     */
    public static List<NavigationDirection> routeDirections(GraphDB g, List<Long> route) {
        List<NavigationDirection> ndList = new ArrayList<>();

        int nextDirection = NavigationDirection.START;
        double distance = 0;
        List<DirectionNode> nodeList = buildDirectionNodes(route, g);
        for (DirectionNode n : nodeList) {
            distance += n.distanceFromPre;
            if (n.isIntersection() || n.next == null) {
                NavigationDirection nd  = new NavigationDirection(nextDirection, n.curWay, distance);
                nextDirection = getDirection(n.nextBearing, n.preBearing);
                ndList.add(nd);
                distance = 0;
            }
        }
        return ndList;
    }
    private static List<DirectionNode> buildDirectionNodes(List<Long> route, GraphDB g) {
        List<DirectionNode> directionNodeList = new ArrayList<>();
        for (int i = 0; i < route.size(); i += 1) {
            Long pre = i == 0 ? null : route.get(i - 1);
            Long cur = route.get(i);
            Long next = i == route.size() - 1 ? null : route.get(i + 1);
            directionNodeList.add(new DirectionNode(pre, cur, next, g));
        }
        return directionNodeList;
    }
    private static class DirectionNode {
        Long pre;
        Long cur;
        Long next;
        Double distanceFromPre;
        Double distanceToNext;
        Double preBearing;
        Double nextBearing;
        String curWay;
        String nextWay;
        public DirectionNode(Long pre, Long cur, Long next, GraphDB g) {
            this.pre = pre;
            this.cur = cur;
            this.next = next;
            distanceFromPre = pre == null ? 0.0 : g.distance(pre, cur);
            distanceToNext = next == null ? 0.0 : g.distance(cur, next);
            preBearing = pre == null ? g.bearing(cur, next) : g.bearing(pre,cur);
            nextBearing = next == null ? g.bearing(pre, cur) : g.bearing(cur, next);
            curWay = pre == null ? g.getNode(cur).getWayToNeighbor(next) : g.getNode(pre).getWayToNeighbor(cur);
            nextWay = next == null ? g.getNode(pre).getWayToNeighbor(cur) : g.getNode(cur).getWayToNeighbor(next);
        }
        private boolean isIntersection() {
            return !isSameWay(curWay, nextWay);
        }

    }
    private static boolean isSameWay(String curWay, String nextWay) {
        if (nextWay == null && curWay == null) {
            return true;
        } else if (nextWay == null || curWay == null) {
            return false;
        }
        return curWay.equals(nextWay);
    }

    private static int getDirection(double currentBearing, double previousBearing) {
        double relativeBearing = currentBearing - previousBearing;
        return bearingToDirection(relativeBearing);
    }
    private static int bearingToDirection(double angle) {
        double absAngle = Math.abs(angle);
        if (absAngle > 180) {
            absAngle = 360 -absAngle;
            angle *= -1;
        }
        if (absAngle <= 15) {
            return NavigationDirection.STRAIGHT;
        } else if (absAngle <= 30) {
            return angle < 0 ? NavigationDirection.SLIGHT_LEFT : NavigationDirection.SLIGHT_RIGHT;
        } else if (absAngle <= 100) {
            return angle < 0 ? NavigationDirection.LEFT : NavigationDirection.RIGHT;
        } else {
            return angle < 0? NavigationDirection.SHARP_LEFT : NavigationDirection.SHARP_RIGHT;
        }

    }


    /**
     * Class to represent a navigation direction, which consists of 3 attributes:
     * a direction to go, a way, and the distance to travel for.
     */
    public static class NavigationDirection {

        /** Integer constants representing directions. */
        public static final int START = 0;
        public static final int STRAIGHT = 1;
        public static final int SLIGHT_LEFT = 2;
        public static final int SLIGHT_RIGHT = 3;
        public static final int RIGHT = 4;
        public static final int LEFT = 5;
        public static final int SHARP_LEFT = 6;
        public static final int SHARP_RIGHT = 7;

        /** Number of directions supported. */
        public static final int NUM_DIRECTIONS = 8;

        /** A mapping of integer values to directions.*/
        public static final String[] DIRECTIONS = new String[NUM_DIRECTIONS];

        /** Default name for an unknown way. */
        public static final String UNKNOWN_ROAD = "unknown road";
        
        /** Static initializer. */
        static {
            DIRECTIONS[START] = "Start";
            DIRECTIONS[STRAIGHT] = "Go straight";
            DIRECTIONS[SLIGHT_LEFT] = "Slight left";
            DIRECTIONS[SLIGHT_RIGHT] = "Slight right";
            DIRECTIONS[LEFT] = "Turn left";
            DIRECTIONS[RIGHT] = "Turn right";
            DIRECTIONS[SHARP_LEFT] = "Sharp left";
            DIRECTIONS[SHARP_RIGHT] = "Sharp right";
        }

        /** The direction a given NavigationDirection represents.*/
        int direction;
        /** The name of the way I represent. */
        String way;
        /** The distance along this way I represent. */
        double distance;

        /**
         * Create a default, anonymous NavigationDirection.
         */
        public NavigationDirection() {
            this.direction = STRAIGHT;
            this.way = UNKNOWN_ROAD;
            this.distance = 0.0;
        }
        public NavigationDirection(int direction, String way, double distance) {
            this.direction = direction;
            if (way == null) {
                this.way = "";
            } else {
                this.way = way;
            }
            this.distance = distance;
        }

        public String toString() {
            return String.format("%s on %s and continue for %.3f miles.",
                    DIRECTIONS[direction], way, distance);
        }

        /**
         * Takes the string representation of a navigation direction and converts it into
         * a Navigation Direction object.
         * @param dirAsString The string representation of the NavigationDirection.
         * @return A NavigationDirection object representing the input string.
         */
        public static NavigationDirection fromString(String dirAsString) {
            String regex = "([a-zA-Z\\s]+) on ([\\w\\s]*) and continue for ([0-9\\.]+) miles\\.";
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(dirAsString);
            NavigationDirection nd = new NavigationDirection();
            if (m.matches()) {
                String direction = m.group(1);
                if (direction.equals("Start")) {
                    nd.direction = NavigationDirection.START;
                } else if (direction.equals("Go straight")) {
                    nd.direction = NavigationDirection.STRAIGHT;
                } else if (direction.equals("Slight left")) {
                    nd.direction = NavigationDirection.SLIGHT_LEFT;
                } else if (direction.equals("Slight right")) {
                    nd.direction = NavigationDirection.SLIGHT_RIGHT;
                } else if (direction.equals("Turn right")) {
                    nd.direction = NavigationDirection.RIGHT;
                } else if (direction.equals("Turn left")) {
                    nd.direction = NavigationDirection.LEFT;
                } else if (direction.equals("Sharp left")) {
                    nd.direction = NavigationDirection.SHARP_LEFT;
                } else if (direction.equals("Sharp right")) {
                    nd.direction = NavigationDirection.SHARP_RIGHT;
                } else {
                    return null;
                }

                nd.way = m.group(2);
                try {
                    nd.distance = Double.parseDouble(m.group(3));
                } catch (NumberFormatException e) {
                    return null;
                }
                return nd;
            } else {
                // not a valid nd
                return null;
            }
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof NavigationDirection) {
                return direction == ((NavigationDirection) o).direction
                    && way.equals(((NavigationDirection) o).way)
                    && distance == ((NavigationDirection) o).distance;
            }
            return false;
        }

        @Override
        public int hashCode() {
            return Objects.hash(direction, way, distance);
        }
    }
}
