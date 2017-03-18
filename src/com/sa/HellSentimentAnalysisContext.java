package com.sa;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sa.service.thread.AnalysisThread;
import com.sa.service.thread.CollectionThread;
import com.sa.service.thread.MusicSearchThread;
import com.sa.service.thread.SaThreadManager;




public class HellSentimentAnalysisContext {
	private final static Logger LOGGER = LoggerFactory.getLogger(HellSentimentAnalysisContext.class);
	private AnalysisThread analysisThread;
	private CollectionThread collectionThread;
	private MusicSearchThread musicSearchThread;
	private SaThreadManager saThreadManager;
	public void init(){
		LOGGER.info("SA SYSTEM INIT");
		Map<String, Thread> mapThreads = new HashMap<String, Thread>();
		mapThreads.put("情绪分析线程", new Thread(this.analysisThread,"analysisThread"));
		mapThreads.put("数据采集线程", new Thread(this.collectionThread,"collectionThread"));
		mapThreads.put("音乐搜索线程", new Thread(this.musicSearchThread,"musicSearchThread"));
		this.saThreadManager.setThreadMap(mapThreads);
		(new Thread(this.saThreadManager, "saThreadManager")).start();
	}
	public void setAnalysisThread(AnalysisThread analysisThread) {
		this.analysisThread = analysisThread;
	}
	public void setCollectionThread(CollectionThread collectionThread) {
		this.collectionThread = collectionThread;
	}
	public void setMusicSearchThread(MusicSearchThread musicSearchThread) {
		this.musicSearchThread = musicSearchThread;
	}
	public void setSaThreadManager(SaThreadManager saThreadManager) {
		this.saThreadManager = saThreadManager;
	}
	
}
