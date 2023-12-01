package Server.socket;

import java.io.*;

public class LogFileHelper {
  private final BufferedWriter writer;

  public LogFileHelper(File outputFile) throws IOException {
    if(!outputFile.exists()){ //The JavaDoc says that it is not certain if the file will be created
      outputFile.createNewFile();
    }

    this.writer = new BufferedWriter(new FileWriter(outputFile, true));
  }

  public void writeLine(String line) throws IOException {
    if(line == null){
      throw new IllegalArgumentException("line may not be null");
    }

    this.writer.write(line);
    this.writer.newLine();
    this.writer.flush(); //Make sure the line we just wrote is written and kept if the application crashes
  }

  public void tryWriteLine(String line) {
    try {
      writeLine(line);
    } catch(IOException ioe){
      //Your exception handling here
    }
  }

  public void close() throws IOException {
    this.writer.close();
  }

  public void tryClose() {
    try {
      this.writer.close();
    } catch(IOException ioe){
      //Your exception handling here
    }
  }
}