package org.springframework.cassandra.core.keyspace;

import static org.springframework.cassandra.core.cql.CqlStringUtils.escapeSingle;
import static org.springframework.cassandra.core.cql.CqlStringUtils.singleQuote;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.cassandra.core.cql.CqlStringUtils;

/**
 * Abstract builder class to support the construction of table specifications that have table options, that is, those
 * options normally specified by <code>WITH ... AND ...</code>.
 * <p/>
 * It is important to note that although this class depends on {@link KeyspaceOption} for convenient and typesafe use,
 * it ultimately stores its options in a <code>Map<String,Object></code> for flexibility. This means that
 * {@link #with(KeyspaceOption)} and {@link #with(KeyspaceOption, Object)} delegate to
 * {@link #with(String, Object, boolean, boolean)}. This design allows the API to support new Cassandra options as they
 * are introduced without having to update the code immediately.
 * 
 * @author John McPeek
 * @param <T> The subtype of the {@link KeyspaceOptionsSpecification}.
 */
public abstract class KeyspaceOptionsSpecification<T extends KeyspaceOptionsSpecification<T>> extends
		KeyspaceActionSpecification<KeyspaceOptionsSpecification<T>> {

	protected Map<String, Object> options = new LinkedHashMap<String, Object>();

	@Override
	@SuppressWarnings("unchecked")
	public T name(String name) {
		return (T) super.name(name);
	}

	/**
	 * Convenience method that calls <code>with(option, null)</code>.
	 * 
	 * @return this
	 */
	public T with(KeyspaceOption option) {
		return with(option, null);
	}

	/**
	 * Sets the given table option. This is a convenience method that calls
	 * {@link #with(String, Object, boolean, boolean)} appropriately from the given {@link KeyspaceOption} and value for
	 * that option.
	 * 
	 * @param option The option to set.
	 * @param value The value of the option. Must be type-compatible with the {@link KeyspaceOption}.
	 * @return this
	 * @see #with(String, Object, boolean, boolean)
	 */
	public T with(KeyspaceOption option, Object value) {
		option.checkValue(value);
		return with(option.getName(), value, option.escapesValue(), option.quotesValue());
	}

	/**
	 * Adds the given option by name to this keyspaces's options.
	 * <p/>
	 * Options that have <code>null</code> values are considered single string options where the name of the option is the
	 * string to be used. Otherwise, the result of {@link Object#toString()} is considered to be the value of the option
	 * with the given name. The value, after conversion to string, may have embedded single quotes escaped according to
	 * parameter <code>escape</code> and may be single-quoted according to parameter <code>quote</code>.
	 * 
	 * @param name The name of the option
	 * @param value The value of the option. If <code>null</code>, the value is ignored and the option is considered to be
	 *          composed of only the name, otherwise the value's {@link Object#toString()} value is used.
	 * @param escape Whether to escape the value via {@link CqlStringUtils#escapeSingle(Object)}. Ignored if given value
	 *          is an instance of a {@link Map}.
	 * @param quote Whether to quote the value via {@link CqlStringUtils#singleQuote(Object)}. Ignored if given value is
	 *          an instance of a {@link Map}.
	 * @return this
	 */
	@SuppressWarnings("unchecked")
	public T with(String name, Object value, boolean escape, boolean quote) {
		if (!(value instanceof Map)) {
			if (escape) {
				value = escapeSingle(value);
			}
			if (quote) {
				value = singleQuote(value);
			}
		}
		options.put(name, value);
		return (T) this;
	}

	public Map<String, Object> getOptions() {
		return Collections.unmodifiableMap(options);
	}
}
