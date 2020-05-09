package lambda.rodeo.lang.s3compileable.functions;

import static org.objectweb.asm.Opcodes.ATHROW;
import static org.objectweb.asm.Opcodes.DUP;
import static org.objectweb.asm.Opcodes.INVOKESPECIAL;
import static org.objectweb.asm.Type.getInternalName;
import static org.objectweb.asm.TypeReference.NEW;

import java.util.List;
import lambda.rodeo.lang.compilation.CompileContext;
import lambda.rodeo.lang.exceptions.CriticalLanguageException;
import lambda.rodeo.lang.s3compileable.functions.patterns.CompileableCaseArg;
import lambda.rodeo.lang.s3compileable.functions.patterns.CompileablePatternCase;
import lambda.rodeo.lang.s3compileable.statement.CompileableStatement;
import lambda.rodeo.lang.s2typed.functions.TypedFunctionBody;
import lambda.rodeo.lang.scope.CompileableTypeScope;
import lambda.rodeo.runtime.exceptions.RuntimeCriticalLanguageException;
import lambda.rodeo.runtime.types.Type;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.objectweb.asm.MethodVisitor;

@Builder
@Getter
@EqualsAndHashCode
public class CompileableFunctionBody {

  private final CompileableTypeScope initialTypeScope;
  private final List<CompileableStatement> statements;
  private final List<CompileablePatternCase> patternCases;
  private final TypedFunctionBody typedFunctionBody;


  public Type getReturnType() {
    return statements.get(statements.size() - 1)
        .getCompileableExpr()
        .getTypedExpression()
        .getType();
  }

  public void compile(MethodVisitor methodVisitor,
      CompileContext compileContext) {
    if (!statements.isEmpty() && !patternCases.isEmpty()) {
      throw new CriticalLanguageException("Function body has both pattern cases and statements");
    }

    for (CompileableStatement statement : statements) {
      statement.compile(methodVisitor, compileContext);
    }
    for (CompileablePatternCase patternCase : patternCases) {
      patternCase.compile(methodVisitor, compileContext);
    }
    if (!patternCases.isEmpty()) {
      //TODO: revisit this, probably don't want the whole language to barf if we don't cover all
      //TODO: cases.
      String exceptionType = getInternalName(RuntimeCriticalLanguageException.class);
      methodVisitor.visitTypeInsn(NEW,
          exceptionType);
      methodVisitor.visitInsn(DUP);
      methodVisitor.visitLdcInsn("No pattern cases matched");
      methodVisitor.visitMethodInsn(INVOKESPECIAL,
          exceptionType,
          "<init>",
          "(Ljava/lang/String;)V",
          false);
      methodVisitor.visitInsn(ATHROW);
    }
  }

  public CompileableTypeScope getFinalTypeScope() {
    return statements.get(statements.size() - 1)
        .getAfterTypeScope();
  }

}
