package jp.ac.waseda.cs.washi.samurai.insight;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

import jp.ac.waseda.cs.washi.samurai.api.Direction;
import jp.ac.waseda.cs.washi.samurai.main.Headquater;
import jp.ac.waseda.cs.washi.samurai.mapping.MappingField;
import jp.ac.waseda.cs.washi.samurai.playable.Playable;

public class InsightShortestPath extends Insight {
	public static InsightShortestPath require(Headquater hq) {
	return (InsightShortestPath)hq.registerInsight(new InsightShortestPath());
}
	private class Node implements Comparable<Node> {
		int distance = defaultDistance;
		Node from = null;

		final MappingField field;

		int heuristic = 0;

		Node(MappingField f) {
			field = f;
		}

		@Override
		public int compareTo(Node o) {
			return Double.compare(getCost(), o.getCost());
		}

		int getCost() {
			return distance + heuristic;
		}
		
		Collection<MappingField> getAdjacents() {
			return field.getAdjacents();
		}
		
		Map<MappingField, Direction> getAdjDirMap() {
			return field.getAdjDirMap();
		}
	}

	public int defaultDistance = Integer.MAX_VALUE;
	public Direction defaultDirection = Direction.UNKNOWN;
	private Map<List<MappingField>, Direction> directions = Collections.emptyMap();
	private Map<List<MappingField>, Integer> distances = Collections.emptyMap();
	private Map<List<MappingField>, MappingField> nextFields = Collections.emptyMap();

	private Map<MappingField, Node> nodes = Collections.emptyMap();
	private PriorityQueue<Node> open;
	private Set<Node> closed;

	@Override
	public void update() {
		if (nodes.isEmpty()) {
			directions = new HashMap<List<MappingField>, Direction>();
			distances = new HashMap<List<MappingField>, Integer>();
			nextFields = new HashMap<List<MappingField>, MappingField>();
			open = new PriorityQueue<Node>();
			closed = new HashSet<Node>();
			nodes = new HashMap<MappingField, Node>();
			for (MappingField f : mesh.getFields()) {
				nodes.put(f, new Node(f));
			}
			nodes = Collections.unmodifiableMap(nodes);
		}
	}
	
	public MappingField getNextField(MappingField from, MappingField to) {
		if (from == to)
			return null;

		List<MappingField> key = generateKey(from, to);

		if (!nextFields.containsKey(key)) {
			shortestPath(from, to);
		}

		return nextFields.containsKey(key) ? nextFields.get(key) : null;
	}
	
	public Direction getShortestDirection(MappingField from, MappingField to) {
		if (from == to || from == MappingField.EMPTY || to == MappingField.EMPTY)
			return defaultDirection;

		List<MappingField> key = generateKey(from, to);

		if (!directions.containsKey(key)) {
			shortestPath(from, to);
		}

		return directions.containsKey(key) ? directions.get(key) : defaultDirection;
	}
	
	public Direction getShortestDirection(Playable from, Playable to) {
		return getShortestDirection(from.getField(), to.getField());
	}

	public int getShortestDistance(MappingField from, MappingField to) {
		if (from == to)
			return 0;
		
		if (from == MappingField.EMPTY || to == MappingField.EMPTY)
			return defaultDistance;

		List<MappingField> key = generateKey(from, to);

		if (!distances.containsKey(key)) {
			shortestPath(from, to);
		}

		return distances.containsKey(key) ? distances.get(key) : defaultDistance;
	}
	
	public int getShortestDistance(Playable from, Playable to) {
		return getShortestDistance(from.getField(), to.getField());
	}

	private List<MappingField> generateKey(MappingField from, MappingField to) {
		return Collections.unmodifiableList(Arrays.asList(from, to));
	}

	private void shortestPath(MappingField from, MappingField to) {
		if (nodes.isEmpty() || from == to)
			return;

		List<MappingField> key = generateKey(from, to);
		if (from == to) {
			directions.put(key, defaultDirection);
			distances.put(key, 0);
		}

		Node start = nodes.get(from);
		start.distance = 0;
		start.heuristic = 0;
		start.from = null;

		open.clear();
		open.add(start);
		closed.clear();

		while (!open.isEmpty()) {
			Node closingNode = open.poll();

			closed.add(closingNode);
			open.remove(closingNode);

			int adjacentDistance = closingNode.distance + 1;
			Collection<MappingField> adjs = closingNode.getAdjacents();

			for (MappingField f : adjs) {
				Node node = nodes.get(f);

				if (closed.contains(node)) continue;
				
				if (open.add(node)) {
					int diffX = to.getX() - f.getX();
					int diffY = to.getY() - f.getY();
					int heuristic = 0;
					heuristic += diffX >= 0 ? diffX : -diffX;
					heuristic += diffY >= 0 ? diffY : -diffY;
					node.heuristic = heuristic;
					node.distance = defaultDistance;
				}
				
				if (node.distance > adjacentDistance) {
					node.distance = adjacentDistance;
					node.from = closingNode;
				}
			}
		}

		Node curr = nodes.get(to);

		int count = 1;
		while (curr.from != start) {
			curr = curr.from;
			count++;
		}

		nextFields.put(key, curr.field);
		directions.put(key, start.getAdjDirMap().get(curr.field));
		distances.put(key, count);
	}
}
