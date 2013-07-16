package jp.ac.waseda.cs.washi.samurai.strategy;

import jp.ac.waseda.cs.washi.samurai.api.Direction;
import jp.ac.waseda.cs.washi.samurai.insight.InsightShortestPath;
import jp.ac.waseda.cs.washi.samurai.main.BallotBox;
import jp.ac.waseda.cs.washi.samurai.main.Headquater;
import jp.ac.waseda.cs.washi.samurai.personality.Personality;
import jp.ac.waseda.cs.washi.samurai.playable.Playable;

public class StrategyAcross extends Strategy {
	private InsightShortestPath sp;
	
	public static StrategyAcross require(Personality ps) {
		return (StrategyAcross)ps.requireStrategy(new StrategyAcross());
	}

	@Override
	public void init(Headquater hq) {
		sp = InsightShortestPath.require(hq);
	}

	@Override
	public void vote(Playable p, BallotBox ballot) {
		Direction d = sp.getShortestDirection(p, p.getCollegue());
		int distance = sp.getShortestDistance(p, p.getCollegue());
		if (distance > 0) {
			ballot.submitElement(d, 1d);
		} else {
			ballot.submitElement(Direction.UNKNOWN, 1d);
		}
	}
}