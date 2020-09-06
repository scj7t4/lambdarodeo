package lambda.rodeo.lang.scope;

import java.util.List;
import java.util.stream.Collectors;
import lambda.rodeo.runtime.types.CompileableLambdaType;
import lambda.rodeo.runtime.types.CompileableType;
import lambda.rodeo.runtime.types.LambdaRodeoType;
import lambda.rodeo.runtime.types.LambdaType;

public class TypeCompiler {
  public static CompileableType compile(LambdaRodeoType type) {
    if (type instanceof CompileableType) {
      return (CompileableType) type;
    }

    if (type instanceof LambdaType) {
      LambdaType asReal = (LambdaType) type;
      CompileableLambdaType.builder()
          .from(type)
          .args(asReal.getArgs().stream().map(TypeCompiler::compile).collect(Collectors.toList()))
          .returnType(compile(asReal.getReturnType()))
          .build();
    }

  }


}
