https://powcoder.com
代写代考加微信 powcoder
Assignment Project Exam Help
Add WeChat powcoder
package model;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.hsqldb.cmdline.SqlFile;
import org.hsqldb.cmdline.SqlToolError;

/*
 * This class is used to perform CRUD operation
 * All method is implemented statically so that other class can invoke them directly.
 * Initialize method must be invoked by UniLinkGUI first
 */
public class DBTool {
	
	private static Connection con;
	
//	private static void generateData() throws SQLException {
//		
//	}
	
//	private static void createTable() throws SQLException {
//		// check whether table exist, if not exist, 
//		// that means this database is newly created, so table should be created and some data should be inserted
//		if(!con.getMetaData().getTables(null, null, "%", null).next()) {
//			// table not exist
//			// 1) created table
//			String sql_template = "CREATE TABLE %s " +
//									"(id CHAR(6) PRIMARY KEY, " +
//									"title VARCHAR(255), " +
//									"description VARCHAR(255), " +
//									"creatorId VARCHAR(20), " +
//									"status BOOLEAN, " +  
//									"image VARCHAR(50), ";
//			String[] table_sqls = new String[4]; 
//			String create_table_sql = sql_template + 
//							"venue VARCHAR(50), " +
//							"date CHAR(10), " + 
//							"capacity INTEGER, " +
//							"attendeeCount INTEGER)";
//			table_sqls[0] = String.format(create_table_sql, "EVENT");
//			create_table_sql = sql_template +
//								"proposedPrice DOUBLE, " + 
//								"lowestOffer DOUBLE)";
//			table_sqls[1] = String.format(create_table_sql, "JOB");
//			create_table_sql = sql_template + 
//								"askingPrice DOUBLE, " +
//								"highestOffer DOUBLE, " +
//								"minimumRaise DOUBLE)";
//			table_sqls[2] = String.format(create_table_sql, "SALE");
//			table_sqls[3] = "CREATE TABLE REPLY " +
//										"(postId CHAR(6), " +
//										 "rvalue DOUBLE, " +
//										"responderId VARCHAR(20))";
//			Statement stmt = con.createStatement();
//			for(String ts : table_sqls) {
//				stmt.executeLargeUpdate(ts);
//			}
//			// inseart data into db
//			DBTool.generateData();
//		}
//	}
	
	// This method must be invoked by UniLinkGUI first
	public static void initialize() {
		try {
			// 1) initialize db connection 
			Class.forName("org.hsqldb.jdbc.JDBCDriver");
			con = DriverManager.getConnection("jdbc:hsqldb:file:database/unilinkdb", "SA", "");
			// 2) load sql file to create table and insert data into table if table is newly created
			if(!con.getMetaData().getTables(null, null, "EVENT", null).next()) {
				SqlFile sqlFile = new SqlFile(new File("database/database_intialize.sql"));
				sqlFile.setConnection(con);
				sqlFile.execute();
			}
		} catch (ClassNotFoundException | SQLException | IOException | SqlToolError e) {
			e.printStackTrace();
			System.err.println("Error initalizing DB. Exit Program");
			System.exit(-1);
		}
	}
	
	// ==================== Add ====================================
	// common used by addEvent/addJob/addSale
	private static void setPostBasicSql(Post post, PreparedStatement stmt) throws SQLException {
		stmt.setString(1, post.getId());
		stmt.setString(2, post.getTitle());
		stmt.setString(3, post.getDescription());
		stmt.setString(4, post.getCreatorId());
		stmt.setBoolean(5, post.getStatus());
		stmt.setString(6, post.getImage());
	}
	
	// add event into database
	public static void addEvent(Event event) throws SQLException {
		PreparedStatement stmt = con.prepareStatement("INSERT INTO EVENT VALUES(?,?,?,?,?,?,?,?,?,?)");
		DBTool.setPostBasicSql(event, stmt);
		stmt.setString(7, event.getVenue());
		stmt.setString(8, event.getDate());
		stmt.setInt(9, event.getCapacity());
		stmt.setInt(10, event.getAttendeeCount());
		stmt.executeUpdate();
	}
	
