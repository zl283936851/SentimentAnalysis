package com.sa.service.thread;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class SaThreadManager implements Runnable{
	private final static Logger LOGGER = LoggerFactory.getLogger(SaThreadManager.class);
	private Map<String, Thread> threadMap;
	private boolean isAlive = true;
	
	public void shotdown(){
		this.isAlive = false;
	}
	
	@Override
	public void run() {
		while(this.isAlive){
			try{
				
				Thread.currentThread().sleep(10000);
				LOGGER.info("begin to check the health of threads.");
				for(Entry<String, Thread> set: this.threadMap.entrySet()){
					
					LOGGER.info("check "+set.getKey()+"...");
					boolean state = set.getValue().isAlive();
					String health = state ? "正常" : "异常";
					LOGGER.info("当前系统状态"+health);
					if(!state){
						LOGGER.info("开始重启线程"+set.getKey()+"...");
						set.getValue().start();
						LOGGER.info("开始重启完成...");
					}
				}
			}catch(Exception e){
				LOGGER.error("守护线程异常",e);
			}
		}
	}
	
	
	
	public void setThreadMap(Map<String, Thread> threadMap) {
		this.threadMap = threadMap;
	}



}
