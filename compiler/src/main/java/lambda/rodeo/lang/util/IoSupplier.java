package lambda.rodeo.lang.util;

import java.io.IOException;

public interface IoSupplier<T> {
    T get() throws IOException;
}
