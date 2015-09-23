package simpledb.buffer;

import simpledb.file.*;
import java.util.*;



/**
 * Manages the pinning and unpinning of buffers to blocks.
 * @author Edward Sciore
 *
 */


class BasicBufferMgr {
   private Buffer[] bufferpool;
   private BitSet emptyBufferArray;
   private int numAvailable;

   /**
    * Creates a buffer manager having the specified number 
    * of buffer slots.
    * This constructor depends on both the {@link FileMgr} and
    * {@link simpledb.log.LogMgr LogMgr} objects 
    * that it gets from the class
    * {@link simpledb.server.SimpleDB}.
    * Those objects are created during system initialization.
    * Thus this constructor cannot be called until 
    * {@link simpledb.server.SimpleDB#initFileAndLogMgr(String)} or
    * is called first.
    * @param numbuffs the number of buffer slots to allocate
    */
   //Add an int array of empty frames in the constructor of the buffer manager
   BasicBufferMgr(int numbuffs) {
	  emptyBufferArray = new BitSet(numbuffs);;
      bufferpool = new Buffer[numbuffs];
      numAvailable = numbuffs;
      for (int i=0; i<numbuffs; i++) {
         bufferpool[i] = new Buffer();
         bufferpool[i].setBufferNum(i);
      	 emptyBufferArray.set(i, true);
      }
   }
   
   /**
    * Flushes the dirty buffers modified by the specified transaction.
    * @param txnum the transaction's id number
    */
   synchronized void flushAll(int txnum) {
      for (Buffer buff : bufferpool)
         if (buff.isModifiedBy(txnum))
         buff.flush();
   }
   
   /**
    * Pins a buffer to the specified block. 
    * If there is already a buffer assigned to that block
    * then that buffer is used;  
    * otherwise, an unpinned buffer from the pool is chosen.
    * Returns a null value if there are no available buffers.
    * @param blk a reference to a disk block
    * @return the pinned buffer
    */
   synchronized Buffer pin(Block blk) {
      Buffer buff = findExistingBuffer(blk);
      if (buff == null) {
         buff = chooseUnpinnedBuffer();
         if (buff == null)
            return null;
         buff.assignToBlock(blk);
      }
      if (!buff.isPinned())
         numAvailable--;
      buff.pin();
      return buff;
   }
   
   /**
    * Allocates a new block in the specified file, and
    * pins a buffer to it. 
    * Returns null (without allocating the block) if 
    * there are no available buffers.
    * @param filename the name of the file
    * @param fmtr a pageformatter object, used to format the new block
    * @return the pinned buffer
    */
   synchronized Buffer pinNew(String filename, PageFormatter fmtr) {
      Buffer buff = chooseUnpinnedBuffer();
      if (buff == null)
         return null;
      buff.assignToNew(filename, fmtr);
      numAvailable--;
      buff.pin();
      return buff;
   }
   
   /**
    * Unpins the specified buffer.
    * @param buff the buffer to be unpinned
    */
   //We need to add this to the free bit array so we can use it.
   synchronized void unpin(Buffer buff) {
      buff.unpin();
      int currentBuffNum = buff.getBufferNum();
      System.out.println("Unpin buffer: " + currentBuffNum);
      //emptyBufferArray.set(currentBuffNum, true);
      if (!buff.isPinned())
         numAvailable++;
   }
   
   /**
    * Returns the number of available (i.e. unpinned) buffers.
    * @return the number of available buffers
    */
   int available() {
      return numAvailable;
   }
   
   private void printBufferMgr() {
	   System.out.println("-----------------");
	   int i = 0;
	   for (Buffer buff : bufferpool) {
		   
		   System.out.println("Buffer num: " + i);
		   System.out.println("Buffer num in Buffer: " + buff.getBufferNum());
		   System.out.println("Buffer pin count: " + buff.getPinCount());
		   i++;
	   }
	   System.out.println("-----------------");
   }
   
   private Buffer findExistingBuffer(Block blk) {
      for (Buffer buff : bufferpool) {
         Block b = buff.block();
         if (b != null && b.equals(blk))
            return buff;
      }
      return null;
	  // int frameNum = BasicBufferMgr.existingBlock.get(blk.getBlkNum());
	   //return bufferpool[frameNum];
   }
   
   /*
   private Buffer chooseUnpinnedBuffer() {
      for (Buffer buff : bufferpool)
         if (!buff.isPinned())
         return buff;
      return null;
   }
   */
   
   //prints the contents of the empty Bitset
   public void printEmptyBufferArray() {
	   System.out.println("+++++++++++++++++++++++++++");
	   int emptyBufferLength = emptyBufferArray.length();
	   for( int i = 0; i < emptyBufferLength; i++) {
		   System.out.println("Empty Array Values " + emptyBufferArray.get(i));
	   }
	   System.out.println("Test Print ");
	   System.out.println("+++++++++++++++++++++++++++++");
   }
   
   //Looks through BitSet array for an empty buffer. Then updates the int array.
   private Buffer chooseUnpinnedBuffer() {
	   int emptyBufferLength = emptyBufferArray.length();
	   for(int i = 0; i < emptyBufferLength; i++) {	   
		   //If there is an empty buffer, return it
		   if(emptyBufferArray.get(i)) {
			   printEmptyBufferArray();
			   emptyBufferArray.set(i, false);
			   printEmptyBufferArray();
//			   if(BasicBufferMgr.existingBlock.isEmpty()) {
//				   System.out.println("Empty hash table");
//				   return bufferpool[i];
//			   }
//			   BasicBufferMgr.existingBlock.remove(bufferpool[i].block().getBlkNum());
			   System.out.println("Number of available buffers " + numAvailable);
			   return bufferpool[i];
		   }
		   printEmptyBufferArray();
	   }
	   //If there are no empty buffers, do the find an unpinned buffer 
	   //This was the initial function given
	      for (Buffer buff : bufferpool) {
	          if (!buff.isPinned()) {
	       	   return buff;
	          }
	      }
	   printEmptyBufferArray();
	   printBufferMgr();
	   System.out.println("Number of available buffers " + numAvailable);
	   return null;
   }
   
}
