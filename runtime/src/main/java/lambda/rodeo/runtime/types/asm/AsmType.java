// ASM: a very small and fast Java bytecode manipulation framework
// Copyright (c) 2000-2011 INRIA, France Telecom
// All rights reserved.
//
// Redistribution and use in source and binary forms, with or without
// modification, are permitted provided that the following conditions
// are met:
// 1. Redistributions of source code must retain the above copyright
//    notice, this list of conditions and the following disclaimer.
// 2. Redistributions in binary form must reproduce the above copyright
//    notice, this list of conditions and the following disclaimer in the
//    documentation and/or other materials provided with the distribution.
// 3. Neither the name of the copyright holders nor the names of its
//    contributors may be used to endorse or promote products derived from
//    this software without specific prior written permission.
//
// THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
// AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
// IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
// ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE
// LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
// CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
// SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
// INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
// CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
// ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
// THE POSSIBILITY OF SUCH DAMAGE.
package lambda.rodeo.runtime.types.asm;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/**
 * A Java field or method type. This class can be used to make it easier to manipulate type and
 * method descriptors.
 *
 * @author Eric Bruneton
 * @author Chris Nokleberg
 */
public final class AsmType {

  /** The sort of the {@code void} type. See {@link #getSort}. */
  public static final int VOID = 0;

  /** The sort of the {@code boolean} type. See {@link #getSort}. */
  public static final int BOOLEAN = 1;

  /** The sort of the {@code char} type. See {@link #getSort}. */
  public static final int CHAR = 2;

  /** The sort of the {@code byte} type. See {@link #getSort}. */
  public static final int BYTE = 3;

  /** The sort of the {@code short} type. See {@link #getSort}. */
  public static final int SHORT = 4;

  /** The sort of the {@code int} type. See {@link #getSort}. */
  public static final int INT = 5;

  /** The sort of the {@code float} type. See {@link #getSort}. */
  public static final int FLOAT = 6;

  /** The sort of the {@code long} type. See {@link #getSort}. */
  public static final int LONG = 7;

  /** The sort of the {@code double} type. See {@link #getSort}. */
  public static final int DOUBLE = 8;

  /** The sort of array reference types. See {@link #getSort}. */
  public static final int ARRAY = 9;

  /** The sort of object reference types. See {@link #getSort}. */
  public static final int OBJECT = 10;

  /** The sort of method types. See {@link #getSort}. */
  public static final int METHOD = 11;

  /** The (private) sort of object reference types represented with an internal name. */
  private static final int INTERNAL = 12;

  /** The descriptors of the primitive types. */
  private static final String PRIMITIVE_DESCRIPTORS = "VZCBSIFJD";

  /** The {@code void} type. */
  public static final AsmType VOID_TYPE = new AsmType(VOID, PRIMITIVE_DESCRIPTORS, VOID, VOID + 1);

  /** The {@code boolean} type. */
  public static final AsmType BOOLEAN_TYPE =
      new AsmType(BOOLEAN, PRIMITIVE_DESCRIPTORS, BOOLEAN, BOOLEAN + 1);

  /** The {@code char} type. */
  public static final AsmType CHAR_TYPE = new AsmType(CHAR, PRIMITIVE_DESCRIPTORS, CHAR, CHAR + 1);

  /** The {@code byte} type. */
  public static final AsmType BYTE_TYPE = new AsmType(BYTE, PRIMITIVE_DESCRIPTORS, BYTE, BYTE + 1);

  /** The {@code short} type. */
  public static final AsmType SHORT_TYPE = new AsmType(SHORT, PRIMITIVE_DESCRIPTORS, SHORT, SHORT + 1);

  /** The {@code int} type. */
  public static final AsmType INT_TYPE = new AsmType(INT, PRIMITIVE_DESCRIPTORS, INT, INT + 1);

  /** The {@code float} type. */
  public static final AsmType FLOAT_TYPE = new AsmType(FLOAT, PRIMITIVE_DESCRIPTORS, FLOAT, FLOAT + 1);

