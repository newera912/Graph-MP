package edu.albany.cs.graph;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.math3.distribution.BinomialDistribution;
import org.apache.commons.math3.distribution.PoissonDistribution;

import edu.albany.cs.base.Edge;

public class GenerateMSRealWindow {

	private int numOfTrueNodes;
	private double restartProb;
	private double q_news;
	private double q_twitter;
	private ArrayList<ArrayList<Integer>> adj;
	private Double[] median_news_counts;
	private Double[] median_twitter_counts;
	private int iterations;

	public Double[][] news_counts = null;
	public Double[][] twitter_counts = null;
	public ArrayList<Edge> treEdges;

	public GenerateMSRealWindow(ArrayList<ArrayList<Integer>> adj, Double[][] news_counts, Double[][] twitter_counts,
			Double[] median_news_counts, Double[] median_twitter_counts, double q_news, double q_twitter,
			double restartProb, double trueSubGraphRatio, int iterations) {
		this.q_news = q_news;
		this.q_twitter = q_twitter;
		this.twitter_counts = twitter_counts;
		this.median_twitter_counts = median_twitter_counts;
		this.news_counts = news_counts;
		this.median_news_counts = median_news_counts;
		this.adj = adj;
		this.numOfTrueNodes = (int) ((twitter_counts.length + 0.0D) * trueSubGraphRatio);
		this.restartProb = restartProb;
		this.iterations = iterations;
		generateGridDataWithNoise();
	}

	private void generateGridDataWithNoise() {

		int[] trueNodes = null;
		treEdges = randomWalk(adj, numOfTrueNodes, restartProb, iterations);
		for (Edge e : treEdges) {
			if (!ArrayUtils.contains(trueNodes, e.i)) {
				trueNodes = ArrayUtils.add(trueNodes, e.i);
			}
			if (!ArrayUtils.contains(trueNodes, e.j)) {
				trueNodes = ArrayUtils.add(trueNodes, e.j);
			}
		}
		//1-4 days  
		for (int i : trueNodes) {
			for(int j=0;j<4;j++){
			news_counts[i][j] = (double) new PoissonDistribution(q_news * (double) median_news_counts[i]).sample();
			twitter_counts[i][j] = (double) new PoissonDistribution(q_twitter * (double) median_twitter_counts[i])
					.sample();
			}
		}
	}

	private ArrayList<Edge> randomWalk(ArrayList<ArrayList<Integer>> arr, int numTrueNodes, double restartProb,
			double iterations) {
		double minCut = Double.MAX_VALUE;
		ArrayList<Edge> bestTrueSubGraph = null;
		for (int i = 0; i < iterations; i++) {
			ArrayList<Edge> trueSubGraph = new ArrayList<Edge>();
			Random random = new Random();
			int start = random.nextInt(arr.size());
			int size = numTrueNodes;
			HashSet<Integer> h = new HashSet<Integer>();
			h.add(start);
			int count = 0;
			int initialStart = start;
			while (h.size() < size) {
				int randomNeibIndex = random.nextInt(arr.get(start).size());
				int next = arr.get(start).get(randomNeibIndex);
				if (h.contains(next)) {
				} else {
					h.add(next);
					trueSubGraph.add(new Edge(start, next, count++, 1.0D));
				}

				BinomialDistribution bi = new BinomialDistribution(1, restartProb);
				if (bi.sample() == 1) {
					start = initialStart;
				} else {
					start = next;
				}
			}
			if (trueSubGraph.size() != (h.size() - 1)) {
				System.out.println("random walk must be wrong... ");
				System.exit(0);
			}
			double cut = getGraphCut(h, arr);
			if (cut < minCut) {
				minCut = cut;
				bestTrueSubGraph = trueSubGraph;
				System.out.println("current best cut: " + minCut);
			}
		}
		return bestTrueSubGraph;
	}

	private double getGraphCut(HashSet<Integer> trueSubGraph, ArrayList<ArrayList<Integer>> arr) {
		double cut = 0.0D;
		for (int i : trueSubGraph) {
			for (int j : arr.get(i)) {
				if (!trueSubGraph.contains(j)) {
					cut += 1.0D;
				}
			}
		}
		return cut;
	}

}
