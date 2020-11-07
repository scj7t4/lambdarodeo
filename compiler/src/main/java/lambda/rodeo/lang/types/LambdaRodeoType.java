package lambda.rodeo.lang.types;

import lambda.rodeo.lang.compilation.CollectsErrors;
import lambda.rodeo.lang.scope.TypedModuleScope;

public interface LambdaRodeoType {

  CompileableType toCompileableType(
      TypedModuleScope typedModuleScope,
      CollectsErrors compileContext);
}
