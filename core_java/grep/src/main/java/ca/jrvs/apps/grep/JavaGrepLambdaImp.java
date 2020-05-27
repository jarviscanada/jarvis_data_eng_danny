package ca.jrvs.apps.grep;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.DirectoryIteratorException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.BasicConfigurator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JavaGrepLambdaImp extends JavaGrepImp{

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

    JavaGrepLambdaImp javaGrepImp = new JavaGrepLambdaImp();
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
    List<File> completeList = listFiles(getRootPath());
    completeList.forEach(file -> {
      readLines(file).forEach(line -> {
        if (containsPattern(line)) {
          matchedLines.add(line);
        }
      });
    });
    writeToFile(matchedLines);
  }

  @Override
  public List<String> readLines(File inputFile) {
    if (!inputFile.isFile()) {
      throw new IllegalArgumentException(inputFile.getName() + " is not a file.");
    }

    List<String> lines= new ArrayList<String>();
    try {
      lines = Files.readAllLines(inputFile.toPath(), Charset.defaultCharset());
    } catch (IOException ioe) {
      logger.error(ioe.getMessage(), ioe);
    }
    return lines;
  }

  @Override
  public List<File> listFiles(String rootDir) {
    List<File> files = new ArrayList<File>();
    try {
      DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(rootDir));
      stream.forEach(path -> {
        if (Files.isDirectory(path)) {
          files.addAll(listFiles(path.toString()));
        } else {
          files.add(path.toFile());
        }
      });
      stream.close();
    } catch (DirectoryIteratorException | IOException e) {
      logger.error(e.getMessage(), e);
    }
    return files;
  }
}
