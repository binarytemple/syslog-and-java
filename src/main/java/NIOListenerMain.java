import org.apache.commons.cli.*;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.net.StandardProtocolFamily;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.DatagramChannel;
import java.util.Properties;

/**
 * Fast NIO 2.0 syslog listener
 *
 * @author Bryan Hunt
 */
public class NIOListenerMain {

    public static void main(String[] args) throws ParseException {


        Properties props = new Properties();
        Options options = new Options();
        Option help = new Option("help", "Print help information");
        options.addOption(help);

        Option port = OptionBuilder.withArgName("port")
                .hasArg()
                .withDescription("listening port (standard 514, default 11514)")
                .create("port");
        options.addOption(port);

        Option outdir = OptionBuilder.withArgName("outdir")
                .hasArg()
                .withDescription("file to store logfiles")
                .create("port");
        options.addOption(port);


        Option ip = OptionBuilder.withArgName("ip")
                .hasArg()
                .withDescription("listening ip addrees (defaults to 127.0.0.1")
                .create("ip");
        options.addOption(ip);


        CommandLineParser parser = new GnuParser();
        CommandLine cmd = parser.parse(options, args);
        HelpFormatter formatter = new HelpFormatter();

        if (cmd.hasOption("help")) {
            formatter.printHelp("syslog listener", options);
            System.exit(0);
        }

        final int LOCAL_PORT = new Integer(cmd.getOptionValue("port", "11514"));
        final String LOCAL_IP = cmd.getOptionValue("ip", "127.0.0.1");
        final String pathname = cmd.getOptionValue("outdir", "/tmp/");
        /** http://www.syslog.cc/ietf/autoarc/msg00855.html */
        final int MAX_PACKET_SIZE = 1024;

        ByteBuffer echoText = ByteBuffer.allocateDirect(MAX_PACKET_SIZE);
        AsyncLogger logger = AsyncLogger.getInstance(new File(pathname));
        try (DatagramChannel datagramChannel = DatagramChannel.open(StandardProtocolFamily.INET)) {
            if (datagramChannel.isOpen()) {
                System.out.println("Echo server was successfully opened!");
                datagramChannel.setOption(StandardSocketOptions.SO_RCVBUF, 1024);
                datagramChannel.bind(new InetSocketAddress(LOCAL_IP, LOCAL_PORT));

                while (true) {
                    SocketAddress clientAddress = datagramChannel.receive(echoText);
                    echoText.flip();
                    byte[] bytes = new byte[echoText.limit()];

                    //System.out.println("I have received " + echoText.limit() + " bytes from " + clientAddress.toString() + "! Sending them back ...");
                    echoText.get(bytes);

                    logger.write(new String(bytes, 0, echoText.limit()));
                    /* datagramChannel.send(echoText, clientAddress); */
                    echoText.clear();
                }
            } else {
                System.out.println("The channel cannot be opened!");
            }
        } catch (Exception ex) {
            if (ex instanceof ClosedChannelException) {
                System.err.println("The channel was unexpected closed ...");
            }
            if (ex instanceof SecurityException) {
                System.err.println("A security exception occured ...");
            }
            if (ex instanceof IOException) {
                System.err.println("An I/O error occured ...");
            }

            System.err.println("\n" + ex);
        }
    }
}
