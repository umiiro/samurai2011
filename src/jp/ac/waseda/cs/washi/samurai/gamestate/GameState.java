package jp.ac.waseda.cs.washi.samurai.gamestate;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import jp.ac.waseda.cs.washi.samurai.api.BonusType;
import jp.ac.waseda.cs.washi.samurai.api.CharaState;
import jp.ac.waseda.cs.washi.samurai.api.Direction;
import jp.ac.waseda.cs.washi.samurai.mapping.MappingField;
import jp.ac.waseda.cs.washi.samurai.mapping.MappingMesh;
import jp.ac.waseda.cs.washi.samurai.playable.Playable;

public abstract class GameState implements Cloneable {
	public static final GameState EMPTY = new ImmutableGameState() {{ prevState = this; }};
	
	protected MappingMesh mesh;
	protected Map<MappingField, BonusType> bonusMap = new HashMap<MappingField, BonusType>();
	protected Map<Playable, PlayableState> playableMap = new HashMap<Playable, GameState.PlayableState>();
	protected int remainingTime = Integer.MAX_VALUE;
	protected GameState prevState;
	
	public GameState(MappingMesh mesh, GameState prevState) {
		this.mesh = mesh;
		this.prevState = prevState;
	}
	
	public GameState getPrevState() {
		return prevState;
	}

	public MappingMesh getMesh() {
		return mesh;
	}

	public int getRemainingTime() {
		return remainingTime;
	}
	
	public BonusType getBonusAt(MappingField field) {
		if (bonusMap.containsKey(field)) {
			return bonusMap.get(field);
		} else {
			return BonusType.NONE;
		}
	}

	public Collection<Playable> getPlayables() {
		return playableMap.keySet();
	}
	
	public MappingField getFieldOf(Playable playable) {
		return getPlayableStateOf(playable).field;
	}

	public Direction getDirectionOf(Playable playable) {
		return getPlayableStateOf(playable).direction;
	}

	public int getScoreOf(Playable playable) {
		return getPlayableStateOf(playable).score;
	}

	public CharaState getStateOf(Playable playable) {
		return getPlayableStateOf(playable).state;
	}
	
	public int getStateRemainingTimeOf(Playable playable) {
		return getPlayableStateOf(playable).statetime;
	}
	
	protected PlayableState getPlayableStateOf(Playable playable) {
		PlayableState pstate;
		if (!playableMap.containsKey(playable)) {
			pstate = new PlayableState();
			playableMap.put(playable, pstate);
		} else {
			pstate = playableMap.get(playable);
		}
		return pstate;
	}
	
	@Override
	public GameState clone() {
		try {
			GameState state = (GameState) super.clone();
			
			state.bonusMap = new HashMap<MappingField, BonusType>(bonusMap);
			
			state.playableMap = new HashMap<Playable, PlayableState>(playableMap.size());
			for (Map.Entry<Playable, PlayableState> e : playableMap.entrySet()) {
				state.playableMap.put(e.getKey(), e.getValue().clone());
			}

			return state;
		} catch (CloneNotSupportedException e) {
			throw new InternalError(e.toString());
		}
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof GameState) {
			GameState other = (GameState) obj;
			return remainingTime == other.remainingTime &&
					mesh.equals(other.mesh) &&
					bonusMap.equals(other.bonusMap) &&
					playableMap.equals(other.playableMap);
		} else {
			return false;
		}
	}
	
	@Override
	public int hashCode() {
		return remainingTime +
				mesh.hashCode() +
				bonusMap.hashCode() +
				playableMap.hashCode();
	}
	
	protected static class PlayableState implements Cloneable {
		public MappingField field = MappingField.EMPTY;
		public Direction direction = Direction.UNKNOWN;
		public Integer score = 0;
		public CharaState state = CharaState.NORMAL;
		public Integer statetime = 0;
		
		@Override
		protected PlayableState clone() {
			try {
				return (PlayableState) super.clone();
			} catch (CloneNotSupportedException e) {
				throw new InternalError(e.toString());
			}
		}
		
		@Override
		public boolean equals(Object obj) {
			if (obj instanceof PlayableState) {
				PlayableState other = (PlayableState) obj;
				return score == other.score &&
						statetime == other.statetime &&
						state == other.state &&
						direction == other.direction &&
						field.equals(other.field);
			} else {
				return false;
			}
		}
		
		@Override
		public int hashCode() {
			return Arrays.hashCode(new int[] { score, statetime })
					+ direction.hashCode()
					+ state.hashCode()
					+ field.hashCode();
		}
	}
}
