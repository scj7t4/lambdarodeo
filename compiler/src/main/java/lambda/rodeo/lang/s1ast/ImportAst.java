package lambda.rodeo.lang.s1ast;

import java.util.ArrayList;
import java.util.List;
import lombok.Builder;
import lombok.NonNull;

@Builder
public class ImportAst {
  @Builder.Default
  private final List<String> imports = new ArrayList<>();

  @NonNull
  private final String source;

  private final int startLine;
  private final int endLine;
  private final int characterStart;

}
