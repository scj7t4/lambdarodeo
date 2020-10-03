package lambda.rodeo.runtime.types;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.math.BigInteger;
import nl.jqno.equalsverifier.EqualsVerifier;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;

class LRObjectTest {

  @Test
  public void checkEquals() {
    EqualsVerifier.forClass(LRObject.class).verify();
  }

  @Test
  public void testBasics() {
    LRObject setOne = LRObject.create()
        .set("nice", BigInteger.valueOf(33L), LRInteger.INSTANCE)
        .set("guy", "Cool boyo", LRString.INSTANCE)
        .done();
    assertThat(setOne.get("nice"), Matchers.equalTo(BigInteger.valueOf(33L)));
    assertThat(setOne.getType("nice"), Matchers.equalTo(LRInteger.INSTANCE));
    assertThat(setOne.get("guy"), Matchers.equalTo("Cool boyo"));
    assertThat(setOne.getType("guy"), Matchers.equalTo(LRString.INSTANCE));
    LRObject valueReplace = setOne
        .set("guy", "bling!!", LRString.INSTANCE)
        .done();
    assertThat(valueReplace.get("guy"), Matchers.equalTo("bling!!"));
    assertThat(valueReplace.getType("guy"), Matchers.equalTo(LRString.INSTANCE));
    LRObject valueSet = setOne
        .set("neat", new Atom("beans"), new Atom("beans"))
        .done();
    assertThat(valueSet.get("neat"), Matchers.equalTo(new Atom("beans")));
    assertThat(valueSet.getType("neat"), Matchers.equalTo(new Atom("beans")));
  }
}