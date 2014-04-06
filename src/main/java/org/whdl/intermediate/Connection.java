package org.whdl.intermediate;

import java.util.HashMap;
import java.util.Map;

public class Connection extends Value {
  private String instanceName;
  private ConnectionTypeDefinition definition;
  public ConnectionTypeDefinition getDefinition(){
    return this.definition;
  }
  
  private Map<String, Value> attributes;
  private Endpoint endpointFrom, endpointTo;
  
  public Connection(String instanceName, ConnectionTypeDefinition definition, Endpoint endpointFrom, Endpoint endpointTo){
    this.instanceName = instanceName;
    this.definition = definition;
    this.attributes = new HashMap<String, Value>();
    this.endpointFrom = endpointFrom;
    this.endpointTo = endpointTo;
  }

  @Override
  public Type acceptVisitor(ValueTypeVisitor v) {
    return v.visit(this);
  }
}