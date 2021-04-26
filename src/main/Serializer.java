package main;

import org.jdom2.*;
import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.IdentityHashMap;

public class Serializer {

    private IdentityHashMap refMap = new IdentityHashMap();
    private Element root = new Element("serialized");
    private Document doc = new Document(root);

    public Document serialize(Object obj) throws Exception {

        // only serialize objects once
        if (!refMap.containsValue(obj)) {

            String id = Integer.toString(refMap.size());
            refMap.put(obj, id);
            Class objClass = obj.getClass();

            Element objElement = new Element("object");
            objElement.setAttribute( "class", objClass.getName() );
            objElement.setAttribute( "id", id );
            doc.getRootElement().addContent(objElement);

            if (objClass.isAssignableFrom(ArrayList.class)) {

                // methods from ArrayList to get values
                Method toArray = ArrayList.class.getDeclaredMethod("toArray", new Class[]{});

                // convert to an array
                Object[] arr = (Object[]) toArray.invoke(obj);

                // serialize array objects
                Class compType = arr.getClass().getComponentType();

                int length = arr.length;
                objElement.setAttribute("length", Integer.toString(length));
                for (int i = 0; i < length; i++) {
                    objElement.addContent( serializeVariable(compType, arr[i]));
                }

            } else if (!objClass.isArray()) {
                serializeFields(objClass, objElement, obj);
            } else {
                // serialize array objects
                Class compType = objClass.getComponentType();

                int length = Array.getLength(obj);
                objElement.setAttribute("length", Integer.toString(length));
                for (int i = 0; i < length; i++) {
                    objElement.addContent( serializeVariable(compType, Array.get(obj, i)));
                }
            }
        }

        return doc;
    }

    public void serializeFields(Class objClass, Element objElement, Object obj) throws Exception {
        Field[] fields = objClass.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {

            if (!Modifier.isPublic(fields[i].getModifiers())) {
                fields[i].setAccessible(true);
            }
            Element fieldElement = new Element("field");
            fieldElement.setAttribute( "name", fields[i].getName() );
            Class decClass = fields[i].getDeclaringClass();
            fieldElement.setAttribute("declaringclass", decClass.getName());

            Class fieldType = fields[i].getType();
            Object child = fields[i].get(obj);

            // skip transient fields
            if (Modifier.isTransient(fields[i].getModifiers())) {
                child = null;
            }

            fieldElement.addContent( serializeVariable(fieldType, child));
            objElement.addContent(fieldElement);
        }
    }

    public Element serializeVariable(Class fieldType, Object child) throws Exception{

        if (child == null) {
            return new Element("null");
            // handles primitive values
        } else if (fieldType.isPrimitive() || fieldType == java.lang.String.class) {
            Element value = new Element("value");
            value.setText(child.toString());
            return value;
        } else {
            // handles ref types
            Element ref = new Element("reference");
            if (refMap.containsKey(child)) {
                ref.setText(refMap.get(child).toString());
            } else {
                ref.setText( Integer.toString(refMap.size()));
                // recurse on the object
                serialize(child);
            }
            return ref;
        }
    }
}
