package simpledb.buffer;

import java.util.Hashtable;

//Singleton class for the hashtable
//Done this was so that every class and instance can access this hashtable
//There is only one of these per JVM. 
public class existingBlocks {
	private static existingBlocks existingBlockList = new existingBlocks();
	public static Hashtable<Integer, Integer> blockList = new Hashtable<Integer, Integer>();
	private existingBlocks() {};
	
	public static existingBlocks getInstance() {
		return existingBlockList;
	}
}
