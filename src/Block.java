import java.awt.*;
public class Block {
    double xpos;
    double ypos;
    int width;
    int height;
    Rectangle rectangle;
    boolean isDeadly = false;
    int xdirection =1;
    int ydirection=1;
    double dx =0;
    double dy=0;
    int xbounceleft = 0;
    int xbounceright = 0;
    int ybouncetop = 0;
    int ybouncebottom=0;
    String bouncedX = "";
    String bouncedY = "";
    double returndistanceX = 0;
    double returndistanceY = 0;


    boolean ismoving = false;
    public Block(){
        refreshRectangle();
    }
    void refreshRectangle(){
        rectangle = new Rectangle((int)xpos,(int)ypos,width,height);
    }
    void placeBlock(int param_xpos,int param_ypos,int param_width, int param_height){
        xpos = param_xpos;
        ypos = param_ypos;
        width = param_width;
        height = param_height;
        refreshRectangle();
    }
    void setinmotion(double param_dx, double param_dy, int param_xbounceleft, int param_xbounceright,int param_ybouncetop,int param_ybouncebottom){
        dx=param_dx;
        dy=param_dy;
        xbounceleft=param_xbounceleft;
        xbounceright=param_xbounceright;
        ybouncetop=param_ybouncetop;
        ybouncebottom=param_ybouncebottom;
        ismoving=true;
    }
    void move(){
        bouncedX = "";
        bouncedY = "";
        if(xpos+width+dx*xdirection>=xbounceright&&dx!=0){
            bouncedX = "right";
        } else if(xpos+dx*xdirection<=xbounceleft&&dx!=0){
            bouncedX = "left";
        }
        if(ypos+height+dy*ydirection>=ybouncebottom&&dy!=0){
            bouncedY="bottom";
        } else if(ypos+dy*ydirection<=ybouncetop&&dy!=0){
            bouncedY="top";
        }
        if(bouncedX.equals("right")){
            xpos+=dx*xdirection;
            returndistanceX=Math.abs(xpos+width-xbounceright);
            xpos=xbounceright-width;
            xdirection*=-1;
            xpos+=dx*(xdirection-returndistanceX);
        }
        if(bouncedX.equals("left")){
            xpos+=dx*xdirection;
            returndistanceX=Math.abs(xpos-xbounceleft);
            xpos=xbounceleft;
            xdirection*=-1;
            xpos+=dx*xdirection-returndistanceX;
        }
        if(bouncedY.equals("top")){
            ypos+=dy*ydirection;
            returndistanceY=Math.abs(ypos-ybouncetop);
            ypos=ybouncetop;
            ydirection*=-1;
            ypos+=dy*ydirection-returndistanceY;
        }
        if(bouncedY.equals("bottom")){
            ypos+=dy*ydirection;
            returndistanceY=Math.abs(ypos+height-ybouncebottom);
            ypos=ybouncebottom-height;
            ydirection*=-1;
            ypos+=dy*ydirection-returndistanceY;
        }
        if(bouncedX=="") {
            xpos += dx * xdirection;
        }
        if(bouncedY==""){
            ypos+=dy*ydirection;
        }
    }

}
