package lambda.rodeo.lang.util;

import org.objectweb.asm.Type;

public class DescriptorUtils {
  public static String genericType(Class<?> baseType, Class<?> t, Class<?>... more) {
    StringBuilder sb = new StringBuilder();
    sb.append(Type.getDescriptor(baseType))
        .setLength(sb.length()-1);
    sb.append('<');
    sb.append(Type.getDescriptor(t));
    for(Class<?> clazz : more) {
      sb.append(Type.getDescriptor(clazz));
    }
    sb.append(">;");
    return sb.toString();
  }

  public static String genericType(Class<?> baseType, String... params) {
    StringBuilder sb = new StringBuilder();
    sb.append(Type.getDescriptor(baseType))
        .setLength(sb.length()-1);
    sb.append('<');
    for (String param : params) {
      sb.append(param);
    }
    sb.append(">;");
    return sb.toString();
  }
}
