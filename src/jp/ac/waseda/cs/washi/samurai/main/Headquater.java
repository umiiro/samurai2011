package jp.ac.waseda.cs.washi.samurai.main;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import jp.ac.waseda.cs.washi.samurai.api.Chara;
import jp.ac.waseda.cs.washi.samurai.api.CharaType;
import jp.ac.waseda.cs.washi.samurai.api.Direction;
import jp.ac.waseda.cs.washi.samurai.insight.Insight;
import jp.ac.waseda.cs.washi.samurai.mapping.MappingMesh;
import jp.ac.waseda.cs.washi.samurai.personality.Personality;
import jp.ac.waseda.cs.washi.samurai.playable.Playable;

public class Headquater {

	private static Logger logger = Logger.getLogger(Headquater.class.getPackage().getName());
	private Map<CharaType, Personality> personalities = new EnumMap<CharaType, Personality>(CharaType.class);
	private MappingMesh mesh = new MappingMesh();
	private Map<String, Insight> insights = new HashMap<String, Insight>();

	public void addPersonality(Personality ps, CharaType ct) {
		ps.init(this);
		personalities.put(ct, ps);
	}
	
	public Insight registerInsight(Insight s) {
		String type = s.getClass().getName();
		if (insights.containsKey(type)) {
			return insights.get(type);
		} else {
			s.init(this);
			insights.put(type, s);
			return s;
		}
	}

	public MappingMesh getMappingMesh() {
		return mesh;
	}

	public String next(Chara c) {
		for (Insight in : insights.values()) {
			in.update();
		}
		
		Playable p = mesh.getPlayable(c);
		BallotBox ballot = new BallotBox();

		if (logger.isLoggable(Level.FINE)) {
			logger.fine("next for " + p);
		}

		ballot.clear();
		ballot.fair();
		Personality ps = personalities.get(c.getType());
		ps.vote(p, ballot);

		for (Insight in : insights.values()) {
			in.finalize();
		}

		return decideDirection(p, ballot);
	}

	public void updateMappingTree(jp.ac.waseda.cs.washi.samurai.api.Map map) {
		mesh.update(map);
	}

	private String decideDirection(Playable p, BallotBox ballot) {
		double maxValue = Double.NEGATIVE_INFINITY;
		Direction maxDirection = Direction.UNKNOWN;
		for (Direction d : p.getField().getDirections()) {
			if (ballot.get(d) > maxValue) {
				maxValue = ballot.get(d);
				maxDirection = d;
			}
		}

		if (logger.isLoggable(Level.FINEST)) {
			logger.finest("Ballot: " + ballot);
			logger.finest("Direction: " + maxDirection.name());
		}
		
		if (maxDirection == Direction.UNKNOWN)
			return "NONE";
		else
			return maxDirection.name();
	}
}