  /** The {@code long} type. */
  public static final AsmType LONG_TYPE = new AsmType(LONG, PRIMITIVE_DESCRIPTORS, LONG, LONG + 1);

  /** The {@code double} type. */
  public static final AsmType DOUBLE_TYPE =
      new AsmType(DOUBLE, PRIMITIVE_DESCRIPTORS, DOUBLE, DOUBLE + 1);

  // -----------------------------------------------------------------------------------------------
  // Fields
  // -----------------------------------------------------------------------------------------------

  /**
   * The sort of this type. Either {@link #VOID}, {@link #BOOLEAN}, {@link #CHAR}, {@link #BYTE},
   * {@link #SHORT}, {@link #INT}, {@link #FLOAT}, {@link #LONG}, {@link #DOUBLE}, {@link #ARRAY},
   * {@link #OBJECT}, {@link #METHOD} or {@link #INTERNAL}.
   */
  private final int sort;

  /**
   * A buffer containing the value of this field or method type. This value is an internal name for
   * {@link #OBJECT} and {@link #INTERNAL} types, and a field or method descriptor in the other
   * cases.
   *
   * <p>For {@link #OBJECT} types, this field also contains the descriptor: the characters in
   * [{@link #valueBegin},{@link #valueEnd}) contain the internal name, and those in [{@link
   * #valueBegin} - 1, {@link #valueEnd} + 1) contain the descriptor.
   */
  private final String valueBuffer;

  /**
   * The beginning index, inclusive, of the value of this Java field or method type in {@link
   * #valueBuffer}. This value is an internal name for {@link #OBJECT} and {@link #INTERNAL} types,
   * and a field or method descriptor in the other cases.
   */
  private final int valueBegin;

  /**
   * The end index, exclusive, of the value of this Java field or method type in {@link
   * #valueBuffer}. This value is an internal name for {@link #OBJECT} and {@link #INTERNAL} types,
   * and a field or method descriptor in the other cases.
   */
  private final int valueEnd;

  /**
   * Constructs a reference type.
   *
   * @param sort the sort of this type, see {@link #sort}.
   * @param valueBuffer a buffer containing the value of this field or method type.
   * @param valueBegin the beginning index, inclusive, of the value of this field or method type in
   *     valueBuffer.
   * @param valueEnd the end index, exclusive, of the value of this field or method type in
   *     valueBuffer.
   */
  private AsmType(final int sort, final String valueBuffer, final int valueBegin, final int valueEnd) {
    this.sort = sort;
    this.valueBuffer = valueBuffer;
    this.valueBegin = valueBegin;
    this.valueEnd = valueEnd;
  }

  // -----------------------------------------------------------------------------------------------
  // Methods to get Type(s) from a descriptor, a reflected Method or Constructor, other types, etc.
  // -----------------------------------------------------------------------------------------------

  /**
   * Returns the {@link AsmType} corresponding to the given type descriptor.
   *
   * @param typeDescriptor a field or method type descriptor.
   * @return the {@link AsmType} corresponding to the given type descriptor.
   */
  public static AsmType getType(final String typeDescriptor) {
    return getTypeInternal(typeDescriptor, 0, typeDescriptor.length());
  }

  /**
   * Returns the {@link AsmType} corresponding to the given class.
   *
   * @param clazz a class.
   * @return the {@link AsmType} corresponding to the given class.
   */
  public static AsmType getType(final Class<?> clazz) {
    if (clazz.isPrimitive()) {
      if (clazz == Integer.TYPE) {
        return INT_TYPE;
      } else if (clazz == Void.TYPE) {
        return VOID_TYPE;
      } else if (clazz == Boolean.TYPE) {
        return BOOLEAN_TYPE;
      } else if (clazz == Byte.TYPE) {
        return BYTE_TYPE;
      } else if (clazz == Character.TYPE) {
        return CHAR_TYPE;
      } else if (clazz == Short.TYPE) {
        return SHORT_TYPE;
      } else if (clazz == Double.TYPE) {
        return DOUBLE_TYPE;
      } else if (clazz == Float.TYPE) {
        return FLOAT_TYPE;
      } else if (clazz == Long.TYPE) {
        return LONG_TYPE;
      } else {
        throw new AssertionError();
      }
    } else {
      return getType(getDescriptor(clazz));
    }
  }

