package com.sa.service.thread;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sa.util.CopyFileUtil;
import com.sa.util.DataUtil;
import com.sa.util.StringUtil;

public class CollectionThread implements Runnable {
	private final static Logger LOGGER = LoggerFactory.getLogger(CollectionThread.class);
	private boolean isAlive = true;
	private String collectionPath = "D://sentimentAnalysis//collectionPath//";
	private String analysisResultPath ="D://sentimentAnalysis//analysisResult//";;
	private Map<String, String> map = new  HashMap<String, String>();
	private String workPath = "workspace";
	private long confirmTime = 3000l;
	private long systemStart = (new Date()).getTime();

	private Map<String,Boolean> dealedFiles = new HashMap<String,Boolean>();
	public void shotdown(){
		this.isAlive = false;
	}
	
	public static void main(String[] args) {
		CollectionThread collectionThread = new CollectionThread();
		collectionThread.run();
	}
	
	@Override
	public void run() {
		LOGGER.info("2222XXXX启动收集数据线程");
		File file = new File(collectionPath);
		File workD = new File(collectionPath+"workPath");
		if(!file.exists()){
			file.mkdir();
		}
		if(!workD.exists()){
				workD.mkdir();
		}
		
		while(this.isAlive){
			try {
				for(File childFile : file.listFiles()){
						long createTime = childFile.lastModified();
						LOGGER.info(this.systemStart +" ----" + createTime);
						if(createTime > systemStart &&!childFile.getName().endsWith(".done") && !childFile.isDirectory() && !map.containsKey(childFile.getName())){
							LOGGER.info("开始处理文件【"+childFile.getName()+"】...");
							
							map.put(childFile.getName(), childFile.getName());
							FileOutputStream out = null;
							try{
								long totolSpace = childFile.length();
								while(true){
									LOGGER.info("数据读取确认【"+totolSpace+"】");
									Thread.currentThread().sleep(confirmTime);
									if(totolSpace == childFile.length()){
										LOGGER.info("数据读取完成【"+totolSpace+"】");
										break;
									}else{
										totolSpace = childFile.length();
									}
								}
								
								LOGGER.info("XXXX开始解析数据"+File.separator);
							
								String dealerFileName =  workD.getPath()+File.separator+ DataUtil.getStringDate().trim().replace(" ", "")+".tmpfile";
								LOGGER.info(dealerFileName);
								CopyFileUtil.copyFile(childFile.getAbsolutePath(),dealerFileName,true);
								childFile.renameTo(new File(childFile.getPath()+".done"));
								LOGGER.info("yyyyyyyy"+(childFile.getPath()));
								String res = changeToAnalsisDate(dealerFileName);
								LOGGER.info(res);
								File resFile = new File(res);
								LOGGER.info(res);
								LOGGER.info("ADSADSADAS【"+(res.replace(".tmp",".arff")));
								resFile.renameTo(new File(res.replace(".tmp",".arff")));
								LOGGER.info("处理文件完成【"+childFile.getName()+"】...");
							}catch(Exception e){
								e.printStackTrace();
								LOGGER.error("发生异常",e);
								
								
							}finally{
								if(out!= null){
									out.close();
								}
							}
						}
						
				}
				
				Thread.currentThread().sleep(10000l);
			} catch (Exception e) {
				LOGGER.error("线程睡眠被打断",e);
			}
		}
	}
	
	public String changeToAnalsisDate(String filePath) throws Exception{
		String res = analysisResultPath + DataUtil.getStringDate().trim().replace(" ", "")+".tmp";
		File sourceFile = new File(filePath);
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(sourceFile),StringUtil.SA_CHARSET));
		String lineTxt = null;
		File file = new File(res);
		file.createNewFile();
		FileOutputStream out = (new FileOutputStream(file,true));
		try{	
			
			out.write(("@relation general-weka.filters.unsupervised.instance.RemovePercentage-P30.0_predicted"+System.getProperty("line.separator")).getBytes(StringUtil.SA_CHARSET));
            out.write((""+System.getProperty("line.separator")).getBytes(StringUtil.SA_CHARSET));
			out.write(("@attribute delta numeric"+System.getProperty("line.separator")).getBytes(StringUtil.SA_CHARSET));
			out.write(("@attribute theta numeric"+System.getProperty("line.separator")).getBytes(StringUtil.SA_CHARSET));
			out.write(("@attribute low_alpha numeric"+System.getProperty("line.separator")).getBytes(StringUtil.SA_CHARSET));
			out.write(("@attribute high_alpha numeric"+System.getProperty("line.separator")).getBytes(StringUtil.SA_CHARSET));
			out.write(("@attribute low_beta numeric"+System.getProperty("line.separator")).getBytes(StringUtil.SA_CHARSET));
			out.write(("@attribute high_beta numeric"+System.getProperty("line.separator")).getBytes(StringUtil.SA_CHARSET));
			out.write(("@attribute low_gamma numeric"+System.getProperty("line.separator")).getBytes(StringUtil.SA_CHARSET));
			out.write(("@attribute mid_gamma numeric"+System.getProperty("line.separator")).getBytes(StringUtil.SA_CHARSET));
			out.write(("@attribute class {happy,sad}"+System.getProperty("line.separator")).getBytes(StringUtil.SA_CHARSET));
			out.write((""+System.getProperty("line.separator")).getBytes(StringUtil.SA_CHARSET));
			out.write(("@data"+System.getProperty("line.separator")).getBytes(StringUtil.SA_CHARSET));
			while ((lineTxt = br.readLine()) != null) {
				if(lineTxt.split(",").length == 8){
					out.write(lineTxt.getBytes(StringUtil.SA_CHARSET));
					out.write(",?".getBytes(StringUtil.SA_CHARSET));
					out.write(System.getProperty("line.separator").getBytes(StringUtil.SA_CHARSET));
				}
			}
			LOGGER.info("TMP数据转换处理完成....");
			out.flush();
		}catch(Exception e){
			e.printStackTrace();
			LOGGER.error("发生异常",e);
		}finally{
			if(out!= null){
				out.close();
			}
		}
		return file.getPath();
	}
	
	public void setConfirmTime(long confirmTime) {
		this.confirmTime = confirmTime;
	}
	public void setCollectionPath(String collectionPath) {
		this.collectionPath = collectionPath;
	}
	public void setAnalysisResultPath(String analysisResultPath) {
		this.analysisResultPath = analysisResultPath;
	}
	public void setWorkPath(String workPath) {
		this.workPath = workPath;
	}

}
