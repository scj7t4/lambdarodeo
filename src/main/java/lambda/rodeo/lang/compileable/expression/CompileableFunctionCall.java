package lambda.rodeo.lang.compileable.expression;

import lambda.rodeo.lang.compilation.CompileContext;
import lambda.rodeo.lang.typed.expressions.TypedExpression;
import lambda.rodeo.lang.typed.expressions.TypedFunctionCall;
import lombok.Builder;
import lombok.Getter;
import org.objectweb.asm.MethodVisitor;

@Builder
@Getter
public class CompileableFunctionCall implements CompileableExpr {
  private final TypedFunctionCall typedExpression;

  //
//  public String getTargetModuleName() {
//    String[] split = functionCallAst.getCallTarget().split("\\.");
//    String[] modTarget = new String[split.length - 1];
//    for (int i = 0; i < modTarget.length; i++) {
//      modTarget[i] = split[i];
//    }
//    if (modTarget.length > 0) {
//      return String.join(".", modTarget);
//    } else {
//      return ModuleAst.THIS_MODULE; // This module
//    }
//  }
//
//  public Optional<ModuleAst> getTargetModule() {
//    return typeScope.get(getTargetModuleName())
//        .filter(x -> x.getType() instanceof ModuleType)
//        .map(x -> (ModuleType) x.getType())
//        .map(ModuleType::getModuleAst);
//  }
//
//  public String getJavaMethodOwner() {
//    String[] split = functionCallAst.getCallTarget().split("\\.");
//    String[] modTarget = new String[split.length - 1];
//    for (int i = 0; i < modTarget.length; i++) {
//      modTarget[i] = split[i];
//    }
//    if (modTarget.length > 0) {
//      return String.join("/", modTarget);
//    } else {
//      return null; // This module
//    }
//  }
//
//  public String getTargetMethod() {
//    String[] split = functionCallAst.getCallTarget().split("\\.");
//    return split[split.length - 1];
//  }
//
//  public String getCallDescriptor(TypeScope typeScope) {
//    StringBuilder sb = new StringBuilder();
//    sb.append("(");
//    for (TypedExpressionAst arg : args) {
//      Type type = arg.getType();
//      sb.append(type.descriptor());
//    }
//    sb.append(")");
//    String returnTypeDescriptor = typeScope.get()
//
//    String returnTypeDescriptor = compileContext
//        .getCompiledModules()
//        .getModule(getTargetModuleName())
//        .flatMap(module -> module.getFunction(getTargetMethod()))
//        .map(fn -> fn.getFunctionSignature().getDeclaredReturnType())
//        .map(Type::descriptor)
//        .orElse(org.objectweb.asm.Type.getDescriptor(Object.class));
//    sb.append(returnTypeDescriptor);
//    return sb.toString();
//  }

  @Override
  public void compile(
      MethodVisitor methodVisitor,
      CompileContext compileContext) {
//    for (TypedExpressionAst expr : args) {
//      expr.compile(methodVisitor, compileContext);
//    }
//    methodVisitor.visitMethodInsn(
//        INVOKEVIRTUAL,
//        getJavaMethodOwner(),
//        getTargetMethod(),
//        getCallDescriptor(compileContext),
//        false);
  }

  @Override
  public TypedExpression getTypedExpression() {
    return null;
  }
}
