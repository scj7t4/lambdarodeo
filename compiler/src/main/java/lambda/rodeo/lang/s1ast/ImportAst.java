package lambda.rodeo.lang.s1ast;

import java.util.ArrayList;
import java.util.List;
import lambda.rodeo.lang.AstNode;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Builder
@Getter
public class ImportAst implements AstNode {
  public enum ImportType {
    MEMBER,
    MODULE
  }

  @Builder.Default
  @NonNull
  private final List<String> imports = new ArrayList<>();

  @NonNull
  private final String source;

  @NonNull
  private final ImportType importType;

  private final int startLine;
  private final int endLine;
  private final int characterStart;

}
