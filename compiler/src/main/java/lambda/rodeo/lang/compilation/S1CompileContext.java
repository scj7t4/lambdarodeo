package lambda.rodeo.lang.compilation;

public interface S1CompileContext extends CollectsErrors {

  String getSource();

  CompileErrorCollector getCompileErrorCollector();
}
