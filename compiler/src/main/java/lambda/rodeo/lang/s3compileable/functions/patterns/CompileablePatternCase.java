package lambda.rodeo.lang.s3compileable.functions.patterns;

import static org.objectweb.asm.Opcodes.ARETURN;
import static org.objectweb.asm.Opcodes.IFEQ;

import java.util.List;
import lambda.rodeo.lang.compilation.S1CompileContext;
import lambda.rodeo.lang.compilation.S2CompileContext;
import lambda.rodeo.lang.s1ast.functions.patterns.ScopeReplaceAndCasts;
import lambda.rodeo.lang.s2typed.functions.patterns.TypedPatternCase;
import lambda.rodeo.lang.s3compileable.statement.CompileableStatement;
import lambda.rodeo.lang.scope.CompileableTypeScope;
import lambda.rodeo.lang.types.CompileableType;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

@Builder
@Getter
public class CompileablePatternCase {
  @NonNull
  private final List<CompileableStatement> statements;

  @NonNull
  private final List<CompileableCaseArg> caseArgs;

  @NonNull
  private final TypedPatternCase typedPatternCase;

  public void compile(MethodVisitor methodVisitor, S1CompileContext compileContext,
      String internalModuleName) {
    Label patternMiss = new Label();
    for (int i = 0; i < caseArgs.size(); i++) {
      CompileableCaseArg caseArg = caseArgs.get(i);
      if(caseArg instanceof WildcardCompileableCaseArg) {
        // Wild card compiles to nothing and always hits, so we skip
        // its compilation and checking to see the outcome of the match.
        continue;
      }
      CompileableCaseArgContext caseArgContext = CompileableCaseArgContext.builder()
          .argIndex(i)
          .internalModuleName(internalModuleName)
          .build();
      caseArg.compile(methodVisitor, compileContext, caseArgContext);
      methodVisitor.visitJumpInsn(IFEQ, patternMiss);
    }
    Label patternHit = new Label();
    methodVisitor.visitLabel(patternHit);
    // Perform all the casts that the pattern match set up:
    for(ScopeReplaceAndCasts cast : typedPatternCase.getScopeReplaceAndCasts()) {
      cast.compileCast(methodVisitor);
    }

    for(CompileableStatement compileableStatement : statements) {
      compileableStatement.compile(methodVisitor, compileContext);
    }
    // After compiling all the statements, the return value should be on the stack:
    // An assigment in the last statement should be illegal, so we don't allow it:
    methodVisitor.visitInsn(ARETURN);
    // Then we indicate where to jump to if the pattern wasn't a match:
    methodVisitor.visitLabel(patternMiss);
  }

  public CompileableType getReturnType() {
    return statements.get(statements.size() - 1)
        .getCompileableExpr()
        .getTypedExpression()
        .getType();
  }

  public CompileableTypeScope getFinalTypeScope() {
    return statements.get(statements.size() - 1)
        .getAfterTypeScope();
  }

  public void lambdaLift(ClassWriter cw, S2CompileContext compileContext, String internalJavaName) {
    for(CompileableStatement statement : statements) {
      statement.lambdaLift(cw, compileContext, internalJavaName);
    }
  }
}
