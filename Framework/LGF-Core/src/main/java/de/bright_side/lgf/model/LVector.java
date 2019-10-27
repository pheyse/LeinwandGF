package de.bright_side.lgf.model;

public class LVector {
    /** x value, made public for higher performance*/
    public double x;
    /** y value, made public for higher performance*/
    public double y;

    public LVector(){
    }

    public LVector(double x, double y){
        this.x = x;
        this.y = y;
    }

    public LVector(int x, int y){
        this.x = x;
        this.y = y;
    }

    public LVector(int x, double y){
        this.x = x;
        this.y = y;
    }

    public LVector(double x, int y){
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return "LVector{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
