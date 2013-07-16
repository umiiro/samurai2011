package jp.ac.waseda.cs.washi.samurai.main;

import java.util.HashMap;
import java.util.Map;

import jp.ac.waseda.cs.washi.samurai.api.Direction;
import jp.ac.waseda.cs.washi.samurai.playable.Playable;

public class BallotBox extends DirectionVector {
	private double weight;
	public BallotBox() {
		new HashMap<Direction, Double>();
	}
	
	public void submitElement(Direction d, double v)
	{
		this.addElement(d, v * weight);
	}

	public void submit(Map<Direction, Double> m)
	{
		for (Map.Entry<Direction, Double> e : m.entrySet()) {
			submitElement(e.getKey(), e.getValue());
		}
	}
	
	public void submit(DirectionVector other) {
		submit(other.box);
	}

	public void withWeight(double w)
	{
		weight = w;
	}

	public void fair()
	{
		weight = 1d;
	}
	
	public Direction getBestDirection(Playable p) {
		double maxValue = Double.NEGATIVE_INFINITY;
		Direction bestDirection = Direction.UNKNOWN;
		
		for (Direction d : p.getField().getDirections()) {
			if (get(d) > maxValue) {
				maxValue = get(d);
				bestDirection = d;
			}
		}

		return bestDirection;
	}
}
