package lambda.rodeo.lang.typed.functions;

import java.util.List;
import java.util.stream.Collectors;
import lambda.rodeo.lang.ast.functions.FunctionBodyAst;
import lambda.rodeo.lang.compileable.functions.CompileableFunctionBody;
import lambda.rodeo.lang.typed.TypedModule;
import lambda.rodeo.lang.types.TypeScope;
import lambda.rodeo.lang.typed.statements.TypedStatement;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class TypedFunctionBody {

  private final TypeScope initialTypeScope;
  private final List<TypedStatement> statements;
  private final FunctionBodyAst functionBodyAst;


  public CompileableFunctionBody toCompileableFunctionBody(
      List<TypedModule> modules) {
    return CompileableFunctionBody.builder()
        .initialTypeScope(initialTypeScope.toCompileableTypeScope(modules))
        .typedFunctionBody(this)
        .statements(statements.stream()
            .map(stmnt -> stmnt.toCompileableStatement(modules))
            .collect(Collectors.toList()))
        .build();
  }

}
