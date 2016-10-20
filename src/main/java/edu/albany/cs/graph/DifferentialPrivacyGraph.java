package edu.albany.cs.graph;

import java.util.Arrays;

import edu.albany.cs.apdmIO.APDMInputFormat;

public class DifferentialPrivacyGraph extends Graph {

	/** each node i has an observation x[i] */
	public final double[] x;
	/** each node i has expectation of observation x[i] */
	public final double[] mu;
	/** each node i has standard deviation of observation x[i] */
	public final double[] sigma;
	/** b is the constant parameter of Laplace distribution, phi = b */
	public final double b;

	public DifferentialPrivacyGraph(APDMInputFormat apdm, double[] x, double[] mu, double[] sigma, double b) {
		super(apdm);
		this.x = x;
		this.mu = mu;
		this.sigma = sigma;
		this.b = b;
	}

	@Override
	public String toString() {
		return "DifferentialPrivacyGraph [x=" + Arrays.toString(x) + ", mu=" + Arrays.toString(mu) + ", sigma="
				+ Arrays.toString(sigma) + ", b=" + b + "]";
	}
	

}
