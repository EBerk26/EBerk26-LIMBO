import java.awt.Graphics2D;
import java.awt.image.BufferStrategy;
import java.awt.*;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.event.*;
import java.awt.event.MouseListener;

public class Main implements Runnable,KeyListener,MouseListener {
    boolean isPlaying = false;
    int numberOfStartLevels = 8;
    int mouseX,mouseY;
    boolean isstartscreen = true;
    Image level10;
    Image[] startlevels = new Image[5];
    Image level9_1;
    Image level9_2;
    Image level11;
    boolean level9_changeImage = false;
    boolean level11_image = false;
    @Override
    public void mouseClicked(MouseEvent e) {
        if(isPlaying) {
            player.teleport(e.getX(), e.getY() - player.height);
            player.dy = 0;
            System.out.println("Mouse: ("+e.getX()+","+e.getY()+")");
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
    boolean hasDied = false;
    public Canvas Canvas;
    public JPanel JPanel;
    Image startLevel7;
    boolean gameStarted = false;
    public BufferStrategy BufferStrategy;
    boolean touchingBlock;
    Player player = new Player();
    int floorLevel = 600+ player.height;
    Block[] blockArray = new Block[10];
    int level = 1;
    int startlevel = 12;
    boolean onHorizontalMovingBlock = false;
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
        level9_1 = Toolkit.getDefaultToolkit().getImage("It's not all empty.png");
        level9_2 = Toolkit.getDefaultToolkit().getImage("Do I go up?.png");
        level10 = Toolkit.getDefaultToolkit().getImage("Even death is no escape.png");
        startLevel7 = Toolkit.getDefaultToolkit().getImage("startLevel7.png");
        level11 = Toolkit.getDefaultToolkit().getImage("There are problems with this reality.png");
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
            level=startlevel;
            if(startlevel>=9){
                player.allowWrapping = false;
                setUpLevels();
            }
            while (true) {
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
        System.out.println("Player: ("+player.xpos+", "+player.ypos+player.height+")");
        touchingBlock = false;
        onHorizontalMovingBlock = false;
        if(level==1&&!gameStarted){
            player.teleport(-player.width, 600+ player.height);
            gameStarted = true;
        }
        player.handleMovement();
        if(level ==9&&player.xpos+player.width>=WIDTH){
            level9_changeImage = true;
        }
        for(int x =0;x<blockArray.length;x++){
            if(blockArray[x].ismoving){
                blockArray[x].move();
                blockArray[x].refreshRectangle();
            }
            if(player.rectangle.intersects(blockArray[x].rectangle)&&blockArray[x].isDeadly){
                hasDied = true;
                player.reset();
                for(int y =0;y<blockArray.length;y++) {
                    if (blockArray[y].ismoving & y > x) {
                        blockArray[y].move();
                        blockArray[y].refreshRectangle();
                    }
                }
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
                if(player.upIsPressed){
                    player.ypos=blockArray[x].ypos-player.height;
                }
            }
            if(!onHorizontalMovingBlock){
                player.dx = Player.originalDx;
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
                setUpLevels();
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
            setUpLevels();
        }
    }
    void setUpLevels(){
        if(level==1+numberOfStartLevels) {
            blockArray[0].placeBlock(1000, 520, 400, 50);
            blockArray[1].placeBlock(800, 449, 30, 20);
            blockArray[2].placeBlock(686, 387, 30, 20);
            blockArray[3].placeBlock(686, 267, 200, 20);
            blockArray[4].placeBlock(926, 179, 50, 10);
            blockArray[5].placeBlock(1063, 126, 41, 24);
        }
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
            blockArray[1].placeBlock(132,117,325-132,90);
            blockArray[1].setinmotion(0,3,-100,2000,-2,floorLevel);
            blockArray[2].placeBlock(325,117-30,30,120);
            blockArray[2].setinmotion(0,3,-100,2000,-32,floorLevel);
            blockArray[2].isDeadly = true;
            blockArray[3].placeBlock(132,117-30,30,120);
            blockArray[3].setinmotion(0,3,-100,2000,-32,floorLevel);
            blockArray[3].isDeadly = true;
        }
        if(level==12){
            blockArray[1].placeBlock(132,560,200,640-560);
            blockArray[1].setinmotion(5,0,132,WIDTH-132,0,0);
            blockArray[1].isDeadly = true;
            blockArray[2].placeBlock(1386,300,WIDTH-1386,73);
            blockArray[2].setinmotion(0,4.7,0,0,300,floorLevel);
            blockArray[3].placeBlock(1386,300+73-20,WIDTH-1386,20);
            blockArray[3].setinmotion(0,4.7,0,0,300+73-20,floorLevel);
            blockArray[3].isDeadly = true;
            blockArray[4].placeBlock(1300-50,180,50,50);
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
                g.drawLine(0,600+player.height,WIDTH,600+player.height);
            } else if (level ==2){
                g.drawImage(startlevels[2],0,0,WIDTH,HEIGHT,null);
                g.drawLine(0,600+player.height,WIDTH,600+player.height);
            } else if (level==3){
                g.drawImage(startlevels[3],0,0,WIDTH,HEIGHT,null);
                g.drawLine(0,600+player.height,WIDTH,600+player.height);
            } else if (level ==4){
                g.drawImage(startlevels[4],0,0,WIDTH,HEIGHT,null);
                g.drawLine(0,600+player.height,WIDTH,600+player.height);
            } else if (level==7){
                g.drawImage(startLevel7,0,0,WIDTH,HEIGHT,null);
                g.drawLine(0,600+player.height,WIDTH,600+player.height);
            } else if (level ==9){
                if(!level9_changeImage) {
                    g.drawImage(level9_1, 164, 78, 502-164, 275-78, null);
                } else {
                    g.drawImage(level9_2,1167, 225, 1405-1167, 357 - 225, null);
                }
            } else if (level==10){
                if(hasDied){
                    g.drawImage(level10,95,116,405-95,315-116,null);
                }
            } else if (level==11){
                if(player.ypos<400) {
                    level11_image = true;
                }
                if(level11_image){
                    g.drawImage(level11,960,131,1299-960,372-131,null);
                }
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