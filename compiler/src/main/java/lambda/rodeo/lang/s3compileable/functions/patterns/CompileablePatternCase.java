package lambda.rodeo.lang.s3compileable.functions.patterns;

import static org.objectweb.asm.Opcodes.IFEQ;

import java.util.List;
import lambda.rodeo.lang.compilation.CompileContext;
import lambda.rodeo.lang.s3compileable.functions.CompileableFunctionBody;
import lombok.Builder;
import lombok.Getter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

@Builder
@Getter
public class CompileablePatternCase {
  private final CompileableFunctionBody compileableFunctionBody;
  private final List<CompileableCaseArg> compileableCaseArgs;

  public void compile(MethodVisitor methodVisitor, CompileContext compileContext) {
    Label patternMiss = new Label();
    for (int i = 0; i < compileableCaseArgs.size(); i++) {
      CompileableCaseArg caseArg = compileableCaseArgs.get(i);
      if(caseArg instanceof WildcardCompileableCaseArg) {
        // Wild card compiles to nothing and always hits, so we skip
        // its compilation and checking to see the outcome of the match.
        continue;
      }
      caseArg.compile(methodVisitor, compileContext, i);
      methodVisitor.visitJumpInsn(IFEQ, patternMiss);
    }
    Label patternHit = new Label();
    methodVisitor.visitLabel(patternHit);
    compileableFunctionBody.compile(methodVisitor, compileContext);
    methodVisitor.visitLabel(patternMiss);
  }
}
