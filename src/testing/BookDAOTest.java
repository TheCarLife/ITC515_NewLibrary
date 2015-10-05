package testing;

import java.util.List;
import library.daos.BookMapDAO;
import library.interfaces.daos.IBookHelper;
import library.interfaces.entities.IBook;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

/**
 *
 * @author ShaneDissanayake
 */
public class BookDAOTest 
{
    IBookHelper mockupBookHelper;
    String name1 = "Carlos Lowe";
    String title1 = "Life";
    String callNo1 = "1";
    
    String name2 = "Mary Jane";
    String title2 = "Cooking life";
    String callNo2 = "2";
    
    IBook mockupBook1;
    IBook mockupBook2;
    BookMapDAO bookMapDAO;
    
    
    @Before
    public void setUp() 
    {
        bookMapDAO = new BookMapDAO(mockupBookHelper);
        mockupBookHelper = mock(IBookHelper.class);
        mockupBook1 = mock(IBook.class);
        mockupBook2 = mock(IBook.class);
        
        when(mockupBook1.getID()).thenReturn(1);
        when(mockupBook1.getTitle()).thenReturn(title1);
        when(mockupBook1.getAuthor()).thenReturn(name1);
        when(mockupBookHelper.makeBook(name1, title1, callNo1, 1)).thenReturn(mockupBook1);
        
        when(mockupBook2.getID()).thenReturn(2);
        when(mockupBook2.getTitle()).thenReturn(title2);
        when(mockupBook2.getAuthor()).thenReturn(name2);
        when(mockupBookHelper.makeBook(name2, title2, callNo2, 2)).thenReturn(mockupBook2);
       
        
    }
    
    @After
    public void clearSetUp()
    {
        mockupBookHelper = null;
        mockupBook1 = null;
        mockupBook2 = null;
        bookMapDAO = null;
    }
    
    
    
    @Test
    public void testCreateBookMapDAO()
    {
        bookMapDAO = new BookMapDAO(mockupBookHelper);
        assertNotNull(bookMapDAO);
    }        
    
    
    
    
    @Test(expected = IllegalArgumentException.class)
    public void testCreateBookMapDAOHelperNull()
    {
        bookMapDAO = new BookMapDAO(null);
        fail("IllegalArgumentException");
    }
    
    
    
    @Test
    public void testAddBook()
    {
        IBook book = bookMapDAO.addBook(name1, title1, callNo1);
        verify(mockupBookHelper).makeBook(name1, title1, callNo1, 1);
        assertEquals(book, mockupBook1);
    }
    
    
    
    @Test(expected = IllegalArgumentException.class)
    public void testAddBookAuthorIsNull() 
    {
        IBook book = bookMapDAO.addBook(null, title1, callNo1);
        
        verify(mockupBookHelper)makeBook(null, title1, callNo1, 1);
        
        assertNull(book.getAuthor());
        
        fail("IllegalArgumentException");
    }
   
    
    
    @Test(expected = IllegalArgumentException.class)
    public void testAddBookTitleNull()
    {
        IBook book = bookMapDAO.addBook(name1, null, callNo1);
        
        verify(mockupBookHelper).makeBook(name1, null, callNo1, 1);

	assertNull(book.getTitle());
		
	fail("IllegalArgumentException")
        
    } 
    
    
    
