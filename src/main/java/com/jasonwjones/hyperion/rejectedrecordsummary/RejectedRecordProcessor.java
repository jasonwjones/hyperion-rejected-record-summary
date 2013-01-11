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

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

/**
 * Main processor class. This class analyzes the input (rejected record files)
 * which can be a filename or just an InputStream (System.in or provided by you
 * from a file elsewhere). This class will generate a summary on demand.
 * 
 * There should be a 1:1 correlation between instances of this processor and
 * rejected record contexts. In other words, you would use just one instance of
 * this class to process a particular reject file or set of reject files from
 * the same load rule or process.
 * 
 * @author Jason W. Jones
 * 
 */
public class RejectedRecordProcessor {

	/**
	 * Internal representation of missing member names and counts. This data
	 * structure happens to be the exact same as needed for the one constructor
	 * to RejectedRecordSummary, so be careful if you feel like changing this.
	 */
	private Map<String, Integer> unknownMembers = new HashMap<String, Integer>();;

	/**
	 * Prefix/suffix to the start/end of a valid rejected record line. Note that
	 * these intentionally have a space on the end and beginning, respectively.
	 * It affects how the data is parsed out.
	 */
	private final static String BEGIN_TEXT = "\\\\ Member ";
	private final static String END_TEXT = " Not Found In Database";

	/**
	 * Pre-computed length of the prefix string that we'll use to determine the
	 * start of the member name we extract with subtring()
	 */
	private final static int BEGIN_TEXT_LENGTH = BEGIN_TEXT.length();

	/**
	 * Process a file into the stats of this object (which are used to build a
	 * summary).
	 * 
	 * @param filename the name of the file
	 * @throws FileNotFoundException if file not found
	 * @throws IOException if error reading file
	 */
	public void process(String filename) throws FileNotFoundException, IOException {
		process(new FileReader(filename));
	}

	/**
	 * Process a given input stream (such as created from a file or even an
	 * object in a database) -- data is data
	 * 
	 * @param inputStream the inputStream to read
	 * @throws IOException if there is an error reading the stream
	 */
	public void process(InputStream inputStream) throws IOException {
		process(new InputStreamReader(inputStream));
	}

	/**
	 * Processes any given Reader. Note that this is used by the other
	 * constructors that boil down their input types to a Reader and call this.
	 * 
	 * @param reader the reader to process
	 * @throws IOException if an error reading it
	 */
	public void process(Reader reader) throws IOException {
		BufferedReader bufferedReader = new BufferedReader(reader);
		for (String line = bufferedReader.readLine(); line != null; line = bufferedReader.readLine()) {
			processLine(line);
		}
		bufferedReader.close();
	}

	/**
	 * Creates an [immutable] summary object based on all of the files/streams
	 * that have been processed by this processor.
	 * 
	 * @return a summary object for the processed rows
	 */
	public RejectedRecordSummary createSummary() {
		return new RejectedRecordSummary(unknownMembers);
	}

	/**
	 * Processes a single line from a reject file/stream
	 * 
	 * @param line the line to process
	 */
	private void processLine(String line) {
		if (line.startsWith(BEGIN_TEXT)) {
			int startOfNotFound = line.indexOf(END_TEXT);
			String unknownMember = line.substring(BEGIN_TEXT_LENGTH, startOfNotFound);
			incrementNotFound(unknownMember);
		}
	}

	/**
	 * Increments the not found count for a particular member name
	 * 
	 * @param memberName the member name to increment
	 */
	private void incrementNotFound(String memberName) {
		Integer count = unknownMembers.containsKey(memberName) ? unknownMembers.get(memberName) + 1 : 1;
		unknownMembers.put(memberName, count);
	}

	/**
	 * Callable main method so that this library can be run standalone if
	 * desired.
	 * 
	 * @param args the list of files to process -- if none, System.in will be
	 *            used.
	 */
	public static void main(String[] args) {
		RejectedRecordProcessor processor = new RejectedRecordProcessor();

		try {
			if (args.length == 0) {
				processor.process(System.in);
			} else {
				for (String filename : args) {
					processor.process(filename);
				}
			}
		} catch (FileNotFoundException e) {
			System.err.println("File error: " + e.getMessage());
			System.exit(1);
		} catch (IOException e) {
			System.err.println("Error reading from file: " + e.getMessage());
			System.exit(1);
		}

		RejectedRecordSummaryPrinter printer = new RejectedRecordSummaryPrinter();
		printer.print(processor.createSummary());
	}

}
