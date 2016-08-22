/**
 * Created by 1224A on 8/16/2016.
 */
class code {
    public static int getCount(String str) {
        int vowelsCount = 0;
        // your code here
        String vowel = "aiueo";
        for (int a=0;a < str.length();a++) {
            if (vowel.contains(String.valueOf(str.toLowerCase().charAt(a)))) {
                vowelsCount++;
            }
        }

        return vowelsCount;
    }
}
