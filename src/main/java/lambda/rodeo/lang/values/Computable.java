package lambda.rodeo.lang.values;

import lambda.rodeo.lang.expressions.Scope;

public interface Computable<T> {

  T compute(Scope scope);
}
