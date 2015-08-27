package ca.verworn.ponceau.steel.net;

import ca.verworn.ponceau.steel.net.ServiceAnnotation.Key;
import ca.verworn.ponceau.steel.net.ServiceAnnotation.Publish;
import ca.verworn.ponceau.steel.net.ServiceAnnotation.Subscribe;
import static ca.verworn.ponceau.steel.util.Logger.Panda;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketAddress;
import java.util.TimerTask;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.nustaq.serialization.FSTConfiguration;

/**
 * RCP.Publish(T implements Service); RCP.Send(id, name, value);
 *
 * @author Evan
 */
public class RCP {
//  HashMap<String, APIEndpoint> api = new HashMap<String, APIEndpoint>();

    public static class MethodEntry<T> {

        public MethodEntry(Method m, T a) {
            method = m;
            annotation = a;
        }
        Method method;
        T annotation;
        TimerTask task;
    }

    public static class ServiceDictionary {

        public boolean isRemote = false;
        Service obj;
        ConcurrentHashMap<String, MethodEntry<Publish>> Pubs = new ConcurrentHashMap<>();
        ConcurrentHashMap<String, MethodEntry<Subscribe>> Subs = new ConcurrentHashMap<>();
        UUID Key;
    }

    public static ConcurrentHashMap<UUID, ServiceDictionary> services = new ConcurrentHashMap<>();

    public static ServiceDictionary Publish(Service s) {
        ServiceDictionary tmp = new ServiceDictionary();
        Class clazz = s.getClass();
        for (Field f : clazz.getFields()) {
            Key annotation = f.getAnnotation(Key.class);
            if (annotation != null) {
                try {
                    tmp.Key = (UUID) f.get(s);
                } catch (IllegalArgumentException ex) {
                    Logger.getLogger(RCP.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IllegalAccessException ex) {
                    Logger.getLogger(RCP.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;
            }
        }
        for (Method m : clazz.getMethods()) {
            Publish pub = m.getAnnotation(Publish.class);
            Subscribe sub = m.getAnnotation(Subscribe.class);
            if (pub != null) {
                tmp.Pubs.put(m.getName(), new MethodEntry<>(m, pub));
            } else if (sub != null) {
                tmp.Subs.put(m.getName(), new MethodEntry<>(m, sub));
            }
        }
        tmp.obj = s;
        return services.put(tmp.Key, tmp);
    }

    public static void Call(UUID key, String name, Object o) {
        ServiceDictionary service = services.get(key);
        if (service == null) {
            try {
                Class<?> clazz = Class.forName(name);
                Constructor<?> ctor = clazz.getConstructor(UUID.class);
                Service object = (Service) ctor.newInstance(new Object[]{key});
                service = Publish(object);
                service.isRemote = true;
            } catch (Exception e) {
                Panda(e.getLocalizedMessage());
            }
            return;
        }
        MethodEntry<Subscribe> method = service.Subs.get(name);
        if(method == null) {
            Panda("No Method", name);
            return; // fuck it
        }
        try {
            method.method.invoke(service.obj, method.method.getParameterTypes()[0].cast(o));
        } catch (Exception ex) {
            Logger.getLogger(RCP.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static long last_packet = 0;
    public static FSTConfiguration serializer = FSTConfiguration.createDefaultConfiguration();

    public static void Send(UUID key, String name, Serializable o) {
        DatagramStucture envelope = new DatagramStucture();
        envelope.key = key;
        envelope.name = name;
        envelope.order = last_packet++;
        envelope.value = o;

        byte[] outbound = serializer.asByteArray(envelope);
        DatagramPacket data = new DatagramPacket(outbound, outbound.length);
        
        try {
            UDPService.Broadcast(data);
        } catch (IOException ex) {
            Logger.getLogger(RCP.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static ConcurrentLinkedQueue<DatagramStucture> theQueue = new ConcurrentLinkedQueue<>();

    public static void handleIncoming() {
        for (DatagramStucture gram : theQueue) {
            RCP.Call(gram.key, gram.name, gram.value);
        }
    }

    private static byte[] buffer;
    private static DatagramSocket socket;
    private static SocketAddress theOtherGuy;
    public static Thread theThread = null;

    @Deprecated
    public static void Start() {
        try {
            socket = new DatagramSocket(6789);
        } catch (Exception ex) {
            Logger.getLogger(RCP.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (theThread != null && theThread.isAlive()) {
            theThread.stop();
        }
        theThread = new Thread(new Runnable() {
            @Override
            public void run() {
                DatagramPacket tmp = null;
                try {
                    socket.receive(tmp);
                    theOtherGuy = tmp.getSocketAddress();
                    DatagramStucture d = (DatagramStucture) serializer.asObject(tmp.getData());
                    Call(d.key, d.name, d.value);
                } catch (IOException ex) {
                    Logger.getLogger(RCP.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        theThread.start();
    }
}
