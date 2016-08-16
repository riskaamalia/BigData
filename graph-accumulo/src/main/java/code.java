/**
 * Created by 1224A on 8/16/2016.
 */
class code {
    public static String print(int n) {
        String result="";
        //segitiga atas
        for (int i=0;i<n/2+1;i++) {
            for (int j=0;j<n/2-i;j++){
                result=result+" ";
            }
            for (int j=0;j<2*i+1;j++){
                result=result+"*";
            }
            result=result+"\n";
        }
        //segitiga bawah
        for (int i=n/2-1;i>=0;i--) {
            for (int j=0;j<n/2-i;j++){
                result=result+" ";
            }
            for (int j=0;j<2*i+1;j++){
                if (i==n/2 || j==0 || j==2*i)
                    result=result+"*";
                else
                    result=result+"*";
            }
            result=result+"\n";
        }
        return result;
    }
}