  /**
   * Returns the method {@link AsmType} corresponding to the given constructor.
   *
   * @param constructor a {@link Constructor} object.
   * @return the method {@link AsmType} corresponding to the given constructor.
   */
  public static AsmType getType(final Constructor<?> constructor) {
    return getType(getConstructorDescriptor(constructor));
  }

  /**
   * Returns the method {@link AsmType} corresponding to the given method.
   *
   * @param method a {@link Method} object.
   * @return the method {@link AsmType} corresponding to the given method.
   */
  public static AsmType getType(final Method method) {
    return getType(getMethodDescriptor(method));
  }

  /**
   * Returns the type of the elements of this array type. This method should only be used for an
   * array type.
   *
   * @return Returns the type of the elements of this array type.
   */
  public AsmType getElementType() {
    final int numDimensions = getDimensions();
    return getTypeInternal(valueBuffer, valueBegin + numDimensions, valueEnd);
  }

  /**
   * Returns the {@link AsmType} corresponding to the given internal name.
   *
   * @param internalName an internal name.
   * @return the {@link AsmType} corresponding to the given internal name.
   */
  public static AsmType getObjectType(final String internalName) {
    return new AsmType(
        internalName.charAt(0) == '[' ? ARRAY : INTERNAL, internalName, 0, internalName.length());
  }

  /**
   * Returns the {@link AsmType} corresponding to the given method descriptor. Equivalent to <code>
   * Type.getType(methodDescriptor)</code>.
   *
   * @param methodDescriptor a method descriptor.
   * @return the {@link AsmType} corresponding to the given method descriptor.
   */
  public static AsmType getMethodType(final String methodDescriptor) {
    return new AsmType(METHOD, methodDescriptor, 0, methodDescriptor.length());
  }

  /**
   * Returns the method {@link AsmType} corresponding to the given argument and return types.
   *
   * @param returnType the return type of the method.
   * @param argumentTypes the argument types of the method.
   * @return the method {@link AsmType} corresponding to the given argument and return types.
   */
  public static AsmType getMethodType(final AsmType returnType, final AsmType... argumentTypes) {
    return getType(getMethodDescriptor(returnType, argumentTypes));
  }

  /**
   * Returns the argument types of methods of this type. This method should only be used for method
   * types.
   *
   * @return the argument types of methods of this type.
   */
  public AsmType[] getArgumentTypes() {
    return getArgumentTypes(getDescriptor());
  }

  /**
   * Returns the {@link AsmType} values corresponding to the argument types of the given method
   * descriptor.
   *
   * @param methodDescriptor a method descriptor.
   * @return the {@link AsmType} values corresponding to the argument types of the given method
   *     descriptor.
   */
  public static AsmType[] getArgumentTypes(final String methodDescriptor) {
    // First step: compute the number of argument types in methodDescriptor.
    int numArgumentTypes = 0;
    // Skip the first character, which is always a '('.
    int currentOffset = 1;
    // Parse the argument types, one at a each loop iteration.
    while (methodDescriptor.charAt(currentOffset) != ')') {
      while (methodDescriptor.charAt(currentOffset) == '[') {
        currentOffset++;
      }
      if (methodDescriptor.charAt(currentOffset++) == 'L') {
        // Skip the argument descriptor content.
        int semiColumnOffset = methodDescriptor.indexOf(';', currentOffset);
        currentOffset = Math.max(currentOffset, semiColumnOffset + 1);
      }
      ++numArgumentTypes;
    }

    // Second step: create a Type instance for each argument type.
    AsmType[] argumentTypes = new AsmType[numArgumentTypes];
    // Skip the first character, which is always a '('.
    currentOffset = 1;
    // Parse and create the argument types, one at each loop iteration.
    int currentArgumentTypeIndex = 0;
    while (methodDescriptor.charAt(currentOffset) != ')') {
      final int currentArgumentTypeOffset = currentOffset;
      while (methodDescriptor.charAt(currentOffset) == '[') {
        currentOffset++;
      }
      if (methodDescriptor.charAt(currentOffset++) == 'L') {
        // Skip the argument descriptor content.
        int semiColumnOffset = methodDescriptor.indexOf(';', currentOffset);
        currentOffset = Math.max(currentOffset, semiColumnOffset + 1);
      }
      argumentTypes[currentArgumentTypeIndex++] =
          getTypeInternal(methodDescriptor, currentArgumentTypeOffset, currentOffset);
    }
    return argumentTypes;
  }

