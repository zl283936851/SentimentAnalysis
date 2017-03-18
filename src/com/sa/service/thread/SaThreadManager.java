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
					String health = state ? "����" : "�쳣";
					LOGGER.info("��ǰϵͳ״̬"+health);
					if(!state){
						LOGGER.info("��ʼ�����߳�"+set.getKey()+"...");
						set.getValue().start();
						LOGGER.info("��ʼ�������...");
					}
				}
			}catch(Exception e){
				LOGGER.error("�ػ��߳��쳣",e);
			}
		}
	}
	
	
	
	public void setThreadMap(Map<String, Thread> threadMap) {
		this.threadMap = threadMap;
	}



}
