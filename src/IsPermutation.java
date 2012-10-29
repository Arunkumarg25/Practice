
public class IsPermutation {
	
	public static boolean isPermutation1(String a, String b){
		if(a.length() != b.length())
			return false;
		char[] arrayA = a.toCharArray();
		char[] arrayB = b.toCharArray();
		java.util.Arrays.sort(arrayA);
		java.util.Arrays.sort(arrayB);
		for(int i = arrayA.length-1; i >=0; i--){
			if(arrayA[i] != arrayB[i])
				return false;
		}
		return true;
	}
	
	public static boolean isPermutation2(String a, String b){
		if(a.length() != b.length())
			return false;
		char[] arrayA = a.toCharArray();
		char[] arrayB = b.toCharArray();
		int[] temp = new int[256];
		for(int i =0; i < arrayA.length; i++){
			temp[arrayA[i]] ++;
		}
		for(int j = 0; j< arrayA.length; j++){
			temp[arrayB[j]] --;
		}
		for(int k =0; k < 256; k++){
			if(temp[k] != 0)
				return false;
		}
		return true;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String a = "aab1ccdefg";
		String b = "cc1baadefg";
		String c = "aabbccddee";
		System.out.println(isPermutation1(a, b));
		System.out.println(isPermutation1(a, c));
		System.out.println(isPermutation2(a, b));
		System.out.println(isPermutation2(a, c));

	}

}
