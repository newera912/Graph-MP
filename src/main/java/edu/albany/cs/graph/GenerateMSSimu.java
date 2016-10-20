package edu.albany.cs.graph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.math3.distribution.BinomialDistribution;
import org.apache.commons.math3.distribution.PoissonDistribution;
import org.apache.commons.math3.random.RandomDataGenerator;

import edu.albany.cs.apdmIO.APDMInputFormat;
import edu.albany.cs.base.Edge;
import edu.albany.cs.utils.GenerateSingleGrid;

public class GenerateMSSimu {

	private final int numOfNodes;
	private final int numOfTrueNodes;
	private final String fileName;
	private final double noiseLevel;
	private final double qx;
	private final double qy;
	private final double[] lambdaX;
	private final double[] lambdaY;
	private final double restartProb;
	public double cut;
	public static double minCut;
	GenerateSingleGrid singleGrid;

	public GenerateMSSimu(int numOfNodes, int numOfTrueNodes, double noiseLevel, String fileName, double qx,
			double qy, double[] lambdaX, double[] lambdaY, double restartProb) {
		this.numOfNodes = numOfNodes;
		this.numOfTrueNodes = numOfTrueNodes;
		this.fileName = fileName;
		this.noiseLevel = noiseLevel;
		this.qx = qx;
		this.qy = qy;
		this.lambdaX = lambdaX;
		this.lambdaY = lambdaY;
		this.restartProb = restartProb;
		this.singleGrid = new GenerateSingleGrid(numOfNodes);

		generateGridDataWithNoise();
	}

	private void generateGridDataWithNoise() {

		String usedAlgorithm = "NULL";
		String dataSource = "GridDataset";

		double[] attributeX = new double[numOfNodes];
		double[] attributeY = new double[numOfNodes];
		int[] trueNodes = null;
		ArrayList<Edge> treEdges = randomWalk(singleGrid.adj, numOfTrueNodes, restartProb);
		for (Edge e : treEdges) {
			if (!ArrayUtils.contains(trueNodes, e.i)) {
				trueNodes = ArrayUtils.add(trueNodes, e.i);
			}
			if (!ArrayUtils.contains(trueNodes, e.j)) {
				trueNodes = ArrayUtils.add(trueNodes, e.j);
			}
		}
		
		int[] normalNodes = ArrayUtils.removeElements(
				new RandomDataGenerator().nextPermutation(attributeX.length, attributeX.length), trueNodes);

		for (int i : normalNodes) {
			attributeX[i] = new PoissonDistribution(lambdaX[i]).sample();
			attributeY[i] = new PoissonDistribution(lambdaY[i]).sample();
		}
		for (int i : trueNodes) {
			attributeX[i] = new PoissonDistribution(qx * lambdaX[i]).sample();
			attributeY[i] = new PoissonDistribution(qy * lambdaY[i]).sample();
		}

		double[] copiedAttributeX = new double[attributeX.length];
		double[] copiedAttributeY = new double[attributeY.length];

		System.arraycopy(attributeX, 0, copiedAttributeX, 0, attributeX.length);
		System.arraycopy(attributeY, 0, copiedAttributeY, 0, attributeX.length);
		int noiseLevelInTrueNodes = (int) (((noiseLevel + 0.0) / 100) * (trueNodes.length));
		int[] alreadyDone = null;
		for (int k = 0; k < noiseLevelInTrueNodes; k++) {
			while (true) {
				int valueToFind = new Random().nextInt(trueNodes.length);
				if (!ArrayUtils.contains(alreadyDone, valueToFind)) {
					copiedAttributeX[trueNodes[valueToFind]] = new PoissonDistribution(lambdaX[trueNodes[valueToFind]])
							.sample();
					copiedAttributeY[trueNodes[valueToFind]] = new PoissonDistribution(lambdaY[trueNodes[valueToFind]])
							.sample();
					alreadyDone = ArrayUtils.add(alreadyDone, valueToFind);
					break;
				}
			}
		}
		int noiseLevelInNormalNodes = (int) (((noiseLevel + 0.0) / 100)
				* (copiedAttributeX.length - trueNodes.length + 0.0));
		alreadyDone = null;
		for (int j = 0; j < noiseLevelInNormalNodes; j++) {
			while (true) {
				int valueToFind = new Random().nextInt(normalNodes.length);
				if (!ArrayUtils.contains(alreadyDone, valueToFind)) {
					copiedAttributeX[normalNodes[valueToFind]] = new PoissonDistribution(
							qx * lambdaX[normalNodes[valueToFind]]).sample();
					copiedAttributeY[normalNodes[valueToFind]] = new PoissonDistribution(
							qy * lambdaY[normalNodes[valueToFind]]).sample();
					alreadyDone = ArrayUtils.add(alreadyDone, valueToFind);
					break;
				}
			}
		}
		attributeX = copiedAttributeX;
		attributeY = copiedAttributeY;
		if (this.cut < minCut) {
			String[] attributeNames = new String[] { "X", "Y", "lambdaX", "lambdaY" };
			APDMInputFormat.generateAPDMFile(usedAlgorithm, dataSource, singleGrid.edges, treEdges, fileName,
					attributeNames, attributeX, attributeY, lambdaX, lambdaY);
			minCut = this.cut;
			System.out.println("minCut: " + minCut);
		}

	}

	private ArrayList<Edge> randomWalk(ArrayList<ArrayList<Integer>> arr, int numTrueNodes, double restartProb) {

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
			h.add(next);
			trueSubGraph.add(new Edge(start, next, count++, 1.0D));
			BinomialDistribution bi = new BinomialDistribution(1, restartProb);
			if (bi.sample() == 1) {
				start = initialStart;
			} else {
				start = next;
			}
		}
		ArrayList<Edge> reducedTreEdges = new ArrayList<Edge>();
		for (Edge edge : trueSubGraph) {
			if (!checkExist(reducedTreEdges, edge)) {
				reducedTreEdges.add(edge);
			}
		}
		cut = getGraphCut(h, arr);
		return reducedTreEdges;
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

	private boolean checkExist(ArrayList<Edge> trueSubGraph, Edge edge) {
		if (trueSubGraph.isEmpty()) {
			return false;
		}
		for (Edge ed : trueSubGraph) {
			if ((ed.i == edge.i && ed.j == edge.j) || (ed.i == edge.j && ed.j == edge.i)) {
				return true;
			}
		}
		return false;
	}

	public static void main(String args[]) {
		GenerateMSSimu.minCut = Double.MAX_VALUE;
		double minCut = Double.MAX_VALUE;
		for (int i = 0; i < 100; i++) {
			int numOfNodes = 400;
			int numOfTrueNodes = 20;
			double noiseLevel = 0.0D;
			double k = 100.0D; // qx and qy
			double qx = k;
			double qy = 50.0D / k;
			double restartProb = 0.5;
			double[] lambdaX = new double[numOfNodes];
			double[] lambdaY = new double[numOfNodes];
			Arrays.fill(lambdaX, 10.0D); // lambdaX is a vector
			Arrays.fill(lambdaY, 10.0D); // lambdaX is a vector
			String fileName = "data/simuDataSet/APDM-GridData-" + numOfNodes + "_TrueSub-" + numOfTrueNodes + "_noise-"
					+ noiseLevel + ".txt";
			GenerateMSSimu gen = new GenerateMSSimu(numOfNodes, numOfTrueNodes, noiseLevel,
					fileName, qx, qy, lambdaX, lambdaY, restartProb);
			if (gen.cut < minCut) {
				minCut = gen.cut;
			}
		}

	}

}
