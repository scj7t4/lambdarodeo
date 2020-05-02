package lambda.rodeo.lang.typed.expressions;

import static org.objectweb.asm.Opcodes.ALOAD;

import lambda.rodeo.lang.ast.expressions.ExpressionAst;
import lambda.rodeo.lang.ast.expressions.VariableAst;
import lambda.rodeo.lang.compilation.CompileContext;
import lambda.rodeo.lang.compileable.expression.Compileable;
import lambda.rodeo.lang.compileable.expression.CompileableExpr;
import lambda.rodeo.lang.compileable.expression.SimpleCompilableExpr;
import lambda.rodeo.lang.types.TypeScope.Entry;
import lambda.rodeo.lang.types.Type;
import lombok.Builder;
import lombok.Getter;
import org.objectweb.asm.MethodVisitor;

@Getter
@Builder
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
  public CompileableExpr toCompileableExpr() {
    return SimpleCompilableExpr.builder()
        .typedExpression(this)
        .build();
  }

  @Override
  public void compile(MethodVisitor methodVisitor, CompileContext compileContext) {
    methodVisitor.visitVarInsn(ALOAD, scopeEntry.getIndex());
  }

}
