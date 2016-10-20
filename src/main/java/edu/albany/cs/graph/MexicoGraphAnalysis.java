package edu.albany.cs.graph;

import java.util.ArrayList;
import java.util.Arrays;

import org.apache.commons.lang3.ArrayUtils;

import edu.albany.cs.apdmIO.APDMInputFormat;
import edu.albany.cs.base.ConnectedComponents;

public class MexicoGraphAnalysis {

	public int verboseLevel = 0;

	public MexicoGraphAnalysis() {
		String mexicoDataRootFolder = "/home/baojian/Dropbox/data/ICDM-2016/mexicoData/";
		String subFolder = "testGraph-2014-03-02_restartProb_0.2_trueRatio_0.05/";
		String fileName = "testGraph_2014-03-02_qTwitter_100.0_qNews_0.01_.txt";
		String filePath = mexicoDataRootFolder + "/" + subFolder + "/" + fileName;
		APDMInputFormat apdm = new APDMInputFormat(filePath);
		MexicoTwitterLocalNewsGraph graph = new MexicoTwitterLocalNewsGraph(apdm);
		int[] arr = new int[graph.numOfNodes];
		for (int i = 0; i < arr.length; i++) {
			arr[i] = graph.arrayListAdj.get(i).size();
		}
		int lessThan100 = 0;
		int[] lessThan100Nodes = null;
		for (int i = 0; i < arr.length; i++) {
			if (arr[i] <= 100) {
				lessThan100++;
				lessThan100Nodes = ArrayUtils.add(lessThan100Nodes, i);
			}
		}
		ConnectedComponents cc = new ConnectedComponents(graph.arrayListAdj);
		cc.computeCCSubGraph(lessThan100Nodes);
		System.out.println("total # of CC is: "+cc.components.size());
		int[] sizeArr = new int[cc.components.size()];
		for(int i = 0 ; i < cc.components.size() ; i++){
			ArrayList<Integer> com = cc.components.get(i);
			//System.out.println("component["+i+"]: "+com.size());
			sizeArr[i] = com.size();
		}
		Arrays.sort(sizeArr);
		for (int i = 0; i < 100; i++) {
			System.out.println(sizeArr[sizeArr.length - i - 1]);
		}
	
		System.out.println("less than 100 : " + lessThan100);
		Arrays.sort(arr);
		if (verboseLevel > 0) {
			for (int i = 0; i < 100; i++) {
				System.out.println(arr[arr.length - i - 1]);
			}
			for (int i = 0; i < 1500; i++) {
				System.out.println(arr[i]);
			}
		}

	}

	public static void main(String args[]) {
		new MexicoGraphAnalysis();
	}

}
