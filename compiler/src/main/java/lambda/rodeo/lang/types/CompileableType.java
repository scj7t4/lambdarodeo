package lambda.rodeo.lang.types;

import java.util.List;
import java.util.Optional;
import lambda.rodeo.lang.compilation.CollectsErrors;
import lambda.rodeo.lang.scope.Entry;
import org.objectweb.asm.MethodVisitor;

public interface CompileableType {

  /**
   * The type this compileable type is derived from.
   */
  LambdaRodeoType getType();

  /**
   * When generating byte-code for this type, this is the descriptor for the Java representation
   * of this type.
   */
  String getDescriptor();

  /**
   * When generating byte-code for this type, this is the internal name for the Java representation
   * of this type.
   */
  String getInternalName();

  /**
   * If this type is represented by a generic, use this to override how the generic is represented
   * in signatures.
   */
  default String getSignature() {
    return getDescriptor();
  }

  /**
   * Tests to see if another compileable type is assignable to this one.
   */
  default boolean assignableFrom(CompileableType other) {
    return this.equals(other);
  }

  /**
   * If true, assign a slot for this argument when allocating memory for arguments.
   */
  default boolean allocateSlot() {
    return true;
  }

  /**
   * If this type needs to be represented in runtime (For example, in pattern matching) this will
   * be used to generate the type.
   */
  void provideRuntimeType(MethodVisitor methodVisitor);

  /**
   * Used to access members of this type via the dot operator.
   *
   * @param parent the scope entry for the item being accessed.
   * @param name the value to the right of the dot.
   * @return An entry that can access the target value after the parent value is accessed.
   */
  Optional<Entry> getMemberEntry(Entry parent, String name);
}
