package zx.tonyfalk.Utils;

public class UMath {
    public static int random(int min , int max) {
        int randomnum = (int)Math.floor(Math.random()*(max-min+1)+min);
        return randomnum;
    }
    public static boolean isNumber(String s){
        try {
            Integer.parseInt(s);
            return true;
        }catch (NumberFormatException e){
            return false;
        }
    }
}