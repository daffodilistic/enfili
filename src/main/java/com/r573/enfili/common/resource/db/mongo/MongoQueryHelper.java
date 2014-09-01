package com.r573.enfili.common.resource.db.mongo;

import java.util.Date;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

public class MongoQueryHelper {

	public static DBObject dateRange(Date from, Date to, boolean inclusive) {
		String greaterThan = "$gte";
		String lessThan = "$lt";
		if(inclusive){
			lessThan = "$lte";
		}
		DBObject dbObj = new BasicDBObject(greaterThan,from).append(lessThan, to);
		return dbObj;
	}
	public static DBObject dateRange(Date from, Date to) {
		return dateRange(from, to, false);
	}
}
