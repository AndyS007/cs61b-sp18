import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.util.*;

/**
 * Graph for storing all of the intersection (vertex) and road (edge) information.
 * Uses your GraphBuildingHandler to convert the XML files into a graph. Your
 * code must include the vertices, adjacent, distance, closest, lat, and lon
 * methods. You'll also need to include instance variables and methods for
 * modifying the graph (e.g. addNode and addEdge).
 *
 * @author Alan Yao, Josh Hug
 */
public class GraphDB {
    /** Your instance variables for storing the graph. You should consider
     * creating helper classes, e.g. Node, Edge, etc. */
    private Map<Long, Node> nodes = new HashMap<>();
    private Map<Long, Node> cleanNodes = new HashMap<>();


    static class Edge {
        String name;
        //int maxSpeed;
        List<Node> way;
        public Edge(List<Node> way) {
            this.way = way;
            this.name = null;
        }
        public void setName(String name) {
            this.name = name;
        }
        public void add(Node node) {
            way.add(node);
        }
        public int size() {
            return way.size();
        }
    }
    void addEdge(Edge e) {
        Node[] nodeArray = new Node[e.size()];
        nodeArray = e.way.toArray(nodeArray);
        for (int i = 0; i < nodeArray.length; i++) {
            if (i + 1 < nodeArray.length) {
                nodeArray[i].neighbor.put(nodeArray[i + 1].id, e.name);
            }
            if (i - 1 >= 0) {
                nodeArray[i].neighbor.put(nodeArray[i - 1].id, e.name);
            }
        }
    }
    static class Node {
        long id;
        double lon;
        double lat;
        String name;
        //Set<Long> neighbor;
        Map<Long, String> neighbor;

        public Node(String id, String lon, String lat) {
            this.id = Long.parseLong(id);
            this.lon = Double.parseDouble(lon);
            this.lat = Double.parseDouble(lat);
            this.neighbor = new HashMap<>();

        }
        public void setName(String name) {
            this.name = name;
        }
    }
    void addNode(Node n) {
        this.nodes.put(n.id, n);
    }
    Node getNode(long id) {
        return nodes.get(id);
    }

    /**
     * Example constructor shows how to create and start an XML parser.
     * You do not need to modify this constructor, but you're welcome to do so.
     * @param dbPath Path to the XML file to be parsed.
     */
    public GraphDB(String dbPath) {
        try {
            File inputFile = new File(dbPath);
            FileInputStream inputStream = new FileInputStream(inputFile);
            // GZIPInputStream stream = new GZIPInputStream(inputStream);

            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            GraphBuildingHandler gbh = new GraphBuildingHandler(this);
            saxParser.parse(inputStream, gbh);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        clean();

    }

    /**
     * Helper to process strings into their "cleaned" form, ignoring punctuation and capitalization.
     * @param s Input string.
     * @return Cleaned string.
     */
    static String cleanString(String s) {
        return s.replaceAll("[^a-zA-Z ]", "").toLowerCase();
    }


    /**
     *  Remove nodes with no connections from the graph.
     *  While this does not guarantee that any two nodes in the remaining graph are connected,
     *  we can reasonably assume this since typically roads are connected.
     */
    private void clean() {
        cleanNodes = new HashMap<>();
        for (Node n : nodes.values()) {
            if (!n.neighbor.isEmpty()) {
                for (long id : n.neighbor.keySet()) {
                    cleanNodes.put(id, nodes.get(id));

                }
            }
        }

        /*
        Edge useless = null;
        for (Edge e : edges) {
            for(Node n : e.way) {
                if (!n.neighbor.isEmpty()) {
                    cleanNodes.put(n.id, n);
                } else {
                    //System.out.println("isolated point found! name: " + n.name + "way name: " + e.name + "way element: ");
                    useless = e;
                }


            }
        }
        edges.remove(useless);
         */
    }

    /**
     * Returns an iterable of all vertex IDs in the graph.
     * @return An iterable of id's of all vertices in the graph.
     */
    Iterable<Long> vertices() {
        return cleanNodes.keySet();

    }

    /**
     * Returns ids of all vertices adjacent to v.
     * @param v The id of the vertex we are looking adjacent to.
     * @return An iterable of the ids of the neighbors of v.
     */
    Iterable<Long> adjacent(long v) {
        Node n = cleanNodes.get(v);
        return n.neighbor.keySet();
    }

    /**
     * Returns the great-circle distance between vertices v and w in miles.
     * Assumes the lon/lat methods are implemented properly.
     * <a href="https://www.movable-type.co.uk/scripts/latlong.html">Source</a>.
     * @param v The id of the first vertex.
     * @param w The id of the second vertex.
     * @return The great-circle distance between the two locations from the graph.
     */
    double distance(long v, long w) {
        return distance(lon(v), lat(v), lon(w), lat(w));
    }

    static double distance(double lonV, double latV, double lonW, double latW) {
        double phi1 = Math.toRadians(latV);
        double phi2 = Math.toRadians(latW);
        double dphi = Math.toRadians(latW - latV);
        double dlambda = Math.toRadians(lonW - lonV);

        double a = Math.sin(dphi / 2.0) * Math.sin(dphi / 2.0);
        a += Math.cos(phi1) * Math.cos(phi2) * Math.sin(dlambda / 2.0) * Math.sin(dlambda / 2.0);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return 3963 * c;
    }

    /**
     * Returns the initial bearing (angle) between vertices v and w in degrees.
     * The initial bearing is the angle that, if followed in a straight line
     * along a great-circle arc from the starting point, would take you to the
     * end point.
     * Assumes the lon/lat methods are implemented properly.
     * <a href="https://www.movable-type.co.uk/scripts/latlong.html">Source</a>.
     * @param v The id of the first vertex.
     * @param w The id of the second vertex.
     * @return The initial bearing between the vertices.
     */
    double bearing(long v, long w) {
        return bearing(lon(v), lat(v), lon(w), lat(w));
    }

    static double bearing(double lonV, double latV, double lonW, double latW) {
        double phi1 = Math.toRadians(latV);
        double phi2 = Math.toRadians(latW);
        double lambda1 = Math.toRadians(lonV);
        double lambda2 = Math.toRadians(lonW);

        double y = Math.sin(lambda2 - lambda1) * Math.cos(phi2);
        double x = Math.cos(phi1) * Math.sin(phi2);
        x -= Math.sin(phi1) * Math.cos(phi2) * Math.cos(lambda2 - lambda1);
        return Math.toDegrees(Math.atan2(y, x));
    }

    /**
     * Returns the vertex closest to the given longitude and latitude.
     * @param lon The target longitude.
     * @param lat The target latitude.
     * @return The id of the node in the graph closest to the target.
     */
    long closest(double lon, double lat) {
        long closestID = 0;
        double min = Double.MAX_VALUE;
        for (Node n : nodes.values()) {
            double tmpDistance = distance(n.lon, n.lat, lon, lat);
            if (tmpDistance < min) {
                closestID = n.id;
                min = tmpDistance;
            }

        }
        return closestID;
    }

    /**
     * Gets the longitude of a vertex.
     * @param v The id of the vertex.
     * @return The longitude of the vertex.
     */
    double lon(long v) {
        Node n = nodes.get(v);
        return n.lon;
    }

    /**
     * Gets the latitude of a vertex.
     * @param v The id of the vertex.
     * @return The latitude of the vertex.
     */
    double lat(long v) {
        Node n = nodes.get(v);
        return n.lat;
    }
}