    @Test(expected = IllegalArgumentException.class)
    public void testAddBookTitleBlank()
    {
        IBook book = bookMapDAO.addBook(name1, "", callNo1);

	verify(mockupBookHelper).makeBook(name1, "", callNo1, 1);

	assertNull(book.getTitle());
		
	fail("IllegalArgumentException");
    }
    
    
    @Test(expected = IllegalArgumentException.class)
    public void testAddBookCallNumberIsNull() 
    {

		
	IBook book = bookMapDAO.addBook(name1, title1, null);

	verify(mockupBookHelper).makeBook(name1, title1, null, 1);

	assertNull(book.getCallNumber());
		
	fail("IllegalArgumentException");
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testAddBookCallNumberIsBlank() 
    {
       
        IBook book = bookMapDAO.addBook(name1, title1, "");

		// asserts
        verify(mockupBookHelper).makeBook(name1, title1, "", 1);

	assertNotNull(book.getCallNumber());
		
	fail("IllegalArgumentException");
    
    }
    
    @Test
    public void testGetBookByID() 
    {

	
        IBook book = bookMapDAO.addBook(name1, title1, callNo1);

        IBook mapBook = bookMapDAO.getBookByID(1);

		
        assertEquals(mapBook, mockupBook1);
    }
        
    
    @Test
    public void testListBooks() 
    {
        bookMapDAO.addBook(name1, title1, callNo1);
        bookMapDAO.addBook("Mary Jane", "Cooking life", "2");

        List<IBook> listBooks = bookMapDAO.listBooks();

		// asserts
        assertEquals(2, listBooks.size());
        assertTrue(listBooks.contains(mockupBook1));
        assertTrue(listBooks.contains(mockupBook2));
    }
	
	
	
    @Test
    public void testListBooksIsEmpty() 
    {
        List<IBook> listBooks = bookMapDAO.listBooks();

        assertEquals(0, listBooks.size());
        assertTrue(listBooks.isEmpty());
    
    }

	
	
    @Test    
    public void testFindBooksByAuthor() 
    {
        bookMapDAO.addBook("Carlos Lowe", "Life", "1");
        bookMapDAO.addBook("Mary Jane", "Cooking life", "2");

        List<IBook> listBooks = bookMapDAO.findBooksByAuthor("Carlos Lowe");

        IBook theBird = listBooks.get(0);

        assertEquals(1, listBooks.size());
        assertEquals("Carlos Lowe", theBird.getAuthor());
    }
	
	
	
    @Test(expected = IllegalArgumentException.class)
    public void testFindBooksByAuthorIsEmpty() 
    {
        IBook actual = bookMapDAO.addBook("", title1, callNo1);

        verify(mockupBookHelper).makeBook("", title1, callNo1, 1);

        List<IBook> listBooks = bookMapDAO.findBooksByAuthor("");

        assertTrue(listBooks.contains(actual));
        assertNull(bookMapDAO.findBooksByAuthor(""));

        fail("IllegalArgumentException");
	
    }
	
	
	
    @Test(expected = IllegalArgumentException.class)
    public void testFindBooksByAuthorIsNull() 
    {
        IBook actual = bookMapDAO.addBook(null, title1, callNo1);

        verify(mockupBookHelper).makeBook(null, title1, callNo1, 1);

        List<IBook> listBooks = bookMapDAO.findBooksByAuthor(null);

        
        assertTrue(listBooks.contains(actual));
        assertNull(bookMapDAO.findBooksByAuthor(null));	
		
        fail("IllegalArgumentException");
	
    }

	
	
    @Test
    public void testFindBooksByTitle() 
    {	
        bookMapDAO.addBook("Carlos Lowe", "Life", "1");
        bookMapDAO.addBook("Mary Jane", "Cooking life", "2");

        List<IBook> listBooks = bookMapDAO.findBooksByTitle("Cooking life");

        IBook theCastle = listBooks.get(0);

		
        assertEquals("Cooking life", theCastle.getTitle());
	
    }
	
	
	
    @Test(expected = IllegalArgumentException.class)
    public void testFindBooksByTitleIsEmpty() 
    {
        IBook actual = bookMapDAO.addBook(name1, "", callNo1);

        verify(mockupBookHelper).makeBook(name1, "", callNo1, 1);

        List<IBook> listBooks = bookMapDAO.findBooksByTitle("");
        
        assertTrue(listBooks.contains(actual));
        assertNull(bookMapDAO.findBooksByTitle(""));
		
        fail("IllegalArgumentException");
	
    }
	
	
	
    @Test(expected = IllegalArgumentException.class)
    public void testFindBooksByTitleIsNull() 
    { 
        IBook actual = bookMapDAO.addBook(name1, null, callNo1);

        verify(mockupBookHelper).makeBook(name1, null, callNo1, 1);

        List<IBook> listBooks = bookMapDAO.findBooksByTitle(null);

        assertTrue(listBooks.contains(actual));
        assertNull(bookMapDAO.findBooksByTitle(null));
		
        fail("IllegalArgumentException");
	
    }

	
	
    @Test
    public void testFindBooksByAuthorTitle() 
    {
        bookMapDAO.addBook("Carlos Lowe", "Life", "1");
        bookMapDAO.addBook("Mary Jane", "Cooking life", "2");

        List<IBook> listBooks = bookMapDAO.findBooksByAuthorTitle("Carlos Lowe", "Life");

        
        assertEquals(1, listBooks.size());
        assertTrue(listBooks.contains(mockupBook1));
    
    }
	
	
	
    @Test(expected = IllegalArgumentException.class)
    public void testFindBooksByAuthorTitleIsEmpty() 
    {
        IBook actual = bookMapDAO.addBook("", "", callNo1);

        verify(mockupBookHelper).makeBook("", "", callNo1, 1);

        List<IBook> listBooks = bookMapDAO.findBooksByAuthorTitle("","");

        assertTrue(listBooks.contains(actual));
        assertNull(bookMapDAO.findBooksByAuthorTitle("",""));
    	
        fail("IllegalArgumentException");
	
    }
	
	
	
    @Test(expected = IllegalArgumentException.class)
    public void testFindBooksByAuthorTitleIsNull() 
    {
        IBook actual = bookMapDAO.addBook(null, null, callNo1);

        verify(mockupBookHelper).makeBook(null, null, callNo1, 1);

        List<IBook> listBooks = bookMapDAO.findBooksByAuthorTitle(null, null);

        
        assertTrue(listBooks.contains(actual));
        assertNull(bookMapDAO.findBooksByAuthorTitle(null, null));
		
        fail("IllegalArgumentException");
	
    }
        

}
