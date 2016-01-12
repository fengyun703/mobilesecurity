package com.mobilesecurity.test;

import com.mobilesecurity.db.dao.AntiVirusDao;

import android.test.AndroidTestCase;

public class AntiVirusTest extends AndroidTestCase {

	public void insertTest(){
		AntiVirusDao.insert("e799b3a3295e6a6f2f608309019ff776", 6, "’‚ «≤°∂æ", "com.example.createvirus");
	}
}
