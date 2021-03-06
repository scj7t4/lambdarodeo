package lambda.rodeo.lang.s1ast.expressions;

import static org.objectweb.asm.Opcodes.DUP;
import static org.objectweb.asm.Opcodes.INVOKESPECIAL;
import static org.objectweb.asm.Opcodes.NEW;

import java.util.Collections;
import java.util.Set;
import lambda.rodeo.lang.s1ast.functions.ToTypedFunctionContext;
import lambda.rodeo.lang.s3compileable.expression.CompileableExpression;
import lambda.rodeo.lang.compilation.S1CompileContext;
import lambda.rodeo.lang.s2typed.expressions.SimpleTypedExpression;
import lambda.rodeo.lang.scope.TypeScope;
import lambda.rodeo.lang.scope.TypedModuleScope;
import lambda.rodeo.lang.types.CompileableAtom;
import lambda.rodeo.runtime.types.Atom;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

@Builder
@Getter
@EqualsAndHashCode
public class AtomAst implements ExpressionAst, CompileableExpression {

  private final CompileableAtom atom;
  private final int startLine;
  private final int endLine;
  private final int characterStart;

  public static SimpleTypedExpression undefinedAtomExpression() {
    return builder()
        .atom(CompileableAtom.UNDEFINED)
        .build()
        .toTypedExpression();
  }

  public SimpleTypedExpression toTypedExpression() {
    return SimpleTypedExpression.builder()
        .toCompileable(() -> this)
        .type(atom)
        .expr(this)
        .build();
  }

  @Override
  public SimpleTypedExpression toTypedExpression(TypeScope typeScope,
      TypedModuleScope typedModuleScope, ToTypedFunctionContext compileContext) {
    return toTypedExpression();
  }

  @Override
  public Set<String> getReferencedVariables() {
    return Collections.emptySet();
  }

  @Override
  public void compile(MethodVisitor methodVisitor,
      S1CompileContext compileContext) {
    methodVisitor.visitTypeInsn(NEW, Type.getInternalName(Atom.class));
    methodVisitor.visitInsn(DUP);
    methodVisitor.visitLdcInsn(atom.getNameLiteral());
    methodVisitor.visitMethodInsn(
        INVOKESPECIAL,
        Type.getInternalName(Atom.class),
        "<init>",
        "(Ljava/lang/String;)V",
        false);

  }
}
