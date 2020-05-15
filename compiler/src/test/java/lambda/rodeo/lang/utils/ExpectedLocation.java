package lambda.rodeo.lang.utils;

import lambda.rodeo.lang.AstNode;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ExpectedLocation implements AstNode {
  private final int startLine;
  private final int endLine;
  private final int characterStart;
}
