package jp.ac.waseda.cs.washi.samurai.personality;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import jp.ac.waseda.cs.washi.samurai.strategy.Strategy;

public abstract class Personality extends Strategy {
	protected static final Logger logger = Logger.getLogger(Personality.class.getPackage().getName());
	protected List<Strategy> strategies = new ArrayList<Strategy>();
	
	public Strategy requireStrategy(Strategy st) {
		strategies.add(st);
		st.init(headquater);
		return st;
	}
}
