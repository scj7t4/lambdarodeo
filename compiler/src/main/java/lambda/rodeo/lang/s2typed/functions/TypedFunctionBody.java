package lambda.rodeo.lang.s2typed.functions;

import java.util.List;
import java.util.stream.Collectors;
import lambda.rodeo.lang.s1ast.functions.FunctionBodyAst;
import lambda.rodeo.lang.s3compileable.functions.CompileableFunctionBody;
import lambda.rodeo.lang.scope.TypeScope;
import lambda.rodeo.lang.s2typed.statements.TypedStatement;
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


  public CompileableFunctionBody toCompileableFunctionBody() {
    return CompileableFunctionBody.builder()
        .initialTypeScope(initialTypeScope.toCompileableTypeScope())
        .typedFunctionBody(this)
        .statements(statements.stream()
            .map(TypedStatement::toCompileableStatement)
            .collect(Collectors.toList()))
        .build();
  }

}
