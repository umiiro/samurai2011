package jp.ac.waseda.cs.washi.samurai.strategy;

import java.util.Map;
import java.util.logging.Level;

import jp.ac.waseda.cs.washi.samurai.api.Direction;
import jp.ac.waseda.cs.washi.samurai.insight.InsightCrossing;
import jp.ac.waseda.cs.washi.samurai.insight.InsightDeadend;
import jp.ac.waseda.cs.washi.samurai.main.BallotBox;
import jp.ac.waseda.cs.washi.samurai.main.Headquater;
import jp.ac.waseda.cs.washi.samurai.mapping.MappingField;
import jp.ac.waseda.cs.washi.samurai.personality.Personality;
import jp.ac.waseda.cs.washi.samurai.playable.Playable;

public class StrategyEscape extends Strategy {
	private InsightCrossing cr;
	private InsightDeadend de;
	
	public static StrategyEscape require(Personality ps) {
		return (StrategyEscape)ps.requireStrategy(new StrategyEscape());
	}
	
	@Override
	public void init(Headquater hq) {
		cr = InsightCrossing.require(hq);
		de = InsightDeadend.require(hq);
	}

	@Override
	public void vote(Playable p, BallotBox ballot) {
		long startTime = System.currentTimeMillis();
		BallotBox bb = new BallotBox();
		BallotBox dd = new BallotBox();
		MappingField f = p.getField();
		Map<Direction, MappingField> diradj = f.getDirAdjMap();
		
		for (Map.Entry<Direction, MappingField> e : diradj.entrySet()) {
			if (cr.isCrossingOffensive(p, e.getKey())) {
				ballot.put(e.getKey(), -100d);
			} else {
				for (Playable pe : e.getValue().getPlayables()) {
					if (!pe.equals(p) && !pe.equals(p.getCollegue())) {
						ballot.put(e.getKey(), -100d);
					}
				}
			}
			dd.addElement(e.getKey(), de.getDeadendDistance(e.getValue()));
		}
		
		dd.normalize();
		dd.magnify(0.01d);
		ballot.submit(dd);

		if (logger.isLoggable(Level.FINE)) {
			logger.fine("Escape: " + (System.currentTimeMillis() - startTime) + "ms");
		}
	}

}
