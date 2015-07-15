package ca.verworn.ponceau.steel.net;

import static ca.verworn.ponceau.steel.util.Logger.Out;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import static java.lang.System.in;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.fourthline.cling.UpnpService;
import org.fourthline.cling.UpnpServiceImpl;
import org.fourthline.cling.support.igd.PortMappingListener;
import org.fourthline.cling.support.model.PortMapping;

/**
 *
 * @author Evan Verworn <evan@verworn.ca>
 */
public class UDPService extends Thread {

    private static DatagramSocket serverSocket;
    private static List<InetAddress> others = new ArrayList<>();

    public static void Broadcast(DatagramPacket data) throws IOException {
        for (InetAddress addr : others) {
            data.setAddress(addr);
            serverSocket.send(data);
        }
    }

    private boolean GREEN_LIGHT = true;

    public void tearDown() {
        GREEN_LIGHT = false;
    }

    @Override
    public void run() {
        UpnpService service = null;
        byte[] receiveData = new byte[1024];
        byte[] sendData;

        int listenPort = 0;
        int targetPort = 0;
        String targetHost = "";

        try {

            BufferedReader input = new BufferedReader(new InputStreamReader(in));
            System.out.print("Port: ");
            listenPort = Integer.parseInt(input.readLine());
            String localAddr = InetAddress.getLocalHost().getHostAddress();
            PortMapping desiredMapping
                    = new PortMapping(
                            listenPort,
                            localAddr,
                            PortMapping.Protocol.UDP,
                            "Ponceau Steel"
                    );
            service = new UpnpServiceImpl(
                    new PortMappingListener(desiredMapping)
            );
            service.getControlPoint().search();

            serverSocket = new DatagramSocket(listenPort, InetAddress.getLocalHost());
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            Out("Server listening...", localAddr, listenPort);
            while (GREEN_LIGHT) {
                serverSocket.receive(receivePacket);
                receivePacket.getData();
                //System.out.println("RECEIVED: " + sentence);
                InetAddress IPAddress = receivePacket.getAddress();
                int port = receivePacket.getPort();
            }
        } catch (Exception e) {
            Out("UDP Exception", e.getLocalizedMessage());
        } finally {
            serverSocket.close();
            service.shutdown();
        }
    }

}
