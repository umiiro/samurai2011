package jp.ac.waseda.cs.washi.samurai.insight;

import java.util.logging.Logger;

import jp.ac.waseda.cs.washi.samurai.api.Direction;
import jp.ac.waseda.cs.washi.samurai.main.Headquater;
import jp.ac.waseda.cs.washi.samurai.mapping.MappingMesh;

public abstract class Insight {
	public static final Direction defaultDirection = Direction.UNKNOWN;
	public static final Direction[] validDirections = new Direction[] { Direction.DOWN,
			Direction.LEFT, Direction.RIGHT, Direction.UP };
	
	protected static final Logger logger = Logger.getLogger(Insight.class.getPackage().getName());
	protected Headquater headquater;
	protected MappingMesh mesh;
	
	public void init(Headquater hq){
		headquater = hq;
		mesh = hq.getMappingMesh();
	}
	
	public void update(){}
	public void finalize(){}
}
