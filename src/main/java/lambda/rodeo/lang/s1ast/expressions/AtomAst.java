package lambda.rodeo.lang.s1ast.expressions;

import static org.objectweb.asm.Opcodes.DUP;
import static org.objectweb.asm.Opcodes.INVOKESPECIAL;
import static org.objectweb.asm.Opcodes.NEW;

import lambda.rodeo.lang.s3compileable.expression.Compileable;
import lambda.rodeo.lang.compilation.CompileContext;
import lambda.rodeo.lang.scope.TypeScope;
import lambda.rodeo.lang.s2typed.expressions.SimpleTypedExpression;
import lambda.rodeo.lang.scope.TypedModuleScope;
import lambda.rodeo.lang.types.Atom;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.objectweb.asm.MethodVisitor;

@Builder
@Getter
@EqualsAndHashCode
public class AtomAst implements ExpressionAst, Compileable {

  private final Atom atom;
  private final int startLine;
  private final int endLine;
  private final int characterStart;

  public SimpleTypedExpression toTypedExpression() {
    return SimpleTypedExpression.builder()
        .toCompileable(() -> this)
        .type(atom)
        .expr(this)
        .build();
  }

  @Override
  public SimpleTypedExpression toTypedExpression(TypeScope typeScope,
      TypedModuleScope typedModuleScope, CompileContext compileContext) {
    return toTypedExpression();
  }

  @Override
  public void compile(MethodVisitor methodVisitor,
      CompileContext compileContext) {
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
