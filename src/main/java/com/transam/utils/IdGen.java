package com.transam.utils;

import java.util.Random;

public class IdGen {
	private static IdGen worker = null;
	
	  private final long twepoch = 1288834974657L;
	  private final long workerIdBits = 5L;
	  private final long datacenterIdBits = 5L;
	  private final long maxWorkerId = -1L ^ (-1L << workerIdBits);
	  private final long maxDatacenterId = -1L ^ (-1L << datacenterIdBits);
	  private final long sequenceBits = 12L;
	  private final long workerIdShift = sequenceBits;
	  private final long datacenterIdShift = sequenceBits + workerIdBits;
	  private final long timestampLeftShift = sequenceBits + workerIdBits + datacenterIdBits;
	  private final long sequenceMask = -1L ^ (-1L << sequenceBits);
	 
	  private long workerId;
	  private long datacenterId;
	  private long sequence = 0L;
	  private long lastTimestamp = -1L;
	 
	  public IdGen(long workerId, long datacenterId) {
	    if (workerId > maxWorkerId || workerId < 0) {
	      throw new IllegalArgumentException(String.format("worker Id can't be greater than %d or less than 0", maxWorkerId));
	    }
	    if (datacenterId > maxDatacenterId || datacenterId < 0) {
	      throw new IllegalArgumentException(String.format("datacenter Id can't be greater than %d or less than 0", maxDatacenterId));
	    }
	    this.workerId = workerId;
	    this.datacenterId = datacenterId;
	  }
	 
	  public synchronized long nextId() {
	    long timestamp = timeGen();
	    if (timestamp < lastTimestamp) {
	      throw new RuntimeException(String.format("Clock moved backwards.  Refusing to generate id for %d milliseconds", lastTimestamp - timestamp));
	    }
	    if (lastTimestamp == timestamp) {
	      sequence = (sequence + 1) & sequenceMask;
	      if (sequence == 0) {
	        timestamp = tilNextMillis(lastTimestamp);
	      }
	    } else {
	      sequence = 0L;
	    }
	 
	    lastTimestamp = timestamp;
	 
	    return ((timestamp - twepoch) << timestampLeftShift) | (datacenterId << datacenterIdShift) | (workerId << workerIdShift) | sequence;
	  }
	 
	  protected long tilNextMillis(long lastTimestamp) {
	    long timestamp = timeGen();
	    while (timestamp <= lastTimestamp) {
	      timestamp = timeGen();
	    }
	    return timestamp;
	  }
	 
	  protected long timeGen() {
	    return System.currentTimeMillis();
	  }
	
	public static long getId(){
		if(worker == null){
			worker = new IdGen(0,0);
		}
		return worker.nextId();
	}
	/*
	public static void main(String[] args) {
		List<Long> list = new ArrayList<Long>();
		for(int i=0;i<1200;i++){
			long id = getId();
			list.add(id);
		}
		
		for ( int i = 0 ; i < list.size() - 1 ; i ++ ) {
		     for ( int j = list.size() - 1 ; j > i; j -- ) {
		       if (list.get(j).equals(list.get(i))) {
		    	   System.out.println(list.get(j));
		         list.remove(j);
		         System.out.println(j);
		       }
		      }
		    }
	}
	*/
	/**
	 * 图片名生成
	 */
	public static String genImageName() {
		//取当前时间的长整形值包含毫秒
		long millis = System.currentTimeMillis();
		//long millis = System.nanoTime();
		//加上三位随机数
		Random random = new Random();
		int end3 = random.nextInt(999);
		//如果不足三位前面补0
		String str = millis + String.format("%03d", end3);
		
		return str;
	}
}