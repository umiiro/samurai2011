package jp.ac.waseda.cs.washi.samurai.insight;

import java.util.HashMap;
import java.util.Map;

import jp.ac.waseda.cs.washi.samurai.main.Headquater;
import jp.ac.waseda.cs.washi.samurai.playable.Playable;

public class InsightPrimaryPlayable extends Insight {
	public Map<Boolean, Map<Playable, Playable>> bests;
	public Map<Playable, Playable> primaryTable;
	
	
	public static InsightPrimaryPlayable require(Headquater hq) {
		return (InsightPrimaryPlayable)hq.registerInsight(new InsightPrimaryPlayable());
	}
	
	@Override
	public void init(Headquater hq) {
		super.init(hq);
		bests = new HashMap<Boolean, Map<Playable,Playable>>();
		bests.put(true, new HashMap<Playable, Playable>());
		bests.put(false, new HashMap<Playable, Playable>());
		primaryTable = new HashMap<Playable, Playable>();
	}
	
	public Playable getPrimaryEnemy(Playable p, boolean offensive) {
		Map<Playable, Playable> bestOf = bests.get(offensive);
		if (bestOf.containsKey(p)) {
			return bestOf.get(p);
		}
		
		Playable best = Playable.EMPTY;
 		int maxScore = -1;

		for (Playable ep : p.getEnemies()) {
			if (ep.isUnknown() || p.isOffensive(ep) != offensive)
				continue;

			int score = ep.getScore();
			if (score > maxScore) {
				maxScore = score;
				best = ep;
			}
		}
		
		bestOf.put(p, best);
		
		return best;
	}
	
	public Playable getPrimaryEnemy(Playable p) {
		if (primaryTable.containsKey(p)) {
			return primaryTable.get(p);
		}

		Playable best = Playable.EMPTY;
 		int maxScore = -1;

		for (Playable ep : p.getEnemies()) {
			if (ep.isUnknown())
				continue;

			int score = ep.getScore();
			if (score > maxScore) {
				maxScore = score;
				best = ep;
			}
		}
		
		primaryTable.put(p, best);
		
		return best;
	}

	@Override
	public void update() {
		bests.get(false).clear();
		bests.get(true).clear();
	}
}
