#!/usr/bin/python
import os
import argparse
import ConfigParser
import logging
import datetime
import time
import codecs

logTime = datetime.datetime.now()
logTime = logTime.strftime("%Y-%m-%d-%H-%M-%S")

parser = argparse.ArgumentParser(description='Generically generate test cases based on templates and configuration file')
parser.add_argument('-t', dest='template', default='/tools/ws/end2end_template', help='Template file or folder path. The default setting is /tools/ws/end2end_template')
parser.add_argument('-d', dest='destination', default='.' + os.sep + 'generated', help='Directory path to store new test cases. The default setting is ./generated')
parser.add_argument('-c', dest='configFile', default='/tools/data/template_config/config.ini', help='Configure file path. The default setting is /tools/data/template_config/config.ini')
parser.add_argument('-l', dest='location', help='The location link. The location link. There is no default setting here. Script will try to get it from command line first. If failed, it will read it from configure file.')
args = parser.parse_args();

config = ConfigParser.ConfigParser()
config.read(args.configFile)
mapping = config.items('mapping')
replacedString = ''
location = ''
if(os.path.exists('/tools/ws/log/') != True):
    os.makedirs('/tools/ws/log/')
logging.basicConfig(filename='/tools/ws/log/' + os.sep + logTime + '.log', filemode = 'w', level=logging.INFO)
console = logging.StreamHandler()
console.setLevel(logging.INFO)
formatter = logging.Formatter('%(name)-s: %(levelname)-s %(message)s')
console.setFormatter(formatter)
logging.getLogger('').addHandler(console)

def searchReplace(templateFile):  
    global location      
    with open(templateFile) as f:
        replacedString = f.read()  
    if(args.location != None and args.location != ''):
        replacedString = replacedString.replace('${location}', args.location)
        location = args.location                 
    for key, value in mapping:       
        replacedString = replacedString.replace(key, value)            
        if(key == '${location}' and location == ''):
            location = value
    return replacedString

def main():          
    logging.info('***You are using ' + args.template + ' as the source of templates. ***') 
    logging.info('***The configure file is ' + args.configFile + ' ***') 
    logging.info('***All generated test cases will be stored at ' + args.destination + ' ***\n') 
    time.sleep(3)    
    if os.path.isdir(args.template):     
        files = os.listdir(args.template)
        if(os.path.exists(args.destination) != True):
            logging.info('The destination directory is not existed. I am creating it now.') 
            os.makedirs(args.destination)
        for f in files:
            logging.info('======Converting file: ' + f + ' ======'  )           
            replacedString = searchReplace(args.template + os.sep + f)                       
            with codecs.open(args.destination + os.sep + f.split('.')[0] + '_' + location + '.xml', 'w') as dfile:
                dfile.write(codecs.BOM_UTF8)
                dfile.write(replacedString)
        logging.info('======Convert is done.======\n') 
        logging.info('***You used ' + args.template + ' as the source of templates. ***') 
        logging.info('***The configure file is ' + args.configFile + ' ***') 
        logging.info('***All generated test cases are stored at ' + args.destination + ' ***\n') 
    elif os.path.isfile(args.template):
        logging.info('======Converting file: ' + args.template.split('\\')[-1] + ' ======'  )     
        replacedString = searchReplace(args.template)         
        fileName = args.template.split('\\')[-1]
        if(os.path.exists(args.destination) != True):
            logging.info('The destination directory is not existed. I am creating it now.') 
            os.makedirs(args.destination)
        with codecs.open(args.destination + os.sep + fileName.split('.')[0] + '_' + location + '.xml', 'w') as f:
            f.write(codecs.BOM_UTF8)
            f.write(replacedString)
        logging.info('======Convert is done.======\n') 
        logging.info('***You used ' + args.template + ' as the source of templates. ***') 
        logging.info('***The configure file is ' + args.configFile + ' ***') 
        logging.info('***All generated test cases are stored at ' + args.destination + ' ***\n') 
    else:
        logging.error('The template argument you input is not a valid file or directory. Please re-check it.') 
        
main()
    
    
        
