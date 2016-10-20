package edu.albany.cs.graph;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;

import org.apache.commons.lang3.ArrayUtils;

import edu.albany.cs.apdmIO.APDMInputFormat;
import edu.albany.cs.base.Edge;

public class MexicoGraphGeneratorSeperateSingnal {

	private MexicoGraphGeneratorSeperateSingnal() {

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

			String[] attributeNames = new String[] { "word", "news_counts", "median_news_counts", "twitter_counts",
					"median_twitter_counts" };

			Integer[] indexArr = null;
			String[] words = null;
			HashMap<String, Integer> wordsMap = new HashMap<String, Integer>();
			Double[] news_counts = null;
			Double[] twitter_counts = null;
			Double[] median_news_counts = null;
			Double[] median_twitter_counts = null;

			int id = 1;
			for (String eachLine : Files.readAllLines(Paths.get(rootFolder, singleFileName))) {

				if (eachLine.startsWith("#")) {
					continue;
				}
				if (eachLine.split(" ").length == 6) {
					Integer index = Integer.parseInt(eachLine.split(" ")[0]);
					String word = eachLine.split(" ")[1];
					wordsMap.put(word, index);
					double news_count_i = Double.parseDouble(eachLine.split(" ")[2]);
					double median_news_i = Double.parseDouble(eachLine.split(" ")[3]);
					double twitter_counts_i = Double.parseDouble(eachLine.split(" ")[4]);
					double median_twitter_i = Double.parseDouble(eachLine.split(" ")[5].trim());

					indexArr = ArrayUtils.add(indexArr, index);
					words = ArrayUtils.add(words, word);
					news_counts = ArrayUtils.add(news_counts, news_count_i);
					median_news_counts = ArrayUtils.add(median_news_counts, median_news_i);
					twitter_counts = ArrayUtils.add(twitter_counts, twitter_counts_i);
					median_twitter_counts = ArrayUtils.add(median_twitter_counts, median_twitter_i);
				}
				if (eachLine.split(" ").length == 3) {
					int end0 = wordsMap.get(eachLine.split(" ")[0]);
					int end1 = wordsMap.get(eachLine.split(" ")[1]);
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

			Double[] news_counts_c = Arrays.copyOf(news_counts, news_counts.length);
			Double[] twitter_counts_c = Arrays.copyOf(twitter_counts, twitter_counts.length);
			Double[] median_news_counts_c = Arrays.copyOf(median_news_counts, twitter_counts.length);
			Double[] median_twitter_counts_c = Arrays.copyOf(median_twitter_counts, twitter_counts.length);

			GenerateMSReal gen = new GenerateMSReal(adj, news_counts_c, twitter_counts_c, median_news_counts_c,
					median_twitter_counts_c, q_news, q_twitter, restartProb, trueSubGraphRatio, iterations);

			trueSubGraphEdges = gen.treEdges;

			APDMInputFormat.generateAPDMFile(usedAlgorithm, dataSource, edges, trueSubGraphEdges,
					resultOriginFilePath.toString(), attributeNames, words, gen.news_counts, median_news_counts,
					gen.twitter_counts, median_twitter_counts);

			if (resultFileNameTwitterFix != null) {
				APDMInputFormat.generateAPDMFile(usedAlgorithm, dataSource, edges, trueSubGraphEdges,
						resultFileNameTwitterFix.toString(), attributeNames, words, gen.news_counts, median_news_counts,
						twitter_counts, median_twitter_counts);
			}
			if (resultFileNameNewsFix != null) {
				APDMInputFormat.generateAPDMFile(usedAlgorithm, dataSource, edges, trueSubGraphEdges,
						resultFileNameNewsFix.toString(), attributeNames, words, news_counts, median_news_counts,
						gen.twitter_counts, median_twitter_counts);
			}
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
		generateTestGraph(rootFolder, date, restartProb, trueSubGraphRatio, q_twitter, q_news,
				Paths.get(resultFileName), null, null, iterations);
	}

	public static void generateTestCase() {
		//String rootFolder = "/home/baojian/Dropbox/data/ICDM-2016/mexicoData/";
		String rootFolder = "F:/workspace/git/data/graph_1000";
		ArrayList<String> date_news =new ArrayList<>(Arrays.asList("2014-01-01","2014-01-02","2014-01-03","2014-01-04"));
		ArrayList<String> date_twitter =new ArrayList<>(Arrays.asList("2014-01-05","2014-01-06","2014-01-07","2014-01-08"));
		int overLadyNo=1;//Two overlap days
		ArrayList<String> overLapDay=new ArrayList<String>();
		Random randomizer = new Random();
		for(int i=0;i<overLadyNo;i++){
			String temp=date_news.get(randomizer.nextInt(date_news.size()));
			overLapDay.add(temp);
			date_news.remove(date_news.indexOf(temp));
			temp=date_twitter.get(randomizer.nextInt(date_twitter.size()));
			overLapDay.add(temp);
			date_twitter.remove(date_twitter.indexOf(temp));
		}
		
		
		System.out.println(ArrayUtils.toString(overLapDay)+"\n"+ArrayUtils.toString(date_news)+"\n"+ArrayUtils.toString(date_twitter));
		double[] trueSubGraphRatioArr = { 0.05D, 0.10D, 0.15D };
		double[] restartProbArr = { 0.0D,0.1D,0.2D,0.3D,0.4D,0.5D};
		double[] q_twitterArr = { 5, 10, 15, 20, 25, 30, 35, 40, 45, 50, 55, 60, 65, 70, 75, 80, 85, 90, 95, 100 };
		for(String day:overLapDay){			
			for (double trueSubGraphRatio : trueSubGraphRatioArr) {
				for (double restartProb : restartProbArr) {
					for (double q_twitter : q_twitterArr) {
						System.out.println("--------------------------------------");
						
						String subFolder = day+"/testGraph-" + day + "_restartProb_" + restartProb + "_trueRatio_"
								+ trueSubGraphRatio;
						
						String Folderpath="F:/workspace/git/data/SSingnal"+"/"+subFolder;
						System.out.println("Folder--"+Folderpath);
						File file = new File(Folderpath);
						if (!file.exists()) {
							 if (file.mkdirs()) {
						            System.out.println("directory created successfully");
						        } else {
						            System.out.println("directory is not created");
						        }
							
						}
						double q_news = 1.0D / q_twitter;
						String resultFileNameOrigin =Folderpath+ "\\testGraph_" + day + "_qTwitter_" + q_twitter + "_qNews_" + q_news
								+ "_.txt";
						//String resultFileNameTwitterFix = "/testGraph_" + date + "_qTwitter_1.0_qNews_" + q_news + "_.txt";
						//String resultFileNameNewsFix = "/testGraph_" + date + "_qTwitter_" + q_twitter + "_qNews_1.0_.txt";
						//System.out.println(Paths.get(rootFolder, subFolder, resultFileNameOrigin).toAbsolutePath());
						
						int iterations = 100;
						generateTestGraph(rootFolder, day, restartProb, trueSubGraphRatio, q_twitter, q_news,
								Paths.get(resultFileNameOrigin),
								null,null, iterations);
					}
				}
			}
		}
		
		//The days only added twitter signal		
		for(String day:date_twitter){			
			for (double trueSubGraphRatio : trueSubGraphRatioArr) {
				for (double restartProb : restartProbArr) {
					for (double q_twitter : q_twitterArr) {
System.out.println("--------------------------------------");
						
						String subFolder = day+"/testGraph-" + day + "_restartProb_" + restartProb + "_trueRatio_"
								+ trueSubGraphRatio;
						
						String Folderpath="F:/workspace/git/data/SSingnal"+"/"+subFolder;
						System.out.println("Folder--"+Folderpath);
						File file = new File(Folderpath);
						if (!file.exists()) {
							 if (file.mkdirs()) {
						            System.out.println("directory created successfully");
						        } else {
						            System.out.println("directory is not created");
						        }
							
						}
						double q_news = 0.0D;
						String resultFileNameOrigin =Folderpath+ "\\testGraph_" + day + "_qTwitter_" + q_twitter + "_qNews_" + q_news
								+ "_.txt";
						//String resultFileNameTwitterFix = "/testGraph_" + date + "_qTwitter_1.0_qNews_" + q_news + "_.txt";
						//String resultFileNameNewsFix = "/testGraph_" + date + "_qTwitter_" + q_twitter + "_qNews_1.0_.txt";
						//System.out.println(Paths.get(rootFolder, subFolder, resultFileNameOrigin).toAbsolutePath());
						
						int iterations = 100;
						generateTestGraph(rootFolder, day, restartProb, trueSubGraphRatio, q_twitter, q_news,
								Paths.get(resultFileNameOrigin),
								null,null, iterations);
					}
				}
			}
		}
		
		//The days only added news signal		
		for(String day:date_news){			
			for (double trueSubGraphRatio : trueSubGraphRatioArr) {
				for (double restartProb : restartProbArr) {
					for (double q_twitter : q_twitterArr) {
System.out.println("--------------------------------------");
						
						String subFolder = day+"/testGraph-" + day + "_restartProb_" + restartProb + "_trueRatio_"
								+ trueSubGraphRatio;
						
						String Folderpath="F:/workspace/git/data/SSingnal"+"/"+subFolder;
						System.out.println("Folder--"+Folderpath);
						File file = new File(Folderpath);
						if (!file.exists()) {
							 if (file.mkdirs()) {
						            System.out.println("directory created successfully");
						        } else {
						            System.out.println("directory is not created");
						        }
							
						}
						double q_news = 1.0D / q_twitter;
						q_twitter=0.0D;
						String resultFileNameOrigin =Folderpath+ "\\testGraph_" + day + "_qTwitter_" + q_twitter + "_qNews_" + q_news
								+ "_.txt";
						//String resultFileNameTwitterFix = "/testGraph_" + date + "_qTwitter_1.0_qNews_" + q_news + "_.txt";
						//String resultFileNameNewsFix = "/testGraph_" + date + "_qTwitter_" + q_twitter + "_qNews_1.0_.txt";
						//System.out.println(Paths.get(rootFolder, subFolder, resultFileNameOrigin).toAbsolutePath());
						
						int iterations = 100;
						generateTestGraph(rootFolder, day, restartProb, trueSubGraphRatio, q_twitter, q_news,
								Paths.get(resultFileNameOrigin),
								null,null, iterations);
					}
				}
			}
		}
	}

	public static void main(String args[]) {
		//generateSingleCase();
		generateTestCase();
	}

}
