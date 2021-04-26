package main;

import org.jdom2.Document;
import org.jdom2.Element;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Deserializer {

    public Object deserialize(Document doc) throws Exception {
        List objList = doc.getRootElement().getChildren();
        Map refMap = new HashMap();
        createInstances( refMap, objList );
        assignFieldValues( refMap, objList );
        return refMap.get("0");
    }

    private void createInstances(Map refMap, List objList) throws Exception {
        for (int i = 0; i < objList.size(); i++) {
            Element objElement = (Element) objList.get(i);
            Class cl = Class.forName(objElement.getAttributeValue("class"));
            Object instance = null;
            if (!cl.isArray()) {
                // use constructor to create instance if not array
                Constructor c = cl.getDeclaredConstructor(null);
                c.setAccessible(true);
                instance = c.newInstance(null);
            } else {
                // create an array instance
                instance = Array.newInstance(cl.getComponentType(),
                        Integer.parseInt(objElement.getAttributeValue("length")));
            }
            refMap.put(objElement.getAttributeValue("id"), instance);
        }
    }

    private void assignFieldValues(Map refMap, List objList) throws Exception {
        for (int i = 0; i < objList.size(); i++) {
            Element objElement = (Element) objList.get(i);
            // retrieve instance of obj in the refMap
            Object instance = refMap.get(objElement.getAttributeValue("id"));
            List fieldElements = objElement.getChildren();

            if (!instance.getClass().isArray() && !instance.getClass().isAssignableFrom(ArrayList.class)) {
                // if not an array, set the fields
                for (int j = 0; j < fieldElements.size(); j++) {
                    Element fieldElement = (Element) fieldElements.get(j);
                    String className = fieldElement.getAttributeValue("declaringclass");
                    Class fieldDeclaringClass = Class.forName(className);
                    String fieldName = fieldElement.getAttributeValue("name");
                    Field f = fieldDeclaringClass.getDeclaredField(fieldName);
                    f.setAccessible(true);

                    Element valueElement = (Element) fieldElement.getChildren().get(0);
                    f.set(instance, deserializeValue( valueElement, f.getType(), refMap));
                }
            } else if (instance.getClass().isAssignableFrom(ArrayList.class)) {
                // is an ArrayList
                //TODO: deserialize arrayLists
            }
            else {
                // is an array
                Class compType = instance.getClass().getComponentType();
                for (int j = 0; j < fieldElements.size(); j++) {
                    Array.set(instance, j, deserializeValue( (Element) fieldElements.get(j), compType, refMap));
                }
            }
        }
    }

    public Object deserializeValue(Element valueElement, Class<?> fieldType, Map refMap) throws Exception {
        String valueType = valueElement.getName();
        if (valueType.equals("null")) {
            return null;
        } else if (valueType.equals("reference")) {
            return refMap.get(valueElement.getText());
        } else {
            // set primitive types
            if (fieldType.equals(boolean.class)) {
                if (valueElement.getText().equals("true")) {
                    return Boolean.TRUE;
                } else {
                    return Boolean.FALSE;
                }
            } else if (fieldType.equals(byte.class)) {
                return Byte.valueOf(valueElement.getText());
            } else if (fieldType.equals(short.class)) {
                return Short.valueOf(valueElement.getText());
            } else if (fieldType.equals(int.class)) {
                return Integer.valueOf(valueElement.getText());
            } else if (fieldType.equals(long.class)) {
                return Long.valueOf(valueElement.getText());
            } else if (fieldType.equals(float.class)) {
                return Float.valueOf(valueElement.getText());
            } else if (fieldType.equals(double.class)) {
                return Double.valueOf(valueElement.getText());
            } else if (fieldType.equals(char.class)) {
                return new Character(valueElement.getText().charAt(0));
            } else {
                return valueElement.getText();
            }
        }
    }

}
