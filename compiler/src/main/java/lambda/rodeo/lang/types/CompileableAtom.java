package lambda.rodeo.lang.types;

import lambda.rodeo.runtime.types.Atom;
import lambda.rodeo.runtime.types.asm.AsmType;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class CompileableAtom implements LambdaRodeoType, CompileableType {

  public static final CompileableAtom UNDEFINED = new CompileableAtom("$UNDEFINED");
  public static final CompileableAtom NULL = new CompileableAtom("null");

  private final String atom;

  public CompileableAtom(String atom) {
    this.atom = atom;
  }

  @Override
  public String toString() {
    return "Compileable:" + atom;
  }

  public String getNameLiteral() {
    return this.atom;
  }

  @Override
  public String getDescriptor() {
    return AsmType.getDescriptor(Atom.class);
  }

  @Override
  public String getInternalName() {
    return AsmType.getInternalName(Atom.class);
  }

  @Override
  public CompileableType toCompileableType() {
    return this;
  }

  @Override
  public LambdaRodeoType getType() {
    return this;
  }
}
