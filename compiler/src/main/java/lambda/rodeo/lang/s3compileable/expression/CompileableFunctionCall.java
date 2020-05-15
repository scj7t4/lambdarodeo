package lambda.rodeo.lang.s3compileable.expression;

import static org.objectweb.asm.Opcodes.INVOKESTATIC;

import java.util.List;
import java.util.Objects;
import lambda.rodeo.lang.s1ast.ModuleAst;
import lambda.rodeo.lang.compilation.CompileContext;
import lambda.rodeo.lang.exceptions.CriticalLanguageException;
import lambda.rodeo.lang.s2typed.expressions.TypedFunctionCall;
import lambda.rodeo.runtime.types.LambdaRodeoType;
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

  public String getTargetMethod() {
    String[] split = typedExpression.getFunctionCallAst().getCallTarget().split("\\.");
    return split[split.length - 1];
  }

  public String getCallDescriptor() {
    StringBuilder sb = new StringBuilder();
    sb.append("(");
    for (CompileableExpr arg : args) {
      LambdaRodeoType type = arg.getTypedExpression().getType();
      sb.append(type.getDescriptor());
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
        .map(LambdaRodeoType::getDescriptor)
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
