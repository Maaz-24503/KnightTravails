import java.util.*;
class tile {
    String name;    //position of the square
    LinkedList<tile> KnightMoves;       //all legal moves of knight from current tile
    boolean isFilled;       //whether the tile is filled or not
    tile(String d){
        name=d;
        KnightMoves = new LinkedList<>();
        isFilled = false;
    }

    @Override
    public String toString() {
        String s = name + ": ";
        for(int i = 0; i< KnightMoves.size(); i++){
            s = s + KnightMoves.get(i).name + " ";
        }
        return s;
    }
}
