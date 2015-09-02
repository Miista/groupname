package hacking;

import java.util.Stack;


/**
 * Created by Soren Palmund on 24-08-2015.
 * Additional work Ivan Naumovski & Martino Secchi
 */
public class GS {
	
	static class Pair {
		String husband, wife;

		@Override
		public String toString() {
			return husband+" -- "+wife;
		}
	}

    public static Pair[] GaleSharpley(Person[] people) {
        int[] matches = new int[ people.length ];

		Stack<Integer> freeMen = new Stack<>();
		
		//every odd number is a man
		for (int i = 1; i< people.length; i=i+2) {
			freeMen.push( i );
		}

        while (!freeMen.empty()) {
            int manId = freeMen.pop();
            if (people[manId].preferred.peek() != null) { // He has not exhausted his preferences
                int futureWifeId = people[manId].preferred.pop();
                Person woman = people[futureWifeId];

                if ( (matches[ futureWifeId ]) == 0) { // She isn't matched yet!
                    matches[ manId ] = futureWifeId;
                    matches[ futureWifeId ] = manId;
                } else {
                    int fianceeId = matches[ futureWifeId ];

                    if (woman.inversePreferred[fianceeId] > woman.inversePreferred[manId]) {
                        freeMen.push( manId ); // Reject!
                    } else {
                        // Break up the engagement!
                        matches[ fianceeId ] = 0; // Fiancee has no wife
                        freeMen.push( fianceeId );

                        // Sealing the deal with another man.
                        matches[ manId ] = futureWifeId; // Set the wife
                        matches[ futureWifeId ] = manId; // Set the husband
                    }
                }
            }
        }
        
        
        Pair[] pairs = new Pair[ people.length/2 ];
        Pair p;
        int n = 0;
        for (int i = 1; i < people.length; i = i+2) {
        	p = new Pair();
        	p.husband = people[i].name;
        	p.wife = people[ matches[ i ] ].name;
			pairs[ n++ ] = p;
		}
        
        return pairs;
	}
	
    public static void main(String[] args) {
		try {
			String filepath = args[0];

			Person[] d = InputParser.parseInput(filepath);

			Pair[] result = GS.GaleSharpley( d);

			for (Pair pair : result) {
                if(pair == null) continue;
                System.out.println( pair.toString() );
            }
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
