package iagocolodetti.conversordebase;

import java.math.BigInteger;

public class FuncoesBase {
    private boolean bigIntegerTryParse(String s) {
        try {
            new BigInteger(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private String reverterString(String s) {
        String string = "";
        char[] array = s.toCharArray();
        for (int i = array.length-1; i >= 0; i--)
            string += String.valueOf(array[i]);
        return string;
    }

    private boolean checarBaseAlgarismo(String algarismo, int base) {
        int numero;
        for (int i = 0; i < algarismo.length(); i++) {
            char c = algarismo.charAt(i);
            if (c >= 48 && c <= 57)
                numero = Integer.parseInt(String.valueOf(c));
            else if (c >= 65 && c <= 86)
                numero = c - 55;
            else if (c >= 97 && c <= 118)
                numero = c - 87;
            else
                return false;

            if (numero >= base)
                return false;
        }
        return true;
    }

    private int algarismoParaNumero(char algarismo) {
        int numero;
        if (algarismo >= 65 && algarismo <= 86)
            numero = algarismo - 55;
        else if (algarismo >= 97 && algarismo <= 118)
            numero = algarismo - 87;
        else
            numero = Integer.parseInt(String.valueOf(algarismo));
        return numero;
    }

    private char numeroParaAlgarismo(int numero) {
        char algarismo;
        if (numero >= 10 && numero <= 31)
            algarismo = (char)(numero + 55);
        else
            algarismo = (char)(numero + 48);
        return algarismo;
    }

    private String converterAlgarismoParaDecimal(String algarismo, int base)
            throws Exception {
        if (algarismo.length() <= 0)
            throw new Exception("Digite algum algarismo para ser convertido.");
        if (algarismo.contains(".,"))
            throw new Exception("Use apenas algarismos inteiros.");
        if (!checarBaseAlgarismo(algarismo, base))
            throw new Exception("Esse algarismo não pertecem a essa base.");
        int i, exp = 0;
        BigInteger numero = BigInteger.ZERO;
        BigInteger tmpN, tmpB;
        for (i = 0; i < algarismo.length(); i++) {
            exp = (algarismo.length() - (1 + i));
            tmpN = BigInteger.valueOf(algarismoParaNumero(algarismo.charAt(i)));
            tmpB = BigInteger.valueOf(base);
            tmpN = tmpN.multiply(tmpB.pow(exp));
            numero = numero.add(tmpN);
        }
        if (!bigIntegerTryParse(numero.toString()))
            throw new Exception("O valor a ser convertido é muito alto.");
        return numero.toString();
    }

    private String converterDecimalParaAlgarismo(String algarismo, int base)
            throws Exception {
        if (algarismo.length() <= 0)
            throw new Exception("Digite algum algarismo para ser convertido.");
        if (algarismo.contains(".,"))
            throw new Exception("Use apenas algarismos inteiros.");
        if (!checarBaseAlgarismo(algarismo, 10))
            throw new Exception("Esse algarismo não pertecem a essa base.");
        BigInteger resto, numero, tmpB;
        numero = new BigInteger(algarismo);
        algarismo = "";
        while (numero.compareTo(BigInteger.ZERO) == 1) {
            tmpB = BigInteger.valueOf(base);
            resto = numero.mod(tmpB);
            algarismo += numeroParaAlgarismo(Integer.parseInt(resto.toString()));
            numero = numero.divide(tmpB);
        }
        return reverterString(algarismo);
    }

    public String converterAlgarismo(String algarismo, int daBase, int paraBase)
            throws Exception {
        try {
            return converterDecimalParaAlgarismo(converterAlgarismoParaDecimal(algarismo, daBase), paraBase);
        }
        catch (Exception e) {
            throw new Exception(e.getMessage());
        }
    }
}
