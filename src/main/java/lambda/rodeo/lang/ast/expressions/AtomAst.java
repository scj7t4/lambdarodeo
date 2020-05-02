package lambda.rodeo.lang.ast.expressions;

import static org.objectweb.asm.Opcodes.DUP;
import static org.objectweb.asm.Opcodes.INVOKESPECIAL;
import static org.objectweb.asm.Opcodes.NEW;

import lambda.rodeo.lang.compilation.CompileContext;
import lambda.rodeo.lang.ast.statements.TypeScope;
import lambda.rodeo.lang.typed.expressions.SimpleTypedExpressionAst;
import lambda.rodeo.lang.types.Atom;
import lombok.Builder;
import lombok.Getter;
import org.objectweb.asm.MethodVisitor;

@Builder
@Getter
public class AtomAst implements ExpressionAst, CompileableExpr {

  private final Atom atom;
  private final int startLine;
  private final int endLine;
  private final int characterStart;

  @Override
  public SimpleTypedExpressionAst toTypedExpressionAst(TypeScope typeScope, CompileContext compileContext) {
    return SimpleTypedExpressionAst.builder()
        .compileableExpr(this::compile)
        .type(atom)
        .expr(this)
        .build();
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
