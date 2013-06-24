#! /usr/bin/env python
import sys
import os

args = str(sys.argv[1])
os.system('java -jar ./run/PSPSeleniumTest.jar -s dc9b17c5ff4bbf0865aa909c490005ea -i 10.88.128.3 -p 5984 -db selenium -xml '+args)
