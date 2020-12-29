package lambda.rodeo.lang.s3compileable.expression;

import static org.objectweb.asm.Opcodes.INVOKESTATIC;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lambda.rodeo.lang.s1ast.ModuleAst;
import lambda.rodeo.lang.compilation.S1CompileContext;
import lambda.rodeo.lang.exceptions.CriticalLanguageException;
import lambda.rodeo.lang.s2typed.expressions.TypedFunctionCall;
import lambda.rodeo.lang.scope.ModuleScope;
import lambda.rodeo.lang.types.CompileableType;
import lambda.rodeo.lang.util.DescriptorUtils;
import lambda.rodeo.lang.util.FunctionDescriptorBuilder;
import lambda.rodeo.runtime.execution.Trampoline;
import lambda.rodeo.runtime.lambda.Lambda0;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

@Builder
@Getter
@EqualsAndHashCode
public class CompileableFunctionCall implements CompileableExpr {

  @NonNull
  private final TypedFunctionCall typedExpression;

  @NonNull
  private final List<CompileableExpr> args;

  public Optional<ModuleAst> getModuleAst() {
    String targetModule = getTargetModule();
    if (targetModule.isEmpty()) {
      return Optional.of(typedExpression.getTypedModuleScope().getThisScope().getThisModule());
    } else {
      return typedExpression.getTypedModuleScope().getImportedModules().stream()
          .filter(imported -> Objects.equals(imported.getAlias(), targetModule))
          .findFirst()
          .map(ModuleScope::getThisModule);
    }
  }

  public String getTargetMethod() {
    String[] split = typedExpression.getCallTarget().split("\\.");
    return split[split.length - 1];
  }

  public String getTargetModule() {
    String[] split = typedExpression.getCallTarget().split("\\.");
    String[] allButLast = Arrays.copyOfRange(split, 0, split.length - 1);
    return String.join(".", allButLast);
  }

  public String getCallDescriptor() {
    StringBuilder sb = new StringBuilder();
    sb.append("(");

    for (CompileableExpr arg : args) {
      sb.append(arg.getTypedExpression().getType().getLambdaDescriptor());
    }
    sb.append(")");

    String returnTypeDescriptor = typedExpression.getReturnType().getLambdaDescriptor();
    sb.append(returnTypeDescriptor);
    return sb.toString();
  }

  @Override
  public void compile(
      MethodVisitor methodVisitor,
      S1CompileContext compileContext) {
    for (CompileableExpr expr : args) {
      expr.compile(methodVisitor, compileContext);
    }

    ModuleAst targetModule = getModuleAst()
        .orElseThrow(() -> new CriticalLanguageException(
            "Couldn't find module for function call " + typedExpression.getCallTarget()
        ));

    methodVisitor.visitMethodInsn(
        INVOKESTATIC,
        targetModule.getInternalJavaName(),
        getTargetMethod(),
        getCallDescriptor(),
        false);
    methodVisitor.visitMethodInsn(
        INVOKESTATIC,
        Type.getInternalName(Trampoline.class),
        "make",
        FunctionDescriptorBuilder.args(Lambda0.class).returns(Lambda0.class),
        false
    );
  }
}
