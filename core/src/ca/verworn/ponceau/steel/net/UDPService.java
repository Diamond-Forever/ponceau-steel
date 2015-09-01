package ca.verworn.ponceau.steel.net;

import static ca.verworn.ponceau.steel.util.Logger.Panda;
import ca.verworn.ponceau.steel.util.MakeshiftInputReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import static java.lang.System.in;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.AbstractMap;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.List;
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
    private static final List<AbstractMap.SimpleEntry<InetAddress,Integer>> others = new ArrayList<>();
    
    public static ArrayList<Runnable> onConnectionCallback = new ArrayList<>();
    
    public static void Broadcast(DatagramPacket data) throws IOException {
        for (AbstractMap.SimpleEntry<InetAddress,Integer> addr : others) {
            data.setAddress(addr.getKey());
            data.setPort(addr.getValue());
            serverSocket.send(data);
        }
    }
    
    public static void addClient(SimpleEntry<InetAddress,Integer> person) {
        synchronized(others) {
            others.add(person);
        }
        fireConnectionEvent();
    }


    private boolean GREEN_LIGHT = true;

    public UDPService() { }
    public UDPService(String host, int port) {
        try {
            others.add(new AbstractMap.SimpleEntry<>(InetAddress.getByName(host), port));
        } catch (UnknownHostException ex) { }
    }

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
        
        MakeshiftInputReader threadedInput = new MakeshiftInputReader();

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
            Panda("Server listening...", localAddr, listenPort);
            threadedInput.start();
            SimpleEntry pair;
            while (GREEN_LIGHT) {
                serverSocket.receive(receivePacket);
                byte[] temp = receivePacket.getData();
                Panda("Recieved Data", receivePacket.getLength(), "bytes");
                RCP.theQueue.add((DatagramStucture) RCP.serializer.asObject(temp));
                InetAddress IPAddress = receivePacket.getAddress();
                int port = receivePacket.getPort();
                // probably shouldn't be creating new throwaway object every packet
                pair = new SimpleEntry(IPAddress, port);
                if(!others.contains(pair)){
                    Panda("new person!", pair.getValue());
                    addClient(pair);
                }
            }
        } catch (IOException | NumberFormatException e) {
            Panda("UDP Exception", e.getLocalizedMessage());
        } finally {
            serverSocket.close();
            service.shutdown();
            Panda("shutting down");
        }
    }

    private static void fireConnectionEvent() {
        for(Runnable r : onConnectionCallback)
                        r.run(); // ***should be on the main thread***
                                 // or at least in it's own thread
                                 // probable race conditions abound!
    }

}
