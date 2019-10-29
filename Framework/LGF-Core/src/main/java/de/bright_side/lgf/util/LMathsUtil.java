package de.bright_side.lgf.util;

import java.util.ArrayList;
import java.util.List;

import de.bright_side.lgf.model.LPolygon;
import de.bright_side.lgf.model.LVector;

public class LMathsUtil {
    public static final double DEGREES_IN_CIRCLE = 360;

    public static LVector add(LVector vector, double x, double y){
        return new LVector(vector.x + x, vector.y + y);
    }

    public static LVector add(LVector vector, LVector moveAmount){
    	if (moveAmount == null){
    		return vector;
    	}
        return new LVector(vector.x + moveAmount.x, vector.y + moveAmount.y);
    }

    public static LVector add(LVector vector, double value){
        return new LVector(vector.x + value, vector.y + value);
    }

    public static LVector subtract(LVector vectorA, LVector vectorB) {
        return new LVector(vectorA.x - vectorB.x, vectorA.y - vectorB.y);
    }

    public static LVector average(LVector vectorA, LVector vectorB) {
        return new LVector((vectorA.x + vectorB.x) / 2.0, (vectorA.y + vectorB.y) / 2.0);
    }

    public static LVector round(LVector vector){
        if (vector == null){
            return null;
        }
        return new LVector(Math.round(vector.x), Math.round(vector.y));
    }

    public static LVector moveInDirection(LVector vector, double direction, double amount){
        double useDirection = direction - 180;
        if (useDirection < 0){
            useDirection += 360;
        }
        double angleInRadiants = (360-useDirection) * Math.PI / 180;
        double x = vector.x + (amount * Math.sin(angleInRadiants));
        double y = vector.y + (amount * Math.cos(angleInRadiants));

        return new LVector(x, y);
    }

    public static LVector multiply(LVector vector, double factor) {
        return new LVector(vector.x * factor, vector.y * factor);
    }

    public static LVector divide(LVector vector, double factor) {
    	return new LVector(vector.x / factor, vector.y / factor);
    }
    
    public static LVector multiply(LVector vectorA, LVector vectorB) {
        return new LVector(vectorA.x * vectorB.x, vectorA.y * vectorB.y);
    }

    public static LPolygon multiply(LPolygon polygon, LVector multiplyVector) {
        LPolygon result = new LPolygon();
        if (polygon.getPoints() == null){
            return result;
        }
        List<LVector> points = new ArrayList<>();
        result.setPoints(points);

        for (LVector i: polygon.getPoints()){
            points.add(multiply(i, multiplyVector));
        }

        return result;
    }

    public static LPolygon add(LPolygon polygon, LVector addVector) {
        LPolygon result = new LPolygon();
        if (polygon.getPoints() == null){
            return result;
        }
        List<LVector> points = new ArrayList<>();
        result.setPoints(points);

        for (LVector i: polygon.getPoints()){
            points.add(add(i, addVector));
        }

        return result;
    }

    public static LVector divide(LVector vectorA, LVector vectorB) {
    	if (vectorA == null){
    		throw new RuntimeException("vectorA is null");
    	}
    	if (vectorB == null){
    		throw new RuntimeException("vectorB is null");
    	}
        return new LVector(vectorA.x / vectorB.x, vectorA.y / vectorB.y);
    }

    public static boolean isInCenteredArea(LVector pos, LVector areaCenterPos, LVector areaSize) {
        LVector halfAreaSize = LMathsUtil.multiply(areaSize, 0.5);
        LVector negativeHalfAreaSize = LMathsUtil.multiply(areaSize, -0.5);
        LVector topLeft = LMathsUtil.add(areaCenterPos, negativeHalfAreaSize);
        LVector bottomRight = LMathsUtil.add(areaCenterPos, halfAreaSize);
        return ((pos.x >= topLeft.x) && (pos.x <= bottomRight.x)
                && (pos.y >= topLeft.y) && (pos.y <= bottomRight.y));
    }

