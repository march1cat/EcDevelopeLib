package ec.crypt;

import java.math.BigInteger;

public class HexTool {

	public static String byte2Hex(byte[] b) {
		String result = "";
		for (int i = 0; i < b.length; i++)
			result += Integer.toString((b[i] & 0xff) + 0x100, 16).substring(1);
		return result;
	}

	public static String string2Hex(String plainText, String charset) throws Exception {
		return String.format("%040x", new BigInteger(1, plainText.getBytes(charset)));
	}

	public static byte[] hex2Byte(String hexString) {
		byte[] bytes = new byte[hexString.length() / 2];
		for (int i = 0; i < bytes.length; i++)
			bytes[i] = (byte) Integer.parseInt(hexString.substring(2 * i, 2 * i + 2), 16);
		return bytes;
	}

	public static String hex2String(String hexString) {
		StringBuilder str = new StringBuilder();
		for (int i = 0; i < hexString.length(); i += 2)
			str.append((char) Integer.parseInt(hexString.substring(i, i + 2), 16));
		return str.toString();
	}
}
