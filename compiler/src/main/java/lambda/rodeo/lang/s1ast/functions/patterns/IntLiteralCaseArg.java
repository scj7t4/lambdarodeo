package lambda.rodeo.lang.s1ast.functions.patterns;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class IntLiteralCaseArg implements CaseArgAst {
  private final String value;
}
