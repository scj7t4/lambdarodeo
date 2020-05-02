package lambda.rodeo.lang.ast.expressions;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class FunctionCallAst {
/*
  private final String callTarget;
  private final List<ExpressionAst> args;
  private final int startLine;
  private final int endLine;
  private final int characterStart;

  @Override
  public SimpleTypedExpressionAst toTypedExpressionAst(TypeScope typeScope, CompileContext compileContext) {
    return null; //TODO determine type
  }

  public String getModule() {
    String[] split = callTarget.split("\\.");
    String[] modTarget = new String[split.length - 1];
    for (int i = 0; i < modTarget.length; i++) {
      modTarget[i] = split[i];
    }
    if (modTarget.length > 0) {
      return String.join(".", modTarget);
    } else {
      return null; // This module
    }
  }

  public String getJavaMethodOwner() {
    String[] split = callTarget.split("\\.");
    String[] modTarget = new String[split.length - 1];
    for (int i = 0; i < modTarget.length; i++) {
      modTarget[i] = split[i];
    }
    if (modTarget.length > 0) {
      return String.join("/", modTarget);
    } else {
      return null; // This module
    }
  }

  public String getTargetMethod() {
    String[] split = callTarget.split("\\.");
    return split[split.length - 1];
  }

  public String getCallDescriptor(CompileContext compileContext) {
    StringBuilder sb = new StringBuilder();
    sb.append("(");
    for (ExpressionAst arg : args) {
      Type type = arg.toTypedExpressionAst();
      sb.append(type.descriptor());
    }
    sb.append(")");
    String returnTypeDescriptor = compileContext
        .getCompiledModules()
        .getModule(getModule())
        .flatMap(module -> module.getFunction(getTargetMethod()))
        .map(fn -> fn.getFunctionSignature().getDeclaredReturnType())
        .map(Type::descriptor)
        .orElse(org.objectweb.asm.Type.getDescriptor(Object.class));
    sb.append(returnTypeDescriptor);
    return sb.toString();
  }

  @Override
  public void compile(MethodVisitor methodVisitor,
      CompileContext compileContext) {
    for (ExpressionAst expr : args) {
      expr.compile(methodVisitor, compileContext);
    }
    methodVisitor.visitMethodInsn(
        INVOKEVIRTUAL,
        getJavaMethodOwner(),
        getTargetMethod(),
        getCallDescriptor(compileContext),
        false);
  }
 */
}
