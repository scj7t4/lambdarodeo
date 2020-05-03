package lambda.rodeo.lang.compileable.functions;

import java.util.List;
import lambda.rodeo.lang.compilation.CompileContext;
import lambda.rodeo.lang.compileable.statement.CompileableStatement;
import lambda.rodeo.lang.typed.functions.TypedFunctionBody;
import lambda.rodeo.lang.scope.CompileableTypeScope;
import lambda.rodeo.lang.types.Type;
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
  private final TypedFunctionBody typedFunctionBody;


  public Type getReturnType() {
    return statements.get(statements.size() - 1)
        .getCompileableExpr()
        .getTypedExpression()
        .getType();
  }

  public void compile(MethodVisitor methodVisitor,
      CompileContext compileContext) {
    for (CompileableStatement statement : statements) {
      statement.compile(methodVisitor, compileContext);
    }
  }

  public CompileableTypeScope getFinalTypeScope() {
    return statements.get(statements.size() - 1)
        .getAfterTypeScope();
  }

}