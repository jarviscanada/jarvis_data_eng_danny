package ca.jrvs.apps.grep;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.io.File;
import java.io.IOException;
import java.util.List;
import org.apache.log4j.BasicConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JavaGrepImp implements JavaGrep{

  final Logger logger = LoggerFactory.getLogger(JavaGrep.class);
  private String regex;
  private String rootPath;
  private String outFile;

  public static void main(String[] args) {
    //configure logger
    BasicConfigurator.configure();

    if (args.length != 3) {
      throw new IllegalArgumentException("USAGE: JavaGrep regex rootPath outFile");
    }

    JavaGrepImp javaGrepImp = new JavaGrepImp();
    javaGrepImp.setRegex(args[0]);
    javaGrepImp.setRootPath(args[1]);
    javaGrepImp.setOutFile(args[2]);

    try {
      javaGrepImp.process();
    } catch (Exception ex) {
      javaGrepImp.logger.error(ex.getMessage(), ex);
    }
  }

  /**
   * Top level search workflow
   * @throws IOException
   */
  @Override
  public void process() throws IOException {
    List<String> matchedLines = new ArrayList<String>();
    List<File> completeList = listFiles(rootPath);
    for (File file: completeList) {
      for (String line: readLines(file)) {
        if (containsPattern(line)) {
          matchedLines.add(line);
        }
      }
    }
    writeToFile(matchedLines);
  }

  /**
   * Traverse a given directory and return all files
   * @param rootDir input directory
   * @return files under the rootDir
   */
  @Override
  public List<File> listFiles(String rootDir) {
    File[] topLevel = new File(rootDir).listFiles();
    List<File> files = new ArrayList<File>();
    for (File file: topLevel) {
      if (file.isDirectory()) {
        //recursively go through subdirectories
        List<File> nextLevel = listFiles(file.getAbsolutePath());
        if (nextLevel != null) {
          files.addAll(nextLevel);
        }
      } else {
        files.add(file);
      }
    }
    return files;
  }

  /**
   * Read a file and return all the lines
   *
   * Explain FileReader, BufferedReader, and character encoding
   *
   * @param inputFile file to be read
   * @return lines
   * @throws IllegalArgumentException if a given inputFile is not a file
   */
  @Override
  public List<String> readLines(File inputFile) {
    if (!inputFile.isFile()) {
      throw new IllegalArgumentException(inputFile.getName() + " is not a file.");
    }

    List<String> lines= new ArrayList<String>();
    try {
      BufferedReader br = new BufferedReader(new FileReader(inputFile));
      String line;
      while ((line = br.readLine()) != null) {
        lines.add(line);
      }
    } catch (IOException ioe) {
      logger.error(ioe.getMessage(), ioe);
    }
    return lines;
  }

  /**
   * check if a line contains the regex pattern (passed by user)
   * @param line input string
   * @return true if there is a match
   */
  @Override
  public boolean containsPattern(String line) {
    return line.matches(getRegex());
  }

  /**
   * Write lines to a file
   *
   * Explore: FileOutputStream, OutputStreamWriter, and BufferedWriter
   *
   * @param lines matched line
   * @throws IOException if write failed
   */
  @Override
  public void writeToFile(List<String> lines) throws IOException {
    try {
      BufferedWriter bw = new BufferedWriter(new FileWriter(getOutFile()));
      for (String line: lines) {
        bw.write(line);
        bw.write("\n");
        bw.flush();
      }
    } catch (IOException ioe) {
      logger.error(ioe.getMessage(), ioe);
    }
  }

  @Override
  public String getRootPath() {
    return this.rootPath;
  }

  @Override
  public void setRootPath(String rootPath) {
    this.rootPath = rootPath;
  }

  @Override
  public String getRegex() {
    return this.regex;
  }

  @Override
  public void setRegex(String regex) {
    this.regex = regex;
  }

  @Override
  public String getOutFile() {
    return this.outFile;
  }

  @Override
  public void setOutFile(String outFile) {
    this.outFile = outFile;
  }
}
