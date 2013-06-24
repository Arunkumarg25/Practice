import sys
import os

arg = sys.argv[1]
partitioned_count = arg.partition("-")
start_count = int(partitioned_count[0])
end_count = int(partitioned_count[2])
total_count = end_count-start_count
while total_count > -1:
	os.system('java -jar PSPSeleniumTest.jar -s 343bc96a95d63c6bc98b04cf8adaed10 -i 10.88.128.3 -p 5984 -db selenium -xml ../data/loadTestXML/addRules_'+str(end_count-total_count)+'.xml')
	total_count -= 1
