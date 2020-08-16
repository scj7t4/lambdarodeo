package lambda.rodeo.lang.compilation;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;

@Getter
public class CompileErrorCollector {

  private final List<CompileError> compileErrors = new ArrayList<>();

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
}
