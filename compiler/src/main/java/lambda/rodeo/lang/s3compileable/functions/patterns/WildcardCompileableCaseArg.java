package lambda.rodeo.lang.s3compileable.functions.patterns;

import lambda.rodeo.lang.compilation.CompileContext;
import lambda.rodeo.lang.s2typed.functions.patterns.WildcardTypedCaseArg;
import lambda.rodeo.lang.s2typed.functions.patterns.TypedStaticPattern;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;

@Builder
@Getter
@EqualsAndHashCode
public class WildcardCompileableCaseArg implements CompileableCaseArg {

  @NonNull
  private final WildcardTypedCaseArg typedCaseArg;
  @NonNull
  private final TypedStaticPattern staticPattern;

  @Override
  public void compile(MethodVisitor methodVisitor, CompileContext compileContext,
      CompileableCaseArgContext caseArgContext) {
    // Wildcard always matches and is handled as a special case in
    // CompileablePatternCase
  }

  @Override
  public void declareMatcherField(ClassWriter classWriter) {
    // Nothing to declare statically
  }

  @Override
  public void matcherInit(MethodVisitor methodVisitor, CompileContext compileContext,
      String internalModuleName) {
    // Nothing to init statically.
  }
}
