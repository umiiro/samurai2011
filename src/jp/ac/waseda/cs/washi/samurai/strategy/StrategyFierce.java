package jp.ac.waseda.cs.washi.samurai.strategy;

import jp.ac.waseda.cs.washi.samurai.api.Direction;
import jp.ac.waseda.cs.washi.samurai.insight.InsightNearest;
import jp.ac.waseda.cs.washi.samurai.insight.InsightShortestPath;
import jp.ac.waseda.cs.washi.samurai.main.BallotBox;
import jp.ac.waseda.cs.washi.samurai.main.Headquater;
import jp.ac.waseda.cs.washi.samurai.personality.Personality;
import jp.ac.waseda.cs.washi.samurai.playable.Playable;

public class StrategyFierce extends Strategy {
	private InsightShortestPath sp;
	private InsightNearest nr;
	
	public static StrategyFierce require(Personality ps) {
		return (StrategyFierce)ps.requireStrategy(new StrategyFierce());
	}

	@Override
	public void init(Headquater hq) {
		nr = InsightNearest.require(hq);
		sp = InsightShortestPath.require(hq);
	}

	@Override
	public void vote(Playable p, BallotBox ballot) {
		Playable nearest = nr.getNearestEnemy(p, false);
		Direction d;
		
		d = sp.getShortestDirection(p.getField(), nearest.getField());
		
		ballot.submitElement(d, 1d);
	}
}