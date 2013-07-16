/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.ac.waseda.cs.washi.samurai.strategy;

import java.util.Map;
import java.util.logging.Level;

import jp.ac.waseda.cs.washi.samurai.api.Direction;
import jp.ac.waseda.cs.washi.samurai.insight.InsightShortestPath;
import jp.ac.waseda.cs.washi.samurai.main.BallotBox;
import jp.ac.waseda.cs.washi.samurai.main.DirectionVector;
import jp.ac.waseda.cs.washi.samurai.main.Headquater;
import jp.ac.waseda.cs.washi.samurai.mapping.MappingField;
import jp.ac.waseda.cs.washi.samurai.personality.Personality;
import jp.ac.waseda.cs.washi.samurai.playable.Playable;

/**
 * 
 * @author kaito
 */
public class StrategyBounce extends Strategy {
	private InsightShortestPath sp;

	public static StrategyBounce require(Personality ps) {
		return (StrategyBounce)ps.requireStrategy(new StrategyBounce());
	}

	@Override
	public void init(Headquater hq) {
		sp = InsightShortestPath.require(hq);
	}

	@Override
	public void vote(Playable p, BallotBox ballot) {
		long startTime = System.currentTimeMillis();
		DirectionVector dv = new DirectionVector();

		for (Map.Entry<Direction, MappingField> e : p.getField().getDirAdjMap().entrySet()) {
			double value = 0d;
			for (Playable ep : p.getEnemies()) {
				if (!p.isOffensive(ep) || ep.getField() == MappingField.EMPTY)
					continue;
				
				if (ep.getField() == e.getValue()) {
					ballot.submitElement(e.getKey(), -1d);
					continue;
				}

				Direction d = sp.getShortestDirection(ep.getField(), e.getValue());
				MappingField ef = ep.getField().getDirAdjMap().get(d);
				//MappingField ef = ep.getField();

				double distance = sp.getShortestDistance(e.getValue(), ef);
				value -= 1 / distance / distance;
			}

			dv.addElement(e.getKey(), value);
		}

		dv.normalize();
		ballot.submit(dv);
		if (logger.isLoggable(Level.FINE)) {
			logger.fine("Bounce: " + (System.currentTimeMillis() - startTime) + "ms");
		}
	}
}
