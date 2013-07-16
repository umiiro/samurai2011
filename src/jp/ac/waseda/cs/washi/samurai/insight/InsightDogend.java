package jp.ac.waseda.cs.washi.samurai.insight;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import jp.ac.waseda.cs.washi.samurai.main.Headquater;
import jp.ac.waseda.cs.washi.samurai.mapping.MappingField;

public class InsightDogend extends Insight {

	private Map<Tuple, Boolean> dogendTable;

	public static InsightDogend require(Headquater hq) {
		return (InsightDogend)hq.registerInsight(new InsightDogend());
	}
	@Override
	public void init(Headquater hq) {
		super.init(hq);
		dogendTable = new HashMap<Tuple, Boolean>();
	}

	public boolean isDogend(MappingField start, Set<MappingField> dogs,
			int depth) {
		Tuple key = new Tuple(start, dogs);

		if (!dogendTable.containsKey(key)) {
			dogendTable.put(key, walk(start, dogs, depth));
		}

		return dogendTable.get(key);
	}

	private boolean walk(MappingField start, Set<MappingField> prohibited,
			int depth) {
		if (depth == 0) {
			return false;
		}

		prohibited.add(start);
		HashSet<MappingField> open = new HashSet<MappingField>();
		open.addAll(start.getAdjacents());
		open.removeAll(prohibited);
		if (open.isEmpty()) {
			return true;
		}

		for (MappingField f : open) {
			if (walk(f, prohibited, depth - 1)) {
				return true;
			}
		}

		return false;
	}

	private class Tuple {
		public MappingField start;
		public Set<MappingField> dogs;

		public Tuple(MappingField s, Set<MappingField> d) {
			start = s;
			dogs = d;
		}

		@Override
		public boolean equals(Object obj) {
			if (!(obj instanceof Tuple))
				return false;
			
			Tuple tup = (Tuple) obj;
			return (start == null ? tup.start == null : start.equals(tup.start))
					&& (dogs == null ? tup.dogs == null : dogs.equals(tup.dogs));
		}

		@Override
		public int hashCode() {
			int hashCode = 1;
			hashCode = 31 * hashCode + (start == null ? 0 : start.hashCode());
			hashCode = 31 * hashCode + (dogs == null ? 0 : dogs.hashCode());
			return hashCode;
		}
	}
}
