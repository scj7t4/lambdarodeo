package lambda.rodeo.lang.ast.expressions;

import static org.objectweb.asm.Opcodes.ALOAD;

import lambda.rodeo.lang.compilation.CompileContext;
import lambda.rodeo.lang.types.TypeScope.Entry;
import lambda.rodeo.lang.typed.expressions.TypedExpressionAst;
import lambda.rodeo.lang.types.Type;
import lombok.Builder;
import lombok.Getter;
import org.objectweb.asm.MethodVisitor;

@Getter
@Builder
public class TypedVariableAst implements TypedExpressionAst {

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
  public void compile(MethodVisitor methodVisitor, CompileContext compileContext) {
    methodVisitor.visitVarInsn(ALOAD, scopeEntry.getIndex());
  }
}
