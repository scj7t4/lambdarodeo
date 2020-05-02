package lambda.rodeo.lang.typed.statements;


import static org.objectweb.asm.Opcodes.ASTORE;

import lambda.rodeo.lang.ast.statements.SimpleAssignmentAst;
import lambda.rodeo.lang.compilation.CompileContext;
import lambda.rodeo.lang.compileable.statement.CompileableAssignment;
import lambda.rodeo.lang.exceptions.CriticalLanguageException;
import lambda.rodeo.lang.types.TypeScope;
import lambda.rodeo.lang.types.TypeScope.Entry;
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
  public void compile(MethodVisitor methodVisitor, CompileContext compileContext) {
    String identifier = assignmentAst.getIdentifier();
    int index = typeScope.get(identifier)
        .map(Entry::getIndex)
        .orElseThrow(() -> new CriticalLanguageException(
            "Identifier '" + identifier + "'wasn't in type scope"));
    methodVisitor.visitVarInsn(ASTORE, index);
  }

  @Override
  public CompileableAssignment toCompileableAssignment() {
    return this;
  }
}
