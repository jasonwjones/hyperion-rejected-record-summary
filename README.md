# Hyperion Rejected Record Summary

Hyperion Rejected Record Summary (rejected-record-summary) is a small Java library/command-line program for processing data from rejected record files generated during a data file load to a Hyperion Essbase OLAP cube.

As with most/all of my open source Hyperion utilities, it is the extension, elaboration, and cleanup of a concept I have used in the past in various production environments.

This program can be used on the command-line as well as a library that can be plugged easily in to your own Java projects. On the command line, this program can read from `STDIN` or be given a rejected record file to process. It will scan through the file and compile stats about the rejected records it finds. 


## Usefulness

The predecessor to this program was used as part of an automation process that would analyze the rejected records and put that information into an email that was sent to myself and others containing information about the records that could not be processed during an automation run. 

Instead of attaching the contents of the rejected record file itself, the high-level stats can be attached or printed instead for more useful at-a-glance functionality.

Additionally, the processing in this library is useful to be embedded in such contexts as a Dashboard, for example.

**What about perl/awk/grep/foo?**

You can achieve most/all of this functionality with a clever perl one-liner or piping things through some Unix command-line stuff. That's a dandy approach and actually the one this originally took years ago. But this method lends itself to putting in a servlet or otherwise integrating into more enterprisey contexts, so there you have it.

## Disclaimer

Rejected Record Summary comes with absolutely no warranty and I will not be held responsible for damaging your data, server, software, or anything related to these processes. Please use it at your own risk and always test in a development environment. Please let me know of any issues you run into so they can be addressed as time permits.

## Download

You can download the latest published version of Rejected Record Summary from [here](http://www.jasonwjones.com/downloads/rejected-record-summary/).

## Usage

RRS can be given a filename, multiple filenames, or process data from `STDIN`. For example, here would be an example of processing a single rejected record file:

    java -jar rejected-record-summary.jar reject-file.txt
   
You can also process multiple files:

	java -jar rejected-record-summary.jar reject-file1.txt reject-file2.txt
	
Or pipe data directly into it:

	cat reject-file.txt | java -jar rejected-record-summary.jar
	
Which is basically the same as this:

	java -jar rejected-record-summary.jar < reject-file.txt

## Example Output

Output is very rudimentary since that's meant to be customized by another Java program (see the method calls below in the Using as a Library section). Here's an example of output from the stock `RejectedRecordSummaryPrinter` that is included for demonstration purposes.

    Number of rejected records: 50000
    Number of unique rejects  : 9083
    Top 5 rejected: 
    - RejectedRecordEntry [memberName=Ac.0170126, rejectCount=24]
    - RejectedRecordEntry [memberName=Ac.0171026, rejectCount=24]
    - RejectedRecordEntry [memberName=Ac.0453902, rejectCount=24]
    - RejectedRecordEntry [memberName=Ac.0170926, rejectCount=24]
    - RejectedRecordEntry [memberName=Ac.0397511, rejectCount=24]
    Top 100 rejected members account for 4.12 % of all rejected rows

## Using as a Library

RRS provides a clean and tight API on its summary class that can be used to easily report some statistics for a single or set of rejected record data files.

Of note, the library provides such useful methods/functions as the following:

* `getTotalRejectedRecords()`: calculates a count of all of the rejected records (this is the same as the number of entries in the rejected record file)

* `getNumberOfUniqueRejects()`: returns the number of unique member names that contributed to a rejected record. Effectively this filters out the duplicates with respect to member names in the rejected record file(s)

* `getRejectedRecordMemberNames()`: returns just the set of member names that contributed to a rejected record

* `getMostRejectedRecords(int top)`: returns the member names and reject count for the top X most rejected member names sorted by how many records there were

* `getTopRejectPercentOfTotal()`: returns the percentage of total rejected records that the top X most common rejected member names contributed to, row count-wise

Note that the RejectedRecordSummary class is typically created from the RejectedRecordProcessor. The processor class provides several convenience functions for reading in data from a file or a stream, so you can easily program against it with various data sources.

## Building

Assuming you have all of the dependencies setup correctly on your system (Essbase jar files installed as artifacts) you can run a `mvn clean compile assembly:single` to create the runnable jar.

## License

RRS is licensed under the fairly liberal Apache Software License version 2. You can pretty much do anything you want with this code. Of course, if you have improvements they are welcome to be integrated back into the main codebase.
