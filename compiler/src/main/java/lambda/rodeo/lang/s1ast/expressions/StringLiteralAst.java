package lambda.rodeo.lang.s1ast.expressions;

import static org.objectweb.asm.Opcodes.INVOKESTATIC;

import java.util.Collections;
import java.util.Set;
import lambda.rodeo.lang.compilation.CollectsErrors;
import lambda.rodeo.lang.compilation.S1CompileContext;
import lambda.rodeo.lang.s1ast.functions.ToTypedFunctionContext;
import lambda.rodeo.lang.s2typed.expressions.TypedExpression;
import lambda.rodeo.lang.s3compileable.expression.CompileableExpr;
import lambda.rodeo.lang.scope.TypeScope;
import lambda.rodeo.lang.scope.TypedModuleScope;
import lambda.rodeo.lang.types.CompileableType;
import lambda.rodeo.lang.types.StringType;
import lambda.rodeo.lang.util.FunctionDescriptorBuilder;
import lambda.rodeo.runtime.lambda.Value;
import lombok.Builder;
import lombok.Getter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

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
  public CompileableType getType() {
    return StringType.INSTANCE;
  }

  @Override
  public ExpressionAst getExpr() {
    return this;
  }

  @Override
  public CompileableExpr toCompileableExpr(
      CollectsErrors compileContext) {
    return this;
  }

  @Override
  public TypedExpression getTypedExpression() {
    return this;
  }

  @Override
  public void compile(MethodVisitor methodVisitor, S1CompileContext compileContext) {
    methodVisitor.visitLdcInsn(contents);
    methodVisitor.visitMethodInsn(INVOKESTATIC,
        Type.getInternalName(Value.class), "of",
        FunctionDescriptorBuilder.args(Object.class)
            .returns(Value.class),
        false);
  }
}
