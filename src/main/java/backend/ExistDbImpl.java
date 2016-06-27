package backend;

import org.exist.xmldb.EXistResource;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xmldb.api.DatabaseManager;
import org.xmldb.api.base.Collection;
import org.xmldb.api.base.Database;
import org.xmldb.api.base.XMLDBException;
import org.xmldb.api.modules.CollectionManagementService;
import org.xmldb.api.modules.XMLResource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;

/**
 * Created by Tomas on 27.06.2016.
 */
public class ExistDbImpl implements ExistDb {

    private static String URI = "xmldb:exist://localhost:8080/exist/xmlrpc/db/";

    public static Collection setUpDatabase() throws Exception {

        Collection collection = null;

        final String driver = "org.exist.xmldb.DatabaseImpl";
        Class cl = Class.forName(driver);
        Database database = (Database) cl.newInstance();
        database.setProperty("create-database", "true");
        DatabaseManager.registerDatabase(database);

        XMLResource res = null;
        XMLResource res2 = null;

        try {
            collection = getOrCreateCollection("revenueEvidence");

            if(!new File(".\\employees.xml").isFile()) {
                File f = createXmlFile("employees");
                if(!f.canRead()) {
                    //vinimka, logger
                }
                res = (XMLResource)collection.createResource(null, "XMLResource");
                res.setContent(f);
                collection.storeResource(res);
            }

            if(!new File(".\\revenues.xml").isFile()) {
                File f2 = createXmlFile("revenues");
                if(!f2.canRead()) {
                    //vynimika, logger
                }
                res2 = (XMLResource)collection.createResource(null, "XMLResource");
                res2.setContent(f2);
                collection.storeResource(res2);
            }

        } finally {
            if(res != null) {
                try {
                    ((EXistResource)res).freeResources();
                } catch(XMLDBException xe) {
                    xe.printStackTrace();
                }
            }
            if(collection != null) {
                try {
                    collection.close();
                } catch(XMLDBException xe) {
                    xe.printStackTrace();
                }
            }
        }

        return collection;
    }

    public static File createXmlFile(String name) {

        File file = null;

        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement(name);
            doc.appendChild(rootElement);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);

            file = new File(".\\files\\" + name + ".xml");
            StreamResult result = new StreamResult(file);

            transformer.transform(source, result);
        } catch (ParserConfigurationException | TransformerException ex) {
            ex.printStackTrace();
        }

        return file;
    }

    private static Collection getOrCreateCollection(String collectionUri) throws
            XMLDBException {
        return getOrCreateCollection(collectionUri, 0);
    }

    private static Collection getOrCreateCollection(String collectionUri, int pathSegmentOffset) throws XMLDBException {

        Collection col = DatabaseManager.getCollection(URI + collectionUri);
        if(col == null) {
            if(collectionUri.startsWith("/")) {
                collectionUri = collectionUri.substring(1);
            }
            String pathSegments[] = collectionUri.split("/");
            if(pathSegments.length > 0) {
                StringBuilder path = new StringBuilder();
                for(int i = 0; i <= pathSegmentOffset; i++) {
                    path.append("/" + pathSegments[i]);
                }
                Collection start = DatabaseManager.getCollection(URI + path);
                if(start == null) {

                    String parentPath = path.substring(0, path.lastIndexOf("/"));
                    Collection parent = DatabaseManager.getCollection(URI + parentPath);
                    CollectionManagementService mgt = (CollectionManagementService) parent.getService("CollectionManagementService", "1.0");
                    col = mgt.createCollection(pathSegments[pathSegmentOffset]);
                    col.close();
                    parent.close();
                } else {
                    start.close();
                }
            }
            return getOrCreateCollection(collectionUri, ++pathSegmentOffset);
        } else {
            return col;
        }
    }
}
