package ca.verworn.ponceau.steel.net;

import java.io.Serializable;
import java.util.UUID;

/**
 *
 * @author Evan Verworn <evan@verworn.ca>
 */
public class DatagramStucture implements Serializable{
    public long order;
    public UUID key;
    public String name;
    public Serializable value;
    
}
