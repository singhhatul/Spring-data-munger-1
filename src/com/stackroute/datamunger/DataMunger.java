package com.stackroute.datamunger;

/*There are total 5 DataMungertest files:
 * 
 * 1)DataMungerTestTask1.java file is for testing following 3 methods
 * a)getSplitStrings()  b) getFileName()  c) getBaseQuery()
 * 
 * Once you implement the above 3 methods,run DataMungerTestTask1.java
 * 
 * 2)DataMungerTestTask2.java file is for testing following 3 methods
 * a)getFields() b) getConditionsPartQuery() c) getConditions()
 * 
 * Once you implement the above 3 methods,run DataMungerTestTask2.java
 * 
 * 3)DataMungerTestTask3.java file is for testing following 2 methods
 * a)getLogicalOperators() b) getOrderByFields()
 * 
 * Once you implement the above 2 methods,run DataMungerTestTask3.java
 * 
 * 4)DataMungerTestTask4.java file is for testing following 2 methods
 * a)getGroupByFields()  b) getAggregateFunctions()
 * 
 * Once you implement the above 2 methods,run DataMungerTestTask4.java
 * 
 * Once you implement all the methods run DataMungerTest.java.This test case consist of all
 * the test cases together.
 */

public class DataMunger {

	/*
	 * This method will split the query string based on space into an array of words
	 * and display it on console
	 */

	public String[] getSplitStrings(String queryString) {

		return queryString.toLowerCase().split(" ");
	}

	/*
	 * Extract the name of the file from the query. File name can be found after a
	 * space after "from" clause. Note: ----- CSV file can contain a field that
	 * contains from as a part of the column name. For eg: from_date,from_hrs etc.
	 * 
	 * Please consider this while extracting the file name in this method.
	 */

	public String getFileName(String queryString) {
		queryString = queryString.toLowerCase();

		int from = queryString.indexOf("from");
		int to = queryString.indexOf("csv");
		return queryString.substring(from + 5, to + 3);
	}

	/*
	 * This method is used to extract the baseQuery from the query string. BaseQuery
	 * contains from the beginning of the query till the where clause
	 * 
	 * Note: ------- 1. The query might not contain where clause but contain order
	 * by or group by clause 2. The query might not contain where, order by or group
	 * by clause 3. The query might not contain where, but can contain both group by
	 * and order by clause
	 */
	
	public String getBaseQuery(String queryString) {
		String BasedQuery = null;
		queryString = queryString.toLowerCase();
		if (queryString.contains("where")) {
			String[] splittedArray = queryString.split(" where");
			BasedQuery = splittedArray[0];
		} else if (queryString.contains("group by")) {
			String[] splittedArray = queryString.split(" group by");
			BasedQuery = splittedArray[0];
		} else if (queryString.contains("order by")) {
			String[] splittedArray = queryString.split(" order by");
			BasedQuery = splittedArray[0];
		} else {
			BasedQuery = "";
		}
		return BasedQuery;
	}

	/*
	 * This method will extract the fields to be selected from the query string. The
	 * query string can have multiple fields separated by comma. The extracted
	 * fields will be stored in a String array which is to be printed in console as
	 * well as to be returned by the method
	 * 
	 * Note: 1. The field name or value in the condition can contain keywords
	 * as a substring. For eg: from_city,job_order_no,group_no etc. 2. The field
	 * name can contain '*'
	 * 
	 */
	
	public String[] getFields(String queryString) {
		queryString = queryString.toLowerCase();

		int posOfSelect= queryString.indexOf("select ");
		int posOfFrom = queryString.indexOf(" from");
		String filename = queryString.substring(posOfSelect + 7, posOfFrom);
		return filename.split(",");
	}

	/*
	 * This method is used to extract the conditions part from the query string. The
	 * conditions part contains starting from where keyword till the next keyword,
	 * which is either group by or order by clause. In case of absence of both group
	 * by and order by clause, it will contain till the end of the query string.
	 * Note:  1. The field name or value in the condition can contain keywords
	 * as a substring. For eg: from_city,job_order_no,group_no etc. 2. The query
	 * might not contain where clause at all.
	 */
	
	public String getConditionsPartQuery(String queryString) {
		String partition = null;
		queryString = queryString.toLowerCase();
		if (queryString.contains("where")) {
			String[] QueryArray = queryString.split("where ");
			if (QueryArray[1].contains("order by")) {
				int getOrderBy = QueryArray[1].indexOf("order by");
				QueryArray[1] = QueryArray[1].substring(0, getOrderBy -1);
				partition = QueryArray[1];
			} else if (QueryArray[1].contains("group by")) {
				int getGroupBy = QueryArray[1].indexOf("group by");
				QueryArray[1] = QueryArray[1].substring(0, getGroupBy -1);
				partition = QueryArray[1];
			} else {
				partition = QueryArray[1];
			}
		}
		return partition;

	}

