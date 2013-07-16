package jp.ac.waseda.cs.washi.samurai.strategy;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

import jp.ac.waseda.cs.washi.samurai.api.Direction;
import jp.ac.waseda.cs.washi.samurai.insight.InsightDogend;
import jp.ac.waseda.cs.washi.samurai.insight.InsightShortestPath;
import jp.ac.waseda.cs.washi.samurai.main.BallotBox;
import jp.ac.waseda.cs.washi.samurai.main.DirectionVector;
import jp.ac.waseda.cs.washi.samurai.main.Headquater;
import jp.ac.waseda.cs.washi.samurai.mapping.MappingField;
import jp.ac.waseda.cs.washi.samurai.personality.Personality;
import jp.ac.waseda.cs.washi.samurai.playable.Playable;

public class StrategyCoward extends Strategy {

	private InsightShortestPath sp;
	private InsightDogend de;
	
	public static StrategyCoward require(Personality ps) {
		return (StrategyCoward)ps.requireStrategy(new StrategyCoward());
	}
	
	@Override
	public void init(Headquater hq) {
		sp = InsightShortestPath.require(hq);
		de = InsightDogend.require(hq);
	}

	@Override
	public void vote(Playable p, BallotBox ballot) {
		long startTime = System.currentTimeMillis();
		MappingField f = p.getField();
		
		Set<MappingField> mask = new HashSet<MappingField>();
		mask.add(f);

		for(Playable ep : p.getEnemies()) {
			MappingField ef = sp.getNextField(ep.getField(), f);
			mask.add(ep.getField());
			mask.add(ef);
		}
		
		DirectionVector dv = new DirectionVector();
		
		for (Map.Entry<Direction, MappingField> e : f.getDirAdjMap().entrySet()) {
			if (de.isDogend(e.getValue(), mask, 3)) {
				dv.addElement(e.getKey(), -1d);

				if (logger.isLoggable(Level.FINEST)) {
					logger.finest("Coward: " + e.getKey() + " is dogend");
				}
			}
		}
		
		//dv.normalize();
		ballot.submit(dv);
		
		if (logger.isLoggable(Level.FINE)) {
			logger.fine("Coward: " + (System.currentTimeMillis() - startTime) + "ms");
		}
	}
}
