/* Copyright 2013 Jason W. Jones
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License. */

package com.jasonwjones.hyperion.rejectedrecordsummary;

import java.io.PrintStream;

/**
 * This is an extremely simple example class that is used to print/format the
 * statistics from the RejectedRecordSummary. If you wanted to pretty up your
 * own output you would create your own equivalent class or just pull the data
 * directly out of the summary object yourself.
 * 
 * @author Jason W. Jones
 * 
 */
public class RejectedRecordSummaryPrinter {

	public void print(RejectedRecordSummary summary) {
		print(summary, System.out);
	}

	public void print(RejectedRecordSummary summary, PrintStream printStream) {
		printStream.println("Number of rejected records: " + summary.getTotalRejectedRecords());
		printStream.println("Number of unique rejects  : " + summary.getNumberOfUniqueRejects());
		printStream.println("Top 5 rejected: ");

		for (RejectedRecordEntry entry : summary.getMostRejectedRecords(5)) {
			printStream.println(" - " + entry);
		}

		printStream.printf("Top 100 rejected members account for %3.2f %% of all rejected rows\n", 100 * summary.getTopRejectPercentOfTotal(100));
	}
}
