package lambda.rodeo.lang.s2typed.type;

public interface S2TypedVar {

  String getName();

  int getStartLine();

  int getEndLine();

  int getCharacterStart();

  lambda.rodeo.lang.types.CompileableType getType();
}
