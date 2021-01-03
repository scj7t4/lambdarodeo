package lambda.rodeo.lang.s1ast.type;

import java.util.List;
import lambda.rodeo.lang.AstNode;
import lambda.rodeo.lang.compilation.CollectsErrors;
import lambda.rodeo.lang.scope.TypeResolver;
import lambda.rodeo.lang.types.CompileableType;
import lambda.rodeo.lang.types.LambdaRodeoType;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Builder
@Getter
public class GenericInterfaceAst implements AstNode, LambdaRodeoType {

  private final int startLine;
  private final int endLine;
  private final int characterStart;

  @NonNull
  private final List<TypedVar> members;

  @NonNull
  private final List<TypedVar> genericParams;

  @Override
  public CompileableType toCompileableType(
      TypeResolver typeResolver,
      CollectsErrors compileContext) {


    return null;
  }
}
