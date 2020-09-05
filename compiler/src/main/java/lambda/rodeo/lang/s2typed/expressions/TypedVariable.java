package lambda.rodeo.lang.s2typed.expressions;

import static org.objectweb.asm.Opcodes.ALOAD;

import lambda.rodeo.lang.s1ast.expressions.ExpressionAst;
import lambda.rodeo.lang.s1ast.expressions.VariableAst;
import lambda.rodeo.lang.compilation.S1CompileContext;
import lambda.rodeo.lang.s3compileable.expression.Compileable;
import lambda.rodeo.lang.s3compileable.expression.CompileableExpr;
import lambda.rodeo.lang.s3compileable.expression.SimpleCompilableExpr;
import lambda.rodeo.lang.scope.TypeScope.Entry;
import lambda.rodeo.runtime.types.CompileableType;
import lambda.rodeo.runtime.types.LambdaRodeoType;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.objectweb.asm.MethodVisitor;

@Getter
@Builder
@EqualsAndHashCode
public class TypedVariable implements TypedExpression, Compileable {

  private final VariableAst variableAst;
  private final Entry scopeEntry;

  @Override
  public CompileableType getType() {
    return scopeEntry.getType();
  }

  @Override
  public ExpressionAst getExpr() {
    return variableAst;
  }

  @Override
  public CompileableExpr toCompileableExpr() {
    return SimpleCompilableExpr.builder()
        .typedExpression(this)
        .compileable(this)
        .build();
  }

  @Override
  public void compile(MethodVisitor methodVisitor, S1CompileContext compileContext) {
    methodVisitor.visitVarInsn(ALOAD, scopeEntry.getIndex());
  }

}
