package railo.extension.io.cache.infinispan;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import org.apache.xerces.parsers.DOMParser;
import org.infinispan.config.Configuration;
import org.infinispan.config.GlobalConfiguration;
import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.manager.EmbeddedCacheManager;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import railo.runtime.config.Config;

public class InfinispanCacheManager extends DefaultCacheManager {

	private static EmbeddedCacheManager manager;
	private static Boolean starting = false;
	
	public static EmbeddedCacheManager getInstance(Config config, Properties properties) {
		if(!starting && manager == null) {
			starting = true;
			manager = new DefaultCacheManager(
			         GlobalConfiguration.getClusteredDefault()
			            .fluent()
			               .transport()
			                  //.addProperty("configurationFile", "jgroups-tcp.xml")
			                  .addProperty("configurationFile", "jgroups-udp.xml")
							    .machineId("qa-machine").rackId("qa-rack")
							    .clusterName("railoInfinispanCluster")
			               .build(), 
			         new Configuration()
			            .fluent()
			            .storeAsBinary()
			               .clustering()
			                  .mode(Configuration.CacheMode.REPL_SYNC)
//							    .mode(Configuration.CacheMode.DIST_SYNC)
//							    .sync()
//							    .l1().lifespan(25000L)
//							    .hash().numOwners(3)
			               .build()
			         ,false);

			Element eCache = null;
	        try {
				eCache=getChildByName(loadDocument(config.getConfigFile().getInputStream()).getDocumentElement(),"cache");
		    	Element[] eConnections=getChildren(eCache,"connection");
		    	List cacheNames = new LinkedList();
				for(int i=0;i<eConnections.length;i++) {
	                Element eConnection=eConnections[i];
	                String name=eConnection.getAttribute("name");
	                String clazzName=eConnection.getAttribute("class");
	                if(clazzName!=null) clazzName=clazzName.trim();
	                if("railo.extension.io.cache.infinispan.InfinispanClusterCache".equals(clazzName)) {
	                	cacheNames.add(name);
            			//System.out.println("adding cache: " + name);
            			manager.defineConfiguration(name, new Configuration().fluent()
            					  .build());
	                }
	                //System.out.println(clazzName);
				}
				manager.startCaches((String[]) cacheNames.toArray(new String[0]));

			} catch (SAXException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

//			try {
//				manager = new DefaultCacheManager(properties.getProperty("infinispan.config.file").toString());
//			} catch (IOException e) {
//				e.printStackTrace();
//			}

		}

		return manager;
	}
	
	public static EmbeddedCacheManager reload(Properties properties) {
		if(manager != null) {
			manager.stop();
			manager = null;
		}
		try {
			manager = new DefaultCacheManager(properties.getProperty("infinispan.config.file").toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return manager;
		
	}

    /**
     * load XML Document from XML File
     * @param is InoutStream to read
     * @return returns the Document
     * @throws SAXException
     * @throws IOException
     */
    private static Document loadDocument(InputStream is) throws SAXException, IOException {
        DOMParser parser = new DOMParser();
	    InputSource source = new InputSource(is);
	    parser.parse(source);
	    is.close();
	    return parser.getDocument();
    }

    /**
	 * return first direct child Elements of a Element with given Name
     * @param parent
     * @param nodeName
     * @return matching children
     */
	public static Element getChildByName(Node parent, String nodeName) {
		return getChildByName(parent, nodeName, false);
	}

    public static Element getChildByName(Node parent, String nodeName,boolean insertBefore) {
    	return getChildByName(parent, nodeName, insertBefore, false);
    }
	
    public static Element getChildByName(Node parent, String nodeName,boolean insertBefore, boolean doNotCreate) {
        if(parent==null) return null;
        NodeList list=parent.getChildNodes();
        int len=list.getLength();
        
        for(int i=0;i<len;i++) {
            Node node=list.item(i);
            
            if(node.getNodeType()==Node.ELEMENT_NODE && node.getNodeName().equalsIgnoreCase(nodeName)) {
                return (Element) node;
            }
        }
        if(doNotCreate) return null;
        
        Element newEl = parent.getOwnerDocument().createElement(nodeName);
        if(insertBefore)parent.insertBefore(newEl, parent.getFirstChild());
        else parent.appendChild(newEl);
        
        

        
        return newEl;
    }
    
	/**
	 * return all direct child Elements of a Element with given Name
     * @param parent
     * @param nodeName
     * @return matching children
     */
    public static Element[] getChildren(Node parent, String nodeName) {
        if(parent==null) return new Element[0];
        NodeList list=parent.getChildNodes();
        int len=list.getLength();
        ArrayList rtn=new ArrayList();
        
        for(int i=0;i<len;i++) {
            Node node=list.item(i);
            if(node.getNodeType()==Node.ELEMENT_NODE && node.getNodeName().equalsIgnoreCase(nodeName)) {
                rtn.add(node);
            }
        }
        return (Element[]) rtn.toArray(new Element[rtn.size()]);
    }     
    
}
