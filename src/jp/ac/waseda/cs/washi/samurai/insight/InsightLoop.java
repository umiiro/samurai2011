package jp.ac.waseda.cs.washi.samurai.insight;

import jp.ac.waseda.cs.washi.samurai.api.Direction;
import jp.ac.waseda.cs.washi.samurai.gamestate.GameState;
import jp.ac.waseda.cs.washi.samurai.gamestate.MutableGameState;
import jp.ac.waseda.cs.washi.samurai.main.Headquater;
import jp.ac.waseda.cs.washi.samurai.mapping.MappingField;
import jp.ac.waseda.cs.washi.samurai.playable.Playable;

public class InsightLoop extends Insight {
	public static InsightLoop require(Headquater hq) {
		return (InsightLoop)hq.registerInsight(new InsightLoop());
	}

	public boolean isLoop(Playable play, Direction dir, int maxLength) {
		return isLoop(play, dir, maxLength, mesh.getCurrentState());
	}
	
	public boolean isLoop(Playable play, Direction dir, int maxLength, GameState base) {
		MutableGameState next = new MutableGameState(base);
		MappingField currField = play.getField(base);
		MappingField nextField = currField.getAdjacent(dir);
		if (nextField == MappingField.EMPTY)
			return false;

		next.setFieldOf(play, nextField);
		return getLoopLength(play, maxLength, next) >= 0;
	}
	
	public int getLoopLength(Playable play, int maxLength) {
		return getLoopLength(play, maxLength, mesh.getCurrentState());
	}

	public int getLoopLength(Playable play, int maxLength, GameState base) {
		GameState state = base;
		MappingField currfield = base.getFieldOf(play);
		Direction currdir = base.getDirectionOf(play);
		for (int i = 0; i < maxLength; i++) {
			state = state.getPrevState();
			if (state == GameState.EMPTY)
				break;

			MappingField prevfield = state.getFieldOf(play);
			Direction prevdir = state.getDirectionOf(play);
			if (currdir == prevdir && currfield.equals(prevfield))
				return i;
		}

		return -1;
	}
}
