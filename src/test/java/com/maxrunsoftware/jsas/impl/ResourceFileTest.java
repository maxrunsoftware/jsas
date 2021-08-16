/*
 * Copyright (c) 2021 Max Run Software (dev@maxrunsoftware.com)
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.maxrunsoftware.jsas.impl;

import static org.junit.Assert.*;

import org.junit.Test;

import com.maxrunsoftware.jsas.Util;

public class ResourceFileTest {

	@Test
	public void test() {

		var vfsfile = new VfsMemory.VfsFileMemory("file1.txt", new byte[] { 1, 2, 3, 5 }, "/dir1/file1.txt");
		var r = new ResourceFile(vfsfile, "file1.txt", "file1", false);
		r.setDescription("Desc");
		assertEquals("file1.txt", r.getFilename());
		assertArrayEquals(new byte[] { 1, 2, 3, 5 }, r.getData());
		assertEquals("file1", r.getName());
		assertEquals("Desc", r.getDescription());
		assertEquals(null, r.getText());

		vfsfile = new VfsMemory.VfsFileMemory("file1.txt", Util.asBytes("HelloWorld"), "/dir1/file1.txt");
		r = new ResourceFile(vfsfile, "file1.txt", "file1", true);
		assertEquals(null, r.getData());
		assertEquals("HelloWorld", r.getText());

		vfsfile = new VfsMemory.VfsFileMemory("file1.txt", null, "/dir1/file1.txt");
		r = new ResourceFile(vfsfile, "file1.txt", "file1", true);
		assertEquals(null, r.getData());
		assertEquals(null, r.getText());
		assertEquals(null, r.getData());
	}

}
