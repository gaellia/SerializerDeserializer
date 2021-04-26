package main;

import org.jdom2.Element;

import org.junit.Test;

import static org.junit.Assert.*;


public class DeserializerTest {

    @Test
    public void deserializeValueTest() throws Exception {
        Deserializer d = new Deserializer();
        Element e = new Element("obj");
        e.setText("1");
        Object o = d.deserializeValue(e, int.class, null);
        assertNotNull(o);
    }

    @Test (expected = NullPointerException.class)
    public void deserializeValueNullTest() throws Exception {
        Deserializer d = new Deserializer();
        Object o = d.deserializeValue(null, null, null);
        assertNotNull(o);
    }
}