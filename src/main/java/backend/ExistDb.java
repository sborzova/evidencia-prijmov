package backend;

import org.xmldb.api.base.Collection;
import org.xmldb.api.base.XMLDBException;

import java.io.File;

/**
 * Created by Tomas on 27.06.2016.
 */
public interface ExistDb {

    Collection setUpDatabase() throws Exception;

    File createXmlFile(String name);

    Collection getOrCreateCollection(String collectionUri, int pathSegmentOffset) throws XMLDBException;
}
