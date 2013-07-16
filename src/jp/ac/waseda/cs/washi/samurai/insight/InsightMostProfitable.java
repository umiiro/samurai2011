package jp.ac.waseda.cs.washi.samurai.insight;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

import jp.ac.waseda.cs.washi.samurai.api.BonusType;
import jp.ac.waseda.cs.washi.samurai.api.Direction;
import jp.ac.waseda.cs.washi.samurai.main.Headquater;
import jp.ac.waseda.cs.washi.samurai.mapping.MappingField;
import jp.ac.waseda.cs.washi.samurai.playable.Playable;

public class InsightMostProfitable extends Insight {

	public int maxDepth = 4;
	public int minDepth = 4;
	public Map<BonusType, Integer> profits;
	private List<Set<MappingField>> visited;
	private Map<Playable, Map<Direction, Integer>> scoreTable;

	public static InsightMostProfitable require(Headquater hq) {
		return (InsightMostProfitable)hq.registerInsight(new InsightMostProfitable());
	}

	@Override
	public void init(Headquater hq) {
		super.init(hq);
		profits = new EnumMap<BonusType, Integer>(BonusType.class);
		profits.put(BonusType.BIG, 10000);
		profits.put(BonusType.SHOGUN, 7500);
		profits.put(BonusType.SMALL, 100);
		profits.put(BonusType.NONE, 0);
		visited = new ArrayList<Set<MappingField>>(maxDepth);
		scoreTable = new HashMap<Playable, Map<Direction,Integer>>();
	}
	
	public Map<Direction,Integer> getScore(Playable p) {
		if (!scoreTable.containsKey(p)) {
			scoreTable.put(p, new EnumMap<Direction, Integer>(Direction.class));
			update();
		}
		
		return scoreTable.get(p);
	}
	
	public boolean isFailed(Playable p) {
		if (!scoreTable.containsKey(p)) {
			scoreTable.put(p, new EnumMap<Direction, Integer>(Direction.class));
			update();
		}
		
		for (int score : scoreTable.get(p).values()) {
			if (score != 0) {
				return false;
			}
		}

		return true;
	}
	
	@Override
	public void update() {
		long startTime = System.currentTimeMillis();
		
		for (Playable p : scoreTable.keySet()) {
			int i;
			for (i = minDepth; i <= maxDepth; i++) {
				if (findProfitable(p, i))
					break;
			}
		}
		
		if (logger.isLoggable(Level.FINE)) {
			logger.info("MostProfitable: " + (System.currentTimeMillis() - startTime) + "ms");
		}
	}

	private boolean findProfitable(Playable p, int depth) {
		int score = -1;
		MappingField f = p.getField();
		Map<Direction, Integer> scores = scoreTable.get(p);
		scores.clear();

		for (Map.Entry<Direction, Set<MappingField>> e : generateMasks(f)
				.entrySet()) {
			setVisited(depth, e.getValue());
			score = walk(f, depth);
			scores.put(e.getKey(), score);
		}
		
		return score >= 0;
	}

	private Map<Direction, Set<MappingField>> generateMasks(MappingField f) {
		EnumMap<Direction, Set<MappingField>> masks = new EnumMap<Direction, Set<MappingField>>(
				Direction.class);

		Collection<MappingField> adjs = f.getAdjacents();
		Map<Direction, MappingField> diradj = f.getDirAdjMap();

		for (Map.Entry<Direction, MappingField> e : diradj.entrySet()) {
			Set<MappingField> mask = new HashSet<MappingField>(adjs);
			mask.add(f);
			mask.remove(e.getValue());
			masks.put(e.getKey(), mask);
		}

		return masks;
	}

	private int walk(MappingField f, int depth) {
		if (depth == 0) {
			return 0;
		}

		int sum = evaluateField(f, depth);

		Set<MappingField> prev = getVisited(depth);
		Set<MappingField> curr = getVisited(depth - 1);

		for (MappingField adj : f.getAdjacents()) {
			curr.clear();
			curr.addAll(prev);
			if (curr.add(adj)) {
				sum += walk(adj, depth - 1);
			}
		}

		return sum;
	}

	private Set<MappingField> getVisited(int index) {
		ensureVisited(index);
		return visited.get(index);
	}
	
	private void setVisited(int index, Set<MappingField> value) {
		ensureVisited(index);
		visited.set(index, value);
	}
	
	private void ensureVisited(int index) {
		while (index >= visited.size()) {
			visited.add(new HashSet<MappingField>());
		}
	}

	private int evaluateField(MappingField f, int depth) {
		return profits.get(f.getBonus()) * depth * depth;
	}
}
