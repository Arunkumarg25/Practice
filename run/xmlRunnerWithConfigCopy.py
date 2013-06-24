#! /usr/bin/env python
import sys
import os
import shutil
import fnmatch
import re


def grem(path, pattern):
	for each in os.listdir(path):
		if pattern.search(each):
			name = os.path.join(path, each)
			if (name!=os.path.join(path, ".svn") and name.find(".xml") != -1):
				try: os.remove(name)
				except:
					grem(name, pattern)
					os.rmdir(name)

dir1 = "../config/"
dir2 = "./data/selenium_config/"
dirList = os.listdir(dir1)

print "removing files..."
regex = re.compile('')
grem(dir2, regex)
print "files moved:"
for file in dirList:
	if fnmatch.fnmatch(file, '*.xml'):
		print(file)
		shutil.copyfile(dir1+file, dir2+'/'+file)	
args = str(sys.argv[1])
os.system('java -jar ./run/PSPSeleniumTest.jar -c ./data/selenium_config -s 2873469d47aa31fa049c28028d554afe -i 10.88.128.3 -p 5984 -db selenium -xml '+args)


