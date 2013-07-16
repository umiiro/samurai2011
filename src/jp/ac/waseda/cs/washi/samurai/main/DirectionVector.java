package jp.ac.waseda.cs.washi.samurai.main;

import java.util.EnumMap;
import java.util.Map;

import jp.ac.waseda.cs.washi.samurai.api.Direction;

public class DirectionVector implements Cloneable {
	public static final double POSITIVE_ZERO = Double.MIN_NORMAL;

    protected Map<Direction, Double> box = new EnumMap<Direction, Double>(Direction.class);
    private boolean dirtySqlen = true, dirtyLen = true;

    private double sqlen, len;
    
    public DirectionVector() {
    	
    }
    
    public DirectionVector(Map<Direction, Double> map) {
    	box = new EnumMap<Direction, Double>(map);
    }
    
    public DirectionVector(DirectionVector other) {
    	this(other.box);
    }
    
    public void add(DirectionVector vector) {
    	for (Map.Entry<Direction, Double> e : box.entrySet()) {
    		addElement(e.getKey(), e.getValue());
    	}
    }
    
    public void addElement(Direction d, double value) {
    	onChange();
    	this.put(d, this.get(d) + value);
    }
    
    public void clear() {
    	onChange();
		box.clear();
	}

    public double get(Direction d) {
        return box.containsKey(d) ? box.get(d) : 0d;
    }
    
    public double getDotWith(DirectionVector other) {
    	double sum = 0d;
    	
    	for (Map.Entry<Direction, Double> e : box.entrySet()) {
    		sum += this.get(e.getKey()) * e.getValue();
    	}
    	
    	return sum;
    }
    
    public double getLength() {
        if (dirtyLen) {
            len = Math.sqrt(this.getSquaredLength());
        }
        return len;
    }

    public Direction getMaximum() {
		double maxValue = Double.NEGATIVE_INFINITY;
		Direction maxDirection = Direction.UNKNOWN;

		for (Map.Entry<Direction, Double> e : box.entrySet()) {
			if (maxValue < e.getValue()) {
				maxDirection = e.getKey();
				maxValue = e.getValue();
			}
		}
		
		return maxDirection;
	}

    public double getSquaredLength() {
        if (dirtySqlen) {
            sqlen = this.getDotWith(this);
            dirtySqlen = false;
        }
        return sqlen;
    }

    public boolean isZero() {
        return getSquaredLength() < POSITIVE_ZERO;
    }

    public void magnify(double value) {
    	for (Map.Entry<Direction, Double> e : box.entrySet()) {
    		magnifyElement(e.getKey(), value);
    	}
    }
    

    public void magnifyElement(Direction d, double value) {
    	onChange();
    	box.put(d, box.get(d) * value);
    }
	
	public void negate() {
    	for (Direction d : box.keySet()) {
    		negateElement(d);
    	}
    }
	
	public void negateElement(Direction d) {
    	onChange();
    	box.put(d, -box.get(d));
    }
	
    public void normalize() {
        if (!this.isZero()) {
            this.magnify(1 / getLength());
        }
    }
    
    public void put(Direction d, double value) {
    	onChange();
        box.put(d, value);
    }

    public void putAll(Map<Direction, Double> other) {
    	onChange();
    	box.putAll(other);
    }

    public void subtract(DirectionVector vector) {
    	for (Map.Entry<Direction, Double> e : box.entrySet()) {
    		addElement(e.getKey(), -e.getValue());
    	}
    }
    public void subtractElement(Direction d, double value) {
    	addElement(d, -value);
    }
    @Override
    public DirectionVector clone() {
        try {
            return (DirectionVector) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new InternalError(e.toString());
        }
    }
    
	@Override
	public String toString() {
		return box.toString();
	}
	
    private void onChange() {
        dirtySqlen = dirtyLen = true;
    }
}
