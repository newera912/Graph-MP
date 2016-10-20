package edu.albany.cs.graph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import edu.albany.cs.apdmIO.APDMInputFormat;

public class MultiSourceGraph extends Graph {

	/** each node i has frequency x_i and y_i */
	public final double[] x;
	public final double[] y;
	public final double[] lambdaX;
	public final double[] lambdaY;
	public final double[][] laplacianMat;
	public final int[] R = new int[] { 1 }; // we set the length of R is 1 by
											// default

	public MultiSourceGraph(APDMInputFormat apdm, double[] x, double[] y, double[] lambdaX, double[] lambdaY) {
		super(apdm);
		this.x = x;
		this.y = y;
		this.lambdaX = lambdaX;
		this.lambdaY = lambdaY;
		double[][] laplacianMat = new double[super.numOfNodes][];
		for (int i = 0; i < laplacianMat.length; i++) {
			laplacianMat[i] = new double[super.numOfNodes];
			Arrays.fill(laplacianMat[i], 0.0D);
			laplacianMat[i][i] = apdm.data.graphAdjList.get(i).size();
		}
		for (Integer[] edge : super.edges) {
			laplacianMat[edge[0]][edge[1]] = -1;
		}
		this.laplacianMat = laplacianMat;
	}

	/**
	 * @param S
	 *            subset of nodes that cut the graph
	 * @return
	 */
	public double cut(int[] S) {
		double result = 0.0D;
		if (S != null) {

		}
		HashSet<Integer> setS = new HashSet<Integer>();
		for (int i : S) {
			setS.add(i);
		}
		for (int i : S) {
			ArrayList<Integer> neis = super.arrayListAdj.get(i);
			for (int n : neis) {
				if (!setS.contains(n)) {
					result += 1.0D;
				}
			}
		}
		return result;
	}

	@Override
	public String toString() {
		return "MultiSourceGraph [x=" + Arrays.toString(x) + ", y=" + Arrays.toString(y) + ", lambdaX="
				+ Arrays.toString(lambdaX) + ", lambdaY=" + Arrays.toString(lambdaY) + ", R=" + Arrays.toString(R)
				+ "]";
	}

}