	// add job into database
	public static void addJob(Job job) throws SQLException {
		PreparedStatement stmt = con.prepareStatement("INSERT INTO JOB VALUES(?,?,?,?,?,?,?,?)");
		DBTool.setPostBasicSql(job, stmt);
		stmt.setDouble(7, job.getProposedPrice());
		stmt.setDouble(8, job.getLowestOffer());
		stmt.executeUpdate();
	}
	
	// add sale into database
	public static void addSale(Sale sale) throws SQLException {
		PreparedStatement stmt = con.prepareStatement("INSERT INTO Sale VALUES(?,?,?,?,?,?,?,?,?)");
		DBTool.setPostBasicSql(sale, stmt);
		stmt.setDouble(7, sale.getAskingPrice());
		stmt.setDouble(8, sale.getHighestOffer());
		stmt.setDouble(9, sale.getMinimumRaise());
		stmt.executeUpdate();
	}
	
	// add reply into datatbase
	public static void addReply(Reply reply) throws SQLException {
		PreparedStatement stmt = con.prepareStatement("INSERT INTO Reply VALUES(?,?,?)");
		stmt.setString(1, reply.getPostId());
		stmt.setDouble(2, reply.getValue());
		stmt.setString(3, reply.getResponderId());
		stmt.executeUpdate();
	}
	// ==================== END Add ====================================
	
	// ========================== Group Query =============================
	// used by getAllEventFilterd/getAllJobFilterd/getAllSaleFilterd
	// table is different post table name
	// when status is null, all post should be selected, others filtered by status
	private static ResultSet queryPostBasic(Boolean status, String creatorId, String table) throws SQLException {
		String sql = "SELECT * FROM " + table;
		ResultSet rs = null;
		if(status == null && creatorId == null) {
			Statement stmt = con.createStatement();
			rs = stmt.executeQuery(sql);
			return rs;
		}
		PreparedStatement stmt = null;
		if(status != null && creatorId == null) {
			sql += " WHERE status=?";
			stmt = con.prepareStatement(sql);
			stmt.setBoolean(1, status);
		}else if(status == null && creatorId != null) {
			sql += " WHERE creatorId=?";
			stmt = con.prepareStatement(sql);
			stmt.setString(1, creatorId);
		}else if(status != null && creatorId != null) {
			sql += " WHERE status=? AND creatorId=?";
			stmt = con.prepareStatement(sql);
			stmt.setBoolean(1, status);
			stmt.setString(2, creatorId);
		}
		rs = stmt.executeQuery();
		return rs;
	}
	
	// used by getAllEventFilterd/getAllJobFilterd/getAllSaleFilterd to generate new post
	private static void setPostBasicValue(Post post, ResultSet rs) throws SQLException {
		post.setId(rs.getString(1));
		post.setTitle(rs.getString(2));
		post.setDescription(rs.getString(3));
		post.setCreatorId(rs.getString(4));
		post.setStatus(rs.getBoolean(5));
		post.setImage(rs.getString(6));
		// query replies
		post.setReplies(DBTool.getRepliesByPostId(post.getId()));
	}
	
	public static List<Event> getAllEventFilterd(Boolean fstatus, String fcreatorId) throws SQLException{
		// if fstatus or fcreatorId is null, then all post with same status is selected
		List<Event> result = new ArrayList<>();
		ResultSet rs = DBTool.queryPostBasic(fstatus, fcreatorId, "EVENT");
		while(rs.next()) {
			Event event = new Event();
			DBTool.setPostBasicValue(event, rs);
			event.setVenue(rs.getString(7));
			event.setDate(rs.getString(8));
			event.setCapacity(rs.getInt(9));
			event.setAttendeeCount(rs.getInt(10));
			result.add(event);
		}
		return result;
	}
	
	public static List<Job> getAllJobFilterd(Boolean fstatus, String fcreatorId) throws SQLException{
		// if fstatus or fcreatorId is null, then all post with same status is selected
		List<Job> result = new ArrayList<>();
		ResultSet rs = DBTool.queryPostBasic(fstatus, fcreatorId, "JOB");
		while(rs.next()) {
			Job job = new Job();
			DBTool.setPostBasicValue(job, rs);
			job.setProposedPrice(rs.getDouble(7));
			job.setLowestOffer(rs.getDouble(8));
			result.add(job);
		}
		return result;
	}
	
