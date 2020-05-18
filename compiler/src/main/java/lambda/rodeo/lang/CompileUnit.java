package lambda.rodeo.lang;

import java.io.Closeable;
import java.io.InputStream;
import java.util.function.Supplier;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CompileUnit {
  private final Supplier<InputStream> contents;
  private final String sourcePath;
}
