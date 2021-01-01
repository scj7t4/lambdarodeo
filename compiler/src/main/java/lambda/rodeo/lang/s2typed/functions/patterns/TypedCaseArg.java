package lambda.rodeo.lang.s2typed.functions.patterns;

import lambda.rodeo.lang.compilation.CollectsErrors;
import lambda.rodeo.lang.s1ast.functions.patterns.CaseArgAst;
import lambda.rodeo.lang.s3compileable.functions.patterns.CompileableCaseArg;
import lambda.rodeo.lang.scope.TypedModuleScope;
import lambda.rodeo.runtime.patterns.Matcher;

public interface TypedCaseArg {

  CaseArgAst getCaseArgAst();

  CompileableCaseArg toCompileableCaseArg(TypedStaticPattern staticPattern,
      TypedModuleScope scope, CollectsErrors compileContext);

  /**
   * Gets the class to use for the static version of this matcher.
   *
   * @return A class, or null if this matcher shouldn't have a static field
   */
  Class<? extends Matcher> getStaticMatcherClass();
}
