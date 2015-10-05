/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testing;


import static org.junit.Assert.*;

import org.junit.Test;

import library.interfaces.daos.IBookHelper;
import library.interfaces.entities.IBook;

public class BookHelperTest 
{
    private final String author = "Kelly";
	private final String title = "Cats";
	private final String callNumber = "3";
	private int id = 1;

	@Test
	public void testMakeBook() 
        {
            BookHelperTest bookHelper = new BookHelperTest();

            IBook book = bookHelper.makeBook(author, title, callNumber, id);
		
            assertNotNull(book);
            
            assertEquals(book.getAuthor(), author);
            assertEquals(book.getTitle(), title);
            assertEquals(book.getCallNumber(), callNumber);
            assertEquals(book.getID(), id);
		

	}
    
}
