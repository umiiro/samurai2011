package jp.ac.waseda.cs.washi.samurai.gamestate;

import java.util.Collection;
import java.util.HashMap;

import jp.ac.waseda.cs.washi.samurai.api.BonusType;
import jp.ac.waseda.cs.washi.samurai.api.Chara;
import jp.ac.waseda.cs.washi.samurai.mapping.MappingField;
import jp.ac.waseda.cs.washi.samurai.mapping.MappingMesh;
import jp.ac.waseda.cs.washi.samurai.mapping.MappingNode;
import jp.ac.waseda.cs.washi.samurai.playable.Playable;

public class ImmutableGameState extends GameState implements Cloneable {
	
	public ImmutableGameState(MutableGameState state) {
		super(state.mesh, state);
		remainingTime = state.remainingTime;
		bonusMap.putAll(state.bonusMap);
		playableMap.putAll(state.playableMap);
	}
	
	public ImmutableGameState(MappingMesh mesh, GameState state, jp.ac.waseda.cs.washi.samurai.api.Map map) {
		super(mesh, state);
		remainingTime = map.getRemainingTime();
		bonusMap = genereteBonusMap(map);
		playableMap = generetePlayableMap(map);
	}
	
	protected ImmutableGameState() {
		super(null, null);
	}
	
	private HashMap<MappingField, BonusType> genereteBonusMap(jp.ac.waseda.cs.washi.samurai.api.Map map) {
		HashMap<MappingField, BonusType> bonusMap = new HashMap<MappingField, BonusType>();

		Collection<MappingField> fields = mesh.getFields();
		for (MappingField field : fields) {
			int x = field.getX();
			int y = field.getY();
			BonusType bonus = map.getBonus(x, y);
			bonusMap.put(field, bonus);
		}
		
		return bonusMap;
	}

	private HashMap<Playable, GameState.PlayableState> generetePlayableMap(jp.ac.waseda.cs.washi.samurai.api.Map map) {
		HashMap<Playable, GameState.PlayableState> playableMap = new HashMap<Playable, GameState.PlayableState>();
		
		for (Chara c : map.getAllCharas()) {
			Playable player = mesh.getPlayable(c);
			GameState.PlayableState pstate = new PlayableState();
			int x = c.getX();
			int y = c.getY();
			MappingNode node = mesh.getNodeAt(x, y);
			if (node instanceof MappingField) {
				pstate.field = (MappingField) node;
			} else {
				pstate.field = MappingField.EMPTY;
			}
			pstate.direction = c.getDirection();
			pstate.score = c.getScore();
			pstate.state = c.getState();
			pstate.statetime = c.getStateRemainingTime();
			playableMap.put(player, pstate);
		}
		
		return playableMap;
	}
}
