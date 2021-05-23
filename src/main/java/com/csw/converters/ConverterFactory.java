package com.csw.converters;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import java.util.Set;

/**
 * Factory class for creating instances of {@link XMLJSONConverterI}.
 */
public final class ConverterFactory {
    public static String jsonFilePath;
    public static String xmlFIlePath;

    /**
     * You should implement this method having it return your version of
     * {@link com.csw.converters.XMLJSONConverterI}.
     *
     * @return {@link com.csw.converters.XMLJSONConverterI} implementation you created.
     */
    public static final XMLJSONConverterI createXMLJSONConverter() {

        //throw new UnsupportedOperationException("Please implement me!");
        return new XMLJSONConverterI(){

            @Override
            public void convertJSONtoXML(File jsonFile, File xmlFile) {

                //Method to read JSON File
                Object obj = readJsonFile(jsonFile);

                //Method to construct root element
                Document document  = constructRootXmlElement(obj);

                //Method to Construct XML
                constructXml( obj, document, null);

                //Method to Write XML
                writeXmlFile(xmlFile, document);
            }
        };
    }

    //public
    public static void main(String[] args) {

         Scanner scanner = new Scanner(System.in);
         String jsonFilePath = scanner.next();
         String xmlFIlePath = scanner.next();

       //jsonFilePath = "D:\\jonej\\Documents\\csw-assignment\\test-data\\1.json";
       //xmlFIlePath = "D:\\jonej\\Documents\\csw-assignment\\output\\1.xml";

        File jsonFile = new File(jsonFilePath);
        File xmlFile = new File(xmlFIlePath);

        XMLJSONConverterI converter = ConverterFactory.createXMLJSONConverter();

        try{
            converter.convertJSONtoXML(jsonFile, xmlFile);
        }catch(Exception exp){
            exp.printStackTrace();
        }


    }

    public static Document constructRootXmlElement(Object obj){
        Document document = null;

        try {

            DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentFactory.newDocumentBuilder();
            document = documentBuilder.newDocument();
            Element rootElement;

            if(obj instanceof JSONObject){
                rootElement = document.createElement("object");

            }else if(obj instanceof JSONArray){
                rootElement = document.createElement("array");
            }else{
                System.out.println("The First Json Element can be of Array or an Object");
                throw new Exception("Cannot Convert!");
            }
            document.appendChild(rootElement);
        } catch (Exception exp) {
            exp.printStackTrace();
        }
        return document;
    }

    private static Element buildXmlElement(String key, Document document, String tagName) {
        Element objectElement = document.createElement(tagName);
        if(key != null){
            Attr attr = document.createAttribute("name");
            attr.setValue(key);
            objectElement.setAttributeNode(attr);
        }
        return objectElement;
    }

    private static Element constructXmlFOrArray(Object obj, Document document) {
        Element element = null;
        if(obj instanceof JSONArray){
            element = constructXmlEleForArray(obj, document,"array");
        } else if(obj instanceof Long || obj instanceof Integer) {
            element = constructXmlEleForArray(obj, document,"number");
        }else  if(obj instanceof String) {
            element = constructXmlEleForArray(obj, document,"string");
        }else  if(obj == null ) {
            element = constructXmlEleForArray(obj, document,"null");
        }else  if(obj  instanceof Boolean) {
            element = constructXmlEleForArray(obj, document,"boolean");
        }else  if(obj  instanceof Object) {
            element = constructXmlEleForArray(obj, document,"object");
        }
        return element;
    }

    public static Element constructXmlEleForArray(Object obj, Document document, String tagName){
        Element ele = document.createElement(tagName);
        ele.appendChild(document.createTextNode(obj.toString()));
        return ele;
    }


    public static Element buildXML(Object k, JSONObject jsonObject, Document document){

        Element element = null;
        if( jsonObject.get(k)  instanceof Long || jsonObject.get(k)  instanceof Integer) {
            element = createXmlObjElement(k, jsonObject, document,"number");
        }else  if(jsonObject.get(k)  instanceof String) {
            element = createXmlObjElement(k, jsonObject, document,"string");
        }else  if(jsonObject.get(k) == null ) {
            element = createXmlObjElement(k, jsonObject, document,"null");
        }else  if(jsonObject.get(k)  instanceof Boolean) {
            element = createXmlObjElement(k, jsonObject, document,"boolean");
        }else  if(jsonObject.get(k)  instanceof Object) {
            element = createXmlObjElement(k, jsonObject, document,"object");
        }
        return element;
    }

