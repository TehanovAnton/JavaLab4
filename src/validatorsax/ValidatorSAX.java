package validatorsax;

import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.IOException;
import java.util.TreeMap;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Logger;

public class ValidatorSAX {
    private static final Logger LOG = Logger.getLogger(String.valueOf(ValidatorSAX.class));

    public static void valid(){
        String fileName = "C:\\Users\\Anton\\source\\repos\\pacei_NV_sovremenueTehnologiyVInternet\\лабораторные\\Lab4(среилилзация)\\javaLabWork3_2curs_2sem\\files\\savedXML.xml";
        String schemaName = "C:\\Users\\Anton\\source\\repos\\pacei_NV_sovremenueTehnologiyVInternet\\лабораторные\\Lab4(среилилзация)\\javaLabWork3_2curs_2sem\\files\\savedSchemaXml.xsd";

        try{
            SchemaFactory factory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
            Schema schema = factory.newSchema(new File(schemaName));
            Validator validator = schema.newValidator();
            validator.validate(new StreamSource(fileName));
        }
        catch (SAXException e) {
            LOG.info(fileName + " SAX error " + e.getMessage());
        }
        catch (IOException e){
            LOG.info(fileName + " IO error " + e.getMessage());
        }
    }
}
