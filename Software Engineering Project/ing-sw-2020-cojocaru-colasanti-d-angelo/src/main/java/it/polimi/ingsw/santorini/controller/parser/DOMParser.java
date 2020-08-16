package it.polimi.ingsw.santorini.controller.parser;

import it.polimi.ingsw.santorini.model.gameoperations.custom.CustomGameOperation;
import it.polimi.ingsw.santorini.model.gameoperations.custom.GameOperationParameters;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.traversal.DocumentTraversal;
import org.w3c.dom.traversal.NodeFilter;
import org.w3c.dom.traversal.NodeIterator;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Supplier;

/**
 * Brief Parser making use of a DOM
 */
public class DOMParser extends Parser{

    /**
     * Brief landmark node names used for parsing
     */
    private static final String godNode = "god", moveNode = "move", buildNode = "build", idNode = "id", imageNode = "imagePath";

    /**
     * {@inheritDoc}
     */
    @Override
    public HashMap<Integer, HashMap<String, String>> parseFileForMeta(String filePath){
        HashMap<Integer, HashMap<String, String>> godMeta = new HashMap<>();
        HashMap<String, String> meta = null;
        int god = 0;
        boolean save = false;
        NodeIterator it = extractNodes(filePath);
        if(it != null){
            Node node = it.nextNode();
            while (node != null) {
                String name = node.getNodeName();
                if (name.equals(idNode)) {
                    save = true;
                    meta = new HashMap<>();
                }
                if (save) meta.put(name, node.getTextContent());
                if (name.equals(imageNode)) {
                    save = false;
                    godMeta.put(god, meta);
                    god++;
                }
                node = it.nextNode();
            }
            return godMeta;
        }
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T extends CustomGameOperation<?>>
    HashMap<Integer, List<T>> parseFileForEffects(String filePath, String effectType, Supplier<T> effectSupplier) {
        HashMap<Integer, List<T>> godEffects = new HashMap<>();
        List<T> effects = null;
        int god = 0, effectIndex = 0;
        boolean save = false;
        NodeIterator it = extractNodes(filePath);
        if(it != null){
            Node node;
            do {
                node = it.nextNode();
                if (node == null) {
                    godEffects.put(god, effects);
                } else {
                    String name = node.getNodeName();
                    if (!save && (name.equals(moveNode) || name.equals(buildNode))) {
                        save = true;
                        effectIndex = 0;
                        effects = new ArrayList<>();
                    }
                    if (save) {
                        if (name.equals(effectType)) {
                            T effect = effectSupplier.get();
                            loadEffect(node, effect, effectIndex);
                            effects.add(effect);
                        }
                        effectIndex++;
                    }
                    if (name.equals(godNode) && effects != null) {
                        save = false;
                        godEffects.put(god, effects);
                        god++;
                    }
                }
            }
            while (node != null);
            return godEffects;
        }
        return null;
    }

    /**
     * Brief Great part of the code in this function comes from http://zetcode.com/java/dom/
     * @param filePath is the path of the file to parse
     * @return an iterator over the file's nodes or null if something went wrong
     */
    private NodeIterator extractNodes(String filePath){
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder loader;

        try {
            loader = factory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            return null;
        }

        Document document;

        try {
            document = loader.parse(getClass().getResource(filePath).toURI().toString());
        } catch (SAXException | IOException | URISyntaxException e) {
            return null;
        }

        return ((DocumentTraversal) document)
                .createNodeIterator(document.getDocumentElement(), NodeFilter.SHOW_ELEMENT, null, true);
    }

    /**
     * Brief Extracts a Boolean attribute from a node
     * @param node the node
     * @param attribute the attribute name
     * @return Boolean the value of the attribute
     */
    private Boolean getBooleanAttribute(Node node, String attribute) {
        return Boolean.valueOf(node.getAttributes().getNamedItem(attribute).getTextContent());
    }

    /**
     * Brief Extracts attributes from a xml effect node and puts it in a destination effect
     * @param node the node storing an effect
     * @param destination the destination effect
     * @param index the index representing an id of the effect (with respect to the order found parsing the file)
     * @param <T> the type of the effect
     */
    private <T extends CustomGameOperation<?>> void loadEffect(Node node, T destination, int index) {
        GameOperationParameters effectParameters = new GameOperationParameters();
        effectParameters.setOptional(getBooleanAttribute(node, "optional"));
        effectParameters.setHasDefaultRules(getBooleanAttribute(node, "default"));
        effectParameters.setRequiresWorkerSelection(getBooleanAttribute(node, "workerSelection"));

        destination.setParameters(effectParameters);
        destination.setOperationName(node.getTextContent());
        destination.setIndex(index);
    }
}