  /**
   * Returns the {@link AsmType} values corresponding to the argument types of the given method.
   *
   * @param method a method.
   * @return the {@link AsmType} values corresponding to the argument types of the given method.
   */
  public static AsmType[] getArgumentTypes(final Method method) {
    Class<?>[] classes = method.getParameterTypes();
    AsmType[] types = new AsmType[classes.length];
    for (int i = classes.length - 1; i >= 0; --i) {
      types[i] = getType(classes[i]);
    }
    return types;
  }

  /**
   * Returns the return type of methods of this type. This method should only be used for method
   * types.
   *
   * @return the return type of methods of this type.
   */
  public AsmType getReturnType() {
    return getReturnType(getDescriptor());
  }

  /**
   * Returns the {@link AsmType} corresponding to the return type of the given method descriptor.
   *
   * @param methodDescriptor a method descriptor.
   * @return the {@link AsmType} corresponding to the return type of the given method descriptor.
   */
  public static AsmType getReturnType(final String methodDescriptor) {
    return getTypeInternal(
        methodDescriptor, getReturnTypeOffset(methodDescriptor), methodDescriptor.length());
  }

  /**
   * Returns the {@link AsmType} corresponding to the return type of the given method.
   *
   * @param method a method.
   * @return the {@link AsmType} corresponding to the return type of the given method.
   */
  public static AsmType getReturnType(final Method method) {
    return getType(method.getReturnType());
  }

  /**
   * Returns the start index of the return type of the given method descriptor.
   *
   * @param methodDescriptor a method descriptor.
   * @return the start index of the return type of the given method descriptor.
   */
  static int getReturnTypeOffset(final String methodDescriptor) {
    // Skip the first character, which is always a '('.
    int currentOffset = 1;
    // Skip the argument types, one at a each loop iteration.
    while (methodDescriptor.charAt(currentOffset) != ')') {
      while (methodDescriptor.charAt(currentOffset) == '[') {
        currentOffset++;
      }
      if (methodDescriptor.charAt(currentOffset++) == 'L') {
        // Skip the argument descriptor content.
        int semiColumnOffset = methodDescriptor.indexOf(';', currentOffset);
        currentOffset = Math.max(currentOffset, semiColumnOffset + 1);
      }
    }
    return currentOffset + 1;
  }

  /**
   * Returns the {@link AsmType} corresponding to the given field or method descriptor.
   *
   * @param descriptorBuffer a buffer containing the field or method descriptor.
   * @param descriptorBegin the beginning index, inclusive, of the field or method descriptor in
   *     descriptorBuffer.
   * @param descriptorEnd the end index, exclusive, of the field or method descriptor in
   *     descriptorBuffer.
   * @return the {@link AsmType} corresponding to the given type descriptor.
   */
  private static AsmType getTypeInternal(
      final String descriptorBuffer, final int descriptorBegin, final int descriptorEnd) {
    switch (descriptorBuffer.charAt(descriptorBegin)) {
      case 'V':
        return VOID_TYPE;
      case 'Z':
        return BOOLEAN_TYPE;
      case 'C':
        return CHAR_TYPE;
      case 'B':
        return BYTE_TYPE;
      case 'S':
        return SHORT_TYPE;
      case 'I':
        return INT_TYPE;
      case 'F':
        return FLOAT_TYPE;
      case 'J':
        return LONG_TYPE;
      case 'D':
        return DOUBLE_TYPE;
      case '[':
        return new AsmType(ARRAY, descriptorBuffer, descriptorBegin, descriptorEnd);
      case 'L':
        return new AsmType(OBJECT, descriptorBuffer, descriptorBegin + 1, descriptorEnd - 1);
      case '(':
        return new AsmType(METHOD, descriptorBuffer, descriptorBegin, descriptorEnd);
      default:
        throw new IllegalArgumentException();
    }
  }

