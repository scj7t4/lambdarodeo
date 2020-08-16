package lambda.rodeo.lang.s3compileable.expression;

import static org.objectweb.asm.Opcodes.INVOKESTATIC;

import java.lang.reflect.Array;
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

  public Optional<ModuleAst> getModuleAst() {
    String targetModule = getTargetModule();
    if(targetModule.isEmpty()) {
      return Optional.of(typedExpression.getTypedModuleScope().getThisScope().getThisModule());
    } else {
      return typedExpression.getTypedModuleScope().getImportedModules().stream()
          .filter(imported -> Objects.equals(imported.getSimpleModuleName(), targetModule))
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
    String[] allButLast = Arrays.copyOfRange(split, 0, split.length-1);
    return String.join(".", allButLast);
  }

  public String getCallDescriptor() {
    StringBuilder sb = new StringBuilder();
    sb.append("(");
    List<LambdaRodeoType> callSignature = new ArrayList<>();

    for (CompileableExpr arg : args) {
      LambdaRodeoType type = arg.getTypedExpression().getType();
      sb.append(type.getDescriptor());
      callSignature.add(type);
    }
    sb.append(")");

    String returnTypeDescriptor = typedExpression.getTypedModuleScope()
        .getCallTarget(typedExpression.getCallTarget(), callSignature)
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
  }
}
