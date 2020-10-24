package lambda.rodeo.lang.types;

import java.util.Map;
import org.objectweb.asm.ClassWriter;

public interface CompilesToInnerClass {
  public void forwardDeclare(String name, ClassWriter classWriter);

  /**
   * Generates the byte code for the inner class.
   *
   * @param internalName The internal name of the inner class.
   * @param parentInternalName The name of the parent class.
   * @param name The name of this class.
   * @param classWriter The parent class writer
   * @return The bytecode for the inner class
   */
  public Map<String, byte[]> declareAndCompile(String internalName,
      String parentInternalName,
      String name,
      ClassWriter classWriter);

}
