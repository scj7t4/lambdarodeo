package lambda.rodeo.lang.s1ast.functions;

import java.util.List;
import java.util.Objects;
import lambda.rodeo.lang.AstNode;
import lambda.rodeo.lang.compilation.CompileError;
import lambda.rodeo.lang.compilation.S2CompileContext;
import lambda.rodeo.lang.s1ast.type.TypedVar;
import lambda.rodeo.lang.s2typed.functions.TypedFunction;
import lambda.rodeo.lang.s2typed.functions.TypedFunctionBody;
import lambda.rodeo.lang.s2typed.functions.patterns.TypedPatternCase;
import lambda.rodeo.lang.scope.TypeScope;
import lambda.rodeo.lang.scope.TypedModuleScope;
import lambda.rodeo.lang.types.CompileableAtom;
import lambda.rodeo.lang.types.CompileableType;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

@Data
@Builder
@EqualsAndHashCode
public class FunctionAst implements AstNode {

  @NonNull
  private final FunctionSigAst functionSignature;
  @NonNull
  private final FunctionBodyAst functionBodyAst;
  private final boolean lambda;
  private final int startLine;
  private final int endLine;
  private final int characterStart;

  public TypedFunction toTypedFunctionAst(
      TypeScope moduleScope,
      TypedModuleScope typedModuleScope,
      S2CompileContext compileContext) {

    functionSignature.checkCollisionAgainstModule(typedModuleScope, compileContext);

    ToTypedFunctionContext toTypedFunctionContext = ToTypedFunctionContext.builder()
        .compileContext(compileContext)
        .functionName(functionSignature.getName())
        .build();

    TypedFunctionBody typedFunctionBody = functionBodyAst.toTypedFunctionBodyAst(
        functionSignature.getInitialTypeScope(moduleScope, typedModuleScope, compileContext),
        typedModuleScope,
        toTypedFunctionContext);

    List<TypedPatternCase> patternCases = typedFunctionBody.getPatternCases();
    for (TypedPatternCase typedPatternCase : patternCases) {
      CompileableType returnedType = typedPatternCase.getReturnedType();
      CompileableType declaredReturnedType = functionSignature
          .getDeclaredReturnType()
          .toCompileableType(typedModuleScope, compileContext);

      if (!declaredReturnedType.assignableFrom(returnedType) &&
          !Objects.equals(returnedType, CompileableAtom.UNDEFINED)) {
        compileContext.getCompileErrorCollector().collect(
            CompileError
                .returnTypeMismatch(typedPatternCase.getPatternCaseAst(), declaredReturnedType,
                    returnedType)
        );
      }
    }

    return TypedFunction.builder()
        .functionAst(this)
        .functionBody(typedFunctionBody)
        .functionSigAst(functionSignature)
        .build();
  }

  public String getName() {
    return getFunctionSignature().getName();
  }

  public List<TypedVar> getArguments() {
    return getFunctionSignature().getArguments();
  }

  public String getSignature() {
    return getName() + "\\\\" + getArguments().size();
  }

  public boolean hasArity(int arity) {
    List<TypedVar> sigArguments = functionSignature.getArguments();
    if (arity != sigArguments.size()) {
      return false;
    }
    return true;
  }
}
