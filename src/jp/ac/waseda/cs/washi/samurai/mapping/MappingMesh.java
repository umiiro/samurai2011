/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.ac.waseda.cs.washi.samurai.mapping;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jp.ac.waseda.cs.washi.samurai.api.Chara;
import jp.ac.waseda.cs.washi.samurai.api.Direction;
import jp.ac.waseda.cs.washi.samurai.gamestate.GameState;
import jp.ac.waseda.cs.washi.samurai.gamestate.ImmutableGameState;
import jp.ac.waseda.cs.washi.samurai.playable.Dog;
import jp.ac.waseda.cs.washi.samurai.playable.Playable;
import jp.ac.waseda.cs.washi.samurai.playable.Samurai;

/**
 * 
 * @author kaito
 */
public class MappingMesh {

	public static final MappingMesh EMPTY = new MappingMesh();
	public static final Direction[] VALID_DIRECTIONS = new Direction[] {
		Direction.DOWN, Direction.LEFT, Direction.RIGHT, Direction.UP
	};
	public static final Direction INVALID_DIRECTION = Direction.UNKNOWN;

	private List<MappingField> fields = Collections.emptyList();
	private List<MappingNode> nodes = Collections.emptyList();
	private Map<Chara, Playable> playables = Collections.emptyMap();
	private int width, height;
	private GameState currentState = GameState.EMPTY;

	public Collection<MappingField> getFields() {
		return fields;
	}
	
	public MappingNode getNodeAt(int x, int y) {
		if (x < 1 || y < 1 || x > width || y > height) {
			return MappingWall.OUT_OF_FIELD;
		} else {
			return nodes.get(y * width + x);
		}
	}
	
	public GameState getCurrentState() {
		return currentState;
	}

	public int getHeight() {
		return height;
	}

	public Playable getPlayable(Chara chara) {
		return playables.get(chara);
	}

	public Collection<Playable> getPlayables() {
		return playables.values();
	}

	public int getWidth() {
		return width;
	}

	public void update(jp.ac.waseda.cs.washi.samurai.api.Map map) {
		if (nodes.size() == 0) {
			allocateMesh(map);
			connectFields();
			retriveChara(map);
		}

		currentState = new ImmutableGameState(this, currentState, map);
	}

	private void allocateMesh(jp.ac.waseda.cs.washi.samurai.api.Map map) {
		width = map.getWidth();
		height = map.getHeight();
		nodes = new ArrayList<MappingNode>(width * height);
		fields = new ArrayList<MappingField>();

		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				if (map.isWall(x, y)) {
					nodes.add(MappingWall.OUT_OF_FIELD);
				} else {
					MappingField field = new MappingField(x, y, this);
					nodes.add(field);
					fields.add(field);
				}
			}
		}

		nodes = Collections.unmodifiableList(nodes);
		fields = Collections.unmodifiableList(fields);
	}

	private void connectFields() {
		for (MappingField field : fields) {
			for (Direction d : VALID_DIRECTIONS) {
				int x = field.getX();
				int y = field.getY();

				MappingNode adj = getNodeAt(x + d.dx, y + d.dy);

				if (adj instanceof MappingField) {
					field.connect((MappingField) adj, d);
				}
			}

			field.freezeConnection();
		}
	}

	private void retriveChara(jp.ac.waseda.cs.washi.samurai.api.Map m) {
		ArrayList<Samurai> sams = new ArrayList<Samurai>();
		ArrayList<Dog> dogs = new ArrayList<Dog>();
		playables = new HashMap<Chara, Playable>();

		for (int i = 0; i < 4; i++) {
			Samurai s = new Samurai(this);
			Dog d = new Dog(this);
			s.setCollegue(d);
			d.setCollegue(s);
			sams.add(s);
			dogs.add(d);
			playables.put(m.getSamurai(i), s);
			playables.put(m.getDog(i), d);
		}

		for (Dog d : dogs) {
			for (Samurai s : sams) {
				if (s == d.getCollegue()) {
					continue;
				}
				d.addEnemy(s);
			}
		}

		for (Samurai s : sams) {
			for (Dog d : dogs) {
				if (d == s.getCollegue()) {
					continue;
				}
				s.addEnemy(d);
			}
		}
		playables = Collections.unmodifiableMap(playables);
	}
}
