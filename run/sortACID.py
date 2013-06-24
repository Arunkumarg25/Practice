#!/usr/bin/python
import os
import argparse
import logging
import datetime
import time
import codecs
import xml.dom.minidom

logTime = datetime.datetime.now()
logTime = logTime.strftime("%Y-%m-%d-%H-%M-%S")

parser = argparse.ArgumentParser(description='Generically generate test cases based on templates and configuration file')
parser.add_argument('-t', dest='template', default='/tools/ws/end2end_template', help='Template file or folder path. The default setting is /tools/ws/end2end_template')
args = parser.parse_args();

replacedString = ''
if(os.path.exists('/tools/ws/log/') != True):
    os.makedirs('/tools/ws/log/')
logging.basicConfig(filename='/tools/ws/log/' + os.sep + logTime + '.log', filemode = 'w', level=logging.INFO)
console = logging.StreamHandler()
console.setLevel(logging.INFO)
formatter = logging.Formatter('%(name)-s: %(levelname)-s %(message)s')
console.setFormatter(formatter)
logging.getLogger('').addHandler(console)

#def Indent(dom, node, indent = 0):
#           # Copy child list because it will change soon
#           children = node.childNodes[:]
#           # Main node doesn't need to be indented
#           if indent:
#               text = dom.createTextNode('\t' * indent)
#               node.parentNode.insertBefore(text, node)
#           if children:
#               # Append newline after last child, except for text nodes
#              if children[-1].nodeType == node.ELEMENT_NODE:
#                  text = dom.createTextNode('\t' * indent)
#                  node.appendChild(text)
#              # Indent children which are elements
#              for n in children:
#                  if n.nodeType == node.ELEMENT_NODE:
#                      Indent(dom, n, indent + 1)

def sortACID(templateFile):          
    dom = xml.dom.minidom.parse(templateFile)
    root = dom.documentElement
    workflows = root.getElementsByTagName('workflow')
    for workflow in workflows:
        i = 1
        actions = workflow.getElementsByTagName('action')       
        for action in actions:
            action.setAttribute('acid', str(i))
            i = i+1         
    domcopy = dom.cloneNode(True)
    #Indent(domcopy, domcopy.documentElement)
    f = file(templateFile, 'wb')
    writer = codecs.lookup('utf-8')[3](f)
    domcopy.writexml(writer, encoding='utf-8')
    writer.close();
    domcopy.unlink()     

def main():          
    logging.info('***You are using ' + args.template + ' as the source of templates. ***')    
    time.sleep(3)    
    if os.path.isdir(args.template):     
        files = os.listdir(args.template)        
        for f in files:
            logging.info('======Converting file: ' + f + ' ======'  )           
            sortACID(args.template + os.sep + f)            
        logging.info('======Convert is done.======\n')        
    elif os.path.isfile(args.template):
        logging.info('======Converting file: ' + args.template.split('\\')[-1] + ' ======'  )     
        sortACID(args.template)       
        logging.info('======Convert is done.======\n')         
    else:
        logging.error('The template argument you input is not a valid file or directory. Please re-check it.') 
        
main()