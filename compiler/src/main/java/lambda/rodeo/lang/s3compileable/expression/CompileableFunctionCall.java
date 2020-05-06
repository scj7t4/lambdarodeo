package lambda.rodeo.lang.s3compileable.expression;

import static org.objectweb.asm.Opcodes.INVOKESTATIC;
import static org.objectweb.asm.Type.getDescriptor;

import java.util.List;
import java.util.Objects;
import lambda.rodeo.lang.s1ast.ModuleAst;
import lambda.rodeo.lang.compilation.CompileContext;
import lambda.rodeo.lang.exceptions.CriticalLanguageException;
import lambda.rodeo.lang.s2typed.expressions.TypedFunctionCall;
import lambda.rodeo.runtime.types.Type;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import org.objectweb.asm.MethodVisitor;

@Builder
@Getter
@EqualsAndHashCode
public class CompileableFunctionCall implements CompileableExpr {

  @NonNull
  private final TypedFunctionCall typedExpression;

  @NonNull
  private final List<CompileableExpr> args;

  public @NonNull ModuleAst getModuleAst() {
    return typedExpression.getTypedModuleScope().getThisScope().getThisModule();
  }

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
  public String getTargetMethod() {
    String[] split = typedExpression.getFunctionCallAst().getCallTarget().split("\\.");
    return split[split.length - 1];
  }

  public String getCallDescriptor() {
    StringBuilder sb = new StringBuilder();
    sb.append("(");
    for (CompileableExpr arg : args) {
      Type type = arg.getTypedExpression().getType();
      sb.append(getDescriptor(type.javaType()));
    }
    sb.append(")");

    String returnTypeDescriptor = typedExpression.getTypedModuleScope()
        .getThisScope()
        .getThisModule()
        .getFunctionAsts()
        .stream()
        .filter(fn -> Objects.equals(fn.getName(), getTargetMethod()))
        .findFirst()
        .map(fn -> fn.getFunctionSignature().getDeclaredReturnType())
        .map(type -> getDescriptor(type.javaType()))
        .orElseThrow(() -> new CriticalLanguageException(
            "Function '" + getTargetMethod() +"' didn't have a return type descriptor"
        ));
    sb.append(returnTypeDescriptor);
    return sb.toString();
  }

  @Override
  public void compile(
      MethodVisitor methodVisitor,
      CompileContext compileContext) {
    for (CompileableExpr expr : args) {
      expr.compile(methodVisitor, compileContext);
    }
    methodVisitor.visitMethodInsn(
        INVOKESTATIC,
        getModuleAst().getInternalJavaName(),
        getTargetMethod(),
        getCallDescriptor(),
        false);
  }
}