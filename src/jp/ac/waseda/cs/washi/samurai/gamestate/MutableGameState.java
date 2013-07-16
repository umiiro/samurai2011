package jp.ac.waseda.cs.washi.samurai.gamestate;

import java.util.Map;

import jp.ac.waseda.cs.washi.samurai.api.BonusType;
import jp.ac.waseda.cs.washi.samurai.api.CharaState;
import jp.ac.waseda.cs.washi.samurai.api.Direction;
import jp.ac.waseda.cs.washi.samurai.mapping.MappingField;
import jp.ac.waseda.cs.washi.samurai.playable.Playable;

public class MutableGameState extends GameState {

	public MutableGameState(GameState state) {
		super(state.mesh, state);
		remainingTime = state.remainingTime;
		bonusMap.putAll(state.bonusMap);
		for (Map.Entry<Playable, PlayableState> e : state.playableMap.entrySet()) {
			playableMap.put(e.getKey(), e.getValue().clone());
		}
	}

	public void setRemainingTime(int remainingTime) {
		this.remainingTime = remainingTime;
	}
	
	public void setBonusAt(MappingField field, BonusType bonus) {
		bonusMap.put(field, bonus);
	}

	public void setFieldOf(Playable playable, MappingField field) {
		getPlayableStateOf(playable).field = field;
	}

	public void setDirectionOf(Playable playable, Direction direction) {
		getPlayableStateOf(playable).direction = direction;
	}

	public void setScoreOf(Playable playable, int score) {
		getPlayableStateOf(playable).score = score;
	}

	public void setStateOf(Playable playable, CharaState state) {
		getPlayableStateOf(playable).state = state;
	}

	public void setStateRemainingTimeOf(Playable playable, int value) {
		getPlayableStateOf(playable).statetime = value;
	}
}
