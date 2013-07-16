package jp.ac.waseda.cs.washi.samurai.insight;

import java.util.Collection;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import jp.ac.waseda.cs.washi.samurai.api.BonusType;
import jp.ac.waseda.cs.washi.samurai.api.Direction;
import jp.ac.waseda.cs.washi.samurai.main.Headquater;
import jp.ac.waseda.cs.washi.samurai.mapping.MappingField;
import jp.ac.waseda.cs.washi.samurai.playable.Playable;

public class InsightNearest extends Insight {
	public static InsightNearest require(Headquater hq) {
		return (InsightNearest)hq.registerInsight(new InsightNearest());
	}
	
	public Map<BonusType, Integer> profits;
	private InsightShortestPath sp;

	
	@Override
	public void init(Headquater hq) {
		super.init(hq);
		sp = InsightShortestPath.require(hq);
		profits = new EnumMap<BonusType, Integer>(BonusType.class);
		profits.put(BonusType.BIG, 100);
		profits.put(BonusType.SHOGUN, 100);
		profits.put(BonusType.SMALL, 10);
		profits.put(BonusType.NONE, 0);
	}

	public MappingField getNearestBonus(Playable p) {
		MappingField pf = p.getField();
		int maxScore = 0;
		int minDistance = Integer.MAX_VALUE;
		MappingField bestField = MappingField.EMPTY;
		
		for (MappingField f : mesh.getFields()) {
			int distance = sp.getShortestDistance(pf, f);
			int score = profits.get(f.getBonus());

			if (score > maxScore || (score == maxScore && distance < minDistance)) {
				maxScore = score;
				minDistance = distance;
				bestField = f;
			}
		}
		
		return bestField;
	}

	public Playable getNearestEnemy(Playable p, boolean offensive) {
		int minDistance = Integer.MAX_VALUE;
		Playable nearestPlayable = Playable.EMPTY;

		for (Playable ep : p.getEnemies()) {
			if (ep.isUnknown() || p.isOffensive(ep) != offensive)
				continue;

			int dist = sp.getShortestDistance(p.getField(), ep.getField());
			if (dist < minDistance) {
				minDistance = dist;
				nearestPlayable = ep;
			}
		}

		return nearestPlayable;
	}

	@Override
	public void update() {
	}
}