	public static List<Sale> getAllSaleFilterd(Boolean fstatus, String fcreatorId) throws SQLException {
		// if fstatus or fcreatorId is null, then all post with same status is selected
		List<Sale> result = new ArrayList<>();
		ResultSet rs = DBTool.queryPostBasic(fstatus, fcreatorId, "SALE");
		while(rs.next()) {
			Sale sale = new Sale();
			DBTool.setPostBasicValue(sale, rs);
			sale.setAskingPrice(rs.getDouble(7));
			sale.setHighestOffer(rs.getDouble(8));
			sale.setMinimumRaise(rs.getDouble(9));
			result.add(sale);
		}
		return result;
	}
	
	public static List<Reply> getRepliesByPostId(String postId) throws SQLException{
		List<Reply> replies = new ArrayList<>();
		String sql = "SELECT * FROM REPLY WHERE postId=?";
		PreparedStatement stmt = con.prepareStatement(sql);
		stmt.setString(1, postId);
		ResultSet rs = stmt.executeQuery();
		while(rs.next()) {
			Reply reply = new Reply();
			reply.setPostId(rs.getString(1));
			reply.setValue(rs.getDouble(2));
			reply.setResponderId(rs.getString(3));
			replies.add(reply);
		}
		return replies;
	}
	
	// ========================== END Group Query =============================
	
	// ======================== UPDATE =================================
	private static void updateBasicPostValue(Post post, PreparedStatement stmt) throws SQLException {
		stmt.setString(1, post.getTitle());
		stmt.setString(2, post.getDescription());
		stmt.setBoolean(3, post.getStatus());
		stmt.setString(4, post.getImage());
	}
	
	public static void updateEvent(Event event) throws SQLException {
		String sql = "UPDATE EVENT SET title=?,description=?,status=?,image=?,venue=?,edate=?,capacity=?,attendeeCount=? WHERE id=?";
		PreparedStatement stmt = con.prepareStatement(sql);
		updateBasicPostValue(event, stmt);
		stmt.setString(5, event.getVenue());
		stmt.setString(6, event.getDate());
		stmt.setInt(7, event.getCapacity());
		stmt.setInt(8, event.getAttendeeCount());
		stmt.setString(9, event.getId());
		stmt.execute();
	}
	
	public static void updateJob(Job job) throws SQLException {
		String sql = "UPDATE JOB SET title=?,description=?,status=?,image=?,proposedPrice=?,lowestOffer=? WHERE id=?";
		PreparedStatement stmt = con.prepareStatement(sql);
		updateBasicPostValue(job, stmt);
		stmt.setDouble(5, job.getProposedPrice());
		stmt.setDouble(6, job.getLowestOffer());
		stmt.setString(7, job.getId());
		stmt.execute();
	}
	
	public static void updateSale(Sale sale) throws SQLException {
		String sql = "UPDATE SALE SET title=?,description=?,status=?,image=?,askingPrice=?,highestOffer=?,minimumRaise=? WHERE id=?";
		PreparedStatement stmt = con.prepareStatement(sql);
		updateBasicPostValue(sale, stmt);
		stmt.setDouble(5, sale.getAskingPrice());
		stmt.setDouble(6, sale.getHighestOffer());
		stmt.setDouble(7, sale.getMinimumRaise());
		stmt.setString(8, sale.getId());
		stmt.execute();
	}
	// =========================== END Update ====================================
	
	// =========================== DELETE ===========================
	public static void deletePost(String type, String id) throws SQLException {
		String sql = "DELETE FROM " + type + " WHERE id=?";
		PreparedStatement stmt = con.prepareStatement(sql);
		stmt.setString(1, id);
		stmt.execute();
	}
	// ==============================================================
	public static int getEntryNum(String table) throws SQLException {
		int entryNum = -1;
		String sql = "SELECT COUNT(*) AS ENTRYNUM FROM " + table;
		Statement stmt = con.createStatement();
		ResultSet rs = stmt.executeQuery(sql);
		if(rs.next()) {
			entryNum = rs.getInt(1);
		}
//		System.out.println("table: " + table + " " + String.valueOf(entryNum));
		return entryNum;
	}
}
