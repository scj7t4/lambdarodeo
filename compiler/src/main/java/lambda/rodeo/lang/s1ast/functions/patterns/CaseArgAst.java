package lambda.rodeo.lang.s1ast.functions.patterns;

import lambda.rodeo.lang.s2typed.functions.patterns.TypedCaseArg;
import lombok.Builder;

public interface CaseArgAst {
  TypedCaseArg toTypedCaseArg();
}
