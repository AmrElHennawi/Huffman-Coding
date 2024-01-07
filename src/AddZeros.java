public class AddZeros {
    public static String addLeadingZeros(String binaryNumber, int desiredLength) {
        // Make sure the binaryNumber is not longer than the desired length
        if (binaryNumber.length() > desiredLength) {
            throw new IllegalArgumentException("Binary number is longer than the desired length");
        }

        // Calculate the number of leading zeros needed
        int numberOfZeros = desiredLength - binaryNumber.length();

        // Add leading zeros
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < numberOfZeros; i++) {
            result.append("0");
        }
        result.append(binaryNumber);

        return result.toString();
    }

    public static String addTrailingZeros(String binaryNumber, int desiredLength) {
        // Make sure the binaryNumber is not longer than the desired length
        if (binaryNumber.length() > desiredLength) {
            throw new IllegalArgumentException("Binary number is longer than the desired length");
        }

        // Calculate the number of trailing zeros needed
        int numberOfZeros = desiredLength - binaryNumber.length();

        // Add trailing zeros
        StringBuilder result = new StringBuilder(binaryNumber);
        for (int i = 0; i < numberOfZeros; i++) {
            result.append("0");
        }

        return result.toString();
    }
}
