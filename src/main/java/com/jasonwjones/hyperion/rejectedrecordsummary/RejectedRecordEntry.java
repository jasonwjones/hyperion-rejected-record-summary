package com.jasonwjones.hyperion.rejectedrecordsummary;

import java.util.Comparator;

public class RejectedRecordEntry implements Comparable<RejectedRecordEntry> {

	private String memberName;
	private Integer rejectCount;

	public static final Comparator<RejectedRecordEntry> MOST_REJECTED_FIRST = new Comparator<RejectedRecordEntry>() {
		public int compare(RejectedRecordEntry left, RejectedRecordEntry right) {
			return right.rejectCount.compareTo(left.rejectCount);
		}
	};

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
