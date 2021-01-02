package lambda.rodeo.lang.s2typed.expressions;

import static org.objectweb.asm.Opcodes.ALOAD;

import lambda.rodeo.lang.compilation.CollectsErrors;
import lambda.rodeo.lang.compilation.S1CompileContext;
import lambda.rodeo.lang.s1ast.expressions.ExpressionAst;
import lambda.rodeo.lang.s1ast.expressions.VariableAst;
import lambda.rodeo.lang.s3compileable.expression.CompileableExpr;
import lambda.rodeo.lang.s3compileable.expression.CompileableExpression;
import lambda.rodeo.lang.s3compileable.expression.SimpleCompilableExpr;
import lambda.rodeo.lang.scope.Entry;
import lambda.rodeo.lang.types.CompileableType;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.objectweb.asm.MethodVisitor;

@Getter
@Builder
@EqualsAndHashCode
public class TypedVariable implements TypedExpression, CompileableExpression {

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
  public CompileableExpr toCompileableExpr(
      CollectsErrors compileContext) {
    return SimpleCompilableExpr.builder()
        .typedExpression(this)
        .compileable(this)
        .build();
  }

  @Override
  public void compile(MethodVisitor methodVisitor, S1CompileContext compileContext) {
    scopeEntry.compileLoad(methodVisitor);
  }

}
