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

import java.util.Comparator;

/**
 * Very simple POJO for modeling a member name that was rejected from a load as
 * well as how many times it was rejected. This is an immutable data structure
 * that is used in the record summary after processing has been completed.
 * 
 * @author Jason W. Jones
 * 
 */
public class RejectedRecordEntry implements Comparable<RejectedRecordEntry> {

	/**
	 * Member name that is rejected, such as Ac.0170100 or January or whatnot
	 */
	private String memberName;

	/**
	 * Count of how many times this was rejected. Note that we use an
	 * Object/boxed int since we're going to need one anyway in the summary
	 */
	private Integer rejectCount;

	/**
	 * Convenience comparator that can be used by containers. Sorts records from
	 * highest to smallest reject count
	 */
	public static final Comparator<RejectedRecordEntry> MOST_REJECTED_FIRST = new Comparator<RejectedRecordEntry>() {
		public int compare(RejectedRecordEntry left, RejectedRecordEntry right) {
			return right.rejectCount.compareTo(left.rejectCount);
		}
	};

	/**
	 * Constructor creates things in one shot
	 * 
	 * @param memberName member name from rejects
	 * @param rejectCount total number of times it was rejected
	 */
	public RejectedRecordEntry(String memberName, Integer rejectCount) {
		this.memberName = memberName;
		this.rejectCount = rejectCount;
	}

	public String getMemberName() {
		return memberName;
	}

	public Integer getRejectCount() {
		return rejectCount;
	}

	/**
	 * Default Basis of comparison will simply be the count of records
	 */
	public int compareTo(RejectedRecordEntry o) {
		return rejectCount.compareTo(o.rejectCount);
	}

	@Override
	public String toString() {
		return "RejectedRecordEntry [memberName=" + memberName + ", rejectCount=" + rejectCount + "]";
	}

}
