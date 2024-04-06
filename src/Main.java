import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.awt.*;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.tools.Tool;
import java.awt.event.*;
import java.awt.event.MouseListener;

public class Main implements Runnable,KeyListener,MouseListener {
    boolean isPlaying = false;
    int numberOfStartLevels = 8;
    int mouseX,mouseY;
    boolean isstartscreen = true;
    Image startlevels[] = new Image[5];
    @Override
    public void mouseClicked(MouseEvent e) {
        if(isPlaying) {
            player.teleport(e.getX(), e.getY() - player.height);
            player.dy = 0;
            System.out.println("("+e.getX()+","+e.getY()+")");
        } else{
            mouseX = e.getX();
            mouseY = e.getY();
        }
    }
    @Override
    public void mousePressed(MouseEvent e) {
    }
    Image startScreen;

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    public int WIDTH = 1470;
    public int HEIGHT = 880;
    public JFrame JFrame;
    public Canvas Canvas;
    public JPanel JPanel;
    boolean gameStarted = false;
    public BufferStrategy BufferStrategy;
    boolean touchingBlock;
    Player player = new Player();
    Block[] blockArray = new Block[10];
    int level = 1;
    int startlevel = 1;
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
        for(int x = 1;x<=4;x++){
            startlevels[x] = Toolkit.getDefaultToolkit().getImage("startLevel"+x+".png");
        }
        Image startLevel7 = Toolkit.getDefaultToolkit().getImage("startLevel7.png");
        setUpGraphics();
        startScreen = Toolkit.getDefaultToolkit().getImage("Limbo Start Screen.png");
    }
    public void run(){
        while(!isPlaying) {
            render();
            if(mouseX>=629&&mouseX<=841&&mouseY>634){
                isstartscreen=false;
                isPlaying = true;
            }
        }
        for(int x =0;x< blockArray.length;x++){
            blockArray[x] = new Block();
        }
        if(isPlaying) {
            while (true) {
                if(level==1+numberOfStartLevels) {
                    blockArray[0].placeBlock(1000, 520, 400, 50);
                    blockArray[1].placeBlock(800, 449, 30, 20);
                    blockArray[2].placeBlock(686, 387, 30, 20);
                    blockArray[3].placeBlock(686, 267, 200, 20);
                    blockArray[4].placeBlock(926, 179, 50, 10);
                    blockArray[5].placeBlock(1063, 126, 41, 24);
                }
                moveThings();
                render();
                pause(16);
            }
        }
    }
    boolean leftRightAlignment(int blocknumber){
        return(player.xpos+player.width>=1+blockArray[blocknumber].xpos&&player.xpos+1<=blockArray[blocknumber].xpos+blockArray[blocknumber].width);
    }
    public void moveThings() {
        touchingBlock = false;
        if(level==1&&!gameStarted){
            player.teleport(-player.width, 600+ player.height);
            gameStarted = true;
        }
        player.handleMovement();
        for(int x =0;x<blockArray.length;x++){
            if(player.rectangle.intersects(blockArray[x].rectangle)&&blockArray[x].isDeadly){
                player.reset();
                break;
            }
            if(blockArray[x].ismoving&&((player.rectangle.intersects(blockArray[x].rectangle)&&player.ypos+player.height<=blockArray[x].ypos+Math.abs(blockArray[x].dy)+1-player.dy&&leftRightAlignment(x))||(leftRightAlignment(x)&&player.ypos+player.height==blockArray[x].ypos))){
                if(player.ypos>=blockArray[x].ypos+blockArray[x].height){
                    player.dy=0;
                    break;
                } else {
                    player.ypos = blockArray[x].ypos - player.height + 1;
                    player.ypos += blockArray[x].dy * blockArray[x].ydirection;
                }
            }
            if(blockArray[x].ismoving){
                blockArray[x].move();
                blockArray[x].refreshRectangle();
            }
            if(player.xpos+player.width>=blockArray[x].xpos&&player.xpos<=blockArray[x].xpos+blockArray[x].width&&player.ypos<=blockArray[x].ypos+blockArray[x].height&&player.ypos+player.height>=blockArray[x].ypos){
                touchingBlock = true;
            }
            if(player.rectangle.intersects(blockArray[x].rectangle)){
                if (player.xpos+player.width<=blockArray[x].xpos+player.dx){
                    player.xpos = blockArray[x].xpos-player.width;
                } if (player.xpos>=blockArray[x].xpos+blockArray[x].width-player.dx){
                    player.xpos = blockArray[x].xpos+blockArray[x].width;
                } //sides of block
                if(player.dy<0&&leftRightAlignment(x)&&player.ypos+player.height<=blockArray[x].ypos+blockArray[x].dy-player.dy+1){
                    player.dy = 0;
                    player.inAir = false;
                    player.onGround = true;
                    player.ypos = blockArray[x].ypos- player.height;
                } //hit top of block while falling
                if (leftRightAlignment(x)&&player.ypos+player.height+3+Math.abs(blockArray[x].dy)>blockArray[x].ypos+blockArray[x].height&&player.dy>0){
                    player.ypos = blockArray[x].ypos+blockArray[x].height-blockArray[x].dy;
                    player.dy=0;
                } //bottom of block
            }
        }
        if(!touchingBlock&&!(player.ypos + player.height==640)&&!player.inAir){
            player.inAir = true;
            player.onGround = false;
            player.dy=0;
            player.fall();
        }
        if(level<=numberOfStartLevels&&player.xpos>=WIDTH){
            level++;
            if(level==1+numberOfStartLevels){
                player.allowWrapping = false;

            }
            player.teleport(-player.width,600);

        }
        if(player.ypos+player.height<0||startlevel>level&&level>numberOfStartLevels){
            level++;
            if(startlevel>level){
                level=startlevel;
            }
            for (Block block : blockArray) {
                block.placeBlock(10000, 2, 2, 2);
                block.isDeadly = false;
                block.setinmotion(0, 0, 0, 0, 0, 0);
                block.ismoving = false;
            }
            player.reset();
            if (level==2+numberOfStartLevels){
                blockArray[1].placeBlock(239,640-120,20,120);
                blockArray[1].isDeadly = true;
                blockArray[2].placeBlock(1079,640-60,120,60);
                blockArray[2].isDeadly = true;
                blockArray[2].setinmotion(2.5,0,400,1206,-100,5000);
                blockArray[3].placeBlock(1470-40,521,40,10);
                blockArray[4].placeBlock(1470-50,521-100,50,10);
                blockArray[5].placeBlock(1470-60,521-200,60,10);
                blockArray[6].placeBlock(1114-31,224+23,200,40);
                blockArray[7].placeBlock(1114-31-70,224+23,70,40);
                blockArray[7].setinmotion(0,-4,0,0,-10,224+23+40);
            }
            if(level==3+numberOfStartLevels){
                blockArray[1].placeBlock(140,644,200,100);
                blockArray[1].setinmotion(0,3,-100,2000,220,750);
            }
        }
    }
    public void pause(int time){
        try {
            Thread.sleep(time);
        } catch (InterruptedException ignored) {
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
        JFrame.setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
        JFrame.pack();
        JFrame.setResizable(false);
        JFrame.setVisible(true);
        Canvas.createBufferStrategy(2);
        BufferStrategy = Canvas.getBufferStrategy();
        Canvas.requestFocus();
        Canvas.addKeyListener(this);
        Canvas.addMouseListener(this);
    }
    private void render(){
        Graphics2D g = (Graphics2D) BufferStrategy.getDrawGraphics();
        if(isstartscreen){
            g.drawImage(startScreen,0,0,WIDTH,HEIGHT,null);
        }
        if(!isstartscreen) {
            background();
            if(level==1){
                g.drawImage(startlevels[1],0,0,WIDTH,HEIGHT,null);
            } else if (level ==2){

            } else if (level==3){

            } else if (level ==4){

            } else if (level==7){

            }
            for (int x = 0; x <= blockArray.length - 1; x++) {
                if (!blockArray[x].isDeadly) {
                    g.setColor(Color.ORANGE);
                    g.fillRect((int) blockArray[x].xpos, (int) blockArray[x].ypos, blockArray[x].width, blockArray[x].height);
                }
                if (blockArray[x].isDeadly) {
                    g.setColor(Color.RED);
                    g.fillRect((int) blockArray[x].xpos, (int) blockArray[x].ypos, blockArray[x].width, blockArray[x].height);
                }
            }
            g.setColor(Color.BLACK);
            g.fillRect((int) player.xpos, (int) player.ypos, player.width, player.height);
        }
            g.dispose();
            BufferStrategy.show();
        }
    void background(){
        Graphics2D g = (Graphics2D) BufferStrategy.getDrawGraphics();
        g.clearRect(0,0,WIDTH,HEIGHT);
        g.drawLine(0,600+player.height,WIDTH,600+player.height);
    }
}