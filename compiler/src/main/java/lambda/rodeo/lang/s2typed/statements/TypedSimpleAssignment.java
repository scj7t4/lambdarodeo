package lambda.rodeo.lang.s2typed.statements;


import static org.objectweb.asm.Opcodes.ASTORE;

import lambda.rodeo.lang.s1ast.statements.SimpleAssignmentAst;
import lambda.rodeo.lang.compilation.S1CompileContext;
import lambda.rodeo.lang.s3compileable.statement.CompileableAssignment;
import lambda.rodeo.lang.exceptions.CriticalLanguageException;
import lambda.rodeo.lang.scope.TypeScope;
import lambda.rodeo.lang.scope.SimpleEntry;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.objectweb.asm.MethodVisitor;

@Builder
@Getter
@EqualsAndHashCode
public class TypedSimpleAssignment implements TypedAssignment, CompileableAssignment {
  private final SimpleAssignmentAst assignmentAst;
  private final TypeScope typeScope;

  @Override
  public void compile(MethodVisitor methodVisitor, S1CompileContext compileContext) {

    String identifier = assignmentAst.getIdentifier();
    SimpleEntry entry = typeScope.getSimple(identifier)
        .findFirst()
        .orElseThrow(() -> new CriticalLanguageException(
            "Identifier '" + identifier + "'wasn't in type scope"));
    entry.compileStore(methodVisitor);
  }

  @Override
  public CompileableAssignment toCompileableAssignment() {
    return this;
  }
}
