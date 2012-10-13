package com.example.storm.test;


import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.List;
import java.util.Random;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import com.example.storm.DatabaseHelper;
import com.example.storm.TestDatabaseHelper;
import com.example.storm.TestDbFactory;
import com.example.storm.entity.SimpleEntity;
import com.example.storm.entity.dao.SimpleEntityDao;
import com.example.storm.entity.dao.SimpleEntityTable;

public class UpgradeTestCase extends AndroidTestCase {

	private Context ctx;
	private SimpleEntityDao dao;
	private DatabaseHelper dbHelper;

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		ctx = getContext();
		openDatabase();
		dao = new SimpleEntityDao(ctx);
	}

	private void openDatabase() {
		dbHelper = TestDbFactory.getDatabaseHelper(ctx);
		SQLiteDatabase db = dbHelper.getReadableDatabase();
		assertEquals(TestDatabaseHelper.DB_VERSION, db.getVersion());
		// wipe database
		dbHelper.dropAndCreate();
	}

	private void persistRandomEntities(int n) {
		for (int i = 0; i < n; i++) {
			SimpleEntity randomEntity = new SimpleEntity();
			randomEntity.setLongField(new Random().nextLong());
			long id = dao.insert(randomEntity);
			assertTrue(id > 0);
		}
	}

	public void testBackupAndRestore() throws IOException {
		persistRandomEntities(11);
		SimpleEntity e = new SimpleEntity();
		populateTestEntity(e);
		dao.insert(e);
		List<SimpleEntity> before = dao.listAll();
		dbHelper.backupAllTablesToCsv();
		dbHelper.dropAndCreate();
		assertEquals(0, dao.listAll().size());
		dbHelper.restoreAllTablesFromCsv();
		List<SimpleEntity> after = dao.listAll();
		for (int i = 0; i < before.size(); i++) {
			DaoTestCase.assertAllFieldsMatch(before.get(i), after.get(i));
		}
	}
	
	public void testWriteToCsv() throws IOException {
		dbHelper.dropAndCreate();
		SimpleEntity populatedEntity = new SimpleEntity();
		populateTestEntity(populatedEntity);
		dao.insert(populatedEntity);
		dao.insert(new SimpleEntity()); // default
		dbHelper.backupAllTablesToCsv();
		FileInputStream ois = ctx.openFileInput("testDb.v2.SimpleEntity");
		InputStreamReader isr = new InputStreamReader(ois);
		BufferedReader reader = new BufferedReader(isr);
		String headerRow = reader.readLine();
		String row1 = reader.readLine();
		String expected = "Q0FGRUJBQkU=,1,0,122,4609965796441453736,1.618034,1,75025,12586269025,0,28657,1,89,88,18000000,-4619630062026626736,-0.618034,1836311903,86267571272,17711,\"Hello, world!\"";
		assertEquals(expected, row1);
		String row2 = reader.readLine();
		expected = ",0,0,0,0,0.0,2,0,0,0,0,,,,,,,,,,";
		assertEquals(expected, row2);
	}
	
	private void populateTestEntity(SimpleEntity e) {
		e.setBlobField("CAFEBABE".getBytes());
		e.setBooleanField(true);
		e.setCharField('z');
		e.setDoubleField((1 + Math.sqrt(5)) / 2);
		e.setFloatField((float) ((1 + Math.sqrt(5)) / 2));
		e.setIntField(75025);
		e.setLongField(12586269025L);
		e.setShortField((short) 28657);
		e.setwBooleanField(Boolean.TRUE);
		e.setwByteField(new Byte((byte) 89));
		e.setwCharacterField('X');
		e.setwDateField(new Date("Jan 1, 1970"));
		e.setwDoubleField((1 - Math.sqrt(5)) / 2);
		e.setwFloatField((float) ((1 - Math.sqrt(5)) / 2));
		e.setwIntegerField(1836311903);
		e.setwLongField(86267571272L);
		e.setwShortField((short) 17711);
		e.setwStringField("Hello, world!");
	}

}