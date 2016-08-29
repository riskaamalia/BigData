package kata;

/**
 * Created by 1224A on 8/16/2016.
 */
public class code {

    /*public static String makeReadable(int seconds) {
        return String.format("%02d:%02d:%02d", seconds / 3600, (seconds / 60) % 60, seconds % 60);
    }*/
    public static boolean isValid(char[] walk) {
        int n=0,s=0,w=0,e=0;
        for (char a:walk) {
            if (String.valueOf(a).equals("n"))
                n++;
            if (String.valueOf(a).equals("s"))
                s++;
            if (String.valueOf(a).equals("w"))
                w++;
            if (String.valueOf(a).equals("e"))
                e++;
        }

        if (n == s && w == e)
            return true;
        else
            return false;
    }
}

