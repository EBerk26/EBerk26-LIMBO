public class Player {
    double xpos = 40;
    double ypos = 600;
    boolean inAir = false;
    boolean leftIsPressed;
    boolean rightIsPressed;
    boolean upIsPressed;
    double dx = 10;
    final double initialJumpPower = 25*3/4;
    double strengthOfGravity=1.5;
    double dy;
    boolean onGround = true;
    int width = 40;
    int height = 40;
    Player(){

    }
    void handleMovement(){
        handleHorizontalMovement();
        handleJumping();
        handleLanding();
    }
    void handleHorizontalMovement(){
        if(rightIsPressed&!leftIsPressed){
            xpos+=dx;
        }
        if(leftIsPressed&!rightIsPressed){
            xpos-=dx;
        }
        if(xpos<0){
            xpos=0;
        }
        if(xpos>1028-width){
            xpos = 1028-width;
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
}
