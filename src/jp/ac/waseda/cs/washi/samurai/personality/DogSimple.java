package jp.ac.waseda.cs.washi.samurai.personality;

import jp.ac.waseda.cs.washi.samurai.api.Direction;
import jp.ac.waseda.cs.washi.samurai.insight.InsightShortestPath;
import jp.ac.waseda.cs.washi.samurai.main.BallotBox;
import jp.ac.waseda.cs.washi.samurai.main.Headquater;
import jp.ac.waseda.cs.washi.samurai.playable.Playable;

public class DogSimple extends Personality {
	private InsightShortestPath sp;
	private Playable target;

	@Override
	public void init(Headquater hq) {
		super.init(hq);
		sp = InsightShortestPath.require(hq);
	}
	
	@Override
	public void vote(Playable p, BallotBox ballot) {
		Direction dir = sp.getShortestDirection(p, target);
		ballot.submitElement(dir, 1d);
	}
	
	public void setTarget(Playable p) {
		target = p;
	}
}
