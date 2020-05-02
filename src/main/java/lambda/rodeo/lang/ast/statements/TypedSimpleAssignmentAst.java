package lambda.rodeo.lang.ast.statements;


import static org.objectweb.asm.Opcodes.ASTORE;

import lambda.rodeo.lang.compilation.CompileContext;
import lambda.rodeo.lang.exceptions.CriticalLanguageException;
import lambda.rodeo.lang.ast.statements.TypeScope.Entry;
import lombok.Builder;
import lombok.Getter;
import org.objectweb.asm.MethodVisitor;

@Builder
@Getter
public class TypedSimpleAssignmentAst implements TypedAssignmentAst {
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
}
