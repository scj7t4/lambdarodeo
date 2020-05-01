package lambda.rodeo.lang.functions;

import java.util.ArrayList;
import java.util.List;
import lambda.rodeo.lang.compilation.CompileContext;
import lambda.rodeo.lang.statements.StatementAst;
import lambda.rodeo.lang.statements.TypeScope;
import lambda.rodeo.lang.statements.TypedStatementAst;
import lambda.rodeo.lang.types.Type;
import lombok.Builder;
import lombok.Getter;
import org.objectweb.asm.MethodVisitor;

@Getter
@Builder
public class FunctionBodyAst {

  private final List<StatementAst> statements;

  public TypedFunctionBodyAst toTypedFunctionBodyAst(
      TypeScope initialTypeScope,
      CompileContext compileContext) {
    TypeScope current = initialTypeScope;
    List<TypedStatementAst> typedStatementAsts = new ArrayList<>();
    for(StatementAst statement : statements) {
      TypedStatementAst typedStatementAst = statement.toTypedStatementAst(current, compileContext);
      current = typedStatementAst.getAfterTypeScope();
      typedStatementAsts.add(typedStatementAst);
    }

    return TypedFunctionBodyAst.builder()
        .functionBodyAst(this)
        .initialTypeScope(initialTypeScope)
        .statements(typedStatementAsts)
        .build();
  }
}
