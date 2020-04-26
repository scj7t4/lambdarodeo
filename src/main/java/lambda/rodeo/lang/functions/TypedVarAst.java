package lambda.rodeo.lang.functions;

import static org.objectweb.asm.Opcodes.ALOAD;

import lambda.rodeo.lang.exceptions.CriticalLanguageException;
import lambda.rodeo.lang.expressions.ExpressionAst;
import lambda.rodeo.lang.statements.TypeScope;
import lambda.rodeo.lang.statements.TypeScope.Entry;
import lambda.rodeo.lang.types.Type;
import lombok.Builder;
import lombok.Data;
import org.objectweb.asm.MethodVisitor;

@Data
@Builder
public class TypedVarAst implements ExpressionAst {

  private final String name;
  private final Type type;

  @Override
  public Type getType(TypeScope typeScope) {
    return type;
  }

  @Override
  public void compile(MethodVisitor methodVisitor, TypeScope scope) {
    Entry entry = scope.get(name)
        .orElseThrow(
            () -> new CriticalLanguageException("Variable '" + name + "' is not defined in scope"));
    methodVisitor.visitVarInsn(ALOAD, entry.getIndex());
  }
}
