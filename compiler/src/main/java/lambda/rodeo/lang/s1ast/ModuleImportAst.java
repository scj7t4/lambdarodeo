package lambda.rodeo.lang.s1ast;

import java.util.ArrayList;
import java.util.List;
import lambda.rodeo.lang.AstNode;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

@Builder
@Getter
public class ModuleImportAst implements AstNode {


  @NonNull
  private final String source;

  @NonNull
  private final String alias;


  private final int startLine;
  private final int endLine;
  private final int characterStart;

}
