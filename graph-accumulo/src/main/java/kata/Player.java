package kata;

/**
 * Created by 1224A on 8/25/2016.
 */
public class Player {
/*
* Symbol    Value
I          1
V          5
X          10
L          50
C          100
D          500
M          1,000
MCMXC 1990*/

    /*public String solution(int n) {
        String romawi = "";
        String [] romrom = {"M","CM","D","C","XC","L","X","IX","V","I"};
        int [] angka = {1000,900,500,100,90,50,10,9,5,1};
        for (int aa=0;aa< angka.length;aa++) {
            if (n >= angka[aa]) {
                if (n / angka[aa] != 4 ) {
                    for (int a = 1; a <= n / angka[aa]; a++) {
                        romawi = romawi + romrom[aa];
                    }
                    n = n % angka[aa];
                } else if (n/angka[aa] == 4) {
                        romawi = romawi + romrom[aa] + romrom[aa - 1];
                        n = n % angka[aa];
                        }
            }
        }
//XCI LXLI
        return romawi;
    }*/
    public static int getCount(String str) {
        int hitung = 0;
        for (char a:str.toCharArray()) {
            if (String.valueOf(a).equals("a") || String.valueOf(a).equals("i") || String.valueOf(a).equals("u") || String.valueOf(a).equals("e") || String.valueOf(a).equals("o"))
                hitung++;
        }
        return hitung;
    }

}
