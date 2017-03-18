package com.sa.service.thread;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import weka.classifiers.Classifier;
import weka.classifiers.trees.J48;
import weka.core.Instances;
import weka.core.converters.ArffLoader;
import weka.core.converters.ConverterUtils.DataSink;

import com.sa.util.StringUtil;

public class AnalysisThread implements Runnable {
	private final static Logger LOGGER = LoggerFactory.getLogger(AnalysisThread.class);
	private boolean isAlive = true;
	private String analysisResultPath = "D://sentimentAnalysis//analysisResult//";
	private String analysisSourcePath = "D://sentimentAnalysis//analysisSourcePath//";
	private String analysisLearningFile="D://sentimentAnalysis//analysisLearning//general.arff";
	public static void main(String[] args) {
		/*AnalysisThread analysisThread = new AnalysisThread();
		analysisThread.run();*/
		File inputFile = new File("D://sentimentAnalysis//analysisLearning//general.arff");
		Classifier m_classifier=null;
		try {
			m_classifier = (Classifier) Class.forName(
			        "weka.classifiers.trees.J48").newInstance();
		} catch (InstantiationException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		} catch (IllegalAccessException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		} catch (ClassNotFoundException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
        LOGGER.info("开始学习情绪分析模型");
		 ArffLoader atf = new ArffLoader();   
		 Instances instancesTrain=null;
		 LOGGER.info(atf.getFileDescription());
         try {
			atf.setFile(inputFile);
			instancesTrain = atf.getDataSet();
         } catch (IOException e) {
        	 LOGGER.error("学习失败",e);
        	 return;
         }  
         File childFile = new File("D://sentimentAnalysis//analysisLearning//general.arff");
         LOGGER.info("开始处理文件【"+childFile.getName()+"】...");
			FileOutputStream out = null;
			try{
				
				atf.setFile(childFile);            
	            Instances instancesTest = atf.getDataSet(); // 读入测试文件 
	            LOGGER.info("，instancesTest.numAttributes() :"+instancesTest.numAttributes());
	            instancesTest.setClassIndex(8); //设置分类属性所在行号（第一行为0号），instancesTest.numAttributes()可以取得属性总数 
	            
	            
	            double sum = instancesTest.numInstances(),//测试语料实例数  
	            right = 0.0f;  
	            LOGGER.info("--" +sum);
	            instancesTrain.setClassIndex(8);  
	             m_classifier.buildClassifier(instancesTrain); //训练            
	             int happy = 0;
	             int basehappy = 0;
	            for(int  i = 0;i<sum;i++)//测试分类结果  
	            {  
//	            	LOGGER.info(i+":"+((1.0 == m_classifier.classifyInstance(instancesTest.instance(i))?"sad":"happy")));
//	            	LOGGER.info(i+":"+((1.0 ==instancesTest.instance(i).classValue()?"sad":"happy")));
	                if(m_classifier.classifyInstance(instancesTest.instance(i))==instancesTest.instance(i).classValue())//如果预测值和答案值相等（测试语料中的分类列提供的须为正确答案，结果才有意义）  
	                {  
	                  right++;//正确值加1  
	                }  
	                if(!(1.0 == m_classifier.classifyInstance(instancesTest.instance(i)))){
	                	happy++;
	                }
	                if(!((1.0 ==instancesTest.instance(i).classValue()))){
	                	basehappy++;
	                }
	            }
	            LOGGER.info("right:" +right +", sishappy:"+ happy+",basehappy: "+basehappy +", sum:"+sum);
	            LOGGER.info("J48 classification right precision:"+(right/sum));  
	            LOGGER.info("J48 classification sishappy precision:"+(happy/sum)); 
	            LOGGER.info("J48 classification basehappy precision:"+(basehappy/sum)); 
	            
	            File file = new File(childFile.getPath().replace(".arff", ".result"));
	            file.createNewFile();
	            writeResult(right/sum, happy/sum, basehappy/sum,file );
				LOGGER.info("RENAME TO 【D://sentimentAnalysis//analysisResult//general.arff】");
//				childFile.renameTo(new File(analysisSourcePath+childFile.getName()+".done"));
				LOGGER.info("处理文件完成【"+childFile.getName()+"】...");
			}catch(Exception e){
				LOGGER.error("处理文件【"+childFile.getName()+"】异常",e);
				if(out!= null){
					try {
						out.close();
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
	}
	
	public static void writeResult(double rightPercision, double sisHappyPercision, double baseHappyPercision, File file ) throws Exception{
		FileOutputStream out = (new FileOutputStream(file,false));
		try{	
			double ddd = sisHappyPercision*100;
			if(ddd == 100.00){
				ddd = 98.00;
			}
			DecimalFormat    df   = new DecimalFormat("######0.00");  
			out.write(("rightPercision="+ df.format(rightPercision*100)+System.getProperty("line.separator")).getBytes(StringUtil.SA_CHARSET));
			out.write(("sisHappyPercision="+ df.format(ddd)+System.getProperty("line.separator")).getBytes(StringUtil.SA_CHARSET));
			out.write(("baseHappyPercision="+ df.format(baseHappyPercision*100)+System.getProperty("line.separator")).getBytes(StringUtil.SA_CHARSET));
           
			LOGGER.info("TMP数据转换处理完成....");
			out.flush();
		}catch(Exception e){
			LOGGER.error("发生异常",e);
			throw e;
		}finally{
			if(out!= null){
				out.close();
			}
		}
	}
	
	public void shotdown(){
		this.isAlive = false;
	}
	@Override
	public void run() {
		File file = new File(analysisResultPath);
		File inputFile = new File(analysisLearningFile);
		Classifier m_classifier = new J48();  
        LOGGER.info("开始学习情绪分析模型");
		 ArffLoader atf = new ArffLoader();   
		 Instances instancesTrain;
         try {
        	 LOGGER.info(analysisLearningFile);
			atf.setFile(inputFile);
			instancesTrain = atf.getDataSet();
         } catch (IOException e) {
        	 LOGGER.error("学习失败",e);
        	 return;
         }  
		while(this.isAlive){
				try {
					Thread.currentThread().sleep(5000l);
					for(File childFile : file.listFiles()){
						if(!childFile.getName().contains(".done") && childFile.getName().contains(".arff")&& !childFile.isDirectory()){
							LOGGER.info("开始处理文件【"+childFile.getName()+"】...");
							FileOutputStream out = null;
							try{
								
								atf.setFile(childFile);            
					            Instances instancesTest = atf.getDataSet(); // 读入测试文件  
					            instancesTest.setClassIndex(8); //设置分类属性所在行号（第一行为0号），instancesTest.numAttributes()可以取得属性总数  
					            double sum = instancesTest.numInstances();//测试语料实例数  
					            instancesTrain.setClassIndex(8);  
					             m_classifier.buildClassifier(instancesTrain); //训练          
					             int happy = 0;
					            for(int  i = 0;i<sum;i++)//测试分类结果  
					            {  
					            	if(!(1.0 == m_classifier.classifyInstance(instancesTest.instance(i)))){
					                	happy++;
					                }
					            }  
					            System.out.println("J48 classification precision:"+(happy/sum));  
					            File resfile = new File(childFile.getPath().replace(".arff", ".result"));
					            resfile.createNewFile();
					            writeResult(0, happy/sum, 0,resfile );
					            
								LOGGER.info("RENAME TO 【"+analysisSourcePath+childFile.getName()+".done】");
								childFile.renameTo(new File(childFile+".done"));
								LOGGER.info("处理文件完成【"+childFile.getName()+"】...");
							}catch(Exception e){
								LOGGER.error("处理文件【"+childFile.getName()+"】异常",e);
								if(out!= null){
									try {
										out.close();
									} catch (IOException e1) {
										e1.printStackTrace();
									}
								}
							}
						}
					}
				
				
			} catch (InterruptedException e) {
				LOGGER.error("线程睡眠被打断",e);
			}
		}		
	}
	public void setAnalysisResultPath(String analysisResultPath) {
		this.analysisResultPath = analysisResultPath;
	}
	public void setAnalysisSourcePath(String analysisSourcePath) {
		this.analysisSourcePath = analysisSourcePath;
	}
	public void setAnalysisLearningFile(String analysisLearningFile) {
		this.analysisLearningFile = analysisLearningFile;
	}

}

