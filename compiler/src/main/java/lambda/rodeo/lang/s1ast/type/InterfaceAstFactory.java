package lambda.rodeo.lang.s1ast.type;

import java.util.List;
import java.util.stream.Collectors;
import lambda.rodeo.lang.antlr.LambdaRodeoBaseVisitor;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.InterfaceDefContext;

public class InterfaceAstFactory extends LambdaRodeoBaseVisitor<InterfaceAst> {

  private final InterfaceAst ast;

  public InterfaceAstFactory(InterfaceDefContext interfaceDef) {
    ast = visit(interfaceDef);
  }

  public InterfaceAst getAst() {
    return ast;
  }

  @Override
  public InterfaceAst visitInterfaceDef(InterfaceDefContext ctx) {
    List<TypedVar> members = ctx.memberDecl().stream()
        .map(decl -> new TypedVarFactory(decl.typedVar()).toAst())
        .collect(Collectors.toList());

    return InterfaceAst.builder()
        .members(members)
        .build();
  }
}
