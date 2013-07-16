package jp.ac.waseda.cs.washi.samurai.insight;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import jp.ac.waseda.cs.washi.samurai.api.BonusType;
import jp.ac.waseda.cs.washi.samurai.api.Direction;
import jp.ac.waseda.cs.washi.samurai.main.Headquater;
import jp.ac.waseda.cs.washi.samurai.mapping.MappingField;
import jp.ac.waseda.cs.washi.samurai.playable.Playable;

public class InsightCrossing extends Insight {
	private Map<Tuple, Boolean> crossing;
	private InsightDeadend de;
	
	public static InsightCrossing require(Headquater hq) {
		return (InsightCrossing)hq.registerInsight(new InsightCrossing());
	}
	
	@Override
	public void init(Headquater hq) {
		super.init(hq);
		crossing = new HashMap<InsightCrossing.Tuple, Boolean>();
		de = InsightDeadend.require(hq);
	}

	public boolean isCrossingOffensive(Playable play) {
		MappingField f = play.getField();
		Collection<Direction> dirs = f.getDirections();
		for (Direction d : dirs) {
			if (isCrossingOffensive(play, d))
				return true;
		}
		return false;
	}

	public boolean isCrossingOffensive(Playable play, Direction dir) {
		Tuple key = new Tuple();
		key.playable = play;
		key.direction = dir;
		
		if (crossing.containsKey(key)) {
			return crossing.get(key);
		}
		
		MappingField f = play.getField();
		Collection<MappingField> adjs1 = f.getAdjacents();
		MappingField next = f.getAdjacent(dir);
		Collection<MappingField> adjs2 = next.getAdjacents();

		for (Playable e : play.getEnemies()) {
			if (!play.isOffensive(e))
				continue;
			
			MappingField ef = e.getField();
			if (next.equals(ef) ||
					adjs2.contains(ef) ||
					(de.getDeadendDistance(next) == 0 && adjs1.contains(ef))) {
				if (next.getBonus() == BonusType.SHOGUN) {
					crossing.put(key, false);
					return false;
				} else {
					crossing.put(key, true);
					return true;
				}
			}
		}
		
		crossing.put(key, false);
		return false;
	}
	
	@Override
	public void finalize() {
		crossing.clear();
	}
	
	private static class Tuple {
		public Playable playable = Playable.EMPTY;
		public Direction direction = Direction.UNKNOWN;

		@Override
		public boolean equals(Object obj) {
			if (!(obj instanceof Tuple))
				return false;
			
			Tuple tup = (Tuple) obj;
			return direction == tup.direction &&
					playable.equals(tup.playable);
		}

		@Override
		public int hashCode() {
			return playable.hashCode() + direction.hashCode();
		}
	}
}
