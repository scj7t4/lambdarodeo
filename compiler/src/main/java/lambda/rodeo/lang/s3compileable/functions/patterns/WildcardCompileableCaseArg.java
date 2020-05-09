package lambda.rodeo.lang.s3compileable.functions.patterns;

import static org.objectweb.asm.Opcodes.ICONST_1;

import lambda.rodeo.lang.compilation.CompileContext;
import lambda.rodeo.lang.s1ast.functions.patterns.WildcardCaseArgAst;
import lambda.rodeo.lang.s2typed.functions.patterns.WildcardTypedCaseArg;
import lombok.Builder;
import lombok.Getter;
import org.objectweb.asm.MethodVisitor;

@Builder
@Getter
public class WildcardCompileableCaseArg implements CompileableCaseArg {
  private final WildcardTypedCaseArg typedCaseArg;

  @Override
  public void compile(MethodVisitor methodVisitor, CompileContext compileContext, int argIndex) {
    // Wildcard always matches and is handled as a special case in
    // CompileablePatternCase
  }
}
