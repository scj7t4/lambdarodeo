package lambda.rodeo.lang.types;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class CompileableLambdaTypeTest {

  @Test
  public void testDescriptorAndSignatureGeneration() {

    List<CompileableType> args = Collections.singletonList(new CompileableAtom("test"));

    CompileableLambdaType type = CompileableLambdaType.builder()
        .args(args)
        .returnType(IntType.INSTANCE)
        .build();

    assertThat(type.getLambdaDescriptor(),
        Matchers.equalTo("Llambda/rodeo/runtime/lambda/Lambda0;"));
    assertThat(type.getDescriptor(),
        Matchers.equalTo("Llambda/rodeo/runtime/lambda/Lambda1;"));
    assertThat(type.getSignature(),
        Matchers.equalTo("Llambda/rodeo/runtime/lambda/Lambda0<Llambda/rodeo/runtime/lambda/Lambda1<Llambda/rodeo/runtime/lambda/Lambda0<Llambda/rodeo/runtime/types/Atom;>;Llambda/rodeo/runtime/lambda/Lambda0<Ljava/math/BigInteger;>;>;>;"));
  }
}