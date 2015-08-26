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
	
	private static int translateId(int original) {
		//if all men are odd, all females are even and we split them in 2 arrays
		// 1:m 2:w 3:m 4:w 5:m 6:w
		// men [1+1/2 = 1, 3+1/2 = 2, 5+1/2 = 3]
		// women [2+1 = 1.5, 4+1/2 = 2.5, 6+1/2 = 3.5] (the .5 is not important for integers)
		return (original+1)/2;
	}
	
	public static Pair[] GaleSharpley(int N, Person[] inp) {
		Integer[] husbands = new Integer[(N+1)];
        Integer[] wives = new Integer[(N+1)];

		Stack<Integer> freeMen = new Stack<>();
		
		//every odd number is a man
		for (int i = 1; i< inp.length; i=i+2) {
			freeMen.push(i);
		}
        
        while (!freeMen.empty()) {
            int manId = freeMen.pop();
            if (inp[manId].preferred.peek() != null) {
                int futureWifeId = inp[manId].preferred.pop();
                Person woman = inp[futureWifeId];
                // She isn't matched yet!
                if ( (husbands[ translateId(futureWifeId) ]) == null) {
                    wives[ translateId(manId) ] = futureWifeId;
                    husbands[ translateId(futureWifeId) ] = manId;
                } else {
                    int fianceeId = husbands[ translateId(futureWifeId) ];

                    if (woman.inversePreferred[fianceeId] > woman.inversePreferred[manId]) {
                        // Reject!
                        freeMen.push(manId);
                    } else {
                        // Break up the engagement!
                        wives[ translateId(fianceeId) ] = 0;
                        wives[ translateId(manId) ] = futureWifeId;
                        int otherMan = husbands[ translateId(futureWifeId) ];
                        freeMen.push(otherMan);
                        husbands[ translateId(futureWifeId) ] = manId;
                    }
                }
            }
        }
        
        
        Pair[] pairs = new Pair[N+1];
        Pair p;
        for (int i = 1; i < inp.length; i = i+2) {
        	p = new Pair();
        	p.husband = inp[i].name;
        	p.wife = inp[ wives[ translateId(i) ] ].name;
			pairs[ translateId(i) ] = p;
		}
        
        return pairs;
	}
	
    public static void main(String[] args) {
		try {
			String filepath = args[0];

			Person[] d = InputParser.parseInput(filepath);

			Pair[] result = GS.GaleSharpley(d.length/2, d);

			for (Pair pair : result) {
                if(pair == null) continue;
                System.out.println( pair.toString() );
            }
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
