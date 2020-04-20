package lambda.rodeo.lang.expressions;

import lambda.rodeo.lang.types.Type;
import lambda.rodeo.lang.values.Computable;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
public class ConstantExpr<T> implements ExpressionAst {
  Computable<T> computable;
  Type type;
}
