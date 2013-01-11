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

import static org.junit.Assert.*;

import java.io.InputStream;

import org.junit.Before;
import org.junit.Test;

public class RejectedRecordProcessorTest {

	private RejectedRecordProcessor processor;

	@Before
	public void setUp() throws Exception {
		processor = new RejectedRecordProcessor();
		InputStream testFile = getClass().getResourceAsStream("/sample1.txt");
		processor.process(testFile);
	}

	@Test
	public void testProcessInputStream() throws Exception {
		RejectedRecordSummary summary = processor.createSummary();
		assertEquals(50000, summary.getTotalRejectedRecords());
		assertEquals(9083, summary.getNumberOfUniqueRejects());
	}
}
