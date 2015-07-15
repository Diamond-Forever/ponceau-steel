package ca.verworn.ponceau.steel.net;

/**
 *
 * @author Evan Verworn <evan@verworn.ca>
 */
public interface Service {
  // a Service MUST contain a field denoted by @Key that is serializable
  
  // a Service SHOULD contain at least one @Publish annotion above a method
  // that returns a non nullable value.
  
  // a Service CAN contain @Subscribe annotations  above methods
}
