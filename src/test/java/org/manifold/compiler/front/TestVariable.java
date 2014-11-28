package org.manifold.compiler.front;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.manifold.compiler.BooleanTypeValue;
import org.manifold.compiler.BooleanValue;
import org.manifold.compiler.TypeValue;
import org.manifold.compiler.Value;

public class TestVariable {
  private NamespaceIdentifier getNamespaceIdentifier() {
    return new NamespaceIdentifier("manifold::is::cool");
  }

  private VariableIdentifier getVariableIdentifier() {
    return new VariableIdentifier(getNamespaceIdentifier(), "foo");
  }

  private Scope getScope() {
    return new Scope();
  }

  private Variable getVariable() {
    // declare "foo" as a variable that stores a Type
    return new Variable(
        getScope(),
        getVariableIdentifier()
    );
  }

  private TypeValue getTypeValue() {
    return BooleanTypeValue.getInstance();
  }

  private Value getValue() {
    return BooleanValue.getInstance(true);
  }

  private Expression getTypeExpression(){
    return new LiteralExpression(getTypeValue());
  }

  private Expression getValueExpression(){
    return new LiteralExpression(getValue());
  }

  @Test
  public void testGetIdentifier() {
    assertEquals(getVariable().getIdentifier(), getVariableIdentifier());
  }

  /*
  @Test
  public void testGetTypeValue() throws TypeMismatchException {
    assertEquals(
        getVariable().getType(),
        getTypeExpression().getValue(getVariable().getScope())
    );
  }
  */

  @Test
  public void testGetValueUnassigned() throws VariableNotAssignedException {
    Variable v = getVariable();
    assertFalse(v.isAssigned());
    assertNull(v.getValue());
  }

  @Test
  public void testSetValue() throws
      MultipleAssignmentException,
      VariableNotAssignedException,
      TypeMismatchException {
    Variable v = getVariable();
    v.setValueExpression(getValueExpression());
    assertEquals(v.getValue(), getValue());
    v.verify(); // Variable should verify successfully after being set.
  }

  @Test(expected = MultipleAssignmentException.class)
  public void testSetValueMultiple() throws MultipleAssignmentException {
    Variable v = getVariable();
    v.setValueExpression(getValueExpression());
    v.setValueExpression(getValueExpression());
  }

  /*
  @Test(expected = TypeMismatchException.class)
  public void verify_nontypeThrow() throws
      TypeMismatchException,
      MultipleAssignmentException {

    Variable v = new Variable(
        getScope(),
        getVariableIdentifier()
    );
    v.verify();
  }

  @Test(expected = TypeMismatchException.class)
  public void verify_typeMismatchThrow() throws
      TypeMismatchException,
      MultipleAssignmentException {

    Variable v = new Variable(
        getScope(),
        getVariableIdentifier()
    );
    v.setValueExpression(new LiteralExpression(BooleanTypeValue.getInstance()));
    v.verify();
  }
  */
}
