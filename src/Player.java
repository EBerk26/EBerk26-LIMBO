import java.awt.*;
public class Player {
    boolean allowWrapping = true;
    double xpos = 40;
    double ypos = 600;
    boolean inAir = false;
    boolean leftIsPressed;
    boolean rightIsPressed;
    boolean upIsPressed;
    double dx = 5.5;
    final double initialJumpPower = 15;
    double strengthOfGravity=0.76;
    double dy;
    boolean onGround = true;
    int width = 30;
    int height = 40;
    public Rectangle rectangle = new Rectangle((int)xpos,(int)ypos,width,height);

    void refreshRectangle(){
        rectangle = new Rectangle((int)xpos,(int)ypos,width,height);
    }
    Player(){

    }
    void reset(){
        xpos = 40;
        ypos = 600;
        inAir = false;
        dy = 0;
        onGround = true;
    }
    void handleMovement(){
        handleHorizontalMovement();
        handleJumping();
        handleLanding();
        refreshRectangle();
    }
    void handleHorizontalMovement(){
        if(rightIsPressed&!leftIsPressed){
            xpos+=dx;
        }
        if(leftIsPressed&!rightIsPressed){
            xpos-=dx;
        }
        if(xpos<0&&!allowWrapping){
            xpos=0;
        } else if (xpos<-width){
            xpos = -width;
        }
        if(xpos>1470-width&&!allowWrapping){
            xpos = 1470-width;
        }
    }
    void handleJumping(){
        if(onGround&&!inAir&&upIsPressed){
            dy = initialJumpPower;
            inAir = true;
            onGround = false;
        }
        if(inAir&&!upIsPressed&&dy>0){
            dy=0;
        }
        if (inAir&&!onGround){
            dy-=strengthOfGravity;
        }
        ypos-=dy;
    }
    void handleLanding(){
        if(ypos>=600&&!onGround){
            onGround = true;
            inAir = false;
            dy=0;
            ypos = 600;
        }
    }
    void fall(){
        if (inAir&&!onGround){
            dy-=strengthOfGravity;
        }
        ypos-=dy;
    }
    void teleport(double param_xpos,double param_ypos){
        xpos = param_xpos;
        ypos = param_ypos;
    }
}
