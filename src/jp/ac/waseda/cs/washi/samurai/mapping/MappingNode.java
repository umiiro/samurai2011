/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jp.ac.waseda.cs.washi.samurai.mapping;


/**
 *
 * @author kaito
 */
public class MappingNode {
    public static final int TYPE_FIELD = 1;
    public static final int TYPE_WALL = 2;
    
    public final int type;
    
    protected MappingNode(int t)
    {
        type = t;
    }
}
