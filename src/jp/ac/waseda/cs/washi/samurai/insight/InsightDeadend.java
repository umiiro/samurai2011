package jp.ac.waseda.cs.washi.samurai.insight;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import jp.ac.waseda.cs.washi.samurai.main.Headquater;
import jp.ac.waseda.cs.washi.samurai.mapping.MappingField;

public class InsightDeadend extends Insight {

	private class ComputationThread extends Thread {
		public int maxDistance;
		private Set<MappingField> next = new HashSet<MappingField>();
		private Set<MappingField> open = new HashSet<MappingField>();
		private Set<MappingField> visited = new HashSet<MappingField>();
		private Map<MappingField, Integer> workingMap, freezedMap;
		
		public ComputationThread() {
			Collection<MappingField> fields = mesh.getFields();
			int numFields = fields.size();
			open = new HashSet<MappingField>();
			next = new HashSet<MappingField>();
			visited = new HashSet<MappingField>(numFields);
			workingMap = new HashMap<MappingField, Integer>(numFields);
			maxDistance = numFields * numFields;
		}
		
		public Map<MappingField, Integer> getMap() {
			return freezedMap;
		}
		
		@Override
		public void run() {
			long startTime = System.currentTimeMillis();
			
			for (MappingField f : mesh.getFields()) {
				workingMap.put(f, findDeadend(f));
			}
			
			freezedMap = Collections.unmodifiableMap(workingMap);
			logger.info("Deadend freeze: " + (System.currentTimeMillis() - startTime) + "ms");
		}
		
		private int findDeadend(MappingField start) {
			open.clear();
			open.add(start);

			visited.clear();

			for (int i = 0; i < maxDistance; i++) {
				next.clear();

				for (MappingField f : open) {
					Collection<MappingField> adj = f.getAdjacents();

					if (adj.size() == 1) {
						return i;
					}

					visited.add(f);
					
					next.addAll(adj);
				}

				next.removeAll(visited);
				
				Set<MappingField> swap;
				swap = open;
				open = next;
				next = swap;
			}		

			return Integer.MAX_VALUE;
		}
	}
	
	public static final int defaultDistance = Integer.MAX_VALUE;
	public static InsightDeadend require(Headquater hq) {
		return (InsightDeadend)hq.registerInsight(new InsightDeadend());
	}
	
	private ComputationThread computationThread = null;

	private Map<MappingField, Integer> map = Collections.emptyMap();

	public int getDeadendDistance(MappingField f) {
		return map.containsKey(f) ? map.get(f) : defaultDistance;
	}
	
	@Override
	public void update() {
		if (map.isEmpty()) {
			if (computationThread == null) {
				computationThread = new ComputationThread();
				computationThread.start();
			} else if (!computationThread.isAlive()) {
				map = computationThread.getMap();
				computationThread = null;
			}
		}
	}
}