  // -----------------------------------------------------------------------------------------------
  // Methods to get class names, internal names or descriptors.
  // -----------------------------------------------------------------------------------------------

  /**
   * Returns the binary name of the class corresponding to this type. This method must not be used
   * on method types.
   *
   * @return the binary name of the class corresponding to this type.
   */
  public String getClassName() {
    switch (sort) {
      case VOID:
        return "void";
      case BOOLEAN:
        return "boolean";
      case CHAR:
        return "char";
      case BYTE:
        return "byte";
      case SHORT:
        return "short";
      case INT:
        return "int";
      case FLOAT:
        return "float";
      case LONG:
        return "long";
      case DOUBLE:
        return "double";
      case ARRAY:
        StringBuilder stringBuilder = new StringBuilder(getElementType().getClassName());
        for (int i = getDimensions(); i > 0; --i) {
          stringBuilder.append("[]");
        }
        return stringBuilder.toString();
      case OBJECT:
      case INTERNAL:
        return valueBuffer.substring(valueBegin, valueEnd).replace('/', '.');
      default:
        throw new AssertionError();
    }
  }

  /**
   * Returns the internal name of the class corresponding to this object or array type. The internal
   * name of a class is its fully qualified name (as returned by Class.getName(), where '.' are
   * replaced by '/'). This method should only be used for an object or array type.
   *
   * @return the internal name of the class corresponding to this object type.
   */
  public String getInternalName() {
    return valueBuffer.substring(valueBegin, valueEnd);
  }

  /**
   * Returns the internal name of the given class. The internal name of a class is its fully
   * qualified name, as returned by Class.getName(), where '.' are replaced by '/'.
   *
   * @param clazz an object or array class.
   * @return the internal name of the given class.
   */
  public static String getInternalName(final Class<?> clazz) {
    return clazz.getName().replace('.', '/');
  }

  /**
   * Returns the descriptor corresponding to this type.
   *
   * @return the descriptor corresponding to this type.
   */
  public String getDescriptor() {
    if (sort == OBJECT) {
      return valueBuffer.substring(valueBegin - 1, valueEnd + 1);
    } else if (sort == INTERNAL) {
      return 'L' + valueBuffer.substring(valueBegin, valueEnd) + ';';
    } else {
      return valueBuffer.substring(valueBegin, valueEnd);
    }
  }

  /**
   * Returns the descriptor corresponding to the given class.
   *
   * @param clazz an object class, a primitive class or an array class.
   * @return the descriptor corresponding to the given class.
   */
  public static String getDescriptor(final Class<?> clazz) {
    StringBuilder stringBuilder = new StringBuilder();
    appendDescriptor(clazz, stringBuilder);
    return stringBuilder.toString();
  }

