package byog.Core;

import java.io.Serializable;

public class Room implements Serializable {
    public int x;
    public int y;
    public int WIDTH;
    public int HEIGHT;
    public Room(int x,int y,int width,int height){
        this.x=x;
        this.y=y;
        this.WIDTH=width;
        this.HEIGHT=height;
    }
}