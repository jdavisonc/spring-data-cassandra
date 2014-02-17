package org.springframework.cassandra.core.cql;

import static org.springframework.cassandra.core.cql.CqlConstantType.Regex.*;
import java.util.regex.Pattern;

public enum CqlConstantType {

	STRING(STRING_PATTERN), INTEGER(INTEGER_PATTERN), FLOAT(FLOAT_PATTERN), BOOLEAN(BOOLEAN_PATTERN), UUID(UUID_PATTERN), BLOB(
			BLOB_PATTERN);

	private Pattern pattern;

	private CqlConstantType(Pattern pattern) {
		this.pattern = pattern;
	}

	public boolean isValid(CharSequence candidate) {
		return pattern.matcher(candidate).matches();
	}

	public static class Regex {

		// TODO: any sequence of characters encased in single quotes, as long as single quotes are doubled
		public static final String STRING_REGEX = "\\'[.TODO]*+\\'";
		public static final Pattern STRING_PATTERN = Pattern.compile(STRING_REGEX);

		public static final String INTEGER_REGEX = "\\-?[0-9]+";
		public static final Pattern INTEGER_PATTERN = Pattern.compile(INTEGER_REGEX);

		public static final String FLOAT_REGEX = "(\\-?[0-9]+(\\.[0-9]*)?([eE][+-]?[0-9+])?)|NaN|Infinity";
		public static final Pattern FLOAT_PATTERN = Pattern.compile(FLOAT_REGEX);

		public static final String BOOLEAN_REGEX = "(?i)true|false";
		public static final Pattern BOOLEAN_PATTERN = Pattern.compile(BOOLEAN_REGEX);

		public static final String UUID_REGEX = "(?i)[0-9A-F]{8}+\\-[0-9A-F]{4}+\\-[0-9A-F]{4}+\\-[0-9A-F]{4}+\\-[0-9A-F]{12}+";
		public static final Pattern UUID_PATTERN = Pattern.compile(UUID_REGEX);

		public static final String BLOB_REGEX = "(?i)0[X](0-9A-F)+";
		public static final Pattern BLOB_PATTERN = Pattern.compile(BLOB_REGEX);
	}
}