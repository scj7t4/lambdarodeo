package lambda.rodeo.lang.s1ast.functions.patterns;

import java.util.List;
import lambda.rodeo.lang.s1ast.functions.FunctionBodyAst;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class PatternCaseAst {
  private final FunctionBodyAst functionBodyAst;
  private final List<CaseArgAst> caseArgAsts;
}
