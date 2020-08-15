package lambda.rodeo.lang.compilation;

public interface S2CompileContext extends CollectsErrors, S1CompileContext {

  String getSource();

  java.util.Map<String, lambda.rodeo.lang.s1ast.ModuleAst> getModules();

  CompileErrorCollector getCompileErrorCollector();
}
