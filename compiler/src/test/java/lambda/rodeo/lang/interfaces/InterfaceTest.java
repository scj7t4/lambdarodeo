package lambda.rodeo.lang.interfaces;

import static lambda.rodeo.lang.utils.TestUtils.parseResource;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;

import lambda.rodeo.lang.antlr.LambdaRodeoParser;
import lambda.rodeo.lang.antlr.LambdaRodeoParser.TypeDefContext;
import lambda.rodeo.lang.s1ast.type.TypeDef;
import lambda.rodeo.lang.s1ast.type.TypeDefAstFactory;
import lambda.rodeo.lang.s1ast.type.TypedVar;
import lambda.rodeo.lang.s1ast.type.InterfaceAst;
import lambda.rodeo.lang.types.CompileableAtom;
import lambda.rodeo.lang.types.DefinedType;
import lambda.rodeo.lang.types.IntType;
import lambda.rodeo.lang.types.LRInterface;
import lombok.SneakyThrows;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

public class InterfaceTest {

  @Test
  @SneakyThrows
  public void testInterfaceAstParsing() {
    String interfaceResource = "/test_cases/interfaces/Interface1.rdo";
    LambdaRodeoParser parser = parseResource(interfaceResource);
    TypeDefContext interfaceDefContext = parser.typeDef();

    TypeDefAstFactory typeDefAstFactory = new TypeDefAstFactory(interfaceDefContext);
    TypeDef ast = typeDefAstFactory.toAst();

    assertThat(ast.getIdentifier(), Matchers.equalTo("MyCoolInterface"));
    InterfaceAst interfaceDef = ((LRInterface) ast.getType()).getFrom();
    assertThat(interfaceDef.getMembers(), contains(
        TypedVar.builder()
            .name("member1")
            .type(IntType.INSTANCE)
            .startLine(2)
            .characterStart(2)
            .endLine(2)
            .build(),
        TypedVar.builder()
            .name("member2")
            .type(CompileableAtom.NULL)
            .startLine(3)
            .endLine(3)
            .characterStart(2)
            .build()
    ));
  }

  @Test
  @SneakyThrows
  public void testInterfaceAstParsing2() {
    String interfaceResource = "/test_cases/interfaces/Interface2.rdo";
    LambdaRodeoParser parser = parseResource(interfaceResource);
    TypeDefContext interfaceDefContext = parser.typeDef();

    TypeDefAstFactory typeDefAstFactory = new TypeDefAstFactory(interfaceDefContext);
    TypeDef ast = typeDefAstFactory.toAst();

    assertThat(ast.getIdentifier(), Matchers.equalTo("Tree"));
    InterfaceAst interfaceDef = ((LRInterface) ast.getType()).getFrom();
    assertThat(interfaceDef.getMembers(), contains(
        TypedVar.builder()
            .name("left")
            .type(DefinedType.builder().declaration("Tree").build())
            .startLine(2)
            .characterStart(2)
            .endLine(2)
            .build(),
        TypedVar.builder()
            .name("right")
            .type(DefinedType.builder().declaration("Tree").build())
            .startLine(3)
            .endLine(3)
            .characterStart(2)
            .build()
    ));
  }
}
