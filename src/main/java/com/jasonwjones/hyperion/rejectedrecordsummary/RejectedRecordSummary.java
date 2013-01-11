/**
 * Copyright 2013 Jason W. Jones
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
 * the License.
 */

package com.jasonwjones.hyperion.rejectedrecordsummary;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

/**
 * This class provides several methods for analyzing and summarizing information
 * about a set of rejected records, such as those loaded from an Essbase
 * rejected record file with help from the RejectedRecordProcesor class.
 * 
 * This class is immutable, because immutable stuff is cool. You can't/shouldn't
 * modify the data inside this object after it is constructed. If you process
 * more rows you would just create a new summary from the proessor.
 * 
 * @author Jason W. Jones
 * 
 */
public class RejectedRecordSummary {

	/**
	 * Internal list of the rejected entries. After construction this list will
	 * be sorted. This was chosen over using a SortedSet or other collection
	 * implementations for simplicity.
	 */
	private List<RejectedRecordEntry> entries;

	/**
	 * Constructs a new record summary based on a mapping of record names to
	 * reject counts. Note that the Map is not retained.
	 * 
	 * @param counts map of member name to reject count
	 */
	public RejectedRecordSummary(Map<String, Integer> counts) {
		entries = new ArrayList<RejectedRecordEntry>();
		for (Entry<String, Integer> reject : counts.entrySet()) {
			RejectedRecordEntry entry = new RejectedRecordEntry(reject.getKey(), reject.getValue());
			entries.add(entry);
		}
		Collections.sort(entries, RejectedRecordEntry.MOST_REJECTED_FIRST);
	}

	/**
	 * Get the number of rejected records (counts of all individual)
	 * 
	 * @return total for rejected records
	 */
	public int getTotalRejectedRecords() {
		return calcRejectedRecords(entries);
	}

	/**
	 * Helper method that adds the reject counts for a list of records
	 * 
	 * @param recordEntries the record entries to sum up
	 * @return the sum of the reject counts for all the given records
	 */
	private int calcRejectedRecords(List<RejectedRecordEntry> recordEntries) {
		int sum = 0;
		for (RejectedRecordEntry entry : recordEntries) {
			sum += entry.getRejectCount();
		}
		return sum;
	}

	/**
	 * The number of unique rejected record reasons
	 * 
	 * @return number of different records (missing member names) that
	 *         contributed to a rejected record
	 */
	public int getNumberOfUniqueRejects() {
		return entries.size();
	}

	/**
	 * Gets a list of the member names that contributed to a rejected record,
	 * irrespective of count.
	 * 
	 * @return set of member names
	 */
	public Set<String> getRejectedRecordMemberNames() {
		Set<String> rejectedMemberNames = new HashSet<String>();
		for (RejectedRecordEntry entry : entries) {
			rejectedMemberNames.add(entry.getMemberName());
		}
		return rejectedMemberNames;
	}

	/**
	 * Calculates what percentage of the total records that the top X rejected
	 * entries account for.
	 * 
	 * @param top the number unique reject reasons to look at
	 * @return a percentage from 0 to 1
	 */
	public float getTopRejectPercentOfTotal(int top) {
		if (entries.size() == 0) return 0;
		int topRejectCount = calcRejectedRecords(getMostRejectedRecords(top));
		int totalRejectedRecords = getTotalRejectedRecords();
		return (float) topRejectCount / totalRejectedRecords;
	}

	/**
	 * Get a list of the top most rejected entries in the dataset(s)
	 * 
	 * @param top the number of top entries for the list. If greater than the
	 *            size of the list, the list is returned.
	 * @return a list with the top most rejected entries
	 */
	public List<RejectedRecordEntry> getMostRejectedRecords(int top) {
		if (top > entries.size()) top = entries.size();
		return entries.subList(0, top);
	}

	/**
	 * Get all of the rejected entries
	 * 
	 * @return an immutable list of all the rejected record entries
	 */
	public List<RejectedRecordEntry> getRejectedRecords() {
		return Collections.unmodifiableList(entries);
	}

	@Override
	public String toString() {
		return "RejectedRecordSummary [entries=" + entries + "]";
	}

}
