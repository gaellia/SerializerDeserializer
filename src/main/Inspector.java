package main;

import java.lang.reflect.*;
import java.util.IdentityHashMap;

public class Inspector {

    IdentityHashMap refMap = new IdentityHashMap();
    int refCounter = 0;

    public void inspect(Object obj, boolean recursive) {
        Class c = obj.getClass();
        inspectClass(c, obj, recursive, 0);
    }

    private void inspectClass(Class c, Object obj, boolean recursive, int depth) {

        if (!refMap.containsValue(obj)) {

            refMap.put(refCounter, obj);
            refCounter++;

            // check if it's an array
            if (c.isArray()) {

                arrayHandler(obj, depth, recursive);

            } else {

                // Name of class
                printDepth(depth);
                System.out.println("Class Name: " + c.getName());

                // Name of superclass
                //inspectSuperclass(c, obj, recursive, depth);

                // Name(s) of interfaces
                //inspectInterfaces(c, obj, recursive, depth);

                // Constructors
                //inspectConstructors(c, depth);

                // Methods
                //inspectMethods(c, depth);

                // Fields
                inspectFields(c, obj, recursive, depth);
            }
        } else {
            printDepth(depth);
            System.out.println("~~ Duplicated Object Detected!! ~~");
        }
    }

    /*
     * Takes the depth of recursion and prints the respective tabs
     */
    private void printDepth(int depth) {
        for (int i = 0; i < depth; i++) {
            System.out.print("\t");
        }
    }

    /*
     * Finds the types of a given array of classes
     * and prints them
     */
    private void findTypes(Class[] types) {
        if (types.length == 0) {
            System.out.print("NONE");
        }
        for (Class type : types) {
            System.out.print(type.getTypeName() + " ");
        }
        System.out.println();
    }

    /*
     * Finds the superclass of a class
     * recursively goes into the superclass as well
     */
    public void inspectSuperclass(Class c, Object obj, boolean recursive, int depth) {
        printDepth(depth);
        if (c.getSuperclass() != null) {
            System.out.println("Superclass: " + c.getSuperclass().getName());
            inspectClass(c.getSuperclass(), obj, recursive, depth+1);
        } else {
            System.out.println("Superclass: NONE");
        }
    }

    /*
     * Finds the interfaces of a class
     * recursively goes into the interface class
     */
    public void inspectInterfaces(Class c, Object obj, boolean recursive, int depth) {
        Class[] interfaces = c.getInterfaces();
        printDepth(depth);
        if (interfaces.length == 0) {
            System.out.println("Interfaces: NONE");
        } else {
            System.out.println("Interfaces: ");
            for (Class anInterface : interfaces) {
                printDepth(depth);
                System.out.println(" | " + anInterface.getName());
                inspectClass(anInterface, obj, recursive, depth+1);
            }
        }
    }

    public void inspectConstructors(Class c, int depth) {
        Constructor[] constructors = c.getDeclaredConstructors();
        printDepth(depth);
        System.out.print("Constructors  (" + c.getName() + ")  : ");
        if (constructors.length == 0) {
            System.out.println(" NONE");
        } else {
            System.out.println();
            for (Constructor constructor : constructors) {

                printDepth(depth);
                System.out.println(" | Name: " + constructor.getName());

                printDepth(depth);
                System.out.print("\tParameter Types: ");
                Class[] parameterTypes = constructor.getParameterTypes();
                findTypes(parameterTypes);

                printDepth(depth);
                System.out.println("\tModifiers: " + Modifier.toString(constructor.getModifiers()));
            }
        }
    }

    public void inspectMethods(Class c, int depth) {
        Method[] methods = c.getDeclaredMethods();
        printDepth(depth);
        System.out.print("Methods  (" + c.getName() + ")  : ");
        if (methods.length == 0) {
            System.out.println(" NONE");
        } else {
            System.out.println();
            for (Method method : methods) {
                method.setAccessible(true);

                printDepth(depth);
                System.out.println(" | Name: " + method.getName());

                printDepth(depth);
                System.out.print("\tExceptions: ");
                Class[] exceptionTypes = method.getExceptionTypes();
                findTypes(exceptionTypes);

                printDepth(depth);
                System.out.print("\tParameter Types: ");
                Class[] parameterTypes = method.getParameterTypes();
                findTypes(parameterTypes);

                printDepth(depth);
                System.out.println("\tReturn Type: " + method.getReturnType().getName());

                printDepth(depth);
                System.out.println("\tModifiers: " + Modifier.toString(method.getModifiers()));
            }
        }
    }

