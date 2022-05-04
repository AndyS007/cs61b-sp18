import java.util.HashMap;
import java.util.Map;

/**
 * This class provides all code necessary to take a query box and produce
 * a query result. The getMapRaster method must return a Map containing all
 * seven of the required fields, otherwise the front end code will probably
 * not draw the output correctly.
 */
public class Rasterer {
    private double[] lonDPPTable;
    private static final int DEPTH = 7;


    private void setLonDPPTable() {
        lonDPPTable = new double[DEPTH + 1];
        lonDPPTable[0] = calculateLonDPP(ROOT_ULLON, ROOT_LRLON, TILE_SIZE);
        for (int i = 1; i <= lonDPPTable.length - 1; i++) {
            lonDPPTable[i] = lonDPPTable[i - 1] / 2;
        }
    }

    /*
    public static void main(String[] args) {
        Rasterer r =  new Rasterer();
    }
     */

    public Rasterer() {
        setLonDPPTable();
    }


    /**
     * Takes a user query and finds the grid of images that best matches the query. These
     * images will be combined into one big image (rastered) by the front end. <br>
     *
     *     The grid of images must obey the following properties, where image in the
     *     grid is referred to as a "tile".
     *     <ul>
     *         <li>The tiles collected must cover the most longitudinal distance per pixel
     *         (LonDPP) possible, while still covering less than or equal to the amount of
     *         longitudinal distance per pixel in the query box for the user viewport size. </li>
     *         <li>Contains all tiles that intersect the query bounding box that fulfill the
     *         above condition.</li>
     *         <li>The tiles must be arranged in-order to reconstruct the full image.</li>
     *     </ul>
     *
     * @param params Map of the HTTP GET request's query parameters - the query box and
     *               the user viewport width and height.
     *
     * @return A map of results for the front end as specified: <br>
     * "render_grid"   : String[][], the files to display. <br>
     * "raster_ul_lon" : Number, the bounding upper left longitude of the rastered image. <br>
     * "raster_ul_lat" : Number, the bounding upper left latitude of the rastered image. <br>
     * "raster_lr_lon" : Number, the bounding lower right longitude of the rastered image. <br>
     * "raster_lr_lat" : Number, the bounding lower right latitude of the rastered image. <br>
     * "depth"         : Number, the depth of the nodes of the rastered image <br>
     * "query_success" : Boolean, whether the query was able to successfully complete; don't
     *                    forget to set this to true on success! <br>
     */
    public Map<String, Object> getMapRaster(Map<String, Double> params) {
        System.out.println(params);
        Map<String, Object> results = new HashMap<>();
        boolean valid = validateLonLat(params);
        if (!valid) {
            results.put("query_success", false);
        } else {
            double left = params.get("ullon");
            double right = params.get("lrlon");
            double up = params.get("ullat");
            double down = params.get("lrlat");
            int depth = getDepth(left, right, params.get("w"));
            setResults(depth, left, right, up, down, results);
        }
        return results;
    }
    private boolean validateLonLat(Map<String, Double> params) {
        double left = params.get("ullon");
        double right = params.get("lrlon");
        double up = params.get("ullat");
        double down = params.get("lrlat");
        if (outOfBound(left, right, up, down)) {
            return false;
        }
        //modify lon and lat to the ROOT
        if (left - ROOT_ULLON < 0) {
            left = ROOT_ULLON;
            params.put("ullon", left);
        }
        if (right - ROOT_LRLON > 0) {
            right = ROOT_LRLON;
            params.put("lrlon", right);
        }
        if (up - ROOT_ULLAT > 0) {
            up = ROOT_ULLAT;
            params.put("ullat", up);
        }
        if (down - ROOT_LRLAT < 0) {
            down = ROOT_LRLAT;
            params.put("lrlat", down);
        }


        return true;


    }
    private boolean outOfBound(double left, double right, double up, double down) {
        boolean condition1 = right - ROOT_ULLON < 0;
        boolean condition2 = left - ROOT_LRLON > 0;
        boolean condition3 = up - ROOT_LRLAT < 0;
        boolean condition4 = down - ROOT_ULLAT > 0;
        boolean upSideDown = up - down < 0;
        boolean leftSideRight = left - right > 0;
        return condition1 || condition2 || condition3 || condition4 || upSideDown || leftSideRight;
    }
    private void setResults(int depth, double left, double right, double up,
                            double down, Map<String, Object> results) {
        int edgeNum = (int) Math.pow(2, depth);
        double lonUnitLen = Math.abs(ROOT_ULLON - ROOT_LRLON) / edgeNum;
        double latUnitLen = Math.abs(ROOT_ULLAT - ROOT_LRLAT) / edgeNum;
        //make the coordinate start at the Upper Left
        left = left - ROOT_ULLON;
        right = right - ROOT_ULLON;
        up = Math.abs(up - ROOT_ULLAT);
        down = Math.abs(down - ROOT_ULLAT);
        //find the right number of the
        int xLeft = binarySearch(0, edgeNum, left, lonUnitLen);
        int xRight = binarySearch(0, edgeNum, right, lonUnitLen);
        int yUp = binarySearch(0, edgeNum, up, latUnitLen);
        int yDown = binarySearch(0, edgeNum, down, latUnitLen);
        int c = xRight - xLeft + 1;
        int r = yDown - yUp + 1;
        String[][] grid = new String[r][c];
        for (int row = yUp; row <= yDown; row++) {
            for (int col = xLeft; col <= xRight; col++) {
                String s = "d" + depth + "_x" + col + "_y" + row + ".png";
                grid[row - yUp][col - xLeft] = s;
            }
        }

        //transfer to the longitude and latitude
        double ullon = ROOT_ULLON + xLeft * lonUnitLen;
        double lrlon = ROOT_ULLON + (xRight + 1) * lonUnitLen;
        double ullat = ROOT_ULLAT - yUp * latUnitLen;
        double lrlat = ROOT_ULLAT - (yDown + 1) * latUnitLen;
        results.put("depth", depth);
        results.put("render_grid", grid);
        results.put("raster_ul_lon", ullon);
        results.put("raster_ul_lat", ullat);
        results.put("raster_lr_lon", lrlon);
        results.put("raster_lr_lat", lrlat);
        results.put("query_success", true);
    }
    private int binarySearch(int first, int last, double val, double unit) {
        while (first < last) {
            int mid = first + (last - first) / 2;
            if (mid * unit <= val) {
                first = mid + 1;
            } else {
                last = mid;
            }
        }
        if (first == 0) {
            return 0;
        }
        return first - 1;



    }
    private double calculateLonDPP(double ullon, double lrlon, double width) {
        return Math.abs(lrlon - ullon) / width;
    }
    private int getDepth(double ullon, double lrlon, double width) {
        int res = 0;
        double requestLonDPP = calculateLonDPP(ullon, lrlon, width);
        boolean found = false;
        for (int i = 0; i < lonDPPTable.length; i++) {
            if (requestLonDPP >= lonDPPTable[i]) {
                res = i;
                found = true;
                break;
            }
        }
        if (!found) {
            res = 7;
        }
        return res;
    }

    private static final int TILE_SIZE = 256;
    public static final double ROOT_ULLAT = 37.892195547244356, ROOT_ULLON = -122.2998046875,
            ROOT_LRLAT = 37.82280243352756, ROOT_LRLON = -122.2119140625;

}
