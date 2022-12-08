
import javax.swing.*;


import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

class ChessGame extends JFrame implements ActionListener, MouseListener
{
    boolean isPressed;      //check if any new action is performed
    private static final int width = 760;
    private static final int height = 760;
    private final static int tileSize = 70;
    private final Chessboard board;
    private int state;      //our board has three states
    int src_x;      //x coordinate of source
    int src_y;      //y coordinate of source
    int dest_x;     //x coordinate of destination
    int dest_y;     //y coordinate of destination
    tile dest;      //destination of tile
    String SRC;     //source name
    String DEST;    //destination name
    tile[] path;    //array of path
    JButton reset;  //button to reset

    /**
     * Constructor simply calls parent JFrame constructor
     */
    public ChessGame()
    {
        super("Chess Board");
        setLayout(null);
        src_x = -1;
        src_y = -1;
        dest_x = -1;
        dest_y = -1;
        isPressed = true;           //for the first refresh to occur
        board = new Chessboard();   //constructing our chess board
        getContentPane().setBackground(Color.YELLOW);    //setting the background to white

        setSize(width, height);     //setting dimensions
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//closing the JFrame set to default term. cond.
        setLocationRelativeTo(null);
        state = 0;  //we start from the state where source is to be selected
        int delay = 8;  //microseconds for refresh-rate
        Timer timer = new Timer(delay, this);
        timer.start();  //refreshes our screen
        addMouseListener(this); //making it so that our code responds to mouse actions
        reset = new JButton("Go back to select start loc.");   //this button will bring us back to state 0
        reset.setBounds(250, 650,200,35);
        reset.addActionListener(e -> {
            state = 0;
            for(int i = 0; i<64; i++) board.adjList.get(i).isFilled = false;   //resetting the board
            isPressed = true;
        });

    }

    /**
     * This function handles all the drawing on the interface
     * @param g - graphics
     */
    void draw(Graphics g)
    {
        drawBoard(g);       //helper function called
        if(state==0) {
            getContentPane().remove(reset);     //state 0 is already at the beginning so no reset
            g.setColor(Color.BLACK);
            g.drawString("Please select start location of the knight" , 200 , 60);
        }
        else{
            getContentPane().add(reset);        //since we are not at initial state we can reset
            reset.repaint();
            if(state==1){
                g.setColor(Color.BLACK);        //message for state 1
                g.drawString("Please select destination of the knight" , 190 , 60);
            }
            g.setColor(new Color(255, 127, 80));
            g.fillRect(src_x ,src_y  ,tileSize,tileSize);  //source rect
            if(state == 2){
                g.setColor(Color.GREEN);
                g.fillRect(dest_x , dest_y , tileSize, tileSize);
                for(int i = 1; i< path.length-1; i++){
                    g.setColor(Color.darkGray);
                    int x = ((int)path[i].name.charAt(0))-65;
                    int y = ((int)path[i].name.charAt(1))-49;
                    g.fillRect((tileSize *x + 100)+10, (tileSize*y + 100)+10,tileSize-20, tileSize-20 );
                    g.setColor(Color.WHITE);
                    g.drawString("" +i, (tileSize *x + 100)+29, (tileSize*y + 100)+42 );
                }
                g.setColor(Color.BLACK);
                g.drawString("Shortest NO. moves: " + (path.length-1) , 245 , 60);
            }
        }
    }

    /**
     * helper function for draw which draws the board fully
     * @param g - graphics
     */
    public void drawBoard(Graphics g){
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.BLACK);
        g2d.drawRect(99,99,tileSize*8 +1,tileSize*8 +1);    //outline of the board
        boolean check = true;
        boolean color = false;  //drawing alternate color squares
        for(int i = 0; i<8; i++){
            color = !color;
            for(int j = 0; j<8; j++){
                String s = "" + ((char)(j+65)) +"" + ((char)(i+49));
                tile curr = board.FindTile(s);
                if(curr.isFilled) check = false;
                if(check) {     //so that overlapping rectangles doesn't flicker
                    if (color) {
                        g2d.setColor(new Color(92, 64, 51));
                        g2d.fillRect(tileSize * j + 100, tileSize * i + 100, tileSize, tileSize);
                    } else {
                        g2d.setColor(new Color(200, 200, 200));
                        g2d.fillRect(tileSize * j + 100, tileSize * i + 100, tileSize, tileSize);
                    }
                }
                color = !color;
                check = true;
            }
        }
        g2d.setColor(Color.BLACK);
        Font font = new Font("Serif", Font.PLAIN, 25);
        g.setFont(font);
        g.setColor(Color.black);    //drawing board coordinates
        for(int i = 0; i<8; i++){
            for(int j=0; j<2; j++){
                if(j==0)g.drawString("" + (char)(i+49),80,99+(tileSize/2)+tileSize*i);
                else g.drawString("" + (char)(i+65),99+(tileSize/2)+tileSize*i,90);
            }
        }
    }
    //this function is called everytime we repaint
    public void paint(Graphics g)
    {
        resetScreen(g);     //refresh screen
        draw(g);            //call draw function
        g.dispose();
    }

    public static void main(String[] args) throws Exception
    {

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run()       //run our class
            {
                new ChessGame().setVisible(true);
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }
    public void resetScreen(Graphics g){
        //clearing the screen by setting it to the background color
        if(isPressed) {
            g.setColor(Color.WHITE);
            g.clearRect(0,0,width,height);
            isPressed = false;
        }
    }
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        int x=e.getX();
        int y=e.getY();
        //this is the range our board exists in
        if(x<660 && x>100 && y<660 && y>100){
            int row = (y-100)/tileSize;
            int col = (x-100)/tileSize;
            if(state==1){       //this means we are selecting destination
                isPressed = true;   //so that everything is refreshed
                dest_x = col*tileSize +100;
                dest_y = row*tileSize + 100;
                state++;        //since we are in the next state
                String ind ="" + ((char)(col+65)) +"" + ((char)(row+49));
                DEST = ind;     //to access adj list
                dest = board.FindTile(ind);
                dest.isFilled = true;   //set tile to filled
                path = board.shortestPath(SRC,DEST);    //call our BFS for knight move
                for(int i = 0; i<path.length; i++){     //filling every tile in the path
                    board.FindTile(path[i].name).isFilled = true;
                }
            }
            if(state==0){       //state for when we are selecting source
                isPressed = true;
                src_x = col*tileSize +100;
                src_y = row*tileSize + 100;
                state++;
                String ind ="" + ((char)(col+65)) +"" + ((char)(row+49));
                SRC = ind;
                tile src = board.FindTile(ind);
                src.isFilled = true;
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}