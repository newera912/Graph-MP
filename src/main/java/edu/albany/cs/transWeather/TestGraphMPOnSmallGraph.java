package edu.albany.cs.transWeather;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

import edu.albany.cs.apdmIO.APDMInputFormat;
import edu.albany.cs.base.Utils;
import edu.albany.cs.graph.TransWeatherGraph;
import edu.albany.cs.graphMP.GraphMP;
import edu.albany.cs.scoreFuncs.EMSStat2;
import edu.albany.cs.scoreFuncs.EMSStat;
import edu.albany.cs.scoreFuncs.Function;
//import edu.albany.cs.scoreFuncs.;

public class TestGraphMPOnSmallGraph {
	
	public static int verboseLevel = 0;
	public static void testSingleFilePR(String singleFile, String resultFileName) {
		long startTime = System.nanoTime();
		APDMInputFormat apdm = new APDMInputFormat(singleFile);
		//TransWeatherGraph graph = new TransWeatherGraph(apdm);
		//TransWeatherGraph graph = new TransWeatherGraph(apdm,"grid");
		if (verboseLevel > 0) {
//			System.out.println("X: " + Arrays.toString(Arrays.copyOf(graph.x, 5)));
//			System.out.println("mean: " + Arrays.toString(Arrays.copyOf(graph.mu, 5)));
//			System.out.println("std: " + Arrays.toString(Arrays.copyOf(graph.sigma, 5)));			
//			System.out.println("trueSubGraphSize: " + graph.trueSubGraph.length);
//			Utils.stop();
		}

		int s = 1000;
		int g = 1;
		double B = s - g + 0.0D;
		int t = 3;
		boolean singleNodeInitial = false;
		
		double[] X = apdm.data.counts;
		//double[] mean = graph.mu;
		

		if (verboseLevel > 0) {
			
			System.out.println("X: " + Arrays.toString(Arrays.copyOf(X, 10)));
			//System.out.println("Y: " + Arrays.toString(Arrays.copyOf(mean, 10)));
			for (int i : apdm.data.trueSubGraphNodes) {
				System.out.print(X[i] + " ");
			}
			System.out.println();
//			for (int i : apdm.data.trueSubGraphNodes) {
//				System.out.print(mean[i] + " ");
//			}
		
			
			System.out.println();
		}
		System.out.println("X: " + Arrays.toString(Arrays.copyOf(X, 10)));
		//System.out.println("Mu: " + Arrays.toString(Arrays.copyOf(graph.mu, 10)));
		Function func = new EMSStat2(X);
		GraphMP graphMP = new GraphMP(apdm.data.intEdges, apdm.data.edgeCosts, X, s, g, B, t, null, func, null, null);
		int[] resultNodes = graphMP.resultNodes_Tail;
		int[] trueNodes = apdm.data.trueSubGraphNodes;

		if (verboseLevel > 0) {
			Arrays.sort(resultNodes);
			Arrays.sort(trueNodes);
			System.out.println(resultNodes.length+" "+Arrays.toString(resultNodes));
			System.out.println(trueNodes.length+" "+Arrays.toString(trueNodes));
		}

		int[] intersect = Utils.intersect(resultNodes, trueNodes);
		double precision = (intersect.length * 1.0D / resultNodes.length * 1.0D);
		double recall = (intersect.length * 1.0D / trueNodes.length * 1.0D);
		try {
			if (resultFileName == null) {
			} else {
				FileWriter fileWriter = new FileWriter(resultFileName, true);
				fileWriter.write(" " + precision + " " + recall + "\n");
				fileWriter.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println("--------------------------------------------------");
		System.out.println("ResultNodes: "+resultNodes.length+" "+Arrays.toString(resultNodes));
		System.out.println("TrueNodes: "+trueNodes.length+" "+Arrays.toString(trueNodes));
		System.out.println("precision: " + precision +" recall: " + recall);		
		System.out.println("--------------------------------------------------");		
		System.out.println();
	}
	public static void testSingleFile(String singleFile, String resultFileName) {
		long startTime = System.nanoTime();
		APDMInputFormat apdm = new APDMInputFormat(singleFile);
		//TransWeatherGraph graph = new TransWeatherGraph(apdm);
		//TransWeatherGraph graph = new TransWeatherGraph(apdm,"grid");
		if (verboseLevel > 0) {
			System.out.println("X: " + Arrays.toString(Arrays.copyOf(apdm.data.counts, 5)));
			//System.out.println("mean: " + Arrays.toString(Arrays.copyOf(graph.mu, 5)));
			//System.out.println("std: " + Arrays.toString(Arrays.copyOf(graph.sigma, 5)));			
			//System.out.println("trueSubGraphSize: " + graph.trueSubGraph.length);
			Utils.stop();
		}

		int s =6;
		int g = 1;
		double B = s - g + 0.0D;
		int t = 3;
		boolean singleNodeInitial = false;
		
		double[] X = apdm.data.counts;
		//double[] mean = graph.mu;
		

		if (verboseLevel > 0) {
			
			System.out.println("X: " + Arrays.toString(Arrays.copyOf(X, 10)));
			//System.out.println("Y: " + Arrays.toString(Arrays.copyOf(mean, 10)));
			for (int i : apdm.data.trueSubGraphNodes) {
				System.out.print(X[i] + " ");
			}
			System.out.println();
//			for (int i : apdm.data.trueSubGraphNodes) {
//				System.out.print(mean[i] + " ");
//			}
		
			
			System.out.println();
		}
		
		//System.out.println("Mu: " + Arrays.toString(Arrays.copyOf(mu, 10)));
		Function func = new EMSStat2(X);
		GraphMP graphMP = new GraphMP(apdm.data.intEdges, apdm.data.edgeCosts, X, s, g, B, t, null, func, null, null);
		int[] resultNodes = graphMP.resultNodes_Tail;
		//int[] trueNodes = graph.trueSubGraph;

		if (verboseLevel > 0) {
			Arrays.sort(resultNodes);
			//Arrays.sort(trueNodes);
			System.out.println("Result Station ID: "+Arrays.toString(resultNodes));
			//System.out.println(trueNodes.length+" "+Arrays.toString(trueNodes));
		}


		System.out.println("--------------------------------------------------");
		System.out.println("X: " + Arrays.toString(X));
		System.out.println("Result nodes: "+Arrays.toString(resultNodes));
		System.out.println("running time: " + (System.nanoTime() - startTime) / 1e9);
		
		System.out.println();
	}
	public static void test_Noise_Signal(){
		String folder="data/NYS_Test_Temp_stdSignal2/20160101/";
		testSingleFile("data/mesonet_data/testAPDM.txt","data/mesonet_data/testAPDM.txt");
		
	}
	
	public static void test_Noise_Signal10(){
		String folder="data/simu10/case1/";
		for(File apdmFile : new File(folder).listFiles()){
			testSingleFilePR(folder+apdmFile.getName(), "data/simu10/case1.txt");
		}
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//testSingleFile("data/temperatureData/apdm/testGraph_0.05_20160116.23.hrrr.wrfsfcf00.NYS_TempData.247_257.txt", "outputs/ADPM_Noise_Signal_Test_result.txt");
		//testSingleFile("data/Grid-Data-100/APDM-GridData-100-precen-0.05-noise_0.txt", "outputs/Test_result.txt");
		//testGraph_0.1_20160116.23.hrrr.wrfsfcf00.NYS_TempData.247_257.txt
		//test_Noise_Signal10();
		test_Noise_Signal();
	}
	
}
