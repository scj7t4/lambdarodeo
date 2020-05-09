package lambda.rodeo.lang.s2typed.functions.patterns;

import lambda.rodeo.lang.s1ast.functions.patterns.CaseArgAst;
import lambda.rodeo.lang.s3compileable.functions.patterns.CompileableCaseArg;

public interface TypedCaseArg {
  CaseArgAst getCaseArgAst();

  CompileableCaseArg toCompileableCaseArg();
}
