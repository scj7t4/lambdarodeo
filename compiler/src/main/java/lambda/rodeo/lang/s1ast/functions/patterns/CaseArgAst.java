package lambda.rodeo.lang.s1ast.functions.patterns;

import lambda.rodeo.lang.AstNode;
import lambda.rodeo.lang.s1ast.functions.ToTypedFunctionContext;
import lambda.rodeo.lang.s2typed.functions.patterns.TypedCaseArg;
import lambda.rodeo.lang.scope.TypeScope;
import lambda.rodeo.lang.scope.TypedModuleScope;

public interface CaseArgAst extends AstNode {
  TypedCaseArg toTypedCaseArg(TypeScope initialTypeScope,
      TypedModuleScope typedModuleScope,
      ToTypedFunctionContext compileContext);
}
