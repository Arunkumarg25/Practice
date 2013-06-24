#!/usr/bin/python
import sys
import shutil as shutil
from elementtree import ElementTree as et

def createXML(count, name):
	partitioned_count = count.partition("-")
	start_count = int(partitioned_count[0])
	end_count = int(partitioned_count[2])
	total_count = end_count - start_count
	src = name
	partitioned_name = src.rpartition(".")
	while total_count > -1:
		shutil.copyfile(src, partitioned_name[0]+"_"+str(end_count-total_count)+partitioned_name[1]+partitioned_name[2])
		total_count -= 1

def modifyXML():
	pass
	
def modifyForLoad(number_to_modify, base_name):
	parsed_base_name = base_name.rpartition(".")
	partitioned_count = number_to_modify.partition("-")
	start_count = int(partitioned_count[0])
	end_count = int(partitioned_count[2])
	total_count = end_count - start_count
	while total_count >= 0:
		file_to_modify = parsed_base_name[0]+"_"+str(end_count-total_count)+parsed_base_name[1]+parsed_base_name[2]
		tree = et.parse(file_to_modify)
		root = tree.getroot()
		wfID = root.get("wfID")
		modified_wfID = wfID+str(end_count-total_count)
		root.set("wfID", modified_wfID)
		actions = root.getchildren()
		for action in actions:
			action_name = action.find("actionName").text
			if action_name == "addCustomer" or action_name == "editAccount":
				action_params = action.findall("actionParam")
				for action_param in action_params:
					param_type = action_param.get("paramType")
					if param_type == "customerName*":
						param_value = action_param.get("paramValue")
						param_value = param_value.replace("var"+str(start_count), "var"+str(end_count-total_count))
						action_param.set("paramValue", param_value)
					if param_type == "emailAddress*":
						param_value = action_param.get("paramValue")
						param_value = param_value.replace("var"+str(start_count), "var"+str(end_count-total_count))
						action_param.set("paramValue", param_value)
					if param_type == "email*":
						param_value = action_param.get("paramValue")
						param_value = param_value.replace("var"+str(start_count), "var"+str(end_count-total_count))
						action_param.set("paramValue", param_value)
			elif action_name == "login":
				action_params = action.findall("actionParam")
				for action_param in action_params:
					param_type = action_param.get("paramType")
					if param_type=="username":
						param_value = action_param.get("paramValue")
						param_value = param_value.replace("var"+str(start_count), "var"+str(end_count-total_count))
						action_param.set("paramValue", param_value)
		tree.write(file_to_modify)
		total_count -= 1

def test():
	root = et.Element("html")
	head = et.SubElement(root, "head")
	title = et.SubElement(head, "title")
	title.text = "Page Title"
	body = et.SubElement(root, "body")
	body.set("testParam", "test")
	body.text = "testing"

	tree = et.ElementTree(root)
	tree.write("test.xml")
	
def parse_args(args):
	option = args[1]
	if option=="-c":
		count = args[2]
		name = args[3]
		createXML(count, name)
	elif option=="-m":
		modifyXML()
	elif option=="-s":
		number_to_modify = args[2]
		base_name = args[3]
		modifyForLoad(number_to_modify, base_name)
	else:
		 print_help() 
		
def print_help():
	print "--Arguments not supported--"
	print "Usage: <option> <value>"
	print "  <option> includes: "		
	print "      -c     Copy XML documents based on <value>"
	print "                     where <value> = <count> <name>, <count> = number of file to create (can be a range: 100-200), <name> = the name of the file to copy"
	print "      -m     Modify XML document based on <value>"
	print "                     where <value> could be multiple instances (<value> <value> ... <value>)"
	print "                     each individual <value> = <xpath>"
	
if __name__=='__main__':
	if len(sys.argv) <= 1:
		print_help()
	else:
		parse_args(sys.argv)
