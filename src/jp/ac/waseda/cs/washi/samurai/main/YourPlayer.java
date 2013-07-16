package jp.ac.waseda.cs.washi.samurai.main;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import jp.ac.waseda.cs.washi.samurai.api.CharaType;
import jp.ac.waseda.cs.washi.samurai.api.Map;
import jp.ac.waseda.cs.washi.samurai.personality.DogDefault;
import jp.ac.waseda.cs.washi.samurai.personality.SamuraiDefault;

public class YourPlayer {

	private static final Logger logger = Logger.getLogger(YourPlayer.class.getPackage().getName());
    private Map map;
    private Headquater hq;

    public YourPlayer() {
    	java.util.Map<String, String> a = new HashMap<String, String>();
    	a = Collections.unmodifiableMap(a);
    	a = Collections.unmodifiableMap(a);
    	a = Collections.unmodifiableMap(a);
        try{
            FileOutputStream err = new FileOutputStream("stderr.log", true);
            System.setErr(new PrintStream(err));
        } catch (FileNotFoundException e){
            
        }
        
        hq = new Headquater();
        hq.addPersonality(new SamuraiDefault(), CharaType.SAMURAI);
        hq.addPersonality(new DogDefault(), CharaType.DOG);
    }

    public void run(Scanner sc) {
        String cmd;
        
        map = Map.createOrUpdateMap(map, sc);
        
		long start = System.currentTimeMillis();

        hq.updateMappingTree(map);

        if ((map.getRemainingTime() & 0x1) == 0) {
            cmd = hq.next(map.getMySamurai());
        } else {
            cmd = hq.next(map.getMyDog());
        }
        
        System.out.println(cmd);
        
        if (logger.isLoggable(Level.FINE)) {
        	logger.fine("Execution: " + (System.currentTimeMillis() -  start));
        }

        System.out.flush();
    }
}
