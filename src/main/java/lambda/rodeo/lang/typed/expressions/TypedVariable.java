package lambda.rodeo.lang.typed.expressions;

import static org.objectweb.asm.Opcodes.ALOAD;

import lambda.rodeo.lang.ast.expressions.ExpressionAst;
import lambda.rodeo.lang.ast.expressions.VariableAst;
import lambda.rodeo.lang.compilation.CompileContext;
import lambda.rodeo.lang.compileable.expression.Compileable;
import lambda.rodeo.lang.compileable.expression.CompileableExpr;
import lambda.rodeo.lang.compileable.expression.SimpleCompilableExpr;
import lambda.rodeo.lang.scope.CompileableModuleScope;
import lambda.rodeo.lang.scope.TypeScope.Entry;
import lambda.rodeo.lang.types.Type;
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
  public Type getType() {
    return scopeEntry.getType();
  }

  @Override
  public ExpressionAst getExpr() {
    return variableAst;
  }

  @Override
  public CompileableExpr toCompileableExpr(
      CompileableModuleScope compileableModuleScope) {
    return SimpleCompilableExpr.builder()
        .typedExpression(this)
        .compileable(this)
        .build();
  }

  @Override
  public void compile(MethodVisitor methodVisitor, CompileContext compileContext) {
    methodVisitor.visitVarInsn(ALOAD, scopeEntry.getIndex());
  }

}
