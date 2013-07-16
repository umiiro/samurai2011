package jp.ac.waseda.cs.washi.samurai.personality;

import java.util.logging.Level;

import jp.ac.waseda.cs.washi.samurai.gamestate.GameState;
import jp.ac.waseda.cs.washi.samurai.insight.InsightCrossing;
import jp.ac.waseda.cs.washi.samurai.insight.InsightNearest;
import jp.ac.waseda.cs.washi.samurai.insight.InsightPrimaryPlayable;
import jp.ac.waseda.cs.washi.samurai.insight.InsightShortestPath;
import jp.ac.waseda.cs.washi.samurai.main.BallotBox;
import jp.ac.waseda.cs.washi.samurai.main.Headquater;
import jp.ac.waseda.cs.washi.samurai.playable.Playable;
import jp.ac.waseda.cs.washi.samurai.strategy.StrategyAcross;
import jp.ac.waseda.cs.washi.samurai.strategy.StrategyEscape;
import jp.ac.waseda.cs.washi.samurai.strategy.StrategyFierce;
import jp.ac.waseda.cs.washi.samurai.strategy.StrategyRobber;
import jp.ac.waseda.cs.washi.samurai.strategy.StrategyStalky;

public class DogDefault extends Personality {
	private StrategyAcross across;
	private StrategyFierce fierce;
	private StrategyRobber robber;
	private StrategyEscape escape;
	private StrategyStalky stalky;
	private InsightCrossing cr;
	private InsightNearest nr;
	private InsightShortestPath sp;
	private InsightPrimaryPlayable pp;

	@Override
	public void init(Headquater headquater) {
		super.init(headquater);
		cr = InsightCrossing.require(headquater);
		nr = InsightNearest.require(headquater);
		sp = InsightShortestPath.require(headquater);
		pp = InsightPrimaryPlayable.require(headquater);
		across = StrategyAcross.require(this);
		fierce = StrategyFierce.require(this);
		robber = StrategyRobber.require(this);
		escape = StrategyEscape.require(this);
		stalky = StrategyStalky.require(this);
	}
	
	@Override
	public void vote(Playable p, BallotBox ballot) {
		if (logger.isLoggable(Level.FINE)) {
			logger.info("===== DogDefault =====");
		}
		
		Playable c = p.getCollegue();
		if (p.getScore() > c.getScore() / 10) {
			withscore(p, ballot);
		} else {
			noscore(p, ballot);
		}
		
		if (logger.isLoggable(Level.FINE)) {
			logger.info("===== /DogDefault =====");
		}
	}

	private void withscore(Playable p, BallotBox ballot) {
		if (cr.isCrossingOffensive(p)) {
			escape.vote(p, ballot);
		} else if (mesh.getCurrentState().getRemainingTime() < 200) {
			across.vote(p, ballot);
		} else if (sp.getShortestDistance(p, nr.getNearestEnemy(p, false)) < 3) {
			fierce.vote(p, ballot);
		} else {
			across.vote(p, ballot);
		}
	}
	
	private void noscore(Playable p, BallotBox ballot) {
		Playable c = p.getCollegue();
		Playable ce = nr.getNearestEnemy(c, true);
		GameState prev = mesh.getCurrentState().getPrevState();
		if (sp.getShortestDistance(c, ce) <= 3 && sp.getShortestDistance(p, p.getCollegue()) < 5) {
			across.vote(p, ballot);
		} else if (c.getField(prev).equals(ce.getField(prev))) {
			across.vote(p, ballot);
		} else if (pp.getPrimaryEnemy(p).getScore() > p.getScore() * 3.0 / 4.0) {
			stalky.vote(p, ballot);
		} else {
			across.vote(p, ballot);
		}
	}
}
