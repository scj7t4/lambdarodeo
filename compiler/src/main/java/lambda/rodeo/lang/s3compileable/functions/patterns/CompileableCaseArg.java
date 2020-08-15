package lambda.rodeo.lang.s3compileable.functions.patterns;

import lambda.rodeo.lang.compilation.S1CompileContext;
import lambda.rodeo.lang.s2typed.functions.patterns.TypedCaseArg;
import lambda.rodeo.lang.s2typed.functions.patterns.TypedStaticPattern;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;

public interface CompileableCaseArg {
  TypedCaseArg getTypedCaseArg();
  /**
   * Compiles this pattern case.
   * @param methodVisitor The method visitor for generating the byte code.
   * @param compileContext For global compile information.
   * @param caseArgContext
   */
  void compile(MethodVisitor methodVisitor, S1CompileContext compileContext,
      CompileableCaseArgContext caseArgContext);

  TypedStaticPattern getStaticPattern();

  void declareMatcherField(ClassWriter classWriter);
  void matcherInit(MethodVisitor methodVisitor, S1CompileContext compileContext,
      String internalModuleName);
}
