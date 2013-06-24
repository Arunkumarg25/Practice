package com.cisco.psp.commons;

public class HTMLSourceParser {	
	/**
	 * Given a HTML string: source, take out from that source anything in the element: elementToTakeOut
	 * For example: <body> blah blah <script>some java script</script> some more body blah <script>some more script</script> blah one more </body>
	 * If given the above HTML source, and script as the elementToTakeOut, this will reutrn <body>blah blah some more body blah blah moe more</body>
	 * @param source the HTML source
	 * @param elementToTakeOut the tag of the element to take out
	 * @return The HTML source with the element taken out
	 */
	public static String takeOutElements(String source, String elementToTakeOut){
		String s = "";
		String[] tempArray = source.split("<"+elementToTakeOut);
		s+=tempArray[0];
		for(int i=1; i<tempArray.length; i++){
			//int endOfElementTag = tempArray[i].indexOf('>');
			int startOfEndElementTag = tempArray[i].indexOf("</"+elementToTakeOut+">");
			String toAddToS = tempArray[i].substring(startOfEndElementTag+elementToTakeOut.length()+3);
			s += toAddToS;
		}
		return s;
	}
}
