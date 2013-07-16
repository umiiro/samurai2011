package jp.ac.waseda.cs.washi.samurai.strategy;

import jp.ac.waseda.cs.washi.samurai.api.Direction;
import jp.ac.waseda.cs.washi.samurai.insight.InsightPrimaryPlayable;
import jp.ac.waseda.cs.washi.samurai.insight.InsightShortestPath;
import jp.ac.waseda.cs.washi.samurai.main.BallotBox;
import jp.ac.waseda.cs.washi.samurai.main.Headquater;
import jp.ac.waseda.cs.washi.samurai.personality.Personality;
import jp.ac.waseda.cs.washi.samurai.playable.Playable;

public class StrategyStalky extends Strategy {
	private InsightShortestPath sp;
	private InsightPrimaryPlayable pp;
	
	public static StrategyStalky require(Personality ps) {
		return (StrategyStalky)ps.requireStrategy(new StrategyStalky());
	}


	@Override
	public void init(Headquater hq) {
		sp = InsightShortestPath.require(hq);
		pp = InsightPrimaryPlayable.require(hq);
	}

	@Override
	public void vote(Playable p, BallotBox ballot) {
		Playable best = pp.getPrimaryEnemy(p);
		Direction d = sp.getShortestDirection(p, best);
		ballot.submitElement(d, 1d);
	}
}