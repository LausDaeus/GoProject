

public class Main{
	//Use this class to test your stuff. Just comment out or delete your code before you commit it.
	public static void main(String[] args){
		
		// Test of FileIO boardWrite()

		Board a = new Board(2,2);
		a.set(0,0,0);
		a.set(0,1,1);
		a.set(1,0,2);
		a.set(1,1,1);

		Board b = new Board(2,2);
		b.set(0,0,1);
		b.set(0,1,2);
		b.set(1,0,1);
		b.set(1,1,0);

		FileIO.writeBoard(a);
		FileIO.writeBoard(b, "saveData/boards/specialBoard"); 

		Board c = FileIO.readBoard();
		FileIO.readBoard("saveData/boards/BoardExampleFormat");

		System.out.println("Board top left "+c.get(0,0));

		//System.out.println(System.getProperty("os.name"));

		//line

		

	}
}


		/* Test of Board.clone()

		import java.util.Arrays;

		Board a = new Board(2,2);
		a.set(0,0,0);
		a.set(0,1,1);
		a.set(1,0,2);
		a.set(1,1,3);

		Board b = a.clone();
		b.set(0,0,3);
		b.set(0,1,2);
		b.set(1,0,1);
		b.set(1,1,0);

		System.out.println("A:");
		System.out.println(a.get(0,0));
		System.out.println(a.get(0,1));
		System.out.println(a.get(1,0));
		System.out.println(a.get(1,1));

		System.out.println("B:");
		System.out.println(b.get(0,0));
		System.out.println(b.get(0,1));
		System.out.println(b.get(1,0));
		System.out.println(b.get(1,1));
		*/