package lambda.rodeo.lang.expressions;

import lambda.rodeo.lang.statements.TypeScope;
import lambda.rodeo.lang.types.Type;
import lambda.rodeo.lang.values.Computable;
import org.objectweb.asm.MethodVisitor;

public interface ExpressionAst {

  Type getType(TypeScope typeScope);
  Computable<?> getComputable();
  default void compile(MethodVisitor methodVisitor) {

  }

}
