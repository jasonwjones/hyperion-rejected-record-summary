package com.jasonwjones.hyperion.rejectedrecordsummary;

import java.io.PrintStream;

public class RejectedRecordSummaryPrinter {

	public void print(RejectedRecordSummary summary) {
		print(summary, System.out);
	}

	public void print(RejectedRecordSummary summary, PrintStream printStream) {
		printStream.println("Number of rejected records: " + summary.getTotalRejectedRecords());
		printStream.println("Number of unique rejects  : " + summary.getNumberOfUniqueRejects());
		// printStream.println("Rej records: " +
		// summary.getRejectedRecordMemberNames());
		printStream.println("Top 5 rejected: " + summary.getMostRejectedRecords(5));

		System.out.printf("Percent: %3.2f %%\n", 100 * summary.getTopRejectPercentOfTotal(100));

		printStream.println("Top 5 rejected records account for " + summary.getTopRejectPercentOfTotal(5)
				+ " percent of rejected records.");
	}
}
