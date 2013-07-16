package jp.ac.waseda.cs.washi.samurai.strategy;

import java.util.logging.Logger;

import jp.ac.waseda.cs.washi.samurai.main.BallotBox;
import jp.ac.waseda.cs.washi.samurai.main.Headquater;
import jp.ac.waseda.cs.washi.samurai.mapping.MappingMesh;
import jp.ac.waseda.cs.washi.samurai.playable.Playable;


public abstract class Strategy {
    protected static final Logger logger = Logger.getLogger(Strategy.class.getPackage().getName());
    protected Headquater headquater;
    protected MappingMesh mesh;
    
    public void init(Headquater hq) {
    	headquater = hq;
    	mesh = hq.getMappingMesh();
    }

    public abstract void vote(Playable c, BallotBox bb);
}
