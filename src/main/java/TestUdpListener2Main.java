import org.apache.log4j.Logger;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 * A really dumb legacy java logger
 */
public class TestUdpListener2Main {

    public static void main(String[] args) throws Exception {
        Logger log = Logger.getLogger(TestUdpListener2Main.class.getSimpleName());

        log.info("STARTING");

        DatagramPacket packet = new DatagramPacket(new byte[2048], 2048);
        boolean finished = false;
        DatagramSocket serverSocket = new DatagramSocket(11154);
        while (!finished) {
            serverSocket.receive(packet);
            log.info(new String(packet.getData(), 0, packet.getLength()));
        }
        serverSocket.close();
    }
}
