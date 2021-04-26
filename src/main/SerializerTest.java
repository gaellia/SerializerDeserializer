package main;

import data.Pokemon;
import org.jdom2.Document;
import org.jdom2.output.XMLOutputter;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class SerializerTest {

    private final ByteArrayOutputStream out = new ByteArrayOutputStream();

    @Before
    public void init() {
        System.setOut(new PrintStream(out));
    }

    @Test
    public void serializeTest() throws Exception {
        Serializer s = new Serializer();
        Pokemon p = new Pokemon();
        p.setDexNum(1);
        p.setLevel(1);
        p.setCanEvolve(false);
        Document doc = s.serialize(p);
        XMLOutputter xmlOutput = new XMLOutputter();
        xmlOutput.output(doc, System.out);
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n" +
                "<serialized><object class=\"data.Pokemon\" id=\"0\"><field name=\"dexNum\" declaringclass=\"data.Pokemon\"><value>1</value></field><field name=\"level\" declaringclass=\"data.Pokemon\"><value>1</value></field><field name=\"canEvolve\" declaringclass=\"data.Pokemon\"><value>false</value></field></object></serialized>\r\n", out.toString());
    }

    @Test (expected = NullPointerException.class)
    public void serializeNullTest() throws Exception {
        Serializer s = new Serializer();
        Document doc = s.serialize(null);
        XMLOutputter xmlOutput = new XMLOutputter();
        xmlOutput.output(doc, System.out);
        assertEquals("", out.toString());
    }
}