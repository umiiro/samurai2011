/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.ac.waseda.cs.washi.samurai.mapping;

/**
 *
 * @author kaito
 */
public class MappingWall extends MappingNode {
    public static final MappingWall OUT_OF_FIELD = new MappingWall();
    
    private MappingWall(){
        super(TYPE_WALL);
    }
}
