package lambda.rodeo.lang.compilation;

import lombok.Builder;
import lombok.Getter;
import org.antlr.v4.runtime.ParserRuleContext;

@Builder
@Getter
public class CompileError {

  public static final String UNDEFINED_VAR = "UNDEFINED_VARIABLE";
  public static final String POINTLESS_ASSIGNMENT = "POINTLESS_ASSIGNMENT";

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

  public static CompileError lastStatementCannotBeAssignment(ParserRuleContext ruleContext) {
    return CompileError.builder()
        .errorType(POINTLESS_ASSIGNMENT)
        .errorMsg("Assigning to a variable as the last statement of a function body is pointless")
        .startLine(ruleContext.getStart().getLine())
        .endLine(ruleContext.getStop().getLine())
        .characterStart(ruleContext.getStart().getCharPositionInLine())
        .build();
  }
}
