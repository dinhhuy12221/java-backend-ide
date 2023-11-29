package Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.exec.PumpStreamHandler;
import org.apache.commons.exec.StreamPumper;

public class PumpStreamHandlerFixed extends PumpStreamHandler {
  public PumpStreamHandlerFixed() {
    super();
  }

  public PumpStreamHandlerFixed(OutputStream out, OutputStream err, InputStream input) {
    super(out, err, input);
  }

  public PumpStreamHandlerFixed(OutputStream out, OutputStream err) {
    super(out, err);
  }

  public PumpStreamHandlerFixed(OutputStream outAndErr) {
    super(outAndErr);
  }

  @Override
  protected Thread createPump(InputStream is, OutputStream os, boolean closeWhenExhausted) {
    os = new AutoFlushingOutputStream(os);

    final Thread result = new Thread(new StreamPumper(is, os, closeWhenExhausted), "Exec Stream Pumper");
    result.setDaemon(true);
    return result;
  }
}

class AutoFlushingOutputStream extends OutputStream {
  private final OutputStream decorated;

  public AutoFlushingOutputStream(OutputStream decorated) {
    this.decorated = decorated;
  }

  @Override
  public void write(byte[] b, int off, int len) throws IOException {
    this.decorated.write(b, off, len);
    this.decorated.flush();
  }

  @Override
  public void write(int b) throws IOException {
    this.decorated.write(b);
    this.decorated.flush();
  }

  @Override
  public void close() throws IOException {
    this.decorated.close();
  }

  @Override
  public void flush() throws IOException {
    this.decorated.flush();
  }
}