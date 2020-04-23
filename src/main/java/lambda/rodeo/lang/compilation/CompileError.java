package lambda.rodeo.lang.compilation;

import lombok.Builder;
import lombok.Getter;
import org.antlr.v4.runtime.ParserRuleContext;

@Builder
@Getter
public class CompileError {

  public static final String UNDEFINED_VAR = "UNDEFINED_VARIABLE";

  private final int line;
  private final int characterStart;
  private final int characterStop;
  private final String errorType;
  private final String errorMsg;

  public static CompileError undefinedVariableError(String variableName,
      ParserRuleContext ruleContext) {
    return CompileError.builder()
        .errorMsg("Variable '" + variableName + "' is not defined in the current scope")
        .line(ruleContext.getStart().getLine())
        .characterStart(ruleContext.getStart().getCharPositionInLine())
        .characterStop(ruleContext.getStop().getCharPositionInLine())
        .build();
  }
}
