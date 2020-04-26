package lambda.rodeo.lang.functions;

import java.util.List;
import lambda.rodeo.lang.exceptions.CriticalLanguageException;
import lambda.rodeo.lang.statements.Scope;
import lambda.rodeo.lang.statements.StatementAst;
import lambda.rodeo.lang.statements.TypeScope;
import lambda.rodeo.lang.types.Type;
import lombok.Builder;
import org.objectweb.asm.MethodVisitor;

@Builder
public class FunctionBodyAst {

  private final List<StatementAst> statements;
  private final TypeScope finalTypeScope;

  public Object compute(Scope initScope) {
    Scope current = initScope;
    for (StatementAst statement : statements) {
      current = statement.compute(current);
    }
    return current.get("$last")
        .orElseThrow(() -> new CriticalLanguageException("$last was not defined in scope"));
  }

  public Type getType(TypeScope typeScope) {
    return statements.get(statements.size() - 1).getType(typeScope);
  }

  public TypeScope compile(MethodVisitor methodVisitor, TypeScope initialTypeScope) {
    TypeScope typeScope = initialTypeScope;
    for (StatementAst statement : statements) {
      typeScope = statement.compile(methodVisitor, typeScope);
    }
    return typeScope;
  }
}
