
public class ReverseString {
	
	public static String reverse(String s){
		String results ="";
		char[] array = s.toCharArray();
		for(int i = array.length -1; i >= 0; i--){
			results += array[i];
		}
		System.out.println(results);
		return results;
	}
	

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String s = "abcdefg";
		reverse(s);

	}

}