    public void inspectFields(Class c, Object obj, boolean recursive, int depth) {
        Field[] fields = c.getDeclaredFields();
        printDepth(depth);
        System.out.print("Fields  (" + c.getName() + ")  : ");
        if (fields.length == 0) {
            System.out.println(" NONE");
        } else {
            System.out.println();
            for (Field field : fields) {
                field.setAccessible(true);

                printDepth(depth);
                System.out.println(" | Name: " + field.getName());

                printDepth(depth);
                System.out.println("\tType: " + field.getType().getName());

                printDepth(depth);
                System.out.println("\tModifiers: " + Modifier.toString(field.getModifiers()));

                printDepth(depth);
                try {
                    Object value = field.get(obj);

                    if (value != null) {

                        // checks primitives and wrappers from autoboxing
                        // and checks member/local/anonymous classes
                        if (value.getClass().isPrimitive() ||
                                value.getClass() == java.lang.Byte.class ||
                                value.getClass() == java.lang.Short.class ||
                                value.getClass() == java.lang.Integer.class ||
                                value.getClass() == java.lang.Long.class ||
                                value.getClass() == java.lang.Float.class ||
                                value.getClass() == java.lang.Double.class ||
                                value.getClass() == java.lang.Character.class ||
                                value.getClass() == java.lang.String.class ||
                                value.getClass() == java.lang.Boolean.class ||
                                value.getClass().isMemberClass() ||
                                value.getClass().isLocalClass() ||
                                value.getClass().isAnonymousClass()) {

                            System.out.println("\tValue: " + value);

                        } else if (value.getClass().isArray()){

                            System.out.println("\tValue: " + value);

                            printDepth(depth+2);
                            System.out.println("Array Name: " + value.getClass().getName());

                            printDepth(depth+2);
                            System.out.println("Component Type: " + value.getClass().getComponentType().getName());

                            printDepth(depth+2);
                            System.out.println("Array Length: " + Array.getLength(value));

                            // array contents
                            printDepth(depth+2);
                            System.out.println("Array Contents: ");
                            printDepth(depth+3);
                            System.out.print(("["));
                            for (int i = 0; i < Array.getLength(value); i++) {
                                if (i > 0) {
                                    System.out.print(", ");
                                }
                                Object arrValue = Array.get(value, i);
                                System.out.print(arrValue);

                                if (arrValue != null) {
                                    // check if not primitive to recurse
                                    if ((!(arrValue.getClass().isPrimitive()) &&
                                            !(arrValue.getClass() == java.lang.Byte.class) &&
                                            !(arrValue.getClass() == java.lang.Short.class) &&
                                            !(arrValue.getClass() == java.lang.Integer.class) &&
                                            !(arrValue.getClass() == java.lang.Long.class) &&
                                            !(arrValue.getClass() == java.lang.Float.class) &&
                                            !(arrValue.getClass() == java.lang.Double.class) &&
                                            !(arrValue.getClass() == java.lang.Character.class) &&
                                            !(arrValue.getClass() == java.lang.String.class) &&
                                            !(arrValue.getClass() == java.lang.Boolean.class)) && recursive) {
                                        inspectClass(arrValue.getClass(), arrValue, recursive, depth+4);
                                    }
                                }
                            }
                            System.out.println("]");

                        } else {
                            int ref = System.identityHashCode(value);
                            System.out.println("\tValue: " + value + "@" + Integer.toHexString(ref));
                            if (recursive == true) {
                                inspectClass(value.getClass(), value, recursive, depth+2);
                            }
                        }
                    } else {
                        System.out.println("\tValue: NULL");
                    }

                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void arrayHandler(Object obj, int depth, boolean recursive) {
        System.out.println();
        printDepth(depth);
        System.out.println("Array Name: " + obj.getClass().getName());

        printDepth(depth);
        System.out.println("Component Type: " + obj.getClass().getComponentType().getName());

        printDepth(depth);
        System.out.println("Array Length: " + Array.getLength(obj));

        // print array contents
        printDepth(depth);
        System.out.println("Array Contents: ");
        printDepth(depth);
        System.out.print(("["));
        for (int i = 0; i < Array.getLength(obj); i++) {
            if (i > 0) {
                System.out.print(", ");
            }
            Object arrValue = Array.get(obj, i);
            System.out.print(arrValue);

            if (arrValue != null) {
                // check if not primitive to recurse
                if ((!(arrValue.getClass().isPrimitive()) &&
                        !(arrValue.getClass() == java.lang.Byte.class) &&
                        !(arrValue.getClass() == java.lang.Short.class) &&
                        !(arrValue.getClass() == java.lang.Integer.class) &&
                        !(arrValue.getClass() == java.lang.Long.class) &&
                        !(arrValue.getClass() == java.lang.Float.class) &&
                        !(arrValue.getClass() == java.lang.Double.class) &&
                        !(arrValue.getClass() == java.lang.Character.class) &&
                        !(arrValue.getClass() == java.lang.String.class) &&
                        !(arrValue.getClass() == java.lang.Boolean.class)) && recursive) {

                    inspectClass(arrValue.getClass(), arrValue, recursive, depth+1);
                }
            }
        }
        System.out.println("]");
    }
}

