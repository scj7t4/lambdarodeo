package lambda.rodeo.lang;

import java.io.Closeable;
import java.io.InputStream;
import java.util.function.Supplier;

import lambda.rodeo.lang.util.IoSupplier;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CompileUnit {
  /**
   * A supplier that can provide an input stream for the contents of the unit to compile.
   */
  private final IoSupplier<InputStream> contents;
  /**
   * The path to the source code for the contents.
   *
   * <p>The path should be a relative path from the source root to the file being loaded. It is used to determine
   * the namespace the module is in. If the source path is "example/project/MyFile.rdo" it will be placed in namespace
   * "example/project".</p>
   *
   * <p>When generating sourcePath on systems that do not use '/' as a path separator, convert to a UNIX style path,
   * that is, the path should use '/' as the separator.</p>
   */
  private final String sourcePath;
}
