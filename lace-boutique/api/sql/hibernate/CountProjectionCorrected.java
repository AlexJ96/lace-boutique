package api.sql.hibernate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.AggregateProjection;
import org.hibernate.criterion.CriteriaQuery;

public class CountProjectionCorrected extends AggregateProjection {
	private boolean distinct;

	/**
	 * Constructs the count projection.
	 *
	 * @param prop The property name
	 *
	 * @see Projections#count(String)
	 * @see Projections#countDistinct(String)
	 */
	protected CountProjectionCorrected(String prop) {
		super( "count", prop );
	}

	@Override
	protected List buildFunctionParameterList(Criteria criteria, CriteriaQuery criteriaQuery) {
		final String[] cols = criteriaQuery.getColumns( propertyName, criteria );
		return ( distinct ? buildCountDistinctParameterList( cols ) : Arrays.asList( cols ) );
	}

	@SuppressWarnings("unchecked")
	private List buildCountDistinctParameterList(String[] cols) {
		final List params = new ArrayList( cols.length + 1 );
		params.add( "distinct" );
		params.addAll( Arrays.asList( cols ) );
		return params;
	}

	/**
	 * Sets the count as being distinct
	 *
	 * @return {@code this}, for method chaining
	 */
	public CountProjectionCorrected setDistinct() {
		distinct = true;
		return this;
	}

	@Override
	public String toString() {
		if ( distinct ) {
			return super.getFunctionName() + "( distinct " + super.getPropertyName() + ")";// + super.toString();
		}
		else {
			return super.toString();
		}
	}

}
