import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigInteger;
import java.util.*;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.nio.file.AccessDeniedException;

public class a {

    private static BigInteger decodeValue(String value, int base) {
        return new BigInteger(value, base);
    }

    private static double lagrangeInterpolation(List<Integer> xVals, List<BigInteger> yVals, int k) {
        double result = 0.0;

        for (int i = 0; i < k; i++) {
            BigInteger xi = BigInteger.valueOf(xVals.get(i));
            BigInteger yi = yVals.get(i);
            BigInteger term = BigInteger.ONE;

            for (int j = 0; j < k; j++) {
                if (i != j) {
                    BigInteger xj = BigInteger.valueOf(xVals.get(j));
                    term = term.multiply(BigInteger.ZERO.subtract(xj)).divide(xi.subtract(xj));
                }
            }

            result += yi.multiply(term).doubleValue();
        }

        return result;
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter the path to the JSON file: ");
        String filePath = sc.nextLine();

        Path path = Paths.get(filePath);
        try {
            String content = new String(Files.readAllBytes(path));
            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject) parser.parse(content);
            JSONObject keys = (JSONObject) jsonObject.get("keys");

            int n = Integer.parseInt(keys.get("n").toString());
            int k = Integer.parseInt(keys.get("k").toString());

            List<Integer> xVals = new ArrayList<>();
            List<BigInteger> yVals = new ArrayList<>();

            for (Object key : jsonObject.keySet()) {
                if (key.equals("keys")) continue;
                JSONObject point = (JSONObject) jsonObject.get(key);
                int x = Integer.parseInt((String) key);
                int base = Integer.parseInt(point.get("base").toString());
                String encodedValue = (String) point.get("value");
                BigInteger y = decodeValue(encodedValue, base);
                xVals.add(x);
                yVals.add(y);
            }

            double c = lagrangeInterpolation(xVals, yVals, k);
            System.out.printf("The constant term (c) of the polynomial is: %.2f%n", c);

        } catch (AccessDeniedException e) {
            System.out.println("Access denied to the file: " + path.toString());
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + path.toString());
        } catch (ParseException e) {
            System.out.println("Error parsing JSON file.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
