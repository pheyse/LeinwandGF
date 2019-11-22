package de.bright_side.lgf.model;

public class LVector implements Comparable<LVector>{
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(x);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(y);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LVector other = (LVector) obj;
		if (Double.doubleToLongBits(x) != Double.doubleToLongBits(other.x))
			return false;
		if (Double.doubleToLongBits(y) != Double.doubleToLongBits(other.y))
			return false;
		return true;
	}

	@Override
	public int compareTo(LVector other) {
		if (other == null) {
			return 1;
		}
		if (x > other.x) {
			return 1;
		}
		if (x < other.x) {
			return -1;
		}
		if (y > other.y) {
			return 1;
		}
		if (y < other.y) {
			return -1;
		}
		return 0;
	}

	
	
}
