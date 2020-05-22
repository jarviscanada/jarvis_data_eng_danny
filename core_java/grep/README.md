# Java GREP  
## Introduction  
This application is an implementation of GREP's most basic features in Java. In short, it attempts to match a regular expression by searching through a given directory. There are two implementations of this program included, one with stream APIs and one without. This project was made using IntellijIDEA, and is intended to be built using the provided `pom.xml`.
  
First, the program will recursively traverse the provided directory, retrieving all file paths along the way. Then it will read each file line-by-line using `BufferedReader` and save the contents to a `List`, producing a `List` of `String` for each file. The program will then analyze each line for a regex match, writing matched lines to a designated output file using `BufferedWriter`.  
  
## Usage  
`Program arguments: [regex string] [root directory] [output file]`  
`e.g. \d+ /home/centos/dev /home/centos/results.txt`  
`regex string`: The regular expression that will be used for comparison.  
`root directory`: The directory where the traversal begins.  
`output file`: The file that will receive results on completion.

## Pseudocode
matches = []  
for file in listFiles(rootDir)  
     for line in readLines(file)  
          if containsPattern(line)  
               matchedLines.add(line)  
writeToFile(matchedLines)  

## Performance Issue
The program has some limitations with regards to reading larger files. The method implemented to read files must return a `List` representing the file's contents. In effect, this means the entire file must be stored in memory to be processed. This is not an issue with most files, but an `OutOfMemoryError` will occur when the file's contents exceed main memory capacity. 

## Improvements
- Completely revamp program interface and implementation to process files with streams, preventing memory issues.  
- Validate the provided regular expression for correctness.  
- Provide different regex comparison options (i.e. match part of a line instead of the whole line).