  /**
   * Returns the descriptor corresponding to the given constructor.
   *
   * @param constructor a {@link Constructor} object.
   * @return the descriptor of the given constructor.
   */
  public static String getConstructorDescriptor(final Constructor<?> constructor) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append('(');
    Class<?>[] parameters = constructor.getParameterTypes();
    for (Class<?> parameter : parameters) {
      appendDescriptor(parameter, stringBuilder);
    }
    return stringBuilder.append(")V").toString();
  }

  /**
   * Returns the descriptor corresponding to the given argument and return types.
   *
   * @param returnType the return type of the method.
   * @param argumentTypes the argument types of the method.
   * @return the descriptor corresponding to the given argument and return types.
   */
  public static String getMethodDescriptor(final AsmType returnType, final AsmType... argumentTypes) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append('(');
    for (AsmType argumentType : argumentTypes) {
      argumentType.appendDescriptor(stringBuilder);
    }
    stringBuilder.append(')');
    returnType.appendDescriptor(stringBuilder);
    return stringBuilder.toString();
  }

  /**
   * Returns the descriptor corresponding to the given method.
   *
   * @param method a {@link Method} object.
   * @return the descriptor of the given method.
   */
  public static String getMethodDescriptor(final Method method) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append('(');
    Class<?>[] parameters = method.getParameterTypes();
    for (Class<?> parameter : parameters) {
      appendDescriptor(parameter, stringBuilder);
    }
    stringBuilder.append(')');
    appendDescriptor(method.getReturnType(), stringBuilder);
    return stringBuilder.toString();
  }

  /**
   * Appends the descriptor corresponding to this type to the given string buffer.
   *
   * @param stringBuilder the string builder to which the descriptor must be appended.
   */
  private void appendDescriptor(final StringBuilder stringBuilder) {
    if (sort == OBJECT) {
      stringBuilder.append(valueBuffer, valueBegin - 1, valueEnd + 1);
    } else if (sort == INTERNAL) {
      stringBuilder.append('L').append(valueBuffer, valueBegin, valueEnd).append(';');
    } else {
      stringBuilder.append(valueBuffer, valueBegin, valueEnd);
    }
  }

  /**
   * Appends the descriptor of the given class to the given string builder.
   *
   * @param clazz the class whose descriptor must be computed.
   * @param stringBuilder the string builder to which the descriptor must be appended.
   */
  private static void appendDescriptor(final Class<?> clazz, final StringBuilder stringBuilder) {
    Class<?> currentClass = clazz;
    while (currentClass.isArray()) {
      stringBuilder.append('[');
      currentClass = currentClass.getComponentType();
    }
    if (currentClass.isPrimitive()) {
      char descriptor;
      if (currentClass == Integer.TYPE) {
        descriptor = 'I';
      } else if (currentClass == Void.TYPE) {
        descriptor = 'V';
      } else if (currentClass == Boolean.TYPE) {
        descriptor = 'Z';
      } else if (currentClass == Byte.TYPE) {
        descriptor = 'B';
      } else if (currentClass == Character.TYPE) {
        descriptor = 'C';
      } else if (currentClass == Short.TYPE) {
        descriptor = 'S';
      } else if (currentClass == Double.TYPE) {
        descriptor = 'D';
      } else if (currentClass == Float.TYPE) {
        descriptor = 'F';
      } else if (currentClass == Long.TYPE) {
        descriptor = 'J';
      } else {
        throw new AssertionError();
      }
      stringBuilder.append(descriptor);
    } else {
      stringBuilder.append('L').append(getInternalName(currentClass)).append(';');
    }
  }

  // -----------------------------------------------------------------------------------------------
  // Methods to get the sort, dimension, size, and opcodes corresponding to a Type or descriptor.
  // -----------------------------------------------------------------------------------------------

  /**
   * Returns the sort of this type.
   *
   * @return {@link #VOID}, {@link #BOOLEAN}, {@link #CHAR}, {@link #BYTE}, {@link #SHORT}, {@link
   *     #INT}, {@link #FLOAT}, {@link #LONG}, {@link #DOUBLE}, {@link #ARRAY}, {@link #OBJECT} or
   *     {@link #METHOD}.
   */
  public int getSort() {
    return sort == INTERNAL ? OBJECT : sort;
  }

  /**
   * Returns the number of dimensions of this array type. This method should only be used for an
   * array type.
   *
   * @return the number of dimensions of this array type.
   */
  public int getDimensions() {
    int numDimensions = 1;
    while (valueBuffer.charAt(valueBegin + numDimensions) == '[') {
      numDimensions++;
    }
    return numDimensions;
  }

  /**
   * Returns the size of values of this type. This method must not be used for method types.
   *
   * @return the size of values of this type, i.e., 2 for {@code long} and {@code double}, 0 for
   *     {@code void} and 1 otherwise.
   */
  public int getSize() {
    switch (sort) {
      case VOID:
        return 0;
      case BOOLEAN:
      case CHAR:
      case BYTE:
      case SHORT:
      case INT:
      case FLOAT:
      case ARRAY:
      case OBJECT:
      case INTERNAL:
        return 1;
      case LONG:
      case DOUBLE:
        return 2;
      default:
        throw new AssertionError();
    }
  }

  /**
   * Returns the size of the arguments and of the return value of methods of this type. This method
   * should only be used for method types.
   *
   * @return the size of the arguments of the method (plus one for the implicit this argument),
   *     argumentsSize, and the size of its return value, returnSize, packed into a single int i =
   *     {@code (argumentsSize &lt;&lt; 2) | returnSize} (argumentsSize is therefore equal to {@code
   *     i &gt;&gt; 2}, and returnSize to {@code i &amp; 0x03}).
   */
  public int getArgumentsAndReturnSizes() {
    return getArgumentsAndReturnSizes(getDescriptor());
  }

  /**
   * Computes the size of the arguments and of the return value of a method.
   *
   * @param methodDescriptor a method descriptor.
   * @return the size of the arguments of the method (plus one for the implicit this argument),
   *     argumentsSize, and the size of its return value, returnSize, packed into a single int i =
   *     {@code (argumentsSize &lt;&lt; 2) | returnSize} (argumentsSize is therefore equal to {@code
   *     i &gt;&gt; 2}, and returnSize to {@code i &amp; 0x03}).
   */
  public static int getArgumentsAndReturnSizes(final String methodDescriptor) {
    int argumentsSize = 1;
    // Skip the first character, which is always a '('.
    int currentOffset = 1;
    int currentChar = methodDescriptor.charAt(currentOffset);
    // Parse the argument types and compute their size, one at a each loop iteration.
    while (currentChar != ')') {
      if (currentChar == 'J' || currentChar == 'D') {
        currentOffset++;
        argumentsSize += 2;
      } else {
        while (methodDescriptor.charAt(currentOffset) == '[') {
          currentOffset++;
        }
        if (methodDescriptor.charAt(currentOffset++) == 'L') {
          // Skip the argument descriptor content.
          int semiColumnOffset = methodDescriptor.indexOf(';', currentOffset);
          currentOffset = Math.max(currentOffset, semiColumnOffset + 1);
        }
        argumentsSize += 1;
      }
      currentChar = methodDescriptor.charAt(currentOffset);
    }
    currentChar = methodDescriptor.charAt(currentOffset + 1);
    if (currentChar == 'V') {
      return argumentsSize << 2;
    } else {
      int returnSize = (currentChar == 'J' || currentChar == 'D') ? 2 : 1;
      return argumentsSize << 2 | returnSize;
    }
  }

  // -----------------------------------------------------------------------------------------------
  // Equals, hashCode and toString.
  // -----------------------------------------------------------------------------------------------

  /**
   * Tests if the given object is equal to this type.
   *
   * @param object the object to be compared to this type.
   * @return {@literal true} if the given object is equal to this type.
   */
  @Override
  public boolean equals(final Object object) {
    if (this == object) {
      return true;
    }
    if (!(object instanceof AsmType)) {
      return false;
    }
    AsmType other = (AsmType) object;
    if ((sort == INTERNAL ? OBJECT : sort) != (other.sort == INTERNAL ? OBJECT : other.sort)) {
      return false;
    }
    int begin = valueBegin;
    int end = valueEnd;
    int otherBegin = other.valueBegin;
    int otherEnd = other.valueEnd;
    // Compare the values.
    if (end - begin != otherEnd - otherBegin) {
      return false;
    }
    for (int i = begin, j = otherBegin; i < end; i++, j++) {
      if (valueBuffer.charAt(i) != other.valueBuffer.charAt(j)) {
        return false;
      }
    }
    return true;
  }

  /**
   * Returns a hash code value for this type.
   *
   * @return a hash code value for this type.
   */
  @Override
  public int hashCode() {
    int hashCode = 13 * (sort == INTERNAL ? OBJECT : sort);
    if (sort >= ARRAY) {
      for (int i = valueBegin, end = valueEnd; i < end; i++) {
        hashCode = 17 * (hashCode + valueBuffer.charAt(i));
      }
    }
    return hashCode;
  }

  /**
   * Returns a string representation of this type.
   *
   * @return the descriptor of this type.
   */
  @Override
  public String toString() {
    return getDescriptor();
  }
}
