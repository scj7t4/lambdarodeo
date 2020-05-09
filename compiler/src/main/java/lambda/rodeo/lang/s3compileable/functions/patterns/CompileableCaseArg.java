package lambda.rodeo.lang.s3compileable.functions.patterns;

import lambda.rodeo.lang.compilation.CompileContext;
import lambda.rodeo.lang.s1ast.functions.patterns.CaseArgAst;
import lambda.rodeo.lang.s2typed.functions.patterns.TypedCaseArg;
import org.objectweb.asm.MethodVisitor;

public interface CompileableCaseArg {
  TypedCaseArg getTypedCaseArg();

  /**
   * Compiles this pattern case.
   * @param methodVisitor The method visitor for generating the byte code.
   * @param compileContext For global compile information.
   * @param argIndex The index of the arg getting checked.
   */
  void compile(MethodVisitor methodVisitor, CompileContext compileContext, int argIndex);
}
