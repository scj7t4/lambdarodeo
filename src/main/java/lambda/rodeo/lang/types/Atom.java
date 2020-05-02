package lambda.rodeo.lang.types;

import lambda.rodeo.lang.ast.expressions.AtomAst;
import lambda.rodeo.lang.ast.functions.Result;
import lambda.rodeo.lang.typed.TypedModule;
import lambda.rodeo.lang.typed.expressions.SimpleTypedExpression;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class Atom implements Type, Result, CompileableType {

  public static final Atom UNDEFINED = new Atom("$UNDEFINED");
  public static final Atom NULL = new Atom("null");

  private final String atom;

  public Atom(String atom) {
    this.atom = atom;
  }

  @Override
  public String toString() {
    return ":" + atom;
  }

  public String getNameLiteral() {
    return this.atom;
  }

  @Override
  public Atom get() {
    return this;
  }

  @Override
  public Class<?> javaType() {
    return Atom.class;
  }

  @Override
  public CompileableType toCompileableType(TypedModule typedModule) {
    return this;
  }

  public SimpleTypedExpression toTypedExpressionAst() {
    return SimpleTypedExpression.builder()
        .type(this)
        .expr(AtomAst.builder()
            .atom(this)
            .build())
        .build();
  }

  @Override
  public Type getType() {
    return this;
  }
}
