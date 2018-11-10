package net.daw.helper;

public class EncodingHelper {

    public static String quotate(String strCadena) {
    	return "\"" + strCadena + "\"";
    }
	
    public static String escapeQuotes(String str) {
        char[] chars = str.toCharArray();
        String strFinal = "";
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == '"') {
                strFinal += String.valueOf("'");
            } else {
                strFinal += String.valueOf(chars[i]);
            }
        }
        return String.valueOf(strFinal);
    }

    public static String escapeLine(String str) {
        String strFinal = "";
        if (!str.equals("")) {
            char[] chars = str.toCharArray();
            for (int i = 0; i < chars.length; i++) {
                if (chars[i] == '\n') {
                    strFinal += String.valueOf(" ");
                } else {
                    strFinal += String.valueOf(chars[i]);
                }
            }
        }
        return String.valueOf(strFinal);
    }
    


}
