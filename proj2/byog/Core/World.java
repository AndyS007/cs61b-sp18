package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class World implements Serializable {
    //private static final long serialVersionUID = 156464981;
    private final int WIDTH;
    private final int HEIGHT;
    private final Random RANDOM;
    private final long SEED;
    private static final int tryAddingRoomTimes =100;
    private TETile[][] map;
    //记录房间的位置
    private TETile[][]positionOfRoom;
    private ArrayList<Room>Rooms;
    public Position player;
    public Position door;

    public World(int width, int height, long seed){
        this.HEIGHT=height;
        this.WIDTH=width;
        //用seed产生一个Random对象
        this.SEED=seed;
        this.RANDOM=new Random(seed);
        //填补死胡同时用来标记哪些点走过
        isPassed =new boolean[WIDTH][HEIGHT];
        this.mapGenerator();

    }
    public TETile[][] getMap() {
        return map;
    }
    public TETile[][] mapGenerator(){
        //当生成的地图不是所有点都可达时，重新生成数组和地图
        do {
            //创建世界数组
            map = new TETile[WIDTH][HEIGHT];
            positionOfRoom = new TETile[WIDTH][HEIGHT];
            //初始化世界数组
            initializeWorldArray();
            //初始化HallWay
            HallWay.initializeHallWay(map);
            //初始化房间列表
            Rooms=new ArrayList<>();
            //往HallWay中填充房间
            fillWithRoom();
            //在房间外打通出迷宫
        }while (HallWay.generateHallWay(map,SEED)==false);
        //在房间上开口
        connectRoomWithHallWay();
        //移除DeadEnds
        removeDeadEnds();
        //销毁多余的墙
        destroyWall();
        GeneratePlayer();
        GenerateDoor();
        return map;
    }
    private void GenerateDoor(){
        while (true){
            int x=RANDOM.nextInt(WIDTH);
            int y=RANDOM.nextInt(HEIGHT);
            if (Tileset.FLOOR.equals(map[x][y])){
                door=new Position(x,y);
                map[x][y]=Tileset.LOCKED_DOOR;
                return;
            }
        }
    }
    private void GeneratePlayer(){
        while (true){
            int x=RANDOM.nextInt(WIDTH);
            int y=RANDOM.nextInt(HEIGHT);
            if (Tileset.FLOOR.equals(map[x][y])){
                player=new Position(x,y);
                map[x][y]=Tileset.PLAYER;
                return;
            }
        }
    }
    //初始化数组
    private void initializeWorldArray(){
        for (int x=0;x<WIDTH;x++)
            for (int y=0;y<HEIGHT;y++){
                positionOfRoom[x][y]=Tileset.NOTHING;
                map[x][y]= Tileset.NOTHING;
            }
    }
    private void removeDeadEnds(){
        for (int i=0;i<WIDTH;i++){
            for (int j=0;j<HEIGHT;j++){
                if ((Tileset.GRASS.equals(map[i][j])||Tileset.FLOOR.equals(map[i][j]))){
                    map[i][j]=Tileset.FLOOR;
                    int cnt=0;
                    if (Tileset.WALL.equals(map[i-1][j]))
                        cnt++;
                    if (Tileset.WALL.equals(map[i+1][j]))
                        cnt++;
                    if (Tileset.WALL.equals(map[i][j-1]))
                        cnt++;
                    if (Tileset.WALL.equals(map[i][j+1]))
                        cnt++;
                    if (cnt>=3)
                        DFS(i,j);
                }
            }
        }
    }
    private static int[][]next={
            {1,0},
            {0,-1},
            {-1,0},
            {0,1}
    };
    private boolean[][] isPassed;
    //对每一个点，我们需要查看周围的四个点是否是WALL，然后改变这个点，再进入下一个点
    //而原本的DFS是沿着一条路线，从一头走到另一头，对路上的每一个点都只是依次查看周围的
    //点，一旦找到可以通过的点，就立马进入，无法确定这一点周围是否有3个WALL。只有当走到
    //头时，扫描了周围的四个点，发现都无法通过，才会往后退。也就是说，只有后退的时候，我们
    //才能知道某一点周围所有点的情况。而填补所有的死胡同需要我们从所有的死胡同的终点出发，
    //向中间汇聚，一边移动一边填补。
    // 所以我们需要将DFS改造成前进和后退时都要查看周围所有点的情况，才能进行下一步。
    private boolean DFS(int x,int y){
        int cnt=0;
        Queue<Position>accessiblePath=new LinkedList<>();
        //先查找某一点周围所有的点，将可以通行的点加入候选列表
        for (int i=0;i<=3;i++){
            int mx=x+next[i][0];
            int my=y+next[i][1];
            if (mx<0||mx>=WIDTH||my<0||my>=HEIGHT)
                continue;
            if (Tileset.WALL.equals(map[mx][my])){
                cnt++;
                continue;
            }
            if (isPassed[mx][my]==true) {
                continue;
            }
            if (Tileset.GRASS.equals(map[mx][my]))
                map[mx][my]=Tileset.FLOOR;
            if (Tileset.FLOOR.equals(map[mx][my])){
                accessiblePath.offer(new Position(mx,my));
            }
        }
        if (cnt>=3) map[x][y]=Tileset.WALL;
        while (!accessiblePath.isEmpty()){
            Position pos=accessiblePath.peek();
            isPassed[pos.x][pos.y]=true;
            if(DFS(pos.x,pos.y))cnt++;
            if (cnt>=3)
                map[x][y]=Tileset.WALL;
            accessiblePath.poll();
        }
        return cnt>=3;
    }
    private void destroyWall(){
        int[][]next2={
                {1,1},
                {1,-1},
                {-1,-1},
                {-1,1}
        };
        for (int i = 0; i< map.length; i++){
            for (int j = 0; j< map[0].length; j++){
                if (Tileset.WALL.equals(map[i][j])){
                    boolean isDestroy=true;
                    //判断某一点对角线上的四个点是否是FLOOR
                    for (int k=0;k<4;k++){
                        int mx=i+next2[k][0];
                        int my=j+next2[k][1];
                        if (mx<0||mx>=WIDTH||my<0||my>=HEIGHT)
                            continue;
                        if (Tileset.FLOOR.equals(map[mx][my]))
                            isDestroy=false;
                    }
                    if (isDestroy==true)
                        map[i][j]=Tileset.NOTHING;
                }
            }
        }
    }

    //在房间上开口，打通房间和迷宫的连接
    private void connectRoomWithHallWay(){
        for (int i=0;i<Rooms.size();i++){
            Room curRoom=Rooms.get(i);
            removeWall(curRoom);
        }
    }
    private void removeWall(Room curRoom){
        for (int i=0;i<100;i++){
            int index=RANDOM.nextInt(4);
            int mx,my;
            switch (index){
                //在左边的墙壁上挖洞
                case 0:
                    mx=curRoom.x;
                    my=RANDOM.nextInt(curRoom.HEIGHT)+curRoom.y+1;
                    if (!canBeRemoved(mx,my,index))continue;
                    map[mx][my]=Tileset.FLOOR;
                    if (Tileset.FLOOR.equals(map[mx-1][my]))
                        return;
                    map[mx-1][my]=Tileset.FLOOR;
                    if (Tileset.GRASS.equals(map[mx-2][my]))
                        continue;
                    return;
                //在右边的墙壁上挖洞
                case 1:
                    mx=curRoom.x+curRoom.WIDTH+1;
                    my=RANDOM.nextInt(curRoom.HEIGHT)+curRoom.y+1;
                    if (!canBeRemoved(mx,my,index))continue;
                    map[mx][my]=Tileset.FLOOR;
                    if (Tileset.FLOOR.equals(map[mx+1][my]))
                        return;
                    map[mx+1][my]=Tileset.FLOOR;
                    if (Tileset.GRASS.equals(map[mx+2][my]))
                        continue;
                    return;
                //在下面的墙壁上挖洞
                case 2:
                    mx=RANDOM.nextInt(curRoom.WIDTH)+curRoom.x+1;
                    my=curRoom.y;
                    if (!canBeRemoved(mx,my,index))continue;
                    map[mx][my]=Tileset.FLOOR;
                    if (Tileset.FLOOR.equals(map[mx][my-1]))
                        return;
                    map[mx][my-1]=Tileset.FLOOR;
                    if (Tileset.GRASS.equals(map[mx][my-2]))
                        continue;
                    return;
                //在上面的墙壁上挖洞
                case 3:
                    mx=RANDOM.nextInt(curRoom.WIDTH)+curRoom.x+1;
                    my=curRoom.y+curRoom.HEIGHT+1;
                    if (!canBeRemoved(mx,my,index))continue;
                    map[mx][my]=Tileset.FLOOR;
                    if (Tileset.FLOOR.equals(map[mx][my+1]))
                        return;
                    map[mx][my+1]=Tileset.FLOOR;
                    if (Tileset.GRASS.equals(map[mx][my+2]))
                        continue;
                    return;

            }

        }
    }
    private boolean canBeRemoved(int x,int y,int direcrion){
        if (Tileset.FLOOR.equals(map[x][y]))
            return false;
        switch (direcrion){
            //向左挖：
            case 0:
                if (x<=1)
                    return false;
                if (Tileset.NOTHING.equals(map[x-1][y])||(Tileset.WALL.equals(map[x-2][y])&&Tileset.WALL.equals(map[x-1][y])))
                    return false;
                return true;
            //向右挖：
            case 1:
                if (x>=WIDTH-2)
                    return false;
                if (Tileset.NOTHING.equals(map[x+1][y])||(Tileset.WALL.equals(map[x+2][y])&&Tileset.WALL.equals(map[x+1][y])))
                    return false;
                return true;
            //向下挖：
            case 2:
                if (y<=1)
                    return false;
                if (Tileset.NOTHING.equals(map[x][y-1])||(Tileset.WALL.equals(map[x][y-2])&&Tileset.WALL.equals(map[x][y-1])))
                    return false;
                return true;
            //向上挖：
            case 3:
                if (y>=HEIGHT-2)
                    return false;
                if (Tileset.NOTHING.equals(map[x][y+1])||(Tileset.WALL.equals(map[x][y+2])&&Tileset.WALL.equals(map[x][y+1])))
                    return false;
                return true;
        }
        return false;
    }

    private void fillWithRoom(){
        //nextInt方法返回0到WIDTH-1之间和0到HEIGHT-1之间的数
        for (int i = 0; i< tryAddingRoomTimes; i++){
            int x=RANDOM.nextInt(WIDTH);
            int y=RANDOM.nextInt(HEIGHT);
            int width=RANDOM.nextInt(WIDTH/10)+2;
            int height=RANDOM.nextInt(HEIGHT/5)+2;
            if (y+height+1>=HEIGHT||x+width+1>=WIDTH)
                continue;
            if (isOverlap(x,y,width,height))
                continue;
            buildRoom(x,y,width,height);
            //将room添加到ArrayList中记录下来，以便在Room外添加完迷宫后对每一个房间打通和迷宫的连接。
            Rooms.add(new Room(x,y,width,height));
        }

    }
    //造房间
    private void buildRoom(int x,int y,int width,int height){
        for (int i=x;i<=x+width+1;i++){
            for (int j=y;j<=y+height+1;j++){
                if (i==x||i==x+width+1||j==y||j==y+height+1){
                    positionOfRoom[i][j]=Tileset.WALL;
                    map[i][j]=Tileset.WALL;
                    continue;
                }
                //由于FLOOR和在房间外的迷宫的连通条件相冲突，
                //所以先在房间内部填GRASS
                map[i][j]=Tileset.GRASS;
                positionOfRoom[i][j]=Tileset.GRASS;
            }
        }
    }
    //判断房间是否重叠
    private boolean isOverlap(int x,int y,int width,int height){
        for (int i=x;i<=x+width+1;i++)
            for (int j=y;j<=y+height+1;j++)
                if (positionOfRoom[i][j]==Tileset.WALL||positionOfRoom[i][j]==Tileset.GRASS)
                    return true;
        return false;
    }
    public static void main(String[] args) throws InterruptedException {
        TERenderer ter=new TERenderer();
        ter.initialize(80,30);
        World world =new World(80,30,284);
        ter.renderFrame(world.mapGenerator());
    }
}