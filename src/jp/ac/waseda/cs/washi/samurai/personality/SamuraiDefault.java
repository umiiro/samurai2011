package jp.ac.waseda.cs.washi.samurai.personality;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Map;
import java.util.Queue;
import java.util.logging.Level;

import jp.ac.waseda.cs.washi.samurai.api.BonusType;
import jp.ac.waseda.cs.washi.samurai.api.CharaState;
import jp.ac.waseda.cs.washi.samurai.api.Direction;
import jp.ac.waseda.cs.washi.samurai.gamestate.GameState;
import jp.ac.waseda.cs.washi.samurai.insight.InsightCrossing;
import jp.ac.waseda.cs.washi.samurai.insight.InsightDeadend;
import jp.ac.waseda.cs.washi.samurai.insight.InsightLoop;
import jp.ac.waseda.cs.washi.samurai.insight.InsightMostProfitable;
import jp.ac.waseda.cs.washi.samurai.insight.InsightNearest;
import jp.ac.waseda.cs.washi.samurai.insight.InsightPrimaryPlayable;
import jp.ac.waseda.cs.washi.samurai.insight.InsightShortestPath;
import jp.ac.waseda.cs.washi.samurai.main.BallotBox;
import jp.ac.waseda.cs.washi.samurai.main.Headquater;
import jp.ac.waseda.cs.washi.samurai.mapping.MappingField;
import jp.ac.waseda.cs.washi.samurai.playable.Playable;
import jp.ac.waseda.cs.washi.samurai.strategy.StrategyAcross;
import jp.ac.waseda.cs.washi.samurai.strategy.StrategyBounce;
import jp.ac.waseda.cs.washi.samurai.strategy.StrategyCoward;
import jp.ac.waseda.cs.washi.samurai.strategy.StrategyEscape;
import jp.ac.waseda.cs.washi.samurai.strategy.StrategyGreedy;
import jp.ac.waseda.cs.washi.samurai.strategy.StrategyNoloop;
import jp.ac.waseda.cs.washi.samurai.strategy.StrategyRemote;
import jp.ac.waseda.cs.washi.samurai.strategy.StrategyRobber;
import jp.ac.waseda.cs.washi.samurai.strategy.StrategyStalky;

public class SamuraiDefault extends Personality {

	private InsightNearest nr;
	private InsightShortestPath sp;
	private InsightMostProfitable mp;
	private InsightDeadend de;
	private InsightCrossing cr;
	private InsightPrimaryPlayable pp;
	private InsightLoop lp;
	private StrategyBounce bounce;
	private StrategyCoward coward;
	private StrategyGreedy greedy;
	private StrategyRobber robber;
	private StrategyRemote remote;
	private StrategyAcross across;
	private StrategyEscape escape;
	private StrategyNoloop noloop;
	private StrategyStalky stalky;

	@Override
	public void init(Headquater headquater) {
		super.init(headquater);
		nr = InsightNearest.require(headquater);
		sp = InsightShortestPath.require(headquater);
		mp = InsightMostProfitable.require(headquater);
		de = InsightDeadend.require(headquater);
		cr = InsightCrossing.require(headquater);
		pp = InsightPrimaryPlayable.require(headquater);
		lp = InsightLoop.require(headquater);
		bounce = StrategyBounce.require(this);
		coward = StrategyCoward.require(this);
		greedy = StrategyGreedy.require(this);
		robber = StrategyRobber.require(this);
		remote = StrategyRemote.require(this);
		across = StrategyAcross.require(this);
		escape = StrategyEscape.require(this);
		noloop = StrategyNoloop.require(this);
		stalky = StrategyStalky.require(this);
	}

	@Override
	public void vote(Playable play, BallotBox ballot) {
		if (logger.isLoggable(Level.FINE)) {
			logger.fine("===== SamuraiDefault =====");
		}

		ballot.fair();

		CharaState cs = play.getState();
		int remtime = play.getStateRemainingTime();
		if (cs == CharaState.INVISIBLE && remtime > 1) {
			invisible(play, ballot);
		} else if (cs == CharaState.SHOGUN && remtime > 1) {
			shogun(play, ballot);
		} else {
			normal(play, ballot);
		}

		if (logger.isLoggable(Level.FINE)) {
			logger.fine("===== /SamuraiDefault =====");
		}
	}

	private void invisible(Playable play, BallotBox ballot) {
		remote.vote(play, ballot);
		escape.vote(play, ballot);
		noloop.vote(play, ballot);
	}

	private void shogun(Playable play, BallotBox ballot) {
		if (play.getCollegue().getScore() > play.getScore() / 10 &&
				mesh.getCurrentState().getRemainingTime() < 200) {
			Playable best = pp.getPrimaryEnemy(play.getCollegue(), false);
			Direction d = sp.getShortestDirection(play, best);
			ballot.submitElement(d, 1d);
		} else if (play.getStateRemainingTime() < 5) {
			if (nr.getNearestEnemy(play, false).getField().equals(play.getField())) {
				across.vote(play, ballot);
			} else if (cr.isCrossingOffensive(play)) {
				escape.vote(play, ballot);
				free(play, ballot);
			} else {
				if (pp.getPrimaryEnemy(play, false).getScore() > play
						.getScore() / 10) {
					robber.vote(play, ballot);
				} else {
					free(play, ballot);
				}
			}
		} else {
			mp.profits.put(BonusType.SHOGUN, 100);
			mp.profits.put(BonusType.BIG, 75);
			mp.profits.put(BonusType.SMALL, 10);
			free(play, ballot);
		}
	}

	private void normal(Playable play, BallotBox ballot) {
		if (cr.isCrossingOffensive(play)) {
			escape.vote(play, ballot);
			mp.profits.put(BonusType.SHOGUN, 100);
			mp.profits.put(BonusType.BIG, 0);
			mp.profits.put(BonusType.SMALL, 0);
			remote.vote(play, ballot);
			noloop.vote(play, ballot);
		} else {
			mp.profits.put(BonusType.SHOGUN, 100);
			mp.profits.put(BonusType.BIG, 75);
			mp.profits.put(BonusType.SMALL, 10);
			remote.vote(play, ballot);
			noloop.vote(play, ballot);
		}
	}

	private void free(Playable play, BallotBox ballot) {
		if (mp.isFailed(play)) {
			remote.vote(play, ballot);
		} else {
			greedy.vote(play, ballot);
		}
		noloop.vote(play, ballot);
	}
}
