
package ca.verworn.ponceau.steel.net.commands;

import ca.verworn.ponceau.steel.net.Service;
import ca.verworn.ponceau.steel.net.ServiceAnnotation.Key;
import ca.verworn.ponceau.steel.net.ServiceAnnotation.Subscribe;
import static ca.verworn.ponceau.steel.util.Logger.Panda;
import java.util.UUID;

/**
 *
 * @author Evan Verworn <evan@verworn.ca>
 */
public class PING implements Service{
    @Key public UUID key = UUID.randomUUID();
    
    public PING() {
        Panda("test empty constructor");
    }
    public PING(UUID remotekey) {
        Panda("test full constructor");
        key = remotekey;
    }
    
    @Subscribe(endpoint = "ping")
    public String ping() {
        Panda("PONG");
        return "PONG";
    }
}
