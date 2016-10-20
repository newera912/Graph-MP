package edu.albany.cs.graph;

import org.apache.commons.lang3.ArrayUtils;

import edu.albany.cs.apdmIO.APDMInputFormat;

public class MexicoTwitterLocalNewsGraphWindow extends MexicoTwitterLocalNewsGraph {

	public String[] words = null;
	public double[][] news_counts = null;
	public double[] median_news_counts = null;
	public double[][] twitter_counts = null;
	public double[] median_twitter_counts = null;
	
	public MexicoTwitterLocalNewsGraphWindow(APDMInputFormat apdm,int windowSize){
		super(apdm);
		words = apdm.data.words;
		news_counts=new double[apdm.data.numNodes][windowSize];
		twitter_counts=new double[apdm.data.numNodes][windowSize];
		//System.out.println(ArrayUtils.toString(apdm.data.attributes));
		for(int i=0;i<windowSize;i++){
			for(int j=0;j<apdm.data.attributes[i].length;j++){
				news_counts[j][i] = apdm.data.attributes[i][j];
				twitter_counts[j][i] = apdm.data.attributes[i+windowSize+1][j];
		}
		}
		median_news_counts = apdm.data.attributes[windowSize];
		
		median_twitter_counts = apdm.data.attributes[2*windowSize+1];
		run();
	}
	
	private void run(){
		for(int i = 0 ; i < news_counts.length ; i++){
			for(int j=0; j<news_counts[0].length;j++){
			if(news_counts[i][j]<=0.0D){
				news_counts[i][j] = 0.1D;
			}
			}
		}
		for(int i = 0 ; i < median_news_counts.length ; i++){
			if(median_news_counts[i]<=0.0D){
				median_news_counts[i] = 0.1D;
			}
		}
		for(int i = 0 ; i < twitter_counts.length ; i++){
			for(int j=0; j<twitter_counts[0].length;j++){
			if(twitter_counts[i][j]<=0.0D){
				twitter_counts[i][j] = 0.1D;
			}
			}
		}
		for(int i = 0 ; i < median_twitter_counts.length ; i++){
			if(median_twitter_counts[i]<=0.0D){
				median_twitter_counts[i] = 0.1D;
			}
		}
	}
	
}
