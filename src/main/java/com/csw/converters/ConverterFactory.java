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
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
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
       // Todo: Implement this method please.

        //throw new UnsupportedOperationException("Please implement me!");
        return new XMLJSONConverterI(){

            @Override
            public void convertJSONtoXML(File jsonFile, File xmlFile) throws IOException {
                System.out.println("Inside convertJSONtoXML !!! line no 34");

                //Method to read JSON File
                Object obj = readJsonFile(jsonFile);

                //Method to Construct XML
                Document document = constructXml(obj);

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
            Element rootElement = null;

            if(obj instanceof JSONObject){
                rootElement = document.createElement("object");

            }else if(obj instanceof JSONArray){
                rootElement = document.createElement("object");
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

    public static Document constructXml(Object obj){

        Document document  = constructRootXmlElement(obj);
        try{
            JSONObject jsonObject;
            if(obj instanceof JSONObject){
                jsonObject = (JSONObject) obj;
                Set<?> s =jsonObject.keySet();
                Iterator<?> i = s.iterator();
                do{
                    String k = i.next().toString();
                    System.out.println(k);
                    buildXML(k, jsonObject, document);
                }while(i.hasNext());
            }else if(obj instanceof JSONArray){
                JSONArray jsonArray = (JSONArray) obj;
                System.out.println(jsonArray);
                for (Object o : jsonArray) {
                    System.out.println("Array Element ::"+ o);
                    document = constructXmlFOrArray(o, document);
                }
            }
        }catch(Exception exp){
            System.out.println("Stack Trace:: "+ exp);
        }

        return document;
    }

    private static Document constructXmlFOrArray(Object obj, Document document) {
        if(obj instanceof JSONArray){
            constructXmlElementForArray(obj, document,"array");
        } else if(obj instanceof Long || obj instanceof Integer) {
            constructXmlElementForArray(obj, document,"number");
        }else  if(obj instanceof String) {
            constructXmlElementForArray(obj, document,"string");
        }else  if(obj == null ) {
            constructXmlElementForArray(obj, document,"null");
        }else  if(obj  instanceof Boolean) {
            constructXmlElementForArray(obj, document,"boolean");
        }else  if(obj  instanceof Object) {
            constructXmlElementForArray(obj, document,"object");
        }
        return null;
    }

    public static void constructXmlElementForArray(Object obj, Document document, String tagName){
        Element ele = document.createElement(tagName);
        ele.appendChild(document.createTextNode(obj.toString()));
        document.getDocumentElement().appendChild(ele);
    }

    public static void constructXmlElement(Object key, JSONObject jsonObject, Document document, String tagName){
        System.out.println("Key ::"+ key);
        System.out.println("Value ::"+ jsonObject.get(key));
        System.out.println("ValueType ::"+tagName);

        Element ele = document.createElement(tagName);
        Attr attr = document.createAttribute("name");
        attr.setValue(key.toString());
        ele.setAttributeNode(attr);
        ele.appendChild(document.createTextNode(jsonObject.get(key).toString()));
        document.getDocumentElement().appendChild(ele);
    }


    public static void buildXML(Object k, JSONObject jsonObject, Document document){


       if(jsonObject.get(k) instanceof JSONArray){
           constructXmlElement(k, jsonObject, document,"array");
        } else if( jsonObject.get(k)  instanceof Long || jsonObject.get(k)  instanceof Integer) {
           constructXmlElement(k, jsonObject, document,"number");
        }else  if(jsonObject.get(k)  instanceof String) {
           constructXmlElement(k, jsonObject, document,"string");
        }else  if(jsonObject.get(k) == null ) {
           constructXmlElement(k, jsonObject, document,"null");
        }else  if(jsonObject.get(k)  instanceof Boolean) {
           constructXmlElement(k, jsonObject, document,"boolean");
        }else  if(jsonObject.get(k)  instanceof Object) {
           constructXmlElement(k, jsonObject, document,"object");
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
            StreamResult streamResult1 = new StreamResult(System.out);

            transformer.transform(domSource, streamResult);
            transformer.transform(domSource, streamResult1);

            System.out.println("Done creating XML File");
        } catch (TransformerException e) {
            e.printStackTrace();
        }

    }
}
