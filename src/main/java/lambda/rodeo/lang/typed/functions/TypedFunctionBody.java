package lambda.rodeo.lang.typed.functions;

import java.util.List;
import java.util.stream.Collectors;
import lambda.rodeo.lang.ast.functions.FunctionBodyAst;
import lambda.rodeo.lang.compileable.functions.CompileableFunctionBody;
import lambda.rodeo.lang.scope.CompileableModuleScope;
import lambda.rodeo.lang.scope.TypeScope;
import lambda.rodeo.lang.typed.statements.TypedStatement;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Builder
@Getter
@EqualsAndHashCode
public class TypedFunctionBody {

  private final TypeScope initialTypeScope;
  private final List<TypedStatement> statements;
  private final FunctionBodyAst functionBodyAst;


  public CompileableFunctionBody toCompileableFunctionBody(
      CompileableModuleScope compileableModuleScope) {
    return CompileableFunctionBody.builder()
        .initialTypeScope(initialTypeScope.toCompileableTypeScope())
        .typedFunctionBody(this)
        .statements(statements.stream()
            .map(stmnt -> stmnt.toCompileableStatement(compileableModuleScope))
            .collect(Collectors.toList()))
        .build();
  }

}