	/*
	 * This method will extract condition(s) from the query string. The query can
	 * contain one or multiple conditions. In case of multiple conditions, the
	 * conditions will be separated by AND/OR keywords. for eg: Input: select
	 * city,winner,player_match from ipl.csv where season > 2014 and city
	 * ='Bangalore'
	 * 
	 * This method will return a string array ["season > 2014","city ='bangalore'"]
	 * and print the array
	 * 
	 * Note: ----- 1. The field name or value in the condition can contain keywords
	 * as a substring. For eg: from_city,job_order_no,group_no etc. 2. The query
	 * might not contain where clause at all.
	 */

	public String[] getConditions(String queryString) {
		queryString = queryString.toLowerCase();
		String[] QueryFinder;

		String temporaryString;
		String[] conditionalQuery;
		String[] getConditionStatement = null;

		if (queryString.contains("where")) {
			QueryFinder = queryString.trim().split("where ");

			if (QueryFinder[1].contains("group by")) {
				conditionalQuery = QueryFinder[1].trim().split("group by");
				temporaryString = conditionalQuery[0];

			} else if (QueryFinder[1].contains("order by")) {
				conditionalQuery = QueryFinder[1].trim().split("order by");
				temporaryString = conditionalQuery[0];
			} else {
				temporaryString = QueryFinder[1];
			}
			getConditionStatement = temporaryString.toLowerCase().trim().split(" and | or ");

		}
		return getConditionStatement;

	}

	/*
	 * This method will extract logical operators(AND/OR) from the query string. The
	 * extracted logical operators will be stored in a String array which will be
	 * returned by the method and the same will be printed Note:  1. AND/OR
	 * keyword will exist in the query only if where conditions exists and it
	 * contains multiple conditions. 2. AND/OR can exist as a substring in the
	 * conditions as well. For eg: name='Alexander',color='Red' etc. Please consider
	 * these as well when extracting the logical operators.
	 * 
	 */

	public String[] getLogicalOperators(String queryString) {
		queryString = queryString.toLowerCase();
		String[] queryOfParameter = queryString.split(" ");

		String getLogicalOperators = "";
		if (queryString.contains("where ")) {
			for (int i = 0; i < queryOfParameter.length; i++) {
				if (queryOfParameter[i].matches("and|or|not")) {

					getLogicalOperators += queryOfParameter[i] + " ";
				}
			}
			return getLogicalOperators.split(" ");
		}
		return null;
	}

	/*
	 * This method extracts the order by fields from the query string. Note: 
	 * 1. The query string can contain more than one order by fields. 2. The query
	 * string might not contain order by clause at all. 3. The field names,condition
	 * values might contain "order" as a substring. For eg:order_number,job_order
	 * Consider this while extracting the order by fields
	 */

	public String[] getOrderByFields(String queryString) {

		queryString = queryString.toLowerCase();
		String[] getOrderByField = null;
		if (queryString.contains("order by")) {
			int orderby = queryString.indexOf("order by ");
			String order = queryString.substring(orderby + 9);
			getOrderByField = order.split(" ");
		}
		return getOrderByField;
	}

	/*
	 * This method extracts the group by fields from the query string. Note:
	 * 1. The query string can contain more than one group by fields. 2. The query
	 * string might not contain group by clause at all. 3. The field names,condition
	 * values might contain "group" as a substring. For eg: newsgroup_name
	 * 
	 * Consider this while extracting the group by fields
	 */

	public String[] getGroupByFields(String queryString) {

		String[] getGroupByField = null;
		queryString = queryString.toLowerCase();
		if (queryString.contains("group by")) {
			int groupby = queryString.indexOf("group by");
			String group = queryString.substring(groupby + 9);
			getGroupByField = group.split(" ");
		}
		return getGroupByField;
	}

	/*
	 * This method extracts the aggregate functions from the query string. Note:
	 *  1. aggregate functions will start with "sum"/"count"/"min"/"max"/"avg"
	 * followed by "(" 2. The field names might
	 * contain"sum"/"count"/"min"/"max"/"avg" as a substring. For eg:
	 * account_number,consumed_qty,nominee_name
	 * 
	 * Consider this while extracting the aggregate functions
	 */

	public String[] getAggregateFunctions(String queryString) {
		queryString = queryString.toLowerCase();
		boolean flag = false;
		String getAggregateFunction = "";
		String[] splittedQuery = queryString.split(" ");
		String[] aggregateQuery = splittedQuery[1].split(",");
		for (int i = 0; i < aggregateQuery.length; i++) {
			if ((aggregateQuery[i].startsWith("max(") && aggregateQuery[i].endsWith(")"))
					|| (aggregateQuery[i].startsWith("min(") && aggregateQuery[i].endsWith(")"))
					|| (aggregateQuery[i].startsWith("count(") && aggregateQuery[i].endsWith(")"))
					|| (aggregateQuery[i].startsWith("avg(") && aggregateQuery[i].endsWith(")"))
					|| (aggregateQuery[i].startsWith("sum") && aggregateQuery[i].endsWith(")"))) {
				getAggregateFunction += aggregateQuery[i] + " ";
				flag = true;
			}
		}
		if (flag == true)
			return getAggregateFunction.trim().split(" ");
		else
			return null;

	}
}

