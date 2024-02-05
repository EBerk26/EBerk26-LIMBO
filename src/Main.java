import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.awt.*;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.event.*;
public class Main implements Runnable,KeyListener {
    public int WIDTH = 1028;
    public int HEIGHT = 700;
    public JFrame JFrame;
    public Canvas Canvas;
    public JPanel JPanel;
    public BufferStrategy BufferStrategy;
    Player player = new Player();
    public void keyPressed(KeyEvent e){
        if(e.getKeyCode()==KeyEvent.VK_RIGHT){
            player.rightIsPressed = true;
        }
        if(e.getKeyCode()==KeyEvent.VK_LEFT){
            player.leftIsPressed = true;
        }
        if(e.getKeyCode()==KeyEvent.VK_UP){
            player.upIsPressed = true;
        }
    }
    public void keyTyped(KeyEvent e){

    }
    public void keyReleased(KeyEvent e){
        if(e.getKeyCode()==KeyEvent.VK_RIGHT){
            player.rightIsPressed = false;
        }
        if(e.getKeyCode()==KeyEvent.VK_LEFT){
            player.leftIsPressed = false;
        }
        if(e.getKeyCode()==KeyEvent.VK_UP){
            player.upIsPressed = false;
        }
    }
    public static void main(String[] args) {
        Main ex = new Main();
        new Thread(ex).start();
    }
    public Main(){
        setUpGraphics();
    }
    public void run(){
        while(true){
            moveThings();
            render();
            pause(16);
        }
    }
    public void moveThings(){
        player.handleMovement();
    }
    public void pause(int time){
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            }
        }
    private void setUpGraphics() {
        JFrame = new JFrame("Application Template");
        JPanel = (JPanel) JFrame.getContentPane();
        JPanel.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        JPanel.setLayout(null);
        Canvas = new Canvas();
        Canvas.setBounds(0, 0, WIDTH, HEIGHT);
        Canvas.setIgnoreRepaint(true);
        JPanel.add(Canvas);
        JFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JFrame.pack();
        JFrame.setResizable(false);
        JFrame.setVisible(true);
        Canvas.createBufferStrategy(2);
        BufferStrategy = Canvas.getBufferStrategy();
        Canvas.requestFocus();
        Canvas.addKeyListener(this);
    }
    private void render(){
        Graphics2D g = (Graphics2D) BufferStrategy.getDrawGraphics();
            background();
            g.fillRect((int)player.xpos,(int)player.ypos,player.width,player.height);
            g.dispose();
            BufferStrategy.show();
        }
    void background(){
        Graphics2D g = (Graphics2D) BufferStrategy.getDrawGraphics();
        g.clearRect(0,0,WIDTH,HEIGHT);
        g.drawLine(0,600+player.height,WIDTH,600+player.height);
    }
}

