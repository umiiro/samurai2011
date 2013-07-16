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
public class Samurai extends Playable {

	public Samurai(MappingMesh mesh) {
		super(mesh);
	}

	public Samurai(Playable prototype, GameState state) {
		super(prototype, state);
	}

	private Playable collegue;
	private Collection<Playable> enemies = new ArrayList<Playable>();

	@Override
	public boolean isOffensive(Playable p) {
		if (getState() == CharaState.SHOGUN) {
			return isOffensiveInNormal(p) && (p.getScore() == 0);
		} else {
			return isOffensiveInNormal(p);
		}
	}

	public boolean isOffensiveInNormal(Playable p) {
		return (p instanceof Dog) && isEnemy(p);
	}

	@Override
	public Playable getCollegue() {
		return collegue;
	}

	@Override
	public Collection<Playable> getEnemies() {
		return enemies;
	}

	public void setCollegue(Dog p) {
		collegue = p;
	}

	public void addEnemy(Dog p) {
		enemies.add(p);
	}
}
