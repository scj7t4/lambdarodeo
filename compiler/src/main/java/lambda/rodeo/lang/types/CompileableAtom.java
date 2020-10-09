package lambda.rodeo.lang.types;

import static org.objectweb.asm.Opcodes.DUP;
import static org.objectweb.asm.Opcodes.INVOKESPECIAL;
import static org.objectweb.asm.Opcodes.NEW;

import lambda.rodeo.runtime.types.Atom;
import lombok.EqualsAndHashCode;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

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
    return Type.getDescriptor(Atom.class);
  }

  @Override
  public String getInternalName() {
    return Type.getInternalName(Atom.class);
  }

  @Override
  public void provideRuntimeType(MethodVisitor methodVisitor) {
    methodVisitor.visitTypeInsn(NEW, "lambda/rodeo/runtime/types/Atom");
    methodVisitor.visitInsn(DUP);
    methodVisitor.visitLdcInsn(atom);
    methodVisitor.visitMethodInsn(INVOKESPECIAL,
        Type.getInternalName(Atom.class),
        "<init>",
        "(Ljava/lang/String;)V",
        false);
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
