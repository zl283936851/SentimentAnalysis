package com.sa.service.thread;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sa.service.music.MusicService;
import com.sa.util.StringUtil;

public class MusicSearchThread implements Runnable {
	private final static Logger LOGGER = LoggerFactory.getLogger(MusicSearchThread.class);
	private Map<Integer, String> muiscPath;
	private String analysisResultPath = "D://sentimentAnalysis//analysisResult//";
	private String musicResultPath = "D://sentimentAnalysis//musicResultPath//";
	private MusicService musicService;
	private boolean isAlive = true;
	
	public void shotdown(){
		this.isAlive = false;
	}
	
	public int parseLvl(double sisHappyPercision){
		
		if(sisHappyPercision <= 10){
			return 1;
		}else if(sisHappyPercision > 10 && sisHappyPercision <= 20){
			return 2;
		}else if(sisHappyPercision > 20 && sisHappyPercision <= 30){
			return 3;
		}else if(sisHappyPercision > 30 && sisHappyPercision <= 40){
			return 4;
		}else if(sisHappyPercision > 40 && sisHappyPercision <= 50){
			return 5;
		}else if(sisHappyPercision > 50 && sisHappyPercision <= 60){
			return 6;
		}else if(sisHappyPercision > 60 && sisHappyPercision <= 70){
			return 7;
		}else if(sisHappyPercision > 70 && sisHappyPercision <= 80){
			return 8;
		}else if(sisHappyPercision > 80 && sisHappyPercision <= 90){
			return 9;
		}else{
			return 10;
		}
	}
	
	@Override
	public void run() {
		while(this.isAlive){
			try {
				Thread.currentThread().sleep(5000l);
				LOGGER.info("开始扫描【"+analysisResultPath+"】");
				File sisDir = new File(analysisResultPath);
				if(!sisDir.exists()){
					sisDir.mkdir();
				}
				for(File childFile : sisDir.listFiles()){
					if(!childFile.getName().contains(".done") && childFile.getName().contains(".result")&& !childFile.isDirectory()){
						LOGGER.info("开始");
						BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(childFile),StringUtil.SA_CHARSET));
						String lineTxt = null;
						double sisHappyPercision = -1;
						while ((lineTxt = br.readLine()) != null) {
							String[] args = lineTxt.split("=");
							if(args.length >= 2 ){
								LOGGER.info(args[0]+";"+args[1]);
								if("sisHappyPercision".equals(args[0])){
									sisHappyPercision  = Double.parseDouble(args[1]);
									break;
								}
							}
						}
						br.close();
						childFile.renameTo(new File(childFile.getPath().replace(".result",".result.done")));
						if(sisHappyPercision != -1){
							Integer lvl = parseLvl(sisHappyPercision);
							musicService.play(new File(muiscPath.get(lvl)));
						}
					}
				}	
				
			} catch (Exception e) {
				LOGGER.error("线程睡眠被打断",e);
			}
		}
	}
	
	public void setMuiscPath(Map<Integer, String> muiscPath) {
		this.muiscPath = muiscPath;
	}
	public void setMusicService(MusicService musicService) {
		this.musicService = musicService;
	}
	public void setAlive(boolean isAlive) {
		this.isAlive = isAlive;
	}

	public void setAnalysisResultPath(String analysisResultPath) {
		this.analysisResultPath = analysisResultPath;
	}

	public void setMusicResultPath(String musicResultPath) {
		this.musicResultPath = musicResultPath;
	}

}
