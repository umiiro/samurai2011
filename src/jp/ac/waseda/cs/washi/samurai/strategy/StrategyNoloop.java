package jp.ac.waseda.cs.washi.samurai.strategy;

import java.util.Collection;

import jp.ac.waseda.cs.washi.samurai.api.Direction;
import jp.ac.waseda.cs.washi.samurai.insight.InsightLoop;
import jp.ac.waseda.cs.washi.samurai.insight.InsightShortestPath;
import jp.ac.waseda.cs.washi.samurai.main.BallotBox;
import jp.ac.waseda.cs.washi.samurai.main.Headquater;
import jp.ac.waseda.cs.washi.samurai.mapping.MappingField;
import jp.ac.waseda.cs.washi.samurai.personality.Personality;
import jp.ac.waseda.cs.washi.samurai.playable.Playable;

public class StrategyNoloop extends Strategy {
	private InsightLoop lp;
	
	public static StrategyNoloop require(Personality ps) {
		return (StrategyNoloop)ps.requireStrategy(new StrategyNoloop());
	}

	@Override
	public void init(Headquater headquater) {
		lp = InsightLoop.require(headquater);
	}

	@Override
	public void vote(Playable play, BallotBox ballot) {
		MappingField field = play.getField();
		Collection<Direction> dirs = field.getDirections();
		for (Direction dir : dirs) {
			if (lp.isLoop(play, dir, 15)) {
				ballot.put(dir, -0.01d);
			}
		}
	}
}