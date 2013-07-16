package jp.ac.waseda.cs.washi.samurai.strategy;

import java.util.Map;
import java.util.logging.Level;

import jp.ac.waseda.cs.washi.samurai.api.Direction;
import jp.ac.waseda.cs.washi.samurai.insight.InsightMostProfitable;
import jp.ac.waseda.cs.washi.samurai.main.BallotBox;
import jp.ac.waseda.cs.washi.samurai.main.DirectionVector;
import jp.ac.waseda.cs.washi.samurai.main.Headquater;
import jp.ac.waseda.cs.washi.samurai.personality.Personality;
import jp.ac.waseda.cs.washi.samurai.playable.Playable;

public class StrategyGreedy extends Strategy {
	private InsightMostProfitable mp;
	
	public static StrategyGreedy require(Personality ps) {
		return (StrategyGreedy)ps.requireStrategy(new StrategyGreedy());
	}

	@Override
	public void init(Headquater hq) {
		mp = InsightMostProfitable.require(hq);
	}

	@Override
	public void vote(Playable p, BallotBox ballot) {
		long startTime = System.currentTimeMillis();
		DirectionVector dv = new DirectionVector();

		for (Map.Entry<Direction,Integer> e : mp.getScore(p).entrySet()) {
			dv.put(e.getKey(), (double)e.getValue());
		}
		
		dv.normalize();
		ballot.submit(dv);
		
		if (logger.isLoggable(Level.FINEST)) {
			logger.finest("Greedy: " + dv);
		}

		if (logger.isLoggable(Level.FINE)) {
			logger.fine("Greedy: " + (System.currentTimeMillis() - startTime) + "ms");
		}
	}
}