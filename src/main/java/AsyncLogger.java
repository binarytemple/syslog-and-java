import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Formatter;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public final class AsyncLogger {

    private static LinkedBlockingQueue<String> bq = null;

    private static ConcurrentMap<File, AsyncLogger> instances = new ConcurrentHashMap<>();

    private static AtomicReference<File> logpath = new AtomicReference<>();

    private static boolean cont = true;


    private BufferedWriter writer = null;

    private AsyncLogger(File path) {
        bq = new LinkedBlockingQueue<String>();

        try {
            String name = "logger-" + new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSSZ").format(new Date());
            Path logFile = Paths.get(path.getAbsolutePath(), name);
            Charset charset = Charset.forName("UTF-8");
            writer = Files.newBufferedWriter(logFile, charset, StandardOpenOption.APPEND, StandardOpenOption.CREATE_NEW);
            System.err.println("Logging to : " + logFile.toUri());
            Runnable r = () -> {
                while (true) {
                    if (cont == true) {
                        ///System.out.println("polling" + new Date());
                        try {
                            String poll = bq.poll(500, TimeUnit.MILLISECONDS);
                            if (poll != null) {
                                //System.out.println(new Formatter().format("%s %s", path, poll));
                                writer.write(poll);
                                writer.flush();
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else {
                        try {
                            writer.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        break;
                    }
                }
                bq = null;
            };
            new Thread(r).start();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static File getLogpath() {
        return logpath.get();
    }

    public static void setLogpath(File logpath) {
        AsyncLogger.logpath.set(logpath);
    }

    public synchronized static AsyncLogger getInstance(final File path) {
        return instances.computeIfAbsent(path, x -> new AsyncLogger(x));
    }

    public void stop() {
        cont = false;
    }

    public void write(String message) {
        try {
            bq.put(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}