import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

public class GameField extends JPanel implements ActionListener {
    private final int SIZE = 320;
    private final int DOT_SIZE = 16;
    private final int ALL_DOTS = 400;

    private Image dot;
    private Image apple;

    private final int[] x = new int[ALL_DOTS];
    private final int[] y = new int[ALL_DOTS];
    private int appleX;
    private int appleY;

    private int dots;

    private boolean left = false;
    private boolean right = true;
    private boolean up = false;
    private boolean down = false;

    private boolean inGame = true;

    public void loadImage(){
        ImageIcon iia = new ImageIcon("apple-red-icon.png");
        apple = iia.getImage();
        ImageIcon iid = new ImageIcon("dot.png");
        dot = iid.getImage();
    }

    public void createApple(){
        Random random = new Random();
        appleX = random.nextInt(20)*DOT_SIZE;//20*20=400 это размерность которую мы указали
        appleY = random.nextInt(20)*DOT_SIZE;//умножаем на дот_сайз чтобы получать координату
    }

    public void initGame(){
        dots = 3;
        for (int i = 0; i<dots; i++){
            y[i] = 48;
            x[i] = 48-i*DOT_SIZE;
        }
        Timer timer = new Timer(150, this);
        timer.start();
        createApple();
    }

    public void checkApple(){
        if (x[0]==appleX&&y[0]==appleY) {
            dots++;
            createApple();
        }
    }

    @Override
    protected void paintComponent(Graphics g){
        super.paintComponent(g);
        if (inGame){
            g.drawImage(apple, appleX, appleY, this);
            for (int i = 0; i < dots; i++) {
                g.drawImage(dot, x[i], y[i], this);
            }
        }else {
            String str = "Змейка сдохла";
            g.setColor(Color.CYAN);
            g.drawString(str, SIZE/4, SIZE/2);
        }
    }

    public void checkCollision(){
        for (int i = dots; i >0; i--) {
            if (x[0] == x[i] && y[0] == y[i]) {
                inGame = false;
                break;
            }
        }
        if (x[0]>SIZE)
            x[0] = 0;
        if (x[0]<0)
            x[0] = SIZE;
        if (y[0]>SIZE)
            y[0] = 0;
        if (y[0]<0)
            y[0]=SIZE;
    }

    @Override
    public void actionPerformed(ActionEvent e){
        if (inGame){
            checkApple();
            checkCollision();
            move();
        }
        repaint();
    }

    public GameField (){
        setBackground(Color.BLACK);
        loadImage();
        initGame();
        addKeyListener(new FiledKeyListener());
        setFocusable(true);
    }
    public void move(){
        for (int i = dots; i > 0; i--) {
            x[i]= x[i-1];
            y[i]= y[i-1];
        }
        if (left)
            x[0]-=DOT_SIZE;
        if (right)
            x[0]+=DOT_SIZE;
        if (up)
            y[0]-=DOT_SIZE;
        if (down)
            y[0]+=DOT_SIZE;
    }

    class FiledKeyListener extends KeyAdapter{
        @Override
        public void keyPressed (KeyEvent k){
            super.keyPressed(k);
            int key = k.getKeyCode();

            if (key == KeyEvent.VK_LEFT && !right){
                left = true;
                up = false;
                down = false;
            }
            if (key == KeyEvent.VK_RIGHT && !left){
                right = true;
                up = false;
                down = false;
            }
            if (key == KeyEvent.VK_UP && !down){
                right = false;
                left = false;
                up = true;
            }
            if (key == KeyEvent.VK_DOWN && !up){
                right = false;
                left = false;
                down = true;
            }
        }
    }
}
