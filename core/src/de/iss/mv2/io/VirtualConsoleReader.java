package de.iss.mv2.io;

import java.io.Console;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Provides functionality to read from an input stream.
 * 
 * @author Marcel Singer
 *
 */
public class VirtualConsoleReader {

	/**
	 * Prevents this class from being instantiated.
	 */
	private VirtualConsoleReader() {

	}

	/**
	 * Reads a password from the given input stream.
	 * 
	 * @param in
	 *            The stream to read.
	 * @param out
	 *            The output stream to write.
	 * @param question
	 *            The password question to prompt.
	 * @return The read password.
	 * @throws IOException
	 *             If an I/O error occurs.
	 */
	public static String readPassword(InputStream in, OutputStream out,
			String question) throws IOException {
		InputStreamReader reader = new InputStreamReader(in);
		OutputStreamWriter writer = new OutputStreamWriter(out);
		writer.write(question);
		writer.flush();
		int read;
		char current;
		List<Character> chars = new ArrayList<Character>();
		while ((read = reader.read()) != -1) {
			current = (char) read;
			if (current == '\n') {
				break;
			}
			chars.add(current);
		}
		for (int i = 0; i < 1000; i++) {
			writer.write("\n");
			writer.flush();
		}
		writer.flush();
		return buildString(chars).trim();
	}

	/**
	 * Reads a password from the systems console or the standard input stream,
	 * if the systems console is not available.
	 * 
	 * @param question
	 *            The question to display.
	 * @return The read password.
	 * @throws IOException
	 *             If an I/O error occurs.
	 */
	public static char[] readPassword(String question) throws IOException {
		Console console = System.console();
		if (console == null)
			return readPassword(System.in, System.out, question).toCharArray();
		return console.readPassword(question);
	}

	/**
	 * Creates a string from the given characters.
	 * 
	 * @param chars
	 *            The characters to process.
	 * @return The built string.
	 */
	private static String buildString(Iterable<Character> chars) {
		StringBuilder sb = new StringBuilder();
		for (Character c : chars) {
			sb.append(c);
		}
		return sb.toString();
	}

	/**
	 * Reads a line from the given input stream.
	 * 
	 * @param in
	 *            The input stream to read.
	 * @param out
	 *            The output stream to write.
	 * @param question
	 *            The question to write to the given output stream.
	 * @return The read line.
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	public static String readLine(InputStream in, OutputStream out,
			String question) throws IOException {
		InputStreamReader reader = new InputStreamReader(in);
		OutputStreamWriter writer = new OutputStreamWriter(out);
		writer.write(question);
		writer.flush();
		int read;
		char current;
		List<Character> chars = new ArrayList<Character>();
		while ((read = reader.read()) != -1) {
			current = (char) read;
			if (current == '\n') {
				break;
			}
			chars.add(current);
		}
		writer.flush();
		return buildString(chars).trim();
	}

	/**
	 * Reads a line from the standard input stream.
	 * 
	 * @param question
	 *            The question to write to the standard output stream.
	 * @return The read line.
	 * @throws IOException
	 *             If an I/O error occurs.
	 */
	public static String readLine(String question) throws IOException {
		return readLine(System.in, System.out, question);
	}

	/**
	 * Reads a boolean value from the given input stream.
	 * 
	 * @param in
	 *            The input stream to read.
	 * @param out
	 *            The output stream to write.
	 * @param question
	 *            The question to write to the give output stream.
	 * @param positiveInput
	 *            The phrase to match to get a positive result.
	 * @return {@code true} if the given phrase was matched.
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	public static boolean readBoolean(InputStream in, OutputStream out,
			String question, String positiveInput) throws IOException {
		String result = readLine(in, out, question);
		return result.trim().equalsIgnoreCase(positiveInput);
	}

	/**
	 * Reads a boolean value from the given input stream.
	 * 
	 * @param in
	 *            The input stream to read.
	 * @param out
	 *            The output stream to read.
	 * @param question
	 *            The question to write to the given output stream.
	 * @return {@code true} if the input equals to <plain>yes</plain>
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	public static boolean readBoolean(InputStream in, OutputStream out,
			String question) throws IOException {
		return readBoolean(in, out, question, "yes");
	}

	/**
	 * Reads a boolean value from the standard input stream.
	 * 
	 * @param question
	 *            The question to write to the standard output stream.
	 * @return {@code true} if the input equals to <plain>yes</plain>
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	public static boolean readBoolean(String question) throws IOException {
		return readBoolean(System.in, System.out, question);
	}

	/**
	 * Reads an integer from the the standard input stream.
	 * 
	 * @param question
	 *            The question to display.
	 * @return The read integer.
	 * @throws IOException
	 *             If an I/O error occurs.
	 */
	public static int readInteger(String question) throws IOException {
		String input;
		boolean invalidInput;
		int value = 0;
		do {
			invalidInput = false;
			input = readLine(question);
			try {
				value = Integer.parseInt(input);
			} catch (NumberFormatException ex) {
				invalidInput = true;
				System.err
						.println("The given input is not a valid integer. Try again...");
			}
		} while (invalidInput);
		return value;
	}

}
