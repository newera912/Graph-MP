package edu.albany.cs.graph;

import java.util.ArrayList;

import edu.albany.cs.apdmIO.APDMInputFormat;

public class MexicoTwitterLocalNewsGraph extends Graph {

	public String[] words = null;
	public double[] news_counts = null;
	public double[] median_news_counts = null;
	public double[] twitter_counts = null;
	public double[] median_twitter_counts = null;
	public ArrayList<Double> edgeCosts;

	public MexicoTwitterLocalNewsGraph(APDMInputFormat apdm) {
		super(apdm);
		words = apdm.data.words;
		news_counts = apdm.data.attributes[0];
		median_news_counts = apdm.data.attributes[1];
		twitter_counts = apdm.data.attributes[2];
		median_twitter_counts = apdm.data.attributes[3];

		edgeCosts = new ArrayList<Double>();
		for (int i = 0; i < apdm.data.edgeCosts.size(); i++) {
			edgeCosts.add(1.0D);
		}
		run();
	}

	private void run() {
		for (int i = 0; i < news_counts.length; i++) {
			if (news_counts[i] <= 0.0D) {
				news_counts[i] = 0.1D;
			}
		}
		for (int i = 0; i < median_news_counts.length; i++) {
			if (median_news_counts[i] <= 0.0D) {
				median_news_counts[i] = 0.1D;
			}
		}
		for (int i = 0; i < twitter_counts.length; i++) {
			if (twitter_counts[i] <= 0.0D) {
				twitter_counts[i] = 0.1D;
			}
		}
		for (int i = 0; i < median_twitter_counts.length; i++) {
			if (median_twitter_counts[i] <= 0.0D) {
				median_twitter_counts[i] = 0.1D;
			}
		}
	}

}
