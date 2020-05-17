package lambda.rodeo.lang.s1ast.expressions;

import java.util.Collections;
import java.util.Set;
import lambda.rodeo.lang.compilation.CompileContext;
import lambda.rodeo.lang.s1ast.functions.ToTypedFunctionContext;
import lambda.rodeo.lang.s2typed.expressions.TypedExpression;
import lambda.rodeo.lang.s3compileable.expression.CompileableExpr;
import lambda.rodeo.lang.scope.TypeScope;
import lambda.rodeo.lang.scope.TypedModuleScope;
import lambda.rodeo.runtime.types.LambdaRodeoType;
import lambda.rodeo.runtime.types.StringType;
import lombok.Builder;
import lombok.Getter;
import org.objectweb.asm.MethodVisitor;

@Builder
@Getter
public class StringLiteralAst implements ExpressionAst, TypedExpression, CompileableExpr {
  private final String contents;
  int startLine;
  int endLine;
  int characterStart;

  @Override
  public TypedExpression toTypedExpression(TypeScope scope, TypedModuleScope typedModuleScope,
      ToTypedFunctionContext compileContext) {
    return this;
  }

  @Override
  public Set<String> getReferencedVariables() {
    return Collections.emptySet();
  }

  @Override
  public LambdaRodeoType getType() {
    return StringType.INSTANCE;
  }

  @Override
  public ExpressionAst getExpr() {
    return this;
  }

  @Override
  public CompileableExpr toCompileableExpr() {
    return this;
  }

  @Override
  public TypedExpression getTypedExpression() {
    return this;
  }

  @Override
  public void compile(MethodVisitor methodVisitor, CompileContext compileContext) {
    methodVisitor.visitLdcInsn(contents);
  }
}
