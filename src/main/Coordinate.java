package main;

public class Coordinate{
        public int x;
        public int y;

    public Coordinate(int x, int y){
            this.x = x;
            this.y = y;
    }

    public void setX(int x){
    	this.x = x;
    }

    public void setY(int y){
    	this.x = y;
    }
    
    public int getX(){
    	return x;
    }
    
    public int getY(){
    	return y;
    }
}
