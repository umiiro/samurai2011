package jp.ac.waseda.cs.washi.samurai.strategy;

import java.util.logging.Level;

import jp.ac.waseda.cs.washi.samurai.api.Direction;
import jp.ac.waseda.cs.washi.samurai.insight.InsightNearest;
import jp.ac.waseda.cs.washi.samurai.insight.InsightShortestPath;
import jp.ac.waseda.cs.washi.samurai.main.BallotBox;
import jp.ac.waseda.cs.washi.samurai.main.Headquater;
import jp.ac.waseda.cs.washi.samurai.mapping.MappingField;
import jp.ac.waseda.cs.washi.samurai.personality.Personality;
import jp.ac.waseda.cs.washi.samurai.playable.Playable;

public class StrategyRemote extends Strategy {
	private InsightNearest nr;
	private InsightShortestPath sp;
	
	public static StrategyRemote require(Personality ps) {
		return (StrategyRemote)ps.requireStrategy(new StrategyRemote());
	}

	@Override
	public void init(Headquater hq) {
		nr = InsightNearest.require(hq);
		sp = InsightShortestPath.require(hq);
	}

	@Override
	public void vote(Playable p, BallotBox ballot) {
		MappingField bonusField = nr.getNearestBonus(p);
		Direction d = sp.getShortestDirection(p.getField(), bonusField);
		
		if (logger.isLoggable(Level.FINEST)) {
			logger.finest("Remote: " + d);
		}
		
		ballot.submitElement(d, 1d);
	}
}