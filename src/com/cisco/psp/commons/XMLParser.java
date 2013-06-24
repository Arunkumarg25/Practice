package com.cisco.psp.commons;

import java.io.File;
import java.io.InputStream;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXParseException;

import com.cisco.psp.objects.Action;
import com.cisco.psp.objects.Workflow;
import com.cisco.psp.testrunner.InvalidTestCaseException;


public class XMLParser {
	Document xml;
	String fileName;
	
	public XMLParser(){
	}
	
	public XMLParser(String fileName){
		try{
			File file = new File(fileName);
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			xml = db.parse(file);
			xml.getDocumentElement(); 
		}catch(NullPointerException e){
			Logger.log(Logger.ERROR, "file: "+fileName+" is null: "+e.getMessage());
		}catch(SAXParseException e){
			Logger.log(Logger.ERROR, "Parse Exception on line: "+e.getLineNumber()+" "+e.getMessage());
		}catch(ParserConfigurationException e){
			Logger.log(Logger.ERROR, e.toString());
		}catch(Exception e){
			Logger.log(Logger.ERROR, e.toString());
		}
	}

	public XMLParser(InputStream xmlInput){
		try{
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			xml = db.parse(xmlInput);
		}catch(Exception e){
			Logger.log(Logger.ERROR, e.toString());
		}
	}
	
	public Workflow getWrokflow() throws InvalidTestCaseException{
		Workflow wf = new Workflow();
		if(xml==null){
			Logger.log(Logger.ERROR, "XML file is null");
			throw new InvalidTestCaseException();
		}
		
		Node workflowNode = xml.getElementsByTagName("workflow").item(0);
		NamedNodeMap workflowAttributes = workflowNode.getAttributes();
		//*****************************************************************
		//READ IN THE WORKFLOW ATTRIBUTES
		//*****************************************************************
		for(int j = 0; j < workflowAttributes.getLength(); j++){
			String attributeName = workflowAttributes.item(j).getNodeName().toLowerCase();
			String attributeValue = workflowAttributes.item(j).getNodeValue().trim();
			if(attributeName.equals("wfid"))
				wf.setWfId(attributeValue);
			else if(attributeName.equals("stoptype")){
				if(attributeValue.equals("continue"))
					wf.setStoptype(Workflow.CONTINUE);
				else if(attributeValue.equals("abort"))
					wf.setStoptype(Workflow.ABORT);
				else{
					Logger.log(Logger.ERROR, "Workflow stoptype: "+attributeValue+" not supported!");
					throw new InvalidTestCaseException();
				}
			}else if(attributeName.equals("profile"))
				wf.setProfile(attributeValue);
			else if(attributeName.equals("driver")){
				if(!attributeValue.equals("selenium")){
					Logger.log(Logger.ERROR, "I only support driver type selenium, I don't know why this workflow is assigned to me, ask the scheduler");
				}
			}else if(attributeName.equals("browser")){
				wf.setBrowser(attributeValue);
			}
		}
		
		//*******************************************************************
		//LIST OF ACTION NODES
		//*******************************************************************
		NodeList actionList = workflowNode.getChildNodes();
		for(int j =0; j < actionList.getLength(); j++){
			Action action = new Action();
			String level2tagName = actionList.item(j).getNodeName().toLowerCase();
			if(level2tagName.equals("action")){
				Node actionNode = actionList.item(j);
				NamedNodeMap actionNodeMap = actionNode.getAttributes();
				for(int k = 0; k < actionNodeMap.getLength(); k++){
					String nodeAttr = actionNodeMap.item(k).getNodeName().toLowerCase();
					if(nodeAttr.equals("acid"))
						action.setAcID(actionNodeMap.item(k).getNodeValue());
					else if(nodeAttr.equals("stoptype"))
						action.setStoptype(actionNodeMap.item(k).getNodeValue());
					else if(nodeAttr.equalsIgnoreCase("loop"))
						action.setLoop(actionNodeMap.item(k).getNodeValue());
					else if(nodeAttr.equalsIgnoreCase("iterations"))
						action.setIterations(actionNodeMap.item(k).getNodeValue());
					else if(nodeAttr.equalsIgnoreCase("sleep"))
						action.setSleep(actionNodeMap.item(k).getNodeValue());
					else if(nodeAttr.equalsIgnoreCase("break"))
						action.setInterupt(actionNodeMap.item(k).getNodeValue());					
					else if(nodeAttr.equals("timeout")){
						String timeoutValue = actionNodeMap.item(k).getNodeValue();
						if(timeoutValue.equals("")){
							action.setTimeout(-1);
						}else{
							try{
								action.setTimeout(Integer.parseInt(timeoutValue));
							}catch(NumberFormatException e){
								Logger.log(Logger.ERROR, "Timeout can only be an integer");
								throw new InvalidTestCaseException();
							}
						}
					}
				}
			
			
				NodeList subActionNodes = actionNode.getChildNodes();
				HashMap<String, String> paramsMap = new HashMap<String, String>();
				for(int k = 0; k < subActionNodes.getLength(); k++){
					String nodeName = subActionNodes.item(k).getNodeName().toLowerCase();
					if(!nodeName.equals("actionname") && !nodeName.equals("actionparam") && !nodeName.equals("#text") && !nodeName.equals("#comment")){
						Logger.log(Logger.ERROR, "Malformat xml, tag name: "+nodeName+" is not supported, check your workflow xml");
						throw new InvalidTestCaseException();
					}
					if(nodeName.equals("actionname")){
						action.setName(subActionNodes.item(k).getTextContent().trim());
					}else if(subActionNodes.item(k).getNodeName().toLowerCase().equals("actionparam")){
						if(!subActionNodes.item(k).getAttributes().item(0).getNodeName().toLowerCase().equals("paramtype")){
							Logger.log(Logger.ERROR, "actionparam attribute: "+subActionNodes.item(k).getAttributes().item(0).getNodeName()+" is not supported!");
							throw new InvalidTestCaseException();
						}
						if(!subActionNodes.item(k).getAttributes().item(1).getNodeName().toLowerCase().equals("paramvalue")){
							Logger.log(Logger.ERROR, "actionparam attribute: "+subActionNodes.item(k).getAttributes().item(1).getNodeName()+" is not supported!");
							throw new InvalidTestCaseException();
						}
						String paramType = subActionNodes.item(k).getAttributes().item(0).getNodeValue().trim();
						String paramValue = subActionNodes.item(k).getAttributes().item(1).getNodeValue().trim();
						paramsMap.put(paramType.toLowerCase(), paramValue);
					}
				}
				action.setParams(paramsMap);
				Logger.log(Logger.DEBUG, action.toString());
				wf.addAction(action);
			}
		}

		return wf;
	}
	
	
	public void setFileName(String fileName){
		this.fileName = fileName;
	}
	
	public String getFileName(){
		return fileName;
	}
	
}
