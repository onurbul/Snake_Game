import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;
public class SnakeGame extends JPanel implements ActionListener, KeyListener {

    //we created a private class which will keep track of all the x and y positions for each tile
    private class Tile {
        int x;
        int y;

        Tile(int x, int y){
            this.x = x;
            this.y = y;
        }
    }
    int boardWidth;
    int boardHeight;
    int tilesize = 25;

    //Snake
    Tile snakehead;
    ArrayList<Tile> snakebody;

    //Food
    Tile food;
    Random random;

    //Game logic
    Timer gameloop;
    int velocityX;
    int velocityY;
    boolean gameover = false;

    SnakeGame(int boardWidth, int boardHeight){
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        setPreferredSize(new Dimension(this.boardWidth, this.boardHeight));
        setBackground(Color.black);
        addKeyListener(this);
        setFocusable(true);

        snakehead = new Tile(5,5);
        snakebody = new ArrayList<Tile>();

        food = new Tile(10, 10);

        random = new Random();
        placeFood();

        velocityX = 0;
        velocityY = 0;

        gameloop = new Timer(100, this);
        gameloop.start();
    }

    public void paintComponent(Graphics g) {    //Graphics is used for drawing
        super.paintComponent(g);
        draw(g);

    }

    private void draw(Graphics g) {
        //Grid
        //g.setColor(Color.WHITE);
        for (int i = 0; i <= boardWidth / tilesize; i++) {
            g.drawLine(i * tilesize, 0, i * tilesize, boardHeight);
            g.drawLine(0, i * tilesize, boardWidth, i * tilesize);
        }

        //Food
        g.setColor(Color.RED);
        g.fill3DRect(food.x * tilesize, food.y * tilesize, tilesize, tilesize,true);

        //Snake Head
        g.setColor(Color.GREEN);
        g.fill3DRect(snakehead.x * tilesize, snakehead.y * tilesize, tilesize, tilesize, true);

        //Snake Body
        for(int i = 0; i < snakebody.size(); i++) {
            Tile snakePart = snakebody.get(i);
            g.fill3DRect(snakePart.x  * tilesize , snakePart.y * tilesize, tilesize, tilesize, true);
        }

        //Score
        g.setFont(new Font("Arial", Font.PLAIN, 16));
        if(gameover) {
            g.setColor(Color.red);
            g.drawString("Game Over : " + String.valueOf(snakebody.size()), tilesize - 16, tilesize);
        }
        else {
            g.drawString("Score : " + String.valueOf(snakebody.size()), tilesize - 16, tilesize);
        }
    }

    public void placeFood() {
        food.x = random.nextInt(boardWidth/tilesize); //600/25 = 24
        food.y = random.nextInt(boardHeight/tilesize);
    }

    public boolean collision(Tile tile1, Tile tile2) {
        return tile1.x == tile2.x && tile1.y == tile2.y;
    }

    public void move() {
        //eat food
        if(collision(snakehead, food)) {
            snakebody.add(new Tile(food.x, food.y));
            placeFood();
        }

        //SnakeBody
        for(int i = snakebody.size()-1; i >= 0 ; i--) {
            Tile snakepart = snakebody.get(i);
            if(i == 0) {
                snakepart.x = snakehead.x;
                snakepart.y = snakehead.y;
            }
            else {
                Tile prevSnakePart = snakebody.get(i-1);
                snakepart.x = prevSnakePart.x;
                snakepart.y = prevSnakePart.y;
            }
        }

        //SnakeHead
        snakehead.x += velocityX;
        snakehead.y += velocityY;

        //Game Over conditions
        for(int i = 0; i < snakebody.size(); i++) {
            Tile snakepart = snakebody.get(i);
            //Collision with the snakehead
            if(collision(snakehead, snakepart)) {
                gameover = true;
            }
        }

        if(snakehead.x * tilesize < 0 || snakehead.x * tilesize > boardWidth
                || snakehead.y * tilesize < 0 || snakehead.y * tilesize > boardHeight) {
            gameover = true;
        }

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
        if(gameover) {
            gameloop.stop();
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_UP && velocityY != 1) {
            velocityX = 0;
            velocityY = -1;
        }
        else if(e.getKeyCode() == KeyEvent.VK_DOWN && velocityY != -1) {
            velocityX = 0;
            velocityY = 1;
        }
        else if(e.getKeyCode() == KeyEvent.VK_LEFT && velocityX != 1) {
            velocityX = -1;
            velocityY = 0;
        }
        else if(e.getKeyCode() == KeyEvent.VK_RIGHT && velocityX != -1 ) {
            velocityX = 1;
            velocityY = 0;
        }
    }

    //do not need this method
    @Override
    public void keyTyped(KeyEvent e) {

    }

    //do not need this method
    @Override
    public void keyReleased(KeyEvent e) {

    }
}
