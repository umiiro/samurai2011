/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.ac.waseda.cs.washi.samurai.playable;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

import jp.ac.waseda.cs.washi.samurai.api.CharaState;
import jp.ac.waseda.cs.washi.samurai.api.Direction;
import jp.ac.waseda.cs.washi.samurai.gamestate.GameState;
import jp.ac.waseda.cs.washi.samurai.mapping.MappingField;
import jp.ac.waseda.cs.washi.samurai.mapping.MappingMesh;

/**
 * 
 * @author kaito
 */
public abstract class Playable {

	public static final Playable EMPTY = new EmptyPlayable();
	private MappingMesh mesh;
	private GameState gameState;
	private Playable prototype;

	public Playable(MappingMesh mesh) {
		this.mesh = mesh;
		gameState = null;
		prototype = null;
	}
	
	public Playable(Playable prototype, GameState state) {
		this.gameState = state;
		this.prototype = prototype;
	}

	public boolean isEnemy(Playable p) {
		return getEnemies().contains(p);
	}

	public abstract Playable getCollegue();

	public abstract Collection<Playable> getEnemies();

	public abstract boolean isOffensive(Playable p);

	public boolean isUnknown() {
		return isUnknown(getGameState());
	}

	public boolean isUnknown(GameState state) {
		return getField(state) == MappingField.EMPTY;
	}

	public MappingField getField() {
		return getField(getGameState());
	}

	public MappingField getField(GameState state) {
		return state.getFieldOf(this);
	}

	public Direction getDirection() {
		return getDirection(getGameState());
	}

	public Direction getDirection(GameState state) {
		return state.getDirectionOf(this);
	}

	public int getScore() {
		return getScore(getGameState());
	}

	public int getScore(GameState state) {
		return state.getScoreOf(this);
	}

	public CharaState getState() {
		return getState(getGameState());
	}

	public CharaState getState(GameState state) {
		return state.getStateOf(this);
	}

	public int getStateRemainingTime() {
		return getStateRemainingTime(getGameState());
	}

	public int getStateRemainingTime(GameState state) {
		return state.getStateRemainingTimeOf(this);
	}

	private GameState getGameState() {
		if (mesh != null) {
			return mesh.getCurrentState();
		} else if (gameState != null) {
			return gameState;
		} else {
			return GameState.EMPTY;
		}
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof Playable))
			return false;
		
		Playable p = (Playable) obj;
		if (p.mesh != null) {
			return super.equals(obj);
		} else if (prototype != null) {
			return prototype.equals(obj);
		} else {
			return super.equals(obj);
		}
	}
	
	@Override
	public int hashCode() {
		if (mesh != null) {
			return super.hashCode();
		} else if (prototype != null) {
			return prototype.hashCode();
		} else {
			return super.hashCode();
		}
	}

	private static class EmptyPlayable extends Playable {
		private EmptyPlayable() {
			super(null);
		}

		@Override
		public boolean isEnemy(Playable p) {
			return false;
		}

		@Override
		public Playable getCollegue() {
			return this;
		}

		@Override
		public Set<Playable> getEnemies() {
			return Collections.emptySet();
		}

		@Override
		public boolean isOffensive(Playable p) {
			return false;
		}
	}
}
