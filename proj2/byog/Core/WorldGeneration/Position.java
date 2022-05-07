package byog.Core.WorldGeneration;

import java.io.Serializable;
import byog.Core.EnumType.Movement;

public class Position implements Serializable {

    public int x;
    public int y;

    public Position() {}
    public Position(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX(){
            return x;
    }
    public int getY() {
            return y;
    }
    public Position nextPosition(Movement movement) {
        Position p = new Position(this.getX(), this.getY());
        switch (movement) {
            case UP : {
                p.y += 1;
                break;
            }
            case DOWN : {
                p.y -= 1;
                break;
            }
            case LEFT : {
                p.x -= 1;
                break;
            }
            case RIGHT : {
                p.x += 1;
                break;
            }
        }
        return p;

    }
    public void move(Movement movement) {
            switch (movement) {
                case UP : {
                    y += 1;
                    break;
                }
                case DOWN : {
                    y -= 1;
                    break;
                }
                case LEFT : {
                    x -= 1;
                    break;
                }
                case RIGHT : {
                    x += 1;
                    break;
                }
            }
    }


}
