/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.ac.waseda.cs.washi.samurai.playable;

import java.util.ArrayList;
import java.util.Collection;

import jp.ac.waseda.cs.washi.samurai.api.CharaState;
import jp.ac.waseda.cs.washi.samurai.gamestate.GameState;
import jp.ac.waseda.cs.washi.samurai.mapping.MappingMesh;

/**
 * 
 * @author kaito
 */
public class Dog extends Playable {

	private Playable collegue;
	private Collection<Playable> enemies = new ArrayList<Playable>();

	public Dog(MappingMesh mesh) {
		super(mesh);
	}

	public Dog(Playable prototype, GameState state) {
		super(prototype, state);
	}

	@Override
	public boolean isOffensive(Playable p) {
		return (p instanceof Samurai) && (p.getState() == CharaState.SHOGUN);
	}

	@Override
	public Playable getCollegue() {
		return collegue;
	}

	@Override
	public Collection<Playable> getEnemies() {
		return enemies;
	}

	public void setCollegue(Playable p) {
		collegue = p;
	}

	public void addEnemy(Playable p) {
		enemies.add(p);
	}
}
