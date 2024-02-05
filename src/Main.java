import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.awt.*;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.event.*;
public class Main implements Runnable,KeyListener {
    final int WIDTH = 1028;
    final int HEIGHT = 700;
    public JFrame JFrame;
    public Canvas Canvas;
    public JPanel JPanel;
    public BufferStrategy BufferStrategy;
    public void keyPressed(KeyEvent e){

    }
    public void keyTyped(KeyEvent e){

    }
    public void keyReleased(KeyEvent e){

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
            g.dispose();
            BufferStrategy.show();
        }
    }
