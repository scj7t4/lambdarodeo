package lambda.rodeo.lang.expressions;

import lambda.rodeo.lang.types.Type;
import lambda.rodeo.lang.values.Computable;

public interface ExpressionAst {
  Type getType();
  Computable<?> getComputable();
}