    public static double rotate(double rotation, double amount) {
        double result = rotation + amount;
        while (result > DEGREES_IN_CIRCLE){
            result -= DEGREES_IN_CIRCLE;
        }
        while (result < 0){
            result += DEGREES_IN_CIRCLE;
        }
        return result;
    }

    public static LVector rotate(LVector point, LVector center, double angle) {
        double angleRad = (angle / 180) * Math.PI;
        double cosAngle = Math.cos(angleRad );
        double sinAngle = Math.sin(angleRad );
        double dx = (point.x - center.x);
        double dy = (point.y - center.y);

        double x = center.x + (dx*cosAngle-dy*sinAngle);
        double y = center.y + (dx*sinAngle+dy*cosAngle);
        return new LVector(x, y);
    }

    public static LPolygon rotate(LPolygon polygon, LVector center, double angle){
        LPolygon result = new LPolygon();
        List<LVector> points = new ArrayList<>();
        result.setPoints(points);
        for (LVector i: polygon.getPoints()){
            points.add(rotate(i, center, angle));
        }
        return  result;
    }

    public static boolean isIntersecting(LPolygon a, LPolygon b){
        for (int x=0; x<2; x++){
            LPolygon polygon = (x==0) ? a : b;

            for (int i1=0; i1<polygon.getPoints().size(); i1++){
                int   i2 = (i1 + 1) % polygon.getPoints().size();
                LVector p1 = polygon.getPoints().get(i1);
                LVector p2 = polygon.getPoints().get(i2);

                LVector normal = new LVector(p2.y - p1.y, p1.x - p2.x);

                double minA = Double.POSITIVE_INFINITY;
                double maxA = Double.NEGATIVE_INFINITY;

                for (LVector p : a.getPoints()) {
                    double projected = normal.x * p.x + normal.y * p.y;

                    if (projected < minA) {
                        minA = projected;
                    }
                    if (projected > maxA) {
                        maxA = projected;
                    }
                }

                double minB = Double.POSITIVE_INFINITY;
                double maxB = Double.NEGATIVE_INFINITY;

                for (LVector p : b.getPoints()) {
                    double projected = normal.x * p.x + normal.y * p.y;

                    if (projected < minB) {
                        minB = projected;
                    }
                    if (projected > maxB) {
                        maxB = projected;
                    }
                }

                if (maxA < minB || maxB < minA) {
                    return false;
                }
            }
        }

        return true;
    }

    public static double getDistance(LVector p1, LVector p2){
        return Math.sqrt((p1.x - p2.x) *  (p1.x - p2.x) + (p1.y - p2.y) * (p1.y - p2.y));
    }

    /**
     * @param p1 point 1
     * @param p2 point 2
     * @return the angle in degrees from point 1 to point 2
     */
    public static double getAngleInDegrees(LVector p1, LVector p2) {
        double xDiff = p2.x - p1.x;
        double yDiff = p2.y - p1.y;
        double result = Math.toDegrees(Math.atan2(yDiff, xDiff)) + 90;
        if (result < 0){
            result += 360;
        } else if (result > 360){
            result -= 360;
        }
        return result;
    }

    public static boolean isZero(LVector vector){
        if (vector == null){
            return false;
        }
        return (vector.x == 0) && (vector.y == 0);
    }

	public static LVector putInRange(LVector pos, LVector topLeft, LVector bottomRight) {
		double x = pos.x;
		double y = pos.y;
		boolean changed = false;
		
		if (x < topLeft.x) {
			x = topLeft.x;
			changed = true;
		} else if (x > bottomRight.x) {
			x = bottomRight.x;
			changed = true;
		}
		if (y < topLeft.y) {
			y = topLeft.y;
			changed = true;
		} else if (y > bottomRight.y) {
			y = bottomRight.y;
			changed = true;
		}

		if (!changed) {
			return pos;
		}
		return new LVector(x, y);
	}

}
