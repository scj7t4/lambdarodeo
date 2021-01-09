package lambda.rodeo.lang.compilation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import lombok.Getter;

@Getter
public class CompileErrorCollector {

  private final LinkedHashSet<CompileError> compileErrors = new LinkedHashSet<>();

  public void collect(CompileError error) {
    this.compileErrors.add(error);
  }

  public void collectAll(CompileErrorCollector other) {
    this.compileErrors.addAll(other.compileErrors);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();

    for(CompileError error : compileErrors) {
      sb.append(error).append("\n");
    }
    return sb.toString();
  }

  public List<CompileError> getCompileErrors() {
    return new ArrayList<>(compileErrors);
  }
}
