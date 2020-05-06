package lambda.rodeo.lang.s2typed.functions.patterns;

import java.util.List;
import lambda.rodeo.lang.s1ast.functions.FunctionBodyAst;
import lambda.rodeo.lang.s2typed.functions.TypedFunctionBody;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class TypedPatternCase {
  private final TypedFunctionBody typedFunctionBody;
  private final List<TypedCaseArg> typedCaseArgs;
}
