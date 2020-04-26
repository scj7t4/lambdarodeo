package lambda.rodeo.lang.expressions;

import static org.objectweb.asm.Opcodes.DUP;
import static org.objectweb.asm.Opcodes.INVOKESPECIAL;
import static org.objectweb.asm.Opcodes.NEW;

import lambda.rodeo.lang.statements.TypeScope;
import lambda.rodeo.lang.types.Atom;
import lambda.rodeo.lang.types.Type;
import lambda.rodeo.lang.values.Computable;
import lombok.Builder;
import lombok.Getter;
import org.objectweb.asm.MethodVisitor;

@Builder
@Getter
public class AtomAst implements ExpressionAst {

  private final Atom atom;

  @Override
  public Type getType(TypeScope typeScope) {
    return atom;
  }

  @Override
  public Computable<?> getComputable() {
    return (scope) -> atom;
  }

  @Override
  public void compile(MethodVisitor methodVisitor) {
    methodVisitor.visitTypeInsn(NEW, "lambda/rodeo/lang/types/Atom");
    methodVisitor.visitInsn(DUP);
    methodVisitor.visitLdcInsn(atom.getNameLiteral());
    methodVisitor.visitMethodInsn(
        INVOKESPECIAL,
        "lambda/rodeo/lang/types/Atom",
        "<init>",
        "(Ljava/lang/String;)V",
        false);

  }
}
