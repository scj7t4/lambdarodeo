package lambda.rodeo.lang.types;

import java.util.List;
import java.util.Map;
import lambda.rodeo.lang.compilation.CollectsErrors;
import lambda.rodeo.lang.scope.TypeResolver;

public interface LambdaRodeoType {

  CompileableType toCompileableType(
      TypeResolver typeResolver,
      CollectsErrors compileContext);

}
