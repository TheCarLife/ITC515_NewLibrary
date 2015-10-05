
package testing;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

import java.util.Calendar;
import java.util.Date;

import library.BorrowUC_CTL;
import library.daos.BookHelper;
import library.daos.BookDAO;
import library.daos.LoanHelper;
import library.daos.LoanMapDAO;
import library.daos.MemberHelper;
import library.daos.MemberMapDAO;
import library.interfaces.EBorrowState;
import library.interfaces.IBorrowUI;
import library.interfaces.daos.IBookDAO;
import library.interfaces.daos.ILoanDAO;
import library.interfaces.daos.IMemberDAO;
import library.interfaces.entities.IBook;
import library.interfaces.entities.ILoan;
import library.interfaces.entities.IMember;
import library.interfaces.hardware.ICardReader;
import library.interfaces.hardware.IDisplay;
import library.interfaces.hardware.IPrinter;
import library.interfaces.hardware.IScanner;

public class TestPath 
{
    BorrowUC_CTL ctl;
    
    IDisplay display;
    IBorrowUI ui;
    IPrinter print;
    IScanner scan;
    ICardReader reader;
    
    ILoanDAO loanDAO;
    IBookDAO bookDAO;
    IMemberDAO memberDAO;
  
    Date date, dueDate;
    Calendar calender;

    @Before
    public void setUp() throws Exception {
    	scan = mock(IScanner.class);
    	reader = mock(ICardReader.class);
        
        print = mock(IPrinter.class);
        display = mock(IDisplay.class);
        ui = mock(IBorrowUI.class);
        
        bookDAO = new BookDAO(new BookHelper());
        memberDAO = new MemberMapDAO(new MemberHelper());
        loanDAO = new LoanMapDAO(new LoanHelper());
                
        ctl = new BorrowUC_CTL(reader, scan, print, display, bookDAO, loanDAO, memberDAO);
        IBook[] book = new IBook[15];
		IMember[] member = new IMember[6];
		
		book[0]  = bookDAO.addBook("author1", "title1", "callNo1");
		book[1]  = bookDAO.addBook("author1", "title2", "callNo2");
		book[2]  = bookDAO.addBook("author5", "title3", "callNo3");
		book[3]  = bookDAO.addBook("author5", "title4", "callNo4");
		book[4]  = bookDAO.addBook("author5", "title5", "callNo5");
		book[5]  = bookDAO.addBook("author4", "title6", "callNo6");
		book[6]  = bookDAO.addBook("author3", "title7", "callNo7");
		book[7]  = bookDAO.addBook("author3", "title8", "callNo8");
		book[8]  = bookDAO.addBook("author4", "title9", "callNo9");
		book[9]  = bookDAO.addBook("author2", "title10", "callNo10");
		book[10] = bookDAO.addBook("author2", "title11", "callNo11");
		book[11] = bookDAO.addBook("author1", "title12", "callNo12");
		book[12] = bookDAO.addBook("author1", "title13", "callNo13");
		book[13] = bookDAO.addBook("author4", "title14", "callNo14");
		book[14] = bookDAO.addBook("author4", "title15", "callNo15");
		
		member[0] = memberDAO.addMember("fName0", "lName0", "0001", "email0");
		member[1] = memberDAO.addMember("fName1", "lName1", "0002", "email1");
		member[2] = memberDAO.addMember("fName2", "lName2", "0003", "email2");
		member[3] = memberDAO.addMember("fName3", "lName3", "0004", "email3");
		member[4] = memberDAO.addMember("fName4", "lName4", "0005", "email4");
		member[5] = memberDAO.addMember("fName5", "lName5", "0006", "email5");
		
		Calendar calendar = Calendar.getInstance();
		Date today = calendar.getTime();
						
		for (int i=0; i<1; i++) {
			ILoan loan = loanDAO.createLoan(member[3], book[i]);
			loanDAO.commitLoan(loan);
		}
		calendar.setTime(today);
		calendar.add(Calendar.DATE, ILoan.LOAN_PERIOD + 1);
		Date date = calendar.getTime();		
		loanDAO.updateOverDueStatus(date);
		
		member[2].addFine(10.0f);
		
		for (int i=1; i<5; i++) {
			ILoan loan = loanDAO.createLoan(member[2], book[i]);
			loanDAO.commitLoan(loan);
		}
    }


    
    
    @Test
    public void testCardSwipeddNoRestrition() 
    {
      
        ctl.setState(EBorrowState.INITIALIZED);
       
        ctl.cardSwiped(1);
        
        verify(reader).setEnabled(false);
        verify(scan).setEnabled(true);
        verify(ui).setState(EBorrowState.SCANNING_BOOKS);
        verify(ui).displayMemberDetails(1, "fn", "1");
        verify(ui).displayExistingLoan(any(String.class));    
        
        assertEquals(EBorrowState.SCANNING_BOOKS, ctl.getState());
    }
    
    
    
    @Test
    public void testCardSwipedRestricted() 
    {

        ctl.setState(EBorrowState.INITIALIZED);
        
        ctl.cardSwiped(3);
      
        verify(reader).setEnabled(false);
        verify(ui).setState(EBorrowState.BORROWING_RESTRICTED);
        verify(ui).displayMemberDetails(3, "fName2 lName2", "0003");
        verify(ui).displayOutstandingFineMessage(10.0f);
        verify(ui).displayOverFineLimitMessage(10.0f);  
        
        //asserts
        assertEquals(EBorrowState.BORROWING_RESTRICTED, ctl.getState());
    }

    
    
    @Test
    public void testCardSwipedReachedLoanLimit() 
    {
        
        ctl.setState(EBorrowState.INITIALIZED);
        
        
        ctl.cardSwiped(3);
        
        verify(reader).setEnabled(false);
        verify(ui).setState(EBorrowState.BORROWING_RESTRICTED);
        verify(ui).displayMemberDetails(3, "fn", "3");
        verify(ui).displayAtLoanLimitMessage();
        verify(ui).displayExistingLoan(any(String.class));
        
        assertEquals(EBorrowState.BORROWING_RESTRICTED, ctl.getState());
    }
    
    
    
    @Test
    public void testCardSwipedOverDueLoan() {
        
        ctl.setState(EBorrowState.INITIALIZED);
       
        ctl.cardSwiped(2);
      
        verify(reader).setEnabled(false);
        verify(ui).setState(EBorrowState.BORROWING_RESTRICTED);
        verify(ui).displayMemberDetails(2, "fn ln", "2");
        verify(ui).displayOverDueMessage();
        verify(ui).displayExistingLoan(any(String.class));
        
        assertEquals(EBorrowState.BORROWING_RESTRICTED, ctl.getState());
    }
    
}
