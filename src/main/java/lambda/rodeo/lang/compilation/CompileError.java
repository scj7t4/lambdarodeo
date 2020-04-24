package lambda.rodeo.lang.compilation;

import lombok.Builder;
import lombok.Getter;
import org.antlr.v4.runtime.ParserRuleContext;

@Builder
@Getter
public class CompileError {

  public static final String UNDEFINED_VAR = "UNDEFINED_VARIABLE";

  private final int startLine;
  private final int endLine;
  private final int characterStart;
  private final String errorType;
  private final String errorMsg;

  public static CompileError undefinedVariableError(String variableName,
      ParserRuleContext ruleContext) {
    return CompileError.builder()
        .errorType(UNDEFINED_VAR)
        .errorMsg("Variable '" + variableName + "' is not defined in the current scope")
        .startLine(ruleContext.getStart().getLine())
        .endLine(ruleContext.getStop().getLine())
        .characterStart(ruleContext.getStart().getCharPositionInLine())
        .build();
  }
}
