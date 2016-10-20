package edu.albany.cs.graph;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.regex.Pattern;

import org.apache.commons.lang3.ArrayUtils;

import edu.albany.cs.apdmIO.APDMInputFormat;
import edu.albany.cs.base.Edge;
import edu.albany.cs.base.Utils;

public class MexicoGraphGeneratorWindow {
	
	
	
	private MexicoGraphGeneratorWindow() {

	}

	public static void generateTestGraph(String rootFolder, String singleFileName, double restartProb,
			double trueSubGraphRatio, double q_twitter, double q_news, Path resultOriginFilePath,
			Path resultFileNameTwitterFix, Path resultFileNameNewsFix, int iterations) {

		try {
			String usedAlgorithm = "NULL";
			String dataSource = "TwitterMexicoCorr";
			ArrayList<Edge> edges = new ArrayList<Edge>();
			ArrayList<int[]> intEdges = new ArrayList<int[]>();
			ArrayList<Edge> trueSubGraphEdges = null;

			String[] attributeNames = new String[] { "word", 
					"new_count1", "2","3","4","5","6","7",
					"median_news_counts",
					"twitter_counts1","2","3","4","5","6","7",
					"median_twitter_counts" };

			Integer[] indexArr = null;
			String[] words = null;
			HashMap<String, Integer> wordsMap = new HashMap<String, Integer>();
			Double[][] news_counts = new Double[5653][7];
			Double[][] twitter_counts = new Double[5653][7];
			Double[] median_news_counts = null;
			Double[] median_twitter_counts = null;

			int id = 0;
			for (String eachLine : Files.readAllLines(Paths.get(rootFolder, "outputs", singleFileName+"_7.txt"))) {
				
				if (eachLine.startsWith("#") || Pattern.compile("^[A-Za-z]").matcher(eachLine).find()) {
					continue; 
				}
				
				if (eachLine.split(" ").length == 18) {
					
					Integer index = Integer.parseInt(eachLine.split(" ")[0]);
					String word = eachLine.split(" ")[1];
					wordsMap.put(word, index);
					String[] lineStr=Arrays.copyOfRange(eachLine.split(" "),2,eachLine.split(" ").length);
					
					double[] terms=new double[16];
					for(int i=0;i<terms.length;i++){
						terms[i]=Double.parseDouble(lineStr[i]);
					}
					//double[] news_count_i = Arrays.copyOfRange(terms,0,7);
					double median_news_i = terms[7];
					//double[] twitter_counts_i = Arrays.copyOfRange(terms,8,15);
					double median_twitter_i = terms[15];
					
					for(int i=0;i<7;i++){
						news_counts[index][i]=terms[i];
						twitter_counts[index][i]=terms[8+i];
					}
					words = ArrayUtils.add(words, word);
					median_news_counts = ArrayUtils.add(median_news_counts, median_news_i);					
					median_twitter_counts = ArrayUtils.add(median_twitter_counts, median_twitter_i);
					
				}
				
				
				if (eachLine.split(" ").length == 3 && !Pattern.compile("^[A-Za-z]").matcher(eachLine).find()) {
					
					int end0 = Integer.parseInt(eachLine.split(" ")[0]);
					int end1 = Integer.parseInt(eachLine.split(" ")[1]);
					// double weight = eachLine.split(" ")[1];
					edges.add(new Edge(end0, end1, id, 1.0D));
					intEdges.add(new int[] { end0, end1 });
					id++;
				}
			}
			
			
			ArrayList<ArrayList<Integer>> adj = new ArrayList<>();
			for (int i = 0; i < words.length; i++) {
				adj.add(new ArrayList<>());
			}
			for (int[] edge : intEdges) {
				if (!adj.get(edge[0]).contains(edge[1])) {
					adj.get(edge[0]).add(edge[1]);
				}
				if (!adj.get(edge[1]).contains(edge[0])) {
					adj.get(edge[1]).add(edge[0]);
				}
			}

			Double[][] news_counts_c = Arrays.copyOf(news_counts, news_counts.length);
			Double[][] twitter_counts_c = Arrays.copyOf(twitter_counts, twitter_counts.length);
			Double[] median_news_counts_c = Arrays.copyOf(median_news_counts, twitter_counts.length);
			Double[] median_twitter_counts_c = Arrays.copyOf(median_twitter_counts, twitter_counts.length);
			System.out.println(ArrayUtils.toString(twitter_counts_c[0]));
			GenerateMSRealWindow gen = new GenerateMSRealWindow(adj, news_counts_c, twitter_counts_c, median_news_counts_c,
					median_twitter_counts_c, q_news, q_twitter, restartProb, trueSubGraphRatio, iterations);

			trueSubGraphEdges = gen.treEdges;

			APDMInputFormat.generateAPDMFile(usedAlgorithm, dataSource, edges, trueSubGraphEdges,
					resultOriginFilePath.toString(), attributeNames, words, gen.news_counts, median_news_counts,
					gen.twitter_counts, median_twitter_counts);

			/*if (resultFileNameTwitterFix != null) {
				APDMInputFormat.generateAPDMFile(usedAlgorithm, dataSource, edges, trueSubGraphEdges,
						resultFileNameTwitterFix.toString(), attributeNames, words, gen.news_counts, median_news_counts,
						twitter_counts, median_twitter_counts);
			}
			if (resultFileNameNewsFix != null) {
				APDMInputFormat.generateAPDMFile(usedAlgorithm, dataSource, edges, trueSubGraphEdges,
						resultFileNameNewsFix.toString(), attributeNames, words, news_counts, median_news_counts,
						gen.twitter_counts, median_twitter_counts);
			}*/
			System.out.println("done");
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void generateSingleCase() {
		double restartProb = 0.5;
		double trueSubGraphRatio = 0.05;
		double q_twitter = 100.0D;
		double q_news = 0.01D;
		String rootFolder = "/home/baojian/Dropbox/data/ICDM-2016/mexicoData/";
		String date = "2014-03-02";
		String subFolder = rootFolder + "testGraph-" + date + "_restartProb_" + restartProb + "_trueRatio_"
				+ trueSubGraphRatio;
		if (!new File(subFolder).exists()) {
			new File(subFolder).mkdir();
		}
		String resultFileName = subFolder + "/testGraph_" + date + "_qTwitter_" + q_twitter + "_qNews_" + q_news
				+ "_.txt";
		int iterations = 1000;
		//generateTestGraph(rootFolder, date, restartProb, trueSubGraphRatio, q_twitter, q_news,
		//		Paths.get(resultFileName), null, null, iterations);
	}
	public static ArrayList<String> readLines(String fileName) throws Exception {
		File file=new File(fileName);
	      if (!file.exists()) {
	          return new ArrayList<String>();
	      }
	      BufferedReader reader = new BufferedReader(new FileReader(file));
	      ArrayList<String> results = new ArrayList<String>();
	      String line = reader.readLine();
	      int countDay=0;
	      
	      while (line != null) {
	    	  
	    	  
	    		  results.add(line.split(" ")[0]);
	    		  countDay++;
	    	  
	          //System.out.println(line);
	          line = reader.readLine();
	      }
	      System.out.println(countDay);
	      return results;
	  }
	public static double[][] getWindowdata(ArrayList<String> dayList,String rootFolder,int startIndex,int endIndex) throws NumberFormatException, IOException{
		double[][] windowNewsTwitter=new double[5653][2*(endIndex-startIndex+1)];
		//double[][] windowTwitter=new double[endIndex-startIndex][];
		for(int i=0;i<endIndex-startIndex;i++){
			for (String eachLine : Files.readAllLines(Paths.get(rootFolder, "outputs", dayList.get(startIndex+i)))) {

				if (eachLine.startsWith("#")) {
					continue;
				}
				if (eachLine.split(" ").length == 6) {
					Integer index = Integer.parseInt(eachLine.split(" ")[0]);
					
					double news_count_i = Double.parseDouble(eachLine.split(" ")[2]);					
					double twitter_counts_i = Double.parseDouble(eachLine.split(" ")[4]);		

					
					windowNewsTwitter[i] = ArrayUtils.add(windowNewsTwitter[i], news_count_i);
					windowNewsTwitter[i+endIndex-startIndex] = ArrayUtils.add(windowNewsTwitter[i+endIndex-startIndex], twitter_counts_i);
					//windowTwitter[i] = ArrayUtils.add(windowTwitter[i], twitter_counts_i);
					
				}	
			}
		}
		double[][] windowNewsTwitter_T=new double[windowNewsTwitter[0].length][windowNewsTwitter.length];
		
		for(int i=0;i<windowNewsTwitter.length;i++){
			for(int j=0;j<windowNewsTwitter[0].length;j++){
				windowNewsTwitter_T[j][i]=windowNewsTwitter[i][j];
			}
		}
			
		return windowNewsTwitter_T;
		
	}
	public static void generateTestCase() throws Exception {
		String rootFolder = "../data/";
		ArrayList<String> dayList=readLines("../data/daylist.txt");
		int windowSize=7;
		String date = "2014-03-02";
		int startIndex=dayList.indexOf(date);
		System.out.println("==="+startIndex);
		//double[][] newsTwitterWindowData=getWindowdata(dayList, rootFolder, startIndex, startIndex+windowSize);
		//System.out.println(newsTwitterData.length+" "+newsTwitterData[0].length+"\n"+ArrayUtils.toString(newsTwitterData));
//		int in=0;
//		for(double[] line:newsTwitterData){
//			System.out.println(in+" "+ArrayUtils.toString(line));
//			in++;
//			
//		}
		
		double[] trueSubGraphRatioArr = { 0.05D, 0.10D, 0.15D };
		double[] restartProbArr = { 0.0D,0.1D,0.2D,0.3D,0.4D,0.5D};
		double[] q_twitterArr = { 5, 10, 15, 20, 25, 30, 35, 40, 45, 50, 55, 60, 65, 70, 75, 80, 85, 90, 95, 100 };
		for (double trueSubGraphRatio : trueSubGraphRatioArr) {
			for (double restartProb : restartProbArr) {
				for (double q_twitter : q_twitterArr) {
					System.out.println("--------------------------------------");
					String subFolder = "testGraph-" + date + "_restartProb_" + restartProb + "_trueRatio_"
							+ trueSubGraphRatio;
					System.out.println(Paths.get(rootFolder, subFolder).toString());
					if (!new File(Paths.get(rootFolder, subFolder).toString()).exists()) {
						new File(Paths.get(rootFolder, subFolder).toString()).mkdir();
					}
					double q_news = 1.0D / q_twitter;
					String resultFileNameOrigin = "/testGraph_" + date + "_qTwitter_" + q_twitter + "_qNews_" + q_news
							+ "_.txt";
					String resultFileNameTwitterFix = "/testGraph_" + date + "_qTwitter_1.0_qNews_" + q_news + "_.txt";
					String resultFileNameNewsFix = "/testGraph_" + date + "_qTwitter_" + q_twitter + "_qNews_1.0_.txt";

					int iterations = 100;
					generateTestGraph(rootFolder, date, restartProb, trueSubGraphRatio, q_twitter, q_news,
							Paths.get(rootFolder, subFolder, resultFileNameOrigin).toAbsolutePath(),
							Paths.get(rootFolder, subFolder, resultFileNameTwitterFix).toAbsolutePath(),
							Paths.get(rootFolder, subFolder, resultFileNameNewsFix).toAbsolutePath(), iterations);
				
				}
			}
		}
	}

	public static void main(String args[]) throws Exception {
		//generateSingleCase();
		generateTestCase();
	}

}