    public static Element createXmlObjElement(Object key, JSONObject jsonObject, Document document, String tagName){
        //System.out.println("Key ::"+ key);
        //System.out.println("Value ::"+ jsonObject.get(key));
        //System.out.println("ValueType ::"+tagName);

        Element ele = document.createElement(tagName);
        Attr attr = document.createAttribute("name");
        attr.setValue(key.toString());
        ele.setAttributeNode(attr);
        if(jsonObject.get(key)!=null){
            ele.appendChild(document.createTextNode(jsonObject.get(key).toString()));
        }
        return ele;
    }

    public static void constructXml(Object obj, Document document, Element parentEle){

        try{
            JSONObject jsonObject;
            Element element;
            if(obj instanceof JSONObject){
                jsonObject = (JSONObject) obj;
                Set<?> keySet =jsonObject.keySet();
                for (Object o : keySet) {
                    String key = o.toString();
                    //System.out.println(key);
                    if (jsonObject.get(key) instanceof JSONObject) {
                        element = buildXmlElement(key,document, "object");
                        if(parentEle != null){
                            parentEle.appendChild(element);
                        }else{
                            document.getDocumentElement().appendChild(element);
                        }
                        constructXml(jsonObject.get(key), document, element);
                    }else if(jsonObject.get(key) instanceof JSONArray){
                        element = buildXmlElement(key,document, "array");
                        if(parentEle != null){
                            parentEle.appendChild(element);
                        }else {
                            document.getDocumentElement().appendChild(element);
                        }
                        constructXml(jsonObject.get(key), document, element);
                    }else{
                        element = buildXML(o, jsonObject, document);
                        if(parentEle != null){
                            parentEle.appendChild(element);
                        }else{
                            document.getDocumentElement().appendChild(element);
                        }
                    }
                }
            }else if(obj instanceof JSONArray){
                JSONArray jsonArray = (JSONArray) obj;
                //System.out.println(jsonArray);
                for (Object ob : jsonArray) {
                    //System.out.println("Array Element ::"+ ob);
                    if(ob instanceof JSONObject){
                        element = buildXmlElement(null,document, "object");
                        if(parentEle != null){
                            parentEle.appendChild(element);
                        }else {
                            document.getDocumentElement().appendChild(element);
                        }
                        constructXml(ob,document, element);
                    }else if (ob instanceof JSONArray){
                        element = buildXmlElement(null,document, "array");
                        if(parentEle != null){
                            parentEle.appendChild(element);
                        }else {
                            document.getDocumentElement().appendChild(element);
                        }
                        constructXml(ob,document, element);
                    }else {
                        element = constructXmlFOrArray(ob,document);
                        if(parentEle != null){
                            parentEle.appendChild(element);
                        }else {
                            document.getDocumentElement().appendChild(element);
                        }
                    }
                }
            }
        }catch(Exception exp){
            System.out.println("Stack Trace:: "+ exp);
        }

    }



    public static Object readJsonFile(File jsonFile){

        Object obj = null;
        Document document = null;
        try{
            //JSON parser object to parse read file
            JSONParser jsonParser = new JSONParser();

            FileReader reader = new FileReader(jsonFilePath);

            //Read JSON file
            obj = jsonParser.parse(reader);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            System.out.println("Not a valid Json");
            System.out.println("Stack Trace:");
            e.printStackTrace();
        }
        return obj;
    }

    public static void writeXmlFile(File xmlFile, Document document){

        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            DOMSource domSource = new DOMSource(document);
            StreamResult streamResult = new StreamResult(xmlFile);
           // StreamResult streamResult1 = new StreamResult(System.out);

            transformer.transform(domSource, streamResult);
            //transformer.transform(domSource, streamResult1);

            //System.out.println("Done creating XML File");
        } catch (TransformerException e) {
            e.printStackTrace();
        }

    }
}
