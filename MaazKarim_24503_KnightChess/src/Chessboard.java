import java.util.*;

public class Chessboard {
    ArrayList<tile> adjList;
    int count;

    /**
     * constructor creates chess board and connects all possible knight moves
     */
    Chessboard(){
        adjList = new ArrayList<>();
        count = 0;
        for(int i = 0; i<8; i++){
            for(int j = 0; j<8; j++){
                char c =(char) (65 + i);        //using the ascii values
                String s = ""+c + (j+1);
                AddTile(s);                     //adding vertices to the graph
            }
        }
        for(int i = 0; i<64; i++) addMoves(adjList.get(i));     //connecting all knight moves

    }

    /**
     * adds a tile to the board(graph)
     * @param n - name of the tile to be added
     */
    public void AddTile(String n){
        adjList.add(new tile(n));
        count++;
    }

    /**
     * finds tile in a board
     * @param n1 - name of the tile
     * @return tile with the given name
     */
    public tile FindTile(String n1){
        for(int i = 0; i<adjList.size(); i++){
            if(adjList.get(i).name.compareToIgnoreCase(n1)==0) return adjList.get(i);
        }
        return null;
    }

    /**
     * find the index of a given tile in the adj list
     * @param V - tile to be found
     * @return index of tile
     */
    public int FindIndex(tile V){
        for(int i = 0 ; i< adjList.size(); i++) if(adjList.get(i)==V) return i;
        return -1;
    }

    /**
     * creates an edge between the two given tiles making it a legal move
     * @param n1 - first tile
     * @param n2 - second tile
     */
    public void AddEdge(tile n1, tile n2){
        if(!n1.KnightMoves.contains(n2)) n1.KnightMoves.add(n2);
        if(!n2.KnightMoves.contains(n1)) n2.KnightMoves.add(n1);
    }

    /**
     * creates an edge between the two tiles with the given names making it a legal move
     * @param n1 - name of tile 1
     * @param n2 - name of tile 2
     */
    public void AddEdge(String n1, String n2){
        tile first = FindTile(n1);
        tile second = FindTile(n2);
        if(!first.KnightMoves.contains(second)) first.KnightMoves.add(second);
        if(!second.KnightMoves.contains(first)) second.KnightMoves.add(first);
    }

    /**
     * removes the given tile from the board removing all its occurrences in legal moves
     * @param n - name of tile to be deleted
     */
    public void deleteTile(String n){
        tile removal = FindTile(n);
        adjList.remove(removal);
        count--;
        for(int i = 0; i<adjList.size(); i++){
            if(adjList.get(i).KnightMoves.contains(removal)){
                adjList.get(i).KnightMoves.remove(removal);
            }
        }
    }

    /**
     * deletes edge between tiles of two given names
     * @param n1 - name of first tile
     * @param n2 - name of second tile
     */
    public void deleteEdge(String n1, String n2){
        tile first = FindTile(n1);
        tile scnd = FindTile(n2);
        if(scnd==null || first==null) return;
        if(first.KnightMoves.contains(scnd)) first.KnightMoves.remove(scnd);
        if(scnd.KnightMoves.contains(first)) scnd.KnightMoves.remove(first);
    }

    /**
     * creates all edges for legal moves of the given tile
     * @param pos - tile to add moves to
     */
    public void addMoves(tile pos){
        String name = pos.name;
        int row = ((int)name.charAt(0))-64;
        int col = ((int)name.charAt(1))-48;
        int ind = adjList.indexOf(pos);

        //all 8 possible moves added making sure they don't go outside the board
        if(row<8 && col>2) AddEdge(pos,adjList.get(ind + 6));
        if(row<8 && col<7) AddEdge(pos,adjList.get(ind + 10));
        if(row<7 && col>1) AddEdge(pos,adjList.get(ind + 15));
        if(row<7 && col<8) AddEdge(pos,adjList.get(ind + 17));
        if(row>1 && col<7) AddEdge(pos,adjList.get(ind - 6));
        if(row>1 && col>2) AddEdge(pos,adjList.get(ind - 10));
        if(row>2 && col<8) AddEdge(pos,adjList.get(ind - 15));
        if(row>2 && col>1) AddEdge(pos,adjList.get(ind - 17));
    }


    /**
     * finds the shortest move from one tile to another using a knight
     * @param SRC - start tile
     * @param DEST - end tile
     * @return ordered array of tiles in the path
     */
    public tile[] shortestPath(String SRC, String DEST) {
        //standard BFS algorithm
        tile src = FindTile(SRC);
        tile dest = FindTile(DEST);
        boolean[] flag = new boolean[64];
        int[] track = new int[64];
        Queue<tile> BFS = new ArrayDeque<tile>();
        BFS.add(src);
        while (!BFS.isEmpty()) {
            tile prev = BFS.remove();
            for(int i = 0; i<prev.KnightMoves.size(); i++){
                if(!flag[FindIndex(prev.KnightMoves.get(i))]){
                    BFS.add(prev.KnightMoves.get(i));
                    flag[FindIndex(prev.KnightMoves.get(i))] = true;
                    track[FindIndex(prev.KnightMoves.get(i))] = FindIndex(prev);
                }
                if(prev.KnightMoves.get(i)==dest) break;
            }
        }
        BFS = null;
        int curr = FindIndex(dest);
        ArrayList<tile> reverse = new ArrayList<>();
        while (true) {
            reverse.add(adjList.get(curr));
            if (curr == FindIndex(src)) break;
            curr = track[curr];
        }
        tile[] tbr = new tile[reverse.size()];
        for (int i = 0; i < reverse.size(); i++) {
            tbr[reverse.size()-i-1] = reverse.get(i);
        }
        return tbr;
    }

    public String toString(){
        String s = "";
        for(int i = 0; i<adjList.size(); i++){
            s = s + adjList.get(i) + " \n";
        }
        return s;
    }
}
