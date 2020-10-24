package lambda.rodeo.lang.s2typed.type;

import lambda.rodeo.lang.types.CompileableType;

public interface S2TypedVar {

  String getName();

  CompileableType getType();
}
