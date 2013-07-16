/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.ac.waseda.cs.washi.samurai.mapping;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.ac.waseda.cs.washi.samurai.api.BonusType;
import jp.ac.waseda.cs.washi.samurai.api.Direction;
import jp.ac.waseda.cs.washi.samurai.gamestate.GameState;
import jp.ac.waseda.cs.washi.samurai.playable.Playable;

/**
 * 
 * @author kaito
 */
public class MappingField extends MappingNode {
	public static final MappingField EMPTY =
			new MappingField(-1, -1, MappingMesh.EMPTY) {{ freezeConnection(); }};

	private Map<MappingField, Direction> adjacentDirection;
	private Map<Direction, MappingField> directionAdjacent;
	private List<MappingField> adjacents;
	private List<Direction> directions;
	private int x, y;
	private MappingMesh mesh;

	public MappingField(int x, int y, MappingMesh mesh) {
		super(TYPE_FIELD);
		directionAdjacent = new EnumMap<Direction, MappingField>(Direction.class);
		adjacentDirection = new HashMap<MappingField, Direction>();
		adjacents = new ArrayList<MappingField>();
		directions = new ArrayList<Direction>();
		this.x = x;
		this.y = y;
		this.mesh = mesh;
	}

	public BonusType getBonus() {
		return mesh.getCurrentState().getBonusAt(this);
	}

	public BonusType getBonus(GameState state) {
		return state.getBonusAt(this);
	}

	public Collection<Playable> getPlayables() {
		return getPlayables(mesh.getCurrentState());
	}

	public Collection<Playable> getPlayables(GameState state) {
		ArrayList<Playable> list = new ArrayList<Playable>();

		for (Playable p : state.getPlayables()) {
			MappingField f = state.getFieldOf(p);
			if (f.equals(this)) {
				list.add(p);
			}
		}
		
		return list;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public Collection<MappingField> getAdjacents() {
		return adjacents;
	}

	public Map<MappingField, Direction> getAdjDirMap() {
		return adjacentDirection;
	}

	public Collection<Direction> getDirections() {
		return directions;
	}

	public Map<Direction, MappingField> getDirAdjMap() {
		return directionAdjacent;
	}
	
	public MappingField getAdjacent(Direction direction) {
		if (directionAdjacent.containsKey(direction)) {
			return directionAdjacent.get(direction);
		} else {
			return MappingField.EMPTY;
		}
	}

	@Override
	public String toString() {
		return "Field{X=" + x + ",Y=" + y + "}";
	}

	protected void connect(MappingField f, Direction d) {
		adjacentDirection.put(f, d);
		directionAdjacent.put(d, f);
		adjacents.add(f);
		directions.add(d);
	}

	protected void freezeConnection() {
		adjacentDirection = Collections.unmodifiableMap(adjacentDirection);
		directionAdjacent = Collections.unmodifiableMap(directionAdjacent);
		adjacents = Collections.unmodifiableList(adjacents);
		directions = Collections.unmodifiableList(directions);
	}
}
