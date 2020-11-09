package lambda.rodeo.lang.s1ast.type;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lambda.rodeo.lang.antlr.LambdaRodeoBaseVisitor;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.InterfaceDefContext;
import lambda.rodeo.lang.compilation.CollectsErrors;
import lambda.rodeo.lang.compilation.CompileError;
import lombok.NonNull;

public class InterfaceAstFactory extends LambdaRodeoBaseVisitor<InterfaceAst> {

  private final InterfaceAst ast;
  @NonNull
  private final CollectsErrors compileContext;

  public InterfaceAstFactory(InterfaceDefContext interfaceDef,
      CollectsErrors compileContext) {
    this.compileContext = compileContext;
    ast = visit(interfaceDef);
  }

  public InterfaceAst getAst() {
    return ast;
  }

  @Override
  public InterfaceAst visitInterfaceDef(InterfaceDefContext ctx) {
    List<TypedVar> members = ctx.memberDecl().stream()
        .map(decl -> new TypedVarFactory(decl.typedVar(), compileContext).toAst())
        .collect(Collectors.toList());

    Map<String, List<TypedVar>> usageCount = new HashMap<>();
    members
        .forEach(member -> usageCount
            .computeIfAbsent(member.getName(), k -> new ArrayList<>())
            .add(member));
    usageCount.entrySet().stream()
        .filter(entry -> entry.getValue().size() > 1)
        .flatMap(entry -> entry.getValue().stream().skip(1))
        .forEach((duplicateTypedVar) -> this.compileContext.getCompileErrorCollector().collect(
            CompileError.identifierAlreadyDeclaredInScope(
                duplicateTypedVar,
                duplicateTypedVar.getName())));

    return InterfaceAst.builder()
        .members(members)
        .build();
  }
}
