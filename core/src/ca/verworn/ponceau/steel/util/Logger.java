package ca.verworn.ponceau.steel.util;

/**
 *
 * @author Evan Verworn <evan@verworn.ca>
 */
public class Logger {
    public static void Panda(Object ... o) {
        System.out.print(System.currentTimeMillis() + ": ");
        for(Object i : o) {
            System.out.print(i + " ");
        }
        System.out.println();
    }
}
