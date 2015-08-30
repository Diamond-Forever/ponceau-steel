
package ca.verworn.ponceau.steel.util;

import ca.verworn.ponceau.steel.net.RCP;
import ca.verworn.ponceau.steel.net.UDPService;
import ca.verworn.ponceau.steel.net.commands.PING;
import static ca.verworn.ponceau.steel.util.Logger.Panda;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import static java.lang.System.in;
import java.net.InetAddress;
import java.util.AbstractMap.SimpleEntry;

/**
 *  This function obviously is only for getting simple input without building
 * a UI with GDX components. The person who makes this class obsolete will be
 * crowned a hero to all in the kingdom.
 * @author Evan Verworn <evan@verworn.ca>
 */
public class MakeshiftInputReader extends Thread {

    SimpleEntry<InetAddress, Integer> pair = null;

    @Override
    public void run() {
        BufferedReader input = new BufferedReader(new InputStreamReader(in));
        Panda("Enter a fully qualified address with portnumber");
        Panda("eg. alpha.verworn.net:8900");
        try {
            String connectTo = input.readLine();
            InetAddress host = InetAddress.getByName(connectTo.split(":")[0]);
            int port = Integer.parseInt(connectTo.split(":")[1]);
            pair = new SimpleEntry<>(host, port);
            UDPService.addClient(pair);
            PING p = new PING();
            RCP.Publish(p);
            RCP.Send(p.key, "ping", "null isn't serializable");
        } catch (IOException ex) {
            Panda("error reading connection input", ex);
        }
    }

}
