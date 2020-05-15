package lambda.rodeo.lang.s3compileable.functions;

import static org.objectweb.asm.Opcodes.ATHROW;
import static org.objectweb.asm.Opcodes.DUP;
import static org.objectweb.asm.Opcodes.INVOKESPECIAL;
import static org.objectweb.asm.Opcodes.NEW;
import static org.objectweb.asm.Type.getInternalName;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import lambda.rodeo.lang.compilation.CompileContext;
import lambda.rodeo.lang.s1ast.functions.patterns.PatternCaseAst;
import lambda.rodeo.lang.s2typed.functions.TypedFunctionBody;
import lambda.rodeo.lang.s3compileable.functions.patterns.CompileableCaseArg;
import lambda.rodeo.lang.s3compileable.functions.patterns.CompileablePatternCase;
import lambda.rodeo.lang.scope.CompileableTypeScope;
import lambda.rodeo.runtime.exceptions.RuntimeCriticalLanguageException;
import lambda.rodeo.runtime.types.Type;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;

@Builder
@Getter
@EqualsAndHashCode
public class CompileableFunctionBody {

  private final CompileableTypeScope initialTypeScope;
  private final CompileableTypeScope finalTypeScope;
  private final List<CompileablePatternCase> patternCases;
  private final TypedFunctionBody typedFunctionBody;

  public void compile(MethodVisitor methodVisitor,
      CompileContext compileContext, String internalModuleName) {
    for (CompileablePatternCase patternCase : patternCases) {
      patternCase.compile(methodVisitor, compileContext, internalModuleName);
    }
    if (!patternCases.get(0).getCaseArgs().isEmpty()) {
      //TODO: revisit this, probably don't want the whole language to barf if we don't cover all
      //TODO: cases.
      String exceptionType = getInternalName(RuntimeCriticalLanguageException.class);
      methodVisitor.visitTypeInsn(NEW, exceptionType);
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
    return finalTypeScope;
  }

  public List<CompileableCaseArg> getAllCaseArgs() {
    return patternCases.stream()
        .map(CompileablePatternCase::getCaseArgs)
        .flatMap(Collection::stream)
        .collect(Collectors.toList());
  }

  public void lambdaLift(ClassWriter cw, CompileContext compileContext, String internalJavaName) {
    for(CompileablePatternCase patternCase : patternCases) {
      patternCase.lambdaLift(cw, compileContext, internalJavaName);
    }
  }
}
