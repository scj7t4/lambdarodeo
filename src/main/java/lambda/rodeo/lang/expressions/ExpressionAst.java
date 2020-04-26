package lambda.rodeo.lang.expressions;

import lambda.rodeo.lang.statements.TypeScope;
import lambda.rodeo.lang.types.Type;
import org.objectweb.asm.MethodVisitor;

public interface ExpressionAst {

  Type getType(TypeScope typeScope);

  void compile(MethodVisitor methodVisitor);

}
