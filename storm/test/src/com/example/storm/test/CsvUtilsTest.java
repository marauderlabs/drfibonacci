package com.example.storm.test;

import java.io.IOException;
import java.util.List;

import junit.framework.TestCase;

import com.example.storm.CsvUtils;

public class CsvUtilsTest extends TestCase {

	private static final char QUOTE = '"';

	public void testEscapePlainValue() {
		String val = "this is as a regular string with _!@#$%^&*() and 0123456789";
		assertEquals(val, CsvUtils.escapeCsv(val));
	}

	public void testEscapeValueContainingComma() {
		String val = "ab,cd";
		assertEquals(QUOTE + val + QUOTE, CsvUtils.escapeCsv(val));
	}
	
	public void testEscapeValueContainingQuote() {
		String val = "abcd" + QUOTE + "efgh";
		assertEquals(QUOTE + "abcd" + QUOTE + QUOTE + "efgh" + QUOTE, CsvUtils.escapeCsv(val));
	}
	
	public void testEscapeValueContainingCR() {
		String val = "abcd\r";
		assertEquals(QUOTE + val + QUOTE, CsvUtils.escapeCsv(val));
	}
	
	public void testEscapeValueContainingLF() {
		String val = "abcd\n";
		assertEquals(QUOTE + val + QUOTE, CsvUtils.escapeCsv(val));
	}
	
	public void testUnescapeValue() {
		String val = QUOTE + "this is a test" + QUOTE;
		assertEquals("this is a test", CsvUtils.unescapeCsv(val));
	}
	
	public void testUnescapeValueContainingQuotes() {
		String orig = "a " + QUOTE + "nice" + QUOTE + " day";
		assertEquals(orig, CsvUtils.unescapeCsv(CsvUtils.escapeCsv(orig)));  
	}
	
	public void testGetTokens() throws IOException {
		String csvRow = "unquotedValue,";
		csvRow += QUOTE + "quotedValue" + QUOTE + ",";
		csvRow += QUOTE + "valueContains\"\"quote\"\"" + QUOTE + ",";
		csvRow += QUOTE + "valueContains\"\"comma,in,quotes\"\"" + QUOTE;
		List<String> tokens = CsvUtils.getValues(csvRow);
		assertEquals(4, tokens.size());
		assertEquals("unquotedValue", tokens.get(0));
		assertEquals("quotedValue", tokens.get(1));
		assertEquals("valueContains\"quote\"", tokens.get(2));
		assertEquals("valueContains\"comma,in,quotes\"", tokens.get(3));
	}

	public void testReadNullValues() {
		String csv = ",0,0,0,0,0.0,2,0,0,0,0,,,,,,,,,,";
		List<String> values = CsvUtils.getValues(csv);
		assertEquals(21, values.size());
	}
	
}