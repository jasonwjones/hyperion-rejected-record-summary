package com.jasonwjones.hyperion.rejectedrecordsummary;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

public class RejectedRecordProcessor {

	private Map<String, Integer> unknownMembers = new HashMap<String, Integer>();;

	private final static String beginText = "\\\\ Member";
	private final static String endText = " Not Found In Database";

	public void process(String filename) throws FileNotFoundException, IOException {
		FileReader fileReader = new FileReader(filename);
		process(fileReader);
	}

	public void process(InputStream inputStream) throws IOException {
		process(new InputStreamReader(inputStream));
	}

	public void process(Reader reader) throws IOException {
		BufferedReader bufferedReader = new BufferedReader(reader);
		String line;
		while ((line = bufferedReader.readLine()) != null) {
			processLine(line);
		}

	}

	public RejectedRecordSummary createSummary() {
		return new RejectedRecordSummary(unknownMembers);
	}

	private void processLine(String line) {
		if (line.startsWith(beginText)) {
			int startOfNotFound = line.indexOf(endText);
			String unknownMember = line.substring(10, startOfNotFound);
			incrementNotFound(unknownMember);
		}
	}

	private void incrementNotFound(String memberName) {
		if (unknownMembers.containsKey(memberName)) {
			Integer count = unknownMembers.get(memberName);
			unknownMembers.put(memberName, count + 1);
		} else {
			unknownMembers.put(memberName, 1);
		}
	}

	public static void main(String[] args) {

		RejectedRecordProcessor processor = new RejectedRecordProcessor();

		try {
			if (args.length == 0) {
				processor.process(System.in);
			} else {
				String filename = "/Users/jasonwjones/Development/saxbi-workspace/rejected-record-summary/src/test/resources/sample1.txt";
				processor.process(filename);
			}
		} catch (FileNotFoundException e) {
			System.err.println("Could not find file with name: " + e.getMessage());
			System.exit(1);
		} catch (IOException e) {
			System.err.println("Error reading from file: " + e.getMessage());
			System.exit(1);
		}

		RejectedRecordSummary summary = processor.createSummary();
		RejectedRecordSummaryPrinter printer = new RejectedRecordSummaryPrinter();
		printer.print(summary);
	}

	public static void usage() {
		System.out.println("Usage");
	}

}
