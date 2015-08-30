package ca.verworn.ponceau.steel.util;

/**
 *
 * @author Evan Verworn <evan@verworn.ca>
 */
public class Logger {
    // because who doesn't want to see a panda.
    public static void Panda(Object ... o) {
        System.out.print(System.currentTimeMillis() + ": ");
        if(o.length == 0) {
            StackTraceElement[] trace = Thread.currentThread().getStackTrace();
            for(int i = 2; i < trace.length; i++) // snip this function call
                System.out.println(trace[i].toString());
        } else {
            for(Object i : o)
                System.out.print(i + " ");
        }
        System.out.println();
    }
}
