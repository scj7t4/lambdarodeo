package lambda.rodeo.lang.values;

import lambda.rodeo.lang.statement.Scope;

public interface Computable<T> {

  T compute(Scope scope);
}
